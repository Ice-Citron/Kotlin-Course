
/* ------ -SINGAPORE-- ------ ------      ---123- ------       ------ ------ */
// equals() -- TWO KINDS OF EQUALITY
/*
    There are two different questions you can ask about two objects:
    - REFERENTIAL EQUALITY (===): "Are these the exact same object in memory?"
    - STRUCTURAL EQUALITY (==): "Do these objects contain the same data?"
 */

/*
Analogy: Identical Twins

    - Imagine identical twins, Alice and Alice-clone. They look exactly the
      same, have the same height, same eye color, same everything.
      - Referential equality (===): "Are you pointing at the same physical
        person?" No--there are two separate humans.
      - Structural equality (==): "Do they have the same properties?" Yes--same
        name, same appearance, same everything.
 */

class Person10(val name: String, val age: Int)

val alice10 = Person10("Alice", 25)
val alice11 = Person10("Alice", 25)
val alice12 = alice10       // same reference

/*
fun main() {
    // Referential -- same object in memory?
    alice10 === alice11       // false - two different objects
    alice11 === alice12       // true - same object

    // Structural -- same content?
    alice10 == alice11        // false (by default!) - we'll fix this
    alice10 == alice12        // true
}
 */

/*
    Wait, why is `alice10 == alice11` false? They have the same data!
 */


// THE DEFAULT `equals()` PROBLEM
/*
    By default, `==` calls `.equals()`, and the default `.equals()` just checks
    if it's the same object (same as `===`). It doesn't know how to compare your
    custom class's properties.
 */
class Person014(val name: String, val age: Int)

val a01 = Person014("Alice", 25)
val b01 = Person014("Alice", 25)

// a == b           // false -- default equals just checks reference
// a.equals(b)      // false -- same thing


/*
    You have to teach Kotlin how to compare your class by overriding `equals()`:
 */
class Person016(val name: String, val age: Int) {
    override fun equals(other: Any?): Boolean {
        if (other !is Person016) return false
        return name == other.name && age == other.age
    }
}

val a016 = Person016("Alice", 25)
val a017 = Person016("Alice", 25)

// a016 == a017         // true - now it compares content



/* ------ -SINGAPORE-- ------ ------      ---123- ------       ------ ------ */
// SMART CASTING
/*
    Notice the `other is Person` check. Once Kotlin confirms `other` is a
    Person, it automatically casts it within that block.

```kotlin
override fun equals(other: Any?) Boolean {
    if (other is Person) {
        // Kotlin knows 'other' is Person here - no cast needed
        return name == other.name
    }
    return false
}
```

    In Java, you'd need an explicit cast

```java
// Java - manual cast required
if (other instanceof Person) {
    Person otherPerson = (Person) other;    // tedious
    return name.equals(otherPerson.name);
}
```

    Kotlin's smart cast eliminates this boilerplate.

    - The problem: you have a million objects and want to find one equickly.
      Checking each one (`equals()` a million times) is slow.
    - The solution: Put objects into numbered buckets based on a hash. Then only
      search within the right bucket.

    ---

    Analogy: Library Filing System

    - Imagine a library with 10,000 books. You want to find "Harry Potter."
      - Slow Way: Walk through every shelf, check every book. O(n)
      - Fast Way: Books are organised by first letter. Go to the "H" section,
        now you only check ~400 books instead of 10,000.
      - `hascode()` is like computing which section to look in. It narrows down
        the search massively.

    ---

    How it works in the slide

```
hashCode = name.length
```

    ...

    --- --- --- ---
    When you search for "Jeff":
    1. Compute hash: `"Jeff".length` = 4
    2. Go to bucket 4
    3. Only compare against Mark and Jeff (not everyone!)
    4. Find Jeff -> return Amazon

    Without hasing, you'd compare against all 5 entries. With hashing, you
    compare against 2. With millions of entries, this difference is huge.
 */

/* ------ -SINGAPORE-- ------ ------      ---123- ------       ------ ------ */





/* ------ -LONDON-- ------ ------      ---123- ------       ------ ------ */

