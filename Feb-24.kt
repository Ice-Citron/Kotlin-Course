import java.security.KeyStore
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.concurrent.locks.StampedLock
import kotlin.concurrent.read
import kotlin.concurrent.withLock
import kotlin.concurrent.write
import kotlin.math.sqrt
import kotlin.concurrent.thread

/*
1. THE OG: `synchronized` (INTRINSIC LOCKS / MONITORS)
    Every object in the JVM has a built-in "hidden" lock called a monitor. The
    `synchronized` keyword simply grabs this lock.
 */
class ThreadSafeCounter_(
    private var count: Int = 0,
    private val lockObject: Any = Any()
) {
    fun increment() {
        // Only one thread can enter this block at a time
        synchronized(lockObject) {
            count++
            println("Count is now $count by ${Thread.currentThread().name}")
        }
    }
}
/*
    - WHY USE IT HERE? It is dead simple. You don't have to worry about
      `try / finally` blocks because the JVM automatically releases the lock
      when the block finishes (even if an exception is thrown).
    - WHEN TO USE: For simple, short, and fast critical solutions where you
      just need basic thread safety without complex routing.
    - WHY NOT USE IT? It is rigid. A thread waiting for a `synchronized` lock
      will wait forever. It cannot be interrupted, and you cannot set a timeout.
    - WHEN NOT TO USE: When you need a timeout, when you need to lock in one
      method and unlock in another, or when you have complex read/write dynamics.
*/
// import java.util.concurrent.locks.ReentrantLock
// import java.util.concurrent.TimeUnit
// import kotlin.concurrent.withLock

class TicketBookingSystem_(
    private val lock: ReentrantLock   = ReentrantLock(),
    private var ticketsAvailable: Int = 1
) {
    fun tryToBookTicket(user: String) {
        // The superpower of ReentrantLock: We don't wait forever!
        // We wait for 2 seconds. If we don't get the lock, we walk away.
        val acquired = lock.tryLock(2, TimeUnit.SECONDS)

        if (acquired) {
            lock.withLock {
                println("HAHA test lock lol")
            }

            try {
                if (ticketsAvailable > 0) {
                    Thread.sleep(500)       // Simulate processing
                    ticketsAvailable--
                    println("$user successfully booked the ticket!")
                } else {
                    println("$user: Sorry, sold out.")
                }
            } finally {
                lock.unlock()       // CRITICAL: You must manually unlock
            }
        } else {
            println("$user gave up waiting for the server.")
        }
    }
}
/*
    - WHY USE IT HERE? The `tryLock()` method prevents threads from freezing
      indefinitely if the system gets jammed.
    - WHEN TO USE: When you need timeouts, fairness (giving the lock to the
      thread that has been waiting the longest), or the ability to interrupt
      a waiting thread.
    - WHY NOT USE IT? You must remember the `try / finally` block (or use
      Kotlin's `lock.withLock {}` helper). If you forget to unlock, your
      entire program deadlocks.
    - WHEN NOT TO USE: If a simple `synchronized` block does the exact same
      job, use `synchronized` to save boilerplate.
* */








/*
3. THE OPTIMIZER: `ReentrantReadWriteLock`
    A standard `ReentrantLock` forces everyone to wait in a single file line.
    But what if 99% of your threads just want to read a value, and only 1%
    want to write? Forcing readers to wait for other readers is highly
    inefficient.
* */
// import java.util.concurrent.locks.ReentrantReadWriteLock
// import kotlin.concurrent.read
// import kotlin.concurrent.write

class HighScoreLeaderboard_(
    private val rwLock: ReentrantReadWriteLock = ReentrantReadWriteLock(),
    private var topScore: Int = 0
) {
    fun getTopScore(): Int {
        // Unlimited threads can enter here simultaneously
        // UNLESS a write lock is currently active.
        rwLock.read {
            return topScore
        }
    }

    fun updateScore(newScore: Int) {
        rwLock.write {
            if (newScore > topScore) {
                topScore = newScore
                println("New top score: $topScore")
            }
        }
    }
}
/*
    - WHY USE IT HERE? A leaderboard is read thousands of times a second but
      updated rarely. This lock prevents a bottleneck by letting all readers
      read at the exact same time.
    - WHEN TO USE: Caches, config files, dictionaries--any shared state where
      `Read >>> Write`
    - WHY NOT USE IT? It is mathematically complex under the hood. The overhead
      of managing two different lock states actually makes it slower than a
      normal `ReentrantLock` if your operations are incredibly fast or if writes
      happen frequently.
    - WHEN NOT TO USE: If writes happen almost as often as reads, or if the
      "read" operation takes less than a millisecond (the lock overhead will
      dominate the execution time).s
* */






