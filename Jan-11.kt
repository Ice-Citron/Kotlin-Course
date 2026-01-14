
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
    Here is the refresher on the "Everything is an Object" dynamic, the `Any`
    class, and how inheritance ties it all together.


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


    2. "EVERYTHING IS AN OBJECT" (NO PRIMITIVES... SORT OF)

    You asked if types like `Int` are "primitives" or classes.
    - IN SYNTAX (YOUR VIEW): They are CLASSES. There is no `Int` vs `Integer`
      distinction like in Java. You just use `Int`. It has methods, it has
      properties (like `Int.MAX_VALUE`), and it inherits from `Number` (which
      inherits from `Any`).
    - UNDER THE HOOD (COMPILER VIEW): For performance, the Kotlin compiler acts
      smart. If it can, it compiles your Int class down to a raw Java primitive
      `int` in the bytecode. But you, the programmer, don't need to worry about
      that. You treat them as objects.


    3. Are `fun`, `operator`, and `=` Classes?

    NO. These are KEYWORDS (part of the language syntax), not classes.
    - `class`, `fun`, `val`: These are instructions to the compiler. You cannot
      instantiate a `fun`.
    - `=`: This is a syntactic assignment operator.
    - `+`, `=`, `*`: These are distinct. While `+` is an operator, Kotlin maps
      it to a FUNCTION CALL inside a class.
        - Writing `a + b` is syntactic sugar for calling the function
          `a.plus(b)`.
        - So, the `+` symbol isn't a class, but it triggers a method belonging
          to a class.


    4. INHERITANCE REFRESHER

    In Java, all classes are "inheritable" (open) by default. In Kotlin, the
    philosophy is "Design for inheritance or forbid it."

    RULE 1: CLASSES ARE CLOSED (`final`) BY DEFAULT. You cannot inherit from a
    standard class. You must explicitly mark it as `open` to allow others to
    extend it.

    RULE 2: METHODS ARE CLOSED BY DEFAULT. Even if the class is open, you cannot
    override its functions unless they are also marked `open`.

    RULE 3: THE SYNTAX
        - Use a colon `:` to inherit
        - You must call the parent's constructor immediately using `()`


    INHERITANCE CODE EXAMPLE

    Here is how you would write a hierarchy where `Animal` is the parent and
    `Dog` is the child.
* */


// 1. `open` allows other classes to inherit from this
open class Animal(val name: String) {

    // 2. `open` allows subclasses to override this specific method
    open fun makeSound() {
        println("Some generic animal sound")
    }

    // This method is NOT open, so it cannot be overridden
    fun sleep() {
        println("Zzz...")
    }
}

// 3. use `:` to inherit. passed `name` to the parent constructor
class Dog_(name: String) : Animal(name) {

    // 4. `override` is required to modify behavior
    override fun makeSound() {
        println("Woof")
    }
}

/*
fun main() {
    val d = Dog("Rex")
    d.makeSound()           // Prints: Woof!
    d.sleep()               // Prints: Zzz...      (inherited from Animal class)

    // 5. Polymorphism: A Dog IS-A Animal, and an Animal IS-A Any
    val a: Animal = d
    val any: Any  = d
}
* */


/*~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^*/

/*
    SUMMARY HIERARCHY

    So the hierarchy for the integer `5` looks roughly like this:

    `Any` (Has `toString`, `equals`) `UP` `Number` (Base for numeric types) `UP`
    `Int` (The class you use)

    And for the `Dog` above

    `Any` `UP` `Animal` `UP` `Dog`
* */

/*
        CHCET
                copy()
                toString()
                hashCode()
                equals()
                componentN()
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
    In Kotlin, classes are `final` BY DEFAULT. This means you CANNOT inherit
    from a class unless you explicitly allow it. This is the opposite of Java
    (where everything is open by default).


    1. `open`: THE "UNLOCK" KEY

        - WHAT IT DOES: It allows other classes to inherit from this class. It
          also allows methods to be overridden.
        - WHEN TO USE IT: When you are building a standard class (like `Person`)
          that works fine on its own, b ut you want to allow someone else to
          extend it (like `Student`).
* */

