import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.StringBuilder
import kotlin.IndexOutOfBoundsException
import kotlin.math.max


/*
       !
       !
       ^
      / \
     /___\
    |=   =|
    |     |
    |  S  |
    |  I  |
    |  E  |
    |  N  |
    |  A  |
    |  R  |
    |     |
    |     |
   /|##!##|\
  / |##!##| \
 /  |##!##|  \
|  / ^ | ^ \  |
| /  ( | )  \ |
|/   ( | )   \|
    ((   ))
   ((  :  ))
   ((  :  ))
    ((   ))
     (( ))
      ( )
       .
       .
       .
* */
/*
    ... focuses on ENCAPSULATION (PRIVACY), GENERICS, and applying them to build
    a LINKED LIST.

    Since you have a C++ background, these concepts map directly to things you
    know well, but with Kotlin-specific syntax and defaults.



    ----------
    1. PRIVACY (ENCAPSULATION)

    In C++, class members are `private` by default. IN KOTLIN, EVERYTHING IS
    `public` BY DEFAULT. This is a major philosophical difference you need to
    watch out for.


    UTILITY
    - HIDING STATE: Prevents external code from messing with internal variables.
    - API DESIGN: Exposing only what the user needs to see ("Exposing just code
      that should be callable is key.")


    SYNTAX & EXAMPLE
    Use the `private` modifier to lock things down.
* */
class BankAccount_ {
    // 1. Private property: Only this class can touch 'balance'
    private var balance: Double = 0.0

    // 2. Public method: The outside world interacts via this
    fun deposit(amount: Double) = if (amount > 0) balance += amount
                                  else throw Exception("Bad")
}

/*
    COMMON PITFALL
    - THE "OPEN" DEFAULT: If you forget to type `private`, anyone can change
      your variables.
        - BAD: `var balance = 0` -> `account.bnalance = -1000` (Direct access
          allowed)
        - GOOD: `private var balance`
* */



/*<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>*/
/*
    2. GENERICS (`<T>`)

    This is Kotlin's equivalent of C++ TEMPLATES. It allows you to write code
    that works for any data type, rather than writing separate classes for
    `IntList`, `StringList`, etc.

    UTILITY
    - REUSABILITY: Write the logic once, use it for Strings, Integers, or
      Zombies.
    - TYPE SAFETY: The compiler catches error if you try to put a `String` into
      a






* */





/*<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>*/

/*<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>*/




/*
    2. GENERICS (`<T>`)

    This is Kotlin's equivalent of C++ TEMPLATES. It allows you to write code
    that works for any data type, rather than writing separate classes for
    `IntList`, `StringList`, etc.


    UTILITY
    - REUSABILITY: Write the logic once, use it for Strings, Integers, or
      Zombies.
    - TYPE SAFETY: The compiler catches error if you try to put a `String` into
      a `List<Int>`.


    SYNTAX & EXAMPLE

    You define the generic type parameter (usually `<T>`) in angle brackets
    after the class name.
* */

// A Generic `Box` that can hold ANY type T
class Box<T>(private val item: T) {
    fun getItem(): T = item
}

/*
fun main() {
    val intBox = Box<Int>(1)            // T is Int
    val strBox = Box<String>("Hi")      // T is String

    // Kotlin infers the type, so you can usually just write:
    val autoBox = Box(5.5)              // T is Double
}
 */

