
/*
    SUMMARY COMPARISON

    - C++ `struct`
        - HOLDS DATA? - Yes (`int x;`)
        - HAS METHODS? - Yes
        - INSTANCES? - Unlimited (`new Struct()`)
    - C++ `enum` (traditional)
        - HOLDS DATA? - No (Just integers)
        - HAS METHODS? - No
        - Instances? - Integers
    - Kotlin `enum class`
        - HOLDS DATA? - Yes (`val x: Int`)
        - HAS METHODS? - Yes
        - INSTANCES? - Fixed Set (`RED`, `BLUE`)
* */

enum class Suit(val color: String) {
    HEARTS("Red"),
    DIAMONDS("Red"),
    // ... finish the rest
    CLUBS("Black"),
    SPADES("Black");
}

/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/

/*
    You have hit on a really deep language design topic. You are right: the
    reason `enum` doesn't have unlimited instances is simply SEMANTIC
    DEFINITION.

    - ENUMERATION literally means "a complete, ordered listing of all items in
      a collection."
    - If you could make new instances at runtime (like `Color("Purple")`), it
      would no longer be an ENUMERATION; it would just be a standard class.

    ... questions about Structs and "Unlimited Data Dumps".

    ------


    1. THE "STRUCT" EQUIVALENT: `data class`            CHCET // copy, componentN, hashCode, equal, toString

    In C++, you often use `struct` because:
        1. CONCISENESS: You don't want to write getters/setters/private fields.
           You just want `public int x;`.
        2. SEMANTICS: It signifies "This is just a bag of data, not a complex
           object."

    ```C++
    struct PlayerData {
        string name;
        int hp;
    }
    ```

    ```Kotlin
    data class PlayerData(val name: String, val hp: Int)
    ```


    WHY KOTLIN FEELS YOU DON'T NEED A SEPARATE `struct` KEYWORD:

    In Kotlin, classes are already as concise as structs.
    - In Java/C++, a class is verbose (private fields, getters, setters).
    - In Kotlin, `class Point(val x: Int, val y: Int)` is already a one-liner.
    - The `data` keyword just adds the extra "struct-like" behavior (printing
      the values, comparing the values, copying).
* */

/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/



/*
    2. THE PERFORMANCE DIFFEENCE (THE "STACK" vs "HEAP" CATCH)

    This is where your Game Engine background will spot the difference.

    - C++ Struct: Can live on the STACK. It is fast. It's a value type. Copying
      it copies the bits.
    - Kotlin `data class`: Lives on the HEAP (JVM). It is a reference. Passing
      it around passes a pointer.


    DOES KOTLIN HAVE A "VALUE TYPE" (STACK STRUCT)? SORT OF.

    It is valled a `value class` (formerly inline class).
    - It allows you to jwrap a primitive (like `Int`) into a type (like
      `Password`) without the memory overhead of a heap allocation.
    - LIMITATION: Currently, it mostly supports wrapping SINGLE values.
    - FUTURE: Project Valhalla (JVM upgrade) aims to bring true, multi-field
      value types (primitive structs) to Java/Kotlin, exactly like C++ structs.


    3. "UNLIMITED INSTANCES ENUM" = SEALED CLASSES

    You mentioned: "Why did Kotlin not want unlimited instances [for enums]?"

    Actually, Kotlin DOES have a feature that is exactly "An Enum, but with         <-- rmb how nishaan mentioned how `sealed class` is basically `enum class` on steroids???
    unlimited instances of data." That is the SEALED CLASS we discussed earlier.

    Think about it:
    - ENUM: Fixed Types (`RED`), Fixed Instances (The singleton `RED` object).
    - SEALED CLASS: Fixed Types (`Success`, `Failure`), UNLIMITED INSTANCES
      (`Success("User1")`, `Success("User2")`)

    So if you ever feel like "I want an Enum but I need to dump different data
    into it every time," you just switch to a SEALED CLASS.


    SUMMARY for your C++ brain...

    - C++ CONCEPT: `struct` (Data)
    - KOTLIN EQUIVALENT: `data class`
    - Notes: Auto-generates `toString`, ... CHCET... Reference type (Heap).

    - C++ CONCEPT: `struct` (stack)
    - KOTLIN EQUIVALENT: `value class`
    - Notes: Optimization for single-value wrappers (eliminates allocation).

    - C++ CONCEPT: `enum`
    - KOTLIN EQUIVALENT: `enum class`
    - Notes: Just like C++ Enum Class.

    - C++ CONCEPT: `union`/`variant`
    - KOTLIN EQUIVALENT: `sealed class`
    - Notes: Type-safe union. The "Unlimited Instance" Enum.
* */




/*
-_                 _-_                 _-_      .
   \             /     \             /     \    .
    \           /       \           /       \   .
_ _ _\ _ _ _ _ / _ _ _ _ \ _ _ _ _ / _ _ _ _ \ _.
      \       /           \       /           \ .
       \     /             \     /             \.
         -_-                 -_-                .
* */

/*
    Let's break this down into two parts: the Kotlin `value class` analogy and
    the "Stack vs Heap." speed reality.


    1. Is a `value class` like a `type`?

    NO.
    - KOTLIN `typealias` (and Haskell `type`) is just a nickname.
        - `typealias UserId = Int`
        - The compiler sees `UserId` and `Int` as the exact same thing. There is
          zero safety. You can accidentally pass a `Duration` to a `UserId`.
    - KOTLIN `value class` (and Haskell `newtype`) is a WRAPPER.
        - `value class UserId(val id: Int)`
        - The compiler sees `UserId` as a distinct, unique type. You cannot pass
          a raw `Int` to a function expecting `UserId`.
        - THE MAGIC: Even though it is a distinct type, the compiler (when
          possible) rips off the wrapper at runtime and just passes the raw
          `Int` around. This gives you TYPE SAFETY WITHOUT THE MEMORY PENALTY.


    |-----      |-----      |-----      |-----
    SUMMARY
    - KOTLIN FEATURE: `typealias`
    - HASKELL EQUIVALENT: `type`
    - SAFETY: None (Just a label)
    - MEMORY: Zero overhead

    - KOTLIN FEATURE: `value class`
    - HASKELL EQUIVALENT: `newtype`
    - SAFETY: STRICT (Compiler error if mixed up)
    - MEMORY: Zero overhead (mostly)
* */


/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/
/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/


/*
    2. STACK VS. HEAP: WHY IS THE STACK "FASTER"?

    You are absolutely correct: THE STACK IS NOT A CACHE. Both the Stack and the
    Heap live in the exact same RAM sticks in your computer.

    So, why does every game engine developer (like Cherno) obssess over Stack
    allocation? It comes down to two specific reasons: ALLOCATION OVERHEAD and
    CPU CACHE MISSES.


    REASON A: THE "BOOKKEEPER" (ALLOCATION COST)
    - THE STACK: The CPU has a dedicated register called the "Stack Pointer." To
      allocate memory, it essentially just does ONE MATH OPERATION:
      `Pointer - Size`. That's it. One CPU instruction. Instant.
    - THE HEAP: The Heap is a messy pile of free and used blocks. When you say
      `new Object()` (or `malloc` in C++), the operating has to:
        1. Pause.
        2. Search through a list of free memory chunks to find one that fits.
        3. Mark it as "taken" so no one else uses it.
        4. Return the pointer.
        5. THIS IS ASTRONIMICALLY SLOWER THAN JUST SUBTRACTING A NUMBER FROM A
           REGISTER.


    REASON B: THE "CACHE LINE" (LOCALITY - THE BIG ONE)

    This is what Cherno talk about most.
    - CPU CACHE: Your CPU is super fast. RAM is relatively slow. The CPU grabs
      chunks of RAM (64 bytes usually) and puts them in its L1 Cache.
    - STACK: Stack memory is CONTIGUOUS (packed tight together). If you have
      `int x`, `int y`, `int z` on the stack, they sit right next to each other.
      When the CPU fetches `x`, it accidentally grabs `y` and `z` too. Accessing
      `y` is now instantaneous becuase it's already in the cache.
    - HEAP: Heap memory is FRAGMENTED.
        - Your `Player` object might be at address 0x1000.
        - The `Enemy` object might be at 0x9000.
        - To read them both, the CPU grabs the chunk at 0x1000... realizes
          0x9000 isn't there... flushes the cache... and goes all the way back
          to RAM to get 0x9000. This is a CACHE MISS, and it kills game
          performance.


    ADDRESSING YOUR POINTER QUESTION

            "dont both heap and stack still clearly need to like have memory pointers pointed to them regardless???"

    Yes, but with a twist:
    - STACK VARIABLE: You access it directly via offset. "The value is 4 bytes
      from my current position." (Direct access).
    - HEAP VARIABLE: You have a pointer on the stack that points to the heap.
        - STEP 1: Read the pointer from the Stack.
        - STEP 2: Travel to that address in the Heap.
        - That "travel" is the INDIRECTION. It's an extra jump that breaks the
          CPU's flow.


    KOTLIN CONTEXT:
    - `data class`: Lives on the HEAP. Accessing it involves that "pointer jump"
      and allocation cost.
    - `value class` (wrapping an Int): Lives on the STACK (usually). It avoids
      the jump, avoids the allocation search, and sits nicely in the cache.
* */


/*
-_                 _-_                 _-_      .
   \             /     \             /     \    .
    \           /       \           /       \   .
_ _ _\ _ _ _ _ / _ _ _ _ \ _ _ _ _ / _ _ _ _ \ _.
      \       /           \       /           \ .
       \     /             \     /             \.
         -_-                 -_-                .
* */


/*
    ... Memory Management (Stack vs. Heap) and Maps (Key-Value stores).


    1. STACK vs. HEAP memory

    - The Stack
        - A structured, orderly list of variables currently "in-scope" (being used
          right now).
        - WHAT GOES HERE: Simple variables (`val n`, `var a`) and REFERENCES
          (pointers) to objects.
        - SPEED: Extremely fast access...
        - EXAMPLE FROM SLIDES:
            - `val n` stores the actual number `01473` directly on the stack.
            - `val person` stores a MEMORY ADDRESS (`04243`) that points to the
              Heap.

    - The Heap
        - WHAT IT IS: A large, chaotic "cloud" of storage where big objects live
        - WHAT GOES HERE: The actual objects (`Person`, `List`, `String`).
        - EXAMPLE FROM SLIDES:
            - When you create `Person(name="Bob")`, the actual "Bob" data lives
              in the Heap.
            - The Stack just holds a "ticket" (address `04243`) saying "Go to
              the Heap, aisle 4, to find Bob."

    KEY TAKEAWAY: When you opass an object (like a `List` or `Person`) to a
    function, you are actually just copying that small "ticket" (reference) on
    the Stack. You aren't copying the whole object. This is why it's fast!
* */


/*
$$$$$$$$$$$$$$$$""$o$o$o$o$o$oo$$""$$$$$$$$$$$$$$$
$$$$$$$$$$$$""o$$$$$$$$$$"$"$$$$$$$o$"$$$$$$$$$$$$
$$$$$$$$$"$o$$$$""$oo $ ""      """$$$oo"$$$$$$$$$
$$$$$$$"o$$$$"   ""o  $oo o o       ""$$$o"$$$$$$$
$$$$$"o$$$"       oo$$$$$$$$$$o        "$$$o"$$$$$
$$$$"o$$$  $  o$$$$$$$$$$$$$$"$$oo       "$$$ $$$$
$$$"$$$"   "$$$$$$$$$$$$$$$$o$o$$$"        $$$o$$$
$$ $$$    o$$$$$$$$$$$$$$$$$$$$$$$$o o   o  "$$o"$
$"$$$"    o$$$$$$$$$"$$$$$$"" "$$$$$$"$$$$$  $$$"$
$o$$"    o$$$$$$$$$$o""$$$""""ooo"$$$$$$$$"   $$$"
$o$$"    o$$$$$$$$$$            ""oo"$"$o""   $$$o
o$$$     o$$$$$$$$$$                """""$    o$$o
o$$$    o$$$$$$$$$$$$o                   "o "oo$$o
o$$$  oo$$$$$$$$$$$$$$$$ooooooo$$$$$oo    $"$ "$$o
o$$$"  ""  $$$$$$$$$$$$$$$$$$$$$$$$$$$$o    " $$$
$ $$$       "$$$$$$$$$$$$$$$$$$$$$$$$$$$o    o$$"$
$$"$$o       "$$$$$$$$$$$$$$$$$$$$$$$$$$$o   $$$o$
$$o$$$o       $$""$$$$$$$$$$$$$$$$$$$$$$$o  $$$ $$
$$$o"$$o    "$""  "$""$$$$$$$$$$$$$$$$$$$oo$$$"$$$
$$$$o"$$$o        "     $$$$$$$$$$$$$$$$$o$$"o$$$$
$$$$$$o"$$$o         oo$$$$$$$$$$$$$$$$$$$$"o$$$$$
$$$$$$$$o"$$$$ooooo$$$$$$$$$$$$$$$$$$$$$$"o$$$$$$$
$$$$$$$$$$o""$$$$$$$$$$$$$$$$$$$$$$$$$"oo$$$$$$$$$
$$$$$$$$$$$$$o$""$$$$$$$$$$$$$$$$$""oo$$$$$$$$$$$$
$$$$$$$$$$$$$$$$$$o$o$"$"$"$"$oo$o$$$$$$$$$$$$$$$$
* */


/*
    2. MAPS (KEY-VALUE STORES)

    Maps are Kotlin's version of a dictionary or a lookup table. They map unique
    KEYS to VALUES.


    CREATING MAPS

    You use `mapOf` to create them. The syntax uses `to` to pair things up.
* */
// Immutable Map (Read-Only)
val grades_ = mapOf(
    "A*" to 23,
    "A" to 41,
    "B" to 88
)
/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/
/*
    ACCESSING DATA

    You can look up values using the key, just like a list index, but with a
    string (or whatever your key type is).
    - `grades.get("A")` returns `41`.
    - `grades["A"]` is the shorthand syntax.

    IMPORTANT NOTE: If the key doesn't exist (e.g., `grades["F"]`), the map
    returns `null`. This is a major source of bugs...



    MUTABLE MAPS

    Just like Lists, Maps are immutable by default. To edit them, you need
    `mutableMapOf` or convert an existing map with `.toMutableMap()`.
* */
val gradesMutable = grades_.toMutableMap()

/*
fun main() {
    gradesMutable.put("D", 9)       // Adds a new entry
    gradesMutable["E"] = 9          // Does the same thing, cleaner syntax...
}
*/


/*
    3. MAPS ATTRIBUTES & METHODS

    Your slides highlight three critical ways to view the data inside a map.

    - ATTRIBUTE: `.keys`
    - RETURNS: `Set<K>`
    - DESCRIPTION: Returns all keys. It's a SET because keys must be unique (you
      can't have two "A" grades).

    - ATTRIBUTE: `.values`
    - RETURNS: `Collection<V>`
    - DESCRIPTION: Returns all values. It's a generic COLLECTION (like a List)
      because values can duplicate (two people can have score 88).

    - ATTRIBUTE: `.entries`
    - RETURNS: `Set<Entry<K, V>>`
    - DESCRIPTION: Returns a Sert of key-value pairs, useful for looping through
      everything.
* */
        // In Kotlin, Map keys are unique; the map holds only one value for each
        // key. In contrast, the same value can be assosciated with several
        // unique keys.

fun main() {
    gradesMutable.put("D", 9)       // Adds a new entry
    gradesMutable["E"] = 9          // Does the same thing, cleaner syntax...

    for (entry in grades_.entries) {
        println("${entry.key} scored ${entry.value}")
    }


    /*
    4. ADVANCED: `groupBy` (FUNCTIONAL POWER)

    This is a very powerful function shown in your slides. It takes a LIST and
    transforms it into a MAP based on a rule you define.

    EXAMPLE FROM SLIDE: You have a list of letters:
        `listOf("a", "b", "c", "a", "b")`.
* */

    // Group by the item itself.
    val letterList = listOf("a", "b", "c", "a", "b")
    val letterFreq = letterList.groupBy { it }
        // Result: Map("a" to ["a", "a"], "b" to ["b", "b"], "c" to ["c"])


/*
    It creates a map where the KEY is the criteria (e.g., the letter) and the
    VALUE is a List of all items that matched that criteria. This is incredibly
    useful for:
        - Histograms (counting frequency).
        - Categorizing items (e.g., `people.groupBy { it.city }`)
* */



    val a33: Set<Int> = setOf()





    /*

    enum class City { London, Paris, Liverpool, Berlin, NewYork, Chicago, Melbourne, Sydney }

    class Airport(val name: String, val city: City)

    val airports = mapOf(
        "LHR" to Airport("Heathrow", City.London),
        "JFK" to Airport("John F Kennedy", City.NewYork),
        "LPL" to Airport("John Lennon", City.Liverpool)
    )




    enum class City { London, Paris, Liverpool, Berlin, NewYork, Chicago, Melbourne, Sydney }

    class Airport(val name: String, val city: City)

    fun byName(airports: Map<String, Airport>): Map<String, Airport> {
        val temp: MutableMap<String, Airport> = mutableMapOf()
        for (airport in airports) {
            temp[airport.value.name] = airport.value
            // temp.put(airport.value.name, airport.value)
        }
        return temp
    }




    enum class City { London, Paris, Liverpool, Berlin, NewYork, Chicago, Melbourne, Sydney }

    class Airport(val name: String, val city: City)

    fun byCity(airports: List<Airport>): Map<City, List<Airport>> = airports.groupBy { it.city }
    //
    * */
    // val a22 = listOf(3, 4, 5).groupBy


}





/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/



/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/






/*
*
*
*
*
* Scenario: You are coding a card game. You need a suit type (HEARTS, CLUBS, etc.). You also want to store the "color" of the suit (Red or Black).
*
*
*
* You have hit on a really deep language design topic. You are right: the reason enum doesn't have unlimited instances is simply semantic definition.
* */





/*
    Enumeration = The action of MENTIONING a number of things one by one.
* */


class `Jan-14` {
}