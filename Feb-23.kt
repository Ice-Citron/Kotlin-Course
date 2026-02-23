import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import java.util.concurrent.Executors

/*
    in kotlin, what's the difference between an open class and an abstract class

---
    In Kotlin, the fundamental difference lies in INSTANTIATION and INTENT. An
    `open` class is a fully functional, "complete" class that can be
    instantiated on its own, but it has been explicitly given permission to be
    inherited from. By default, all Kotlin classes are `final`, meaning they are
    closed to subclassing; marking one as `open` simply unlocks that door. You
    would use an `open` class when you have a working base model that provides a
    default implementation, but you want to allow child classes to override
    specific behaviors if they choose.

    An `abstract` class, conversely, is an "incomplete" blueprint that CANNOT
    be instantiated. Its primary purpose is to define a common template for a
    family of subclasses. Abstract classes can contain `abstract` properties or
    functions that have no body, forcing any non-abstract subclass to provide
    the specific implementation. While an `open` class ALLOWS for extension, an
    `abstract` class REQUIRES it to be of any use. Use `abstract` when you have
    a high-level concept (like `Shape`) that shouldn't exist on its own but
    provides the essential structure for specific types (like `Circle` or
    `Square`).


---
KEY DIFFERENCE AT A GLANCE

    Feature // `open` class // `abstract` class
    INSTANTIABLE? // Yes // No
    DEFAULT METHODS // Must have a body // Can have bodies or be empty
    PURPOSE // To allow inheritance/overriding // To define a mandatory template
    ABSTRACT MEMBERS // Not allowed // Allowed




---
    Think of SEMAPHORE as a high-tech bouncer for a club with a strictly limited
    capacity. Unlike a standard lock (or Mutex) that only allows one person in
    at a time, a semaphore maintains a set number of "permits." When a thread
    wants to access a shared resource, it must first acquire a permit; if all
    permits are currently taken, the thread has to wait in line until someone
    else finishes their task and releases one. This makes semaphores perfect for
    managing THROTTLING, such as limiting the number of simultaneous database
    connections or concurrent API calls to prevent a system crash.

    There are two main types: COUNTING SEMAPHORES and BINARY SEMAPHORES. A
    counting semaphore can have any number of permits (e.g., 5 permits for 5
    available printer slots), while a binary semaphore has only one, acting very
    similarly to a traditional lock. The key distinction is that semaphores are
    about capacity management rather than ownership. In many implementations,
    any thread can release a permit, whereas, with a Mutex, only the thread that
    locked the door is allowed to unlock it.


    ---
KOTLIN EXAMPLE: THROTTLING COROUTINES
    In Kotlin, we typically use the `Semaphore` class from the
    `kotlinx.coroutines` library to manage concurrency within asynchronous code.
*/
// import kotlin




/* ----     ----    ----    ----    ----    ----    ----    ----    ----   */
/* ----     ----    ----    ----    ----    ----    ----    ----    ----   */
/*
    ... Learning raw, underlying thread mechanics before jumping into high-level
    abstractions like coroutines is a very classic (and incredibly solid) way
    to build your computer science foundation.

    If you aren't using coroutines, you will be relying on the heavy-duty JVM
    threading primitives that Kotlin inherits. Instead of `Mutex` and `suspend`
    functions, we use `ReentrantLock`, `java.util.concurrent.Semaphore`, and
    standard OS-level threads.

    Here are ... rewritten using standard, blocking JVM threads.


---
3. THE "RATE-LIMITED API CRAWLER" (Semaphore with Timeout)
    Instead of coroutines, we will use a classic thread pool (`ExecutorService`)
    to spawn multiple concurrent tasks. We use `java.util.concurrent.Semaphore`
    and its `tryAcquire` method to handle the timeout logic.

    THE COMPLEX PART: When working with raw threading primitives, you don't
    always get the luxury of automatic cleanup. You MUST put your permit release
    inside a `finally` block to guarantee the semaphore permit is returned even
    if the thread throws an exception.
* */
// import java.util.concurrent.Semaphore
// import java.util.concurrent.TimeUnit
// import java.util.concurrent.Executors

