/*
... syntax can slip away quickly when you aren't using it.

Since you have the slides for L0-4, let's do a "rapid recall" session to get
your brain back into Kotlin mode. We will move from the basic building blocks up
to that class definition you mentioned.

Here is your KEY REFRESHER:



1. THE BASICS (L0 & 1)

   Kotlin is designed to be concise.
      - FUNCTIONS: You define them with `fun`.
         - Slide Ref: `fun successor(x: Int): Int = x + 1`
         - NOTE: You can use an `=` for one-line functions (expression body) or
           `{}` for larger ones (block body).
      - VARIABLES:
         - `val`: VALUE (Immutable). Once assigned, it cannot change. Preferred
           in Kotlin.
         - `var`: VARIABLE (Mutable). Can be re-assigned.
      - STRING TEMPLATES: No more clunky concatenation. Use `$` to inject
        variables: `"Hello ${name}"`




2. FUNCTIONAL POWERS (Lecture 2)
   Kotlin treates functions as "first-class citizens," meaning you pass them
   around like data.
      - COLLECTIONS: `listOf(1, 2, 3)` creates a read-only list.
      - LAMBDAS: These are anonymous functions inside curly braces `{}`. We use
        them heavily with collections.
         - `map`: Transforms items. `nums.map { it * it }` (squares each number)
         - `filter`: Selects items. `nums.filter { it > 2 }`
         - Note: `it` is the default name for a signle argument in a lambda.




3. CLASSES & OBJECTS (L3) -- The part you asked about!
   In Kotlin, the PRIMARY CONSTRUCTOR is part of the class header. This is very
   different from Java or C++.
      - THE BLUEPRINT:

```Kotlin
// Class definition with properties x, y, and radius
class Circle(val x: Int, val y: Int, val radius: Int)
```
         - KEY DETAIL: Putting `val` or `var` in the parentheses automatically creates
           a property (field + getter) for you.

      - INSTANTIATION: No `new` keyword! Just call it like a function:

```Kotlin
val myCircle: Circle = Circle(2, 3, 4)
```






4. STATE & ENUMS (Lecture 4)
   - MUTABLE LISTS: If you need to add/remove items, use `mutableListOf(...)`.
      - `list.add("Item")`
   - ENUMS: Great for fixed options (like days of the week).

```Kotlin
enum class Direction { NORTH, SOUTH, EAST, WEST }
```
      - They are full classes, so they can hold properties too.
* */




/*
   ||====================================================================||
   ||//$\\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\//$\\||
   ||(100)==================| FEDERAL RESERVE NOTE |================(100)||
   ||\\$//        ~         '------========--------'                \\$//||
   ||<< /        /$\              // ____ \\                         \ >>||
   ||>>|  12    //L\\            // ///..) \\         L38036133B   12 |<<||
   ||<<|        \\ //           || <||  >\  ||                        |>>||
   ||>>|         \$/            ||  $$ --/  ||        One Hundred     |<<||
||====================================================================||>||
||//$\\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\//$\\||<||
||(100)==================| FEDERAL RESERVE NOTE |================(100)||>||
||\\$//        ~         '------========--------'                \\$//||\||
||<< /        /$\              // ____ \\                         \ >>||)||
||>>|  12    //L\\            // ///..) \\         L38036133B   12 |<<||/||
||<<|        \\ //           || <||  >\  ||                        |>>||=||
||>>|         \$/            ||  $$ --/  ||        One Hundred     |<<||
||<<|      L38036133B        *\\  |\_/  //* series                 |>>||
||>>|  12                     *\\/___\_//*   1989                  |<<||
||<<\      Treasurer     ______/Franklin\________     Secretary 12 />>||
||//$\                 ~|UNITED STATES OF AMERICA|~               /$\\||
||(100)===================  ONE HUNDRED DOLLARS =================(100)||
||\\$//\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\\$//||
||====================================================================||
* */*/*/



/*
LECTURE 5: MEMORY, MAPS and NULL SAFETY

This lecture bridges how your computer stores data (memory) with how you
organise it (Maps) and how you protect it from crashing (Null Safety).



1. STACK vs. HEAP MEMORY
   This is the mental model of where your variables live.
      - STACK: Think of this as a tidy, organised stack of post-it notes. It
        stores LOCAL VARIABLES and REFERENCES (addresses) to objects. It is
        very fast but limited in size.
      - HEAP: This is a big, messy cloud of storage. The ACTUAL OBJECTS (like a
        `Person` or a `List`) live here.

   CODE EXAMPLE:
```Kotlin
// `n` is a primitive (Int). The value `5` lives directly on the Stack.
val n: Int = 5

// `person` is a reference.
// The Stack holds the address (e.g., 0x0423as72)
// The actual `Person` object `{ name: "Bob", age: 18 }` lives in the Heap.
val person = Person("Bob", 18)
```
   Key takeaway: When you pass an object to a function, you are usually passing
   the address (reference) on the stack, not copying the whole object.





2. MAPS (Key-Value Stores)
   Maps are collections that pair a unique KEY with a VALUE. Think of a
   dictionary: "Word" (Key) \rightarrow "Definition" (Value).
      - IMMUTABLE MAP (`mapOf`): You cannot change it after creation.
      - MUTABLE MAP (`mutableMapOf`): You can add or update entries.

   CODE EXAMPLE:
```Kotlin
// 1. Create a Read-Only Map
val grades = mapOf(
    "A" to 41,          // Key "A" points to Value 41
    "B" to 88
)

// 2. Accessing data
val score = grades["A"]        // Returns 41

// 3. Mutable Map
val grades_ = grades.toMutableMap()
grades_["D"] = 9                // Adds a new pair "D" to 9
grades.put("A", 42)             // Updates "A" to 42

```



* */

