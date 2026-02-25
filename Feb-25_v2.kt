import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/*
3. OVERRIDING PROPERTIES and `super`
    Just like you can override functions in a subclass, you can override `val`
    and `var` properties to inject custom logic whenever a property is accessed
    (`get`) or changed (`set`),

    EXPLAIN BY EXAMPLE:
* */
open class Cell20(open var value: Int)

class BackedUpCell20(value: Int) : Cell20(value) {

    private val history: MutableList<Int> = mutableListOf()

    // Overriding the property!
    override var value: Int
        get() = super.value     // Read the parent's actual value
        set(newValue) {
            history.add(0, super.value) // Save the old value first!
            super.value = newValue
        }
}


/*
    - WHEN DO I USE THEM: When a subclass needs to track, validate, or react to
     changes happening to a property it inherited from a parent class.
    - WHY DO I USE THEM: It allows you to add powerful features (like undo
      histories, validation checks, or UI updates) while keeping the clean
      syntax of simply typing `cell.value = 5`.
    - WHEN DO I NOT USE THEM AND WHY: Never forget the `super` keyword! If you
      write `value = newValue` inside the setter, the setter will recursively
      call itself forever until the app crashes with a `StackOverflowError`. You
      must route the data to the parent's backing field using `super`.
    - WHY THEM OVER OTHER USE CASES: In Java, fields (variables) cannot be
      overridden, which forces developers to write bloated `getValue()` and
      `setValue()` methods everywhere. Kotlin handles this gracefully at the
      language level.
* */





/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
4. ALTERNATIVES TO INHERITANCE: The Strategy Pattern
    Inheritance (creating subclasses) is rigid. Sometimes, instead of creating a
    massive tree of abstract classes and subclasses, it is vastly cleaner to
    abandon inheritance entirely and just pass a function into a generic class
    to tell it how to behave.
* */

// THE OLD WAY: Rigid Inheritance
abstract class GridWorld2 {
    abstract fun handleOverrun(pos: Pair<Int, Int>): Pair<Int, Int>
}
class DeadlyGridWorld2 : GridWorld2() {
    override fun handleOverrun(pos: Pair<Int, Int>): Pair<Int, Int> = throw Exception("You died!")
}
class BouncingGridWorld2 : GridWorld2() {
    override fun handleOverrun(pos: Pair<Int, Int>): Pair<Int, Int> = Pair(0, 0)
}


// THE NEW WAY: The Strategy Pattern (Passing behavior as a variable)
class GridWorld3(
    val handleOverrun: (Pair<Int, Int>) -> Pair<Int, Int>
    // <-- The Strategy (a function property)
)

fun main73() {
    // We create drastically different worlds using the EXACT SAME class via factories!
    val deadlyWorld: GridWorld3 = GridWorld3(handleOverrun = { throw Exception("Fell off the map!") })
    val boundedWorld: GridWorld3 = GridWorld3(handleOverrun = { position -> Pair(0, 0) })
}

/*
    - WHEN DO I USE THEM: When you have a single class that needs to behave in a
      few different ways, but creating a whole new subclass file for each
      variation feels like overkill.
    - WHY DO I USE THEM: "Composition over Inheritance." It prevents "Class
      Explosion" (ending up with 50 different subclass files). Furthermore,
      because the strategy is just a `var`, you can dynamically swap strategies
      while the application is running. You cannot change an object's inherited
      class at runtime!
    - WHEN DO I NOT USE THEM AND WHY: If `DeadlyGridWorld` requires entirely
      different internal variables, highly complex private helper methods, and
      unique state tracking compared to `BouncingGridWorld`, a simple strategy
      function won't cut it. You should use standard `abstract`/`open`
      subclasses.
    - WHY THEM OVER OTHER USE CASES: Languages without first-class functions
      (like older Java) require you to create entire interface files and
      anonymous inner class just to pass a single behavior. Kotlin's lambda
      syntax `{ ... }` makes the Strategy Pattern incredibly lightweight and
      readable.
* */













/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    ... senior-level backend engineering territory now.

    Combining standard library data structures (Iterators), thread safety
    (Locks), and functional idioms (Scope Functions) into a single, unified
    architecture is exactly what professional Kotlin development looks like.

    As requested, I have removed the syntax hints. You will have to rely purely
    on your architectural instincts.

    Before we hit the gauntlet, let's demystify exactly what an Iterator is.