/*
    COMMON PITFALL
    - TYPE ERASURE: Like Java, generic types are erased at runtime. You
      generally cannot check `if (obj is T)` inside the class because at runtime
      , `T` doesn't exist (it's just `Object`).
* */




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
    3. THE CAPSTONE: LinkedList Demonstration

    The slides use a LINKED LIST to demonstrate how Classes, Privacy, and
    Generics come together.


    THE STRUCTURE
    A Linked List consists of NODES. Each node holds:
        1. DATA: The generic item `T`.
        2. NEXT: A pointer to the next Node (or `null` if it's the end).


    IMPLEMENTING IT (THE "KOTLIN WAY")
    Here is how you would translate the diagrams in your slides into code.


    STEP 1: THE NODE (RECURSIVE DATA STRUCTURE)
    ```Kotlin
    // Data Class makes it print nicely automatically.
    data class Node<T>(
        val data: T,
        var next: Node<T>? = null       // Nullable because the last node has no next
    )
    ```

    STEP 2: THE LIST WRAPPER
    The slide suggests creating a wrapper class to manage the `head`.
    ```Kotlin
    class LinkedList<T> {
        // Private head: The user shouldn't touch nodes directly
        private var head: Node<T>? = null

        fun add(item: T) {
            val newNode = Node(item)

            // If empty, new node is head
            if (head == null) {
                head = newNode
            } else {
                // Traverse to the end (O(n) operation)
                var current = head
                while (current?.next != null) {
                    current = current?.next
                }
                current?.next = newNode
            }
        }

        // Override toString to print the whole chain nicely
        override fun toString(): String {
            return head.toString()       // Relies on Node's data class toString
        }
    }
    ```


    WHEN TO USE LinkedList vs ArrayList


    LinkedList
        - ACCESS: Slow (O(n)) - Must hop through pointers.
        - INSERT/DELETE: Fast (Just change 2 pointers).
        - MEMORY: Higher (Overhead for storing pointers).
        - CACHE FRIENDLINESS: BAD (Nodes scattered in Heap)


    ArrayList (Standard `List`)
        - ACCESS: Fast (O(1)) - Jump straight to index.
        - INSERT/DELETE: SLOW (Must shift all elements).
        - MEMORY: Lower (Contiguous block).
        - CACHE FRIENDLINESS: Good (Contiguous in memory).




    SUMMARY FOR YOUR REVISION:
    1. PRIVACY: Default is `public`. Explicitly use `private` to protect your
       class internals.
    2. GENERICS: Use `<T>` to make flexible containers.
    3. LINKED LIST: A chain of generic `Node<T>` objects linked by references.
       Great for heavy insertion/deletion, bad for random access.
* */



/*<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>*/













/*
    ... explain C++ TEMPLATES from general knowledge, but crucially, I will
    contrast them with Kotlin so you understand the difference for your exams.


    1. WHAT ARE C++ TEMPLATES? (THE "COPY-PASTE" MACHINE)

    In C++, templates are a COMPILE-TIME CODE GENERATION mechanism.

    When you write `std::vector<T>`, the compiler acts like a giant photocopier.
    - If you use `std::vector<int>`, the compiler generates a specific class for
      `Int`.
    - If you use `std::vector<float>`, it generates a brand new, separate class
      for `float`.

    This is called MONOMORPHIZATION.


    C++ EXAMPLE:
    ```
    template <typename T>
    T add(T a, T b) {
        return a + b;
    }

    // Usage
    add(1, 2);          // Compiler generates:          int add (int, int)
    add(1.5, 2.5);      // Compiler generates:          double add (double, double)
    ```



    2. THE KEY DIFFERENCE: C++ TEMPLATES vs, KOTLIN GENERICS

    This is the most common exam topic when comparing these languages,#

    C++ TEMPLATES
    - MECHANISM: Reification / Monomorphization
               : New code is generated for every type.
    - RUNTIME IDENTITY: `vector<int>` and `vector<float>` are DIFFERENT classes.
    - PERFORMANCE: Faster (specialised code), but larger binary size (code
      bloat).
    - CONSTRAINTS: DUCK TYPING: If type `T` has a `+` operator, it works.
      Checked at instantiation.


    KOTLIN GENERICS (on JVM)
    - MECHANISM: TYPE ERASURE
               : The type information is deleted after compilation.
    - RUNTIME IDENTITY: `List<Int>` and `List<String>` are the SAME class at
      runtime.
    - PERFORMANCE: Slower (boxing/unboxing), but smaller binary.
    - CONSTRAINTS: BOUNDED QUANTIFICATION: You must say `<T : Number>`. Checked
      at definition.
* */


/*
   __
 .^o ~\
Y /'~) }      _____
l/  / /    ,-~     ~~--.,_
   ( (    /  ~-._         ^.
    \ "--'--.    "-._       \
     "-.________     ~--.,__ ^.
               \"~r-.,___.-'-. ^.
                YI    \\      ~-.\
                ||     \\        `\
                ||     //
                ||    //
                ()   //
                ||  //     -Row
                || ( c
   ___._ __  ___I|__`--__._ __  _
 "~     ~  "~   ::  ~~"    ~  ~~
                ::
                .:
                 .
* */






/*
    This is a crucial question. Since you are coming from C++, "Generics" look
    syntactically identical to "Templates," but under the hood, they work in
    completely different ways.

    Here is the deep dive into GENERICS, why they differ from Templates, and
    practical examples.


    1. THE CORE DIFFERENCE: "Copy-Paste" vs "Erasure"

    C++ TEMPLATES (THE "COPY-PASTE" MODEL) In C++, when you write `vector<int>`
    and `vector<string>`, the compiler literally generates TWO COMPLETELY
    DIFFERENT CLASSES in the binary code. It "copies and pastes" your template
    code, replacing `T` with `int` in one and `string` in the other.

    - PROS: Extremely fast (specialised optimization), can do specific things
      for specific types.
    - CONS: Code bloat (large binary size).



    KOTLIN GENERICS (THE "TYPE ERASURE" MODEL)
    In Kotlin (and Java), the compiler generates ONLY ONE CLASS in the bytecode.
    At compile time, it checks your type to make sure you don't put a String in
    an Int box. At runtime, it erases the `T` and treats everything as a generic
    `Object` (or `Any`).

    - THE CONSEQUENCE: This is why you CANNOT check `if (x is T)` at runtime.
      The runtime literally doesn't know what `T` is anymore!



    2. EXAMPLE & USE CASES

    EXAMPLE A: THE "WRAPPER" (DATA CLASS)

    USE CASE: You are fetching data from a server. It might be a `User`, a
    `Product`, or a `List<Int>`. You need a standard wrapper for the API
    response.
* */

// The Generic Class <T>
data class ApiResponse<T>(
    val data: T,
    val status: String
)

fun main() {
    // 1. Holding a String
    val strResponse = ApiResponse("Login Success", "OK")

    // 2. Holding a generic List of Ints
    val listResponse = ApiResponse(listOf(1, 2, 3), "OK")

    // 3. Access is Type-Safe (No casting needed!)
    val message: String = strResponse.data          // Compiler knows this is String


    /*
    EXAMPLE B: GENERIC CONSTRAINTS (THE "LIMIT")

    USE CASE: You want a function that sums two numbers, but you want to prevent
    someone from passing `String`. You can restrict `T` to be a subtype of
    `Number`.
* */

    // T must inherit from Number (Int, Double, Float)
    fun <T : Number> doubleValue(item: T): Double {
        return item.toDouble()      // We can call toDouble() because we know it's a Number
    }

    println(doubleValue(5))     // Works (Int is Number)
    println(doubleValue(2.5))   // Works (Double is Number)

    // doubleValue("Hello")          // ERROR: String is not a Number


    /*
    3. WHEN TO USE GENERICS VS. `ANY` (POLYMORPHISM)

    You might ask: "Why not just use `Any` (like `void* in C++`)?"


    THE "ANY" APPROACH (BAD)
* */
    class BadBox(val item: Any)

    val b = BadBox("Hello")
    // DANGER: You have to manually cast it back
    // If you get this wrong, the app crashes at runtime
    val s = b.item as String


    /*
            Kotlin uses type casting to convert an object from one type to
            another. The two primary casting operators are:

            as (Unsafe Cast): Forces a cast and throws a ClassCastException if
                              the cast fails.
            as? (Safe Cast) : Returns null if the cast is not possible,
                              preventing runtime exceptions.
        * */


    // THE "GENERIC" APPROACH (GOOD)
    class GoodBox<T>(val item: T)

    val b2 = GoodBox("Hello")
    // SAFE: The compiler knows `s` is a String automatically.
    // No crashing, no manual casting.
    val s2 = b2.item


    /*
        RULE OF THUMB:
        - USE GENERICS when the logic is the same, but the data type changes
          (e.g., Lists, Boxes, Response Wrappers).
        - USE `Any` only when you truly don't care what the object is (e.g., a
          logging function that just calls `.toString()` on whatever you pass it
          ).



    4. SUMMARY

    C++ TEMPLATE
    - CODE GENERATION: Creates a new class for every type (`Vector<Int>`,
      `Vector<Float>` are distinct).
    - RUNTIME CHECKS: You can check types easily.
    - BINARY SIZE: Can get huge (Template Bloat).



    KOTLIN GENERIC
    - CODE GENERATION: Create ONE class. `Box<Int>` and `Box<String>` are the
      same class at runtime.
    - RUNTIME CHECKS: TYPE ERASURE: You cannot check `is T` directly (unless
      using `inline` + `reified`)
    - BINARY SIZE: Small (One class shared by everyone).
    * */


}
/*

                                |
                                |
                               _|_
                              /___\
                             /_____\
\___________________________\       /___________________________/
  -----------|------|--------\_____/--------|------|-----------'
            ( )    ( )     O|OOo|oOO|O     ( )    ( )
* */

    /*
    You have found the one loophole in the "Type Erasure" rule I mentioned
    earlier!

    In standard Java/Kotlin, generic types are lost at runtime. However, Kotlin
    provides a special combination of keywords--`inline` and `reified`--that
    allows you to keep the type information.


    1. THE "INLINE" CONCEPT (THE MECHANISM)

    Normally, when you call a function, the computer "jumps" to that function's
    code in memory, runs it, and jumps back.

    When you mark a function as `inline`, you are telling the compiler:

        "Don't jump. Copy and paste the actual code of this function directly
        into the place where I called it."


    WHY DO THIS?

    1. PERFORMANCE: It eliminates the overhead of function calls (mostly useful
       for Higher-Order Functions with lambdas).
    2. THE MAGIC-TRICK: It enables `reified` types.



    -----------
    2. THE "REIFIED" CONCEPT (THE MAGIC)

    REIFIED comes from the word "Real." It makes the Generic T "real" at runtime

    Remember how I said you can't check `if (obj is T)` because `T` is erased?
    If you `inline` the function, the compiler copies your code to the call
    site. At that specific call site, THE COMPILER KNOWS EXACTLY WHAT T IS.

        - If you call `myFunc<String>()`, the compiler pastes the code and
          replaces `T` with `String`.
        - If you call `myFunc<Int>()`, it pastes the code and replaces `T` with
          `Int`.



    ------------
    3. EXAMPLE: THE "IMPOSSIBLE" CHECK

    WITHOUT REIFIED (The Error)
    * */

    // ERROR: Cannot check fo instance of erased type: T
/*
fun <T> checkType_(item: Any) {
    if (item is T) {
        println("It's a match!")
    }
}
*/


// WITH REIFIED (THE SOLUTION) You must add `inline` to the function and
// `reified` to the generic parameter.

    // 1. Mark function as `inline`
    // 2. Mark generic <T> as `reified`
inline fun <reified T> checkType(item: Any) {
    // Now this works! T is real here.
    if (item is T) {
        println("It is a ${T::class.simpleName}")
    }
}



/*<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>*/
/*
    4. REAL WORLD USE CASE: `filterIsInstance`

    This is probably the most common use of reified types in standard Kotlin
    development.

    Imagine you have a list of mixed objects: `[1, "Hello", 2, "World", 3.5]`.
    You want only the Strings.



    THE OLD WAY (CLASS PASSING): In Java, you have to pass the class manually to
    check the type.

```Java
// Java style (clunky)
list.filter(String.class)
```


    THE KOTLIN WAY (REIFIED): Because of reified generics, Kotlin's standard
    library has a function called `filterIsInstance<T>()`.

```Kotlin
val mixedList = listOf(1, "Hello", 2, "World", 3.5)

// Looks like magic: Checking the type <String> inside the generic!
val stringsOnly = mixedList.filterIsInstance<String>()

println(stringsOnly)            // Output: [Hello, World]
```


    5. WHEN TO USE vs. WHEN NOT TO USE

    `inline`
    - BENEFIT: Faster execution for lambdas. Enables `reified`.
    - COST/TRADE-OFF: CODE BLOAD. Because it copies/pastes code, your compiled
      binary gets larger if the function is large and called often.

    `reified`
    - BENEFIT: Allows `is T`, `as T`, and accessing `T::class`.
    - COST/TRADE-OFF: Can ONLY be used with `inline` functions. You cannot
      access `reified` types from Java code (because Java doesn't support this
      inlining trick).




    SUMMARY RULE: Use `inline fun <reified T>` ONLY when you absolutely need to
    access the type `T` inside the function (like for casting `as T` or checking
    `is T`). Otherwise, stick to standard Generics.


* */
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
    ... focuses on building a FIXED-CAPACITY LIST (essentially building your own
    simplified version of `ArrayList` from scratch).

    This is a classic CS exercise: implementing a dynamic data structure using a
    raw, fixed-size array.



    1. THE GOAL: A "MANUAL" LIST

    In high-level Kotlin, you juse use `mutableListOf(1, 2)`. But how does that
    work under the hood?

    - THE PROBLEM: Arrays are fixed size. If you make an array of size 10, it
      always uses memory for 10 items, even if you only store 1 number.
    - THE SOLUTION: You create a class that manages an array. It tracks:
        1. CAPACITY: The total space available (e.g., 10 slots).
        2. SIZE: The number of slots actually used (e.g., 1 slot).
        3. ELEMENTS: The raw array itself.



    ----------|-<>
    2. CONSTRUCTORS vs. PROPERTIES (THE INITIALIZATION)

    This is a critical syntax lesson in Kotlin. The slides show how to set up
    the class.
* */

// `capacity` is a Constructor Parameter (NOT a property yet)
class FixedCapacityList(capacity: Int) {

    // 1. Public Read, Private wRITE
    // Anyone can see the size, but only this class can change it.
    var size: Int = 0
        private set

    // 2. Private Property (The backing field)
    // We fill it with -1 (or null) to represent "empty" slots
    private val elements = Array(capacity) { -1 }               // <-- elements == the raw array itself


    /*
    UTILITY:
    - `private set`: This is encapsulation in action. You don't want an external
      user writing `list.size = 100` without actually adding 100 items! That
      would break your logic.
    - `Array(capacity) { -1 }`: This creates the "storage tank." It initializes
      every slot to `-1`.



    -------|-<>

    3. ADDING ITEMS (LOGIC & EXCEPTIONS)

    You need to handle two cases when adding an item: "Happy Path" (space exists
    ) and "Sad Path" (list is full or index is wrong).


    LOGIC: `add(index, element)`

    To insert an item at `index`, you must SHIFT everything else to the right to
    make room.
* */

    fun add(index: Int, element: Int) {
        // 1. Safety Check (Exceptions)
        if (index !in 0..size) throw IndexOutOfBoundsException()
        if (size >= elements.size) throw IllegalStateException("List is full")

        // 2. The Shift (Moving items to the right)
        // We loop backwards to avoid overwriting data we haven't moved yet
        for (i in size downTo index + 1) {
            elements[i] = elements[i - 1]
        }

        // 3. Insert and Update
        elements[index] = element
        size++                          // We used up one more slot
    }


    /*
    UTILITY:
    - EXCEPTIONS: `throw IndexOutOfBoundsException()` stops the program before
      it corrupts memory. This is standard safety practice.
    - SHIFTING: This shows why inserting into the middle of an array-based list
      is slow (O(n)). You have to physically move every subsequent item.




    ---------|--<>

    4. METHOD OVERLOADING (CONVENIENCE)

    You often want to just "add to the end" without specifying an index. Kotlin
    allows METHOD OVERLOADING (same name, different arguments).

    ```Kotlin
    // The complex one
    fun add(index: Int, element: Int) { ... }

    // The simple one (Delegates to the complex one)
    fun add(element: Int) {
        // Just add it at the current `size` (which is the end)
        add(index = size, element = element)
    }
    ```

    WHEN TO USE: Whenever you want to provide a "default behavior for a function
    (like adding to the end) whilst keeping the flexibility of the more complex
    version."
* */
    /*
       !
       !
       ^
      / \
     /___\
    |=   =|
    |     |
    |  S  |
    |  I  |
    |  E  |
    |  N  |
    |  A  |
    |  R  |
    |     |
    |     |
   /|##!##|\
  / |##!##| \
 /  |##!##|  \
|  / ^ | ^ \  |
| /  ( | )  \ |
|/   ( | )   \|
    ((   ))
   ((  :  ))
   ((  :  ))
    ((   ))
     (( ))
      ( )
       .
       .
       .
* */
    /*
    5. ACCESSING DATA (`get` operator)

    To retrieve data, you again need safety checks.
* */
    fun get(index: Int): Int {
        // '0.. < size' is a cool Kotlin range operator (excludes the upper bound)
        if (index !in 0..<size) {
            throw IndexOutOfBoundsException()
        }
        return elements[index]
    }

    /*
    UTILITY:
    - The `0..<size` syntax is cleaner than `0 until size` or `0..size-1`. It
      ensures you don't access a "valid but empty" lot (e.g., index 5 in a
      capacity-10 list that only holds 2 items).



    ---------|--<>

    SUMMARY CHECKLIST
    1. BACKING ARRAY: Use `private val elements` to store data.
    2. TRACKING STATE: Use a `size` variable to know how many slots are "real".
    3. ENCAPSULATION: Use `private set` on `size` so the world can't corrupt it.        private get // private set
    4. EXCEPTIONS: THROW errors early if inputs are bad (`throw Exception()`).
    5. OVERLOADING: Create multiple versions of `add()` for ease of use.
* */



/*
                            O         O
.  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .
 .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .
   .    .    .    .    .    .    .    .    .    .    .    .    .
   .     .     .     .     .     .     .     .     .     .     .
     .      .      .      .      .      .      .      .      .
 .       .       .       .       .       .       .       .       .
      .        .        .        .        .        .        .
   .         .         .         .         .         .         .
.          .          .          .          .          .          .
|          |          |          |          |          |          |
|          |          |          |          |          |          |
|          |          |          |          |          |          |
   .         .         .         .         .         .         .
      .        .        .        .        .        .        .
 .       .       .       .       .       .       .       .       .
     .      .      .      .      .      .      .      .      .
   .     .     .     .     .     .     .     .     .     .     .
   .    .    .    .    .    .    .    .    .    .    .    .    .
 .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .
.  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .
* */

    /*<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>*/

    /*
    1. `clear`
        - GOAL: Reset the list to an empty state.
        - THE "EFFICIENT" TRICK: You do NOT need to loop through the array and
          set everything to `-1`. This is a waste of CPU cycles.
        - LOGIC: Just set `size = 0`. Since your other methods (like `get` and
          `add`) check `size` to know where the valid data ends, the old data is
          effectively "invisible" and will eventually be overwritten.
    * */
    fun clear() {
        this.size = 0           // No need to wipe the array content manually!
    }


    /*<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>*/
    /*
    2. `contains`
        - GOAL: Check if a specific number exists in the list.
        - LOGIC: Loop from `0` up to `size` (exclusive). If you find a match,
          return `true`. If you finish the loop without finding it, return
          `false`.
    * */
    fun contains(element: Int): Boolean {
        for (i in 0..<size) {
            if (elements[i] == element) return true
        }
        return false
    }


    /*<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>*/
    /*
    3. `removeAt` (THE HARD ONE)
        - GOAL: Remove an item at a specific index and SHIFT everything else
          down to fill the gap.
        - LOGIC:
            1. CHECK BOUNDS: If index is invalid, throw exception.
            2. SAVE THE ITEM: You need to return it later.
            3. SHIFT LEFT: Loop starting from the target `index`. Overwrite
               `current` with `next`.
            4. DECREMENT SIZE: The list is now 1 item shorter.
    * */
    fun removeAt(index: Int): Int {
        if (index !in 0..<size) throw IndexOutOfBoundsException()

        val removedItem = elements[index]

        // Shift everything left: elements[3] becomes elements[4], etc.
        for (i in index until size - 1) elements[i] = elements[i + 1]

        size --
        return removedItem
    }


    /*<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>*/
    /*
    4. `remove` (By Value)
        - GOAL: Find the first occurrence of a specific value and delete it.
        - LOGIC:
            1. Loop to find the index of the element.
            2. If found, call `removeAt(index)` (reuse your own code!) and
               return `true`.
            3. If not found, return `false`.
    * */
    fun remove(element: Int): Boolean {
        for (i in 0 until size) {
            if (elements[i] == element) {
                removeAt(i)         // Reuse the shifting logic we just wrote
                return true
            }
        }
        return false
    }


    /*<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>*/
    /*
    5. `set`
        - GOAL: Overwrite an existing value at a specific index.
        - LOGIC: This is basically array access `arr[i] = x`, but with safety
          checks.
    * */
    fun set(index: Int, element: Int) {
        if (index !in 0..<size) throw IndexOutOfBoundsException()
        elements[index] = element
    }


    /*<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>*/
    /*
    6. `toString`
        - GOAL: Print the list nicely with `[1, 2, 3]` instead of
          `FixedCapacityList@23a1e`
        - LOGIC:
            1. Start with `[`
            2. Loop through the valid items.
            3. Append the number.
            4. If it's NOT the last item, append `, ` (comma + space).
            5. End with `]`.
    * */
    override fun toString(): String {
        // StringBuilder is efficient for loop concatenation
        val sb = StringBuilder("[")
        for (i in 0..<size) {
            sb.append(elements[i])
            if (i < size - 1) sb.append(", ")   // Don't add comma after the last one
        }
        sb.append("]")
        return sb.toString()
    }


    /*<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>-<]#[>*/
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
    ... we are moving from a specific `FixedCapacityList` (which only held
    Integers) to a GENERIC one that can hold anything.

    The slides take you through a journey: first trying the "lazy" way (using
    `Any`), failing, and then doing it the "right" way (using Generics `<T>`).

    Here is the run-through.



    1. THE "POOR PERSON'S GENERIC LIST" (List of `Any`)

    In C++, if you wanted a list to hold anything without templates, you might
    use `void*`. In Kotlin, the equivalent is `Any` (the root of all objects).

    THE SETUP:
    - THE ARRAY: You create an array of type `Array<Any?>`.
    - WHY `Any?` (Nullable): Because the empty slots in your array (the "spare
      capacity") need to be initialized to something. `null` is the perfect
      placeholder.
* */
class FixedCapacityAnyList(capacity: Int) {
    // We use 'arrayOfNulls' to create the backing storage
    private val elements: Array<Any?> = arrayOfNulls(capacity)
}

/*
    THE INVARIANT (THE RULE OF LAW):
    Your class promises that:
        1. Indices `0` to `size-1` contain REAL OBJECTS (not null).
        2. Indices `size` to `capacity-1` contain `null`.



    ---------|--<>

    2. THE DANGER: WHY `List<Any>` SUCKS
    The slide demonstrates exactly why this is bad practice.

    THE "MIXED BAG" PROBLEM:
    Since the list accepts `Any`, you can accidentally mix types.






*/







/*
* Continue with lecture 8
* conntinue to new 4
* finish PPT-5 kotlin
* */


class `Jan-15_v2` {
}



/*
    You are spot on with your suspicion. Thinking of Any as void* is dangerous because it misses the most important feature of Kotlin: Safety.
* */