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


//


fun main() {
    println(successor(2)) // function call
}