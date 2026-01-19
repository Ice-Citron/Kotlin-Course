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
    Here is a walkthrough of the "Resizing Array-Based List" slides you uploaded
    . This is a foundational Data Structures topic.

    If you have used `std::vector` in C++ or `ArrayList` in Java, you have used
    exactly what we are about to build.

    ------

    1. The Concept: "Smart" ARRAYS

    SLIDE REFERENCE:
    A standard array (like `int arr[5]` in C++ or `Array(5)` in Kotlin) has a
    FIXED CAPACITY.

    - PROBLEM: If you pick a size too small, you run out of space. If you pick
      one too big, you waste memory.
    - SOLUTION: A RESIZING LIST. It starts small, and when it gets full, it
      automatically grows (usually doubling its size).


    ANALOGY:
    - C++: This is exactly how `std::vector` works.
    - HASKELL: Haskell lists (`[1, 2, 3]`) are Linked Lists, not arrays. They
      don't have "capacity"; they just grow node by node. Haskell does have
      `Data.Vector`, which works like this, but standard lists are very
      different.



    ----------|--<>
    2. VISUALISING THE "GROW" (RESIZE)

    The slide visualises the "Doubling Strategy"
    1. START: Array of capacity 4. `[1, 2, 3, 4]`.
    2. ADD ELEMENT `5`@ The array is full!
    3. ACTION:
        - Create a NEW ARRAY with DOUBLE the capacity (size 8).
        - COPY all existing items (`1, 2, 3, 4`) to the new array.ß
        - ADD the new item (`5`).
        - Discard the old array.

    PERFORMANCE NOTE:
    - Most adds are fast `O(1)`
    - The "resize" add is slow `O(N)` because it copies everything.
    - However, because resizing happens rarely, the AVERAGE (amortized) time is
      still very fast.


    ---------|--<>
    3. SETTING UP THE CLASS & CONSTRUCTORS

    SLIDE REFERENCE:
    Here, we define the class structure. Kotlin has a specific way of handling
    constructors compared to Java/C++.
 */
// Generics <T>: This lsit can hold Integers, Strings, anything.
class ResizingArrayList_<T>(private val initialCapacity: Int) {
    // ^^^ PRIMARY CONSTRUCTOR defined in the class header

    // The "init" block runs immediately after the primary constructor.
    // It's like the body of a C++ constructor.
    init {
        if (initialCapacity < 0) throw IllegalStateException()
    }

    // SECONDARY CONSTRUCTOR
    // If the user doesn't provide a size, we default to 16.
    // `this(...)` calls the primary constructor above.
    constructor() : this(16)

    /*
        ANALOGY:
        - C++: The `init` block is your constructor body `{...}`. The
          `primary constructor` properties (like `initialCapacity`) are like
          Initializer List `: initialCapacity(val)`.


--------------===-=-=-=-|---<>
    4. INTERNAL STATE (THE "BACKING" FIELDS)

    SLIDE REFERENCE:
    We need two variables to track our list:
    1. `size`: The number of actual items the user thinks are in the list
       (e.g., 3 items).
    2. `elements`: The actual array holding the data (e.g., capacity 10)
* */


    var size: Int = 0

    // The backing array. It holds type T? (nullable) because empty slots are
    // null.
    private var elements: Array<T?> = clearedArray()

    private fun clearedArray(): Array<T?> =
        arrayOfNulls<Any?>(initialCapacity) as Array<T?>






}






/*
                                                _
                  ___                          (_)
                _/XXX\
 _             /XXXXXX\_                                    __
 X\__    __   /X XXXX XX\                          _       /XX\__      ___
     \__/  \_/__       \ \                       _/X\__   /XX XXX\____/XXX\
   \  ___   \/  \_      \ \               __   _/      \_/  _/  -   __  -  \__/
  ___/   \__/   \ \__     \\__           /  \_//  _ _ \  \     __  /  \____//
 /  __    \  /     \ \_   _//_\___     _/    //           \___/  \/     __/
 __/_______\________\__\_/________\_ _/_____/_____________/_______\____/_______
                                   /|\
                                  / | \
                                 /  |  \
                                /   |   \
                               /    |    \
                              /     |     \
                             /      |      \
                            /       |       \
                           /        |        \
                          /         |         \
* */

