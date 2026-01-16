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

    ... NULL SAFETY--arguably Kotlin's most famous feature and the solution to
    the "Billion-Dollar Mistake."


    1. THE "BILLION-DOLLAR MISTAKE"

    ... Tony Hoare, the inventor of the null reference.
    - THE CONCEPT: In many languages (like Java or C++), if you try to access a
      property on an object that doesn't exist (`null`), the program instantly
      crashes with a NullPointerException (NPE).
    - THE COST: The graph titled "Costs of bugs" illustrates why this matters.
      Fixing a bug during the CODING phase costs ~$25. Fixing that same bug
      after POST RELEASE costs ~$16,000.
    - KOTLIN'S GOAL: Move these errors from "Post Release" (crashes users see)
      to "Coding" (red underlines the compiler shows you), saving massive
      amounts of money and time.



    --------

    2. THE SCENARIO: COMBINING MAPS AND NULL

    The slide provides a practical example of how map lookups cause crashes.

    THE DATA:
    1. MAP 1 (PERSON -> COMPANY):
        - `Tim` -> `Apple`
        - `Jamie` -> `Imperial`
    2. MAP 2 (COMPANY -> ADDRESS):
        - `Google` -> `1600 Amphitheatre`
        - `Apple` -> `1 Infinite Loop`
        - (Note: "Imperial" is MISSING from this map)

    THE BUG: If you run the code `companies["Jamie"]`, you get `"Imperial"`. If
    you then run `addresses["Imperial"]`, you get `null` (because it's missing).
    If you then try to access the street name `addresses["Imperial"].street`,
    CRASH.

    The error message shown is: `Cannot read field "street" because the return value... is null`.



    ------

    3. THE SOLUTION: NULLABLE TYPES

    Kotlin forces you to handle these scenarios explicitly using specific syntax


    A. THE TYPE SYSTEM (`?`)

    In Kotlin, a `String` can never be null. If you want something to hold a
    null value, you must add a question mark.
    - `val name: String  = null`     NO! COMPILE ERROR (caught early! cheap!)
    - `val name: String? = null`     YES! ALLOWED


    b. SAFE CALL OPERATOR (`?.`)

    This is how you safely chain map lookups without crashing.
    - SYNTAX: `variable?.method()`
    - MEANING: "If the variable is not null, run the method. If it is null, just
      give up and return null (don't crash)."
    - FIXING THE MAP CRASH:


    ```Kotlin
    val company = people["Jamie"]           // Returns "Imperial"
    val address = addresses[company]        // Returns null
    val street  = address?.street           // Returns null (NO CRASH!)
    ```


    C. THE ELVIS OPERATOR (`?:`)

    This allows you to provide a DEFAULT VALUE if something turns out to be null
    - SYNTAX: `variable ?: default_value`
    - MEANING: "If the left side is not null, use it. If it is null, use the
                right side."
    - EXAMPLE:
    ```Kotlin
    val location = address?.street ?: "Unknown Location"
    // If address is null, 'location' becomes "Unknown Location"
    ```


    SUMMARY CHECKLIST
    1. `Any?` : This nullable version of the root class.
    2. `safe?`: The safe call operator (`?.`) prevents crashes on null values.
    3. `?:`   : The Elvis operator provides a fallback default.
    4. `filterNotNull`: A helpful list function that strips out all
* */



/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/

/*
        fun uppercase(ss: String?): String = ss?.uppercase() ?: ""



        val words: List<String?> = listOf("quick", "brown", null, "fox", null, "lazy")

        fun uppercaseAllNonNull(strings: List<String?>): List<String> = strings.filterNotNull().map { it.uppercase() }




        // sss
* */

class StaffMember_(val name: String, val role: Role_)

enum class Role_ {
    SW_ENG, SENIOR_SW_ENG, ENG_MANAGER, DIRECTOR, VP
}

val salaries_ = mapOf(
    Role_.SW_ENG to 50000,
    Role_.SENIOR_SW_ENG to 70000,
    Role_.ENG_MANAGER to 90000,
    Role_.DIRECTOR to 140000
)

fun averageSalaryOf_(xs: List<StaffMember_?>): Double {
    val temp = xs.filterNotNull().map{ salaries_[it.role] }.filterNotNull()
    var sum: Double = 0.0
    for (i in temp) sum += i
    return (sum / temp.size)
}


/*
fun main() {
    val a = "hello".uppercase()

    val xs = listOf(3, 3, 4)
    averageSalaryOf_(listOf(
        null,
        null,
        StaffMember_("a", Role_.ENG_MANAGER),
        StaffMember_("a", Role_.SW_ENG),
        StaffMember_("a", Role_.DIRECTOR),
        // StaffMember_("a", null)
    ))
}
*/

/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/
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
/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/



/*
    1. EQUALITY: `equals()` vs `==`

    Your slides emphasize the distinction between REFERENTIAL EQUALITY and
    STRUCTURAL EQUALITY.
    - REFERENTIAL (`===`): Checks "Are these the exact same point in memory?"
    - STRUCTURAL (`==`): Checks "Do these objects hold the same data?"

    Without `data class`: You have to manually write the logic to compare every
    field (like the `Shop` class example in your slide). WITH `data class`:
    Kotlin writes this for you automatically.

    C   - copy
    H   - hashCode
    C   - componentN
    E   - equals
    T   - toString


    EXAMPLE:
    ```Kotlin
    data class User(val name: String, val age: Int)

    val u1 = User("Matt", 30)
    val u2 = User("Matt", 30)
    val u3 = u1

    println(u1 == u2)   // true  (Structural: Kotlin checks if name & age match)
    println(u1 === u2)  // false (Referential: They are different objects in memory)
    println(u1 === u3)  // true  (Referential: Both point to the same memory)
    ```
* */



/*
    2. THE LOOKUP ACCELERATOR: `hashCode()`

    The slides use a visual of "Buckets" (e.g., Tim/Apple, Mark/Meta) to explain
    this.

    - THE PROBLEM: Searching a huge list of objects one by one is slow.
    - THE SOLUTION (`hashCode`): It generates an integer "summary" of the object
      . When you put items in a `Set` or `Map`, Kotlin looks for the object's
      "bucket" (hash) first.
    - THE SLIDE EXAMPLE: It suggests using "length of the string" as a
      simplified hash. If you look for "Jeff", you only check the bucket for
      length 4. You don't bother checking "Jamie" (length 5) or "Tim" (length 3)


    WHY IT MATTERS:

    If you override `equals()` (which data classes do), you MUST override
    `hashCode()`. If you don't two "equal" `User("Matt", 30)` objects might end
    up in different buckets in a `HashMap`, causing bugs where you can't find an
    object you know is there. Data classes handle this contract for you
    automatically.



    3. THE DEBUGGER FRIEND: `toString()`

    The slide notes that the default `toString()` is "common and repetitive"


* */




class Coordinate_(private val x: Int, private val y: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other)        return true      // seems like referential equality is paramound here!
        if (other !is Coordinate_) return false
        return other.x == this.x && other.y == this.y
    }

    override fun hashCode(): Int {
        return 31 * x + y
    }
}