/*
           _.-------._
        _-'_.------._ `-_
      _- _-          `-_/
     -  -
 ___/  /______________
/___  .______________/
 ___| |_____________
/___  .____________/
    \  \
     -_ -_             /|
       -_ -._        _- |
 Benoit  -._ `------'_./
 Rigaut     `-------'
* */


/*
3. NULL SAFETY (The Billion Dollar Fix)
   Java (and C++) famously allow variables to be `null` without warning, leading
   to crashes (`NullPointerException`). Kotlin stops this at compile time.
      - `String`: Can NEVER be null.
      - `String?`: Can hold a String OR null.

   THE THREE TOLLS:
      1. SAFE CALL (`?.`): "If not null, do it. If null, return null."
      2. ELVIS OPERATOR (`?:`): "If null, use this default value instead."
      3. NOT-NULL ASSERTION (`!!`): "I promise it's not null (crashes if you
         lie)."

   CODE EXAMPLE:
```Kotlin
// The type usually requires a `?` to allow nulls
val name : String? = null#

// Safe Call: This won't crash. It just returns null
val length = name?.length

// Elvis Operator: If name is null, use "unknown"
val length_ : String = name ?: "unknown"
```





LECTURE 6: EQUALITY, DATA CLASSES AND EXCEPTIONS
This lecture focuses on how we compare objects and how we handle errors.


1. EQUALITY (`==` vs `===`)
   How do you know if two things are the same?
   - STRUCTURAL EQUALITY (`==`): Do they look the same? (e.g., Do two different
     `Person` objects both have the name "Bob"?). this calls `.equals()` under
     the hood.
   - REFERENTIAL EQUALITY (`===`): Are they literally the same object in memory?
     (Do the stack references point to the same heap address?).

   CODE EXAMPLE:
```
val p1 = Person("Alice")
val p2 = Person("Alice")
val p3 = p1

    // p1 == p2  is TRUE (if .equals() is defined correctly) because content matches
    // p1 === p2 is FALSE because they are different objects in the Heap.
    // p1 === p3 is TRUE because they point to the same address.
```




2. HASHCODE
   If you put objects into a `Set` or use them as Keys in a `Map`, Kotlin needs
   a fast way to find them. It uses "hash code" (an integer ID) to put objects
   into "buckets".
      - RULE: If `a == b`, then `a.hashCode()` MUST equal `b.hashCode()`.






3. DATA CLASSES (The Magic Shortcut)
   Writing `equals()`, `hashCode()`, `toString()`, and `copy()` by hand is
   tedious. Kotlin's `data class` generates all this for you automatically.

```Kotlin
// Just addind `data` creates all the helper methods!
data class Student(val name: String, var id: Int)

fun main() {
    val s1 = Student("Basri", 1)

    // 1. toString() is reaable
    println(s1)         // Prints: Student(name=Matt, id=1)

    // 2. copy() allows modifying one field while keeping others
    val s2 = s1.copy(id = 2)        // Student(name=Matt, id=2)
}
```






4. EXCEPTIONS
   When things go wrong (like a file missing), the program throws an `Exception`
   . You handle this with `try-catch` to prevent the app from crashing.

CODE EXAMPLE:
```
import java.io.File
import java.io.FileNotFoundException

fun readFile() {
    try {
        // Dangerous code that might fail
        val text = File("./missing.txt").readText()
    } catch (e: FileNotFoundException) {
        // What to do if it fails
        println("File not found! Using default text.")
    }
}
```
* */







/*
⠀⠀⠀⡎⢉⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⢈⣁⠆⡀⠀⠀⠀⠀⣄⠀⠀
⠀⠀⠀⢳⢹⠁⠀⠱⡀⠀⠀⢈⠆⠀
⠀⠀⠀⠠⢾⣆⠀⠞⠁⠀⣠⠮⡤⡀
⠀⠀⠀⠀⠐⠹⢦⡀⠀⠰⠁⡀⢰⡁
⠀⠀⠀⢔⠞⠛⠶⣟⣦⣀⠀⠀⠛⠀
⠀⠀⠀⠌⣤⡴⠀⠀⠀⠉⠳⡰⡡⠄
⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡀⠹⡄⠀
⠀⠀⠀⠀⠀⠀⠀⠀⡇⢄⠙⢀⢷⢁
⠀⠀⠀⠀⣀⣄⡀⠀⠑⠀⠀⠊⣸⢰
⠀⠀⠀⡔⠁⠠⠗⠀⠀⠀⠀⠀⡭⠄
⠀⠀⠀⢣⠀⠀⠲⣄⣀⣀⢤⡾⠁⠀
⠀⠀⠀⠀⠑⠢⠀⠀⢠⣴⣟⠍⠀⠀
⠐⠄⠄⠤⢐⡢⣀⡼⡾⠋⠀⠀⠀⠀
⠀⠀⢀⠔⣡⣞⠏⠀⠀⠀⠀⠀⠀⠀
⠀⠔⢡⠞⠁⡎⠀⠔⠒⡄⠀⠀⠀⠀
⡎⢰⠃⠀⠀⡗⠄⠁⣀⠆⠀⠀⠀⠀
⠀⠁⢀⠃⠀⠣⡀⠀⠀⠀⠀⣥⠀⠱
⠀⠀⡂⢧⡀⠐⢌⣀⠀⠀⢀⡠⢠⢀
⠀⠀⠂⠀⠁⠀⠀⢐⠩⣏⣋⡸⠗⠊
⠀⠀⠀⠀⠀⠈⠉⡆⢡⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠑⠤⠔⠁⣸⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠥⠃⠀⠀⠀⠀
* */



/*
... These lectures move from the basic building blocks to more structural
concepts like managing memory buffers (Arrays), controlling access (Privacy),
and writing flexible code (Generics).

---



LECTURE 7: ARRAYS, MAIN ARGS, AND PRIVACY

   1. ARRAYS (FIXED-SIZE MEMORY)
      Unlike `List` (which is high-level and flexible), an `Array` is a
      low-level, fixed-size sequence of data. It maps directly to how memory is
      allocated.
      - FIXED SIZE: Once created, it cannot grow or shrink.
      - MUTABLE: You can change the contents at an index, just not the size.

      CODE EXAMPLE:
      A. SIMPLE ARRAY:
```Kotlin
// 1. Literal definition (creates size 5 automatically)
val numbers = arrayOf(1, 2, 3, 4, 5)

// Accessing elements (0-indexed)
println(numbers[3])
```





   B. CONSTRUCTOR (THE POWER USER WAY):
      This is useful when you need a big array initialised with a pattern.

```Kotlin
// Creates an array of size 100.
// The lambda `{ i -> i * 2}` initialises each slot based on its index.
// Index 0 -> 0, index 1 -> 2, Index 2 -> 4
val arr = Array(100) { i -> i * 2 }
```


   C. 2D ARRAYS (MATRICES/GRIDS):
      An array where every element is another array.
```Kotlin
// A 3x3 Grid
val grid = Array(3) { Array(3) { 0 } }
grid[0][0] = 1          // Set top-left corner to 1
```






   2. MAIN ARGUMENTS
   How do you pass information to a program before it starts running? Through
   the command line.

   CODE EXAMPLE:
```Kotlin
// `args` is an Array of Strings passed from the terminal.
fun main(args: Array<String>) {
    if (args.isNotEmpty) println("First argument: ${args[0]}")
}
```

   If you ran this in a terminal like `kotlin MyProgram.jar -debug`, then
   `args[0]` would be `"-debug"`
* */






