import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/*
    This is a ... understanding the SWE and CS concepts that your ... are teeing
    for your final exam.

    This specific practical is a goldmine for three classic exam topics: STATE
    ENCAPSULATION, GRAPH TRAVERSAL (BFS), and ADVANCED CONCURRENCY (Deadlocks
    & Thread Synchronisation).

    ... walkthrough of the architecture, the solutions, and exactly WHAT YOU
    NEED TO MEMORISE FOR...


---
TOPIC 1: DEFENSIVE PROGRAMMING
    THE BRIEF: Create a room that tracks occupancy, but mathematically prevents
    malicious or accidental bugs (like having -1 people in a room, or exceeding
    capacity).

    THE ARCHITECTURAL SOLUTION:
* */

/*
class MuseumRoom(
    val name: String,
    val capacity: Int,
    private val lock: ReentrantLock = ReentrantLock(),
) : MuseumSite {
    init {                      // 1. Constructor Validation
        require(capacity > 0) { "Capacity must be positive" }
    }

    var occupancy: Int = 0      // 2. Encapsulated State
        private set     // Outsiders can read, but only internal methods can write!

    fun hasCapacity(): Boolean = occupancy < capacity

    fun enter() {
        check(hasCapacity()) { "Room is full" }
        lock.withLock {
            occupancy++
        }
    }

    fun exit() {
        check(occupancy == 0) { "Room is empty" }
        lock.withLock {
            occupancy--
        }
    }
}
 */


/*
    IR - MC
        Iranian Minecraft
        init   --> require()
        method --> check()
    `public var` (public by default in kotlin...)
        BUT `private set`


    KEY EXAM TECHNIQUES:
        1. `require` vs `check`: Exams test your knowledge of the standard
           library. Use `require(condition)` in `init` blocks to validate inputs
           (throws `IllegalArgumentException`). Use `check(condition)` inside
           methods to validate the object's current state (throws
           `IllegalStateException`).
        2. `private set`: The golden rule of OOP encapsulation. If you use a
           public `var` without `private set`, you will lose marks because any
           random class could maliciously write `room.occupancy = 99999`.
* */




/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
FRACTALFRACTALFRACTALFRACTALFRACTALFRACTCARFLATCARFLATCARFLATCARFLATCARFLATCARF
FRACTAL                        _       _                                LATCARF
FRACTAL                       |_|_    |_|_                              LATCARF
FRACTAL                    _   _|_|_   _|_|                             LATCARF
FRACTAL                   |_|_| |_| |_|_|_                              LATCARF
FRACTAL                    _|        _|_|_|    _                        LATCARF
FRACTAL                   |_        |_| |_    |_|_                      LATCARF
FRACTAL                     |_|          _|_   _|_|                     LATCARF
FRACTAL                                _|_|_|_|_|_                      LATCARF
FRACTAL                              _|_|_|_|_| |_|                     LATCARF
FRACTAL                             |_| |_|_|_                          LATCARF
FRACTAL                                  _| |_|                         LATCARF
FRACTAL                        _   _   _|_                              LATCARF
FRACTAL                      _|_|_|_|_|_|_|    _                        LATCARF
FRACTAL                     |_| |_|_|_|_|_    |_|_                      LATCARF
FRACTAL                          _|_|_|_|_|_   _|_|                     LATCARF
FRACTAL                _   _   _|_|_|_|_|_|_|_|_|_                      LATCARF
FRACTAL              _|_|_|_|_| |_|_|_| |_|_|_| |_|                     LATCARF
FRACTAL             |_| |_|_|_    |_|_    |_|_                          LATCARF
FRACTAL                  _| |_|     |_|     |_|                         LATCARF
FRACTAL                _|_                                              LATCARF
FRACTAL              _|_|_|                                             LATCARF
FRACTAL             |_| |_     _                                        LATCARF
FRACTAL                  _|_   _|                                       LATCARF
FRACTAL                 |_| |_|                                         LATCARF
FRACTAL                                                                 LATCARF
FRACTALFRACTALFRACTALFRACTALFRACTALFRACTCARFLATCARFLATCARFLATCARFLATCARFLATCARF
* */
/*
    In Kotlin, assertions are primarily used to document and verify your
    assumptions during development. If a condition is false, the program throws
    an `AssertionError`.


1. BASIC `assert`
    The most common form is the `assert` function. It takes a boolean condition
    and an optional message.

```Kotlin
val capacity  = 10
val occupancy = 5

// Simple check
assert (occupancy < capacity)

// Check with a custom error message
assert (occupancy >= 0) { "Occupancy cannot be negative, but was $occupancy" }
```



2. `require` and `check`
    While `assert` can be disabled at runtime by the JVM, Kotlin provides two
    powerful alternatives that are ALWAYS ENABLED and very common in exams:

    * `require()`: Used for validating INPUT PARAMETERS. It throws an
      `IllegalArgumentException`.
    * `check()`: Used for validating INTERNAL STATE. It throws an
      `IllegalStateException`.

```Kotlin
class MuseumRoom(val name: String, val capacity: Int) {
    // Exam Technique: Always use `require` for preconditions
    init { require(capacity > 0) { "Capacity must be positive" } }
}
```


3. ASSERTIONS IN TESTING (JUnit)
    When writing your test files like `MuseumRoomTest.kt`, you use specific
    assertion functions from testing libraries (like `kotlin.test`)...

    ...
    (Not really needed I believe)
* */










