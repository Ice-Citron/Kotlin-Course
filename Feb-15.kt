import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write



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
   - THE STACK: Every thread gets its own private call stack. Local variables
     declared inside a function are inherently thread-safe.
   - THE HEAP: All threads share the same Heap. In Kotlin, ALL CLASS INSTANCES
     (OBJECTS) live on the Heap. Therefore, if multiple threads have a reference
     to the same object, its properties are subject to concurrent modification.


4. RACE CONDITIONS vs. DATA RACES (S 20-24)
   The lecture draws a strict academic distinction between these two terms:
   - RACE CONDITION (LOGIC LEVEL): A flaw (or feature) in system behavior where
     the output depends on the unpredictable sequence/timing of threads.
      - Example: Two people trying to buy the last Taylor Swift concert ticket
        online. It's nondeterministic who gets it, but the system functions
        logically. Not all race conditions are bugs!
   - DATA RACE (MEMORY LEVEL): The low-level disaster. Two threads access the
     exact same memory location simultaneously, at least one is write, and there
     is no synchronisation. ALWAYS A BUG.#


   THE CLASSIC EXAMPLE:
```Kotlin
var value = 0

// This is NOT atomic. Under the hood, it's a Read-Modify-Write.
fun inc() {
    value++
}
```

   If two threads call `inc()` simultaneously on the same shared object, they
   will both read `0`, both compute `1`, and both write `1` back to RAM. You
   lose an increment because the operations interleave.



PART 2 (S 25-48): MUTUAL EXCLUSION, LOCKS and DEADLOCK AVOIDANCE

   This section maps directly to C++ concepts like `std::mutex`,
   `std::recursive_mutex`, and `std::lock_guard`. Because Kotlin runs on the JVM
   , it handles these synchronisation primitives slightly differently, relying
   on higher-order functions rather than RAII.


1. MUTUAL EXCLUSION AND CRITICAL SECTIONS (S 25-33)
   To solve the data race from P1 (the lost increment), we need MUTUAL EXCLUSION
   . A block of code that accesses shared, mutable state is called a CRITICAL
   SECTION. A robust solution guarantees three things:
      1. MUTUAL EXCLUSION: At most one threads enters the critical section at a
         time.
      2. FREEDOM FROM DEADLOCK: If threads are trying to enter, at least one
         will eventually succeed.
      3. FREEDOM FROM STARVATION: Every thread trying to enter will eventually
         succeed.


   LOCKS IN KOTLIN:
   Kotlin doesn't have a native lock primitive; it imports the highly optimised
   `Lock` interface from Java's `java.util.concurrent.locks` package.

```Kotlin
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class Counter {
    // 1. Create the mutex
    private val lock: Lock = ReentrantLock()
    var value = 0
        private set

    // The unsafe, manual way
    fun inc(): Int {
        lock.lock()         // 2. Acquire
        val result = value
        value++             // Critical Section
        lock.unlock()       // 3. Release
    }
}
```




2. LOCK HYGIENE AND `withLock` (S34-38) [CRUCIAL KOTLIN IDIOM]

   In C++, you are taught never to manually call `.lock()` and `.unlock()`. If
   an exception is throw in the middle of the critical section, or if you
   `return` early, the `.unlock()` is bypassed, and the mutex is permanently
   locked, freezing your program. In C++, you solve this using RAII
   (`std::lock_guard`).

   Kotlin does not have destructors for RAII, but it solves this problem
   beautifully using higher-order functions (lambdas). The idiomatic way to
   handle locks in Kotlin is the `withLock` extension function.