/*
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⠳⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡏⠀⠙⢆⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡤⠴⠚⠀⠀⠀⠀⠀⠀⢀⠚⠁⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠓⢤⡀⠀⠀⠀⠀⠀⠀⢠⠊⠀⠀⠠⢀⠱⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⡇⠀⠀⠀⠀⠀⠀⢇⠀⠀⠈⠀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡇⢀⡤⠚⠐⠒⠠⠼⡄⠀⢀⣾⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠟⠉⠀⠀⠀⣀⣀⣀⣀⣠⢏⡌⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠹⣮⠩⣭⣥⣾⣿⣘⠳⠦⣤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣳⢹⣿⣿⣿⡿⣋⡴⠛⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⡏⢞⣋⣝⠻⡇⣏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠟⠛⠋⠈⠈⠳⢦⡗⠀⠀⠀⢰⣦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠀⢀⣠⡏⠈⢷⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⣿⡹⠉⠉⠀⠀⠀⠀⠉⣹⠏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠈⢡⠂⠀⠙⢦⠀⠀⠀⠀⢠⡞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠋⠁⠁⠀⠀⢼⠀⣀⡤⣄⡀⣷⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⡀⠀⠾⠋⠁⠀⠀⠙⠛⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⢀⣀⣀⣀⠞⣕⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠁⠐⡢⠀⠹⣌⢿⣾⣿⣶⡭⣹⠶⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠃⠀⠰⠀⠀⠀⡼⡼⢟⢿⡇⡎⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠀⠀⠀⠸⠓⠚⠉⠓⢧⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⡏⢧⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢶⣛⠉⠀⠀⢉⡼⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢈⡇⢀⣄⣈⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠟⠉⠀⠉⠙⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
* */


/*
   3. PRIVACY (Encapsulation)
      In Kotlin, everything is `public` by default. This means any other code
      can change your class's variables. To build safe software, we want to hide
      internal data
      - `private`: Only visible inside this specific class.
      - `public`: Visible to everyone.s

      CODE EXAMPLE:
```
class BankAccount {
    // PRIVATE: Outside code cannot touch this directly
    // This prevents someone doing `account.balance = -1`
    private var balance: Double = 0.0

    // PUBLIC: We provide a safe way to interact with the balance#
    fun deposit(amoun: Double): Unit = if (amount > 0) balance += amount
}
```









LECTURE 8: GENERICS and DATA STRUCTURE
   This is a big leap. Instead of writing code for one specific type (like a
   list of Integers), we write code for any type.

   1. GENERICS (`<T>`)
      Generics allow you to write a class or function once and reuse it for
      Strings, Integers, or custom objects. `T` is the standard placeholder
      name for "Type".

      CODE EXAMPLE:
```
// A generic class `Box` that can hold ANY type T
class Box<T>(val item: T)

fun main() {
    // T becomes Int
    val intBox = Box(1)

    // T becomes String. Same class code!
    val strBox = Box("Hello")
}
```





   2. THE LinkedList (Putting it together)
      The lecture uses a Linked List to demonstrate Generics. A Linked List is
      a chain of "Nodes."
         - NODE: Holds DATA and a reference (pointer) to the NEXT node.
         - LinkedList: A wrapper class that just holds the HEAD (the first node)

      CODE EXAMPLE:

      STEP 1: THE NODE
```Kotlin
// Data Class makes it easy to print
// `T` allows this node to hold anything
// `next` is nullable (Node<T>?) because the last node points to null.

data class Node<T>(val data: T, var next: Node<T>? = null)
```

      STEP 2: THE LIST WRAPPER
```Kotlin
class LinkedList<T> {
    // We start with no nodes, so head is null.
    private var head: Node<T>? = null

    fun add(item: T) {
        val newNode = Node(item)

        // Logic: If empty, new node is head.
        // If not, travel to the end and link it.
        if (head == null) {
            head = newNode
        } else {
            // (Traversal logic would go here)
        }
    }
}
```




* */







/*
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⠳⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡏⠀⠙⢆⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡤⠴⠚⠀⠀⠀⠀⠀⠀⢀⠚⠁⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠓⢤⡀⠀⠀⠀⠀⠀⠀⢠⠊⠀⠀⠠⢀⠱⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⡇⠀⠀⠀⠀⠀⠀⢇⠀⠀⠈⠀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡇⢀⡤⠚⠐⠒⠠⠼⡄⠀⢀⣾⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠟⠉⠀⠀⠀⣀⣀⣀⣀⣠⢏⡌⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠹⣮⠩⣭⣥⣾⣿⣘⠳⠦⣤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣳⢹⣿⣿⣿⡿⣋⡴⠛⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⡏⢞⣋⣝⠻⡇⣏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠟⠛⠋⠈⠈⠳⢦⡗⠀⠀⠀⢰⣦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠀⢀⣠⡏⠈⢷⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⣿⡹⠉⠉⠀⠀⠀⠀⠉⣹⠏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠈⢡⠂⠀⠙⢦⠀⠀⠀⠀⢠⡞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠋⠁⠁⠀⠀⢼⠀⣀⡤⣄⡀⣷⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⡀⠀⠾⠋⠁⠀⠀⠙⠛⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⢀⣀⣀⣀⠞⣕⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠁⠐⡢⠀⠹⣌⢿⣾⣿⣶⡭⣹⠶⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠃⠀⠰⠀⠀⠀⡼⡼⢟⢿⡇⡎⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠀⠀⠀⠸⠓⠚⠉⠓⢧⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⡏⢧⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢶⣛⠉⠀⠀⢉⡼⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢈⡇⢀⣄⣈⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠟⠉⠀⠉⠙⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
* */