/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
--- Preconditions are checks that validate whether certain conditions hold
    before executing a piece of code. In Kotlin, these checks help prevent
    unexpected behaviors by ensuring that functions receive valid arguments
    and that objects are in appropriate states.
* */


/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
TOPIC 2: Type Checking (`is` and `as`)
    The prompt explicitly mentions Kotlin's `is` and `as` keywords. The museum
    has regular rooms, but it also has an "Outside" space that behaves slightly
    differently (it has infinite capacity and you can't leave it). If you
    create a `MuseumSite` interface that both inherit from, you need to check
    types.

KEY EXAM TECHNIQUES:
    Exam love testing you on safe vs. unsafe casting.
* */


fun inspectSite(site: MuseumSite) {
    // 1. The Smart Cast (`is`)...
    if (site is MuseumRoom) {
        // The compiler automatically casts it inside this block!
        println(site.capacity)
    }

    // 2. The Safe Cast (`as?`) -- PRO TIP
    //    Returns null if it's not a MuseumRoom, avoiding a crash.
    val safeRoom = site as? MuseumRoom
    safeRoom?.enter()

    // 3. The Unsafe Cast (`as`) - DANGEROUS
    //    Crashes with ClassCastException if the site is the "Outside" space
    val room = site as MuseumRoom
}


/*
    In Kotlin, `as?` is the SAFE CAST OPERATOR, which attempts to cast an object
    to a specific type without throwing a `ClassCastException` if the cast fails
    . When you use `expression as? Type`, the compiler checks if the expression
    is an instance of that type; if it is, the cast succeeds and returns the
    value as that type, but if the types are incompatible, it simply returns
    `null` instead of crashing your program. This is particularly useful in
    exams for safety handling polymorphic types where you suspect an object
    might be a specific subtype but want to avoid the risks assosciated with the
    "unsafe" cast operator `as`.
* */















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
TOPIC 3: Graph Traversal (`checkWellFormed`)
    THE BRIEF: The Museum is secretly a DIRECTED GRAPH. Rooms are Nodes, and
    Turnstiles are Edges. You must prove that no rooms are isolated (unreachable
    from the entrance).

THE ARCHITECTURAL SOLUTION:
    Inside the `Museum` class, you would store the layout as an ADJACENCY LIST
    (e.g., `val turnstiles = mutableMapOf<MuseumRoom, MutableList<MuseumRoom>>()`
    ).

```Kotlin
fun checkWellFormed() {
    // --- STANDARD BFS GRAPH TRAVERSAL TEMPLATE ---
    val visited = mutableSetOf<MuseumRoom>()
}
```
* */




abstract class MuseumSite(
    val name: String,
    val lock: ReentrantLock = ReentrantLock(),
    val condition: Condition = lock.newCondition()      // For PatientVisitors (Extension)
) {
    // init {                      // 1. Constructor Validation
    //     require(capacity > 0) { "Capacity must be positive" }
    // }

    var occupancy: Int = 0      // 2. Encapsulated State
        private set     // Outsiders can read, but only internal methods can write!

    abstract fun hasCapacity(): Boolean // = occupancy < capacity

    open fun enter() {
        check(hasCapacity()) { "Room is full" }
        lock.withLock {
            occupancy++
        }
    }

    open fun exit() {
        check(occupancy == 0) { "Room is empty" }
        lock.withLock {
            occupancy--
        }
    }
}


class MuseumRoom(name: String, val capacity: Int) : MuseumSite(name) {
    init {
        require(capacity > 0) { "Capacity must be positive" }
    }

    override fun hasCapacity(): Boolean = occupancy < capacity

    override fun enter() {
        check(hasCapacity()) { "Room is full" }
        super.enter()
    }
}

class Outside : MuseumSite("Outside") {
    override fun hasCapacity(): Boolean = true  // Infinite capacity

    override fun exit() =
        throw UnsupportedOperationException("Cannot leave the outside")
}











/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    ...

1. ABSTRACT CLASSES IN KOTLIN
    Because Kotlin is obsessed with safety, it locks everything down by default.

            abstract class Object01 {
                fun doSomething()
                fun doSomething() { ... }
                open fun doSomething() { ... }
            }

    * If you write `abstract fun doSomething()`, it forces the subclass to
      override it.
    * If you write a regular `fun doSomething() { ... }` with a body inside an
      abstract class, Kotlin assumes it is a fixed, helper method. It is `final`
    * If you want to give the subclass the option to change it, you must
      explicitly unlock it with `open fun doSomething() { ... }`.


2. INTERFACES IN KOTLIN
    Interfaces are the exception. The entire point of an interface is to be a
    contract for other classes to implement.

        * Everything in an interface is `open` by default. #
        * Even if you write a function with a default body in a Kotlin interface
          , you can still override it in the child class without ever typing
          `even`.

    THE JAVA PLOT TWIST
        ... flip your brain backwards!

        In Java, EVERY method is `open` by default (Java calls this "virtual").
        If you write a normal method in a Java class or abstract class, any
        child class çan override it. If you want to lock it down like Kotlin
        does, you have to explicitly type the word `final` before the method!


---





* */





















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */



/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
--- Defensive programming is a, METHODOLOGY approach to software development
    that assumes mistakes, invalid inputs, or unexpected behaviors will occur
    and explicitly codes to detect and handle them. It increases code robustness
    , security, and stability by validating data, implementing strict error
    handling, and using assertions to ensure continued functionality.

    Key aspects and techniques include:

        - INPUT VALIDATION: Sanitising and verifying all user inputs and data
          sources to ensure they meet expected criteria, which helps prevent
          injection attacks.
        - ERROR HANDLING: Implementing robust error handling, such as
          `try-catch` blocks, to manage exceptions gracefully rather than
          letting the application crash.
        - ASSERTIONS: Using checks to confirm that assumptions
          (preconditions/postconditions) are true at specific point in the code.
        - DESIGNING FOR FAILURE: Planning for all potential contingencies, such
          as RESOURCE EXHAUSTION (e.g., memory, network) or null values.
        - CODE CLARITY: Writing clear, well-documented, and readable code to
          make it easier to audit and debug.
* */










/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    Let's apply the concept of `Condition` DIRECTLY TO THE `MuseumSite` CODE YOU
    PROVIDED.

    Currently, your `enter()` method uses `check(hasCapacity())`. If a room is
    full, the thread throws an exception and crashes. This is fine if you only
    check capacity before calling `enter()` (like the `ImpatientVisitor` does).
    But the `PatientVisitor` extension requires visitors to STAND AT THE DOOR
    AND WAIT for space to open up.

    Here is how `Condition` solves this exact problem, step-by-step.


1. KEY FEATURES OF `lock.newCondition()`
    A `Condition` acts as a "waiting room" for threads.
        * It is permanently glued to the `ReentrantLock` that created it. You
          cannot use a `Condition` unless you already hold its parent lock.
        * It allows threads to "let go" of the lock temporarily while they wait,
          so that other threads can enter the room and change the `occupancy`
          state.


2. KEY METHODS (The Core Trio)
    * `await()`: The thread goes to sleep, is added to a queue of waiters, and
      RELEASES THE LOCK. When it is eventually woken up, it must wait its turn
      to RE-ACQUIRE THE LOCK before it can move to the next line of code.
    * `signal()`: Wakes up exactly ONE thread that is currently asleep in the
      `await()` waiting room.
    * `signalAll()`: Wakes up ALL threads that are currently asleep.


3. HOW TO USE THEM (Applying it to `MuseumSite`)
    To make your museum support "Patient Visitors", you must modify both the
    entering and exiting logic.


    STEP A: THE WAITER (`enterByWaiting`)
        Instead of crashing when the room is full, a patient visitor will
        `await()` until space frees up.
```Kotlin
fun enterByWaiting() {
    lock.withLock {
        // EXAM TECHNIQUE: ALWAYS use a while loop for `await()`!
        condition.await()
        // --> Thread wakes up here and RE-ACQUIRES the lock.
        // The while loop repeats to double-check that the room STILL has capacity.
    }

    // We only reach this line if `hasCapacity() == true` AND we hold the lock
    occupancy++
}
```


    STEP B: THE SIGNALER (`exit`)
        If visitors are waiting outside, they will sleep forever unless someone
        leaving the room explicitly tells them to wake up.
```Kotlin
fun exit() {
    lock.withLock {
        check(occupancy > 0) { "Room is emtpy" }
        occupancy--

        // We just freed up a space! Wake up the waiting visitors.
        condition.signalAll()
    }
}
```


    4. WHY THE `while` LOOP IS MANDATORY (Spurious Wakeups)
        In exams, examiners love to test if you wrote `if (!hasCapacity())`
        instead of `while (!hasCapacity())`.

        You must use `while` because of SPURIOUS WAKEUPS (the OS waking a thread
        up randomly). Furthermore, even if `signalAll()` wakes up 5 threads,
        there might only be 1 space available. The `while` loop forces all 5
        threads to re-check `hasCapacity()`; the first one to grab the lock
        enters, and the other 4 realises it's full again and go back to
        `await()`.


    5. WHEN AND WHY NOT TO USE CONDITIONS
        * WHEN THREADS DON'T NEED TO WAIT FOR STATE CHANGES: Your
          `ImpatientVisitor` randomly selects a turnstile, and if it fails, they
          just pause for 10ms and try a completely different turnstile. They do
          not stand at the door waiting. Therefore, they don't need `Condition`.
        * WHEN USING MODERN KOTLIN: Raw locks and `Condition` variables are
          legacy Java concepts. In modern professional Kotlin, you would use
          Coroutines (`Mutex` and `StateFlow` or `Channel`) to handle
          concurrency, which are safer and don't block actual OS threads. You
          are only using them here because this lab specifically aims to teach
          you how underlying threading primitives work.
* */

class ParkingGarage(
    val capacity: Int,
    private val lock: ReentrantLock = ReentrantLock(),
    private val spaceAvailable: Condition = lock.newCondition(),
) {
    init { require(capacity > 0) { "Max capacity must be positive." } }

    var occupancy: Int = 0
        private set

    fun hasCapacity(): Boolean = capacity >= occupancy

    fun enter() {
        lock.withLock {
            while (!hasCapacity()) { spaceAvailable.await() }
            occupancy++
        }
    }

    fun leave() {
        lock.withLock {
            occupancy--
            spaceAvailable.signalAll()
        }
    }
}
















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */

class MessageBuffer(
    private var message: String? = null,
    private val lock: ReentrantLock = ReentrantLock(),
    private val notFull: Condition = lock.newCondition(),
    private val notEmpty: Condition = lock.newCondition(),
) {
    fun send(msg: String): Unit {
        lock.withLock {
            while (message != null) { notFull.await() }
            message = msg
            notEmpty.signal()   // Tell a waiting receiver the message is ready
        }
    }

    fun receive(msg: String): String {
        lock.withLock {
            while (message == null) { notEmpty.await() }
            val msgToReturn: String = message!!
            message = null
            notFull.signal()
            return msgToReturn
        }
    }
}















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
class Customer(val name: String)

class Table(
    private var customer: Customer? = null,
    private val lock: ReentrantLock = ReentrantLock(),
    private val tableAvailable: Condition = lock.newCondition(),
) {
    fun freeUpSeat() {
        tableAvailable.signal()
        customer = null
    }

    fun occupySeat(newCustomer: Customer) {
        customer = newCustomer
    }

    fun trySeat(maxWaitMillis: Long): Boolean {

        lock.withLock {
            while (customer != null) {
                tableAvailable.await(
                    maxWaitMillis,
                    TimeUnit.MILLISECONDS
                )
            }
            return true
        }
        return false
    }
}













/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
class TourGuide(
    private var groupSize: Int = 0,
    private val lock: ReentrantLock = ReentrantLock(),
    private val tourReady: Condition = lock.newCondition(),
) {
    fun joinTour () {
        lock.withLock {
            groupSize++
            if (groupSize == 5) {
                // I am the 5th person! Wake everyone else up.
                tourReady.signalAll()
            } else {
                // I am early. Wait for the others.
                while (groupSize < 5) {
                    tourReady.await()
                }
            }
        }
    }
}















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
class BankAccount694(
    balance: Int,
    private val lock: ReentrantLock = ReentrantLock(),
    private val sufficientFunds: Condition = lock.newCondition(),
) {
    var balance: Int = balance
        private set

    fun deposit(amount: Int) {
        lock.withLock {
            balance += amount
            sufficientFunds.signalAll()
        }
    }

    fun withdraw(amount: Int) {
        lock.withLock {
            while (balance < amount) {
                sufficientFunds.await()
            }
            balance -= amount
        }
    }
}




















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
3. `Museum.kt` (THE GRAPH, BFS, AND THREAD-SAFETY MANAGER)
    This is the core of the practical. Note the BFS logic and the Deadlock
    avoidance in `transition()`.
* */
// package museumvisit

// import java.util.concurrent.TimeUnit
// import kotlin.concurrent.withLock

/*
class Museum(
    val name: String,
    val entrance: MuseumRoom,
) {
    var admitted: Int = 0
        private set

    val outside = Outside()
    val rooms = mutableSetOf(entrance)

    // Adjacency List representing the Graph (Room -> Turnstiles)
    private val turnstiles =
        LinkedHashMap<MuseumRoom, MutableList<MuseumSite>>().apply {
            put(entrance, mutableListOf())
        }
}
 */











/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */



















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */














/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */














/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
