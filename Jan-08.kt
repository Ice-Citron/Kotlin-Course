import kotlin.math.sqrt

/*
               .
              /=\\
             /===\ \
            /=====\' \
           /=======\'' \
          /=========\ ' '\
         /===========\''   \
        /=============\ ' '  \
       /===============\   ''  \
      /=================\' ' ' ' \
     /===================\' ' '  ' \
    /=====================\' '   ' ' \
   /=======================\  '   ' /
  /=========================\   ' /
 /===========================\'  /
/=============================\/

*/

/*
    Since you already have an OOP background, you will likely appreciate how
    Kotlin reduces boilerplate (like getters, setters and verbose assignments).


    1. THE ENTRY POINT: `main()`

    In Kotlin, `main` is a TOP-LEVEL FUNCTION, meaning it does not need to be
    wrapped inside a class like in Java.
        - SYNTAX: You simply define `fun main()`.
        - PRINTING: You can use `println()` directly. String templates allow you
          to insert variables using `$variable`.
 */

/*
fun main() {
    val name = "Matt"
    println("Hello $name")      // Prints: Hello Matt
}
*/


/*
    2. Variables and Types

    Kotlin focuses heavily on immutability (a concept from FP).
    - `val`: Read-only reference (Immutable). USE THIS BY DEFAULT.
    - `var`: Mutable reference (can be reassigned).
    - TYPES: Types are capitalized (e.g., `Int`, `Double`, `Boolean`, `String`).
      Kotlin can often infer this type, but function arguments must be
      explicitly typed.



    ----------
    3. DEFINING FUNCTIONS

    You define functions using the `fun` keyword. There are two main styles
    you will see in your notes:


    A. BLOCK BODY (Standard) Used when you have multiple lines of logic. It
       looks like a standard method in other languages.
 */
fun difference_(x: Int, y: Int): Int {
    if (x > y) return x - y
    else       return y - x
}

/*
*   B. EXPRESSION BODY (CONCISE) If a function returns a single expression, you
*   can remove the curly braces and the `return` keyword, using `=` isntead.
*   This is very common in Kotlin.
* */
fun successor_(x: Int): Int = x + 1
fun even_(x: Int): Boolean = x % 2 == 0


/*
    4. CONTROL FLOW: `if` and `when`

    - `if` IS AN EXPRESSION: Unlike many other languages, `if` in Kotlin returns
      a value, so you can assign the result of an if-statement to a variable.
    - `when`: This is Kotlin's replacement for `switch`. It is more powerful and
      can also return a value. It eliminates the need for `break` statements.
* */

/*
fun main() {
    val ifreturn = if (2 % 2 == 0) "hello" else "stupid fucks"
    println(ifreturn)
}
 */

fun signum_(x: Int): Int = when {
    x > 0  -> 1
    x == 0 -> 0
    else   -> -1
}

/*
    5. CLASSES AND OBJECTS (OOP)

    Kotlin simplifies class declarations significantly compared to Java/C++.

    DEFINING A CLASS You can define the class name and its PRIMARY CONSTRUCTOR
    (properties) in a single line.

    - The `val` keyword in the constructor automatically creates a read-only
      property (field + getter)
    - Methods are defined inside the class body.
* */

// Defines a class 'Circ2' with properties 'pos' and 'rad'
class Circ2(val pos: Point, val rad: Int) {

    // A method to calculate area
    fun area() = rad * rad * Math.PI
    val a = 3 + 3           // hovering over the plus operator shows us it's name and doc
}


/*
    OPERATOR OVERLOADING

    You can overload standard operations (like `+`) by using the `operator`
    keyword.
* */
// operator fun plus(p: Point): Point = Point(x + p.x, y + p.y)


/*
        You are getting this error because your `plus` function is defined
        OUTSIDE of any class (at the "top level" of the file), but it tries to
        access `x` and `y` as if it were inside a `Point` object.

        The error message `'operator' modifier ... must be a member of an
        extension function` is telling you exactly what is wrong: Kotlin doesn't
        know that this `plus` function is supposed to belong to the `Point`
        class.

    ------

        OPTION 1: The "Extension Function" (Recommended Fix)

        If you want to keep the function where it is (outside the class), you
        need to tell Kotlin that this function EXTENDS the `Point` class.
        You do this by adding `Point.` before the function name.
*/
class Point_(val x: Int, val y: Int)

