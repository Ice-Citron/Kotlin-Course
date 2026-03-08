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

class MuseumRoom(
    val name: String,
    val capacity: Int,
    private val lock: ReentrantLock = ReentrantLock(),
) {
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