/*
fun main() {
    println(Coordinate_(3, 5) == Coordinate_(3, 5))
}
*/



/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/
/*
 ____________________________________                 ______________
|------|------|     __   __   __     |     ___________     |           () |
| 64X4 | 64X4 | || |  | |  | |  |    |    |           |    |           ___|
|------|------| || |  | |  | |  |    |____|           |____|         || D |
| 64X4 | 64X4 | || |__| |__| |__|                 ________________  ||| I |
|------|------|  |  ________   ______   ______   | ADV476KN50     | ||| P |
| 64X4 | 64X4 |    |TRIDENT | |______| |______|  | 1-54BV  8940   | ||| S |
|------|------| || |TVGA    | |______| |______|  |________________| |||___|
| 64X4 | 64X4 | || |8800CS  |          ________________                ___|
|------|------| || |11380029|    LOW->|  /\ SUPER VGA  | _________    |   |
| 64X4 | 64X4 |     --------    BIOS  | \/         (1) ||_________|   | 1 |
|------|------| ||  ______  J  ______ |________________| _________    | 5 |
| 64X4 | 64X4 | || |______| 2 |______| ________________ |_________|   |___|
|------|------| ||  ________   ______ |  /\ SUPER VGA  |               ___|
| 64X4 | 64X4 |    |________| |______|| \/         (2) |   _________  |   |
|------|------| ()              HIGH->|________________|  |_________| | 9 |
 | 64X4 | 64X4 |     ________   _________   _____________   _________  |   |
 |______|______|__  |________| |_________| |_____________| |_________| |___|
                 |               __    TVGA-1623D                    _ () |
                 |LLLLLLLLLLLLLL|  |LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL| |___|
                                                                          |
                                                                          |
* */
/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/

