// Intended Learning Outcomes
/*
Type arguments
- Pairs
- Lists

Higher order functions
- Map and filter
- Anonymous functions
- Composition and functional design

Project structure
- main()
- Gradle build
 */
import kotlin.math.pow
import kotlin.math.sqrt

fun distanceBetween(
    a: Pair<Int, Int>,
    b: Pair<Int, Int>,
): Double {
    val xDiff = a.first - b.first
    val yDiff = a.second - b.second
    return sqrt((xDiff * xDiff + yDiff * yDiff).toDouble())
}

// ------ ------ ------ ------ ------ ------ ------ ------ ------ ------

// Or you can define extension functions for cleaner syntax:
fun Int.squared() = this * this

fun Int.cubed() = this * this * this

// If you're working with ```Double```m ```import kotlin.math.pow``` works.
// For Integers, where you want an integer results, multiplication is cleaner
// than ```Math.pow(x.toDouble(), 2.0).toInt()```.

// ------ ------ ------ ------ ------ ------ ------ ------ ------ ------

/*
- On `this`: Yes, `Int` is a class, and `this` refers to the instance the method
  is called on. When you write `5.squared(), `this` is `5`. And yes,
  `this * this` works because `Int` has an operator functioned defined for `*`
  ---something like `operator fun times(other: Int): Int`. So `this * this` is
  really `this.times(this)`.

- On type inference: Kotlin can infer return types for single-expression
  functions (the `= ...` form) So these are equivalent:
 */
fun Int.squared2(): Int = this * this // explicit return type

fun Int.squared3() = this * this // inferred return type

// The compiler sees `Int * Int` and knows the result is `Int`

// However, for block-body functions (with `{}`), you must declare the return
// type
fun Int.squared4(): Int = this * this

/*
    So it's not quite like Haskell where types are always optional---it depends
    on the function form. The rule is: expression body can infer, block body
    cannot.

    Also, parameter types are never inferred---you always have to write those
    explcitly:
 */
fun add(
    x: Int,
    y: Int,
): Int = x + y // can't omit the `: Int` on parameters

// ------ ------ ------ ------ ------ ------ ------ ------ ------ ------
// Encapuslation

/*
    Encapsulation is about controlling access to a class's internals---hiding
    implementation details and exposing only what's necessary.

# Visibility Modifiers

    Kotlin has four visibility levels:
 */

class BankAccount(
    private val accountNumber: String,
) {
    // only visible inside this class
    private var balance: Double = 0.0

    // visible in this class + subclasses
    protected open fun log(msg: String) {}

    // visible within the same module
    internal fun audit() {}

    // public (default), visible everywhere
    fun deposit(amount: Double) {}
}

// Why encapuslate?
// Consider this unencapuslated class:

class BankAccount2 {
    var balance: Double = 0.0
}

val account = BankAccount2()
// account.balance = -1000.0       // nothing stops invalid state
// normally need to put code above in main() function

// Now with encapsulation:
class BankAccount3 {
    private var balance: Double = 0.0

    fun deposit(amount: Double) {
        if (amount > 0) balance += amount
    }

    fun withdraw(amount: Double): Boolean {
        if (amount > 0 && this.balance >= amount) {
            balance -= amount
            return true
        }
        return false
    }

    // function is public by default
    fun getBalance(): Double = this.balance
}

/*
// Run this in main()

val account2 = BankAccount3()
account2.deposit(100)
account2.withdraw(99)
account2.balance = -999.0       // compile error--can't access private field
 */

// ------ ------ ------ ------ ------ ------ ------ ------ ------ ------
// ENCAPUSLATION2
// ------ ------ ------ ------ ------ ------ ------ ------ ------ ------

// Properties with custom accessors
// Kotlin has a cleaner pattern using property getters/setters:
class Temperature {
    var celsius: Double = 0.0
        set(value) {
            if (value >= -273.15) {
                field = value
            }
            // 'field' is the backing field
        }

    val fahrenheit: Double
        get() = celsius * 9 / 5 + 32
    // computed property, no backing field
}

/*
// Run this in main()

val temp = Temperature()
temp.celsius = 25.0
println(temp.fahrenheit)    // 77.0
temp.celsius = -300.0       // silently rejected, stays at 25.0
 */

// Private setters
// Common pattern--readable publicly, writable only internally:
class Counter {
    var count: Int = 0
        private set

    fun increment() {
        count++
    }
}

/*
// Run this in main()

val c = Counter()
println(c.count)    // fine, can read
c.count = 10        // compile error, can't write
c.increment()       // must use the method to increment...
 */

// The core idea: expose behavior (methods), hide state (fields). This lets you
// change the internal implementation later without breaking code that uses your
// class.

