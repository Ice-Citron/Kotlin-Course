import kotlin.math.roundToInt


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
        resizeList()

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
    1. PROPERTIES: `size` and `elements` (Array).
    2. CONSTRUCTORS: Primary (capacity) and Secondary (default 16).
    3. READ: `get(index)`.
    4. WRITE: `set(index, value)`
    5. GROW: `add(index, value)` (with doubling logic)
    6. SHRINK: `removeAt(index)` (with shifting logic)
    7. UTILS: `isEmpty()`, `size`.
    * */

    fun resizeList() {
        if (elements.size <= 16) return
        if (size > (elements.size / 4).toInt()) return
        val newArray: Array<T?> =
            arrayOfNulls<Any?>((elements.size / 2).toInt()) as Array<T?>
        for (i in 0..<size) {
            newArray[i] = elements[i]
        }
        elements = newArray

    }
}
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
    ... we are moving from the "Array" world to the "Linked List" world. This is
    a classic CS topic where Kotlin's approach to NULL SAFETY and ENCAPSULATION
    really shines compared to C++.

    Here is the walkthrough of your SINGLY-LINKED LIST slides.

    ------=--=---==|---<0>


    1. THE MOTIVATION: WHY BOTHER?

    - THE ARRAY PROBLEM: If you want to insert an item at the start (`index 0`)
      of an `ArrayList`, you have to shift EVERY SINGLE OTHER ITEM to the right.
      This is O(N) (slow).
    - THE LINKED LIST SOLUTION: You just create a new node and point it to the
      old head. This is O(1) (instant).


    2. THE BUILDING BLOCK: THE `Node`

    A Linked List is a chain of "Nodes." Each node holds two things:
    1. DATA (`element`): The value (e.g. `42`).
    2. POINTER (`next`): The address of the next node.

    ...

    COMPARISON:
    - C++: You would use `Node* next;`. IF it's the end of the list, it points
      to `nullptr`
    - KOTLIN: You use `Node<T>?`. The `?` is crucial. It forces you to handle
      the end of the list (where `next` is `null`) or the compiler won't let you
      run.
    - HASKELL: A list `[a]` is defined recursively as `Empty | Cons a (List a)`.
      Kotlin's `Node` is basically the `Cons` cell, but mutable (`var`).

    ```Kotlin
    class Node<T>(var element: T, var next: Node<T>? = null)
    ```


    ==---=-=-=------|---<>
    3. ENCAPSULATION: HIDING THE "PLUMBING"

    The slides iterate through a "bad" design to a "good" one.

    - ATTEMPT 1 (BAD): Defining `class Node` globally.
        - PROBLEM: If you have `SinglyLinkedList.kt` and `DoublyLinkedList.kt`,
          both might try to define `class Node`. Name clash!
    - ATTEMPT 2 (GOOD): NESTED PRIVATE CLASS.
        - You put the `Node` class inside the `SinglyLinkedList` class and mark
          it `private`.



  -----                                                               -----
1 | H |                                                               |He |
  |---+----                                       --------------------+---|
2 |Li |Be |                                       | B | C | N | O | F |Ne |
  |---+---|                                       |---+---+---+---+---+---|
3 |Na |Mg |3B  4B  5B  6B  7B |    8B     |1B  2B |Al |Si | P | S |Cl |Ar |
  |---+---+---------------------------------------+---+---+---+---+---+---|
4 | K |Ca |Sc |Ti | V |Cr |Mn |Fe |Co |Ni |Cu |Zn |Ga |Ge |As |Se |Br |Kr |
  |---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---|
5 |Rb |Sr | Y |Zr |Nb |Mo |Tc |Ru |Rh |Pd |Ag |Cd |In |Sn |Sb |Te | I |Xe |
  |---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---|
6 |Cs |Ba |LAN|Hf |Ta | W |Re |Os |Ir |Pt |Au |Hg |Tl |Pb |Bi |Po |At |Rn |
  |---+---+---+------------------------------------------------------------