/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/*
4. THE CS FLEX: `StampedLock` (Optimistic Locking)
    ... `StampedLock` (introduced in Java 8) is an ultra-fast alternative to
    `ReadWriteLock`. It introduces OPTIMISTIC READING. It assumes "nobody will
    probably write while I am reading." It doesn't actually lock the memory; it
    just gives you a "stamp" (the version number). You read your data, then
    check if the stamp changed. If it did, it means a write happened, and you
    try again.
* */
// import java.util.concurrent.locks.StampedLock

class Point_24 (
    private var x: Double = 0.0,
    private var y: Double = 0.0,
    private val lock: StampedLock = StampedLock()
) {
    fun move(deltaX: Double, deltaY: Double) {
        val stamp = lock.writeLock()        // Standard exclusive lock
        try {
            x += deltaX
            y += deltaY
        } finally {
            lock.unlockWrite(stamp)
        }
    }

    fun distanceFromOrigin(): Double {
        // 1. OPTIMISTIC READ: Don't block anyone. Just get a version stamp.
        var stamp = lock.tryOptimisticRead()
        var currentX = x
        var currentY = y

        // 2. Validate: Did anyone write to X or Y while I was copying them?
        if (!lock.validate(stamp)) {
            // 3. Fallback: Yes, a write happened. Fall back to a strict read
            //    lock.
            stamp = lock.readLock()
            try {
                currentX = x
                currentY = y
            } finally {
                lock.unlockRead(stamp)
            }
        }

        // 4. Safely calculate
        return sqrt(currentX * currentX + currentY * currentY)
    }
}
/*
    - WHY USE IT HERE? It offers the absolute maximum throughput for read-heavy
      operations, as optimistic reads don't block the OS thread at all.
    - WHEN TO USE: High-frequency, high-performance, ultra-low-latency systems
      (like financial trading engines or game loop physics).
    - WHY NOT USE IT? It is incredibly easy to mess up. Also, `StampedLock` is
      NOT REENTRANT--if a thread holding a `StampLock` tries to acquire it
      again, it will deadlock itself.
    - WHEN NOT TO USE: Standard application logic. It's too complex for 99% of
      daily use cases.
* */






/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/

interface Lock {
    var isLocked: Boolean
    fun lock()
    fun unlock()
}

class PadLock(): Lock {
    override var isLocked: Boolean = true   // Must override the interface property
    override fun lock() {
        isLocked = true
        println("Padlock clicked shut.")
    }
    override fun unlock() {
        isLocked = false
        println("Padlock popped open.")
    }
}

/*
fun main() {
    // apply configures the object immediately. Returns the padlock instance.
    val lockPL = PadLock().apply { unlock() }       // Directly calling the method! Same as `this.unlock()`
}
 */






/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
// SmartLock requires everything Lock requires, PLUS a battery.
interface SmartLock : Lock {
    var batteryLevel: Double
}

// Sub-type implementation
class WifiLock(
    override var batteryLevel: Double = 100.0,
    override var isLocked: Boolean = true
) : SmartLock {
    override fun lock() {
        isLocked = true
        println("WifiLock just slammed shut!")
    }

    override fun unlock() {
        isLocked = false
        println("WifiLock just snapped open!")
    }
}

fun securityScanner(device: Lock) {
    // `with` groups multiple calls. We can't access batteryLevel here
    // because the Apparent Type is just `Lock`
    with (device) {         // `device as WifiLock`
        unlock()
        lock()
    } // `with` returns block result
}

/*
    val WLock: WifiLock = WifiLock()
    securityScanner(WLock)  // OK: WifiLock offers everything Lock requires
}
 */


/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
// QUESTION 3: THE NULLABILITY HIERARCHY AND `let`

/*
fun main() {
    var backDoorLock: Lock? = null
    backDoorLock = PadLock()
    backDoorLock?.let {
        it.unlock()
    }
}
 */