/*
... LECTURE 2: FIXED-CAPACITY LISTS. This lecture is a classic "under the hood"
lesson. You normally use `ArrayList` or `mutableListOf`, but building one from
scratch teaches you about arrays, memory management, and why some operations
(like inserting in the middle) are slower than others.

... the implementation of `FixedCapacityIntList` (a list that only holds
integers).




S3: THE CONCEPT (REPRESENTATION)
   We need to fake a dynamic list using a fixed block of memory.
   - THE PROBLEM; Normal Arrays (`Array<Int>`) can't grow or shrink.
   - THE SOLUTION: We allocate a bigger array than we need immediately.
      - `elements`: The backing array (Capacity = N)
      - `size`: A counter tracking how many slots are we actually using.




S4-7: VISUALISING OPERATIONS
   These slides show the state of the array in memory.
   - EMPTY STATE: `size = 0`. The array is full of default values (e.g., `-1`),
     but we ignore them because `size` tells us they aren't "real" data.
   - ADDING: When you add `5`, it goes into index 0, and `size` becomes 1.
   - REMOVING (S5): This is the tricky part. If you remove the item at index 2:
      1. You shift everything after index 2 to the LEFT to fill the gap.
      2. You decrement `size`.
      3. CRUCIAL DETAIL: You usually don't bother overwriting the "ghost" data
         left at the end of the array. The slide asks "Why hasn't this been
         reset to -1?"
         - ANSWER: It's inefficient to wipe it. Since `size` decreased, our code
           will never look at that slot again, so it doesn't matter what junk is
           left there.




S9: THE CLASS STRUCTURE

```Kotlin
class FixedCapacityIntList(capacity: Int) {
    // 1. Track the size.
    // `private set` means outsider can read the size, but only WE can change it
    var size: Int = 0
        private set

    // 2. The Backing Array
    // We fill it with -1 placeholders.
    private val elements: Array<Int> =
        if (capacity < 0) {
            throw IllegalArgumentException
        } else {
            Array(capacity) { -1 }
        }
}
```
    KEY CONCEPT: `Array(capacity) { -1 }`. This constructor takes a size and a
    lambda function to initialise every slot. Here, it just puts `-1`
    everywhere.





SLIDE 11: ADDING AT AN INDEX
    This is the most complex algorithm in the class. To put a number in the
    middle, you have to make space.

```Kotlin
fun add(index: Int, element: Int) {
    // 1. Safety Checks
    if (size >= elements.size || index !in 0..size) {
        throw IndexOutOfBoundsException()
    }

    // 2. The Shift (Right)
    // We must loop BACKWARDS (downTo).
    // If we looped forward, we would overwrite the neighbour we are trying to
    // move!
    for (i in size downTo index + 1) elements[i] = elements[i - 1]

    // 3. Insert and update size
    elements[index] = element
    size++
}
```



SLIDE 12: METHOD OVERLOADING
   We want convenience. Usually, people just want to "add the end." We don't
   want to rewrite the logic, so we forward the call.

```Kotlin
// Helper method for the lazy user
fun add(element: Int) = add(size, element)
```

   This reuses the complex logic we just wrote, simply passing the current
   `size` as the insertion index.




S13: GETTING DATA
    Reading data is fast (O(1)), but we must protect against invalid access.

```Kotlin
fun get(index: Int): Int =
    // The range 0..<size excludes the upper bound (same as 0 until size)
    if (index !in 0..<size) {
        throw IndexOutOfBoundsException()
    } else {
        elements[index]
    }
```

    Note: The user might ask for index 5. Even if the array has capacity 10, if
    `size` is only 2, index 5 is invalid (it holds garbage data). The check
    `index !in 0..<size` prevents reading that garbage.
* */