---
HOW ITERATORS ACTUALLY WORK
    An `Iterator` is an object that acts as a CURSOR pointing to a space between
    elements in a collection (like a `List` or `Set`). It abstracts away how
    the data is stored, giving you a uniform way to traverse it.

    It relies on a strict mathematical contract of two methods:
        1. `hasNext(): Boolean` -- Asks, "Is there an item ahead of my cursor?"
        2. `next(): T` -- Says, "Give me the item ahead of my cursor, AND step
           the cursor forward."

THE "FOR-LOOP" ILLUSION
    When you write a standard loop: `for (item in list) { ... }`, the Kotlin
    compiler secretly rewrites it into this:

```Kotlin
val iterator = list.iterator()
while (iterator.hasNext()) {
    val item = iterator.next()
    // ... your code ...
}
```


THE `MutableIterator` UPGRADE:
    Standard iterators are read-only. Kotlin also has `MutableIterator`, which
    adds a third method: `remove()`. This safely deletes the last item returned
    by `next()` from the underlying collection.


THE `ConcurrentModificationException` TRAP:
    Standard iterators are famously "fail-fast." If Thread A is looping through
    a list, and Thread B adds or removes an item from that list, Thread A's
    iterator will instantly crash with a `ConcurrentModificationException`.
    The cursor gets confused because the list's internal size changed
    unexpectedly.


THE FIX:
    1. If you need to remove items while looping, you cannot use `list.remove()`
       . You must use a `MutableIterator` and call ITS `iterator.remove()`
       method.
    2. In a multithreaded environment, you must wrap the entire `while` loop
       inside a lock so no other thread can touch the list until the iteration
       is finished.
* */













/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
// THE HARDCORE PRACTICE GAUNTLET

class TaskQueue(
    val queue: MutableList<String>,
    private val lock: ReentrantLock = ReentrantLock()
) {
    fun processUrgentTasks() {
        lock.withLock {
            val iter: MutableIterator<String> = queue.iterator()
            while (iter.hasNext()) {
                val current: String = iter.next()
                if ("URGENT" in current)            // current.contains("URGENT")
                    iter.remove()
                    println("[Worker] Processed and removed: $current.")
            }
        }
    }

}












/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
QUESTION 2: DEADLOCK-FREE ZIPPING (`run` + `with` + MULTIPLE ITERATORS)
    THE SCENARIO: Two nodes in a server cluster need to merge their log streams.
    You must pull logs from Node A to Node B simultaneously, pairing them
    together.
* */

class Node(
    val id: Int,
    val logs: List<String>,
    val lock: ReentrantLock = ReentrantLock()
) {


}

fun zipLogs(nodeA: Node, nodeB: Node) {
    val (n1, n2) =
        if (nodeA.id < nodeB.id) Pair(nodeA, nodeB) else Pair(nodeB, nodeA)

    n1.lock.withLock {
        n2.lock.withLock {
            val iterA = nodeA.logs.iterator()
            val iterB = nodeB.logs.iterator()
            val builder: StringBuilder = StringBuilder()
            with (builder) {
                while (iterA.hasNext() && iterB.hasNext()) {
                    append("Matched A: [${iterA.next()}] " +
                           "with B: [${iterB.next()}]\n")
                }
            }
            println(builder.toString())
        }
    }
}











/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
// QUESTION 3: The Custom Paginated Iterator

class PaginatedIterator(
    val fetchStrategy: () -> List<String>,
    private val buffer: MutableList<String> = mutableListOf(),
    private val lock: ReentrantLock = ReentrantLock()
) : Iterator<String> {

    override fun hasNext(): Boolean {
        return lock.withLock {
            // `run` encapsulates the buffer-filling logic
            run {
                if (buffer.isEmpty()) {
                    println("Buffer empty. Calling API for next chunk...")
                    fetchStrategy().forEach { buffer.add(it) }
                }
                buffer.isNotEmpty()
            }
        }
    }

    override fun next(): String {
        lock.withLock {
            if (this.hasNext()) {
                return buffer.removeAt(0)   // Pop from the front
            } else {
                // `let` gracefully throws an exception if the API is totally emptyu
                "No more data". let { throw NoSuchElementException() }
            }
        }
    }
}




/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
// QUESTION 4: The Snapshot Iterable (`Iterable` + `apply`)




















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */








/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */

/*
--- In Kotlin, the `.forEach()` method iterates over elements in a collection
    (array, list or set) and performs the provided action on each element.


--- `forEach()` function allows looping through array items without storing
    outcomes. It is commonly used for displaying values or modifying a user
    interface. Alternatively, the `.map()` function is used to modify array
    elements and create a new array with the modified values.


--- A `StringBuilder` is essentially a high-performance text buffer. Unlike a
    standard `String`, which is entirely immutable (meaning every time you
    change it, the system destroys the old one and creates a brand new one in
    memory), a `StringBuilder` modifies the exact same block of memory until
    you are completely done.

    KEY ATTRIBUTES
        A `StringBuilder` has two primary attributes that dictate how it manages
        memory:
        - `length`: The actual number of characters currently stored inside the
          builder.
        - `capacity`: The totall amount of memory currently reserved by the
          builder. It always starts larger than the text it holds so it has
          "room to grow" without needing to ask the system for more memory.

    COMMONLY USED METHODS
    - `append(value)`
        * WHAT IT DOES: Adds text, numbers, or objects o the very end of the
          current sequence.
        * WHY YOU USE IT: This is the bread and butter. You use it to
          sequentially build up a document, log, or data payload piece by piece.
    - `insert(index, value)`
        * WHAT IT DOES: Wedges new text into the middle of the existing sequence
          at the specified index.
        * WHY YOU USE IT: Used when you need to prefix something or inject
          missing data into a string you've already started building.
    - `delete(start, end)`
        * WHAT IT DOES: Removes a specific chunk of characters between two index
          points.
        * WHY YOU USE IT: Used to trim off trailing commas, remove mistakes, or
          clean up formatting without rebuilding the string.
    - `replace(start, end, str)`
        * WHAT IT DOES: Swaps out a specific section of text with new text.
        * WHY YOU USE IT: Used for template injection or replacing placeholder
          values dynamically.
    - `reverse()`
        * WHAT IT DOES: Flips the entire sequence of characters backwards in
          place.
        * WHY YOU USE IT: Used in algorithmic challenges or specified data
          parsing tasks where reading from right-to-left is required.
    - `clear()`
        * WHAT IT DOES: Empties the builder entirely, resetting the `length` to
          0.
        * WHY YOU USE IT: Used when you want to reuse the exact same memory
          buffer for a new task instead of creating a new `StringBuilder` object
    - `toString()`
        * WHAT IT DOES: Converts the mutable buffer into a final, immutable
          `String`.
        * WHY YOU USE IT: Used as the very last step when you are done modifying
          and need to pass the final text to a standard function.


    WHEN TO USE A `StringBuilder` (and Why)
        You should reach for a `StringBuilder` (or Kotlin's
        `buildString { ... }`) in these specific scenarios:

        - LOOPS: If you are concatenating strings inside a `for` or `while` loop
          (e.g., iterating through a large dataset to build a CSV file). Doing
          this with the standard `+` operator creates a massive amount of
          garbage data in memory, which triggers the Garbage Collector and slows
          down your application.
        - COMPLEX PROGRAMMATIC TEXT: When you are building a large block of text
          where the structure depends heavily on `if/else` conditions (like
          generating an HTML report or a dynamic SQL query).
        - HEAVY MANIPULATIONS: When your logic requires constant inserting,
          deleting, or reversing of characters.

    When NOT to Use a StringBuilder (and Why)
        You should avoid `StringBuilder` in these situations:

        - SIMPLE ONE-LINERS: If you just need to combine a few variables into a
          single sentence, use Kotlin's String Templates
          (e.g., `val msg = "User $name logged in at $time"`). Under the hood,
          the compiler automatically optimises this into a builder for you.
          Writing out a `StringBuilder` manually here just makes your code ugly
          and harder to read.
        - MULTI-THREADED ENVIRONMENTS: A standard `StringBuilder` is NOT
          THREAD-SAFE. If two different background threads try to `append()`
          to the same builder at the exact same millisecond, the internal
          character array will corrupt. If you absolutely must share a builder
          across threads, you have to use a `StringBuffer` (which is the
          synchronized, thread-safe, but slower cousin) or protect it with a
          `ReentrantLock`.


* */