```Kotlin
import kotlin.concurrent.withLock

class ThreadSafeCounter {
    private val lock: Lock = ReentrantLock()
    var value = 0

    fun inc(): Int {
        // `withLock` automatically acquires the lock, runs the lambda,
        // and guarantees the lock is released in a `finally` block under the hood
        // even if an exception is thrown or you return early!
        lock.withLock {
            val result = value
            value++
            return result
        }
    }
}
```
   Rule of thumb: Always wrap your critical sections in `lock.withLock { ... }`










   I completely understand. Concurrency is notoriously one of the most difficult
   topics in computer science because human brains are wired to think
   sequentially (step 1, then step 2, then step 3). When you introduce
   concurrency, the code on your screen no longer executes from top to bottom in
   a predictable ORDER.

   Let's erase any assumptions... We are going to build your mental model from
   the absolute ground up, focusing on exactly WHAT the computer hardware is
   doing, WHY we need these tools, and the trade-offs of using them.


   ---
   1. CONCURRENCY vs. PARALLELISM (The Philosophy)
      People use these words interchangeably, but in computer architecture, they
      describe two fundamentally different things.

      - CONCURRENCY IS ABOUT STRUCTURE (Dealing with many things at once).
           It means debugging your program so that multiple tasks are in
           progress at the same time, even if they aren't physically happening
           at the exact same nanosecond.
      - PARALLELISM IS ABOUT EXECUTION (Doing many things at once).
           It means physically executing two tasks at the exact same time on
           different hardware.

   THE KITCHEN ANALOGY:
      Imagine you are cooking dinner (you are the CPU core).
      - SEQUENTIAL (No Concurrency): You chop onions... then wait 10 mins for
        water to boil... then you cook pasta... You do absolutely nothing else
        while waiting.
      - CONCURRENCY: You put water on the stove. While waiting for it to boil,
        you pivot and chop onions. You rapidly switch back and forth. Only one
        physical task is being worked on at any given second, but multiple
        tasks are progressing. The OS is the manager that rapidly switches the
        CPU between tasks so fast (thousands of times a second) that it feels
        simultaneous.
      - PARALLELISM: You hire a second chef. You chop the onions while the other
        chef boils the pasta. Two physical actions are happening at the exact
        same nanosecond. (This requires a multi-core CPU)


   When and Why to use them:
   - USE CONCURRENCY FOR "WAITING" (I/O Bound tasks): If your app needs to
     download a file from the internet, the network takes time. If your code is
     sequential, the app's User Interface (UI) freezes while the CPU sits idle
     waiting for the Wi-Fi chip. By putting the download on a background
     concurrent task, the OS switches back to the UI, allowing the user to keep
     scrolling while the file downloads in the background.
   - USE PARALLELISM FOR "MATH" (CPU BOUND tasks): If you are applying a blur
     filter to a 10-megapixel photo, splitting the image into 4 pieces and
     handing them to 4 physical CPU cores will finish the math 4 times faster.


   WHEN NOT TO USE THEM:
   - If a task is simple and fast (e.g., adding 1,000 numbers), DO NOT USE THEM.
     Setting up concurrent tasks requires the OS to allocate memory and manage
     schedules. The overhead of "hiring a new chef" takes longer than just doing
     the simple math yourself sequentially.


---
2. PROCESSES vs. THREADS (The Architecture)
   When you tell the OS to run a program, it gives you containers to run your
   code in.

   A PROCESS is a heavy, isolated container. When you open Google Chrome, you
   spawn a process. When you open Spotify, that's another process.
      - THE RULE: Processes do NOT share memory (RAM). Spotify cannot read the
        RAM that Chrome is using. This is great for security and stability--if
        Chrome crashes, Spotify keeps playing music.

   A THREAD is a lightweight worker inside a process. A single process can have
   dozens of threads running around inside it.
   - THE RULE: Threads DO share memory. If your app spawns Thread A and Thread B
     , both threads have access to the exact same pool of RAM.


THE MEMORY SPLIT:
   Because threads live in the same house, they handle memory in two distinct
   ways:

   1. THE STACK (Private): Every thread gets its own private backpack. Any
      variable you create inside a local function goes in the backpack. No other
      thread can see or touch it. THIS IS 100% SAFE.
   2. THE HEAP (Shared): ... ALL OBJECTS live on the Heap. Any thread can walk
      up to an object and alter it. THIS IS HIGHLY DANGEROUS.



WHEN and WHY to use Threads:
   You use threads when you have multiple tasks that need to work together and
   SHARE DATA instantly. Passing data between two entirely different Processes
   is slow and complicated; threads can just look at the exact same variable
   in memory.



---
3. THE DANGER: SHARED MEMORY AND THE DATA RACE
   Because threads share the Heap, we run into the most terrifying bug in
   programming: the DATA RACE.

   Let's look at how computer architecture actually handles a simple line of
   code: `score = score + 1`

   The CPU cannot do math directly inside the RAM sticks. It must perform three
   distinct hardware steps:
      1. READ: Fetch the current `score` from RAM into the CPU's internal
         scratchpad (a register).
      2. MODIFY: The CPU adds 1.
      3. WRITE: Send the new number from the CPU back to RAM.


   THE DISASTER SCENARIO:
      Imagine two threads are running on two different CPU cores. The `score` is
      currently `0`.
         1. ...

         ...

      THE RESULT: Two threads successfully added a point, but the final score is
      `1`. You just permanently lost daa. This happens invisibly at the silicon
      level, and it is a nightmare to debug, because it might only happen 1 out
      of 10,000 times depending on how the OS times the pauses.



 ---
 4. THE SOLUTION: LOCKS (Mutual Exclusion)
    To fix the Data Race, we use a LOCK (also called a Mutex - Mutual Exclusion).

    Think of a Lock like the physical key to a single-occupancy bathroom at a
    coffee shop.
       - If Thread A wants to modify the `score` (the Critical Section), it must
         take the key.
       - While Thread A has the key, it does the 3-step hardware math safely.
       - If Thread B arrives and wants the key, it sees the key is gone. Thread
         B must WAIT IN LINE (its thread is put to sleep by the OS). It uses
         zero CPU cycles while waiting.
       - When Thread A finishes, it returns the key. The OS wakes Thread B up
         and hands it the key.

    By using a lock, we force the 3 hardware steps (Read, Modify, Write) to
    become ATOMIC (indivisible).



WHEN AND WHY TO USE LOCKS:
   You MUST use a lock whenever multiple threads need to read and write to the
   exact same shared data on the Heap. It is the only way to guarantee the data
   isn't corrupted.


WHEN NOT TO USE LOCKS (The Trade-offs):
   1. READ-ONLY (Immutable) DATA: If you have a list of configuration settings
      that is created once at startup and never changes, DO NOT USE A LOCK.
      1,000 threads can read it at the exact same time safely. Hardware
      conflicts only happen if someone is writing.

   2. DESTROYING PARALLELISM: If you have an 8-core CPU, but 8 threads are all
      waiting in line for the same bathroom key, 7 of your CPU cores are sitting
      idle doing nothing. You have downgraded your computer to a single-core
      machine.
         RULE OF THUMB: Keep locked sections of code as tiny and fast as
         humanly possible.



5. THE DANGER OF LOCKS: DEADLOCK
   If locks solve Data Races, why not just put locks everywhere to be safe?
   Enter the DEADLOCK.

   Deadlock happens when two or more threads are waiting for each other to
   release a lock, and therefore both wait forever. Your program permanently
   freezes.


THE SCENARIO:
   Alice wants to transfer ...



