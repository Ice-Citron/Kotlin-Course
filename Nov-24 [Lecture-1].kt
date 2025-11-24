// Kotlin is a functional, object-oriented, null safe, flexible language built
// on the JVM

// Week 1 - Building functional programs
// Week 2 - Defining and using classes and objects
// Week 3 - Mutability, null and Exceptions
// Week 4 - IO

/* -------- -------- -------- -------- -------- -------- -------- -------- */

// Lecture 1 - Intended Learning Outcomes
/*
- Getting started with a Kotlin project in IntelliJ
- Function definitions
- Basic types
- Conditional logic
- String manipulation
 */

import kotlin.math.PI

// function keyword // function identifier // function arguments
// return type // function definition
fun successor(x: Int): Int = x + 1

fun successor1(x: Int): Int {
    return x + 1;       // Function defintion
}

// Kotlin Boolean Syntax
    // Write a function to check if an integer is even
/*
- function identifier
- integer type
- native types always capialised
- function definition: % is `mod`
- no need for a return keyword for single expression functions
 */
fun even(x: Int): Boolean = x % 2 == 0
fun even2(x: Int): Boolean {
    return (x % 2 == 0)
}


// Multiple arguments
    /*
    - Write a function to return the difference between two integers
        - Conditional expression in brackets
        - space before condition-controlled logic
     */
fun difference(x: Int, y: Int): Int {
    if (x > y) return x - y
    else return y - x
}


// When conditional logic
    /*
    - Write a function to return the signature of an integer
    - Note
        * The arrow -> operator
        * The necessary else
        * Single expression function
        * The (lack of) styling
     */
fun signum(x: Int): Int =
    when {
        x > 0 -> 1
        x < 0 -> -1
        else  -> 0
    }


// Block body
    // Write a function to calculate the number of turns of a measuring wheel
    // per a given distance
fun turns(start: Double, end: Double, radius: Double): Double {
    val kmToM = 1000
    // val distance = (end-start)*kmToM
    return (end - start)*kmToM / (2 * PI * radius)
}

/* -------- -------- -------- -------- -------- -------- -------- -------- */

// EXERCISES

fun bigger(x: Int, y: Int): Int =
    when {
        x > y -> x
        y > x -> y
        else  -> x // or y, because they're equal
    }

// or more simply
fun bigger2(x: Int, y: Int): Int =
    when {
        x >= y -> x
        else   -> y
    }

fun biggestOfThree(x: Int, y: Int, z: Int): Int {
    return bigger(bigger(x, y), z)
}

fun fact(n: Int): Int {
    if (n == 0) return 1
    else        return n * fact(n-1)
}

// Write a recursive function fib() to calculate the nth Fibonacci number
    // existing function is tail-recursive and O(n)
fun fib(n: Int): Int {
    if (n == 1 || n == 2) return 1
    fun go(x: Int, y: Int, n: Int): Int {
        if (n == 0) return y
        else return go(y, x+y, n-1)
    }
    return go(1, 1, n-2)
}

// more compact version
    /*
    - Added ```tailrec``` keyword. This tells the compiler to optimize the tail
      recursion into a loop, guaranteeing no stack overflow.
    - Simplified the base case logic by starting with ```k = n - 1```
    - Used expression body with ```if/else``` instead of block body with
      ```return```
     */
fun fibCompact(n: Int): Int {
    tailrec fun go(a: Int, b: Int, k: Int): Int =
        if (k == 0) a else go(b, a + b, k -1)
    return go(1, 1, n - 1)
}

/* -------- -------- -------- -------- -------- -------- -------- -------- */

// String
    /*
    - Write a function that takes a String argument called name and return Hello
      name
        - Make first letter of name uppercase
     */
fun helloName(name: String): String =
    "Hello " + name[0].uppercaseChar() + name.substring(1)

    /*
    - Write a function that returns "Hello", "world" or "Hello World" depending
      on what the first letter of the argument is
     */
fun stringMan(str: String): String =
    when {
        str[0] == 'j' -> "hello"
        str[1] == 'a' -> "bruh"
        else -> "nope"
    }

fun main() {
    println(successor(2)) // function call
}

/*
Defining Functions review

- Function declarations
    - Arguments
    - Return types
    - Expressions vs block bodies
- Common Basic (native) types
    - Integer, Double, Boolean, String, Pair, List
- String manipulation
 */