/*
    You almost never call `hashCode()`.

    But Data Structures call it constantly behind your back.

    If you are just using a `List` or an `Array, `hashCode` is useless. It sits
    there doing nothing. However, the moment you use a `HashSet` or a `HashMap`,
    that function becomes the most important line of code in your class.

    Here is exactly when and how it gets called "internally"


    1. The "Invisible" Call

```kotlin
val mySet = hashSetOf<Person>()
val p1 = Person("Alice", 25)

mySet.add(p1)       // <-- INTERNALLY calls p1.hashCode()
```

    When you call `.add()`, the `HashSet` stops and asks: "Which bucket should
    I put this object in?"

        1. It calls `p1.hashCode()`
        2. Let's say it returns `99`
        3. The Set looks at its internal memory array (the buckets).
        4. It puts Alice in Bucket #99.


    2. The Retrieval (The "Why")

    Later, you want to check if Alice is in the set.

```kotlin
val searchKey = Person("Alice", 25)     // A new object, but same data
val exists = mySet.contains(searchKey)  // <--- INTERNALLY calls
                                        // searchKey.hashCode()
```

    Here is the sequence:
    1. The Set calls `searchKey.hashCode()`
    2. It returns `99` (becuase you overrode it correctly!)
    3. The Set goes directly to Bucket #99.
    4. It finds the object there.
    5. Then (and only then) does it use `equals()` to double-check: "Is this
       really Alice?"


    3. What happens if you DON'T override it? (The Bug)

    If you delete your `hashCode` override, Kotlin falls back to the default
    behavior: THE MEMORY ADDRESS
        1. `p1` is at memory address `0x111`. Hash = `111` -> Bucket #111.
        2. `searchKey` is at memory address `0x222`. Hash = `222` -> Bucket #222

    When you run `mySet.contains(searchKey)`
        1. The Set calculates the hash (`222`).
        2. It looks in Bucket #222.
        3. It finds nothing (because Alice is in Bucket #111).
        4. It returns `false`.

    Result: You lost your data, even though `equals` says they are the same
            person.


    Summary:
    - Do you call it? No.
    - Does `List` use it? No.
    - Does `Set`/`Map` use it? Yes, immediately. It is the GPS coordinate for
      where your object lives in memory. If the coordinate is wrong, the map can
      never find the object again.
 */



/*
    The confusion comes from the difference between "The exact same object in
    memory" vs. "A new object that looks identical."

    If you use the exact same variable (same memory address) to retrieve the
    value, the default behavior works fine. But that is rarely what happens in
    real life.

    Here's the breakdown of why the "Default" (Memory Address) behavior causes
    you to lose data.



1. The "Base Case" (Where you are right)

    If you do this, the default behavior (Memory Address) works perfectly:

```kotlin
// Default behavior: hashCode is based on Memory Address (e.g. @1234)
class Person(val name: String)

val map = HashMap<Person, String>()
val p1 = Person("Alice")      // Memory Address: @1234

map[p1] = "Employee of the Month"

// retrieving with the EXACT same object (passed in as key)
println(map[p1])        // WORKS! Finds "Employee of the Month" <-- value
```

    WHY IT WORKS: You put it in using address `@1234`. You looked it up using
    address `@1234`. The map looked in bucket `@1234` and found it.



2. The "Real World" Case (Where you lose the object)

    In real apps, you usually don't have the original variable `p1` anymore. You
    get data from a database of a user input, and you create a NEW object that
    represents the same person.

```kotlin
// Default behavior: hashCode is based on Memory Address
class Person(val name: String)

val map = HashMap<Person, String>()
val p1 = Person("Alice")        // Memory Address: @1111 (Bucket 1)

map[p1] = "Data"

// LATER... we create a new object for the same person
val p2 = Person("Alice")        // Memory Address: @2222 (Bucket 2)
```


 */





// INTERESTINGLY, THIS HASHCODE THING ISN'T AN ISSUE IF OUR KEY IS `String`