/*
    ... You have OVERLOADED the `equals` method instead of OVERRIDING it.


    THE BUG

    In Kotlin (and Java), the standard equality method defined in the `Any`
    class (the root of all objects) has this specific signauture:
            `open operator fun equals(other: Any?): Boolean`

    Your code has this signature: `fun equals(cs: Coordinate): Boolean`



    WHAT IS HAPPENING

    1. The test framework calls `equals(Any?)` (the standard one).
    2. Since you didn't OVERRIDE that specific signature, Kotlin falls back to
       the default implementation in `Any`.
    3. The default implementation checks REFERENCE EQUALITY (`===`). It asks,
       "Are these the exact same point in memory?"
    4. Since your test creates two new instances (`Coordinate(1, 1)` and
       `Coordinate(1, 1)`), they are at different memory addresses.
    5. RESULT: `false`. Your custom method `equals(Coordinatre)` is never even
       called by the test runner.



    THE FIX

    You need to match the signature exactly (`Any?`) and cast the input. This is
    the manual code that `data class` writes for you.


```Kotlin

class Coordinate(private val x: Int, private val y: Int) {

    // 1. MUST use `override` keyword (forces you to get the signature right)
    // 2. Parameter MUST be `Any?` (nullable Any)
    override fun equals(other: Any?): Boolean {
        // Step 1: Check if it's the exact same object in memory (Optimization)
        if (this === other)       return true

        // Step 2: Check if the other object is actually a coordinate.
        if (other !is Coordinate) return false

        // Step 3: Compare properties (Smart cast lets us use `other.x` now)
        return this.x == other.x && this.y == other.y
    }


    // WARNING: If you override equals, you MUST override hashCode!
    override fun hashCode(): Int {
        return 42 * this.x + this.y
    }
}
```
* */






/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/
/*
               ,,ggddY"""Ybbgg,,
          ,agd888b,_ "Y8, ___`""Ybga,
       ,gdP""88888888baa,.""8b    "888g,
     ,dP"     ]888888888P'  "Y     `888Yb,
   ,dP"      ,88888888P"  db,       "8P""Yb,
  ,8"       ,888888888b, d8888a           "8,
 ,8'        d88888888888,88P"' a,          `8,
,8'         88888888888888PP"  ""           `8,
d'          I88888888888P"                   `b
8           `8"88P""Y8P'                      8
8            Y 8[  _ "                        8
8              "Y8d8b  "Y a                   8
8                 `""8d,   __                 8
Y,                    `"8bd888b,             ,P
`8,                     ,d8888888baaa       ,8'
 `8,                    888888888888'      ,8'
  `8a                   "8888888888I      a8'
   `Yba                  `Y8888888P'    adP'
     "Yba                 `888888P'   adY"
       `"Yba,             d8888P" ,adP"'
          `"Y8baa,      ,d888P,ad8P"'
               ``""YYba8888P""''
* */
/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/



/*
    2. THE LOOKUP ACCELERATOR: `hashCode()`

    ...

    WHY IT MATTERS: If you override `equals()` (which data classes do), you MUST
    override `hashCode()`. If you don't, two "equal" `User("Matt", 30)` objects
    might end up in different buckets in a `HashMap`, causing bugs where you
    can't find an object you know is there. Data classes handle this contract
    for you automatically.



    3. THE DEBUGGER FRIEND: `toString()`

    The slide notes that the default `toString()` is "common and repetitive,"
    often printing unreadable memory addresses in standard classes.
        - LOGIC: It returns a string in the format `ClassName(prop1=value, prop2=value)`
        - USE CASE: This is purely for your sanity when debugging logs.

    EXAMPLE:
    ```Kotlin
    println(u1)
    // Output: User(name=Matt, age=30)
    // Instead of: User@5e2de80c
    ```



    4. IMMUTABILITY HELPER: `copy()`

    The slide mentions this allows an "optional argument update". This is
    crucial for Functional Programming (FP) where we prefer IMMUTABLE data
    (read-only `val`).

    - LOGIC: "Create a clone of this object, but change just these specific
      fields."
    - WHY IT'S BETTER: You don't need to manually read every single property
      from the old object to create a new one.



    ```Kotlin
    val u1 = User("Matt", 30)

    // "Update" the age (actually creates a new object, leaving u1 untouched)
    val u2 = u1.copy(age = 31)

    println(u2)     // User(name="Matt", age=31)
    ```



    5. DESTRUCTURING: `componentN()`

    The slide refers to "Inherit de-structuring operators for positional
    arguments."

    - LOGIC: Kotlin generates invisible functions `component1()`, `component2()`
      , etc. that correspond to the properties in order.
    - USE CASE: Allows you to "unpack" an object into variables in a single
      line.



```Kotlin
val user = User("Matt", 30)

// This works because component1() returns `name` and component2() returns `age`
val (name, age) = user

println("Name: $name, Age: $age")
```


        ```Kotlin
        try {

        } catch (Exception e) {

        }
        ```

        EXCEPTIONS AND java.io.File

            - FileNotFoundException is a common issue and reflective of
              depending on external inputs
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
    ... the "plumbing" of programming: ARRAYS, COMMAND-LINE ARGUMENTS, and
    INPUT/OUTPUT (I/O).

    With your C++ background, these concepts woioll feel very familiar, but
    Kotlin has specific quirks (and safeguards) you need to know.



    ----------
    1. ARRAYS (THE "NATIVE" MEMORY BLOCK)

    In the previous lecture, we talked about `List` (which is like `std::vector`
    or a Linked List). ARRAYS are the low-level, fixed-size memory blocks
    equivalent to raw C++ arrays (`int arr[10]`).


    HOW TO USE THEM
    - FIXED SIZE: You cannot add or remove items.
    - CONSTRUCTION: You can use `arrayOf` (for literals) or the constructor
      `Array(Size) { lambda }` (for dynamic generation).
* */

