import java.security.KeyStore
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.concurrent.locks.StampedLock
import kotlin.concurrent.read
import kotlin.concurrent.withLock
import kotlin.concurrent.write
import kotlin.math.sqrt

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
interface SmartLock : Lock {
    var batteryLevel: Double
}

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
    with (device) {
        unlock()
        lock()
    }
}


fun main() {
    val WLock: WifiLock = WifiLock()
    securityScanner(WLock)
}


/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/







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
















/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/
/* ---- ----    ----    ----    ----    ----    ----    -----   ----    ----*/





/*
            Gemini said: https://gemini.google.com/app/61f5e835c1f55bc4
                I completely understand why it feels confusing! You have actually nailed the three foundational classes related to inheritance, but that isn't quite the whole picture.

                Kotlin introduces several extra "specialty" classes. Instead of making you write hundreds of lines of boilerplate code (like you would in traditional Java), Kotlin gives you specific keywords to handle common scenarios instantly.

                Here is your complete cheat sheet for every type of class in Kotlin:
* */