// Note the "Point." prefix
operator fun Point_.plus(p: Point_): Point_ = Point_(x + p.x, y + p.y)

/*
*       WHY THIS WORKS:
*       - `Point.`: This tells Kotlin, "I am adding this function to the `Point`
*         type."
*       - `x` and `y`: because it is now an extension on `Point`, the function
*         can implicitly access the `x` and `y` of the `Point` instance on the
*         left side of the `+`.
* */



/*
*       OPTION 2: THE "MEMBER FUNCTION" (MOVE THE CODE)
*
*       Alternatively, you can move the function definition INSIDE the curly
*       braces of the `Point` class itself (if you have access to modify that
*       class file).
* */
class Point_2(val x: Int, val y: Int) {

    operator fun plus(p: Point_2): Point_2 = Point_2(x + p.x, y + p.y)
}

/*
                In Kotlin, you cannot define `operator fun (+)`. Instead, the
                language specification dictates that if you use the `operator`
                modifier, the function MUST have a pre-defined name to work with
                a specific symbol.

        HOW KOTLIN TRANSLATES OPERATORS

        When you write `a + b`, the Kotlin compiler literally transaltes that
        code into `a.plus(b)`. It looks for a function named `plus` marked with
        `operator`.


        WHY YOUR INTUITION WAS DIFFERENT

        In Kotlin, function names generally cannot contain symbols like `+`
        (unless you escape them with backticks for Java interoperability, but
        that doesn't trigger operator overloading). By enforcing English names
        like `plus` or `minus`, Kotllin avoids some of the parsing ambiguity
        that can happen in languages allowing arbitrary symbolic custom
        operators.
* */


/*
 /\/\/\                            /  \
| \  / |                         /      \
|  \/  |                       /          \
|  /\  |----------------------|     /\     |
| /  \ |                      |    /  \    |
|/    \|                      |   /    \   |
|\    /|                      |  | (  ) |  |
| \  / |                      |  | (  ) |  |
|  \/  |                 /\   |  |      |  |   /\
|  /\  |                /  \  |  |      |  |  /  \
| /  \ |               |----| |  |      |  | |----|
|/    \|---------------|    | | /|   .  |\ | |    |
|\    /|               |    | /  |   .  |  \ |    |
| \  / |               |    /    |   .  |    \    |
|  \/  |               |  /      |   .  |      \  |
|  /\  |---------------|/        |   .  |        \|
| /  \ |              /   NASA   |   .  |  NASA    \
|/    \|              (          |      |           )
|/\/\/\|               |    | |--|      |--| |    |
------------------------/  \-----/  \/  \-----/  \--------
                        \\//     \\//\\//     \\//
                         \/       \/  \/       \/
*/


/*
    6. FUNCTIONAL CONCEPTS: LISTS & LAMBDAS

    This is where the "Hybrid" nature of Kotlin shines. You often pass functions
    as arguments.

    LISTS
    - IMMUTABLE LISTS: Created with `listOf()`. You cannot add/remove items.            <-- interesting... i guess i never realised this but like... calling `listOf` alone is literally just a function/method call by itself!
    - MUTABLE LISTS: Created with `mutableListOf()`. You can use `.add()` or
      `.remove()`.

    LAMBDA (ANONYMOUS FUNCTIONS) A lambda is a function literal defined in curly
    braces `{ ... }`.
        - SYNTAX: `{ argument -> body }`
        - `it` keyword: If a lambda has only one argument, you can omit the
          definition and just refer to it as `it`.
*/
val nums_ = listOf(1, 2, 3)

fun main() {
    // Using map with explicit argument naming
    nums.map { x -> x * x }

    // Using map with the 'it' keyword (equivalent to above)
    nums.map { it * it }

    pairs.filter { pair -> pair.first + pair.second == x }

    val num = 3.toDouble()
    val num2 = 40.toFloat()
    println(40.squared())

    val circ1 = Circle30(Point30(0, 0), 5)
    val circ2 = Circle30(Point30(5, 0), 5)

    println(circ1.intersects(circ2))        // expects True
}


