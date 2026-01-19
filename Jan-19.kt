


/*
(\o/)___________________________________________________________(\o/)
(/|\)                                                           (/|\)
  |                                          .-~~~-.              |
  |                                        /        }             |
  |                                       /      .-~              |
  |                             \        |        }               |
  |             __   __       ___\.~~-.-~|     . -~_              |
  |            / \./  \/\_       { O |  ` .-~.    ;  ~-.__        |
  |        __{^\_ _}_   )  }/^\   ~--~/-|_\|   :   : .-~          |
  |       /  /\_/^\._}_/  //  /     /   |  \~ - - ~               |
  |      (  (__{(@)}\__}.//_/__A__/_A___|__A_\___A______A_____A   |
  |       \__/{/(_)\_}  )\\ \\---v-----V----v----v-----V-----v--- |
  |         (   (__)_)_/  )\ \>                                   |
  |          \__/     \__/\/\/                                    |
  |             \__,--'                                           |
  |                                                               |
(\o/)___________________________________________________________(\o/)
(/|\)                                                           (/|\)
* */

/*
    3. SETTING UP THE CLASS & CONSTRUCTORS

    Here, we define the class structure. Kotlin has a specific way of handling
    constructors compared to Java/C++.
* */
// Generics <T>: This list can hold Integers, Strings, anything.
class ResizingArrayList<T>(private val initialCapacity: Int) {
    // ^^^ PRIMARY CONSTRUCTOR defined in the class header

    // The "init" block runs immediately after the primary constructor.
    // It's like the body of a C++ constructor.
    init {
        if (initialCapacity < 0) throw IllegalArgumentException()
    }

    // SECONDARY CONSTRUCTOR
    // If the user doesn't provide a size, we default to 16.
    // `this(...)` calls the primary constructor above.
    constructor() : this(16)


    /*
    ANALOGY:
    - C++: The `init` block is your constructor body `{ ... }`. The
    `primary constructor` properties (like `initialCapacity`) are like your
    Initializer List `: initialCapacity(val)` <-- C++.



    -=-====--==---------|--<>
    4. INTERNAL STATE (THE "BACKING" FIELDS)

    We need two variables to track our list:
    1. `size`: The number of actual items the user thinks are in the list (e.g.,
       3 items).
    2. `elements`: The actual array holding the data (e.g., capacity 10).
* */

    var size: Int = 0

    // The backing array. It
    private var elements: Array<T?> = clearedArray()

    private fun clearedArray(): Array<T?> =
        arrayOfNulls<Any?>(initialCapacity) as Array<T?>

    /*
    NOTE: The slide uses `arrayOfNulls` or a custom helper because you cannot do
    `new T[]` in generics due to Type Erasure (similar to Java).



    ----=--=-=------|---<>
    5. READING DATA: `get()`

    This implementation mimics an array access but adds safety.
    * */
    fun get(index: Int): T {
        // 1. Bounds Check (Safety First!)
        if (index !in 0..<size) throw IndexOutOfBoundsException()

        // 2. Return the value
        // The `!!` tells Kotlin: "I promise this isn't null."
        // (since we logically control `size`, we know it's safe)
        return elements[index]!!            // <-- `!!` promises to Kotlin compiler that value isn't null.... useful for converting T? to T output return type!!!
    }

    /*
    ANALOGY:
    - C++: This is like `std::vector::at(i)`, which checks bounds. Standard
      `v[i]` in C++ does not check bounds and can segfault. Kotlin prefers
      safety by default.


    -----------|--<>
    6. ADDING DATA (THE CORE LOGIC)

    The slide shows the `add` method which inserts an item at a specific index.
    This is where the magic happens.

    * */

    fun add(index: Int, element: T) {
        // 1. BOUNDS CHECK: Don't allow gaps!
        if (index !in 0..size) {
            throw java.lang.IndexOutOfBoundsException()
        }

        // 2. RESIZE: Check if we need more space
        if (size + 1 > elements.size) {
            // Standard approach is doubling the current size (or 1 if 0)#
            val newCapacity = if (elements.size == 0) 1 else elements.size * 2
            elements = elements.copyOf(newCapacity)
        }

        // 3. SHIFT: Move elements to the right to make space
            // Note: Use `size` (old size), now `size + 1`
        for (i in size downTo index + 1) elements[i] = elements[i - 1]

        // 4. INSERT && INCREMENT
        elements[index] = element
        size++      // Increment size LAST
    }
            /*
                KEY TAKEAWAY: The loop `for (i in size downTo index + 1)` relies
                on `size` pointing to the first empty slot in the array. If you
                increment it early, you point to a slot that might not exist yet
                .
            * */