7 |Fr |Ra |ACT|
  -------------
              -------------------------------------------------------------
   Lanthanide |La |Ce |Pr |Nd |Pm |Sm |Eu |Gd |Tb |Dy |Ho |Er |Tm |Yb |Lu |
              |---+---+---+---+---+---+---+---+---+---+---+---+---+---+---|
   Actinide   |Ac |Th |Pa | U |Np |Pu |Am |Cm |Bk |Cf |Es |Fm |Md |No |Lw |
              -------------------------------------------------------------



    3. ENCAPSULATION: HIDING THE "PLUMBING"

    The slides iterate through a "bad" design to a "good" one.

    - ATTEMPT 1 (BAD): Defining `class Node` globally.
        - PROBLEM: If you have `SinglyLinkedList.kt` and `DoublyLinkedList.kt`,
          both might try to define `class Node`. Name clash!

    - ATTEMPT 2 (GOOD): NESTED PRIVATE CLASS.
        - You put the `Node` class inside the `SinglyLinkedList` class and mark
          it `private`.
        - Now, `Node` is only visible to the List itself. The outside world just
          sees a List; they don't care about ndoes.


    C++ COMPARISON: This is exactly like defining a `struct Node` inside the
    `private:` section of a C++ class header.


    ----------|--<>
    4. ADDING ELEMENTS (`add`)

    Adding to the end of the list involves updating pointers.

    ```Kotlin
    fun add(element: T) {
        val newNode = Node(element)

        // CASE 1: The list is currently empty
        if (head == null) {
            head = newNode
            tail = newNode      // Head and Tail are the same single node
        }

        // CASE 2: The list has stuff in it
        else {
            tail!!.next = newNode           // 1. Link the old tail to the new node
            tail = newNode                  // 2. Update the 'tail' label to the new node
        }
        size++
    }
    ```

    KEY KOTLIN CONCEPT (`!!`): Notice `tail!!.next`
    - `tail` is nullable (`Node?`).
    - In the `else` block, we know `tail` isn't null (because `head` wasn't
      null).
    - We use `!!` to scream at the compiler: "Trust me, this exists!"


    -=-=-=------|--<>
    5. REMOVING ELEMENTS (`removeAt`)

    To remove node B (between A and C), you don't "delete" B directly. You just
    tell A to point at C. B is effectivelly bypassed.


    THE "GARBAGE COLLECTION" DIFFERENCE:
    - C++: You must manually `delete nodeB;` after unlinking it, or you get a
      memory leak.
    - KOTLIN: As the slide notes: "This node is no longer part of the list -- in
      due course it will be garbage-collected." You just drop the reference,
      and the JVM cleans it up.

    THE LOGIC (TRAVERSAL): To remove index `i`, you need to get a reference to
    the node at `i - 1` (the "previous" node) so you can change its `next`
    pointer. The slid uses a helper function `traverseTo(index)` to walk the
    chain:

    ```Kotlin
    // Concept from slide
    previous.next = current.next
    // (A points to c, skipping B)
    ```


    -=-=-=------|-<<<<
    SUMMARY CHECKLIST FOR IMPLEMENTATION

    If you have to write this for an exam, remember:
    1. THE CLASS: `class SinglyLinkedList<T>`
    2. THE STATE: `head`, `tail`, `size`.
    3. THE NODE: `private class Node<T>(var item: T, var next: Node<T>? = null)`
       inside the list class.
    4. THE EDGE CASES: Always check `if (head == null)` before trying to follow
       pointers!



                                              ____________
                               --)-----------|____________|
                                             /        /
               -)------========            /  _____ /
                        \     \          /   /____/
                          \     \      /        /
                            \     \__/_________/__
                              \ _ _| ^--      || |
                   ________--------|_____________|\
    -  \-----------                               ||
 -     ============================ ______________ |
    - _____________________________|  |'      || |/
                               /   |_____________|
                             /     /  \         \
                           /     /      \      ___\
               -)---------========        \    \____\
