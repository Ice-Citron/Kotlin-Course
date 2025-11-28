import javax.swing.text.rtf.RTFEditorKit

// Intended Learning Outcomes
/*
    - Types
      - Objects
      - Type Aliases
    - Classes
      - Instance Objects
      - Constructors
      - Attributes
    - Methods
      - Inheritance
      - Override
 */

/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// HYBRID: FUNCTIONAL AND OBJECT-ORIENTED

/*
    - Modelling things in the real world
    - Purposeful naming and structure in the code - representation
 */

typealias HttpHandler = (Request) -> Response

class Request(
    val url: String,
    val authToken: String = ""
)

class Response (
    val status: Status,
    val body: String = ""
)

enum class Status (
    val code: Int
) {
    OK(200),
    FORBIDDEN(403),
    NOT_FOUND(404)
}


// Request argument used for downstream app functionality

fun docHandler(request: Request): Response =
    Response(Status.OK, "This is Doc.")

fun helloHandler(request: Request): Response =
    Response(Status.OK, "Hello World!")

fun homePageHandler(request: Request): Response =
    Response(Status.NOT_FOUND, "No link found :(")

fun path(url: String): String = url

fun route(request: Request): Response =
    when {
        path(request.url) == "/say-hello" -> helloHandler(request)
        path(request.url) == "/" -> homePageHandler(request)
        path(request.url) == "" -> homePageHandler(request)
        path(request.url) == "/computing" -> helloHandler(request) // normally `-> docHandler(request)`
        else -> Response(Status.NOT_FOUND, "404")
    }

/*
    `typealias HttpHandler = (Request) -> Response`

    This creates a type alias called `HttpHandler` for any function that:
    - Takes a `Request` object as input
    - Returns a `Response` object

    So instead of writing `(Request) -> Response` everywhere, you can just
    write `HttpHandler`. For exmaple:
    ```Kotlin
    val myHandler: HttpHandler = { request -> Response("Hello") }
                                   // do something with request

    It's just a cleaner way to refer to functions with that specific signature.
    ```
 */


/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// TYPEALIAS

/*
    - Giving a new name to existing type to provide meaning
    - Define a type alias for point that is represented by a pair
 */

typealias Point = Pair<Int, Int>

fun distanceBetweenPoints(p1: Point, p2: Point): Double =
    distanceBetween(p1, p2)


// CLASSES

/*
    - Provide a template to create instances
    - Write a Circle class that has position `x, y` and `radius`
            `class Circle(x: Int, y: Int, radius: Int)`
    - Create two instances of a circle
            ```Kotlin
            val c1 = Circle(2, 3, 4)
            val c2 = Circle(3, 4, 5)
            ```
 */


// CLASS CONSTRUCTORS

/*
    - Calculate the distance between circles c1 and c2
        - distanceBetween(Pair(c1.x, c1.y), Pair(c2.x, c2.y))
        - Add properties to the class

        ```Kotlin
        class Circ(val x: Int, val y: Int, val radius: Int)
        class Circ2(val pos: Point, val radius: Int)

        class Circle(x: Int, y: Int, radius: Int) {
            val x = x
            val y = y
            val radius = radius
        }


        val c1 = Circ2(Point(2, 3), 4)
        val c2 = Circ2(Point(3, 4), 5)

        distanceBetweenPoints(c1.pos, c2.pos)
        ```
 */

/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */

// `filter`
    // keeps elements that match a condition

val numbers = listOf<Int>(1, 2, 3, 4, 5, 6)
val evens = numbers.filter { x -> (x % 2 == 0)}     // result [2, 4, 6]
val bigNumbers = numbers.filter { x -> (x > 4)}     // result [5, 6]


// `map`
    // transforms each element
val triple = numbers.map { x -> x * 3}
val asStrings = numbers.map { x -> "Numbers: $x" }


// Chaining them together
val res = numbers.filter {x -> (x % 2 == 0)}
                 .map {x -> {"Numbers supremo $x"}}
// interestingly, the output of this is [Function0<java.lang.String>, Function0<java.lang.String>,
//                                       Function0<java.lang.String>]


/*
fun main() {
    println(evens)
    println(bigNumbers.toString() + "\n")
    println("triple: " + triple) //.toString())
    println("Converted stuff" + asStrings.toString() + "\n")
    println(res.toString())
}
 */

/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// EXERCISE
/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */

class Person(name: String, age: Int, addressPerson: AddressPerson) {
    val name = name
    val age = age
    val addressPerson = addressPerson
}

class AddressPerson(street: String, city: String) {
    val street = street
    val city = city
}

fun constraintCity(personList: List<Person>, city: String): List<Person> {
    return personList.filter { x -> x.addressPerson.city == city }
}

/*
fun main() {
    var person1: Person = Person("Shi Hao", 20,
                        AddressPerson("South Kensington", "London"))
    var person2: Person = Person("Yumin Lee", 19,
        AddressPerson("St. Albert", "Melbourne"))
    var person3: Person = Person("Yusei Saeki", 19,
        AddressPerson("South Kensington", "London"))

    var personList: List<Person> = listOf(person1, person2, person3)
    var filteredList: List<Person> = constraintCity(personList, "London")

    for (i in filteredList) { println(i.name) }
}
 */


/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// TYPEALIAS
/*
    A typealias lets you create a new name for an existing type. It doesn't
    create a new type--it's purely for making your code more readable.

    Think of it like a nickname. If your friend's name is "Alexander", you
    might call him "Alex." Same person, different name. Similarly,
    `typealias Point = Pair<Int, Int>` means "whenever I say Point, I mean
    Pair<Int, Int>".

    It's especially useful for complex types that you use repeatedly:
 */

// This is tedious to write repeatedly
var a: Map<String, List<Pair<Int, Int>>> = mapOf()

// Give it a name
typealias PointHistory = List<Pair<Int, Int>>


/*
    One important thing: because it's just an alias, not a new type, Kotlin
    won't stop you from mixing them up:
 */
typealias Age = Int
typealias Height = Int

val age: Age = 25
val height: Height = 180

val sum = age + height    // Kotlin allows this--both are just Int

/*
    If you want actual type safety (preventing mixing), you'd need a data class
    instead.
 */

/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// CLASSES - THE BASICS

/*
    A class is a single blueprint for creating objects. Think of it like a
    cookie cutter--the class defines the shape, and each object (instance) is
    a cookie made from that cutter.

    ```class Circle(x: Int, y: Int, z: Int)```

    This defines a Circle that takes three integer to construct. But here's a
    gotcha--those parameters only exist during construction:

    ```
    val c = Circle(1, 2, 3)
    // c.x      // ERROR! x doesn't exist as a property
    ```

    Wait, what? I passed in x, y, radius... where did they go?

    They were used ot construct the object, but weren't stored. It's like giving
    someone ingredients to make a cake, but they threw away the recipe card
    after. The cake exists, but you can't ask "what ingredients did you use?"
 */

/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
/*
    There are two ways to interpret "check for types": either you want to print
    out what type a variable is (for debugging), or you want to write logic
    that behaves differently based on the type (conditionals).

    Here is how to do both inside the `main` function.
 */

/*
    1. How to print the type name (Reflection)

        If you want to see what a variable is at runtime, use the `::class`
        reference.
 */

/*
fun main() {
    val name = "Kotlin"
    val age = 5
    val isFun = true
    val complexList = listOf(1, "Two", 3.0)     // List<Any>

    // Use ::class.simpleName for the clean Kotlin type
    println(name::class.simpleName)     // Output: String
    println(age::class.simpleName)      // Output: Int
    println(complexList::class.simpleName)  // Output: ArrayList

    // Use ::class.qualifiedName for the full package path
    println(name::class.qualifiedName)
}
 */


/*
    2. How to check type in logic (`is` keyword)

        If you want to perform actions based on the type, use the `is` keyword.
        This is the equivalent of Java's `instanceof`.

        The "Smart Cast" Feature: The best part about Kotlin is that once you
        check the type, Kotlin automatically converts it for you inside that
        block. You don't need to cast it manually.
 */

/*
fun main() {
    val unknownData: Any = "I am actually a String"

    if (unknownData is String) {
        // Kotlin knows `unknownData` is a String here!
        // We can access String methods like `.length` immediately.
        println("It is a string of length: ${unknownData.length}")
    }
    else if (unknownData is Int) {
        println("It is a number: ${unknownData.plus(10)}")
    }
    else {
        println("Unknown type")
    }
}
 */

/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// Kotlin's best features --> PRIMARY CONSTRUCTOR PROPERTIES

/*
    1. The "Verbose" Way (Java Style)
 */
class User(x: Int) {    // 1. Define a constructor parameter named 'x'
    var x: Int = x      // 2. Define a property named 'x' and assign the
}                       //    parameter to it
/*
        In this version, `x` in the parantheses is just a parameter (like an
        argument passed to a function). It dies after the class is initialized.
            - It is NOT visible to other functions in the class.
            - You have to manually save it into a variable (`var x`) if you
              want to keep it.
 */


/*
    2. The "Kotlin" Way (The Shortcut)
 */
class User2(var x: Int) // Defines parameter AND property AND assigns it
/*
        When you add `var` (or `val`) inside the parentheses, you are telling
        Kotlin:
            "I am too lazy to type this twice. Please take the parameter `x`,
            and turn it into a property named `x`."
        This makes `x` accessible everywhere inside the class immediately.

 */


/*
    3. When should you ever use the "Long" way?

        You only use the verbose version if you need to CHANGE or VALIDATE the
        data before saving it.

        EXAMPLE: PREVENTING NEGATIVE NUMBERS
        If you use the shortcut (var x: Int), whatever number is passed in is
        saved immediately. If you want to stop that, you do it the long way":
 */
class User3(x: Int) {
    // Logic: If they pass a negative number, save 0 instead
    var x: Int = if (x > 0) x else 0
}


// Use `val` by default. Only use `var` when you genuinely need to change the
// value later.

/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// CLASSES -- ADDING BEHAVIOUR

/*
    Classes can have methods (functions that belong to the class):
 */
class Circle2(val x: Int, val y: Int, val radius: Int) {
    fun area(): Double = Math.PI * radius * radius

    fun contains(px: Int, py: Int): Boolean {
        val dx = px - x
        val dy = py - y
        return (dx*dx + dy*dy <= radius*radius)
    }
}

/*
    Inside a method, you can access the class's properties directly. When I
    write `radius`, Kotlin knows I mean `this.radius`--the radius of this
    particular circle.
 */

/*
    Each instance has its own `x`, `y`, `radius`, so `area()` returns different
    results for different circles.
 */

/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// ENUM CLASSES

/*
    An enum (enumeration) defines a fixed set of possible values. Use it when
    something can only be one of a specific list of options.
 */
enum class Status2(val code: Int) {
    OK(200),
    FORBIDDEN(403),
    NOT_FOUND(404)
}

/*
fun main() {
    /*
        This says: a `Status` can only be `OK`, `FORBIDDEN` or `NOT_FOUND`.
        Nothing else. Each has an assosciated HTTP code.
     */
        val s = Status2.OK
    // println(s.code)   // 200


    /*
        Why not just use integers or strings? Type safety:
     */
    // With raw integers--easy to mess up
    fun handleResponse(code: Int) {}
    handleResponse(999)     // Compiles, but 999 isn't a real status

    // With enum-compiler catches mistake
    fun handleResponse2(status: Status2) {}
    handleResponse2(Status2.OK)   // Only valid statuses allowed

    /*
        Enums work beautifully with `when`, and Kotlin checks you'e covered all
        cases:
     */
    fun describe(status: Status2): String = when(status) {
        Status2.OK -> "Success"
        Status2.FORBIDDEN -> "No access"
        Status2.NOT_FOUND -> "Missing"
    }       //  If you forget one, Kotlin warns you!
}
 */

/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */

/*
    You are correct: `OK` is not an attribute (property) of a generic `Status`
    object.

    The reason you have to access it like `Status.OK` (which looks like a static
    property) is because Enums are just a fancy list of pre-created, static
    objects.

    Here is the mental shift to make it click.
 */

/*
    1. The "Secret" Translation

        When you write an `enum class`, the compiler actually generates a
        standard class for you.

        If we were to write the `Status` enum manually without the `enum`
        keyword, it would look roughly like this:
 */

// This is effectively what an Enum is:
class Status3(val code: Int) {
    // These are static, pre-made OBJECTS of this class
    companion object {
        val OK = Status3(200)
        val FORBIDDEN = Status3(403)
        val NOT_FOUND = Status3(404)
    }
}

/*
    Do you se it now?
        - `Status` is the Factory (the Class).
        - `OK` is a Finished Product (an Instance/Object) sitting on the factory
          shelf, waiting for you to pick it up.

    You call `Status.OK` because you are asking the Class (`Status`) to hand you
    the specific pre-made instance named `OK`.
 */


/*
    2. Can you avoid the "Dot" notation?

        You asked if `Status.OK` is the only way to call them.

        Technically, yes, you must reference the object. However, you can use
        an import to make it look less like an attribute access and more like a
        global constant.

        If you add this import to the top of your file:

    ```
    // The * allows you to see all enum constants directly
    import com.yourpackage.Status.*
    ```

        You can then write cod elike this:

    ```
    fun main() {
        // No "Status." prefix needed anymore!
        val currentStatus = OK

        if (currentStatus == FORBIDDEN) {
            println("Stop right there!")
        }
    }
    ```

    Summary:
        It feels like an attribute because, syntactically, it is stored as a
        static constant inside the class.

        But logically, Status.OK is a full-blown object instance, just like
        `val s = Status(200)` would be (if you were allowed to create them
        manually). The Enum just forces you to use the specific instances
        they created for you.
 */


/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// Combining Concepts

// Typealias for clarity
typealias Point2 = Pair<Int, Int>

// Enum for fixed set of states
enum class Color {
    RED,
    GREEN,
    BLUE
}

// Class combining everything
class Circle3(val center: Point, val radius: Int, val color: Color) {

    // Computed properties-derived from other properties
    val x: Int get() = center.first
    val y: Int get() = center.second
        /*
            `get()`

            The Live Feed (Custom Getter)
            ```
            // This runs EVERY TIME you use .x
            val x: Int get() = center.first
            ```
                - What happens: This doesn't store a number. It stores a formula
                - The Logic: "Hey, whenever someone asks for `x`, go look at
                    `center` right now and grab the first value."
                - The Benefit: It's always up-to-date... Whenever `val center`
                    changes, `val x` hence changes too.
         */

    fun overlaps(other: Circle3): Boolean {
        val dx = other.x - this.x
        val dy = other.y - this.y
        val distance = Math.sqrt((dx*dx + dy*dy).toDouble())
        return distance < radius + other.radius
    }
}


/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// DATA CLASSES
/*
    A regular class in Kotlin is just a container--it doesn't come with much
    built-in funcionality. If you want to compare two objects or print them
    nicely, yoy have to write that yourself:
 */
class Person4(val name: String, val age: Int)

val p1 = Person4("Alice", 25)
val p2 = Person4("Alice", 25)

/*
    `p1 == p2` is false. Because by default `==` checks if they're the same
    object in memory, not if they contain the same values. They're two separate
    objects, so they're not equal.

    To fix this, you'd need o manually override `equals()`, `hashCode()`.
    and `toString()`.
 */


/*
    Data Classes do this automatically
 */
data class Person5(val name: String, val age: Int)

val p3 = Person5("Alice", 25)
val p4 = Person5("Alice", 25)

/*
fun main() {
    println(p1)         // Person@3a8998ab - not helpful
    println(p1 == p2)   // false - even though they have the same data!

    println(p3)         // Person5(name=Alice, age=25) - nice!
    println(p3 == p4)   // true - compares by content now
}
 */


/*
    Just adding `data` in front gives you:
        - `toString()` that prints all properties nicely
        - `equals()` that compares by content
        - `hashCode()` that's consistent with equals (important for using in
          maps/sets)
        - `copy()` for creating modified copies
        - `componentN()` functions for destructuring
 */

/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// WHEN TO USE DATA CLASSES?

/*
    Use `data class` when the class is primarily a container for data--like a
    record or struct. Think: DTOs, API responses, coordinates, configurations.
 */
/*
    Data Transfer Objects (DTOs) are all about how this data is represented. If
    you know C then a DTO is a struct. If you don't know C then a DTO is an
    object without methods, it is pure data. Data Transfer Objects have only
    public fields, and these fields may have one of a limited set of types.
 */

