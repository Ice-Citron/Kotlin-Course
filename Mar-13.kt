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

* */





/*
TASK 4: JAVA LINKED LIST CONVERSION (`SinglyLinkedListJava.java`)
* */