THE FORCE, LUKE,                            \         \
USE THE FORCE!                                \_________\_
                              --)-------------|___________|
    ------
    SLIDES REFERENCES

    ```Kotlin
    class SinglyLinkedList<t>() {
        private var head: Node<T>? = null
        private var tail: Node<T>? = null
        var size: Int = 0
            private set

        private class Node<T>(item: T, next: Node<T>? = null)
    }

    ```


    ------------|---<>
    ```Kotlin
    fun add(element: T) {
        size++
        val newNode = Node(element)             // Exploits default parameter: new node's successor is NULL by default
        if (head == null) head = newNode; tail = newNode; return            // <-- interestingly this check here appears to be the INVARIANT which ensures that `tail` is not-null here
        tail!!.next = newNode
        tail = newNode
    }
    ```


    ```Kotlin
    private fun traverseTo(index: Int): Pair<Node<T>?, Node<T>> {
        var previous: Node<T>? = null
        var current: Node<T>? = head
        for (i in 0..<index) {
            previous = current
            current = current!!.next
        }
        return Pair(previous, current!!)
    }
    ```


    ```Kotlin
    private fun unlink(previous: Node<T>?, current: Node<T>) {
        if (previous == null) head = current.next
        else                  previous.next = current.next
        if (current == tail)  tail = previous
        size--
    }
    ```


    ```Kotlin
    fun removeAt(index: Int): T {
        if (index !in 0..<size) throw IndexOutOfBoundsException()
        val (previous: Node<T>?, current: Node<T>) = traverseTo(index)  // <-- interesting... helper function #1!
        val result = current.element
        unlink(result, current)                                         // <-- interesting... helper function #2!
        return result
    }

    ```
* */






class SinglyLinkedList<T>() {

    private var head: Node<T>? = null
    private var tail: Node<T>? = null
    var size: Int = 0
        private set

    private class Node<T>(var element: T, var next: Node<T>? = null)


    private fun nodeAt(index: Int): Node<T> {
        if (index !in 0..<size) throw IndexOutOfBoundsException()
        if (head == null) throw Exception("Linked List is empty!")
        var current: Node<T> = head!!
        repeat(index) {             //  for (i in 0..<index) {          <-- seems that using `repeat(n)` is better!
            current = current.next!!
        }
        return current
    }

    fun set(newElement: T, index: Int) {
        var current: Node<T> = nodeAt(index)
        current.element = newElement
    }

    fun get(index: Int): T {
        var current: Node<T> = nodeAt(index)
        return current.element
    }

    fun add(element: T) {
        val newNode: Node<T> = Node<T>(element)
        if (head == null) {
            head = newNode
            tail = newNode
        }
        else {
            tail!!.next = newNode
            tail = newNode
        }
        size++
    }

    fun add(index: Int, element: T) {
        if (index !in 0..size) throw IndexOutOfBoundsException()

        // Case 1: Inserting at the start
        if (index == 0) {
            val newNode = Node(element, next = head)    // Point to old head
            head = newNode
            if (tail == null) tail = newNode
        }
        // Case 2: Inserting middle or end
        else {
            val prev = nodeAt(index - 1)
            val newNode = Node(element, next = prev.next)       // Sandwich the node
            prev.next = newNode
            if (newNode.next == null) tail = newNode
        }
        size++
    }

    fun removeAt(index: Int): T {
        if (index !in 0..<size) throw IndexOutOfBoundsException()
        var removedNode: Node<T> = nodeAt(index)
        if (index == 0) {                                 // consider if at head
            head = removedNode!!.next
            if (head == null) tail = null               // If list is now empty, fix tail
        }
        else if (index == size - 1) {                     // consider if at tail
            tail = nodeAt(index - 1)
            nodeAt(index - 1).next = null             // remove references
        }
        else {                              // case in between
            nodeAt(index - 1).next = removedNode.next
        }
        size--
        return removedNode.element
    }






}













