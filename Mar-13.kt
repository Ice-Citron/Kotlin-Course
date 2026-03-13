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

1. When

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
        return eleemnts[index] as T
    }


}





























