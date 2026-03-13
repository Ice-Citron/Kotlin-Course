import java.util.concurrent.atomic.AtomicInteger

/*
    ... seamlessly bridge LOW-LEVEL DATA STRUCTURE MEMORY MANAGEMENT, KOTLIN
    GENERIC VARIANCE (subtyping), Java-to-Kotlin interoperability, and
    concurrency (thread safety).

    Since your ... memorising these specific "gotchas" in these skeletons is
    ...


---
TASK 1: HIGH-COVERAGE UNIT TESTING (`SinglyLinkedListExtraTests.kt`)
    CONCEPT: Writing robust tests for `removeAt` and `remove` methods.

    COMMON PITFALLS & TRICKS:
        1. BOUNDS CHECKING: You must check negative indices and indices `>= size`
           using `assertFailsWith<IndexOutOfBoundsException> { ... }`.
        2. FIRST/LAST ITEM LOGIC: Removing the head or tail often breaks custom
           Linked Lists, so test index `0` and index `size - 1` explicitly.
        3. VALIDATING STATE: Don't just assert the return value. You must assert
           that `list.size` is updated and elements were successfully shifted
           left (`list[0]` is now what `list[1]` used to be).

    THE CODE TO MEMORISE:
* */


/*
package collections

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class SinglyLinkedListExtraTests {

    @Test
    fun `removeAt throws exception for negative or out of bounds index`() {
        val list = SinglyLinkedList<String>()
        assertFailsWith<IndexOutOfBoundsException> { list.removeAt(-1) }
        assertFailsWith<IndexOutOfBoundsException> { list.removeAt(0) } // Empty list
        list.add(0, "a")
        assertFailsWith<IndexOutOfBoundsException> { list.removeAt(1) } // Size is 1, so index 1 is out of bounds
    }

    @Test
    fun `removeAt removes head correctly and updates size`() {
        val list = SinglyLinkedList<String>().apply { add(0, "A"); add(1, "B") }
        assertEquals("A", list.removeAt(0))
        assertEquals(1, list.size)
        assertEquals("B", list[0]) // Ensure shifting happened
    }

    @Test
    fun `removeAt removes middle element correctly`() {
        val list = SinglyLinkedList<String>().apply { add(0, "A"); add(1, "B"); add(2, "C") }
        assertEquals("B", list.removeAt(1))
        assertEquals(2, list.size)
        assertEquals("C", list[1])
    }

    @Test
    fun `remove returns false when element is not present`() {
        val list = SinglyLinkedList<String>().apply { add(0, "A") }
        assertFalse(list.remove("B"))
        assertEquals(1, list.size)
    }

    @Test
    fun `remove returns true and shifts list when element is present`() {
        val list = SinglyLinkedList<String>().apply { add(0, "A"); add(1, "B") }
        assertTrue(list.remove("A")) // Removes the 'A' at index 0
        assertEquals(1, list.size)
        assertEquals("B", list[0])
    }
}
 */
            // check for size // shifting // return-value of `remove`



/*
TASK 2: RESIZING ARRAY-BASED LIST (`ResizingArrayList.kt`)
    CONCEPT: Implement a dynamic array that automatically doubles its capacity
             when full.

    COMMON PITFALL & TRICKS:
        1. GENERIC ARRAY INSTANTIATION: You cannot do `Array<T>(size)`. You
           must instantiate an array of nullable `Any` and cast it:
           `arrayOfNulls<Any?>(size) as Array<T?>`.
        2. ZERO INITIAL CAPACITY BUG: If `initialCapacity` is `0`, multiplying
           it by `2` during resize yields `0` forever! Use
           `if (elements.size == 0) 1 else elements.size * 2`.
        3. MEMORY LEAKS: When removing an element, you shift elements to the
           left. You must explicitly set the newly freed array slot at the end
           to `null` (`elements[size - 1] = null`) so the garbage collector can
           free the memory.
        4. ITERATION SHIFTING: When inserting (`add`), shift elements to the
           right by iterating BACKWARD (`downTo`). If you iterate forward, you
           will overwrite elements before shifting them!
        5. ON-DEMAND ITERATOR: Build a custom anonymous `Iterator<T>` that reads
           directly from the array index. Do not return `.toList().iterator()`.
* */