    /*
|     .-.
|    /   \         .-.
|   /     \       /   \       .-.     .-.     _   _
+--/-------\-----/-----\-----/---\---/---\---/-\-/-\/\/---
| /         \   /       \   /     '-'     '-'
|/           '-'         '-'

    * */
    /*
    7. RESETTING: `clear()`

    How do we empty the list? We don't just set `size = 0`. We usually want to
    release the references to the objects so the GARBAGE COLLECTOR can clean
    them up.
    * */
    fun clear() {
        elements = clearedArray()       // Replace the array with a fresh, empty one
        size = 0
    }



    /*
    1. THE EASY WINS: `isEmpty` and `set`

    These don't require resizing of shifting, so they are the best place to
    start.
    - `isEmpty()`: Just checks the size.
    - `set(index, element)`: Overwrites an existing value. It needs a bounds
      check but no resizing.
    * */
    fun isEmpty(): Boolean = if (size == 0) true else false

    fun set(index: Int, element: T) {
        // 1. Check bounds (Cannot set index 5 if size is 3)
        if (index !in 0..<size) throw IndexOutOfBoundsException()

        // 2. Overwrite
        elements[index] = element
    }


    /*
    2. THE "CONVENIENCE ADD": `add(element: T)`

    The slide showed the complex `add(index: Int, element: T)` that inserts into
    the middle. Usually, you also want a simple `add` that just appends to the
    end.
    - LOGIC: It's just a special case of inserting at the current `size`.
    * */
    fun add(element: T) {
        // Reuse your complex logic!
        // Inserting at `size` places it at the very end.
        add(size, element)
    }


    /*
    3. THE HARD ONE: `removeAt(index: Int)`

    This is the inverse of the `add` logic you saw in the slides.
    - LOGIC: When you delete item `i`, you must shift everything after it to the
      LEFT to fill the gap.
    - MEMORY LEAK WARNING: You must manually set the last slot to `null` after
      shifting, or the Garbage Collector won't be able to free that object.
    * */
    fun removeAt(index: Int): T {
        // 1. Bounds Check
        if (index !in 0..<size) throw IndexOutOfBoundsException()

        // 2. Save the value to return it later
        val itemToRemove: T = elements[index]!!

        // 3. SHIFT LEFT: Overwrite the hole
        for (i in size downTo index + 1) {
            elements[i - 1] = elements[i]
        }

        // 4. Cleanup & Resize
        elements[--size] = null       // IMPORTANT! Remove reference to 'D' (ghost object)

        return itemToRemove
    }



    /*
    4. OPTIONAL: SHRINKING (MEMORY OPTIMIZATION)

    The prompt asks to mimic `FixedCapacityList`, but a smart `ResizingList`
    also shrinks.
    - PROBLEM: If you grow to 1,000,000 items and delete 999,999, your internal
      array is still huge.
    - SOLUTION: In `removeAt`, check if `size` drops to 1/4 of the capacity. If
      so, resize the array to 1/2 the capacity. (We don't resize at 1/2 to avoid
      "thrashing" if we add/remove repeatedly)


    SUMMARY CHECKLIST FOR THE EXERCISE
    To fully complete the class as requested:
    1. PROPERTIES:
    * */











}








/*nunununununununununununununununununununununununununununununununununununununununununununununununununununununununununununu*/


/*
* conntinue to new 4        <-- then we will switch to other modules!
*                               https://gemini.google.com/app/bd659ecdaa303cef
*                               https://gemini.google.com/app/40c2e1110b00cd2a
* finish PPT-5 kotlin
*
            * DO INFORMATION 2-4 SLIDE EXERCISES <-- ASKS GEMINI 3 TO WRITE EXERCISE AND
            * TEST CASES FOR US
*
* ask more detail about how does the technique of casting <Any?> then back to `as <T>` work in more detail
* ... like I can kind of see how it works but not fully at the same time.
* */




/*nunununununununununununununununununununununununununununununununununununununununununununununununununununununununununununu*/
/*
    - segfault = In computing, a segmentation fault (often shortened to segfault
      ) or access violation is a failure condition raised by hardware with
      memory protection, notifying an OS that the software has attempted to
      access a restricted area of memory (a memory access violation).
* */