/*
fun main() {

    // 1. Literal style (Like C++ int arr[] {1, 2, 3})
    val simple = arrayOf(1, 2, 3)

    // 2. Constructor style (The "Power User" way)
    // Creates an array of size 5: [0, 2, 4, 6, 8]
    val generated = Array(5) { i -> i * 2}

    // 3. 2D Arrays (Matrices/Chess boards)
    // Access using standard bracket syntax: board[0][0]
    val board = Array(8) { Array(8) { "Empty" } }
    board[0][0] = "Rook"
}
 */

/*
    WHEN TO USE vs. `List`

    - FEATURE: Array (`Array<T>`)
    - SIZE: FIXED (Fast, overhead-free)
    - MUTABILITY: MUTABLE (Always)
    - PERFORMANCE: HIGH (Native JVM array)
    - USE CASE: Math matrices, Pixel buffers, Interop with Java APIs.


    - FEATURE: List (`List<T>`)
    - SIZE: Dynamic (Flexible)
    - MUTABILITY: Immutable (default) or Mutable
    - PERFORMANCE: Good (Wrapper object overhead)
    - USE CASE: 99% OF GENERAL APPLICATION LOGIC.



    COMMON PITFALL: INVARIANCE

    In Java, `String[]` is considered a subtype of `Object[]`. This causes
    runtime crashes. In Kotlin, arrays are INVARIANT.

    - You CANNOT assign an `Array<String>` to a variable of type `Array<Any>`.
      The compiler will stop you to prevent type errors.
* */



/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/
/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/



/*
    2. MAIN ARGS (COMMAND LINE FLAGS)

    Just like `int main(int argc, char** argv)` in C++, Kotlin allows you to
    read arguments passed when the program starts.


    USAGE

    You must change the standard `main()` to accept `args`.

    ```Kotlin
    // The "Entry Point"

    fun main(args: Array<String>) {
        if (args.isNotEmpty()) {
            println("First argument: ${args[0]}")
        }
    }


    UTILITY
    - CONFIGURATION: Passing file paths, debug flags, or server ports without
      recompiling the code.
    - EXAMPLE: `java -jar game.jar -windowed -debug`
        - Inside Code: `if ("-debug" in args) enableLogging()`
    ```
* */