/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
fun triggerAlarm(): Nothing {
    throw Exception("Intruder")
}

/*
fun main() {
    val pin: Int = 3310
    val status: String = run {          // This compiles because `Nothing` is a subtype of String
        if (pin == 1234) "Access Granted"
        else triggerAlarm()
    }
}
 */





/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
// Rule: If a function returns T, it is OK to return U, as long as U <: T.
// Lock is the supertype, WifiLock is the subtype.
fun lockFactory(): Lock {

    // also executes the print statement, and then seamlessly
    // returns the WifiLock object down the chain to the `return` statement.
    return WifiLock().also { println("WifiLock successfully manufactured.") }
}

/*
fun main() {
    val newLock = lockFactory()
}
* */











/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/*
fun main() {
    val myThread = Thread {
        println("Chatty thread running!")
        Thread.sleep(500)
    }
    myThread.apply {
        start()         // Begins execution
        join()          // Main thread pauses here until this thread finishes
    }
    println("Main thread done!")

    val myTask = thread(start = false) {
        // Heavy math or I/O here...
        println("Working...")
    }

    myTask.apply{
        start()     // Begin execution
        // ... do other work here ...
        join()      // Wait for myTask to finish before moving
        println("Task complete!")
    }

    myTask.start()
    // ... HEAVY SERIES OF OTHER TASKS RUNNING IN PARALLEL!
    myTask.join()
    println("V2 tasks completed!!!")
}
 */


/*
    In Kotlin, a THREAD is a standard way to achieve parallelism by running a
    block of code concurrently with the main program. Since Kotlin typically
    runs on the JVM, its `Thread` is a wrapper around the native operating
    system thread. You create one by either instantiating the `Thread` class
    or using the `thread { ... }` convenience function. When a thread is
    created, it sits in a "New" state and doesn't actually execute until you
    trigger it. This allows you to offload heavy computations--like processing
    drone telemetry or running a computer vision model--away from the main
    thread, keeping your application responsive.

    The function `start()` and `join()` manage the thread's lifecycle. Calling
    `start()` tells the JVM to allocate resources and actually begin executing
    the code inside the thread's block; without it, the thread is just an idle
    object. `join()`, on the other hand, is a blocking call used by the "parent"
    thread (usually the main thread). When you call `threadA.join()`, the parent
    thread will pause and wait until `threadA` has completely finished its
    execution before moving to the next line of code.


WHEN AND HOW THEY ARE USED
    You normally use this pattern when you have a specific task that must run in
    the background but whose result is required before the program can proceed.
    For example:
    - `start()`: Used immediately after setup to begin a background task, like
      a socker listener for a robotic arm.
    - `join()`: Used when you need to "synchronize" back up. If you start a
      thread to calculate a complex path for a drone, you would call `join()`
      right before the command to "move," ensuring the path coordinates are
      actually ready.
* */






/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/


/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/*
    In Kotlin, `synchronized` and `withLock` both solve the problem of "mutual
    exclusion" (ensuring only one thread accesses a block of code at a time),
    but they belong to different programming eras. `synchronized` is a keyword
    inherited directly from Java; it uses the intrinsic lock (monitor) built
    into every Java object. It is simple to use but inflexible--it is
    "structured", meaning the lock is automatically acquired when you enter the
    block and released when you leave. However, it doesn't work with Kotlin's
    coroutines because it blocks the entire thread, which can lead to
    performance bottlenecks or deadlocks in an asynchronous environment.

    `withLock`, on the other hand, is an extension for the `ReentrantLock` class
    in the `java.util.concurrent.locks` package. It is generally considered more
    modern and "idiomatic" for complex Kotlin applications. Unlike the basic
    `synchronized` block, a `ReentrantLock` allows for more advanced features,
    such as `tryLock()` (attempting to grab a lock without waiting forever) or
    setting a timeout. This prevents your application from hanging indefinitely
    if a resource is stuck. Using `withLock` ensures that the `unlock()`
    function is called in a `finally` block BEHIND THE SCENES, so you don't
    accidentally leave the door locked if an exception occurs.

    The biggest practical difference for you ... is how they handle
    INTERRUPTIBILITY. If a thread is stuck waiting for a `synchronized` block,
    you can't easily tell it to stop waiting. With `withLock` and `ReentrantLock`
    , the thread remains responsive to interruptions. If you are writing
    high-performance code or working with heavy concurrency, `withLock` is the
    safer, more flexible bet, whereas `synchronized` is fine for quick, simple
    thread-safety in legacy-style JVM code.
* */













