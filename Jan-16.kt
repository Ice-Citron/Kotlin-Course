import kotlin.IllegalStateException


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
    ... we are moving from a specific `FixedCapacityList` (which only held
    Integers) to a GENERIC one that can hold anything.

    The slides take you through a journey: first trying the "lazy" way (using
    `Any`), failing, and then doing it the "right" way (using Generics `<T>`).



    1. THE "POOR PERSON'S GENERIC LIST" (LIST OF `Any`)

    In C++, if you wanted a list to hold anything without templates, you might
    use `void*`. In Kotlin, the equivalent is `Any` (the root of all objects).

    THE SETUP:
      - THE ARRAY: You create an array of type `Array<Any?>`.
      - WHY `Any?` (Nullable): Because the empty slots in your array (the "spare
        capacity") need to be initialized to something. `null` is the perfect
        placeholder.
* */

class FixedCapacityAnyList_(capacity: Int) {

    // We use `arrayOfNulls` to create the backing storage
    private val elements: Array<Any?> = arrayOfNulls(capacity)
    var size: Int = 0
        private set

    /*
    THE INVARIANT (THE RULE OF LAW): Your class promises that:
        1. Indices `0` to `size-1` contain REAL OBJECTS (not null).
        2. Indices `size` to `capacity-1` contain `null`.


    ------------|--<>


    2. THE DANGER: WHY `List<Any>` SUCKS

    The slides demonstrate exactly why this is a bad practice.

    THE "MIXED BAG" PROBLEM: Since the list accepts `Any`, you can accidentally
    mix types.

        ```Kotlin
        val list = FixedCapacityList_(10)
        list.add("Hello")       // OK
        list.add(42)            // OK (Wait, do we want numbers in our string list?)
        ```


    THE CRASH (ClassCastException):
    When you retrieve data, the compiler only knows it is an `Any`. You have to
    cast it manually. If you are wrong, the program crashes.

        ```Kotlin
        val item = list.get(1) as String            // CRASH! Item 1 was 42 (Int), not String
        ```


    --------|--<>


    3. THE `!!` OPERATOR (NON-NULLABLE ASSERTION)

    This is a very specific Kotlin operator introduced in these slides.
        - SYMBOL: `!!` (pronounced "double-bang").
        - MEANING: "I promised this variable is NOT null. If I am lying, crash
          the program immediately with a `NullPointerException`."

    WHY USE IT HERE? Your array is defined as `Array<Any?>` (nullable), but
    your `get()` function promises to return a real `Any` (non-null). You know
    that if `index < size`, the item cannot be null (because of your invariant).
    The compiler doesn't know that. You use `!!` to force the compiler to trust
    you.
    * */
    fun get(index: Int): Any {
        if (index !in 0..<size) throw IndexOutOfBoundsException()
        return elements[index]!!        // "Trust me, compiler, this specific slot is not null"
    }


    /*
    4. GARBAGE COLLECTION (THE "CLEAR" LOGIC)

    This is a concept you didn't have to worry about manually in C++ (where you
    `delete`), but in Java/Kotlin, you have to help the Garbage Collector (GC).

    THE PROBLEM: If you just decrement `size--` but leave the object in the
    array, the array still "points" to that object. The GC sees the pointer and
    thinks "Oh, this object is still in use, I better keep it in memory."

    THE FIX (NULLING OUT): When you remove an item (or clear the list), you must
    explicitly set that slot to `null`.
    * */
    fun clear() {
        // 1. Null out every slot so the GC can eat the old objects
        for (i in 0..<size) elements[i] = null
        size = 0            // 2. Reset Size
    }


    /*
    - UTILITY: This prevents "Memory Leaks" where your list holds onto gigabytes
      of data that the user thinks they deleted.


    --------|--<>

    5. THE FINAL SOLUTION: GENERIC LIST `<T>`

    Finally, the slides show the correct way to build this using Generics.

    This combines everything:
        1. TYPE SAFETY: `T` prevents mixing Integers and Strings.
        2. NULLABLE BACKING: `Array<T?>` allows empty slots to be null.
        3. INVARIANT: We still promise valid items are non-null.
    * */
}



/*nunununununununununununununununununununununununununununununununununununununununununununununununununununununununununununu*/

class FixedCapacityList_<T>(capacity: Int) {
    private val elements: Array<T?> = arrayOfNulls<Any?>(capacity) as Array<T?>
    var size: Int = 0
        private set

    fun add(element: T) {
        if (size > elements.size) throw IllegalStateException("List is full!")
        elements[size] = element
        size++
    }
}

/*
    COMMON PITFALL (THE UNCHECKED CAST): You might see a warning
    `Unchecked cast: Array<Any?> to Array<T?>`. This is due to the "Type
    Erasure" we discussed earlier. In this specific low-level data structure
    case, it is safe to suppress/ignore that warning because you control the
    array.
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
    1. THE EXERCISE: CONVERTING TO `FixedCapacityList<T>`

    The goal is to take your working `Int` list and make it generic. As the
    slide says, the changes are minimal but critical regarding NULLABILITY.


    STEP 1: CLASS DEFINITION

    Change the class to accept a type parameter `<T>`.
* */
class FixedCapacityList_2<T>(capacity: Int) {

    private val elements: Array<T?> = arrayOfNulls<Any?>(capacity) as Array<T?>
    var size: Int = 0
        private set


    /*
    STEP 2: THE BACKING ARRAY (THE HARDEST PART)

    You cannot just write `Array<T>(capacity)`.
        - WHY? You don't have an instance of `T` to fill the array with (unlike
          `Int`, where we used `-1`).
        - THE FIX: Use nullable `T?` and fill it with `null`.

        ```Kotlin
        // OLD
        private val elements = Array(capacity) { -1 }

        // NEW - We create an array of nulls and cast it to our generic type
        private val elements: Array<T?> = arrayOfNulls<Any?>(capacity) as Array<T?>
        ```


    -----------|--<>>


    STEP 3: `add` method

    Change `Int` to `T`. No other logic changes needed.

        ```Kotlin
        // OLD
        fun add(element: Int) { ... }

        // NEW
        fun add(element: T) {
            if (size >= elements.size) throw IllegalStateException("Full")
            elements[size++] = element      // Works fine               <-- just like C++/C/Java! int++,... the number is post-increment! as in the value is only incremented AFTER the whole line has been processed
        }
        ```
    */
    fun add(element: T): Unit {
        if (size >= elements.size) throw IndexOutOfBoundsException()
        elements[size++] = element
    }



    /*
    STEP 4: `get` METHOD (THE "TRUST ME" OPERATOR)

    The array holds `T?` (nullable), but your method promises to return `T`
    (non-null). You need `!!`.              <-- interesting! so if there's a mismatch between Any? (original) and Any (for niche fun) then we actually need to use `!!` !!!!

        ```Kotlin
        // OLD
        fun get(index: Int): Int { ... return elements[index] }         // index check.., throw IndexOutOfBoundsException()

        // NEW
        fun get(index: Int): T {
            if (index !in 0..<size) throw IndexOutOfBoundsException()
            return elements[index]!!
                // We promise the compiler: "I know the array type is T?,
                // but at this valid index, I swear it is not null."
        }

        ```
    * */
    fun get(index: Int): T {
        if (index !in 0..<size) throw IndexOutOfBoundsException()
        return elements[index]!!
    }


    /*
    2. DO WE NEED `inline` and `reified` here?

    THE SHORT ANSWER: NO, you do not need them for this specific class.

    THE LONG ANSWER (and when you WOULD need them): You successfully dodged the
        need for `reified` because of how you initialized the array:

        `arrayOfNulls<Any?>(capacity) as Array<T?>`

    This creates a standard Java `Object[]` array under the hood. Since "Type
    Erasure" means `T` becomes `Object` at runtime anyway, this fits perfectly.
    The compiler lets you do this with an "Unchecked Cast" warning (which you
    ignore).



    WHEN WOULD YOU NEED `inline`/`reified`?

    You would need it if you tried to create the array SAFELY without casting,
    or if you needed to throw the REAL TYPE of T inside the class.

    SCENARIO: Imagine you wanted a method that returns a raw array `T[]` (not a
    List) to the user.

        ```Kotlin
        // WITHOUT REIFIED (Impossible)
        fun toArray(): Array<T> {
            // ERROR: Compiler says "I don't know what T is, so I can't make an array of it"
            return Array<T>(size) { ... }
        }
        ```


        ```Kotlin
        // WITH REIFIED (Possible)
        // This MUST be an extension function or top-level function
        // (Constructors cannot be inline!)
        inline fun <reified T> FixedCapacityList<T>.toNativeArray(): Array<T> {
            return Array<T>(size) { i -> this.get(i) }
                // Success! `T` is known, so we can make a real Array<T>
        }
        ```
    * */


    /*
    SUMMARY:
        - FOR INTERNAL STORAGE (IMPLEMENTATION DETAILS): You usually cheat with
          `Any?` casts. No `reified` needed.
        - FOR EXTERNAL API (PUBLIC METHODS): If you need to return a specific
          `Array<T>` or check `is T`, you need `reified`.
    * */
}













/*nunununununununununununununununununununununununununununununununununununununununununununununununununununununununununununu*/



/*nunununununununununununununununununununununununununununununununununununununununununununununununununununununununununununu*/

/*nunununununununununununununununununununununununununununununununununununununununununununununununununununununununununununu*/












/*nunununununununununununununununununununununununununununununununununununununununununununununununununununununununununununu*/
/*
   *              17 October 1997, 02:30 AM         *
           *                                                  *
                 ______  _____  _____  ______
                /      \/    /  \    \/      \   *
     *         /  \    / ___/    \___ \    /  \
              /    \  / /            \ \  /    \           *
             _\____ \ \ \     /\     / / / ____/_
            /   __ \ \ \ \    \/*   / / / / __   \
           /   /  \ \ \ \ \        / / / / /  \   \      *
     *    /___/    \ \ \ \ \______/ / / / /    \___\
                    \ \ \ \        / / / /
 *      /\    __     \ \ \ \______/ / / /     __    /\
       /  \   \_\    /  \ \        / /  \    /_/   /  \     *
      /   /         /   /  \      /  \   \         \   \
     _\  /         /   /    \    /    \   \         \  /_
    /  \ \--------/   /      \__/      \   \--------/ /  \
   /    \------------/       /  \       \------------/    \
  < ------------------------<    >------------------------ >
   \    /------------\       \__/       /------------\    /
    \__/ /--------\ * \      /  \      /   /--------\ \__/
      /  \         \   \    /    \    /   /         /  \
      \   \    __   \   \  /      \  /   /   __    /   /
       \  /   /_/    \  / / ______ \ \  /    \_\   \  /
 *      \/           / / / /      \ \ \ \           \/     *
           ___      / / / / ______ \ \ \ \       ___
          \   \    / / / / /      \ \ \ \ \    /   /
           \   \__/ / / / /        \ \ \ \ \__/   /  *
            \_ ____/ / / /    /\    \ \ \ \____ _/
              /     / / /     \/     \ \ \     \
              \    /  \ \___      ___/ /  \    /
               \  /    \    \    /    / *  \  /        *
    *           \______/\____\  /____/\______/
           *                              *
* */*/

/*
    You are spot on with your suspicion. Thinking of `Any` as `void*` is
    DANGEROUS because it misses the most important feature of Kotlin: SAFETY.

    While both conceptually "can hold anything," they behave completely
    differently at runtime.


    1. THE C++ `void*` (THE AMNESIAC)

    In C++, a `void*` is just a raw memory address. It points to something, but
    the compiler has forgotten what that something is.
    - SAFETY: Zero. If you cast a `void*` pointing to a `Car` into a `Spaceship`
      , your program will likely just crash or corrupt memory.
    - RUNTIME INFO: None. The pointer strictly holds an address (e.g.
      `0x7fx11s12`). It does not know if it points to an `int`, a `class` or
      garbage.


    2. THE KOTLIN `Any` (THE UNIVERSAL PARENT)

    In Kotlin, `Any` is the root of the class hierarchy (equivalent to Java's
    `object`).
    - SAFETY: High. You cannot treat `Any` as a `String` unless you check it
      first.
    - RUNTIME INFO: It REMEMBERS what it is. A variable of type `Any` holding a
      string knows it is a String at runtime.


    --------|--<>
    THE "MENTAL MODEL" CORRECTION

    If you want a mental mapping, use this instead:

    KOTLIN CONCEPT: `Any`
    C++ EQUIVALENT: `std::any` (C++ 17)
    WHY?            Both hold a value and its type information safely. You can
                    query "what are you?" at runtime.

    KOTLIN CONCEPT: `Any`
    C++ EQUIVALENT: `java.lang.Object`      (more like java translation lol)
    WHY?          : This is the literal translation on the JVM.

    KOTLIN CONCEPT: Native Pointers
    C++ EQUIVALENT: `void*`
    WHY?          : Kotlin usually hides this unless you are doing Kotlin/Native
                    (where you literally have `CPointer<Void>`).




    -----------|--p
    WHY THE DIFFERENCE MATTERS FOR YOUR EXAM

    If you try to use `Any` like a `void*`, you will get confused by SMART CASTS

    IN C++ (`void*`): You have to manually force the type.

    ```C++
    void* ptr = new String("Hello")
    // You MUST blindly trust that this is a string
    String* str = static_cast<String*>(ptr);
    ```


    IN KOTLIN (`Any`): The compiler does the work for you because `Any` keeps
    the type info.

    ```Kotlin
    val obj: Any = "Hello"

    // You check the type, and Kotlin AUTO-CASTS it
    if (obj is String) {
        // obj is automatically treated as String here
        println(obj.uppercase())
    }
    ```


VERDICT: `voidl*` is a RAW MEMORY ADDRESS. `Any` is a POLYMORPHIC OBJECT REFERENCE.


    -----------|--<>
        SIENAR HEAVY INDUSTRIES
    <>--|-----------
* */



/*nunununununununununununununununununununununununununununununununununununununununununununununununununununununununununununu*/


/*
* conntinue to new 4        <-- then we will switch to other modules!
*                               https://gemini.google.com/app/bd659ecdaa303cef
*                               https://gemini.google.com/app/40c2e1110b00cd2a
* finish PPT-5 kotlin
*
* ask more detail about how does the technique of casting <Any?> then back to `as <T>` work in more detail
* ... like I can kind of see how it works but not fully at the same time.
* */







/*
    Amnesiac = a person experiencing a partial or total loss of memory
             = experiencing or relating to a partial or total loss of memory
* */


