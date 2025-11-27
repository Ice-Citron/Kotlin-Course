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
val (x1, y1) = p7    // writing x1 instead of x because ran out of names. lol



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






























