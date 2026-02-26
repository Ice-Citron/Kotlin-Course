import java.util.Collections

/*
2. JUST-IN-TIME (JIT) Compilation && The JVM
    Kotlin and Java use a hybrid approach called JUST-IN-TIME (JIT) Compilation
    to get the best of both worlds.

EXPLAIN BY EXAMPLE:
    1. STATIC STEP: When you compile Kotlin (`kotlinc`), it does not create
       machine code. It creates an intermediate, universal language called
       BYTECODE.
    2. DYNAMIC STEP: When you run the app, the JVM steps in. It starts
       interpreting the Bytecode so your app opens instantly.
    3. JIT STEP: If the JVM notices you are running a specific `while` loop
       thousands of times (a "Hotspot"), the JIT COMPILER activates in the
       background and physically compiles that loop into native Machine Code
       for a massive speed boost.

    - WHEN DO I USE THEM: Whenever you write Kotlin, Java, or Scala, you are
      relying on a JIT Virtual Machine.
    - WHY DO I USE THEM: "Write Once, Run Anywhere." You only compile your code
      to Bytecode once. Anyone with a JVM installed on their computer can run
      that Byteode perfectly. Plus, JIT optimisation means the longer your
      backend server runs, the faster it gets!
    - WHEN DO I NOT USE THEM AND WHY: Do not use JVM/JIT languages for
      embedded systems with tiny amounts of RAM (like an Arduino). A Virtual
      Machine is a heavy, memory-hungry piece of software, and JIT compilation
      can cause unpredictable micro-stutters when it triggers.
    - WHY THEM OVER OTHER USE CASES: It mathematically proves your code is
      structurally sound before running (like a Static Compiler), but provides
      the cross-platform flexibility of an Interpreter.


---
3. GARBAGE COLLECTION (Mark and Sweep)
    In older language like C, if you create an object in RAM, you must manually
    delete it when you are done. If you forget, your app's memory filles up
    until it crashes (a Memory Leak). The JVM handles this automatically via
    GC.

EXPLAIN BY EXAMPLE:
    Imagine the JVM pauses your app for a microsecond to clean up:

    1. MARK: It looks at the "Roots" (your active thread stacks and top-level
       variables) and traces every single object concurrently connected to them.
    2. SWEEP: It looks at the Heap (the physical RAM). Any object that is not
       marked is considered "Garbage." It instantly deletes them to free up
       memory.

    - WHEN DO I USE THEM: It runs invisibly in the background 24/7 in your
      Kotlin/Java apps.
    - WHY DO I USE THEM: It eliminates 90% of memory-leak bugs. You just create
      objets, use them, and let the system clean up your mess.
    - WHEN DO I NOT USE THEM AND WHY: You do not use GC languages for HARD
      REAL-TIME SYSTEMS (like Pacemakers, self-driving car brakes, or
      fighter-jet flight controls). When the GC pauses your app to swepp memory,
      it can cause a micro-stutter (e.g., 5 milliseconds). In a self-driving car
      at 70mph, a 5-millisecond pause could be fatal. You must use manual
      memory management (C/C++) for those.
    - WHY THEM OVER OTHER USE CASES: Manual memory management is notoriously
      difficult and is the #1 cause of security vulnerabilities in the world.
      GC allows for massively safer code and faster developer productivity.

* */











/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
4. COMPUTED PROPERTIES vs. BACKING FIELDS (`field`)
    In Java, variables are called "Fields". Kotlin abstracted this away into
    "Properties" (`val`/`var`), but under the hood, Kotlin still relies on
    physical Java fields to store data in the RAM.

EXPLAIN BY EXAMPLE:
* */

class test_case85 (val width: Int, val height: Int) {

    val area85: Int
        get() = width * height

    var score: Int = 0
        set(newValue) {
            if (newValue < 0) throw Exception("No negatives!")

            // THE TRAP:
            // score = newValue     // CRASH! This calls set() again! INFINITE RECURSION

            // THE FIX:
            field = newValue        // Bypasses the setter and writes directly to the RAM.
        }
}

/*

* */








/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    ... The `get()` method is significantly simpler than the `set()` method.

    For a custom getter, there is NO EQUIVALENT TO `newValue`, and the only
    keyword you need to remember is `field`.

    ... how it works and why it's simpler:


1. WHY IS THERE NO `newValue`?
    A getter's entire job is to return data, not receive it. Because you aren't
    passing any new information into the property when you read it, there is no
    parameter equivalent to `newValue` (or `value`). It is just an empty
    function: `get()`.


2. THE `field` KEYWORD (The Backing Field)
    Just like in your setter, `field` is the magic keyword that references the
    actual hidden memory slot where the data lives. You use it to grab the
    stored value and return it (or modify it slightly before returning it)
    without accidentally calling the getter again and causing an infinite loop.

    Example...:
