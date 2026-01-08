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

fun main() {
    val name = "Matt"
    println("Hello $name")      // Prints: Hello Matt
}



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



class `Jan-08` {
}