/*
    You are getting a "free ride" because the creators of Java and Kotlin
    already wrote the `override fun hashCode` code for `String` that you forgot
    to write for `Person`

    ...

    The String Class says: "Do they have the same latters? Yes. Then give them
    the exact same HashCode."

    This is why `map["LHR"]` always works, even if you typed the letters `LHR`
    five minutes later in a different part of the code.


    2. The "DIY Treatment" for your classes

        - When you create `class Airport`, it is RAW.
          - Kotlin doesn't know what makes an airport unique. Is it the name?
            The code? the memory address?
            - Default: It assumes memory address...

    3. The CHEAT CODE: `data class`
        - This is why Kotlin developers love DATA CLASSES.
        - If you just add the word `data` to the front of your class, Kotlin
          writes the `hashCode()` and `equals()` functions for you automatically

    Summary
      - Strings / Int / Booleans: The `hashCode` is pre-written based on Content
        (Safe to use as Keys)
      - Normal Classes: The `hashCode` is based on MEMORY ADDRESS. (Unsafe to
        use as Keys unless you override it).
      - Data Classes: The `hashCode` is auto-generated based on Properties. (
        Safe to use as keys).

 */


/* ------ -SINGAPORE-- ------ ------      ---123- ------       ------ ------ */
/*
1. Use `data class` for "Passive Data" (Value Objects)

    Use this when the object IS the data. If you have two copies of it with
    the same info, they should be treated as identical.
    - The Litmus Test: If I secretly swapped your object with a clone that
      has the exact same variables, would you care?
      * No? -> Use `data class`.
    - KEY FEATURES:
      * You need `equals()`/`hashCode()` based on content (for Maps/Sets).
      * You need `toString()` to see the data nicely logs.
      * You need `.copy()` to make modified versions.
    - Examples:
      * Coordinates: `Point(x=10, y=20)` is the same as another `Point(10, 20)`
      * API Responses: `UserJson(id=1, name="Bob")`
      * Config: `ScreenSettings(width=1920, height=1800)`

---

2. Use `class` for "Active Workers" or "Entities" (State Machines)

    Use a regular class when the object represents a UNIQUE PROCESS, a service,
    or an entity with a LIFECYCLE.
    - The Litmus Test: If I have two "Game Managers" and they both happen to be
      on "Level 1" right now, are they the same manager?
      * NO! They are distinct instances running in memory. One might be Player
        1, the other for player 2. They just happen to look similar right now.
    - Why NOT use Data Class?
      * You don't want `equals()` to check the fields. You want `equals()` to
        check "Is this the exact same spot in memory?" (REFERENTIAL EQUALITY).
      * You generally DON'T want to print the whole state every time you log
        the object.
    - Examples:
      * State Machines: `TrafficLightController`.
      * Services: `DatabaseConnection`, `BluetoothManager`, `AudioPlayer`.
      * UI Components: `Button`, `Window`.

---

3. The Dangerous Trap: MUTABLE KEYS

    You mentioned Hashes. Here is a massive reason to prefer Regular Classes
    for state machines.

    If you make a MUTABLE Data Class and put it in a Map, you will break the
    Map.

```kotlin
// BAD IDEA
data class PlayerState(var score: Int)

val map = HashMap<PlayerState, String>()
val p1  = PlayerState(10)

map[p1] = "Winner"      // Hashes '10'. Puts in Bucket 10.

// ... later ...
p1.score = 99           // <-- The HASHCODE JUST CHANGED TO 99!

// The Map looks for p1 in Bucket 99.
// but the object is physically sitting in Bucket 10.
// RESULT: Object Lost.
println(map[p1])        // Returns null
```

    THE RULE:
    - DATA CLASSES SHOULD IDEALLY BE IMMUTABLE (`val`).
    - If you need a class with lots of changing `var`s (like a State Machine),
      use a *Regular Class*. It uses the Memory Address for the hash, which
      never changes, so it never gets lost in a Map.

SUMMARY CHECKLIST
    - `data class`
      * Concept: A box of info.
      * Equality:
        - CONTENT: Do they contain the same numbers/strings?
      * Hashing: Based on fields (Risky if fields change).
      * Best For: JSON, DTOs (Structs from C), Coordinates, Configs.
    - `class`
      * Concept: A generic object / A worker.
      * Equality:
        - IDENTITY: Is it the same memory address? (`===`)
      * Hashing: Based on Memory (Safe even if fields change).
      * Best For: Services, Managers, UI Views, Logic Handlers
 */