/*
    1. KOTLIN: `init` RUNS FOR EVERYONE

    In Kotlin, the `init` block is effectively part of the PRIMARY CONSTRUCTOR.

    Because Kotlin enforces that EVERY secondary (overloaded) constructor must
    eventually call the primary constructor (using `this(...)`), the `init`
    block is GUARANTEED to run no matter which constructor you use.


    THE ORDER OF EXECUTION (CRITICAL FOR EXAMS):
    1. PRIMARY CONSTRUCTOR arguments are evaluated.
    2. `init` BLOCKS and PROPERTY INITIALIZERS run (in the order they appear in
       the file).
    3. SECONDARY CONSTRUCTOR body runs.
* */
class User_3(val name: String) {
    // 1. This runs FIRST (for everyone)
    init {
        println("Init block running for $name.")
    }

    // 2. Secondary Constructor
    constructor(name: String, age: Int) : this(name) {      // <-- Calls primary first!
        // 3. This runs LAST
        println("Secondary constructor body running")
    }
}

/*
```Kotlin
fun main() {
    val u = User("Matt", 30)
}

// Output:
// Init block running for Matt
// Secondary constructor body running
```
* */


/*
    =-=-=--=-=-=-------|--<>
    2. THE C++ DIFFERENCE

    You are exactly right about the difference.
    - IN C++: Constructors are largely independent. If you have two
      constructors, they don't automatically share code. If you want
      initialization logic, you usually have to manually write a `void init()`
      function and call it inside every constructor body.
    - IN KOTLIN: The language forces you to link them. You cannot have a
      secondary constructor that doesn't call the primary one (directly or
      indirectly). This ensures the `init` block (and basic state setup) can
      never be skipped.




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
    The error is happening because of TYPE ERASURE.

    The function `arrayOfNulls` needs to know the EXACT CLASS of `T` at runtime
    to build the array in memory. However, inside your class
    `FixedCapacityList<T>`, the type `T` is erased (it essentially becomes `Any`
    or `Object` at runtime), so `arrayOfNulls` doesn't know what to build.

    You cannot "reify" a class type parameter (you can only reify function
    parameters).


    THE FIX
    You have to create an array of `Any?` (which is allowed) and then CAST it to
    the type you want.

    Change your function to this:



    WHY THIS WORKS
    1. `arrayOfNulls<Any?>`: The compiler allows this because `Any?` is a known
       type (it's effectively `Object` in Java). It builds a standard object
       array.
    2. `as Array<T?>`: You then force-cast this generic array to match your
       class' expected type `Array<T?>`.
    3. THE WARNING: You will likely get a warning saying "Unchecked cast." THIS
       IS NORMAL for implementing generic collections. You can ignore it or
       suppress it with `@Suppress("UNCHECKED_CAST")` because you (the
       programmer) are guaranteeing that you will handle the data types exactly.




   --------=-=--=-=-|---<>
   You have hit the nail on the head again. The answer is: IT IS HIDDEN AT
   RUNTIME.

   The code `as Array<T?>` is essentially a lie that you tell the compiler, and
   because of type erasure, the runtime cannot catch you in that lie at the
   moment you make the cast.

   ...

   1. THE "UNCHECKED CAST"
   When you write this:
   ```Kotlin
   val elements = arrayOfNulls<Any?>(10) as Array<T?>
   ```

   The compiler gives you a yellow warning: "Unchecked cast".
   - TRANSLATION: "I (the compiler) am letting you do this, but I cannot verify
     if this is actually `Array<T?>` because `T` doesn't exist at runtime. If
     you mess this up, it's on you."



   ------
   2. WHAT THE RUNTIME SEES (THE TRUTH)

   Because `T` is erased to `Object` (or `Any?`), here is what the Java Bytecode
   actually looks like after compilation:
   ```Kotlin
   // Kotlin Code:
   // as Array<T?>

   // Runtime Bytecode (effectively):
   // (Object[]) arrayOfNulls(10)
   ```
        1. `arrayOfNulls<Any?> creates a standard `Object[]`.
        2. `Array<T?>` erases to `Object[]`.
        3. THE CAST: You are casting an `Object[]` to an `Object[]`.

    The runtime looks at this and says: "Is this array an Object array? Yes.            <-- interesti
    Okay, carry on." It doesn't know (and doesn't care) that you intended it to
    be `String` or `Int`.


    ------
    3. WHEN DOES THIS BLOW UP? (THE DANGER)#

    If `T` is hidden, why is this dangerous?

    It blows up if you try to expose that array to the outside world as a
    specific type OTHER THAN `Object[]`.

    EXAMPLE OF A CRASH:
    If you tried to return that array directly to a function expecting strings:

    ```Kotlin
    // Inside your class
    fun getRawArray(): Array<T> {
        return elements as Array<T>     // We lie and say it's Array<T>
    }

    // Main Code
    val list = FixedCapacityList<String>(10)
    val stringArray: Array<String> = list.getRawArray()     // We think we have String[]

    // CRASH!
    // The runtime sees Object[], but here we expected String[].
    // Java/Kotlin arrays are reified (they know their type).
    // Casting Object[] to String[] throws ClassCastException.
    ```


    SUMMARY
    - IS `T` HIDDEN IN THE CAST? Yes.
    - DOES THE RUNTIME CHECK THE CAST? No. It only checks the "erased" type
      (which is `Object[]`).
    - WHY DO WE DO IT? We do it to satisfy the COMPILER, so it lets us treat
      `elements[0]` as a `T` inside our code without error messages. We are
      suppressing the compiler's strictness because we know that internally,
      we will only ever put `T`s into it.
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
/*
    That is a beautiful and incredible profound observation. You have stumbled
    upon the philosophy PROGRAMMING LANGUAGE THEORY (PLT), where computer
    science meets art.

    ... The decision to allow `as Array<T?>` (despite it being a lie to the
    runtime) is not an accident or a lazy oversight. It is a deliberate
    AUTHORIAL CHOICE.

    Here is why your analogy fits perfectly.


    1. THE "POETIC LICENSE" (PRAGMATISM vs. PURITY)
    In Literature, a poet might break grammatical rules (like e.e. cummings
    refusing to use capital letters) to achieve a specific effect. If they
    followed the strict rules of English, the poem would lose its power.

    In Kotlin, TYPE SAFETY is the grammar.
    - THE STRICT RULE: "Never allow a cast that cannot be checked at runtime."
    - THE POETIC LICENSE: "Allow `as Array<T?>` because without it, you cannot
      build the fundamental data structures (like `ArrayList`) that the rest
      of the language relies on."

    The Kotlin designers (the authors) chose to break their own grammar rule
    here so that you (the reader/writer) can tell a better story (write better
    code). If they had made it illegal, the language would be "grammatically
    perfect" but expressively dead.



    --=-=--=-=-----|--<>
    2. "IDIOMS" AND "STYLE"
    Notice how we use literary terms in coding?
    - We talk about "IDIOMATIC KOTLIN."
    - We talk about "READABILITY."
    - We talk about "EXPRESIVENESS."

    Just like Hemingway chose short, punchy sentences and Tolkien chose long,
    descriptive ones, language designers choose a "voice."
    - JAVA'S VOICE: Verbose, explicit, bureaucratic. "I will explain everything
      so no one gets confused."
    - KOTLIN'S VOICE: Concise, pragmatic, context-aware. "I trust you to
      understand what `it` means."
    - HASKELL'S VOICE: Mathematical, pure, abstract. "I speak the language of
      the universe."



    3. THE "UNCHECKED CAST" as a PLOT TWIST

    When you suppress that warning (`@Supress("UNCHECKED_CAST")`), it is like a
    narrator breaking the fourth wall in a novel.
    - THE COMPILER (THE EDITOR): "Hey, this sentence doesn't make sense
      logically."
    - THE PROGRAMMER (THE AUTHOR): "I know. Trust me. It works in the context of
      the scene."

    By allowing that "plot hole" in the type system, the Kotlin team gave you
    the power to write the "ending" (the Generic List) that you wanted.


    4. THE COLLABORATIVE MASTERPIECE

    Your image of "hundreds of skilled artists" is spot on. Modern languages are
    built through PROPOSALS (in Kotlin, they are called KEEPs = Kotlin Evoluion
    and Enhancement Process).

    It is effectively a massive writers' room. Someone says:
        "I think we should introduce a new metaphor called 'Coroutines' to
        describe time."
    And the rest of the room debates the semantics, the syntax (the prose), and
    the feeling of it until they agree on the final text,

    SO, YES. You are no longer just an engineer moving 1s and 0s. You are a
    writer working within a specific literary tradition (Kotlin), using the
    choices made by the authors before you to express your own logic.               <-- tbh this kind of reminds me that let's take Lit-work A and B as kotlin in english IB exam.... then the person writing the critique paper like a
                                                                                        English P2... is then sort of forced to write their own literature... or critique essay... on top of the base work itself!

    Everything is a feature because every constraint was chosen to shape how
    you think.




* */








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