/*
        fun filterByCity(people: List<Person>, targetCity: String): List<Person> =
            people.filter { it.address.city == targetCity }
*/


class Circle30(val centre: Point30, val radius: Int) {
    fun intersects(c2: Circle30): Boolean {
        val temp = (centre.x-c2.centre.x)*(centre.x-c2.centre.x) + (centre.y-c2.centre.y)*(centre.y-c2.centre.y)
        val distance = sqrt(temp.toDouble())
        if (distance > (radius + c2.radius)) return false       // don't forget about the interpretation of final answer!! don't forget about return now that your back in  OOP Kotlin
        else                                 return true
    }
}

class Point30(val x: Int, val y: Int)


/*
    Higher-Order Functions

    Functions like `map` and `filter` take other functions as input.

    - FILTER: Returns a list containing only elements that match a condition.
* */



val as22 = Pair(3, 3)
val test22 = "hello-world".reversed()

/*~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^*/
/*
*   EXERCISE 2
*
            *       // string literal
                    val string = "hello world"

                    // constructing a pair
                    val p_: Pair<Int, String> = Pair(3, "C")

                    // property access
                    val l_ = string.length
                    val n_ = p_.first

                    // method call
                    val uc_ = string.uppercase()
*
* */




/*~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^*/

/*
    In Kotlin, `typealias` is almost exactly the same as `type` in Haskell.


    1. LIKE HASKELL'S `type` (SYNONYM)

    - WHAT IT IS: It is just a different name for an existing type. It does not
      create a new type.
    - INTERCHANGEABILITY: If you define `typealias Point = Pair<Int, Int>`, you
      can absolutely pass a raw `Pair<Int, Int>` into a function that asks for
      a `Point`, and vice versa. The compiler sees them as the exact same thing.
    - NO RUNTIME OVERHEAD: It is erased at compile time.


    2. NOT LIKE HASKELL'S `newtype` or `data`

    - NO TYPE SAFETY: In Haskell, `newtype` creates a distinct type that the
      compiler enforces (you can't accidentally pass an `Int` to a function
      expecting a `UserId` if `UserId` is a `newtype` of `Int`).
    - In Kotlin, `typealias` DOES NOT offer this safety. If you alias
      `typealias UserId = Int` and `typealias Age = Int`, you can accidentally
      pass an `Age` to a function expecting a `UserId`, and the compiler won't
      stop you.

    This is purely for READABILITY. It clarifies that this specific pair of
    integers represents a coordinate in space, rather than just two random
    numbers.



    SUMMARY
    - KOTLIN `typealias` = Haskell `type`   (Synonym, completely interchangeable)
    - KOTLIN `inline class` / `value class` = Haskell `newtype`
            (Wrapper, distinct type, optimized).
* */






/*
            class Address(val street: String, val city: String)

            class Person(val name: String, val age: Int, val address: Address)


            fun main() {
                val personList = listOf(
                    Person("Alice", 25, Address("Baker St", "London")),
                    Person("Bob", 30, Address("Main St", "New York"))
                )

                val filteredList = personList.filter { p -> p.address.city == "London" }
                println(filteredList.map { it.name })
            }
* */

/*~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^*/


// RESTART FROM METHODS.CTD ONWARDS

/*
    1. ENCAPSULATION: PUT LOGIC WHERE IT BELONGS

    The slides show a progression of how to check if a point is inside a circle.

    - THE "MESSY" WAY: Calculating the Euclidean distance formula inside the
      `Circle` class. This clutters the Circle class with math that really
      belongs to `Point`.

    - THE "CLEAN" WAY (DELEGATION):
        1. Give the `Point` class a `distance(other: Point)` method.
        2. Now the `Circle` class only needs to ask: "Is the distance from my
           center to that point less than my radius?"
        3. Code: `fun includes(p: Point) = rad > pos.distance(p)`


    TAKEAWAY FOR YOUR EXERCISE:

    If you find yourself writing complex math about a specific property (like
    coordinates), ask yourself if that logic should be moved into the class that
    owns these properties (e.g., `Point`).
* */
fun distanceBetween2(a: Point_1, b: Point_1): Double {
    val xDiff = a.x - b.x                     // val xDiff = a.first - b.first
    val yDiff = a.y - b.y                     // val yDiff = a.second - b.second
    return sqrt((xDiff * xDiff + yDiff * yDiff).toDouble())
}