/*
SSSSSSSSSSSSSSS                        SSSSSSSSSSSSSSSSSSSSS
SSSSSSSSSSSSSSSSS                      SSSSSSSSSSSSSSSSSSS
SSSSSSSSSSSSSSSSSSS                    SSSSSSSSSSSSSSSSS
SSSSSSSSSSSSSSSSSSSSS                  SSSSSSSSSSSSSSS                      SSS
    SSSSSSSSSSSSSSSSSSSSS                     SSSSSS                   SSSSSSSS
         SSSSSSSSSSSSSSSSSS       SSSSSSSSSSS   SS                 SSSSSSSSSSSS
              SSSSSSSSSSSSSSS  SSSSSSSSSSSSSSSSS              SSSSSSSSSSSSSSSSS
                   SSSSSSSS  SSSSSSSSSSSSSSSSSSSSS       SSSSSSSSSSSSSSSSSSSSSS
                        SS  SSSSSSSSSSSSSSSSSSSSSSS  SSSSSSSSSSSSSSSSSSSSSSSSSS
                           SSSSS     SSS     SSSSSSS  SSSSSSSSSSSSSSSSSSSSSSSSS
                          SSSSS   SS  S   SS  SSSSSSS  SSSSSSSSSSSSSSSSSSSSSSSS
SSSSSSSSSSSSSSSSSSSSSSSS  SSSSSSSSSSS SSSSSSSSSSSSSSS  SSSSSSSSSSSSSSSSSSSSSSSS
SSSSSSSSSSSSSSSSSSSSSSSS  SSSSSSSS S   SS SSSSSSSSSSS
SSSSSSSSSSSSSSSSSSSSSSSSS  SSSSS  SSSSSSSS   SSSSSSS
SSSSSSSSSSSSSSSSSSSSSSSSSS  SSS SS        SSS SSSSS  SS
SSSSSSSSSSSSSSSSSSSSSS       SSSSSSSSSSSSSSSSSSSSS  SSSSSSSS
SSSSSSSSSSSSSSSSS              SSSSS   SSSSSSSSS  SSSSSSSSSSSSSSS
SSSSSSSSSSSS                 SS   SSSSSSSSSSS   SSSSSSSSSSSSSSSSSSSSSS
SSSSSSS                    SSSSSS                 SSSSSSSSSSSSSSSSSSSSSSSSS
SS                       SSSSSSSSSSSSSSS            SSSSSSSSSSSSSSSSSSSSSSSSSSS
                       SSSSSSSSSSSSSSSSS              SSSSSSSSSSSSSSSSSSSSSSSSS
                     SSSSSSSSSSSSSSSSSSS                SSSSSSSSSSSSSSSSSSSSSSS
                   SSSSSSSSSSSSSSSSSSSSS                  SSSSSSSSSSSSSSSSSSSSS
* */


/*
    ... C++ ABSOLUTELY ALLOWS YOU TO DEFINE CLASSES INSIDE CLASSES.

    However, there is a HUGE difference in how they work by default compared to
    Kotlin. This is a classic "gotcha" for people between the two languages.

    Here is the breakdown of the NESTED CLASS vs. INNER CLASS distinction.


    1. THE C++ WAY (the "STATIC" DEFAULT)

    In C++, if you define `Class B` inside `Class A`, it is purely a SCOPING
    decision.
    - `B` is just "a class that lives inside the namespace of `A`."
    - `B` does NOT have access to the variables of `A` automatically.
    - It does NOT secretly hold a pointer to an instance of `A`

    ```C++
    class Outer {
    private:
        int x = 10;

    public:
        // This is just a normal class, hidden inside Outer's name.
        class Inner {
        public:
            void foo() {
                // ERROR! I don't know which `Outer` object you are talking
                // about.
                // cout << x << endl;
            }
        }
    }
    ```

    To make it work in C++, you have to manually pass a pointer:

    ```C++
    void foo(Outer* parent) {
        cout << parent->x << endl;          // Now it works
    }
    ```



    --------=-----|---<>
    2. THE KOTLIN WAY (TWO TYPES)

    Kotlin splits this concept into two explicit keywords becuase it wants to be
    safer about memory (holding hidden references causes memory leaks).


    A. THE NESTED CLASS (DEFAULT - LIKE C++)

    If you write `class` inside `class`, it behaves exactly like C++. It is
    static. It CANNOT touch the outer class.            <-- IMPORTANT KEYWORD HERE... THAT IS THAT IT'S STATICCCCC
* */
class Outer {
    private val bar: Int = 1