HOW TO AVOID IT (GLOBAL ORDERING):
   The mathematical way to avoid deadlocks is to enforce a GLOBAL LOCK ORDER.

   You make a strict, unbreakable rule in your system architecture: If a thread
   needs multiple locks, it must always acquire them in numerical (or
   alphabetical) order.

   If we enforce numerical ordering (Alice is Account #1, Bob is Account #2):
      - THREAD 1 needs Alice and Bob. Alice is the lower number, so it grabs
        Alice's lock.
      - THREAD 2 needs Bob and Alice. Alice is the lower number, so it tries to
        grab Alice's lock.
      - Because Thread 1 already has Alice's lock, Thread 2 is forced to go to
        sleep immediately.
      - Thread 1 safely grabs Bob's lock, finishes the transfer, and releases
        both.
      - Thread 2 wakes up, grabs Alice, grabs Bob, and finishes its transfer.

   By agreeing on a global sorting order, a Deadlock is mathematically
   impossible!


---
SUMMARY CHECKLIST FOR SYSTEM DESIGN
   When you are architecting a program, ask yourself:
   1. DO I NEED TO DO SOMETHING IN THE BACKGROUND WITHOUT FREEZING THE UI?
      --> Use Concurrency (Threads)
   2. ARE THESE THREADS LOOKING AT THE EXACT SAME VARIABLES IN MEMORY?
      --> If yes, you are sharing the Heap.
   3. IS ANYONE ALTERING (WRITING) THAT SHARED MEMORY?
      --> If yes, you MUST use a Lock.
   4. ARE MY THREADS TAKING MULTIPLE LOCKS AT THE SAME TIME?
      --> Sort them numerically before locking to avoid a Deadlock.























---
            Think of a SEMAPHORE as a high-tech bouncer for a club with a
            strictly limited capacity. Unlike a standard lock (or Mutex) that
            only allow one person in at a time, a semaphore maintains a set
            number of "permits." When a thread wants to access a shared
            resource, it must first acquire a permit; if all permits are
            currently taken, the thread has to wait in line until someone else
            finishes their task and releases one. This makes semaphores perfect
            for managing THROTTLING, such as limiting the number of simultaneous
            database connections or concurrent API calls to prevent a system
            crash.

            There are two main types: COUNTING SEMAPHORES and BINARY SEMAPHORES.
            A counting semaphore can have any number of permits (e.g., 5 permits
            for 5 available printer slots), whilst a binary semaphore has only
            one, acting very similarly to a traditional lock. The key
            distinction is that semaphores are about capacity management rather
            than ownership. In many implementations, any thread can release a
            permit, whereas, with a Mutex, only the thread  that locked the door
            is allowed to unlock it.


            ---
            KOTLIN EXAMPLE: Throttling Coroutines
               In Kotlin, we typically use the `Semaphore` class from the
               `kotlinx.coroutines` library to manage concurrency within
               asynchronous code.
```Kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

val semaphore = Semaphore(permit = 3)       // Only 3 tasks can run at once

fun main() = runBlocking {
    repeat(10) { id ->
        launch {
            // withPermit automatically acquires and releases
            semaphore.withPermit {
                println("Task $id is processing in the club...")
                delay(1000)         // Simulating work
                println("Task $id is leaving.")
            }
        }
    }
}
```
   QUICK COMPARISON

   // Feature // Mutex // Semaphore //
   CAPACITY // Exactly 1 // $N$ (User defined)
   OWNERSHIP // Thread that locks must unlock // Any thread can usually release
   PRIMARY USE // Mutual Exclusion (Safety) // Resource Throttling (Flow)
























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



            - RAII = The principle that objects own resources is also known as
              "resource acquisition is initialisation," or RAII. When a
              resource-owning stack object goes out of scope, its destructor is
              automatically invoked. In this way, Garbage Collection in C++
              is closely related to object lifetime, and is deterministic.
* */










/*


   ... architecture-first understanding of why concurrency is dangerous (shared
   memory and data races) and how we fix it theoretically (mutual exclusion),
   let's re-examine exactly how Kotlin implements these concepts in your code.

   Here is the from-scratch re-explanation of
      PART 2 (S25-48): WRITING SAFE CONCURRENCY IN KOTLIN.


---
1. THE CRITICAL SECTION && THE `Lock` (S 25-33)
   As we established, a DATA RACE happens when multiple threads try to read,
   modify, and write to the same shared memory location at the exact same time.
   The specific lines of your code that actually touch this shared memory are
   called the CRITICAL SECTION (the "danger zone").

   To protect the critical section, we need MUTUAL EXCLUSION--a physical
   mechanism that ensures only one thread can be inside that section of code at
   any given time.


HOW KOTLIN DOES IT:
   Kotlin uses an object called a `Lock`. You can think of this object as the
   single physical key to a room.

```Kotlin
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class Counter {
    // 1. We create a single physical "key" that all threads must share.
    private val myLock: Lock = ReentrantLock()

    val score = 0
        private set

    fun addPoint(): Int {
        // 2. A thread arrives and asks for the key.
        // If another thread is currently holding it, the OS puts this thread to
        // sleep right here.
        myLock.lock()

        // --- CRITICAL SECTION START ---
        val result = score
        score = result + 1
        // --- CRITICAL SECTION END

        // 3. The thread is finished and hands the key back to the OS.
        myLock.unlock()

        return result
    }
}
```
    - WHEN TO USE A LOCK: Whenever multiple threads need to write (modify) the
      exact same variable on the Heap.
    - When NOT to use a Lock: If the data is read-only (like a list of
      configuration settings loaded at startup). If data never changes, millions
      of threads can read it simultaneously with zero risk. Locks force threads
      to wait in line, which destroys parallelism, so only use them when
      mutation is actually happening.



---
2. LOCK HYGIENE AND THE `withLock` SAVIOR (S34-38)
   The code above looks logical, but in the real world of SWE, IT IS A TICKING
   TIME BOMB.

THE DANGER (Human Error):
   What happens if the code inside your critical section crashes? Imagine you
   are writing to a file, but the hard drive is full, so the system throws an
   Error and panics.

   Because the key was never returned to the system, every other thread waiting
   in line for that key will sleep until the end of time. Your application is
   permanently, irreversibly frozen.



THE KOTLIN SOLUTION (`withLock`):
   To prevent this, you should practically never write `.lock()` and `.unlock()`
   manually. Kotlin provides a brilliant, automated "smart-door" called
   `withLock`,

```Kotlin
import kotlin.concurrent.withLock

class SafeCounter {
    private val myLock: Lock = ReentrantLock()
    var score = 0

    fun addPoint(): Int {
        // withLock automatically takes the key, runs your block of code,
        // and absolutely GUARANTEES it returns the key when finished.
        myLock.withLock {
            val result = score
            score = result + 1

            return result       // it is perfectly safe to return early!
        } // Even if the code crashes, the door unlocks itself right here.
    }
}
```


    - WHEN TO USE IT: 100% of the time. Wrap every single critical section in a
      `withLock { ... }` block to guarantee your app won't permanently freeze
      due to an unexpected error.



---
3. WHY `ReentrantLock`? Preventing Self-Sabotage (S39-44)
   Notice that when we created our lock, we specifically chose a `ReentrantLock`
   . What does "Reentrant" mean, and why is it so important?


THE PROBLEM WITH "DUMB" LOCKS:
   Imagine you write a Thread-Safe List class. You want to lock the list
   whenever you add an item.

```Kotlin
class ThreadSafeList {
    private val myLock = ReentrantLock()

    // Method A
    fun add(elements: String) {
        myLock.withLock { /. Add to the end of the list ./ }
    }

    // Method B
    fun addAtEndIfFull(index: Int, element: String) {
        myLock.withLock { // 1. Thread takes the lock here.
            if (index == size) {
                // 2. we are already inside the locked room!
                // Now we call Method A, which ALSO asks for the lock!
                add(element)
            }
        }
    }
}
```

   If this were a standard "dumb" lock, the thread would enter Method B, take
   the key, and then call Method A. Method A asks for the key. The lock would
   say: "Sorry, the key is currently taken." The thread would go to sleep,
   waiting for the key to be returned... BUT THE THREAD ITSELF IS THE ONE
   HOLDING THE KEY! It has deadlocked itself.



THE SOLUTION:
   A `ReentrantLock` is smart. It remembers which specific thread currently owns
   it. When Method A asks for the lock, the ReentrantLock checks the thread's ID
   , realises "Oh, you're the exact same thread that already owns this," and
   lets it pass right through. It simply keeps a tally (e.g., "This thread has
   entered 2 locked doors"). As long as the thread leaves 2 doors, the lock
   is eventually freed for others.
      - WHEN TO USE IT: Always use `ReentrantLock` as your default. In
        Object-Oriented Programming, methods inside a class constantly call
        other methods inside the same class. Reentrant locks allow them to do
        this safely without freezing themselves.



---
4. SYSTEM DEADLOCKS &B THE COFFMAN CONDITIONS (S 45-46)

   Even with `withLock` and `ReentrantLock`, you can still freeze your app if
   two different threads need two different locks at the exact same time.

   The lecture formally defines the 4 rules (The Coffman Conditions) that must
   exist simultaneously for a Deadlock to occur:
      1. MUTUAL EXCLUSION: Resources require exclusive access (Locks exist).
      2. HOLD and WAIT: A thread holds Lock A while waiting in line to grab Lock
         B.
      3. NO PRE-EMPTION: The Operating System cannot forcefully strip a lock out
         of a thread's hands.
      4. CIRCULAR WAIT: Thread 1 holds A and waits for B. Thread 2 holds B and
         waits for A. They stare at each other forever.


---
5. BREAKING THE CYCLE: THE BANK TRANSFER (S 47-48)

   To fix a Deadlock, we simply break Rule #4 (Circular Wait). We do this using
   an architectural strategy called GLOBAL LOCK ORDERING.

   If Alice wants to send money to Bob, the system must lock Alice's account
   AND Bob's account at the same time so no money goes missing.



THE BUGGY WAY (Causes Deadlock):
```Kotlin
fun transfer(fromAccount: Account, toAccount: Account, amount: Int) {
    // If Thread 1 is Alice->Bob, and Thread 2 is Bob->Alice...
    // Thread 1 locks Alice. Thread 2 locks Bob.
    // They cross paths and freeze forever right here.
    fromAccount.lock.withLock {
        toAccount.lock.withLock {
            fromAccount.withdraw(amount)
            toAccount.withdraw(amount)
        }
    }
}
```



THE SAFE WAY (Global Ordering):
   We must force every thread in the system to acquire locks in the exact same
   sequence, regardless of who is sending the money to whom. Every account has a
   unique `accountNumber`.

```Kotlin
fun transfer(fromAccount: Account, toAccount: Account, amount: Int) {

    // 1. Sort the accounts by their mathematical ID number.
    val (firstLock, secondLock) = if (fromAccount.accountNumber < toAcount.accountNumber) {
        Pair(fromAccount, toAccount)    // fromAccount is lower, lock it first
    } else {
        Pair(toAccount, fromAccount)    // toAccount is lower, lock IT first
    }

    // 2. Both Thread 1 and Thread 2 will now hit this exact same sequence.
    // They will BOTH try to lock the exact same "firstLock" account first.
    firstLock.lock.withLock {
        secondLock.lock.withLock {

            // 3. We safely have both keys. Do the math.
            if (fromAccount.balance >= amount) {
                fromAccount.withdraw(amount)
                toAccount.deposit(amount)
            }
        }
    }
}
```

   WHY THIS WORKS: If Alice is Account #1 and Bob is Account #2:
      - Thread 1 (Alice to Bob) evaluates the math and tries to lock Alice first.
      - Thread 2 (Bob to Alice) also evaluates the math and tries to lock Alice
        first.
      - Thread 2 sees that Thread 1 already has Alice's key, so Thread 2 is
        forced to wait in line at the very first door.
      - Thread 1 gets Alice, gets Bob, finishes, and unlocks them both.
      - Thread 2 wakes up and proceeds safely. The circle is broken, and a
        deadlock is mathematically impossible.
      - WHEN TO USE THIS: Whenever a single function in your code requires
        acquiring MORE THAN ONE LOCK AT A TIME, you must sort them. If you
        don't, a deadlock is guaranteed to happen eventually.








            RULE 1: Establish a Global Lock Order:
               This order must be consistent across all threads to avoid
               conflicts. Consider the following example where we have two
               resources, ResourceA and ResourceB. We decide that the global
               lock order is based on the hash code of the resource objects.

* */


/*----------------------------------------------------------------------------*/

/*
   Here is a collection of complex concurrency scenarios in Kotlin. I have
   focused on COROUTINE-NATIVE primitives (like `Mutex` and `Semaphore` from
   `kotlinx.coroutines`) as these are idiomatic for modern Kotlin, but I also
   included a standard Java-style ReadWriteLock which is still very useful for
   specific performance profiles on the JVM.


   1. THE "READ-HEAVY" SHARED CACHE (ReadWriteLock)
      This uses `ReentrantReadWriteLock`. This is complex because it allows
      multiple threads to read simultaneously (non-blocking) but enforces
      exclusive access for writing. It is significantly more efficient than a
      standard `Mutex` when reads outnumber writes.

```Kotlin
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write
```
* */
class ThreadSafeCache<K, V> {
    private val map = HashMap<K, V>()
    // Fair = true helps prevent writer starvation in heavy load
    private val lock = ReentrantReadWriteLock(true)

    fun put(key: K, value: V) {
        // Only allows ONE thread to enter at a time
        lock.write {
            println("Writing: $key")
            map[key] = value
        }
    }

    fun get(key: K): V? {
        // Allows MULTIPLE threads to enter simultaneously
        // Blocks if a write lock is currently active
        lock.read {
            println("Reading: $key")
            return map[key]
        }
    }
}



/*
2. THE "DINING PHILOSOPHERS" (Mutex & Deadlock Avoidance)

   This is a classic CS problem showing how to manage CIRCULAR DEPENDENCIES.
   If every philosopher picks up their left fork and waits for the right,
   the program hangs (deadlock).

THE COMPLEX PART: We avoid deadlock by enforcing a consistent locking order (#
always lock the lower ID fork first).
* */




/*----------------------------------------------------------------------------*/
/*----------------------------------------------------------------------------*/


/*
```Kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class Fork(val id: Int, val mutex: Mutex())

class Philosopher(val id: Int, val leftFork: Fork, val rightFork: Fork) {
    suspend fun eat() {
        // CRITICAL: Order locks to prevent deadlock
        // Always pick up the fork with the lower ID first
        val firstFork = if (leftFork.id < rightFork.id) leftFork else rightFork
        val secondFork = if (leftFork.id < rightFork.id) rightFork else leftFork

        firstFork.mutex.withLock {
            println("Philosopher $id picked up fork ${firstFork.id}")
            delay(100)          // Simulating "picking up" time

            secondFork.mutex.withLock {
                println("Philosopher $id picked up fork ${secondFork.id} and is EATING")
                delay(500)      // Eat
                println("Philosopher $id put down both forks")
            }
        }
    }
}
```
* */







/*----------------------------------------------------------------------------*/
/*----------------------------------------------------------------------------*/

/*
   We will focus on pure logic, SERVICE CONTRACTS, and how computer physically
   categorises data in its memory.









* */



/*
                In CS, service contracts define the formal agreement between a
                service provider and a consumer, outlining how they interact.
                They specify technical details like API operation signatures,
                message formats (XML/JSON), and data types. They also often
                include QUALITY OF SERVICE (QoS) constraints such as uptime,
                latency, and response times, ensuring standardised, reliable
                communication between software components.

                KEY ASPECTS OF SERVICE CONTRACTS:
                - TECHNICAL SPECIFICATION: Defines the operations, inputs, and
                  outputs, such as in Web Services Description Language (WSDL)
                  or OpenAPI specifications.
                - SERVICE LEVEL AGREEMENT (SLA): Specifies non-functional
                  requirements like uptime (e.g., 99.9%), maximum latency, and
                  support response times.
                - DESIGN BY CONTRACT: Specifies preconditions (what must be true
                  before a method runs) and postconditions (what the method
                  guarantees afterwards) to ensure software correctness.
                - STANDARDISATION: Ensures consistent interaction patterns
                  across a service inventory, improving interoperability.
                - STRUCTURE: Includes endpoint locations, security requirements,
                  and message exchange patterns.

                These contracts are crucial in Service-Oriented-Architecture
                (SOA) and microservices, allowing teams to deploy, and update
                services independently as long as they adhere to agreed-upon
                interface.
* */




/*----------------------------------------------------------------------------*/
/*----------------------------------------------------------------------------*/















/*----------------------------------------------------------------------------*/
/*----------------------------------------------------------------------------*/



















/*----------------------------------------------------------------------------*/
/*----------------------------------------------------------------------------*/










/*----------------------------------------------------------------------------*/