class Point_1(val x: Int, val y: Int)

class Circ_2(val pos: Point_1, val rad: Int) {
    fun area() = rad * rad * kotlin.math.PI
    fun includes(p: Point_1) = rad > distanceBetween2(pos, p)
}

class Point_3(val x: Int, val y: Int) {
    fun distance(positionReceived: Point_3): Double {
        val dx = x - positionReceived.x
        val dy = y - positionReceived.y
        return sqrt((dx*dx + dy*dy).toDouble())
    }
}

class Circ_3(val pos: Point_3, val rad: Int) {
    fun area() = kotlin.math.PI * rad * rad
    fun includes(p: Point_3) = rad > pos.distance(p)
}





/*~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^*/
/*
                 _______
                / _____ \
          _____/ /     \ \_____
         / _____/  311  \_____ \
   _____/ /     \       /     \ \_____
  / _____/  221  \_____/  412  \_____ \
 / /     \       /     \       /     \ \
/ /  131  \_____/  322  \_____/  513  \ \
\ \       /     \       /     \       / /
 \ \_____/  232  \_____/  423  \_____/ /
 / /     \       /     \       /     \ \
/ /  142  \_____/  333  \_____/  524  \ \
\ \       /     \       /     \       / /
 \ \_____/  243  \_____/  434  \_____/ /
 / /     \       /     \       /     \ \
/ /  153  \_____/  344  \_____/  535  \ \
\ \       /     \       /     \       / /
 \ \_____/  254  \_____/  445  \_____/ /
  \_____ \       /     \       / _____/
        \ \_____/  355  \_____/ /
         \_____ \       / _____/
               \ \_____/ /
                \_______/
* */




/*
    2 Making Classes "Human Readable" (`toString`)

    By default, printing an object gives you a memory address (e.g.,
    `Point@548c23ks`). To fix this, you OVERRIDE the standard string #
    representation.

    - SYNTAX: You must use the `override` keyword because `toString()` belongs
      to the ultimate parent class `Any` (Java's `Object`).
    - EXAMPLE:
* */
class Jolie(val name: String, val age: Int) {
    override fun toString(): String = "Jolie's name is $name and shes $age y.o."
}




/*
    3. DESTRUCTURING (`componentN`)

    You might see code like `val(x, y) = myPoint`. This is called DESTRUCTURING
    DECLARATION.

    - It allows you to unpack an object into variables in one line.
    - HOW IT WORKS: Kotlin looks for functions named `component1()`,
      `component2()`, etc.
    - DATA CLASSES: If you use `data class Point(...)`, Kotlin generates these
      for you automatically! If you use a normal `class`, you have to write them
      manually using `operator fun component1() = x`.
*/
class Jolie2(val name: String, val age: Int) {
    override fun toString(): String = "Jolie's name is $name and shes $age y.o."

    // destructuring!
    operator fun component1(): String = name
    operator fun component2(): Int = age
}
/*~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^*/





/*
    Since you are fresh from Haskell exams, the best way to explain this is:

    - STANDARD CLASS ~=~ A standard OOP definition.
    - DATA CLASS ~=~ A Haskell `data` type with `deriving (Eq, Show)`. It has
      VALUE (structural equality).

    ------

    1. THE "FREE" STUFF (Boilerplate Generation)

    When you add the keyword `data` before `class`, the Kotlin compiler
    automatically generates the following methods based ONLY on the properties
    in the primary constructor.

    - FEATURE: `toString()`
        - Standard `class`: Prints memory address (e.g. `Person@3t31s0`)
        - `data class`: Prins value (e.g. `Person(name="Matt", age=30)`)

    - FEATURE: `equals()`
        - Standard `class`: Checks REFERENCE Equality (Are they the same object
          in memory?)
        - `data class`: Checks STRUCTURAL Equality (Do they have the same
          content?)

    - FEATURE: `hashCode()`
        - Standard `class`: Based on memory address
        - `data class`: Based on property values (safe for HashMaps)

    - FEATURE: `copy()`
        - Standard `class`: Does not exist
        - `data class`: Based on property values (safe for HashMaps)

    - FEATURE: DESTRUCUTRING `componentN()`
        - Standard `class`: Must manually define `operator fun component1()` etc.
        - `data class`: AUTO-GENERATED. Enables `val (name, age) = person`.
*/