    class Nested {          // <-- THIS IS A STATIC CLASS!!!
        fun foo() = 2
        // fun access() = bar       // ERROR! I don't have access to `bar`
    }
}


    /*
    B. THE INNER CLASS (THE "JAVA" WAY)

    If you add the keyword `inner`, magic happens.
    - Kotlin implicitly passes a hidden reference (pointer) to the specific
      `Outer` object that created it.
    - You CAN access outer variables.s
    * */
    class Outer_2 {
        private val bar: Int = 1
        inner class Inner {
            fun foo() = bar     // WORKS! Accesses `this@Outer.bar`
        }
    }


    /*
    FEATURE: C++ `class B` inside `class A`
    RELATIONSHIP: Just neighbors (static)
    ACCESS OUTER vars? NO (unless passed pointer)
    MEMORY COST: Low (No extra pointer)
    USE CASE: Helper structs, Builders

    FEATURE: Kotlin `class B` inside `class A`
    RELATIONSHIP: Just neighbors (static)
    ACCESS OUTER vars? No
    MEMORY COST: Low (No extra pointer)
    USE CASE: Constants, Helpers

    FEATURE: Kotlin `inner class B` inside `class A`
    RELATIONSHIP: MARRIED (Bound to instance)
    ACCESS OUTER vars? YES (Automatic)
    MEMORY COST: HIGHER (Holds hidden reference)
    USE CASE: Adapters, Listeners.
    * */


/*nunununununununununununununununununununununununununununununununununununununununununununununununununununununununununununu*/
/*
    The specific invariant that makes `tail!!` safe here is: "IF THE LIST IS NOT
    EMPTY (SIZE > 0), THEN `head` and `tail` ARE BOTH NON-NULL."

    ... why your intuition about "size is not 0" is the key:

    1. THE "EMPTY" CHECK: Look at the `if` statement in the code:

    ```Kotlin
    if (head == null) head = newNode; tail = newNode; return
    ```

    If the list was empty (size 0), the code enters this block, sets up the
    first node, and EXITS the function immediately.


    2. THE "NON-EMPTY" REALITY: If the computer reaches the line
       `tail!!.next = newNode`, it simply implies that the `if (head == null)`
       check was FALSE.
       - This means `head` is not null.
       - THE INVARIANT: In a valid Linked List, if `head` is not null (the list
         has items), then `tail` cannot be null (there must be an end).

    So, because you filtered out the "Empty" case (Size 0) in the `if` block,
    you are guaranteed that we are in the "Non-Empty" (Size > 0), where `tail`
    is guaranteed to exist.


* */







/*nunununununununununununununununununununununununununununununununununununununununununununununununununununununununununununu*/


/*

* finish PPT-5 kotlin
*
            * DO INFORMATION 2-4 SLIDE EXERCISES <-- ASKS GEMINI 3 TO WRITE EXERCISE AND
            * TEST CASES FOR US
*
* Do PPT-6 for Kotlin
* Do 40017 -> 40008 -> 40018 -> 40005
* */




/*nunununununununununununununununununununununununununununununununununununununununununununununununununununununununununununu*/
/*
    - segfault = In computing, a segmentation fault (often shortened to segfault
      ) or access violation is a failure condition raised by hardware with
      memory protection, notifying an OS that the software has attempted to
      access a restricted area of memory (a memory access violation).

    - invariant = In Computer Science, an INVARIANT is a condition or property
      that remains true (doesn't change) throughout a specific sequence of
      operations or execution of a program, used to reason about correctness,
      simplify algorithms (like LOOP INVARIANTS in loop), or define valid object
      states (like CLASS INVARIANTS). It helps prove that a program behaves as
      expected by defining assumptions that holds true before, during, and after
      certain code sections.
* */