// ------ ------ ------ ------ ------ ------ ------ ------ ------ ------
// EXERCISE
// ------ ------ ------ ------ ------ ------ ------ ------ ------ ------

/*
    Kotlin is an object-oriented language, and almost every piece of data we use
    in our programs is an object. This means that as well as encapuslating some
    data, they provide a set of methods, which are functions that act on that
    data.
 */

/*
SYNTAX REMINDER:
    We create objects by calling constructors. These look like regular function
    calls, but match the parameters of the types being constructed. Strings
    are objects, and we often create a string just by using a string literal.
    We call methods and access properties by using dot notation. A method call
    ends with a pair of parentheses, but a property access does not.
 */

// string literal
val str: String = "hello world"

// constructing a pair
val p: Pair<Int, String> = Pair(3, "C")

// property access
val l = str.length
val n = p.first

// method call
val uc = str.uppercase()

fun snap(p: Pair<String, String>): Boolean {
    if (p.first.isEmpty() && p.second.isEmpty()) return true
    for (i in p.first.lowercase()) {
        for (j in p.second.lowercase()) {
            if (i == j) return true
        }
    }
    return false
}
// returns true if both empth because of vacuous truth...
        /*
            The question is: "do these two strings share a matching character?"
            For empty strings, there are no characters to *Fail* the match. It's
            like asking "are all elements in an empty set positive?"--vacuously
            true because there's no counter-example.

            But honestly, this is a design decision. The test is asserting that
            two empty strings should "match", probably because they're
            considered equal (`"" == ""`). Your logic checks for a common
            character, which empty strings can't have.
         */

fun isPalindrome(str: String): Boolean {
    val length = str.length
    if (length <= 1) return true
    if (str[0] == str[length - 1]) {
        return true && isPalindrome(str.substring(1, length - 1))
    } else {
        return false
    }
}

// Cleanup version
fun isPalindrome2(str: String): Boolean {
    if (str.length <= 1) return true
    return str.first() == str.last() && isPalindrome2(str.drop(1).dropLast(1))
}
    /*
        using `String.first()` and `String.last()` here instead. ... much
        cleaner for indexing.
        `drop(1).dropLast(1)` is more readable than `substring`, and
        `return true &&` is redundant--just return the condition directly.
     */

// tail-recursive variant
/*
    On `tailrec`: Your version isn't tail recursive because the recursive call
    isn't the last operation--it's wrapped in &&. To make it tail-recursive:
 */
tailrec fun isPalindrome3(str: String): Boolean =
    when {
        str.length <= 1 -> true
        str.first() != str.last() -> false
        else -> isPalindrome3(str.drop(1).dropLast(1))
    }

// Or a totaly different approach:
fun isPalindrome4(str: String) = str == str.reversed()
    /*
        One-liner, though O(n) extra spaced for reversed string. Your recursive
        approach is arguably more elegant from a Haskell perspective.
     */

// ------ ------ ------ ------ ------ ------ ------ ------ ------ ------
// LISTS
// ------ ------ ------ ------ ------ ------ ------ ------ ------ ------

/*
    - Lists can be created with the listOf method
    - List types are defined as List<T>
    - Access elements with [index] or get()
    - What would be the type of points?
        `List<Pair<Int, Int>>`
        The `to` keyword creates a `Pair`, so `1 to 3` is `Pair(1, 3)`. A list
        of those pairs gives you `List<Pair<Int, Int>>`
 */

val nums: List<Int> = listOf(1, 2, 3, 4, 5, 6)

val points =
    listOf(
        1 to 3,
        2 to 4,
        5 to 6,
        7 to 10,
    )

// FUNCTION VARIABLES
// Write a function square in threee different ways

// declaring a conventional function
fun square(x: Int): Int = x * x

// declaring a function as an object
val square2: (Int) -> Int = fun(x: Int): Int = x * x

// declaring a function as an object with anonymous shorthand
val square3 = { x: Int -> x * x }

// LISTS WITH MAP
    /*
    Functions can be aplied to the contents to lists with `map`. Apply a square
    function to the list nums.
        - Three alternatives for passing a function
        - Function as a val? Note the function type
     */
val nums2: List<Int> = listOf(1, 2, 3, 4, 5, 6)

fun printSquare() {
    println(nums2.map(::square))
    println(nums2.map { x -> x * x })
    println(nums2.map({ x -> x * x }))
}

// LISTS WITH FILTER
    /*
        Write a function that takes an integer and a list of pairs then returns
        only the pairs that sum to the given value.
     */
fun matchingTotal(
    x: Int,
    pairs: List<Pair<Int, Int>>,
): List<Pair<Int, Int>> = pairs.filter({ pair -> pair.first + pair.second == x })