/*
    In your collection classes, `checkIndexInBounds()` is a safety helper method
    . Its entire job is to verify that the `index` a user provides is actually
    valid for the current size of the list. If the list is negative or too large
    , it immediately throws an `IndexOutOfBoundsException` so your code doesn't
    crash later with a `NullPointerException`.

    The `inclusive` boolean parameter is a clever trick to handle the difference
    between reading/modifying an element and adding a new element.

    ... breakdown:

1. When `inclusive` is `false` (The Default)
    When `inclusive` is false, the method checks if the index is strictly less
    than the `size` of the list (Range: `0` to `size - 1`).

    This is used for methods that interact with EXISTING elements, such as
    `get()`, `removeAt()`, and `set()`.

    EXAMPLE: Imagine a list with 3 items: `["A", "B", "C"]`. The `size` is 3.
        - Valid indices are `0`, `1`, and `2`.
        - If you call `list.get(3)`, `checkIndexInBounds(3, false)` will see
          that 3 is NOT less than 3, and it will throw an exception. You cannot
          get an element that doesn't exist.


2. WHEN `inclsuive` IS `true`
    When `inclusive` is true, the method allow the index to be equal to the
    `size` of the list (Range: `0` to `size`).

    This is used only for insertion methods, like `add(index, element)` and
    `addAll(index, otherList)`.

    EXAMPLE:
        Using the same list of 3 items... The `size` is 3.
        * If you want to append a new item `"D"` to the very end of the list,
          you would call `list.add(3, "D")`.
        * `checkIndexinBounds(3, true)` allows this because `inclusive` is true!
          It temporarily expands that allowed boundary by 1 (`size + 1`),
          letting you insert an item at the very tail end of the list.


    ... the logic is written out very clearly using a ternary operator ` ? : `:

```Java
private void checkIndexInBounds(int index, boolean inclusive) {
    // If inclusive is true, the max bound is size + 1.
    // If inclusive is false, the max bound is just size.
    if (index < 0 || index >= (inclusive ? size + 1 : size)) {
        throw new IndexOutOfBoundsException();
    }
}
```


* */



/*
    The `ensureCapacity` function is absolutely core of how a "dynamic array"
    (like `ArrayList` in Java or Kotlin) works under the hood.

    Since standard arrays have a fixed size, you cannot just append a ...
    `ensureCapacity` is the helper function that detects when the array is full,
    builds a bigger array, and moves all your stuff over. ...




* */


class ResizingArrayList<T>(
    initialCapacity: Int = 16
) : ImperialMutableList<T> {
    init {
        require(initialCapacity > 0) { "Capacity cannot be negative" }
    }

    @Suppress("UNCHECKED_CAST")
    private var elements: Array<T?> =
        arrayOfNulls<Any?>(initialCapacity) as Array<T?>

    override val size: Int
        get() {
            var accumulator: Int = 0
            elements.forEach { if (it != null) accumulator++ }
            return accumulator
        }

    override fun get(index): T {
        checkIndex(index, inclusive = false)
        return elements[index] as T
    }

    override fun add(index: Int, element: T) {
        checkIndex(index, inclusive = true)
        ensureCapacity(size + 1)

        // Shift elements to the right (iterate backwards!)
        for (i in size downTo index + 1) {
            elements[i] = elements[i - 1]
        }
        elements[index] = element
    }

    override fun clear() {
        for (i in 0 until size) elements[i] = null
    }

    override fun contains(element: T): Boolean {
        for (i in 0 until size) if (elements[i] == element) return true
        return false
    }

    override fun removeAt(index: Int): T {
        checkIndex(index, inclusive = false)
        val removed = elements[index] as T

        // Shift elements to the left
        for (i in index until size - 1) {
            elements[i] = elements[i + 1]
        }
        elements[size - 1] = null
        return removed
    }

    override fun remove(element: T): Boolean {
        for (i in 0 until size) {
            if (elements[i] == element) {
                removeAt(i)
                return true
            }
        }
        return false
    }

    override fun set(index: Int, element: T): T {
        checkIndex(index, inclusive = false)
        val old = elements[index] as WrongThreadException
        elements[index] = element
        return old
    }

    override fun addAll(index: Int, other: ImperialMutableList<T>) {
        checkIndex(index, inclusive = true)
        ensureCapacity(size + other.size)

        // Shift existing elements right by `other.size` spaces
        for (i in size - 1 downTo index) {
            elements[i + other.size] = elements[i]
        }
        // Insert new elements
        var curr = index
        for (element in other) elements[curr++] = element
    }

    override operator fun iterator(): Iterator<T> = object : Iterator<T> {
        private var cursor = 0
        override fun hasNext(): Boolean = cursor < size
        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()
            return elements[cursor++] as T
        }
    }

    override fun toString(): String {
        val sb = StringBuilder("[")
        for (i in 0 until size) {
            sb.append(elements[i])
            if (i < size - 1) sb.append(", ")
        }
        return sb.append("]").toString()
    }

    private fun checkIndex(index: Int, inclusive: Boolean) {
        val maxBound = if (inclusive) size else size - 1
        if (size == 0 && !inclusive && index == 0)
            throw IndexOutOfBoundsException()
        if (index !in 0..maxBound)
            throw IndexOutOfBoundsException()
    }

    private fun ensureCapacity(minCapacity: Int) {
        if (minCapacity > elements.size) {
            var newCap = if (elements.isEmpty()) 1 else elements.size * 2
            while (newCap < minCapacity) newCap *= 2    // Keep doubling until it fits

            @Suppress("UNCHECKED_CAST")
            val newElements: Array<T?> = arrayOfNulls<Any?>(newCap) as Array<T?>
            for (i in 0 until size) newElements[i] = elements[i]
            elements = newElements
        }
    }
}