/*~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^*/


/*
    2. THE HASKELL ANALOGY

    Since you know Haskell:

    - DATA CLASS: Think of it as:

        ```Haskell
        data Person = Person { name :: String, age :: Int } deriving (Show, Eq)
        ```

      The `copy()` method in Kotlin is basically record update syntax in
      Haskell: `person { name = "New Name" }`.

    - STANDARD CLASS: Think of it as a raw data type without deriving anything.
      You have to manually write instance definitions for `Show` (`toString`)
      and `Eq` (`equals`).




    ------
    3. CODE COMPARISON

    STANDARD CLASS (THE HARD WAY) If you wanted a standard class to behave like
    a data class, you would have to write all this manually:
* */

class User_(val name: String, val age: Int) {
    // You have to write this yourself
    override fun toString(): String = "User Jolie is [$name, $age]"

    //      CHCET       =       copy, hashCode, componentN, equals, toString

    // You have to write this yourself
    override fun hashCode(): Int = 31 * name.hashCode() + age

    // You have to write this yourself (and it's painful)
    fun copy(name: String = this.name, age: Int = this.age): User_
        = User_(name, age)

    // You have to write this yourself
    operator fun component1() = name
    operator fun component2() = age

    // You have to write this yourself
    override fun equals(other: Any?): Boolean {
        return true
    }

}

                // difference --> toString()    // equals()     // hashCode()       // copy()       // componentN()
                // CHC.ET
                    /*
                        copy()
                        hashCode()
                        componentN()
                        equals()
                        toString()
                    * */



/*
    DATA CLASS (THE EASY WAY)
* */
data class User_2(val name: String, val age: Int)
    // That's it! Everything above is generated automatically.

/*
    4. CONSTRAINTS

    There's a "catch". To ensure the guaranteed code works correctly, data
    classes have rules:

    1. The primary constructor needs at least one parameter.
    2. All primary constructor parameters must be marked as `val` or `var.`
    3. Data classes cannot be `open`, `abstract`, or `sealed` (they are final).


    WHEN TO USE WHICH?
    - Use DATA CLASS for holding data (models, JSON parsing results,
      coordinates).
    - Use STANDARD CLASS for holding logic or state that shouldn't just be
      printed/copied blindly (Services, Managers, UI Controllers).
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
*/


/*
    4. FP vs. OOP Design (`scaleBy` and `translate`)

    The slide poses a key design question: "UPDATE THE OBJECT OR RETURN A NEW
    OBJECT?".

    This depends on whether your class is MUTABLE (`var`) or IMMUTABLE (`val`).

    - OPTION A: MUTABLE (CLASSIC OOP)
        - Properties are `var`.
        - The method returns `Unit` (nothing).
        - EFFECT: The circle itself changes sie.

```Kotlin
fun scaleBy(factor: Int) {
    this.radius *= factor
}
```

    - OPTION B: IMMUTABLE (Functional Style -- Recommended for this course)
        - Properties are `val`.
        - The method returns `Circle`.
        - EFFECT: The old circle stays the same; you get a new circle that is
          bigger.

```Kotlin
fun scaleBy(factor: Int) {
    return Circle(this.center, this.radius * factor)
}
```


    -- Since the course emphasizes "Functional Programming in an OOP language,"
       default to OPTION B (returning a new object) unless the instructions
       explicitly ask you to modify state.
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
*/