// HoFs (Higher-order Functions) and Composition
    /*
        Write a function composition that takes two functions and returns
        a function of their product. Use square functions to create a power4.
     */
fun composition(
    f: (Int) -> Int,
    g: (Int) -> Int,
): (Int) -> Int = { x -> f(g(x)) }

val power4: (Int) -> Int = composition(square2, square3)

val power8 = composition(power4, ::square)

// ------ ------ ------ ------ ------ ------ ------ ------ ------ ------

// Just writing a function here to store all notes related to loops
fun forLoop() {
    // For the range:
    var fl1 = (1..5).toList() // [1, 2, 3, 4, 5]
    println(fl1)

    // Or just use the range directly:
    for (i in 1..5) println(i)

    // Other variations
    var fl2 = (1 until 5).toList() // [1, 2, 3, 4] -- excludes end
    var fl3 = (1..10 step 2).toList() // [1, 3, 5, 7, 9]
    var fl4 = (5 downTo 1).toList() // [5, 4, 3, 2, 1]

    // On `to`:
    // Yes, it just creates a `Pair`. That's literally all it does:
    val p = 1 to 3 // Pair(1, 3)
    println(p.first) // 1
    println(p.second) // 3

    /*
        It's syntactic sugar--`1 to 3` is equivalent to `1.to(3)` which returns
        `Pair(1, 3)`. It's commonly used for maps:
     */
    val map = mapOf("ac" to 1, 'b' to 2, 'c' to 3)
    println(map)
}

// `::` is the FUNCTION REFERENCE operator. It lets you refer to a function
// without calling it.
fun cube(x: Int): Int = x * x * x

// These are equivalent
/*
nums.map { x -> square(x) }     // lambda that calls square
nums.map(::square)              // references to square directly
 */

// So `::square` means "the function itself" rather than "call square now."

// different uses:
/*                      // try run these in main() if needed

// Reference a top-level function
::square                // refers to fun square(...)

// References a class method
"hello"::length         // refers to length of that specific string

// Reference a constructor
::Person                // refers to Peson constructor

// Reference a member function
String::length  `       // refers to length on any String
 */

/*
        Analogy to Haskell:
        - It's similar to how in Haskell you can write `map square [1, 2, 3]`
          passing `square` directly. In Kotlin you need the `::` to disambiguate
          that you mean the function reference, not a function call.


The point is that `::` captures a function reference that you can store, pass
around, or call later. It's lke grabbing a handle to the function itself.

val f = "hello"::reversed           // grab reference
println(f())                        // call via reference

The reference approach is useful when you want to pass the function to something
like `,map`, `filter`, etc.
 */

// ------ ------ ------ ------ ------ ------ ------ ------ ------ ------
// EXERCISES 3
// ------ ------ ------ ------ ------ ------ ------ ------ ------ ------
// HIGHER-ORDER FUNCTIONS IN KOTLIN

/*
    Kotlin includes a lot of powerful features from functional languages like
    Haskell. One of the main featuyres that we think of as being characeristic
    of a functional language is the ability to use higher-order functions.
 */

// Syntax reminder:
    /*
    Here's a reminder of two different ways of passing a function as an argument
    to another function, either by function reference (double-colon), or by
    passing a lambda - an anonymous function declared in braces.
     */
fun square4(x: Int): Int = x * x

/*
fun main() {
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8)
        // or `val numbers = (1..8).toList()`

    // passing a function reference
    val squares5 = numbers.map(::square4)

    // passing a lambda
    val squares6 = numbers.map { x: Int-> x * x}
        // or val squares6 = numbers.map { x -> x * x}
}
 */

// Write a function `lengths()` that takes a list of strings and returns a
// list of their lengths
fun lengths(xs: List<String>): List<Int> = xs.map({ s -> s.length })

// Write a function `complements()` that takes a list of integers, and returns
// a list of pairs, where each pair contains the input number as the first
// element, and the total of the numbers in the pair makes 10.
fun complements(xs: List<Int>): List<Pair<Int, Int>> = xs.map { s -> Pair(s, (10 - s)) }
// xs.map({ s -> s to (10 - s) })       // or alternatively

// Write a function `matchingTotal(x, pairs)` that takes a total, and list of
// pairs of integers, returning a list containing only those pairs where the
// sum of the elements of the pair matches the given total. If the x = 10, then
// (4, 6) would be included, but (6, 6) would be excluded.
val pairs = listOf(Pair(1, 9), Pair(3, 4), Pair(5, 5))

fun matchingTotal2(
    x: Int,
    xs: List<Pair<Int, Int>>,
): List<Pair<Int, Int>> = xs.filter { s -> if (s.first + s.second == x) true else false }