/*
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⠳⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡏⠀⠙⢆⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡤⠴⠚⠀⠀⠀⠀⠀⠀⢀⠚⠁⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠓⢤⡀⠀⠀⠀⠀⠀⠀⢠⠊⠀⠀⠠⢀⠱⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⡇⠀⠀⠀⠀⠀⠀⢇⠀⠀⠈⠀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡇⢀⡤⠚⠐⠒⠠⠼⡄⠀⢀⣾⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠟⠉⠀⠀⠀⣀⣀⣀⣀⣠⢏⡌⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠹⣮⠩⣭⣥⣾⣿⣘⠳⠦⣤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣳⢹⣿⣿⣿⡿⣋⡴⠛⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⡏⢞⣋⣝⠻⡇⣏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠟⠛⠋⠈⠈⠳⢦⡗⠀⠀⠀⢰⣦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠀⢀⣠⡏⠈⢷⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⣿⡹⠉⠉⠀⠀⠀⠀⠉⣹⠏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠈⢡⠂⠀⠙⢦⠀⠀⠀⠀⢠⡞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠋⠁⠁⠀⠀⢼⠀⣀⡤⣄⡀⣷⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⡀⠀⠾⠋⠁⠀⠀⠙⠛⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⢀⣀⣀⣀⠞⣕⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠁⠐⡢⠀⠹⣌⢿⣾⣿⣶⡭⣹⠶⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠃⠀⠰⠀⠀⠀⡼⡼⢟⢿⡇⡎⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠀⠀⠀⠸⠓⠚⠉⠓⢧⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⡏⢧⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢶⣛⠉⠀⠀⢉⡼⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢈⡇⢀⣄⣈⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠟⠉⠀⠉⠙⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
* */



/*

PART 3: THE "ANY" LIST (A FAILED EXPERIMENT) [S15-27]
   The lecture asks: How do we make a list that holds Strings, or Persons,
   without rewriting the class every time?

   1. THE "POOR PERSON'S GENERIC LIST"
      The first attempt uses `Any` (Kotlin's root type, like `Object` in Java).
      - IDEA: Since everything is an `Any`, a `FixedCapacityList` can hold
        anything.
      - BACKING ARRAY: We use `Array<Any?>` because empty slots must be `null`.
      - INITIALISATION:
```
private val elements: Array<Any?> = arrayOfNulls(capacity)
```
      Note: `arrayOfNulls` creates an array where every slot starts as `null`.



   2. THE DANGER OF `Any`
      While this works, it introduces massive risks:
      - MIXED TYPES: You can accidentally put an `Int` into a list you intended
        for `Strings`.
```Kotlin
val list = FixedCapacityAnyList(10)
list.add("Hello")
list.add(42)        // Compiler allows this!
```
      - THE CRASH: When you get data out, the compiler only knows it's an `Any`.
        You have to cast it manually.
```Kotlin
// CRASH! ClassCastException (Int cannot be String)
val s = list.get(1) as String
```





   3. THE `!!` OPERATOR (NON-NULL)
      When writing the `get()` method for this list, we hit a snag. The array
      holds `Any?` (nullable), but we want to return `Any` (non-nullable).
         - THE INVARIANT: We know that if `index < size`, the slot isn't null.
         - THE FIX: We force the compiler to trust us using `!!`.
```Kotlin
fun get(index: Int) Any = if (index < size) return elements[index]!!
```





    /////////////////////////////
PART 4: THE GENERIC SOLUTION (`<T>`) [S 28-32]
   This is the correct way to solve the problem. Instead of `Any`, we use a TYPE
   PARAMETER `T`.


   1. THE CLASS DEFINITION
```Kotlin
class FixedCapacityList<T>(capacity: Int)
```
   Now, when you create the list, you specify what `T` is (e.g., `String`). The
   compiler enforces it.



   2. THE BACKING ARRAY
   This looks slightly different. We still use an array of nulls, but the type
   is `Array<T?>`.
```Kotlin
private val elements: Array<T?> =
    if (capacity < 0) throw IllegalArgumentException()
    else arrayOfNulls(capacity)
```
   (Note: In reality, you often have to cast this
   `arrayOfNulls<Any?>(capacity) as Array<T?>` though the slide simplifies this
   slightly for the concept).



   3. WHY THIS IS BETTER
      - SAFETY: `list.add(42)` fails at compile time if it's a `String` list.
      - CONVENIENCE: No manual casting needed.
```Kotlin
val s: String = list.get(0)
```



<<<<
SUMMARY OF THE EVOLUTION

`Int` // Simple, fast. // Only works for numbers.
`Any` // Works for everything. // DANGEROUS. No type safety. Crashes at runtime.
`<T>` // Works for everything. // SAFE. Compiler catches error.
* */