/* ------ -HEAVY INDUSTRIES-- ------      ---123- ------       ------ ------ */
// THE `hashCode` CONTRACT

/*
    There's a critical rule:

    If two objects are equal (`a == b`), they MUST have the same hashCode.

    Why? If equal objects had different hashCodes, they'd be in different
    buckets, and you'd never find them!
 */

class Person0115(val name: String, val age: Int) {

    override fun equals(other: Any?): Boolean {
        if (other !is Person0115) return false
        return name == other.name && age == other.age
    }

    override fun hashCode(): Int {
        return name.hashCode() + age    // based on same fields as equals
    }
}

/*
    The reverse isn't required: objects with the same hashCode don't have to be
    equal. That's just a "collision" (like Mark and Jeff both having 4-letter
    names). Collisions are fine--you just check `equals()` within the bucket.
 */


/* ------ -SIENAR-- ------ ------      ---123- ---JAEMUS       ---- -*(%6--- */
// DATA CLASSES DO THIS AUTOMATICALLY
/*
    Writing `equals()` and `hashCode()` manually is tedious and error-prone.
    `data class` generates both:

```kotlin
data class Person(val name: String, val age: Int)

val a = Person("Alice", 25)
val b = Person("Alice", 25)

a == b              // true -- auto-generated equals
a.hashCode()        // consistent with equals -- auto-generated
```

    This is why `data class` exists--it generates:
    - `equals()` -- compares all properties
    - `hashCode()` -- based on all properties
    - `toString()` -- pretty printing
    - `copy()` -- create modified copies
    - `componentN()` -- destructuring
 */




// EXCEPTIONS AND TRY-CATCH
/*
    Exceptions are how programs handle error--things going wrong at runtime that
    you couldn't prevent at compile time.

--------        --------    --------
ANALOGY

    You're doing a trapeze act (reading a file). Most of the time it works. But
    sometimes:
    - The file doesn't exist
    - You don't have permission
    - The risk is corrupted
    Instead of crashing to the ground (program terminates), you set up a safety
    net (catch block) to handle the fall gracefully.

-----
Basic Syntax:
```kotlin
try {
    // Risky code that might fail
    val content = File("data.txt").readText()
    println(content)
} catch (e: FileNotFoundException) {
    // Handle the specific error
    println("File not found: ${e.message}")
} catch (e: Exception) {
    // Handle any other error
    println("Something went wrong: ${e.message}")
}
```

    If `data.txt` doesn't exist, instead of crashing, the code jumps to the
    catch block.

------ --- -    ------ --- ------- --- -
// MULTIPLE CATCH BLOCKS -- ORDER MATTERS:
```kotlin
try {
    riskyOperation()
} catch (e: FileNotFoundException) {
    // Specific error first
    println("File Missing. ${e}")
} catch (e: IOException) {
    // More general error second
    println("IO problem")
} catch (e: Exception) {
    // Catch-all last
    println("Unknown error")
}
```

    Kotlin checks catch blocks top to bottom, uses the first matching one. Put
    specific exceptions before general ones.


----- --0 SIENA ----- --0 isxaknj ----- --0 isxaknj ----- --0 isxaknj
// FINALLY BLOCK -- ALWAYS RUNS:

```kotlin
val file = File("data.txt")
try {
    // Use the file
    processFile(file)
} catch (e: Exception) {
    println("Error: ${e.message}")
} finally {
    // Always runs -- whether success or failure
    file.close()
}
```

    The `finally` block runs no matter what--good for cleanup like closing files
    or database connections.
 */