/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/
/*
    3. INPUT/OUTPUT (STREAMS)

    This section deals with reading and writing data. Kotlin relies on Java's
    I/O libraries but adds some nice syntax.


    THE HIERARCHY (FROM BYTES TO TEXT)

    Your slides explain the layers of wrapping required to read inputs
    efficiently.
        1. `System.in`: The raw BYTE STREAM. It reads 1s and 0s. (Note: `in` is
           a keyword in Kotlin, so you must escape it as ``in`` to use it).
        2. `InputStreamReader`: Converts Bytes -> CHARACTERS (handles encoding
           like UTF-8).
        3. `BufferedReader`: Buffers characters into LINES (Strings) much faster
           than reading one char at a time.



    THE "EASY WAY" (Scanner) vs. THE "FAST WAY" (BufferedReader)

    OPTION A: SCANNER (EASY, GOOD FOR SIMPLE SCRIPTS)
    Use this for simple text parsing or competitive programming problems where
    input size is small.

```Kotlin
import java.util.Scanner

val reader = Scanner(System.`in`)       // note the backticks
val number = reader.nextInt()
val text = reader.nextLine()
```


    OPTION B: BUFFEREDREADER (FAST, GOOD FOR HUGE FILES)
    Use this for reading massive logs or game assets.

```Kotlin
import java.io.BufferedReader
import java.io.InputStreamReader

val reader = BufferedReader(InputStreamReader(System.`in`))
val line   = reader.readLine()          // Returns String or null
```


    FILE I/O (THE KOTLIN WAY)
    Kotlin adds "Extension Functions" to the standard Java File class that make
    reading files one line of code.

```Kotlin
import java.io.File

// Reading
val text  = File("data.txt").readText()      // Reads entire file into memory
val lines = File("data.txt").readLines()     // Returns List<String>

// Writing
File("save.dat").writeText("Level: 5")
```


    COMMON PITFALL: `readText() ON BIG FILES
    - DON'T USE: `File("huge_log.txt").readText()` on a 2GB file. It tries to
      load the entire file into RAM (Heap) at once and will crash your app
      (OutOfMemoryError).
    - DO USE: `File("huge_log.txt").forEachLine { line -> process(line) }`. This
      streams it line-by-line using very little memory.


* */





/*
ARRAYS EXERCISE

        class RGB(val red:Int, val green:Int, val blue: Int)
        val background = Array(256) { Array(256) { RGB(it, 10, 10)}}

        -----



        import kotlin.math.max

        /*
        class RGB(val red:Int, val green:Int, val blue: Int) {
            override fun equals(other: Any?): Boolean {
                if (other === this) return true
                if (other !is RGB)  return false
                return other.red == red && other.green == green && other.blue == blue
            }
        }
        */

        data class RGB(val red: Int, val green: Int, val blue: Int)

        data class Sprite(val x:Int, val y:Int)

        //2-D array for background
        val background = Array(256) { Array(256) { RGB(0, 0, 0)}}

        //function to create 2-D pixel array for sprite layer
        fun spriteLayer(sprites:List<Sprite>):Array<Array<RGB>> {
            sprites.forEach { background[it.y][it.x] = RGB(255, 255, 255) }
            return background
        }

* */




/*
    ... the exact same issue you faced with the `Coordinate` class earlier!

    The problem is likely NOT your array logic anymore--it's the `RGB` class
    definition itself.


    THE PROBLEM
    ... `class RGB(val red: Int, val green: Int, val blue: Int)`

    It's a normal `class`, not a `data class`.
    - In Kotlin, normal classes verify equality by REFERENCE (memory address).      <-- THE IMPORTANT PART
    - The test creates a NEW `RGB(255, 255, 255)` object and compares it to the
      one in your array.
    - Since they are two different objects in memory, `==` returns `false`,
      causing the test to fail even if the numbers are correct.


    ...
* */

// import kotlin.math.max

data class RGB(val red:Int, val green:Int, val blue: Int){
    operator fun plus(pixel:RGB) =
        RGB(
            max(pixel.red,red),
            max(pixel.green,green),
            max(pixel.blue,blue)
        )
}

data class Sprite(val x:Int, val y:Int){
    fun move(step:Pair<Int,Int>) = Sprite(x+step.first,y+step.second)
}

//2-D array for background
val background = Array(256) { Array(256) { RGB(0, 0, 0)}}

//function to create 2-D pixel array for sprite layer
fun spriteLayer(sprites:List<Sprite>):Array<Array<RGB>> {
    val temp = Array(256) { Array(256) { RGB(0, 0, 0)}}
    sprites.forEach { temp[it.y][it.x] = RGB(255, 255, 255) }
    return temp
}


fun merge(xs1: Array<Array<RGB>>, xs2: Array<Array<RGB>>): Array<Array<RGB>> {
    val temp = Array(256) { Array(256) { RGB(0, 0, 0)}}
    for (i in 0..255) {
        for (j in 0..255) {
            temp[i][j] = xs1[i][j] + xs2[i][j]
        }
    }
    return temp
}



fun main() {

    for (i in 1..10) {}
}

/*1010101010101010101010101010101010101010101010101010101010101010101010101010*/




/*
* Continue with lecture 8
* conntinue to new 4
* finish PPT-5 kotlin
* */


class `Jan-15` {
}