// 1. You MUST write `open` to allow inheritance
open class Person_(val name: String) {
    // 2. You MUST write `open` to allow overriding this function.
    open fun describe(): String = "I am a person."
}

class Student_(name: String) : Person_(name) {
    override fun describe(): String = "I am a student named $name."
}

// Usage
val p_1 = Person_("Matt")           // Valid: can instantiate the parent directly
val s_1 = Student_("Alice")



/*~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^*/

/*
    2. `abstract`: THE "INCOMPLETE TEMPLATE"                    <-- akin to C++ `virtual` classes

    - WHAT IT DOES: You CANNOT create an instance of an abstract class. It is
      "half-finished." It can hold state (properties) and methods with code, but
      it can also have `abstract` methods that have `no body`.      <-- just like C++ `virtual` functions as well!
            Subclasses must fill in the blanks.
    - WHEN TO USE IT: When you have a base concept (like `Shape` or `Animal`)
      that shouldn't exist on its own. You never see a generic "Shape" floating
      in the void; you only see a Circle or a Square.
* */
abstract class Shape_ {
    // Standard state (inherited by everyone)
    val color: String = "Red"

    // Abstract method: NO BODY. Sublcasses are forced to write this.
    abstract fun area(): Double
}

// ERROR: Cannot create an instance of an abstract class
// val s = Shape()

class Circle_(val radius: Double) : Shape() {
    override fun area(): Double = kotlin.math.PI * radius * radius
}



/*~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^*/

/*
    3. `sealed`: THE "RESTRICTED SET"

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
sealed class NetworkResult_ {
    data class Success(val data: String) : NetworkResult_()
    data class Error(val code: Int, val message: String) : NetworkResult_()
    object Loading : NetworkResult_()       // Singleton state
}

fun handleResult(result: NetworkResult_) {
    // Look! No `else` branch needed. Kotlin knows these are the only 3 options.
    when (result) {
        is NetworkResult_.Success -> println("Got data: ${result.data}")
        is NetworkResult_.Error -> println("Error ${result.code}")
        is NetworkResult_.Loading -> println("Please wait...")
    }
}


/*
    SUMMARY COMPARISON
    - MODIFIER: (default)                   <-- normal `class`
         - Can Instantiate? Yes
         - Can Inherit? NO                  <-- because normal class is FINAL class by default
         - Subclasses Limit: N/A
         - Use Case: Standard objects (security, simplicity)
    - MODIFIER: `open`
        - Can Instantiate? Yes
        - Can Inherit? Yes
        - Subclasses Limit: UNLIMITED
        - Use Case: Extensible libraries, base classes
    - MODIFIER: `abstract`
        - Can Instantiate? No
        - Can Inherit? Yes
        - Subclasses Limit: UNLIMITED
        - Use Case: Templates, base concepts (`Shape`)
    - MODIFIER: `sealed`
        - Can Instantiate? No
        - Can Inherit? Yes
        - Subclasses Limit: RESTRICTED
        - Use Case: Finite states, ADTs (`Success` / `Failure`)

* */


/*
    MEMORY REFRESHER ABOUT `data class`

    THE MAIN DOWNSIDES

    1. NO INHERITANCE (THE BIG ONE) Data classes are `final` by default and
       CANNOT be marked as `open` or `abstract`.

       - You cannot create a subclass of a `data` class.
       - If you are building a class hierarchy (e.g., a base `Enemy` class with
         `Orc` and `Goblin` subclasses), the parent `Enemy` cannot be a data
         class.

    2. CONSTRUCTOR PROPERTIES ONLY: The automatically generated methods
       (`equals`. `hashCode`, `copy`) ONLY consider properties defined in the
       PRIMARY CONSTRUCTOR.

       - If you define a property inside the class body (curly braces `{}`), it
         will be IGNORED by `equals()` and `toString()`. Two objects could have
         different internal states but still be considered "equal".

    3. ARRAY GOTCHAS: If your data class has an `Array` property, the generated
       `equals()` method checks the array's REFERENCE, not its contents. Two
       data objects with identical arrays will return `false` for equality
       unless you manualy override `equals` and `hashCode`, defeating the
       purpose of the `data` keyword.


    SUMMARY: WHEN TO USE WHICH?
    - `data class`
        - PURPOSE: Just holding data (DTOs, state)
        - INHERITANCE: You don't need to subclass it
        - IDENTITY: Content defines equality (Value Semantics)
        - EXAMPLE: `Coordinate(x, y)`, `UserConfig`
    - `class`
        - PURPOSE: Encapsulation, behavior or logic
        - INHERITANCE: You need an `open`, `abstract` or `sealed` parent
        - IDENTITY: Reference defines equality (Entity semantics)
        - EXAMPLE: `GameManager`, `DatabaseConnection`


    QUICK RULE OF THUMB: If you ever find yourself saying, "I need to check if
    these objects contain the same values," usa a DATA CLASS. If you are saying,
    "I need to create a hiearrchy" or "This object performs actions/logic," use
    a NORMAL CLASS.
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
    4. FP vs OOP Design (`scaleBy` and `translate`)

    The slide poses a key design question: "Update the object or return a new
    object?".

    This depends on whether your class is MUTABLE (`var`) or IMMUTABLE (`val`).

    - OPTION A: Mutable (Classic OOP)
        - Properties are `var`
        - The method returns `Unit` (nothing).
        - Effect: The circle itself changes size.
        ```Kotlin
        fun scaleBy(factor: Int) {
            this.radius *= factor
        }
        ```

    - OPTION B: IMMUTABLE (Functional Style - Recommemnded for this course)
        - Properties are `val`.
        - The method returns `Circle`.
        - Effect: The old circle stays the same; you get a new circle that is
          bigger.
        ```Kotlin
        fun scaleBy(factor: Int): Circle {
            return Circle(this.center, this.radius * factor)
        }
        ```


    -- Since the course emphasizes "Functional Programming in an OOP language,"
       default to OPTION B (returning a new object) unless the instructions
       explicitly ask you to modify state.
* */




/*~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^*/