class ApiCrawler {
    // Standard JVM Semaphore: Only 3 concurrent network requests allowed
    private val throttle = Semaphore(3)

    fun fetchUrl(url: String) {
        // We block the thread for up to 2 seconds waiting for a permit.
        // Returns true if a permit is secured, false if it timed out.
        val acquired = throttle.tryAcquire(2000, TimeUnit.MILLISECONDS)

        if (acquired) {
            try {
                println("Downloading $url [Permit Acquired] by ${
                    Thread.currentThread().name
                }")
                Thread.sleep(500)       // Simulate blocking network latency
                println("Finished $url")
            } finally {
                // CRITICAL: Always release in a finally block so permits aren't
                // permanently lost
                throttle.release()
            }
        } else {
            println("Skipped $url: System too busy (Timeout).")
        }
    }
}

/*
fun main() {
    val crawler = ApiCrawler()
    // Create a pool of 10 standard threads to blast our crawler with requests
    val executor = Executors.newFixedThreadPool(10)

    for (i in 1..10) executor.submit { crawler.fetchUrl("site_$i.com") }
}
 */

/* ----     ----    ----    ----    ----    ----    ----    ----    ----   */


/*
    Semaphore in `java.util.concurrent` is a synchronisation aid that controls
    access to a shared resource using a count of permits. It is commonly used
    to limit the number of threads that can access a resource simultaneously.

KEY METHODS
    - `acquire()`
      Acquires a single permit, blocking if none are available. Throws
      `InterruptedException` if the thread is interrupted while waiting.

    - `acquire(int permits)`
      Acquires the specified number of permits, blocking until all are available
      . Throws `InterruptedException` if interrupted.

    - `acquireUninterruptibly(int permits)`
      Acquires the specified number of permits without interruption, blocking
      until all are available.

    - `tryAcquire()`
      Attempts to acquire a permit immediately. Returns `true` if successful,
      `false` if no permit is available.

    - `tryAcquire(int permits)`
      Attempts to acquire the specified number of permits immediately. Returns
      `true` if all are available, `false` otherwise.

    - `tryAcquire(long timeout, TimeUnit unit)`
      Attempts to acquire a permit within the specified timeout. Returns `true`
      if successful, `false` otherwise.

    - `release()`
      Releases a single permit, increasing the available permit count. May
      unblock a waiting thread.

    - `release(int permits)`
      Releases the specified number of permits, increasing the available count.
      May unblock waiting threads.

    - `availablePermits()`
      Returns the current number of available permits. Useful for debugging.

    - `drainPermits()`
      Acquires and returns all available permits immediately, returning the
      count acquired.

    - `getQueueLength()`
      Returns an estimate of the number of threads waiting to acquire permits.

    - `hasQueueThreads()`
      Returns `true` if any threads are waiting to acquire a permit.

    - `isFair()`
      Returns `true` if the semaphore is configured with fairness (FIFO)
      ordering.

    - `toString()`
      Returns a string representation of the semaphore, including the number of
      available permits.


KEY ATTRIBUTES
    - PERMIT COUNT: Maintains a count of available permits, initialised in the
      constructor.
    - FAIRNESS: Configurable via constructor (`true` for FIFO, `false` for
      non-fair/barging behavior).

    These methods allow fine-grained control over concurrency, making
    `Semaphore` ideal for resource pools, limiting thread access, or
    implementing locks with non-ownership semantics (binary semaphore).




--

    Interop (Interoperability) scenarios in programming refer to situations
    where different software components, systems, or programming languages must
    collaborate, exchange data, or function together seamlessly. These scenarios
    are essential for utilising legacy code, leveraging platform-specific APIs,
    or combining the strengths of different languages (e.g., performance of C++
    with the UI capabilities of C#).

    Common interop scenarios include:

    ...


---
    Being interrupted in the context of `java.util.concurrent.Semaphore` (which
    is often used in kotlin/Java interop scenarios) means that a thread
    currently blocked waiting for a permit has been requested to stop waiting.

    When a thread is waiting in `acquire()` and another thread calls
    `interrupt()` on it, the following occurs:

    - `InterruptedException` IS THROWN: The `acquire()` method immediately
      stops waiting and throws an `InterruptedException()`.
    - Interrupt Status is Cleared: By convention, the exception handler needs to
      be aware that the interruption status of the thread is cleared when the
      exception is thrown.
    - PERMIT IS NOT ACQUIRED: The thread does not acquire the permit it was
      waiting for.


---
DETAILED BEHAVIOR BREAKDOWN
    1. BLOCKING BEHAVIOR (`acquire()`): When using the standard `acquire()`, if
       the thread is interrupted, it abandons the wait.
    2. HANDLING THE EXCEPTION: The code must catch the `InterruptedException`.
       This is the mechanism for breaking out of a blocking state to shut down
       a task or thread cleanly.
    3. ALTERNATIVE - `acquireUninterruptibly()`: If you use
       `acquireUninterruptibly()` instead of `acquire()`, the thread will NOT
       throw an exception if interrupted. It will keep waiting until it receives
       a permit, and only then will its interrupted status be set.
    4. IMPORTANT NOTE ON KOTLIN COROUTINES: If you are using the Kotlin-native

       ...


PROPER HANDLING OF INTERRUPTION
    When catching `InterruptedException`, it is best practice to restore the
    interrupted status, because catching the exception clears it:

```Kotlin
try {
    semaphore.acquire()
    // ... use resource
} catch (e: InterruptedException) {
    // Re-interrupt the thread to ensure other code knows it was interrupted
    Thread.currentThread().interrupt()
    // ... handle cancellation/shutdown
}
```


KEY DIFFERENCE IN SEMAPHORE INTERRUPTS

    // Method // Behaviour on `Thread.Interrupt()` //
    // `acquire()` // Throws `InterruptedException`, stops waiting, clears
                      interrupt status
    // `acquireUninterruptibly()` // Continues waiting, sets interrupt status
                                     after acquiring
    // `tryAcquire()` // Not blocking, does not throw `InterruptedException`



 */