/*
    1. THE `Any` CLASS: THE MOTHER OF ALL OBJECTS

    In Kotlin, `Any` is the root of the class hierarchy. Every class you define,
    and every built-in type, eventually inherits from `Any`.

    - WHAT IT IS: It is the equivalent of `java.lang.Object` in Java.
    - WHAT IT GIVES YOU: Because everything inherits from `Any`, every single
      object in Kotlin is guaranteed to have three methods:
        1. `equals()`
        2. `hashCode()`
        3. `toString()`

    This is why you can call `.toString()` on anything, whether it's a number
    `1.toString()`, a generic list, or a custom `Person` object.



    2. "EVERYTHING IS AN OBJECT" (NO PRIMITIVES... sort of)

    You asked if types like `Int` are "primtives" or classes.

    - IN SYNTAX (Your View): They are CLASSES. There is no `int` vs `Integer`
      distinction like in Java. You just use `Int`. It has methods, it has
      properties (like `Int.MAX_VALUE`), and it inherits from `Number`
      (which inherits from `Any`).
    - UNDER THE HOOD (Compiler View): For performance, the Kotlin compiler acts
      smart. If it can, it compiles your `Int` class down to a raw Java
      primitive `int` in the bytecode. But you, the programmer, don't need to
      worry about that. You treat them as objects.


    3. Are `fun`, `operator`, and `=` Classes?

    NO. These are KEYWORDS (part of the language syntax), not classes.

    - `class`, `fun`, `val`: These are instructions to the compiler. You cannot     <-- SPECIAL CASES
      instantitate a `fun`.
    - `=`: This is a syntactic assignment operator.                                 <-- SPECIAL CASE
    - `+`, `-`, `*`: These are distinct. While `+` is an operator, Kotlin maps      <-- REASSIGNMENT CASE
      it to a FUNCTION CALL inside a class.
      - Writing `a + b` is syntactic sugar for calling the function `a.plus(b)`.
      - So, the `+` symbol isn't a class, but it TRIGGERS a method belonging to
        a class.



    4. INHERITANCE REFRESHER

    In Java, all classes are "inheritable" (open) by default. In Kotlin, the
    philosophy is "Design for inheritance or forbid it."

    RULE 1: CLASSES ARE CLOSED (`final`) BY DEFAULT                             <-- WOW THIS IS INSANE! AN INCREDIBLE FEATURE FOR KOTLIN INDEED
        You cannot inherit from a standard class. You must explicitly mark it
        as `open` to allow others to extend it.
    RULE 2: METHODS ARE CLOSED BY DEFAULT
        Even if the class is open, you cannot override its functions unless they
        are also marked `open`.
    RULE 3: THE SYNTAX
        - Use a colon `:` to inherit.
        - You must call the parent's constructor immediately using `()`.



    INHERITANCE CODE EXAMPLE

    Here is how you would write a hierarchy where `Animal` is the parent and
    `Dog` is the child.
* */



// 1. 'open' allows other classes to inherit from this
open class Animal_(val name: String) {

    // 2. 'open' allows subclasses to override this specific method
    open fun makeSound() {
        println("Some generic animal sound")
    }

    // This method is NOT open, so it cannot be overriden
    fun sleep() {
        println("Zzz...")
    }
}


// 3. use `:` to inherit. passed `name` to the parent constructor
class Dog(name: String) : Animal_(name) {

    // 4. 'override' is required to modify behavior
    override fun makeSound() {
        println("Woof!")
    }
}


/*
fun main() {

    val d = Dog("Rex")
    d.makeSound()       // Prints: Woof!
    d.sleep()           // Prints: Zzz... (inherited from Animal)

    // 5. Polymorphism: A Dog IS-A Animal, and an Animal IS-A Any
    val a: Animal = d
    val any: Any = d                                                    // ah yes... this is very C++ like. but still feels surreal to see this again!!!
}
*/


/*
        SUMMARY HIERARCHY

        So, the hierarchy for the intefer `5` looks roughly like this:

        `Any` (Has `toString`, `equals`) <---Inherit from--- `Number` (Base for
        numeric types) <---Inherit from--- `Int` (The class you use)


        And for the `Dog` above:

        `Any` (UP) `Animal` (UP) `Dog`
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
*/



/*
    Here is the breakdown. Since you know Haskell, some of these will map
    directly to concepts you already know (especially `sealed` classes).

    In Kotlin, classes are `final` BY DEFAULT. This means you CANNOT inherit
    from a class unless you explicitly allow it. This is the opposite of Java
    (where everything is open by default).


    1. `open`: THE "UNLOCK" KEY

      - WHAT IT DOES: It allows other classes to inherit from this class. It
        allows methods to be overridden.
      - WHEN TO USE IT: When you are building a standard class (like `Person`)
        that works fine on its own, but you want to allow someone else to extend
        it (like `Student`).
      - HASKELL ANALOGY: Standard types that don't enforce restrictions.
* */