/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/

class SafeTicketCounter(
    var totalTickets: Int = 0,
    private val lock: ReentrantLock = ReentrantLock()
) {
    fun sellTicket(): Int {
        lock.withLock {
            return (totalTickets++).also {println("Ticket sold! Total: $it.")}
        }
    }
}





/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
class ThreadSafeList(
    private val lock: ReentrantLock = ReentrantLock(),
    val items: MutableList<String> = mutableListOf<String>()
) {
    fun addOne(item: String) {
        lock.withLock { items.add(item) }
    }
    fun addTwo(item: String) {
        lock.withLock {
            with (this) { addOne(item); addOne(item) }
        }
    }
}

/*
fun main() {
    val safeList = ThreadSafeList()

    safeList.addTwo("Hello World")
    println(safeList.items)
}
 */


/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/

var latestTicket: String? = "VIP-Ticket"
val lock: ReentrantLock = ReentrantLock()

fun processTicket() {
    lock.withLock {
        latestTicket?.let { println(it); throw Exception() }
    }
}

/*
fun main() {
    try {
        processTicket()
    } catch (e: Exception) {
        println("Caught an error, but continuing...")
    }
    println("Ticket successfully released regardless!")
}
 */









/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
// QUESTION 5: Breaking the "Circular Wait" Deadlock with `run`
class Account(
    val id: Int,
    var balance: Double = 1000.0,
    val lock: ReentrantLock = ReentrantLock()
)

fun transferA(fromAcc: Account, toAcc: Account, amount: Double) {
    // We ise `run` to calculate and return a Pair of accounts strictly ordered
    // by ID.
    // This entirely eliminates the Coffman "Circular Wait" deadlock condition!
    val (firstAcc, secondAcc) = run {
        if (fromAcc.id < toAcc.id) Pair(fromAcc, toAcc) else Pair(toAcc, fromAcc)
    }

    // Always acquire locks in the globally consistent order we just calculated
    firstAcc.lock.withLock {
        secondAcc.lock.withLock{

            // Critical section is now 100% safe from data races and deadlocks!
            if (fromAcc.balance >= amount) {
                fromAcc.balance -= amount
                toAcc.balance += amount
                println("Successfully transferred $amount")
            }
        }
    }
}



/*
    ... Imagine you have two users, ALICE (id=1) and BOB (id=2), trying to send
    money to each other at the exact same millisecond on different threads.

    - THREAD A (Alice to Bob): Locks Alice... then waits to lock Bob.
    - THREAD B (Bob to Alice): Locks Bob... then waits to lock Alice.

    Neither thread can move forward because the other is holding the "key" they
    need. They will sit there forever, staring at each other. This is a classic
    dead-lock.


WHY THE ID ORDER FIXES IT
    By forcing the code to ALWAYS LOCK THE SMALLER ID FIRST, you break the
    circle and turn it into a queue. Let's look at ... with
    `if (id1 < id2)` logic applied:

    1. THREAD A (Alice to Bob): Sees IDs 1 and 2. It locks 1 first, then tries
       to lock 2.
    2. THREAD B (Bob to Alice): Sees IDs 2 and 1. Because of your code, it also
       tries to lock 1 first.

    Now, instead of a standoff, Thread B sees that Alice is already locked by
    Thread A. Thread B simply waits its turn. Once Thread A finishes the whole
    transfer and releases both locks, Thread B can grab Alice, then Bob, and
    finish its work.
* */






/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/








/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/







/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/





/*
            Gemini said: https://gemini.google.com/app/61f5e835c1f55bc4
                I completely understand why it feels confusing! You have actually nailed the three foundational classes related to inheritance, but that isn't quite the whole picture.

                Kotlin introduces several extra "specialty" classes. Instead of making you write hundreds of lines of boilerplate code (like you would in traditional Java), Kotlin gives you specific keywords to handle common scenarios instantly.

                Here is your complete cheat sheet for every type of class in Kotlin:
* */