/*
    It looks like there might be a small typo in your question--you likely
    meant "WHY IS THE FUNCTIONAL (IMMUTABLE) STYLE RECOMMENDED OVER THE MUTABLE
    (`var`) STYLE?"

    You are completely right that writing `var` (mutable state) often feels
    simpler and more intuitive at first. It mimics how we think in the real
    world: "I have a cup, I fill it with water." (The cup changes state). The
    functional approach says: "I have an empty cup. I create a new universe
    where there is a full cup."

    This sounds crazy and overly complex. So, why do we recommend it? You hit on
    th key reasons ("prevent logic errors or bugs"), but here is the deeper
    breakdown of why Kotlin and modern development push for `val`:



    1. PREDICTABILITY (THE "WHO CHANGED MY DATA?" PROBLEM)

    With `var`, any part of your code (or any other thread) can change an
    object.
    - THE BUG: You pass a `Person` object to a function called `printName(p)`.
      Unbeknownst to you, that function also sets `p.age = 0` as a side effect.
      Later, your calculation fails.
    - THE `val` FIX: If the object is immutable, you can pass it anywhere
      without fear. You know exactly what the value is because it CANNOT change
      after creation. This makes debugging massive codebases significantly
      easier.


    2. THREAD SAFETY (CONCURRENCY)

    This is the biggest technical reason.
    - The `var` Risk: If two threads try to update the same `var counter` at the
      same time, you get race conditions and crashes. You have to write complex
      locking code (mutexes) to stop them from fighting.
    - The `val` Fix: Immutable objects are inherently thread-safe. Since no one
      can change them, a million threads can read them simultaneously without
      any issues.


    3. "Time Travel" and History

    In the `val` (functional) style, when you "change" something, you actually
    create a copy with the new value (like `data class copy()`).
    - This means you inherently keep the old version safe.
    - This is essential for features like "Undo/Redo" or tracking state changes
      over time (like in React or Redux architectures, which heavily influenced
      modern UI frameworks like Jetpack Compose in Kotlin).


    4. The Lecture Context

    ... "Update the object or return a new object?"
    - UPDATE (`var`): The method returns `Unit` (nothing) and secretly changes
      the world.
    - RETURN NEW (`val`): The method returns a `Circle`. It's a pure
      transformation (`Input -> Output`).


    ------------

    WHEN IS `var` ACTUALLY BETTER?

    You are right that `var` is simpler. It's perfectly fine to use `var` for:
    - LOCAL VARIABLES INSIDE A FUNCTION: Loops, counters and temporary logic
      where the variable never escapes that specific function scope.
    - PERFORMANCE-CRITICAL LOOPS: Creating a new object every milisecond can
      sometimes be slower than updating one integer (though the Kotlin compiler
      is very good at optimizing this away.)

    SUMMARY: We accept the slightly "harder" syntax of `val` and copying objects
    because it eliminates entire categories of bugs (race conditions, accidental
    state changes) that are extremely painful to fix later.
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


    1. WHAT IS AN `object`? (The Singleton)

    In C++, if you wanted a "GameManager" or "Logger" that only has one instance
    , you would implement the SINGLETON PATTERN (private constructor, static
    `getInstance()`) method).

    In Kotlin, `object` IS the Singleton Pattern baked into the language.
    - `class`: Defines a blueprint. You can make 0, 1, or 100 instances (`new`
      in C++).
    - `object`: Defines the blueprint AND creates the single instance
      immediately. You cannot create more.


    C++ ANALOGY: Think of a Kotlin `object` as a C++ class where EVERY MEMBER IS
    STATIC, but you can pass it around like a variable.
* */
// DEFINITION
object GameManager {
    var score = 0
    fun startGame() { println("Start!") }
}

// USAGE
fun main() {
    // No constructor call "GameManager()". You just access it directly.
    GameManager.score += 10
    GameManager.startGame()
}




/*~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^*/
/*
    THE "COMPANION OBJECT" (C++ STATIC MEMBERS)

    Kotlin classes don't have a `static` keyword. Instead, if you want something
    to belong to the class rather than an instance (like a factory method or a
    constant), you put it inside a `companion object`.
* */

class Bullet {
    // Normal instance method
    fun fly() {}

    // Static stuff goes here
    companion object {
        fun createStandardBullet(): Bullet = Bullet()
    }
}

// Usage
val b = Bullet.createStandardBullet()       // Looks exactly like C++ static call



/*
    2. SEALED CLASSES: THE "SUPER-POWERED ENUM"

    You asked: "Why is this used for STATE?"

    In Game Development, you definitely used FINITE STATE MACHINES (FSM).

    - SIMPLE STATE (ENUMS): `IDLE`, `RUNNING`, `JUMPING`.
    - COMPLEX STATE (The Problem): What about the `ATTACKING` state? It might
      need an integer for `damage`. What about `DYING`? It might need a float
      for `timeRemaining`/

    In C++, you might have done this with a `union`, a `void*`, or a base class
    `State` with casting. SEALED CLASSES solve this by allowing each "State" to
    carry its own specific data.



    EXAMPLE 1: THE GAME LOOP STATE (THE "VISUAL" EXAMPLE)

    Imagine your game engine's main loop. The game is always in ONE specific
    mode.
* */
// The Parent: "The Game is currently in one of these modes"
sealed class GameState

// 1. Menu: No data needed. It's just a flag.
// Use 'object' (Singleton) because all menus are the same.
object Menu : GameState()

// 2. Playing: Needs specific data (current level, player HP).
// Use `class` because state changes (HP goes down).
data class Playing(val levelId: String, val playerHp: Int) : GameState()















class `Jan-11` {
}