// ------ ------ ------ ------ ------ ------ ------ ------ ------ ------

/*
    - What is a return type Unit?

        `Unit` is Kotlin's equivalent of `void` in Java/C--it means "this
        function doesn't return anything meaningful."
 */
fun greet(name: String): Unit = println("Hello $name!")

// Same thing--Unit is implicit when omitted:
fun greet2(name: String) = println("Hello $name!")

/*
        The difference from `void`: `Unit` is an actual type with a single
        value (also called `Unit`). This makes Kotlin's type system more
        consistent--every function returns something, even if that something is
        just `Unit`.
 */
val result: Unit = println("hi") // result is Unit

fun hello(name: String): String = "Welcome Back! $name!"

/*
    - Projects--the infrastructure to manage to build lifecycle
        - Can have multiple packages and sub-packages for scope and software
          management
    - Modules - src folders for compilation
        - Packages
        - Main - source folder
        - Test - test folder
    - Sources folder identified for compilation
        - Best to follow the mvn (Maven) default project structures
    - Check gradle, IntelliJ and mvn project setups
        - You will use gradle for LabTS and most projects
 */

/*
    TESTS

        - Based on the Java junittest framework
        - Usually replicates source files and classes with test files and
          classes
            - Append 'Test' to the class name
        - `test method identifiers can be sentences in backticks`

        - Manual interrogation of your code during runtime can be done with
          debugging.
 */

// ------ ------ ------ ------ ------ ------ ------ ------ ------ ------
// STYLING AND FORMATTING

/*
         ```$ ktlint -F ./src/*```

         ------ ------ ------ ------ ------ ------ ------ ------ ------ ------
            Summary error count (descending) by rule:
              standard:no-empty-file: 2
              Not a valid Kotlin file: 1
              standard:filename: 1
              standard:import-ordering: 1
         ------ ------ ------ ------ ------ ------ ------ ------ ------ ------



         ```$ ktlint -F "./Nov-24 [Lecture-2].kt"```

         ------ ------ ------ ------ ------ ------ ------ ------ ------ ------
            Summary error count (descending) by rule:
              standard:no-consecutive-comments: 47
              standard:filename: 2
              standard:if-else-wrapping: 1
         ------ ------ ------ ------ ------ ------ ------ ------ ------ ------
 */*/

// ------ ------ ------ ------ ------ ------ ------ ------ ------ ------
// LECTURE REVIEW

/*
Functional programming with Kotlin
- Pairs and Lists
- Map and Filter
- Function as Objects and HOFs (Higher-order Functions)

Building Kotlin projects
- Project Structures
- Package Structures
- Build lifecycle
- Ktlint              ``` ktlint -F ./src/*``` and ```ktlint -F src``` works too
- Testing frameworks
 */*/

// ------ ------ ------ ------ ------ ------ ------ ------ ------ ------

fun main() {
    println(hello("Sienar Heavy-Industries"))

    println("123456".dropLast(3))

    println(::square) // function square (Kotlin reflection is not available)
    println("hello"::length) // property length (Kotlin reflection is not available)

    // printSquare()
    // println(distanceBetween(Pair(3, 2), Pair(5, 20)))

    // println(5.squared())
    // println(5.squared())
}


// ------ ------ ------ ------ ------ ------ ------ ------ ------ ------
// LISTS AND FUNCTION COMPOSITION
    // Write a function composition that takes a string and returns the number
    // of bits required to store the string using different encoders.


// An additional contained example of composition that is closer to the
// requirement for the PPT exercise this week
val strsList = listOf("quick", "brown", "fox", "lazy")

val strLength: (String)->Int   = { s: String -> s.length }
val bitsAscii: (Int)->Int      = { x: Int    -> x * 8 }
val bitsMattSecure: (Int)->Int = { x: Int    -> x * 8 + 8 }

// Example function composition
fun strBits(f: (Int)->Int): (String)->Int =
    { str: String -> f( strLength( str )) }

// Demonstrating applying the function composition to a list of numbers
// strList.map { x -> strBits(bitsAscii)(x) }       // converts each of list's string value, like "quick"; then
                                                    // retrieve it's length using strLength inside strBits.
                                                    // Finally. bitsAscii: (Int)->Int converts length of "quick", i.e.
                                                    // 5 to how many bits (space) does 5x `Char`s take...
// strList.map { x -> strBits(bitsMattSecure)(x) }  // Similar, but uses secure bit instead


// Example adding the encoder functions to a list of functions before embedding
// the list map in the higher list map
val encoders: List<(String)->Int> =
    listOf(strBits(bitsAscii), strBits(bitsMattSecure))

val smart = encoders.map { f -> strsList.map { x -> f(x) }} // an elegant way to cycle through different means
                                                            // of encoding