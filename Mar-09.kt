import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/*
3. `Museum.kt` (THE GRAPH, BFS, AND THREAD-SAFETY MANAGER)
    This is the core of the practical. Note the BFS logic and the Deadlock
    avoidance in `transition()`.
* */
// package museumvisit

// import java.util.concurrent.TimeUnit
// import kotlin.concurrent.withLock

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

    fun addRoom(room: MuseumRoom) {
        require(rooms.none { it.name == room.name }) {
            "Room already exists"
        }
        rooms.add(room)
        turnstiles[room] = mutableListOf<MuseumSite>()
    }

    fun connectRoomTo(from: MuseumRoom, to: MuseumRoom) {
        require(from in rooms && to in rooms) {
            "Rooms must be in museum"
        }
        require (from != to) {
            "Cannot connect room to itself"
        }
        val dests = turnstiles[from]!!
        require(to !in dests) { "Turnstile already exists" }
        dests.add(to)
    }

    fun connectRoomToExit(room: MuseumRoom) {
        require(room in rooms) { "Room must be in museum" }
        val dests = turnstiles[room]!!
        require(outside !in dests) { "Exit already exists" }
        dests.add(outside)
    }

    fun getTurnstiles(room: MuseumRoom): List<MuseumSite> =
        turnstiles[room] ?: listOf()

    // ---- CONCURRENCY LOGIC ----

    fun enterIfPossible(): MuseumRoom? {
        return entrance.lock.withLock {
            if (entrance.hasCapacity()) {
                entrance.enter()
                admitted++
                entrance
            } else null         // alternatively we could instead use `check() {}`
        }
    }

    // EXTENSION: Patient entrance uing Condition Variables
    fun enterByWaiting(maxWaitMs: Long): MuseumRoom? {
        return entrance.lock.withLock {
            var nanosLeft = TimeUnit.MILLISECONDS.toNanos(maxWaitMs)
            while (!entrance.hasCapacity() && nanosLeft > 0L) {
                nanosLeft = entrance.condition.awaitNanos(nanosLeft)
            }
            if (entrance.hasCapacity()) {
                entrance.enter()
                admitted++
                entrance
            } else null
        }
    }

    fun transition(from: MuseumRoom, to: MuseumSite): MuseumSite? {
        // DEADLOCK PREVENTION: Global lock ordering alphabetically
        val (first, second) =
            if (from.name < to.name) from to to else to to from

        return first.lock.withLock {
            second.lock.withLock {
                if (to.hasCapacity()) {
                    from.exit()
                    to.enter()
                    to  // Success
                } else null
            }
        }
    }

    // EXTENSION: Patient Transition (Resolves "Hold and Wait" Deadlocks via
    // Timeouts)
    fun transitionWaiting(from: MuseumRoom, to: MuseumSite,
                          timeoutMs: Long): MuseumSite? {
        val (first, second) =
            if (from.name < to.name) from to to else to to from
        return first.lock.withLock {
            second.lock.withLock {
                var nanosLeft: Long =
                    TimeUnit.MILLISECONDS.toNanos(timeoutMs)
                while (!to.hasCapacity() && nanosLeft > 0L) {
                    nanosLeft = to.condition.awaitNanos(nanosLeft)
                }
                if (to.hasCapacity()) {
                    from.exit()
                    to.enter()
                    to
                } else null
            }
        }
    }

    // ---- GRAPH CHECKING (BFS) ----
    fun checkWellFormed() {
        // 1. Find Unreachable Rooms (BFS from Entrance)
        val reachable = mutableSetOf<MuseumRoom>()
        val queue = ArrayDeque<MuseumRoom>()

        queue.
    }
}




/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */

















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    In Kotlin, `Iterable.none()` is a versatile extension function that checks
    for the absence of elements, often used as the logical opposite of `any()`.


HOW `Iterable.none()` WORKS
    There are two ways to use `none()`:
        1. WITHOUT A PREDICATE: It returns `true` if the collection is empty.
        2. WITH A PREDICATE: It returns `true` if NO elements match the given
           condition. It is functionally equivalent `!any { ... }`
* */

val visitors68: List<String> = listOf("Neha", "Alex")

// 1. No predicate: Returns true if the list is empty
val isEmpty68: Boolean = visitors68.none()      // false

// 2. With predicate: Returns true if no one is named "Zork"
val noZorks: Boolean = visitors68.none { it == "Zork" }     // true



/*
HOW ITERABLES ARE USE GENERALLY
    An `Iterable` represents a sequence of elements that can be stepped through
    one by one. In this lab, you use them to manage rooms, turnstiles, and
    visitors.

    * ITERATION (THE `for` LOOP): The most common way to use an `Iterable` is to
      process each item in a loop.
```Kotlin
for (room in museum.rooms) {
    println(room.name)
}
```
    * TRANSFORMATION (`map`): You can use this to convert a collection of one
      type into another, such as getting a list of names from a list of rooms.
```Kotlin
val roomNames = rooms.map { it.name }
```
    * FILTERING (`filter`): This creates a new collection containing only the
      items that satisfy a specific condition.
```Kotlin
val fullRooms = rooms.filter { !it.hasCapacity() }
```
    * ACCUMULATION (`fold` or `reduce`): These are used to combine all elements
      into a single value, like building a string representation of the museum.


WHY ITERABLES MATTER FOR YOUR LAB
    * TURNSTILES: You are tasked with ordering turnstiles based on why they were
      added. This makes `List` (which is an `Iterable`) the ideal choice because
      it preserves insertion order.
    * GRAPH TRAVERSAL: When checking if a museum is "well-formed", you iterate
      through sets of unreachable rooms to format error strings.
    * CONCURRENCY: You will use `map` to launch multiple visitor threads
      simultaneously and then use `forEach` to call `start()` and `join()` on
      them.
* */













/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */