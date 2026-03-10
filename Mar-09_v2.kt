import java.security.KeyStore

/*
    It is one of the most common "gotchas" in OOP! Almost everyone assumes
    `.contains()` just magically loops through the list and checks if the
    properties match.

    The realisation that a `Set` DOESN'T EVEN LOOK AT YOUR `.equals()` METHOD
    until it finds a matching `.hashCode()` is just a huge lightbulb moment.
    It's done for performance: instead of checking 10,000 items one by one
    (O(N) time), a Set uses the hash code to jump directly to the exact memory
    bucket where the item should be (O(1) time). If the hash isn't there, it
    instantly returns `false`.

    And yes, comparing `Double` values manually is usually a minefield because
    `0.1 + 0.2 == 0.3` is technically `false` in most programming languages
    due to how binary represents decimals!

    Since you just leveled hup your ...


---
    What your test cases are expecting is called VALUE EQUALITY (or Structural
    Equality).

    By default, when you create a standard `class` in Kotlin, it uses REFERENCE
    EQUALITY. This means if you create `Object A` and `Object B` with the exact
    same parameters, the computer sees them as two completely separate items
    living in two different houses (memory addresses) in your RAM. If you ask a
    `Set` if it contains `Object B`, it checks the memory addresses, says
    "Nope, i only have Object A's address," and returns `false`.

    To make the Set return `true` based only on the parameters matching, you
    have to change the rules of how the class defines "equality."

    ,,,


---
HOW `Set.contains()` ACTUALLY WORKS UNDER THE HOOD
    To make `.contains()` work for a `Set`, you must understand that a `Set`
    does a TWO-STEP VERIFICATION for performance reasons. If you only do Step 2,
    your code will fail.

        1. STEP 1: THE `hashCode()` CHECK. Instead of checking every single item
          in the set (which is slow), the Set generates an integer "ID number"
          (a hash code) based on the object's data. It looks in its internal
          filing cabinet for that ID. If it doesn't find the ID, it immediately
          returns `false` without ever looking at the parameters.
        2. STEP 2: THE `equals()` CHECK. If the Set does find the matching ID
           number in its filing cabinet, it then pulls out the object and
           compares the parameters one by one to ensure it's a true match.

    Therefore, you MUST override both methods.


---
IMPLEMENTATION METHOD 1: THE "Kotlin Cheat Code" (Best Practice)
    Kotlin was designed to solve this exact headache. If your class's primary
    job is just to hold data parameters (like `pitch` and `duration`, or `name`
    and `capacity`), you simply add the word `data` before the class definition.

```Kotlin
data class Note(val pitch: Int, val duration: Double)
```

    WHAT THIS DOES:
        The moment you type `data class`, the Kotlin compiler secretly writes
        a perfect `.equals()` and a perfect `.hashCode()` function for your
        behind the scenes. It automatically uses all the parameters in the
        primary constructor to calculate the equality and the hash. Your test
        cases will instantly pass.


---
IMPLEMENTATION METHOD 2: THE MANUAL WAY (EXAM STYLE)
    If an exam specifically tells you to write a standard `class` or you need to
    do custom logic, you have to write both methods manually. Here is the exact
    template you should...:
*/
class Note(
    val pitch: Int,
    val duration: Double,
) {

    // 1. OVERRIDE EQUALS
    //    Note: The parameter must be of type `Any?`, not `Note`.
    override fun equals(other: Any?): Boolean {
        // Check 1: Are they literally the exact same object in memory?
        //          (=== checks memory address). This is a fast shortcut to
        //          return true.
        if (this === other) return true

        // Check 2: Is the `other` object actually a Note? Or is it null?
        //          If it's a different class entirely, they can't be equal.
        if (other !is Note) return false

        // Check 3: Now we compare the actual parameter values.
        //          Because of the `is Note` check above, Kotlin "smart casts"
        //          `other` so we can access its pitch and duration.
        return this.pitch == other.pitch && this.duration == other.duration
    }

    // 2. OVERRIDE HASHCODE
    //    You must generate an Int that will ALWAYS be the same if the
    //    parameters are the same.
    override fun hashCode(): Int {
        // Start with the hash code of the first parameter
        var result = pitch.hashCode()

        // Multiply by an odd prime number (31 is the standard convention in
        // Kotlin/Java) and add the hash code of the next parameter.
        result = 31 * result + duration.hashCode()
        return result
    }
}



/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    ... breakdown of `enum class` vs `sealed class` in Kotlin.

    Think of them as two different ways to restrict your data so that the
    compiler (and the `when` )










* */





/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    In Kotlin, the `when` statement is a powerful conditional expression that
    replaces the traditional `switch` found in languages like Java. It matches
    the argument against a series of branches sequentially until a condition
    is satisfied. Unlike a Java `switch`, `when` can be used as either a
    statement (executing code) or an expression (returning a value). Each branch
    consists of a condition followed by an arrow (`->`) and the action to
    perform, providing a highly readable alternative to complex `if-else` chains.

    The versatility of `when` lies in the variety of conditions it can evaluate.
    Branches can check for specific values, constants, or expressions, and can
    even include multiple values separated by commas if they share the same
    logic. Furthermore, it can perform type checks using the `is` keyword or
    range checks using `in`. This flexibility allows developers to handle
    diverse types of logic within a single, concise block.

    When used as an expression, the `else` branch is mandatory unless the
    compiler can verify that all possible cases are covered, such as with `enum`
    classes or sealed hierarchies. This "exhaustiveness" check ensures that
    your code is safe and handles every possible input. Additionally, `when` can
    be used without an argument; in this mode, it acts like a clean `if-else if`
    chain where each branch condition is a simple Boolean expression.
* */








/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
1. THE BASIC `when` (LIKE A JAVA `switch`)
    This is the most common use case. It checks a variable against specific
    values.
* */
val command = "invert"

fun command183() {
    when (command) {
        "invert" -> {
            println("Inverting the image...")
            println("funny is it not... I can now print multiple brackets!")
        }
        "grayscale" -> println("Converting to black and white...")
        "flipH" -> println("Flipping horizontally...")
        else -> println("Unknown command!") // The `else` is like `default` in Java
    }
}


/*
2. USING `when` AS AN EXPRESSION (RETURNING A VALUE)
    Instead of just executing code, `when` can actually return a value directly
    into a variable. Notice how clean this makes the code (no need to write
    `return` on every line).
* */
val pixelIntensity = 120

val brightnessCategory = when (pixelIntensity) {
    0 -> "Pitch Black"
    255 -> "Pure White"
    else -> "Somewhere in between"
}






/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    ... think of `when` as having two distinct "modes" depending on whether you
    put something in the parentheses:

MODE 1: PATTERN MATCHING (Has an argument)
    * SYNTAX: `when (variable) { ... }`
    * WHAT IT DOES: It looks at the variable and tries to match it against
      patterns (exact values, ranges like `in 1..10`, or types like `is String`)

MODE 2: THE `if-else` REPLACEMENT (No argument)
    * SYNTAX: `when { ... }`
    * WHAT IT DOES: It just evaluates a list of boolean conditions from top to
      bottom, exactly like a chain of `if () ... else if () ... else`.


    ...
* */












/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
3. Checking Multiple Values at Once
    If multiple cases could trigger the exact same code, you can group them
    together using a comma `,`.
* */
val color = "Red"

fun color255(): Unit {
    when (color) {
        "Red", "Green", "Blue" -> println("This is a primary RGB color!")
        "Cyan", "Magenta", "Yellow" -> println("This is a secondary color.")
        else -> println("Just a regular color.")
    }
}



/*
4. CHECKING RANGES USING `in`
    You can check if a number falls within a specific range, which is super
    useful for things like image processing thresholds or grading systems.
* */
val score271: Int = 85

fun score271() {
    when (score) {
        in 90..100 -> println ("A")
        else -> println("Asian parents: Why you no doctor yet. " +
                        "Needs improvement!")
    }
}



/*
...

6. CHECKING TYPES USING `is`
    If you don't know exactly what type of object you are dealing with, `when`
    can check the type and automatically "smart cast" it for you.
* */
fun printInfo(data: Any) {
    when (data) {
        is String -> { println("It is a string of length ${data.length}") }
        is Int -> {}
        is Boolean -> {}
        else -> println("Unknown type")
    }
}













/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    ... breakdown of `enum class` vs. `sealed class` in Kotlin.

    Think of them as two different ways to restrict your data so that the
    compiler (and the `when` statement) knows exactly all the possible options
    that can ever exist.


1. ENUM CLASSES (The "Fixed Constants")
    An `enum class` is used when you have a strict, finite set of identical
    VALUES. Every item in an enum is the exact same type of thing, and they all
    have the exact same properties. They are essentially singletons.
* */
enum class TicketType(val price: Double) {
    ADULT(15.0),
    CHILD(5.0),
    SERNIOR(10.0),
}

/*
    WHEN TO USE THEM:
        * When the options are simple and never change (e.g., Days of the week,
          Compass directions, User Roles).
        * When every option has the exact same structure (e.g., every
          `TicketType` has a `price`).
        * THE LIMITATION: You cannot create new instances of `ADULT` with
          different data. `TicketType.ADULT` is a single, hardcoded object in
          memory.



2. SEALED CLASSES (The "Enum on Steroids")
    A `sealed class` is used when you have a strict, finite set of TYPES
    (subclasses). Unlike enums, the subclasses of a sealed class can be entirely
    different from each other. Some can be standard objects, some can be data
    classes holding unique variables, and you can create multiple unique
    instances of them!
* */
sealed class MuseumEvent {
    // An object: Just a simple constant (like an enum)
    object MuseumOpened : MuseumEvent()

    // A data class: holds specific state/data for this exact event!
    data class VisitorEntered(
        val visitorName: String,
        val roomName: String,
    ) : MuseumEvent()

    // Another data class with completely different parameters
    data class RoomReachedCapacity(
        val roomName: String,
        val waitingCount: Int
    ) : MuseumEvent()
}
/*
    WHEN TO USE THEM:
        * When the result of an operation can be drastically different things
          (e.g., a network call returning `Success(val data: String)` or
          `Error(val code: Int, val message: String)`).
        * When you want to use a `when` statement and have the compiler force
          you to handle every possible subclass, making your code incredibly
          safe.



    THE GOLDEN RULE SUMMARY
        * Use `enum class` if you just need a list of simple, static constants
          that all share the exact same properties.
        * Use `sealed class` if your options need to hold different type of data
          , or if you need to create multiple unique instances of those options.
* */






/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
data class VisitorBadge(
    val visitorName: String,
    val idNumber: Int,
)

/*
fun main() {
    val vb1 = VisitorBadge("Neha", 101)
    val vb2 = VisitorBadge("Neha", 101)
    val tbSet: MutableSet<VisitorBadge> = mutableSetOf()
    tbSet.add(vb1)
    println("${tbSet.contains(vb2)}")
}
 */


enum class TurnstileDirection(val occupancyChange: Int) {
    INBOUND(1),
    OUTBOUND(-1),
}

sealed class TurnstileAttempt {
    data class Success(
        val destinationRoom: MuseumRoom
    ) : TurnstileAttempt()

    data class FailedRoomFull(
        val estimatedWaitTime: Long
    )

    object MuseumClosed: TurnstileAttempt()
}





/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */









