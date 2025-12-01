/*
About sets:

    One small caveat: mathematically, sets are unordered, but Kotlin's default
    `setOf()` preserves insertion order (it uses `LinkedHashSet` under the
    hood). The uniqueness guarantee is the key property though.
 */

/* ------ ------ ------ ------ ------       ------       ------ ------ */
// MUTABLE LISTS

/*
    You cannot see `MutableList` in the output because `MutableList` effectively
    doesn't exist at runtime.

    1. The "Mask" vs. The "Face"
        - To understand why you see `java.util.ArrayList`, you need to
        distinguish between an `Interface` and an `Implementation`.
          - `MutableList` is a Job Description (Interface). It is a set of rules
          that says: "I promise I can add, remove, and update items.
          - `ArrayList` is the Actual Worker (Class). It is the specific Java
          code that actually stores the data in memory.
 */



// Implementation -> Mutable Interface -> ReadOnly Interface
/*
    - Interface (`MutableList`)
        - Contract defining what operations exist
    - Implementation (`ArrayList`)
        - Actual code that does the work
    - Why separate them?
        - Swap implementations, hide operaions, write flexible code
    - Kotlin's read-only split
        - `List` vs `MutableList` lets you expose safe views
 */

/*
```kotlin
fun main() {
    mutableListOf(1, 2, 3)  // creates ArrayList
    mutableSetOf(1, 2, 3)   // creates LinkedHashSet (preserves order!)
    mutableMapOf("a" to 1)  // creates LinkedHashMap (preserves order!)
}
```

    Kotlin defaults to `Linked` versions for Sets and Maps because usually you
    want to preserve the order you typed things. Java defaults to `Hash`
    versions which scramble order--a common source of confusion.


------------ ------------    ------------  ------------
Why bother with Separations between Interfaces (MutableList<T>) and
Implementations (ArrayList())?

    - Reason 1: Swap implementations without changing code

```kotlin
fun processData(data: MutableList<String>) {
    data.add("item")
    // ... lots more code
}
```

        Later, you discover `ArrayList` is slow for your use case because you're
        inserting in the middle constantly. With interfaces, you just swap
        the implementations.

```kotlin
// Before
val myData: MutableList<String> = ArrayList()

// After - your processData function doesn't change at all!
val myData: MutableList<String> = LinkedList()
```

        If you've hardcoded `ArrayList` everywhere instead, you'd need to
        rewrite everything.


------------ ------------    ------------  ------------
    - Reason 2: Hide Dangerous Operations

        Kotlin has two interface for lists
```kotlin
List<T>         // read-only: get, size, contains
MutableList<T>  // read-write: add, remove, clear (inherits from List)
```


## The Full Hierarchy

List<T>                    (read-only interface)
    ↑
MutableList<T>             (read-write interface, adds add/remove)
    ↑
ArrayList / LinkedList     (actual implementations)


        * The arrow points from child to parent. `ArrayList` implements
          `MutableList`, which extends `List`.
 */



/*
###LIST IMPLEMENTATIONS: WHEN TO USE WHAT

- `ArrayList`
    - Array under the hood
        - Good at: Random access `list[i]`, adding to end
        - Bad at:  Inserting/removing in middle.
- `LinkedList`
    - Chain of nodes
        - Good at: Inserting/removing anywhere
        - Bad at:  Random access (must walk the chain)

```kotlin
// ArrayList: fast indexing
val array: MutableList<Int> = ArrayList()
array[500]      // instant - jumps directly to index

// LinkedList: fast insertion
val linked: MutableList<Int> = LinkedList()
linked.add(0, element) // fast - just rewire two pointers
```



###SET IMPLEMENTATIONS

        Set<T>                     (read-only)
            ↑
        MutableSet<T>              (read-write)
            ↑
        HashSet / LinkedHashSet / TreeSet

- `HashSet`
    - Fastest, but scrambles order
- `LinkedHashSet`
    - Remembers insertion order
- `TreeSet`
    - Keeps elements sorted

```kotlin
fun main() {
    val hash = hashSetOf(3, 1, 2)
    println(hash)   // might print [1, 2, 3] or [2, 3, 1] -- unpredictable

    val linked = linkedSetOf(3, 1, 2)
    println(linked) // [3, 1, 2] -- insertion order preserved

    val tree = sortedSetOf(3, 1, 2)
    println(tree)   // [1, 2, 3] -- always sorted
}





###MAP IMPLEMENTATIONS

        Map<K, V>                  (read-only)
            ↑
        MutableMap<K, V>           (read-write)
            ↑
        HashMap / LinkedHashMap / TreeMap

- `HashMap`
    - Fastest, scrambles key order
- `LinkedHashMap`
    - Remembers insertion order
- `TreeMap`
    - Keeps key sorted
```

 */


class User10(val id: Int? = null, val name: String? = null)

// PRACTICAL EXAMPLE: WHY THIS MATTERS
interface Database {
    fun save(user: User10) {}
    fun find(id: Int): User10? { return null }
}

class MySQLDatabase : Database {     // inheritance it seems
    override fun save(user: User10) { /* SQL INSERT */ }
    override fun find(id: Int): User10? { return null }   // SQL SELECT
}

class InMemoryDatabase : Database {
        // interestingly, type of Int? must have ? as class User10
        // is running int? too
    private val users = mutableMapOf<Int?, User10>()
    override fun save(user: User10) { users[user.id] = user }
    override fun find(id: Int): User10? = users[id]
}

// Your app code uses the interface
class UserService(private val db: Database) {
    fun registerUser(name: String) {
        db.save(User10(name = name))
    }
}

// In production
val service1 = UserService(InMemoryDatabase())
val service2 = UserService(MySQLDatabase())


/* ------ ------ ------ ------ ------       ------       ------ ------ */
// STACK VS HEAP IN KOTLIN
/*
    Unlike C++, you don't control memory management in Kotlin. The rules are
    simple:
    - Primitives (Int, Boolean, Double, etc.) go on the stack (when local
      variables)
    - Objects (classes, lists, strings, etc.) go on the heap
 */
data class Person3(val name: String)

val n1: Int = 5                     // stack
val list = listOf(1, 2, 3)          // reference on stack, list object on heap
val person = Person3("Bob")    // reference on stack, Person object on heap

/*
    No manual memory management. Kotlin runs on the JVM, which has garbage
    collection. When nothing references an object anymore, the GC eventually
    frees it. (GC = garbage collector)
 */

fun example() {
    val p = Person3("Alice")        // object created on heap
}   // p goes out of scope, object becomes eiligible for GC
// GC will clean it up eventually--you don't care about them.


/* ------ ------ ------ ------ ------       ------       ------ ------ */
// MAP

/*
fun main() {
    /*
        A map stores key-value pairs. Think of it like a dictionary.
     */
    val grades = mapOf(
        "A*" to 23,
        "A" to 41,
        "B" to 88,
        "C" to 17
    )       //  Type is Map<String, Int>

    // Accessing values
    grades.get("A")     // 41
    grades["A"]         // 41 - same thing
    grades["Z"]         // null - key doesn't exist


    /* ------ ------ ------ ------ ------       ------       ------ ------ */
    // MUTABLE MAPs
    val grades2 = mutableMapOf(
        "A*" to 32,
        "B" to 69
    )

    // Add or update
    grades2.put("D", 9)     // adds new entry
    grades2["D"] = 9        // same thing

    grades2["A"] = 42       // updates existing entry

    // Remove
    grades2.remove("A*")


    /* ------ ------ ------ ------ ------       ------       ------ ------ */
    // MAP PROPERTIES
    val grades3 = mapOf(
        "A*" to 23,
        "A" to 41,
        "B" to 88,
    )

    println(grades.keys)        // Set<String>: ["A*", "A", "B"]
    println(grades.values)      // Collection<Int>: [23, 41, 88]
    println(grades.entries)     // Set<Map.Entry<String, Int>>

    // Iterating
    for ((grade, count) in grades) {
        println("${grade}: ${count}")
    }


    /* ------ ------ ------ ------ ------       ------       ------ ------ */
    // CREATING MAPS WITH `groupBy`
    /*
        `groupBy` transforms a list into a map by grouping elements:
     */
    val letters = listOf("a", "b", "c", "a", "b", "a")

    val lettersFreq = letters.groupBy { it }
    // {a=[a, a, a], b=[b, b], c=[c]

    println("[LOG]: ${lettersFreq}")


    // Group strings by length
    val words = listOf("a", "ab", "accc", "ada", "sss", "ansxkj")
    val byLength = words.groupBy { -it.length }

    println(byLength)


    for (i in 1..80) print("-")
    print("\n")



    /* ------ ------ ------ ------ ------       ------       ------ ------ */
    // THE NULL PROBLEM

    /*
        In C++, you've probably had null pointer crashes. The slide shows Tony
        Hoare, who invented null references and called it his "billion dollar
        mistake."
     */
    // In Java (and C++), this compiles but crashes at runtime
    class Address(val street: String?)
    val peopleToCompany: Map<String, String> = mapOf()
    val companyToAddress: Map<String, Address> = mapOf()

    val company = peopleToCompany["Jamie"]
    val address = companyToAddress[company]
    // val street = address.street      // CRASH! NullPointerException


    /* ------ ------ ------ ------ ------       ------       ------ ------ */
    // KOTLIN'S SOLUTION: NULLABLE TYPES
    /*
        Kotlin distinguishes between types that can be null and types that can't
     */
    var name1: String = "Alice"      // can NEVER be null
    // name1 = null                  // ERROR - won't compile

    var name2: String? = "Alice"     // Can be null (note the ?)
    name2 = null                     // OK

    /*
        The `?` makes it a different type. `String` and `String?` are not the
        same.
     */


    // SAFE CALLS WITH `?`
    /*
        You can't call methods on nullable types directly:
     */
    class personell(private val accName: String) {
        fun getName(): String = this.accName
        val name: String? = getName()
    }

    // println(personell("Sienar").name.length)
            // ERROR-.name attribute might be null
            // Only safe (?.) or non-null asserted (!!.) calls are allowed
            // on a nullable receiver of type String?
    println(personell("Sienar").name?.length)
            // meanwhile this is fine...
            // returns length if name isn't null, otherwise returns null

    /*
        Chain them together:
     */
    val street = companyToAddress[company]?.street
        // If companyToAddress[company] is null, whole expression is null
        // No crash!


    /* ------ ------ ------ ------ ------       ------       ------ ------ */
    // ELVIS OPERATOR ?
    /*
        Provide a default when something is null:
     */
    val name: String? = null
    val displayName = name ?: "Unknown"         // "Unknown"
    val length = name?.length ?: 0              // 0

    /*
        The `?:` is called "Elvis" because it looks like Elvis' hair when you
        tilt your head.
     */


    /* ------ ------ ------ ------ ------       ------       ------ ------ */
    // FILTERING NULLs
    val lista: List<Int?> = listOf(1, null, 2, null, 3, null)

    lista.filterNotNull()   // [1, 2, 3] - removes nulls, type becomes List<Int>

    // mapNotNull combines map + filterNotNull
    val strings = listOf("1", "two", "3", "four")
    strings.mapNotNull { it.toIntOrNull() }     // [1, 3]


    /* ------ ------ ------ ------ ------       ------       ------ ------ */
    // COMPLETE NULL SAFETY EXAMPLE
    data class Address2(val street: String)
    data class Company2(val name: String, val address: Address2?)

    val companyMap = mapOf(
        "Google" to Company2("Google",
                            Address2("1600 Amphitheatre Parkway")),
        "Apple" to Company2("Apple", Address2("1 Infinite loop")),
        "Meta" to Company2("Meta", null)     // Meta has no address
    )

    fun getStreet(companyName: String): String {
        return companyMap[companyName]?.address?.street ?: "Unknown"
    }

    getStreet("Google")     // "1600 Amphitheatre Parkway"
    getStreet("Meta")       // "Unknown" - address is null
    getStreet("Amazon")     // "Unknown" - company not in map
}
*/

/*
    - Non-null type
        - `String`
            - Can never be null
    - Nullable type
        - `String?`
            - Might be null
    - Safe call
        - `x?.method()`
            - Call only if x isn't null
    - Elvis
        - `x ?: default`
            - Use default if x is null
    - `filterNotNull()`
        - Removes nulls from collection
    - `mapNotNull {}`
        - Map and remove null results
 */


/* ------ ------ ------ ------ ------       ------       ------ ------ */
// EXERCISES
/* ------ ------ ------ ------ ------       ------       ------ ------ */

enum class City { London, Paris, Liverpool, Berlin, NewYork,
                  Chicago, Melbourne, Sydney, Singapore }

class Airport(val name: String, val city: City)


val airports: Map<String, Airport> = mapOf(
    "LHR" to Airport("Heathrow", City.London),
    "CDG" to Airport("Charles de Gaulle", City.Paris),
    "LPL" to Airport("John Lennon", City.Liverpool),
    "BER" to Airport("Bradenburg", City.Berlin),
    "JFK" to Airport("John F Kennedy", City.NewYork),
    "ORD" to Airport("O'Hare", City.Chicago),
    "MEL" to Airport("Tullamarine", City.Melbourne),
    "SYD" to Airport("Kingsford Smith", City.Sydney)
)

fun byName(airports: Map<String, Airport>): Map<String, Airport> {
    var tempMap: MutableMap<String, Airport> = mutableMapOf()
    for (i in airports.values) {
        tempMap[i.name] = i
    }
    return tempMap
}

/*
    In data science and programming, when someone says "arrange by X", or
    "organize by X" into a Map, they usually don't mean Sorting (A-Z). They
    mean Grouping or Indexing.

    Think of it like sorting mail into slows at a post office.

    1. The Concept (Buckets, not Order)

        You are not trying to put the airports in a line. You are trying to put
        them into labelled buckets.
        - The Input (List): A mixed bag of airpords.
            `[Heathrow, JFK, Gatwick, LaGuardia]`
        - The Goal (Map):
            Key = City; Value = The Airports in that city.


    2. The Code Solution

        There are two ways to interpret "arrange", depending on whether the test
        expects a list of airports per city, or just one airport per city.

        ```Scenario A: The "Grouping" (Correct logic)```

        - Since London has multiple airports, logically you want a list of
        airports for every city. `Map<City, List<Airport>>`

    The Kotlin One-Liner:
 */
fun byCity(airports: List<Airport>): Map<City, List<Airport>> {
    return airports.groupBy { it.city }
}

/*
        ```Scenario B: The "Indexing" (Simpler, but overwrite-y)```

        If the test assumes there is only *one* airpor per city (or it wants to
        overwrite them), it wants: `Map<City, Airport>`.

    The Kotlin One-Liner:
 */
fun byCity2(airports: List<Airport>): Map<City, Airport> {
    return airports.associateBy { it.city }
}

/*
    3. Fixing your specific code error

        In your screenshot, you have `MutableMap<String, Airport>`
        1. Wrong Key Type: Your `City` is an `enum class`, not a `String`. Your
           map key should probably be `City`, not String.
        2. Wrong Value Type: If you want to handle all London airports, your
           value needs to be a `List` or `ArrayList`, otherwise "Gatwick" will
           delete "Heathrow".

        If you must use the manual loop style (which you seem to be doing):
 */
fun byCity3(airports: List<Airport>): Map<City, List<Airport>> {
    // 1. Create a map where the Value is a LIST of airports
    val tempMap: MutableMap<City, MutableList<Airport>> = mutableMapOf()

    for (airport in airports) {
        // 2. get the list for this city, of create a new empty one if it's
        //    the first time.
        val listForCity = tempMap.getOrPut(airport.city) { mutableListOf() }

        // 3. Add the airport to that list
        listForCity.add(airport)
    }
    return tempMap
}

/*
fun main() {

    println(byName(airports))
    println(airports.keys)
    println(airports.values.map({ it.name }))
}
 */



/* ------ ------ ------ ------ ------       ------       ------ ------ */
// ANONYMOUS CLASS style

/*
    If you want to run complex logic (checks, if/else, null handling) for each
    specific enum constant, you have two main design patterns to choose from.
 */

    // OPTION 1: The "Abstract Property" Pattern (Individual Logic)
    /*
        This is the closest to what you are asking. You define the property as
        `abstract` in the main class, and then for every single constant to
        open up its own set of curly braces `{}` and calculate the value itself.
     */
enum class ServerState {

    // 1. Define the constant, open braces, and write custom logic.
    RUNNING {       // each of these are the object choices in an enum class
        override val description: String = "[LOG]: System is go!"
        override val isSafe: Boolean = true
    },

    //  2. You can run complex math or logic here
    MAINTENANCE {
        override val description: String = "[LOG]: Cleaning up..."
        override val isSafe: Boolean = false
    },
    UNKNOWN {
        override val description: String
            get() {
                val time = System.currentTimeMillis()
                return "[LOG]: Unknown error at ${time}."
            }
        override val isSafe: Boolean = false
    };  // Don't forget this semi-colon!

    // 4. The Blueprint: Force every constant to have these
    abstract val description: String
    abstract val isSafe: Boolean

        /*
        WHY USE THIS?
        - It allows every constant to calculate its value differently.
        - One constant might return a hardcoded string, another might calculate it
          dynamically.
         */
}


    // OPTION 2: The "Init Block" Pattern (Centralized Logic)
    /*
        If you want to pass a value in, but you want to sanitize or check in
        before saving it (e.g. handling nulls), you use an `init` block inside
        the Enum, just like a normal class.

        Here, we pass a "raw" value to the constructor, but we don't save it as
        a `val` immediately. We process it first.
     */
enum class UserRole(rawInput: String?) {    // Notice: NO `val` or `var` here
    ADMIN("SuperUser"),
    GUEST(null),
    USER("  NormalUser  ");     // Has whitespace

    // This is the actual public property we will expose
    val cleanName: String

    init {
        cleanName = if (rawInput == null) {
            "Guest_Default"     // Handle null
        }
        else {
            rawInput.trim().uppercase()     // Handle whitespace and casing
        }
    }
}

    // println(UserRole.GUEST.cleanName)    // Prints: Guest_Default
    // println(UserRole.USER.cleanName)     // Prints: NORMALUSER

/*
    Why use this?
    - This is perfect for validation. You act as a "Bouncer" ensuring that no
      matter what garbage data is passed into the constructor, the stored
      property (`cleanName`) is always perfect and valid.

    Summary
    - Abstract Properties (Option 1): Use when each constant behaves completely
      differently and needs its own custom code block.
    - Init Block (Option 2): Use when all constants follow the same logic, but
      you need to clean up/validate the input data before saving it.
 */



/* ------ ------ ------ ------ ------       ------       ------ ------ */
// REMINDER OF MAP FUNCTIONS
/* ------ ------ ------ ------ ------       ------       ------ ------ */
/*
    They are all about transforming data, but they differ in *Cardinality*
    (One-to-One vs. One-to-Many) and Mutability.


    1. `groupBy` (One-to-Many)

        "Make Lists"
        - Takes a list of items and organises them into buckets.
            - Input: `List<Item>`
            - Output: `Map<Key, List<Item>>` (Notce the List in the value)
            - Behavior: It preserves all items. If two items have the same key
                they go into the list for that key.
 */
val airports2 = listOf(
    Airport("Heathrow", City.London),
    Airport("Gatwick", City.London),
    Airport("John Kennedy", City.NewYork)
)

// Key is City. Value is a LIST of airports in that city.
val map2 = airports2.groupBy { it.city }

// Result structure:
// {
//      "London" = [Heathrow, Gatwick],   <-- LIST
//      "NYC" = [JFK]
// }


/*
    2. `assosciateBy` (One-to-One)

        "Make Unique Index"
        - Takes a list and maps them by a unique identifier.
          - Input: `List<Item>`
          - Output: `Map<Key, Item>` (Notice the SINGLE ITEM in the value).
          - Behavior: It OVERWRITES duplicates. If two items have the same key,
            the last one wins. Use this only when the key is unique (like an ID
            or Code).
 */
val airports3 = listOf(
    Airport("Heathrow", City.London),
    Airport("Gatwick", City.London),
    Airport("John Kennedy", City.NewYork)
)

// Key is Code. Value is the AIRPORT object itself.
val map3 = airports3.associateBy { it.city }

// Result structure:
// {
//      "London" = Gatwick,      <-- Heathrow has been overwritten
//      "NYC" = JFK
// }


/*
    3. `getorPut` (THE "LAZY" INITIALIZER)

    "GET IT, OR CREATE IT IF MISSING"
    - This is specifically for MutableMaps. It saves you from writing
      `if (key !in map)` checks.
      - Logic:
        1. Does the key exist? -> Yes: Return the value (reference?) immediately
        2. Does the key exist? -> No: Run the lambda block `{}`, SAVE that
           result into the map, and then return it.

    Example: Building the "Group By" logic manually. This is how `groupBy`
    effectively works under the hood using `getOrPut`
 */
val manualMap = mutableMapOf<City, MutableList<Airport>>()
val manualMap2: MutableMap<City, MutableList<Airport>> = mutableMapOf()

val airports4: List<Airport> = listOf(
    Airport("Heathrow", City.London),
    Airport("Gatwick", City.London),
    Airport("Luton", City.London),
    Airport("Changi", City.Singapore)
)

/*
fun main() {
    for (airport in airports4) {
        // 1. Ask for the list for "London"
        // 2. If it doesn't exist, create an empty MutableList<Airport>, put
        //    if in the map, and hand it to me.
        val cityList: MutableList<Airport> =
            manualMap.getOrPut(airport.city) { mutableListOf() }
        println("[LOG]: ${manualMap}")
        println("[LOG]: ${cityList}")

        // 3. Now I can safely add to it, knowing it exists.
        cityList.add(airport)

        println("[LOG2]: ${manualMap}")
        println("[LOG2]: ${cityList}")

        // Get reference (original)
        val cityList2 = manualMap.getOrPut(airport.city) { mutableListOf() }
        // Get a copy instead
        val cityList3 =
            manualMap[airport.city]?.toMutableList() ?: mutableListOf()

        for (i in 1..5) println()

        for ((key, value) in byCity(airports4)) {
            for (j in value) {
                println("[LOG3]: ${key} - ${j.name} -- ${j.city}")
            }
        }
        println(byCity2(airports4))

    }
}
 */

/*
    - `groupBy`
      - Returns: `Map<K, List<V>>`
      - Purpose: Categorising items into groups.
      - Duplicate Keys?: Keeps all. Puts them in a list.
    - `assosciateBy`
      - Returns: `Map<K, V>`
      - Purpose: Indexing items by ID.
      - Duplicate Keys?: Overwrites. Last one wins.
    - `getOrPut`
      - Returns: `V`
      - Purpose: Safely accessing/initialising a MutableMap.
      - Duplicate Keys?: N/A (Handles creation).
 */

/* ------ ------ ------      ------ ------     ------ ------ ------ ------ */
/* ------ ------ ------      ------ ------     ------ ------ ------ ------ */
/* ------ ------ ------      ------ ------     ------ ------ ------ ------ */
// CODING CONVENTIONS

/*
    KOTLIN CONVENTION: Omit types when obvious, include them when they add
    clarity.
 */
// Type obvious from RHS - omit
val name = "Alice"
val count = 42
val list42 = mutableListOf<String>()
val list43: MutableList<String> = mutableListOf()

// Type not obvious - include
val result2: MutableList<Airport> =
    manualMap.getOrPut(City.London) { mutableListOf() }
// val parsed: City = gson.fromJson(json, City::class.java)

/*
    Without the annotation, a reader has to mentally trace: "What's
    `manualMap`'s type? What does `getOrPut` return?" The explicit type answers
    that immediately.

    ------

    My recommendation: Do what you're doing. Explicit types for anything
    non-trivial is a good habit, especially because.

    1. Readable without IDE - not everyone reads code in IntelliJ with
       hover-for-type.
    2. Catches subtle bugs - if `manualMap` changes type, you get an error here
       instead of somewhere downstream.
    3. Self-documenting - future you (or teammates) immediately knows what
       `cityList` is
    4. Haskell instinct is good - Haskell's culture of explicit signatures
       exists for a reason.
 */

fun byNam3(airports: Map<String, Airport>): Map<String, Airport> {
    return airports.values.associateBy { it.name }
}

/* ------ ------ ------      ------ ------     ------ ------ ------ ------ */

/*
fun main() {
    fun uppercase(s: String?): String = s?.uppercase() ?: ""

    val words = listOf("quick", "brown", null, "fox", null, "lazy")

    fun uppercaseAllNonNull(strings: List<String?>): List<String> {
        return strings.filterNotNull().map { it.uppercase() }
    }

    println(uppercaseAllNonNull(words))
}
 */


class StaffMember(val name: String, val role: Role)

enum class Role {
    SW_ENG, SENIOR_SW_ENG, ENG_MANAGER, DIRECTOR, VP
}

val salaries = mapOf(Role.SW_ENG to 50000,
    Role.SENIOR_SW_ENG to 70000,
    Role.ENG_MANAGER to 90000,
    Role.DIRECTOR to 140000)

fun averageSalaryOf(xs: List<StaffMember?>): Double {
    val temp: MutableList<Double?> = mutableListOf()
    for (i in xs) {
        temp.add(salaries[i?.role]?.toDouble() ?: null)
    }
    return temp.filterNotNull().average()
}

fun main() {

}





