/*
   The OPEN-CLOSED PRINCIPLE (OCP) is one of the most famous rules in SWE--it is
   the "O" in the SOLID principles of Object-Oriented Design.

   It is absolutely not just a pedantic convention; it exists to solve massive,
   real-world headaches on large codebases.

   Let's break it down exactly as you asked: first defining the principle, then
   working backwards to see why the `enum`/`switch` approach violates it, and
   finally looking at the concrete architectural advantages.


---
1. WHAT IS THE OPEN-CLOSED PRINCIPLE?
   The principle states:
      "Software entities (classes, modules, functions) should be OPEN FOR
      EXTENSION, but CLOSED FOR MODIFICATION."

   - OPEN FOR EXTENSION: As the requirements of your app grow, you should be
     able to make the software do new things (like adding a brand-new type of
     game world).
   - CLOSED FOR MODIFICATION: You should be able to achieve this extension
     WITHOUT TOUCHING THE EXISTING, ALREADY-WORKING, ALREADY-TESTED SOURCE CODE.


   THE HARDWARE ANALOGY: Think of a computer motherboard's USB port.
      If you want to add a new peripheral (a webcam, a weird custom joystick),
      the computer is open for extension--you just plug it in. But the computer
      is closed for modification--you don't have to open the PC case, take out
      a soldering iron, and physically wire the new device directly to the
      motherboard's circuity to make it work.


---
2. WORKING BACKWARDS: WHY THE `enum`/`when` VIOLATES OCP
   Let's look at the "Enum Anti-Pattern" used in the first attempt at `GridWorld`:
```Kotlin
enum class WorldKind { BOUNDED, DEADLY, RANDOM }

class GridWorld(val kind: WorldKind) {
    fun updatePosition(newPos: Pair<Int, Int>) {
        // ... if player walks off the edge:
        when (kind) {
            WorldKind.BOUNDED -> doNothing()
            WorldKind.DEADLY  -> die()
            WorldKind.RANDOM  -> teleport()
        }
    }
}
```
   Imagine this code is written, fully tested, and shipped to production.

   Now, your boss comes to you and says: "We need a new world type: a `TORUS`
   world that wraps around like Pac-Man."

   To implement this, what files do you have to open and edit?
   1. You must open the file with the `enum` and modify it to add `TORUS`.
   2. You must scroll down into the core `GridWorld` class, find the `when`
      statement, and MODIFY it to inject a brand-new
      `WorldKind.TORUS -> wrapAround()` branch.

   THE VIOLATION: To extend the game with a new feature, you were forced to
   modify the core `GridWorld` class. Therefore, the class is not closed for
   modification. If you add 50 new world types over the next year, you will
   have to take a soldering iron to that exact same function 50 times.


   ---
3. HOW ABSTRACT CLASSES FIX THIS (Achieving OCP)
   To follow OCP, we use polymorphism (the equivalent of C++ pure virtual
   functions) to invert the design. We extract the "what happens when you fall
   off the edge" logic into an `abstract` method (our "USB port"):

```Kotlin
abstract class GridWorld {
    fun updatePosition(newPos: Pair<Int, Int>) {
        // ... if player walks off the edge:
        position = handleOverrun(newPos)        // Polymorphic call!
    }

    // "I don't know what will happen, the subclass will tell me."
    protected abstract fun handleOverrun(newPos: Pair<Int, Int>): Pair<Int, Int>
}
```
   Now, the boss asks for the `TORUS` world. What do you do?

   You DO NOT touch `GridWorld.kt`. You leave that file completely alone.
   Instead, you create a BRAND-NEW FILE, `TorusGridWorld.kt`:

```Kotlin
class TorusGridWorld : GridWorld() {
    override fun handleOverrun(newPos: Pair<Int, Int>): Pair<Int, Int> {
        // Pac-man wrap-around math goes here
        return wrappedPosition
    }
}
```

   THE COMPLIANCE: You successfully extended the game with a massive new feature
   , but `GridWorld` remained entirely closed for modification.


   ---
4. WHY IS THIS SO IMPORTANT? (THE ADVANTAGES)
   Coming from a C++ background, you will immediately recognise the engineering
   pain points that OCP prevents:

   A. PREVENTING REGRESSION BUGS (Safety)
      Every time you open an existing, working core class to add a new `switch`
      branch, you risk accidentally breaking the existing logic (e.g.,
      mistyping a variable, accidentally deleting a `break`/`return`, or messing
      up the scope). By putting `TorusGridWorld` in its own isolated file, it is
      mathematically impossible for your new code to break the old, tested
      `DeadlyGridWorld` code.


   B. COMPILE TIMES (THE C++ PARALLEL)
      In C++, if you add a new value to an `enum` in a core header file
      (`GridWorld.h`), every single `.cpp` file in your project that `#include`s
      it must be recompiled. In a massive codebase, changing one enum could
      trigger a 45-minute compile. If you use OCP and just add a new
      `TorusWorld.cpp` file, only that single new file compiles. (Kotlin
      compilation is faster, but the dependency principle is the same).


   C. TEAM COLLABORATION (NO MERGE CONFLICTS)
      If you are adding `Torus` logic, your coworker is adding `Bouncy` logic,
      and a third is adding `Sticky` logic, using the `enum`/`when` approach
      means all three of you are editing the exact same 155 lines in
      `GridWorld.kt`. When you push to Git, you will have a nightmare of merge
      conflicts. With the OCP approach, you are all working in three separate
      subclass files. Zero merge conflicts.


   D. AVOIDING "God Classes"
      If you use a `switch` statement, `GridWorld` has to contain the logic and
      dependencies for every single world type ever created. It becomes a
      5,000-line "God Class" that knows everything about everything about
      everything. With OCP, `GridWorld` only knows about basic movement, and the
      specific rules are cleanly delegated to small, focused files.


   E. THIRD-PARTY PLUGINS / LIBRARIES
      Imagine you compile this game engine into a library (`.dll` or `.jar`) and
      sell it. If you used the `enum` approach, a customer CANNOT add a custom
      world type because they cannot edit your compiled source code to add to
      the `enum`. If you use the `abstract` approach, the customer can easily
      write `class MyCustomWorld : GridWorld()` in their own project. OCP IS THE
      FUNDAMENTAL MECHANISM BEHIND ALL PLUGIN ARCHITECTURES.







                              /
                   __       //
                   -\= \=\ //
                 --=_\=---//=--
               -_==/  \/ //\/--
                ==/   /O   O\==--
   _ _ _ _     /_/    \  ]  /--
  /\ ( (- \    /       ] ] ]==-
 (\ _\_\_\-\__/     \  (,_,)--
(\_/                 \     \-
\/      /       (   ( \  ] /)
/      (         \   \_ \./ )
(       \         \      )  \
(       /\_ _ _ _ /---/ /\_  \
 \     / \     / ____/ /   \  \
  (   /   )   / /  /__ )   (  )
  (  )   / __/ '---`       / /
  \  /   \ \             _/ /
  ] ]     )_\_         /__\/
  /_\     ]___\
 (___)





   This section tackles a classic architectural problem: How do you write logic
   that handles multiple diffeent behaviors without writing massive, unmaintable#
   `switch` statements?

   For a C++ developer, you will immediately recognise the progression from an
   `enum`/`switch` to a virtual method, and finally to a pure virtual function.


---
1. The "Enum Anti-Pattern" (Slides 35–46)
   Imagine `GridWorld` can have different behaviors when the player walks off
   the edge:
      - BOUNDED: Hitting the edge does nothing (blocks movement).
      - DEADLY: Hitting the edge throws an exception (kills the player).
      - RANDOM: Hitting the edge teleports the player randomly.

   A novice approach is to pass an `enum` into the `GridWorld` class and use a
   `when` statement (Kotlin's `switch`):
```Kotlin
enum class WorldKind { BOUNDED, DEADLY, RANDOM }

class GridWorld() {
    private fun updatePosition(newPosition: Pair<Int, Int>) {
        // ... if off grid:
        when (worldKind) {
            WorldKind.BOUNDED -> return     // Do nothing
            WorldKind.DEADLY  -> throw DeadPlayerException("Fell off world!")
            WorldKind.RANDOM  -> position = randomPosition()
        }
    }
}
```
   THE FATAL FLAW: What if you want to add a `TORUS` world (wrapping around the
   edges like Pac-Man)? You are forced to open up the core `GridWorld` class,
   add to the enum, and add a branch to the `when` statement. This violates the
   OPEN-CLOSED PRINCIPLE (classes should be open for extension, but closed for
   modifcation).


2. THE FIRST INHERITANCE ATTEMPT (S 47-54)
   To make it extensible, we drop the enum, mark `GridWorld` as `open`, and
   delegate the specific logic to subclasses via an `open` method. Because the
   base class doesn't know what to do, it provides a "dummy" implementation.

```Kotlin
open class GridWorld(...) {
    // Virtual function with a "dummy"
    protected open fun handleOverrun(newPosition: Pair<Int, Int>):
        Pair<Int, Int> = throw NotImplementedError("This method should be provided by subclasses")
}
```
   Subclasses like `BoundedGridWorld` then inherit this and `override` the
   method to provide their specific behavior.

      KOTLIN TIP: NARROWING TO `Nothing` (s 53)
         When overriding `handleOverrun` in `DeadlyGridWorld`, its only job is
         to unconditionally throw an exception. Because it never successfully
         returns, Kotlin allows you to "narrow" the return type from
         `Pair<Int, Int>` to `Nothing`--a special Kotlin type meaning
         "this function never completes normally," conceptually similar to
         `[[noreturn]]` in C++.


3. THE THREE FLAWS OF "DUMMY" METHODS (S 55-57)
   Using a standard `open` base class with a dummy method throwing an exception
   has three glaring issues:

   1. INSTANTIATION: Nothing stops a client from creating a "plain"
      `GridWorld(10, 10)`, which doesn't make logical sense and will crash if
      they hit the edge.
   2. MISSING OVERRIDES: If you create `TorusGridWorld` but forget to `override`
      the method, the compiler won't warn you. It just quietly inherits the
      dummy method and crashes at runtime.
   3. ACCIDENTAL SUPER CALLS: A subclass might accidentally call
      `super.handleOverrun()`, immediately triggering the dummy exception crash.


4. THE ULTIMATE SOLUTION: `abstract` Classes (S 58-66) [CORE CONCEPT]
   To fix all three flaws instantly, Kotlin provides the `abstract` keyword
   (identical to making a C++ class abstract via pure virtual functions).

   An `abstract class` automatically implies `open`. An `abstract fun` has no
   body and forces all concrete subclasses to provide an implementation.
```Kotlin
abstract clas GridWorld(
    protected val width: Int,
    protected val height: Int
) {
    // Concrete methods and state can still exist in abstract classes!
    private var position: Pair<Int, Int> = randomPosition()

    private fun updatePosition(newPosition: Pair<Int, Int>) {
        // ... calls the abstract method polymorphically
        position = handleOverrun(newPosition)
    }

    // Equivalent to C++ `= 0`. No body allowed!
    protected abstract fun handleOverrun(newPosition: Pair<Int, Int>):
        Pair<Int, Int>
}
```


   HOW `abstract` COMPLETELY SOLVES THE 3 FLAWS:
   1. INSTANTIATION SOLVED: `val world = GridWorld(10, 10)` throws a compile
      error: Cannot create an instance of an abstract class.
   2. MISSING OVERRIDES SOLVED: If `TorusGridWorld` forgets to override the
      method, the compiler immediately throws an error: Class is not abstract
      and does not implement abstract base class member.
   3. ACCIDENTAL CALLS SOLVED: Calling `super.handleOverrun()` throws a compile
      error because the method physically has no body to call.


---
SUMMARY OF T2L7
   You have now learned how to model clean class hierarchies using:
   1. `open` vs `final`: Kotlin strictly requires permission to inherit or
      override.
   2. `protected` STATE: Sharing data down the inheritance tree without exposing
      it publicly (and splitting visibility with `private set`).
   3. ABSTRACT CLASSES: The C++ pure virtual equivalent. They provide concrete
      base logic but force classes to implement specific, missing pieces of the
      puzzle via `abstract fun`.
   4. THE `Nothing` TYPE: Used for functions that guarantee they will never
      return normally.







LECTURE 8: CONCURRENCY.
   Given your deep background in C++ and CUDA, you already know the low-level
   theory of concurrency, parallelism, data races, and memory architecture
   inside out. We will skip the basic OS definitions and focus strictly on
   how Kotlin handles these concepts under the hood and in its syntax.


PART 1 (S 1-24): CONCURRENCY BASICS AND KOTLIN THREADING

1. CONCURRENCY vs. PARALLELISM (S 1-8 & 15-16)
   Just to align terminology:
   - CONCURRENCY (Logical): Structuring a program as multiple independently
     executing tasks. They might run on a single core via rapid OS
     context-switching.
   - PARALLELISM (Physical): Actually executing tasks simultaneously on multiple
     physical CPU cores (like your CUDA kernels).
   - Fun Math Fact from the slides: When doing parallel reductions (like
     splitting a dot product across threads), the parallel version might give
     slightly different results thant the sequential versions because FP
     arithmetic is not perfectly associative.


2. LAUNCHING THREADS IN KOTLIN (S 9-14)
   Kotlin does not have its own underlying raw threading primitives; it
   piggybacks entirely on the JAVA VIRTUAL MACHINE (JVM) threading system.

   To create an OS-level thread, Kotlin uses the Java `Runnable` interface and
   `Thread` class. It behaves exactly like passing a functor or lambda to
   `std::thread`.


THE SYNTAX:
```Kotlin
// 1. Implement the Runnable interface (the work to be done)
class MyFriend : Runnable {
    override fun run() {
        println("Hello from another thread!")
    }
}

fun main() {
    // 2. Pass the Runnable instance into a Thread object
    val myFirstThread = Thread(myFriend())

    // 3. Start and Join (exactly like C++)
    myFirstThread.start()           // Spawns the OS thread and schedules it
    myFirstThread.join()            // Bocks the main thread until this one finishes
}
```
   (Note: In modern Kotlin production code, you will rarely use raw `Thread`
   classes like this--you will use COROUTINES, which are lightweight user-space
   threads. However, universities teach the fundamental JVM OS-thread primitives
   first).


3. SHARED MEMORY AND THE JVM (S 17-19)
   The memory model works exactly as you'd expect from C++:





















* */