* */
var username139: String = "default_user"
    get() {
        // We use `field` to get the actual stored text.
        // If we typed `return username.uppercase()`, it would trigger
        // `get()` again...  forever!
        field = "$field haha"
        return field.uppercase()
    }
    set(value) {
        field = value.trim()
    }




/*
THE "COMPUTED PROPERTY" EXCEPTION
    There is one massive difference with `get()`: YOU DON'T ALWAYS NEED `field`.

    If your getter completely calculates its return value based on other
    variables, the Kotlin compiler realises it doesn't need to store any data
    for this property at all. It won't create a backing field in memory,
    m,aking your code highly efficient.
* */

val firstName164: String = "Shi"
val lastName164: String = "Hao"

// Notice there is no `= ""` initialisation here, and no `field` keyword!
// This takes up zero memory space of its own.
val fullName164: String
    get() {
        return "$firstName164 $lastName164"
    }

    // (Note: Because `fullName164` is a `val`, it only has a `get()`. You
    // cannot write a `set()` for a `val`.)



/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    - WHEN DO I USE THEM: Use Computed Properties (`get() = ...`) when a value
      is just math based on other variables. Use the `field` keyword inside a
      custom setter where you need to validate or log data before physically
      storing it.
    - WHY DO I USE THEM: Computed properties ensure data is never "out of sync"
      because they calculate the answer dynamically. `field` allows you to
      strictly control what data enters your class memory.
    - WHEN DO I NOT USE THEM AND WHY: Never type the property's actual name
      inside its own custom setter (e.g., `score = newValue`). It will endlessly
      trigger itself until the program crashes with a `StackOverflowError`.
    - WHY THEM OVER OTHER USE CASES: In Java, you are forced to write verbose
      `public void setScore(int s)` methods. Kotlin's `field` keyword allows you
      to keep the syntax beautiful (`game.score = 5`) while doing complex
      validation safely under the hood.
* */








/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
5. CO-LOCATING KOTLIN AND JAVA (Namespaces and Visibility)
    Because both Kotlin and Java compile down to the exact same BYTECODE, you
    can mix and match them perfectly in the same project. However, you must obey
    strict namespace and visibility rule.


EXPLAIN BY EXAMPLE:
    You can have `src/main/java/Utils.java` and `src/main/kotlin/App.kt` in the
    exact same codebase, and they can call each other's functions seamlessly.
    - WHEN DO I USE THEM: When you get hired at a company with a massive,
      10-year-old Java codebase, and you want to start writing new features in
      modern Kotlin.
    - WHY DO I USE THEM: "100% Interoperability". You don't have to rewrite
      millions of lines of legacy Java just to use Kotlin.
    - WHEN DO I NOT USE THEM AND WHY: THE NAMESPACE RULE. You cannot have a
      `Counter.java` and a `Counter.kt` in the exact same folder/package.
      Because both compile down to `Counter.class` bytecode, the JVM won't know
      which one is the real one, and your compiler will throw a "Duplicate Class
      " error.
    - WHY THEM OVER OTHER USE CASES: Java and Kotlin's shared JVM environment
      makes transitioning incredibly smooth. However, note that Kotlin defaults
      to `public` visibility, whereas Java defaults to "package-private." In
      Java, you must explicitly write `public class Counter` to make it
      accessible everywhere.
* */












/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    ... Successfully architecting a deadlock-free lock order and a custom
    iterator using the Strategy Pattern means you have mastered the most
    difficult, industry-level concepts in this module. You are definitely ready
    for the real world!

    ... Instead of teaching you how to write code, the first half of this
    lecture takes you under the hood to explain HOW THE COMPUTER ACTUALLY RUNS
    YOUR CODE. It breaks down exactly what happens between you clicking "Run"
    and the CPU executing your logic.

    ...


---
1. STATIC COMPILATION
    Before understanding Kotlin, you must understand the traditional way code
    is run.
    - ...

2. INTERPRETATION...
* */








/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
JAVA FIELDS SHOULD ALWAYS BE PRIVATE

    Best practice:
    - Make all fields PRIVATE
    - Only provide a public "getter" method if reading the field value is part
      of the service your class should provide
    - Make fields `final` if possible
    - Only provide a public "setter" for a non-final field if changing the value
      of the field is part of the service your class should provide

    This approach maximises ENCAPSULATION
* */





/*
private final Set<Integer> observedValues = new HashSet<>();

public Set<Integer> getObservedValues() {
    return Collections.unmodifiableSet(observedValues);
}
 */








/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */









/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */






/*
            Gemini said: https://gemini.google.com/app/61f5e835c1f55bc4
                I completely understand why it feels confusing! You have actually nailed the three foundational classes related to inheritance, but that isn't quite the whole picture.

                Kotlin introduces several extra "specialty" classes. Instead of making you write hundreds of lines of boilerplate code (like you would in traditional Java), Kotlin gives you specific keywords to handle common scenarios instantly.

                Here is your complete cheat sheet for every type of class in Kotlin:
* */
