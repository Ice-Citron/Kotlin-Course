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
}


















class `Jan-08` {
}