/*
            - pedantic = excessively concerned with minor details or rules;
              overscrupulous.

            - In Kotlin, abstract classes are classes that can't be instantiated
              directly. They are designed to be inherited by other classes
              which define their actual behavior. This behavior is called
              implementation.
                 However, abstract classes can contain data in attributes!

            - A Kotlin interface can have properties--but note that it can't
              maintain state. This means that the properties cannot store data.
              This is in contrast to abstract classes, which can maintain state.

            - A pure virtual function or pure virtual method is a virtual
              function that is required to be implemented by a derived class if
              the derived class is not abstract. Classes containing pure virtual
              methods are termed `abstract` and they cannot be instantiated
              directly.

            - In Kotlin, the fundamental difference of `abstract` and `open`
              lies in INSTANTIATION and INTENT. An `open` class is a fully
              functional, "complete" class that can be instantiated on its own,
              but it has been explicitly given permission to be inherited from.
              By default, all Kotlin classes are `final`, meaning they are
              closed to subclassing; marking one as `open` simply unlocks
              that door. You would use an `open` class when you have a working
              base model that provides a default implementation, but you want to
              allow child classes to override specific behaviors if they chose.

              An `abstract` class, conversely, is an "incomplete" blueprint that
              CANNOT be instantiated. Its primary purpose is to define a common
              template for a family of subclasses. Abstract classes can contain
              `abstract` properties or functions that have no body (pure virtual
              functions), forcing any non-abstract subclass to provide the
              specific implementation. While an `open` class allows for
              extension, an `abstract` class requires it to be of any use. Use
              `abstract` when you have a high-level concept (like `Shape`) that
              shouldn't exist on its own but provides the essential structure
              for specific types (like `Circle` or `Square`).
* */










/*



* */