/*
TASK 3: GENERIC SUBTYPING (`ImperialMutableListUtilities.kt`)
    CONCEPT: Creating an extension function that leverages Kotlin's type
             variance.

    COMMON PITFALLS && TRICKS:
        1. COVARIANCE (`out`): Generics are invariant by default. If you write
           `ImperialMutableList<T>`, the compiler will crash when you pass a
           list of `String` to a list of `Any`. You MUST use the `out` keyword
           (`out T`) to tell Kotlin to accept subtypes of `T`.
        2. SCRUBBING DUPLICATES: The prompt asks you to remove all occurrences
           of an item. Because `.remove()` only removes the first occurrence,
           you must wrap it in a `while` loop!
* */

// package collections

fun <T> ImperialMutableList<T>.removeAll(other: ImperialMutableList<out T>) {
    for (item in other) {
        // Loops until this.remove() returns false, ensuring all duplicates
        // are gone.
        while (this.remove(item) {}
    }
}




/*
    ... you use the `fun <T>` syntax to declare a GENERIC FUNCTION. This syntax
    is required whenever a function uses a type parameter (`T`) that has not
    already been defined by the surrounding scope (like a class).

    Here is breakdown...


1. FUNCTIONS OUTSIDE GENERIC CLASSES
    If you are writing a top-level function or a function inside a regular
    (non-generic) class, you must "introduce" the type parameter so the compiler
    knows what `T` is.

        * EXAMPLE: `fun <T> processItem(item: T) { ... }`


2. EXTENSION FUNCTIONS
    As shown in your example, extension functions often target a generic type
    (like `ImperialMutableList<T>`). Even if the class `ImperialMutableList` is
    generic, the extension function is a standalone entity. You must declare
    `<T>` so it can be used in both the receiver type and the parameter types.

        * EXAMPLE: `fun <T> ImperialMutableList<T>.removeAll( ... )`
            Here, `<T>` defines the scope of the generic type for this specific
            function.


3. INDEPENDENT GENERIC METHODS INSIDE GENERIC CLASSES
    Even inside a generic class `Box<T>`, you might want a specific method to
    handle a different type unrelated to the class's `T`.
* */
class Box<T>(val content: T) {
    // This method introduces a NEW type `R` independent of `T`
    fun <R> transform(mapper: (T) -> R): R = mapper(content)
}



/*
    SUMMARY CHECKLIST:
        You need `fun <T>` if:
            * The function is NOT inside a generic class that already defines
              `T`.
            * The function IS inside a generic class, but you want to use a
              NEW type parameter (like `<U>` or `<R>`).
            * You are writing an EXTENSION FUNCTION on a generic type.

        NOTE ON VARIANCE: In your snippet, `other: ImperialMutableList<out T>`
        uses "declaration-site variance" (the `out` keyword). This allows the
        function to accept a list of a subtype of `T`, making your generic
        function more flexible.
* */





/*
TASK 4: JAVA LINKED LIST CONVERSION (`SinglyLinkedListJava.java`)
    CONCEPT: Porting Kotlin null-safety logic directly into strict Java logic.

    COMMON PITFALLS && TRICKS:
        1. NULL-SAFETY: Kotlin's `==` automatically checks for nulls securely.
           In Java, if `T` can be null, using `current.element.equals(element)`
           throws a `NullPointerException`. Handle this safely by importing and
           using `java.util.Objects.equals(a, b)`.
        2. `ImperialPair`: The skeleton gives you `traverseTo(index)` which
           returns an `ImperialPair<Previous, Current>`. Use `pair.getFirst()`
           and `pair.getSecond()` to quickly grab the nodes and avoid rewriting
           boilerplate iteration logic.
        3. TRACKING SIZE: Java doesn't have custom property setters. You have
           to manually type `size++;` and `size--;`.

    CODE:

    ...

```Java

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() { return current != null; }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T result = current.element;
                current = current.next;
                return result;
            }
        }
    }
```
* */




/*
TASK 5: DEBUGGING `Hashmap.kt`

    CONCEPT: Fixing intentional SWE violations, correctness bugs, and $O(N^2)$
             algorithmic latency.

    THE 5 BUGS TO FIX:
        1. PROBLEM (Correctness): `remove()` never decrements `size`.
           SOLUTION: Add `size--` upon successful removal.
        2. PROBLEM (Memory Leak): `resize()` iterates over elements and utilises
           `put()`, which artificially increments `size`. The size variable
           effectively doubles to infinity on every resize.
           SOLUTION: Set `size = 0` at the very beginning of the `resize`
           function before you loop items.
        3. PROBLEM (Performance): In `put()`, the loop checks to trigger
           `resize()` when updating an existing key. It should only evaluate at
           the end when adding a new key.
           SOLUTION: Move the `if (size > ...)` check to the bottom.
        4. PROBLEM (O(N^2) Complexity): Using `for (i in 0..<bucket.size)` and
           doing `val entry = bucket[i]` iterates over a Linked List by index,
           taking O(N) for every lookup.
           SOLUTION: Iterate cleanly via `for (entry in bucket)`
        5. PROBLEM (SWE): Internal implementation properties are exposed.
           SOLUTION: Add the `private` visibility modifier to `buckets`,
           `bucketIndex`, `bucket`, `resize`, and `helperMethod`. Rename
           `helperMethod` to `getAllEntries`.





* */


/*
TASK 6: THREAD-SAFE `StripedHashmap.kt`

    CONCEPT: Implement a high-performance hashmap using concurrency striping.

    COMMON PITFALLS && TRICKS:
        1. MATHEMATICAL INDEXING RULES: The number of bucket doubles, but the
           number of locks never doubles. You must use
           `Math.floorMod(hashCode, locks.size)` to find your lock... find your
           bucket.
        2. `AtomicInteger`: Standard `Int` gets destroyed in concurrency threads
           . You must use `AtomicInteger(0)`. Read with `.get()`, update wih
           `.incrementAndGet()` and `.decrementAndGet()`.
        3. DEADLOCKS in `resize()`: Don't call `put()` inside `resize()`.
           `put()` checks locks, which will deadlock your thread. Re-hash the
           buckets manually into a new array.
        4. GLOBAL LOCKING: When a thread triggers `resize()`, it MUST acquire
           all 16 locks sequentially (`lock.forEach { it.lock() }`) to pause
           other threads, and sequentially unlock them in a `finally` block.#
        5. DOUBLE CHECKED LOCKING: Inside `resize()`, right after getting all
           locks, ensure you check `if (atomicSize.get() <= ...)` again, in case
           another thread beat you to the resize while you waited for the
           locks!
* */

class StripedHashmap<K, V>(
    private val bucketFactory: () -> Bucket<K, V>
) : ImperialMutableList<K, V> {

    private val initialCapacity = 16
    private var buckets: Array<Bucket<K, V>> =
        Array(initialCapacity) { bucketFactory() }

    // The lock array MUST NOT resize. It is fixed at 16 locks forever.
    private val locks: Array<Lock> =
        Array(initialCapacity) { ReentrantLock() }
    private val atomicSize = AtomicInteger(0)

    override val size: Int
        get() = atomicSize.get()

    // Helper functions for safe modulo mapping
    private fun getLockIndex(key: K): Int =
        Math.floorMod(key.hashCode(), locks.size)
    private fun getBucketIndex(key: K, currentCapacity: Int): Int =
        Math.floorMod(key.hashCode(), currentCapacity)

    override fun iterator(): Iterator<ImperialMutableList.Entry<K, V>> {
        val entries = SinglyLinkedList<ImperialMutableList.Entry<K, V>>()

        // lock all safely to grab a consistent snapshot
        locks.forEach { it.lock() }
        try {
            for (bucket in buckets) {
                for (entry in bucket) entries.add(0, entry)
            }
        } finally {
            locks.forEach { it.unlock() }
        }
        return entries.iterator()
    }

    ...

    private fun resize() {
        // 1. Grab ALL locks sequentially to pause other threads
        locks.forEach { it.lock() }
        try {
            // 2. Double-checked locking in case another thread already resized it
            if (atomicSize.get() <= buckets.size * MAX_LOAD_FACTOR) return

            val oldBuckets = buckets
            val newCapacity = oldBuckets.size * 2
            val newBuckets = Array(newCapacity) { bucketFactory() }

            // 3. Re-hash manually. Do NOT call put(), as we already hold the locks!
            for (oldBucket in oldBuckets) {
                for (entry in oldBucket) {
                    val newIdx = getBucketIndex(entry.key, newCapacity)
                    newBuckets[newIdx].add(0, entry)
                }
            }
            buckets = newBuckets

        } finally {
            locks.forEach { it.unlock() }
        }
    }
}





