/* ----     ----    ----    ----    ----    ----    ----    ----    ----   */
/* ----     ----    ----    ----    ----    ----    ----    ----    ----   */








/* ----     ----    ----    ----    ----    ----    ----    ----    ----   */
/* ----     ----    ----    ----    ----    ----    ----    ----    ----   */
/*
--- Synchronous code simply means CODE THAT GETS EXECUTED SEQUENTIALLY. It is
    code that gets executed line by line, from top to bottom, in order. This is
    also known as single-threaded code. JS code is single-threaded, but it is
    capable of handling asynchronous code with a mechanism called the event loop

    ... Asynchronous programming differs in that it ALLOWS MULTIPLE TASKS TO RUN
    AT THE SAME TIME, and the programmer can often manage these tasks directly.
    It allows programs to continue to run even after you start a specific
    action.


--- ExecutorService: To simplify the development of multi-threaded applications,
    Kotlin provides an interface called ExecutorService (or simply executor). It
    encapsulates one or more threads into a single pool and puts submitted tasks
    in an internal queue to execute them in threads.


------
    In CS, particularly in systems programming languages like C++ and Rust,
    NON-OWNERSHIP SEMANTICS refers to a way of handling data where a variable,
    pointer, or reference has access to data but is NOT RESPONSIBLE FOR ITS
    LIFETIME OR DEALLOCATION.

    It represents a "borrowed" or "observer" relationship, rather than a
    "possessor" relationship.


KEY CHARACTERISTICS
    - NO ALLOCATION/DESTRUCTION: Non-owning types (like raw pointers `T*` or
      string views `std::string_view`) do not own the resource, meaning they do
      not destroy the object when they go out of scope.
    - DANGLING RISK: Because the type does not control the lifetime, if the true
      owner destroys the data, the non-owning pointer becomes a "dangling
      pointer".
    - REFERENCE-LIKE: Non-ownership types act more like references or pointers
      to the original object rather than independent copies.
    - EFFICIENCY: They are often lightweight (trivially copyable), as they only
      hold a memory address.


---
    In CS, NON-OWNERSHIP SEMANTICS (often referred to as "borrowing" or
    "viewing" in languages like C++) refers to a design pattern where a
    reference or pointer to data is used, but that reference does not manage
    the lifetime, allocation, or destruction of the underlying data.

    In the context of KOTLIN, non-ownership semantics are the default behaviour
    because Kotlin uses garbage collection (GC) and reference types for objects,
    rather than the manual, strict ownership rules found in languages like Rust
    or C++.

    ... breakdown of what non-ownership semantic means in Kotlin:

KEY ASPECTS OF NON-OWNERSHIP IN KOTLIN:
    - NO LIFECYCLE RESPONSIBILITY: A variable referencing an object does not
      "own" it. It merely points to it. The garbage collector determines when
      the memory can be freed, regardless of how many references point to it.
    - OBSERVER/VIEW ROLE: In Kotlin, you often pass objects around. These
      references are "observers"--they can read or mutate the object, but they
      do not destroy it when the variable goes out of scope.
    - DEFAULT BEHAVIOR FOR REFERENCES: Unlike `std::unique_ptr` in C++, which
      implies ownership, Kotlin's `val obj = SomeObject()` is a non-owning
      reference to an object in the heap.
    - DIFFERENCE FROM OWNERSHIP LANGUAGES: In C++, a non-owning pointer
      (`T*` or `std::string_view`) can "dangle" if the owner deletes the data.
      In Kotlin, because of GC, an object is not destroyed as long as at least
      one reference (even a non-owning one) points to it.
* */