// 1. You MUST write 'open' to allow inheritance
open class Person_2(val name: String) {
    // 2. You MUST write 'open' to allow overriding this function
    open fun describe(): String = "I am a person named $name."
}

class Student(name: String) : Person_2(name) {
    override fun describe(): String = "I am a student named $name."
}

// Usage
val p_ = Person_2("Matt")                  // Valid: Can instantiate the parent directly
val s_ = Student("Alice")




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
*/

/*
    2. `abstract`: THE "INCOMPLETE TEMPLATE"

    - WHAT IT DOES: You CANNOT create an instance of an abstract class. It is
      "half-finished." It can hold state (properties) and methods with code,
      but it can also have `abstract` methods that have NO BODY. Subclasses         <-- i remember this type of superclass in C++ too forgot what it's called
      must fill in the blanks.                                                      <-- AH YES! VIRTUAL CLASSES!!!

    - WHEN TO USE IT: When you have a base concept (like `Shape` or `Animal`)
      that shouldn't exist on it's own. You never see a generic "Shape" floating
      in the void; you only see a CIRCLE or a SQUARE.

    - Haskell Analogy: Like a TYPE CLASS (conceptually), but with internal
      state.
* */

abstract class Shape {
    // Standard state (inherited by everyone)
    val color: String = "Red"

    // Abstract method: NO BODY. Subclasses are forced to write this.       <-- ah i get it now... so it means empty {} body!!
    abstract fun area(): Double
}

// ERROR: Cannot create an instance of an sbtractg class
// val s = Shape()                  <-- funny enough above is the actual error, lol!


class Circle_2(val radius: Double) : Shape() {
    override fun area() = Math.PI * radius * radius
}






/*~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^*/

/*

    3. `sealed`: THE "RESTRICTED SET" (YOUR HASKELL FRIEND)

    - WHAT IT DOES: It restricts the hierarchy. All subclasses must be known at
      compile-time (usually defined in the same file). You cannot create a
      subclass of a sealed class outside of the module where it is defined.

    - WHEN TO USE IT: When you want to represent STATE or ALGEBRAIC DATA TYPES
      (ADTs).

    - HASKELL ANALOGY: This is exactly a SUM TYPE (`data type` with `|`).
        - Haskell: `data Result = Success String | Failure Int`
        - Kotlin: `sealed class Result` with subclasses `Success` and `Failure`.

    Because the compiler knows every possible subclass, `when` expressions
    become EXHAUSTIVE (you don't need an `else` branch).
* */

sealed class NetworkResult {
    data class Success(val data: String) : NetworkResult()
    data class Error(val code: Int, val message: String) : NetworkResult()
    object Loading : NetworkResult()        // Singleton state              <-- I'm still actually confused myself as with how this `object Loading...` stuff actually works mhmmm...
}

fun handleResult(result: NetworkResult) {
    // look! No 'else' branch needed. Kotlin knows these are the only 3 options.#
    when (result) {
        is NetworkResult.Success -> println("Got Data: ${result.data}.")
        is NetworkResult.Error   -> println("Error ${result.code}.")            // <-- interesting how with how when() {} needs like all 3 possible subclasses of like sealed superclass to be valid... i wonder what does when do and may i
                                                                                // ask like when should I use when() and when do I not use it? thanks
        is NetworkResult.Loading -> println("Please wait...")
    }
}

/*
* Summary Comparison
*
*
*
*
*
* */


/*
* QUESTION FOR TOMMOROW

        - Sorry but i still can't see why is it that `sealed` is used for STATE
        * or ADTs... like how?? i still can't... like please provide me some
        * examples to run through. thanks!... like more diff examples please ish
        * cuz i can kinda see it with NetworkResult... but like i still need a
        * bit more to visualise please. thank you! like give me examples shown in kotlin exams if possible. thanks
*
* */



















class `Jan-08` {
}