/* ------ -HEAVY INDUSTRIES-- ------      ---123- ------       ------ ------ */

// KOTLIN'S USE FUNCTION -- CLEANER RESOURCE HANDLING:
/*
    Instead of try-finally for closing resources:

```kotlin
File("data.txt").bufferedReader().use { reader ->
    // `use` automatically closes the reader when done
    println(reader.readText())
}
// READER IS CLOSED HERE, EVEN IF AN EXCEPTION OCCURED
```

    This is like Python's `with` or C#'s `using`.



// THROWING EXCEPTIONS:
```kotlin
fun divide(a: Int, b: Int): Int {
    if (b == 0) throw IllegalArgumentException("Cannot divide by zero")
    return a / b
}

try {
    divide(10, 0)
} catch (e: IllegalArgumentException) {
    println(e.message)      // "Cannot divide by zero."
}

```

 */


/* ------ -SINGAPORE-- ------ ------      ---123- ------       ------ ------ */
// KOTLIN vs JAVA: NO CHECKED EXCEPTIONS

/*
    In Java, some exceptions MUST be caught or declared--the compiler forces
    you:

```java
// Java - forced to handle IOException
public void readFile() throws IOException {
    FileReader f = new FileReader("file.txt");      // must catch or declare
}
```

    Kotlin doesn't have checked exceptions. You can catch whatever you want, or
    nothing:

```kotlin
// Kotlin - your choice whether to catch
fun readFile(): Unit {
    val f = FileReader("file.txt")        // might throw, but compiler doesn't
                                          // force handling
}
```

    This is controversial. Kotlin's view: forced exception handling often leads
    to empty catch blocks and cluttered code. Better to make it optional and
    trust developers to handle errors appropriately.
 */



/*
SUMMARY

- `===`
    * Referential equality -- same object in memory?
- `==`
    * Structural equality -- calls `.equals()`, same content?
- `hashCode()`
    * Bucket number for fast lookups in HashMaps/Sets
- `data class`
    * Auto-generates `equals()`, `hashCode()`, `toString()`, `copy()`
- Smart cast
    * After `is` check, Kotlin auto-casts the type
- `try-catch`
    * Handle runtime errors gracefully
- `finally`
    * Always runs, good for cleanup
- `.use {}`
    * Auto-closes resources when done.
 */



/*
    Null safe lambda -- removes null values
    - filterNotNull
    - mapNotNull
 */



/* ------ -SINGAPORE-- ------ ------      ---123- ------       ------ ------ */
// EXERCISE


class Coordinate(private val x: Int, private val y: Int) {
    override fun equals(other: Any?): Boolean {
        if (other !is Coordinate) return false
        return other.x == this.x && other.y == this.y
    }

    override fun toString(): String {
        return "Coordinate Class@${this.hashCode()}: x=${this.x}, y=${this.y}."
    }

    override fun hashCode(): Int {
        return this.x*this.x + this.y*5 + 3
    }
}




/* ------ -Great Britain-- --- ------      ---123- ------       -asa- ------ */
/* ------ -SINGAPORE-- ------ ------      ---123- ------       ------ ------ */
/* ------ -SINGAPORE-- ------ ------      ---123- ------       ------ ------ */




fun main() {
    // Both function declaration below appears to be equivalent.
    val map = HashMap<String, String?>()
    val map2: HashMap<String, String?> = hashMapOf(
        "Sienar" to null,
        "Sienar" to "Jaemus"
    )

    println("[LOG]: ${map2.filter { it.value != null }}")
    println("[LOG]: ${map2.mapNotNull {it.value + "J"}}")

    println("[LOG]: ${listOf(3, 4, 5, null, 4).mapNotNull {it?.plus(2)}}")

    fun divide(a: Int, b: Int): Int {
        if (b == 0) throw IllegalArgumentException("Cannot divide by zero")
        return a / b
    }

    try { divide(10, 0) }
    catch (e: IllegalArgumentException) { println(e.message) }
                // "Cannot divide by zero."
}