/* ----     ----    ----    ----    ----    ----    ----    ----    ----   */
/* ----     ----    ----    ----    ----    ----    ----    ----    ----   */
/*
4. THE "BANK TRANSACTION" (Double Locking)
    To handle the bank transfer without coroutines, we use `ReentrantLock`. It
    behaves exactly like a `Mutex`, but it physically blocks the operating
    system thread that tries to acquire it until the lock becomes available.


* */






/* ----     ----    ----    ----    ----    ----    ----    ----    ----   */
/* ----     ----    ----    ----    ----    ----    ----    ----    ----   */


/*
    In asynchronous Kotlin (using Coroutines), a LOCK is a synchronisation
    mechanism, most commonly implemented via a `Mutex` (Mutual Exclusion),
    that restricts access to a shared resource or a critical section of code to
    only one coroutine at a time. Unlike traditional thread locks that block
    execution, a Kotlin `Mutex` is "suspending", meaning it suspends the
    coroutine, allowing the underlying thread to perform other work until the
    lock becomes available.


WHY USE LOCKS IN ASYNCHRONOUS KOTLIN
    You use locks in asynchronous Kotlin to ensure DATA INTEGRITY and prevent
    RACE CONDITIONS when multiple coroutines try to read and modify the same
    mutable variable or resource simultaneously.

    - PREVENT DATA CORRUPTION: If two coroutines increment a ... A `Mutex`
      ensures one finishes before the next begins.
    - NON-BLOCKING BEHAVIOR: Unlike the Java `synchronized` keyword, which
      blocks the thread, a `Mutex.lock()` suspends the coroutine, freeing up the
      thread to do other work, which is much more efficient.
    - SAFE RESOURCE ACCESS: Protecting shares resources like caches, in-memory
      databases, or file systems from concurrent modifications.


KEY MECHANISMS: Mutex and withLock
    The standard way to use lock in Kotlin is the `Mutex` class and its
    `withLock` extension function.

```Kotlin
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay

val mutex = Mutex()
val counter = 0

fun main() = runBlocking {
    repeat(100) {
        launch {
            // Protects the counter from concurrent access
            mutex.withLock {
                counter++
            }
        }
    }
    println("Counter: $counter")
}




```

    - `withLock`: Automatically acquires the lock, executes the block, and
      releases the lock, even if an exception occurs inside the block.
    - `lock()` / `unlock()`: Low-level functions, useful for more complex
      scenarios, but `withLock` is safer and preferred.
    - `tryLock()`: Attempts to acquire the lock immediately without waiting.
      Returns `false` if it cannot acquire it.

BEST PRACTICES
    - KEEP LOCKS SHORT: Minimise the time spent holding a lock to avoid
      bottle-necking the application.
    - AVOID NESTED LOCKS: Using one lock inside another can lead to deadlocks.
    -
* */


// fun main() = repeat(100) { print("LALALALALA") }



