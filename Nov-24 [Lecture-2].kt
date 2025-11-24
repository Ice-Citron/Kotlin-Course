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
import kotlin.math.sqrt
import kotlin.math.pow

fun distanceBetween(a: Pair<Int, Int>, b: Pair<Int, Int>): Double {
    val xDiff = a.first - b.first
    val yDiff = a.second - b.second
    return sqrt((xDiff*xDiff + yDiff*yDiff).toDouble())
}

/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */

// Or you can define extension functions for cleaner syntax:
fun Int.squared() = this * this
fun Int.cubed()   = this * this * this

// If you're working with ```Double```m ```import kotlin.math.pow``` works.
// For Integers, where you want an integer results, multiplication is cleaner
// than ```Math.pow(x.toDouble(), 2.0).toInt()```.

/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */

/*
- On `this`: Yes, `Int` is a class, and `this` refers to the instance the method
  is called on. When you write `5.squared(), `this` is `5`. And yes,
  `this * this` works because `Int` has an operator functioned defined for `*`
  ---something like `operator fun times(other: Int): Int`. So `this * this` is
  really `this.times(this)`.

- On type inference: Kotlin can infer return types for single-expression
  functions (the `= ...` form) So these are equivalent:
 */
fun Int.squared2(): Int = this * this   // explicit return type
fun Int.squared3()      = this * this   // inferred return type

// The compiler sees `Int * Int` and knows the result is `Int`


// However, for block-body functions (with `{}`), you must declare the return
// type
fun Int.squared4(): Int {
    return this * this
}

/*
    So it's not quite like Haskell where types are always optional---it depends
    on the function form. The rule is: expression body can infer, block body
    cannot.

    Also, parameter types are never inferred---you always have to write those
    explcitly:
 */
fun add(x: Int, y: Int): Int = x + y    // can't omit the `: Int` on parameters

/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */
// Encapuslation

/*
    Encapsulation is about controlling access to a class's internals---hiding
    implementation details and exposing only what's necessary.

# Visibility Modifiers

    Kotlin has four visibility levels:
 */

class BankAccount(private val accountNumber: String) {
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
    private var balance: Double = 0.0;

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

/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */
// ENCAPUSLATION2
/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */

// Properties with custom accessors
    // Kotlin has a cleaner pattern using property getters/setters:
class Temperature {
    var celsius: Double = 0.0
        set(value) {
            if (value >= -273.15) { field = value }
                // 'field' is the backing field
        }

    val fahrenheit: Double
        get() = celsius * 9/5 + 32
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
class Counter{
    var count: Int = 0
        private set

    fun increment() { count++ }
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

/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */
// EXERCISE
/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */

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
val str:String = "hello world"

// constructing a pair
val p:Pair<Int, String> = Pair(3, "C")

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
    if (str[0] == str[length-1])
        return true && isPalindrome(str.substring(1, length-1))
    else
        return false
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
tailrec fun isPalindrome3(str: String): Boolean = when {
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


/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */

fun main() {
    println("123456".dropLast(3))
    // println(distanceBetween(Pair(3, 2), Pair(5, 20)))

    // println(5.squared())
    // println(5.squared())
}