/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    In Kotlin, `Iterable` is the "mother of all collections." If you are working
    with a `List`, a `Set`, or any group of items you can loop through, you are
    working with an `Iterable`.

    At its core, the `Iterable` interface actually only requires one single
    thing: a method called `iterator()` that returns an `Iterator` object. This
    iterator knows how to point to the current item and move to the `next()`
    one.

    However, the real power of `Iterable` comes from the massive library of
    EXTENSION FUNCTIONS Kotlin provides out of the box. These functions allow
    you to manipulate data in a clean, functional style.


COMMONLY USED METHODS (EXTENSION FUNCTIONS)
    Here is the breakdown of the heavy hitters you will use daily, grouped by
    what they do:

1. TRANSFORMATION (CHANGING THE DATA)
    - `map { ... }`
        * WHAT IT DOES: Applies a function to every element and returns a new
          list with the results.
        * WHY YOU USE IT: Used when you need to convert a list of one type to
          a list of another (e.g., extracting just the IDs from a list of user
          objects).
    - `flatMap { ... }`
        * WHAT IT DOES: Like `map`, but if your transformation returns lists, it
          flattens them all into a single, 1D lists.
        * WHY YOU USE IT: Used when you have a list of items that each contain
          their own lists, and you want everything in one flat pool.
    - `mapNotNull { ... }`
        * WHAT IT DOES: Transforms elements but automatically drops any results
          that evaluate to `null`.
        * WHY YOU USE IT: Used to clean up data while transforming it, avoiding
          `null` crashes later.


2. FILTERING (TRIMMING THE DATA)
    - `filter { ... }`
        * WHAT IT DOES: Returns a new list containing only elements that match
          your condition (evaluate to `true`).
        * WHY YOU USE IT: Used to sift through data, like finding all active
          users or scores above 90.
    - `filterNotNull()`
        * WHAT IT DOES: Strips out all `null` values from your collection.
        * WHY YOU USE IT: Used as a quick safety mechanism before performing
          operations that require actual values.