/*
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⠳⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡏⠀⠙⢆⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⡤⠴⠚⠀⠀⠀⠀⠀⠀⢀⠚⠁⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠓⢤⡀⠀⠀⠀⠀⠀⠀⢠⠊⠀⠀⠠⢀⠱⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⡇⠀⠀⠀⠀⠀⠀⢇⠀⠀⠈⠀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡇⢀⡤⠚⠐⠒⠠⠼⡄⠀⢀⣾⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠟⠉⠀⠀⠀⣀⣀⣀⣀⣠⢏⡌⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠹⣮⠩⣭⣥⣾⣿⣘⠳⠦⣤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣳⢹⣿⣿⣿⡿⣋⡴⠛⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⡏⢞⣋⣝⠻⡇⣏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠟⠛⠋⠈⠈⠳⢦⡗⠀⠀⠀⢰⣦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠀⢀⣠⡏⠈⢷⣀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⣿⡹⠉⠉⠀⠀⠀⠀⠉⣹⠏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠢⠈⢡⠂⠀⠙⢦⠀⠀⠀⠀⢠⡞⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠋⠁⠁⠀⠀⢼⠀⣀⡤⣄⡀⣷⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⡀⠀⠾⠋⠁⠀⠀⠙⠛⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⢀⣀⣀⣀⠞⣕⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⡀⠁⠐⡢⠀⠹⣌⢿⣾⣿⣶⡭⣹⠶⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠃⠀⠰⠀⠀⠀⡼⡼⢟⢿⡇⡎⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠀⠀⠀⠸⠓⠚⠉⠓⢧⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⡏⢧⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⢶⣛⠉⠀⠀⢉⡼⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢈⡇⢀⣄⣈⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠟⠉⠀⠉⠙⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
* */

/*
... this section introduces two major upgrades: making arrays that grow
automatically and building chains of data (Linked Lists).


   PART 1: THE RESIZING ARRAY LIST (S3-13)
   The `FixedCapacityList` we build before had a fatal flaw: it crashed if you
   added one too many items. The `ResizingArrayList` solves this by checking if
   it's full and GROWING if necessary.


   1. THE GROWTH STRATEGY (S5)
      When the array if full and you try to add an item:
      1. ALLOCATE: Create a new array with DOUBLE the capacity ($2 \times N$)
      2. COPY: Move all existing items to the new array.
      3. SWAP: Replace the odd array reference with the new one.
      4. ADD: Finally, insert the new item.

   2. CONSTRUCTORS (S6-7)
      We use a primary constructor for specific sizes and a secondary
      constructor for defaults.
```Kotlin
// Primary: For power users who know the size they need
class ResizingArrayList<T>(private val initialCapacity: Int) {

    // Secondary: For normal users (defaults to size 16)
    constructor() : this(16)

    init {
        if (initialCapacity < 0) throw IllegalArgumentException()
    }
}
```

* */


/*⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠
⠀⠀⠀⢢⣄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣤⣶⡿⠋
⠀⠀⠀⠀⠙⠪⣝⠒⠦⢤⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⠴⠊⢉⣴⣋⣀⠀
⠀⠀⠀⠀⠤⡶⠾⠷⣦⣀⡀⠉⠙⠒⠒⠒⠢⠤⢤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣠⠤⠖⠒⠋⠁⣀⣤⠶⠛⢉⡩⠇⠀
⠀⠀⠀⠀⠀⠉⠓⠤⣄⣈⣉⢉⡉⢈⣣⡶⠒⠒⠀⠀⠈⠙⢦⡀⠀⠀⠀⠀⠀⠀⠀⢀⡤⠞⠉⠠⠤⠶⢢⣎⡉⣉⣭⣤⡤⠞⠉⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⢤⡾⠋⠉⠉⣹⠏⠁⠈⣻⠟⠋⢁⣠⡆⠀⠹⠆⠀⠀⠀⠀⠀⣰⠏⠀⡀⠐⠒⠶⢶⠋⠀⢙⣢⠀⠀⢀⡬⠟⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠉⠑⠲⣶⠾⠛⢫⡟⠀⢉⣉⡿⢭⣤⠾⠖⠀⠀⠀⠀⠀⠀⣣⣀⣀⡛⣶⡖⠒⠉⠘⢯⠛⠚⠯⣍⠉⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠛⠦⠤⠤⠬⠝⢻⣛⡥⠖⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⢯⣙⠛⣳⡻⠭⠥⠤⠶⠚⠉⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
* */