// Good candidates for data class
data class Point3(val x: Int, val y: Int)
data class User4(val id: Int, val name: String, val email: String)
data class HttpResponse2(val status: Int, val body: String)

// Not a good candidate--has complex behavior, not just data
class BankAccount4(val number: String) {
    private var balance: Double = 0.0
    fun deposit(amount: Double): Unit {}
    fun withdraw(amount:Double): Unit {}
}

/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// THE `COPY()` FUNCTION

/*
    Here's the problem: if you make your properties `val` (immutable), how do
    you "change" something?
 */
data class Person7(val name: String, val age: Int)

val alice = Person7("Alice", 25)
// alice.age = 26       //  ERROR! age is val, can't modify


/*
    You don't mutate--you create a new object with the changed value. The
    `copy()` function makes this easy:
 */
val alice2 = Person7("Alice", 25)
val olderAlice = alice2.copy(age = 26)

/*
fun main() {
    println(alice2)         // Person7(name=Alice, age=25)
    println(olderAlice)     // Person7(name=Alice, age=26)
}
 */


/*
    You can specify what you want to change. Everything else is copied over:
 */

data class Config(
    val host: String,
    val port: Int,
    val debug: Boolean,
    val timeout: Int
)

val defaultConfig = Config("localhost", 8080,
                            false, 30)

// Just change one thing
val debugConfig = defaultConfig.copy(debug=true)

// Change multiple things
val prodConfig = defaultConfig.copy(
    host = "api.example.com",
    timeout = 60
)

/*
    This pattern--immutable data with copy--is fundamental to functional
    programming. Instead of mutating state, you create new state. It makes
    code easier to reason about because values never change unexpectedly.
 */


/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// DIFFERENCE BETWEEN `VAL` AND `VAR` IN KOTLIN

/*
    Kotlin uses two different keywords to declare variables: val and var. Use
    `val` for a variable whose value never changes. You can't reassign a value
    to a variable that was declared using `val`. Use `var` for a varianble
    whose value can change.
 */


/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ---- */
// DESTRUCTURING
/*
    Data classes automatically generate `component(1)`, `component(2)`, etc.
 */

data class Point7(val x: Int, val y: Int)

val p7 = Point7(3, 4)

// Instead of:
val x = p7.x
val y = p7.y

// You can write:
/*
fun main() {
    val (x1, y1) = p7   // writing x1 instead of x because ran out of names. lol
                        // also seems that destructuring is only possible in
                        // main function... because it's deemed as a local var

    // useful in loops:
    val points = listOf(Point(1, 2), Point(2, 3), Point(3, 4))

    for ((x, y) in points) {
        println("x=${x}, y=${y}")
    }


    // and with maps:
    val scores = mapOf("Alice" to 95, "Bob" to 87) // creates pairs

    for ((name, score) in scores) {
        println("${name} scored ${score}.")
    }
}
 */

// ABOUT COMPONENT100() IN `data class`
/*
    Summary

    - Data Classes: Kotlin auto-generates `componentN` functions for every
      single property in the primary constructor (even if there are 100).

        Yes, the Kotlin compiler will silently generate `component1()` all the
        way up to `component100()` for you in the background. You could
        technically write:

            ```val (val1, val2, ... val100) = monsterObject```

        However, *please never do this*. If you are destructuring more than 3 or
        4 variables, your code becomes unreadable because usage relies on
        position, not name... never spot the bug if miss up variable #45 and #46

    - Regular Classes: You have to write `operator fun componentN()` for every
      single property you want to be accessible via destructuring. <-- NOT
      AUTO GENERATED

        Pro Tip: If you have an object with 5 properties but you only care
        about the first two, you don't need to write variables for the rest.
        You can ignore them, or use `_` for ones you want to skip in the middle.

            ```val (x, y, _) = point3D``` (Ignores `z`)
 */


/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ---- */
// C-Style Macros in Kotlin

/*
    Kotlin doesn't have C-style preprocesser macros (`#define`). This is
    intentional--macros can make code hard to understand and debug.

    But Kotlin has alternatives for the common use cases:
 */

// 1. Constants -- use `const val`

/*
    In C:
        ```
        #define MAX_SIZE 100
        #define PI       3.14159
        ```

    In Kotlin:
 */
const val MAX_SIZE = 100
const val PI       = 3.14159


/*
    `const val` is a compile-time constant. It must be a primitive or String,
    and must be defined at the top level or in an object:
 */
// Top-level
const val APP_NAME = "MyApp"

// or in an object
object Config2 {
    const val MAX_RETRIES = 3
    const val TIMEOUT_MS = 5000
}

/*
fun main() {
    println("${Config2.MAX_RETRIES} || ${Config2.TIMEOUT_MS}")
}
 */


// 2. INLINE FUNCTIONS - FOR MACRO-LIKE CODE SUBSTITUTION
/*
    In C, macros can include code:
        ```
        #define SQUARE(x) ((x) * (x))
        ```

    In Kotlin, use `inline fun`:
 */
const val DEBUG_MODE = true

inline fun debug(message: () -> String) {
    if (DEBUG_MODE) println("[DEBUG]: ${message()}")
}

/*
fun main() {
    debug { "Current value: ${x}" } // lambda not even created if DEBUG_MODE is
                                    // false
}
 */


// 3. Type aliases -- for macro-like type shorthand
/*
    In C:
        ```
        #define uint unsigned int
        ```

    In Kotlin:
 */
typealias uint = UInt


// 4. COMPILE-TIME CONDITIONS -- LIMITED SUPPORT
/*
    C has `#ifdef` for conditional compilation. Kotlin doesn't have direct
    equivalents, but you can use:
 */

// Build variants (in Android/Gradle)
/*
fun main() {
    if (BuildConfig.DEBUG) {
        // debug-only code
    }

    // Or platform checks in multiplatform
    // expect fun platformName(): String       // defined differently per platform
}
 */



/*
    Summary
    - `data class`
        - Auto-generates equals, hashCode, toString, copy, destructuring
    - `copy()`
        - Creates new instance with some fields changed (immutability pattern)
    - `const val`
        - Compile-time constants (replaces `#define` for values)
    - `inline fun`
        - Inlines function body at call site (replaces #define for code)
 */


/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// Kotlin actually does have multiple constructors and visibility control--it
// just looks different from C++.


// PRIMARY CONSTRUCTOR
/*
    The one in the class header is the primary constructor:
 */
class Human(val name: String, val age: Int)

/*
    This is just shorthand. In C++ terms you'd write
        ```cpp
        class Person {
        public:
            string name;
            int age;

            Person(string name, int age) : name(name), age(age) {}
        };
        ```

    Kotlin collapses all that into one line.
 */


// SECONDARY CONSTRUCTORS
/*
    You can have additional constructors using the `constructor` keyword:
 */
class Human2(val name: String, val age: Int) {      // Primary Constructor

    // Secondary constructor-must call primary constructor
    constructor(name: String) : this(name, 0) {
        println("[LOG]: Created person with unknown age.")
    }

    constructor() : this("UNKNOWN", 0) {
        println("[LOG]: Created anonymous person")
    }
}

val h1 = Human("Alice", 25)
val h2 = Human2("Alice")
val h3 = Human2()

/*
    The rule: secondary constructors must eventually call the primary
    constructor (via `this(...)`). This ensures the primary constructor's logic
    always runs.
 */


// INIT BLOCKS
/*
    For compelx initialization logic, use `init`:
 */
class HumanL(val name: String, val age: Int) {

    init {
        require(age >= 0) { "Age can't be negative" }
    }

    init {
        // Can have multiple init blocks-run in order
        println("[LOG]: Another init block")
        println("[LOG]: `HumanL` created with name=\"${this.name}\" and " +
                        "age=${this.age}.")
    }
}
/*
    Init blocks run after the primary constructor, in the order they appear.
 */


/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// VISBILITY ON CONSTRUCTORS

/*
    You can make constructors private, protected, etc.:
 */
// Private constructors--can't instaniate from outside
class Singleton private constructor(val value: Int) {
    companion object {
        val instance = Singleton(42)
    }
}

// Singleton.instance           // works
// Singleton(10)                // ERROR! Constructor is Private


// Protected constructor--only subclasses can call it
open class Base protected constructor(val x: Int)

class Derived : Base(10)     // OK-subclass can all it
// val b = Base(5)              // ERROR! Can't call from outside.


/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// DEFAULT PARAMETERS

/*
    In Kotlin, default parameters handle this:
 */
class Rectangle(val width: Int = 0, val height: Int = width)

val r1: Rectangle = Rectangle()             // 0, 0
val r2: Rectangle = Rectangle(5)     // 5, 5 (square


/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// FACTORY PATTERN WITH COMPANION OBJECT
/*
    Sometimes you want named "constructors" that describe what they create. Use
    companion object functions:
 */
class Color2 private constructor(val r: Int, val g: Int, val b: Int) {

    companion object {
        fun fromRGB(r: Int, g: Int, b: Int): Color2 = Color2(r, g, b)

        fun fromHex(hex: String): Color2 {
            val r = hex.substring(0, 2).toInt(16)
            val g = hex.substring(2, 4).toInt(16)
            val b = hex.substring(4, 6).toInt(16)
            return Color2(r, g, b)
        }

        // Predefined instances
        val RED   = Color2(255, 0, 0)
        val GREEN = Color2(0, 255, 0)
        val BLUE  = Color2(0, 0, 255)
    }
}

val c1 = Color2.fromRGB(100, 150, 200)
val c2 = Color2.fromHex("FF5733")
val c3 = Color2.RED
// Color2(1, 2, 3)      // ERROR! Constructor is private.

/*
    This gives you descriptive "constructors" while hiding the real one.
 */


/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// SUMMARY
/*
    Kotlin has everything C++ has, just organized differently:

        - Multiple constructor overloads
            - Default parameters + Secondary constructors
        - Initializer lists
            - Primary constructor + init blocks
        - Private/protected constructors
            - Same--`private constructor(...)`
        - Static factory methods
            - Companion object functions

    The design philosophy: reduce boilerplate. Default parameters eliminate 80%
 */


/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// EVERYTHING IS AN OBJECT

/*
    In Kotlin, there are no "primitives" like in C++ or Java. Everything is an
    object, including numbers.
 */



// THE ANY CLASS
/*
    `Any` is the root of Kotlin's class hierarchy--every class inherits from it.
    It's like `Object` in Java.

        ```kotlin
        open class Any {
            open fun equals(other: Any?): Boolean
            open fun hashCode(): Int
            open fun toString(): String
        }
        ```
 */

fun printAnything(value: Any) {
    println(value.toString())
}

// printAnything(42)
// printAnything("hello")
// printAnything(listOf(1, 2, 3))

/*
    Breakdown of the hierarchy:

    1. Hierarchy
        - `Any` (No question mark): This is the parent of every object in Kotlin
                    It can hold a String, an Int, a User, a Circle, etc. But it
                    cannot hold `null`.
        - `Any?` (With question mark): This is the parent of `Any` and `null`.
                    It is the absolute top of the Kotlin universe.

    2. Why is it used in `equals`?
        - In your screenshot, the method signature is `equals(other: Any?)`

        - This is done on purpose for safety. If you have a `User` object, you
          want to be able to ask: "Is this User equal to null?"
          - If the signature was `equals(other: Any)`: you could never check
            against null. `user.equals(null)` would be a compilation error.
          - Because the singature is `equals(other: Any?)`: you are allowed to
            pass `null` into the function. The function will just return `false`
            (instead of crashing or throwing an error).

    3. The "Box" Analogy
        - Think of variables as boxes.
            - `val x: Any` -> An infinite box that can hold any physical object,
              but the box is never allowed to be empty.
            - `val x: Any?` -> An infinite box that can hold any physical
              object, OR it can be empty.


 */


// `val` VS `var` WITH OBJECTS
/*
    For objects, `val` means you can't reassign the reference, but you can still
    mutate the object's contents.
 */



// NOTHING AND UNIT
/*
    Two special types at the botom:
 */

// Unit = "no meaningful value" (like void)
fun greet(): Unit {
    println("Hello")
}

// Nothing = "never returns" (function throws or loops forever)
fun fail(): Nothing {
    throw Exception("Failed")
}

/*
        Any
         │
    (all types)
         │
       Unit     (single value, represents "no value")
         │
      Nothing   (no values, represents "never happens")

 */

/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// OVERRIDE AND OVERLOADING

/*
    These are two different concepts that sound similar:
    - Override: Replacing a method you inherited from a parent class
    - Overload: Creating multiple versions of the same operator/method for
      different types
 */


// OVERRIDING `toString()`
/*
    Every class inherits from `Any`, which has a `toString()` method. By
    default, it prints something useless:
 */
class PointO(val x: Int, val y: Int)

val pO = PointO(3, 4)

class pointO2(val x: Int, val y: Int) {
    override fun toString(): String = "Point at [x: ${this.x}, y: ${this.y}]"
}

val pO2 = pointO2(4, 5)

/*
    The `override` keyword is required--it tells Kotlin you're intentionally
    replacing the parent's method. If you misspell the method name, Kotlin will
    error rather than silently creating a new method.
 */

/*
fun main() {

    println(pO)      // Point@3a82f6ef -- memory address, not helpful
    println(pO2)

    val hl1 = HumanL("Sienar Jaemus", 12005)
}
 */


/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// OPERATOR OVERLOADING

class PointO3(val x: Int, val y: Int) {
    operator fun plus(p: PointO3): PointO3 = PointO3(this.x + p.x,
                                                     this.y + p.y)
    operator fun minus(p: PointO3): PointO3 = PointO3(this.x - p.x,
        this.y - p.y)
    operator fun times(scale: Int): PointO3 = PointO3(this.x * scale,
        this.y * scale)
}


// DESTRUCTURING WITH `componentN()`
/*
    Destructuring lets you unpack an object into separate variables:

        ```kotlin
        val (x, y) = somePoint
        ```

    For this to work, your class needs `component1()`, `component2()`, etc.
        ```kotlin
        class Point(val x: Int, val y: Int) {
            operator fun component1(): Int = x
            operator fun component2(): Int = y
        }

        val p = Point(3, 4)
        val (a, b) = p      //  a = 3, b = 4 - calls component1(), component2()
        ```

    This is why `data class` is convenient--it generates these automatically:
        ```kotlin
        data class Point(val x: Int, val y: Int)
        // component1(), component2() are auto-generated

        val(x, y) = Point(3, 4)     // just works
        ```
 */


/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// FP vs OOP: MUTATION VS NEW OBJECTS
/*
    There are two philosophies for how methods should work:
 */


// 1. OOP style (mutate the object):
class Circle(var x: Int, var y: Int, var radius: Int) {

    fun translate(dx: Int, dy: Int) {
        this.x += dx
        this.y += dy
    }

    fun scaleBy(factor: Int) {
        radius *= factor        //  modifies this object
    }
}

/*
```kotlin
val c = Circle(0, 0, 5)
c.translate(3, 4)       // c is now at (3, 4)
c.scaleBy(2)            // c.radius is now 10
```
 */

// THE ORIGINAL OBJECT CHANGED!



// 2. FP style (return a new object):
class Circle7(val x: Int, val y: Int, val radius: Int) {

    fun translate(dx: Int, dy: Int): Circle7 {
        return Circle7(this.x+dx, this.y+dy, this.radius)
                                                        // new object
    }

    fun scaleBy(factor: Int): Circle7 {
        return Circle7(this.x, this.y, this.radius * factor)
                                                        // new object
    }
}

/*
```kotlin
val c1 = Circle(0, 0, 5)
val c2 = c1.translate(3, 4)     // c1 unchanged, c2 is new
val c3 = c2.scaleBy(2)          // c2 unchanged, c3 is new

println(c1.radius)      // still 5
println(c3.radius)      // 10
```
 */


/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
// WHY DOES THIS MATTER?

/*
    MUTABLE (OOP) APPROACH:
        - Pros: Memory efficient--no new objects created.
        - Cons: Hard to track what changed when. If you pass an object to a
                function, it might modify it unexpectedly.
 */

/*
    IMMUTABLE (FP) APPROACH:
        - Pros: Safe--objects never change unexpectedly. Easy to reason about.
        - Cons: Creates more objects (though usually this is fine)
 */

/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */

fun main() {

}

/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */
/* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */





