3. SEARCHING & CHECKING (Querying the data)
    - `find { ... }` / `firstOrNull { ... }`
        * WHAT IT DOES: Returns the very first element matching your condition
        * WHY YOU USE IT: Used when you only need one specific item from a
          dataset and want to stop looking once you find it.
    - `any { ... }` / `all { ... }`
        * WHAT IT DOES: Returns a single `true` or `false`. `any` checks if
          every single one matches.
        * WHY YOU USE IT: Used for quick validation checks (e.g., "Are all
          servers online?")


4. AGGREGATION (Summarising the data)
    - `sumOf { ... }`
        * WHAT IT DOES: Adds up the numeric results of the block for every
          element.
        * WHY YOU USE IT: Used for totals (e.g., calculating the total size
          of files in a folder)
    - `fold(initial) {acc, item -> }`
        * WHAT IT DOES: Starts with an initial value, then loops through the
          iterable, accumulating a final result based on your logic.
        * WHY YOU USE IT: Used for complex aggregations where simple addition
          isn't enough.




---
WHEN TO USE ITERABLE FUNCTIONS (and Why)
    You should rely heavily on these extension functions when:

    - WRITING DECLARATIVE CODE: You want your code to read like a sentence
      ("Filter these items, map them to strings, and group them"). It is vastly
      easier to read them than nested `for` loops.
    - AVOIDING MUTABILITY: Most `Iterable` functions return a brand new list.
      This is mathematically safer because you aren't modifying existing
      variables, which prevents side effects and race conditions in
      multi-threaded environments.


WHEN NOT TO USE ITERABLE FUNCTIONS (and Why)
    You should avoid chaining `Iterable` functions in these specific situations:

    - MASSIVE DATASETS WITH MULTIPLE CHAINED CALLS: This is the biggest trap.
      Every time you call `filter`, `map`, or `toList` on an `Iterable`, Kotlin
      creates a completely new list in memory. If you have 100,000 items and do
      `list.filter{}.map{}.sorted()`, you just created three massive, temporary
      lists. This causes memory spikes and triggers the GC.
    - SIMPLE ARRAY INDEX MANIPULATION: If you need to heavily rely on the
      specific index position of items, or if you need to swap items back
      and forth based on their indices (like a sorting algorithm), standard
      `for (i in indices)` loops or `Array` manipulations are faster and more
      appropriate.
    - MUTATING WHILE ITERATING: If you need to remove items from a list while
      you are looping through it, `Iterable` methods will fail or throw a
      `ConcurrentModificationException`. You need a `MutableIterator` for that.
* */




/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
// QUESTION 4: The Snapshot Iterable (`Iterable` + `apply`)



// class Player2(val name: String)

class PlayerTracker(
    private val set: MutableSet<String>,
    private val lock: ReentrantLock = ReentrantLock()
) : Iterable<String> {

    fun addPlayer(name: String) = lock.withLock { set.add(name) }

    // Overriding this allows client to write: `for (player in tracker) { ... }`
    override operator fun iterator(): Iterator<String> {
        lock.withLock {
            set.toList().apply {        // O(N) operation to create a safe clone
                                // `apply` logs the snapshot creation, and seamlessly return the clone
                println("Snapshot generated with ${this.size} players")
                return this.iterator()
            }
        }
    }
}
/*
            ...

            WHY `.toList()` WINS HERE
                Because the function signature strictly returns an
                `Iterator<String>`, the caller doesn't actually care (and won't
                even know) what underlying collection type the snapshot is. They
                just want to loop through it from start to finish. Since the
                original `players` collection is already a `Set` (meaning
                uniqueness is already guaranteed), paying the extra memory cost
                to create another hash-based Set for the snapshot is
                unnecessary overhead.

                In short: `.toSet()` is semantically correct, but `.toList()` is
                a leaner, faster vehicle for returning a simple iterator!
* */





/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    ... successfully architecting Deadlock-free zipping and a custom Paginated
    Iterator means you have essentially mastered the practical, industry-level
    concepts of this module. You are definitely ready for the real world!

    ... From Kotlin to Java.

    Instead of teaching you how to write code, the first half of this lecture
    takes you under the hood to explain HOW THE COMPUTER ACTUALLY RUNS YOUR
    CODE. It then uses that foundation to introduce the syntax differences
    between Kotlin and Java.

    ...


---
1. STATIC COMPILATION vs. INTERPRETATION
    Before understanding Kotlin, you must understand the two traditional ways
    programming languages talk to a CPU.

EXPLAIN BY EXAMPLE:
    - STATIC COMPILATION (C/C++): A compiler translates your entire
      human-readable code directly into raw binary `0`s and `1`s (Machine Code)
      for your specific CPU before you run it. (This creates an `.exe` file
      on Windows).
    - INTERPRETATION (Python/JavaScript): There is no pre-compilation. An
      Interpreter program reads your source code line-by-line while the app is
      running, translating and executing it on the fly.
    * WHEN DO I USE THEM: You don't actively choose this while coding, but you
      choose the language based on it. Use Compiled languages for heavy 3D games
      or OS kernels. Use Interpreted languages for quick data science scripts
      or web automation.
    * WHY DO I USE THEM: Compilation is blazingly fast and catches type errors
      before the app runs. Interpretations gives instant feedback (no waiting
      for massive builds to finish) and high portability (the exact same
      Python script runs on Mac and Windows).
    * WHEN DO I NOT USE THEM AND WHY: Do not use strictly compiled code if you
      want users on completely different architectures (Intel vs. Apple Silicon)
      to run your app effortlessly. You'd have to compile a different binary
      file for every chip type. Do not use interpreted code for heavy math or
      physics engines, as translating line-by-line in real-time is extremely
      slow.


---
2. Just-In-Time (JIT) Compilation & The JVM
    Kotlin and Java use a hybrid approach called JUST-IN-TIME (JIT) COMPILATION
    to get the best of both worlds.

    EXPLAIN BY EXAMPLE:
    1. STATIC STEP: When you compile Kotlin (`kotlinc`), it does not create
       machine code. It create an intermediate, universal language called
       BYTECODE.
    2. DYNAMIC STEP: When you run the app, the JVM steps in. It starts out
       interpreting the Bytecode so your app opens instantly.
    3. JIT STEP: If the JVM notices you are running a specific `while` loop
       thousands of times (a "Hotspot"), the JIT COMPILER activates in the
       background and physically compiles that loop into native machine code
       for a massive speed boost.



When do I use them: Whenever you write Kotlin, Java, or Scala, you are relying on a JIT Virtual Machine.
                        https://gemini.google.com/app/05319e773afe7230


* */












/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */












/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */














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