/*
   3. THE `add` METHOD WITH RESIZING (S11)

   This is the most critical code snippet. It combines the "shift logic" from
   the fixed list with the new "resize logic."

```
fun add(index: Int, element: T) {
    if (index !in 0..size) throw IndexOutOfBoundsException()

    // THE UPGRADE: Check capacity & grow if needed
    if (size + 1 > elements.size) {
        // copyOf creates a new array of the new size and copies data over
        elements = elements.copyOf(2 * elements.size)
    }

    // Standard Shift Logic (same as FixedList)
    for (i in size downTo index + 1) {
        elements[i] = elements[i - 1]
    }

    elements[index] = element
    size++
}
```

    - WHY DOUBLE? Doubling the size $(2 \times)$ is much more efficient than
      adding just 1 slot at a time. It ensures that "expensive" resize
      operations happen rarely.






<<<<<<<<<<
   PART 2: SINGLY-LINKED LISTS (S 17-21)
   Now we abandon arrays entirely. Instead of a contiguous block of memory, we
   use scattered objects linked together.


   1. THE NODE (S 17-18)
      A "Node" is a container that holds data and a map to the next container.
```Kotlin
// It's private because the user shouldn't see `Nodes`, just the data inside.
private class Node<T>(var element: T, var next: Node<T>? = null)
            // Nullable because the last node points to nothing
```







   2. THE LIST WRAPPER (S19-21)
      The `SinglyLinkedList` class managed the chain. It holds the references
      to the start (`head`) and usually the end (`tail`).

```Kotlin
class SinglyLinkedList<T> {
    private var head: Node<T>? = null
    private var tail: Node<T>? = null
    var size: Int = 0
        private set
}
```



    Visualising the Chain ...


* */



/*
⠀⠀⠀⠀⠀⠀⠀⣀⡄⠀⠀⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠐⢿⠓⠀⢀⡴⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠹⡒⠤⣀⡀⠀⢀⡴⠋⢠⠇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠱⡀⠀⠉⠑⠋⠀⠀⣸⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⢱⡄⠀⠀⠀⠀⠀⠉⠒⠤⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⡴⠋⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣈⠵⠦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⢀⡤⠋⣀⣀⣀⣤⠀⠀⠀⢰⠋⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠈⠉⠁⠀⠀⠀⠀⢧⠀⠀⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⢐⣶⣆⠀⠀⢠⠈⢇⢰⠃⠀⠀⠀⠀⠀⠀⢀⠀⠀⠀⠀⠀⣰⡄⠀⠀⠀⠀⠀⠀
⠀⠈⠙⠀⠀⠀⣏⣧⠈⠟⠀⠀⠀⠀⠀⠀⠽⡿⠆⠀⠀⠀⢀⣿⣿⣦⣶⣶⠟⠀⠀
⠀⠀⠀⠀⣀⣸⣿⣯⢧⠤⢤⣤⣴⠦⠀⠀⠀⠁⠀⠀⠛⠿⣿⣿⣿⣿⣿⡁⠀⠀⠀
⠀⠙⠯⡻⣿⣿⣿⣿⣿⣿⡿⠟⠁⠀⠰⣄⣠⡇⠀⠀⠀⠀⢸⣿⡿⠛⠛⠿⣆⠀⠀
⠀⠀⠀⠈⢻⣿⣿⣿⣿⣿⠁⠀⠀⠀⣠⢿⣿⠟⠒⠀⠀⠀⠸⠊⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⡾⣿⠿⠺⢝⡯⢧⠀⠀⠀⠀⠀⠻⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⢼⠓⠁⠀⠀⠀⠉⠺⠆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⢿⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡜⠈⡇⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⢟⡒⠒⠛⠁⠀⠘⠒⠒⢲⡶⠂⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣤⡆⠀⠈⢢⠀⠀⠀⠀⡤⠚⠁⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠻⠉⠀⢠⠇⢀⡤⣀⠀⢳⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡿⠊⠁⠀⠈⠳⣼⡄⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⡄⠀⣀⠀⠀⢀⣄⡀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠶⢾⣿⣟⠁⠀⠀⠺⡟⠃
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⡏⢉⠓⠀⠀⠀⠀⠀
 */


/*          ---                 ---                 ---             --- */
/*
-   Assertions are expressions in programming that evaluate whether certain
    conditions within the code are as expected. If the result is true, the
    program continues to run.

    An assertion statement specifies a condition that you expect to be true at
    a point in your program. If that condition is not true, the assertion fails,
    execution of your program is interrupted, and the Assertion Failed dialog
    box appears.



- KOTLIN DATACLASSES                    // CHCET
  - `equals()` - to check if two objects are qual.
  - `hashCode()` - used when storing objects in hash-based collections.
  - `toString()` - to get a string version of the object
  - `copy()` - to copy an object with some modified value
  - `componentN()` - ...
* */




