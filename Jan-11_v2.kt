/*
         @\_______/@
        @|XXXXXXXX |
       @ |X||    X |
      @  |X||    X |
     @   |XXXXXXXX |
    @    |X||    X |             V
   @     |X||   .X |
  @      |X||.  .X |                      V
 @      |%XXXXXXXX%||
@       |X||  . . X||
        |X||   .. X||                               @     @
        |X||  .   X||.                              ||====%
        |X|| .    X|| .                             ||    %
        |X||.     X||   .                           ||====%
       |XXXXXXXXXXXX||     .                        ||    %
       |XXXXXXXXXXXX||         .                 .  ||====% .
       |XX|        X||                .        .    ||    %  .
       |XX|        X||                   .          ||====%   .
       |XX|        X||              .          .    ||    %     .
       |XX|======= X||============================+ || .. %  ........
===== /            X||                              ||    %
                   X||           /)                 ||    %
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* */

/*
    This is a great pivot. With your C++ and Game Engine background, a lot of
    Kotlin's specific design choices (like `object` and `sealed` classes) will
    actually make more sense to you as clean architectural patterns for things
    you probably had to implement manually in C++.



    1. WHAT IS AN `object`? (THE SINGLETON)

    In C++, if you wanted a "GameManager" or "Logger" that only has one instance
    , you would implement the SINGLETON PATTERN (private constructor, static
    `getInstance()` method).

    In Kotlin, `object` IS the Singleton Pattern baked into the language.
    - `class`: Defines a blueprint. You can make 0, 1 or 100 instances (`new` in
      C++).
    - `object`: Defines the blueprint AND creates the single instance
      immediately. You cannot create more.


    C++ ANALOGY: Think of a Kotlin `object` as a C++ class where EVERY MEMBER
    IS STATIC, but you can pass it around like a variable.
* */
// DEFINITION
object GameManager_ {                       // <-- private constructor ++ all static vars/funs !!
    var score = 0
    fun startGame() { println("Start!") }
}

// USAGE
fun main() {
    // No constructor call "GameManager()"
    GameManager.score += 10
    GameManager.startGame()
}




/*
    THE "COMPANION OBJECT" (C++ STATIC MEMBERS)

    Kotlin classes don't have a `static` keyword. Instead, if you want something
    to belong to the class rather than an instance (like a factory method of a
    constant), you put it inside `companion object`.
* */
class Bullet_{
    // Normal instance method
    fun fly() {}

    // Static stuff goes here
    companion object {                                  // <-- static methods go here!!
        fun createStandardBullet(): Bullet = Bullet()
    }
}

// Usage
val b_2 = Bullet.createStandardBullet()     // looks exactly like C++ static call




/*¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/
/*
    2. SEALED CLASSES: THE "SUPER-POWERED ENUM"

    You asked: "Why is this used for STATE?"

    In Game Development, you definitely used FINITE STATE MACHINES (FSM).
    - SIMPLE STATE (Enums): `IDLE`, `RUNNING`, `JUMPING`.
    - COMPLEX STATE (The Problem): What about the `ATTACKING` state? It might
      need an integer for `damage`. What about `DYING`? It might need a float
      for `timeRemaining`.

    In C++, you might have done this with a `union`, a `void*`, or a base class
    `State` with casting. SEALED CLASSES solve this by allowing each "State" to
    carry its own specific data.



    EXAMPLE 1: THE GAME LOOP STATE (THE "VISUAL" EXAMPLE)

    Imagine your game engine's main loop. The game is always in ONE specific
    mode.
* */

// The Parent: "The Game is currently in one of these modes"
sealed class GameState_

// 1. Menu: No data needed. It's just a flag.
// Use `object` (Singleton) because all menus are the same.
object Menu_ : GameState()                  // <-- singleton... private constructor && static fun/var

// 2. Playing: Needs specific data (Current Level, Player HP).
// Use `class` because state chanegs (HP goes down).
data class Playing_(val levelId: String, val playerHp: Int) : GameState()

// 3. GameOver: Needs data (Final Score).
data class GameOver_(val score: Int) : GameState()

/*
// THE LOGIC
fun update(currentState: GameState) {
    // `when` is exhaustive. C++ switch would need a default case; Kotlin doesn't
    when (currentState) {
        is Menu_ -> { showMenuUI() }

        // kotlin smart-casts `currentState` to `Playing` here automatically
        is Playing_ -> {
            println("Loading Level: ${currentState.levelId}")       // .levelId is valid here!
            spawnPlayer(hp = currentState.playerHp)
        }
        is GameOver_ -> { saveScore(currentState.score) } // .score is valid here!
    }
}
*/
/*
    WHY IS THIS "STATE"? It models the state of your application at a specific
    slice in time. It isn't just a label (like an Enum); it is LABEL + THE DATA
    REQUIRED FOR THAT LABEL.



    EXAMPLE 2: LOADING DATA (COMMON EXAM PATTERN)

    This is the classic "Network Request" example used in exams and Android
    apps.
* */
sealed class RequestState {
    object Loading : RequestState()                             // Just a spinner
    data class Success(val json: String) : RequestState()       // Holds the data
    data class Error(val exception: Exception) : RequestState() // Holds the crash reason
}
/*
    CHCET
    copy // toString // equals // componentN // hashCode
* */


/*

* */

sealed class Shape_2
class Circle_3(val radius: Double) : Shape_2()
class Rectangle_3(val width: Double, val height: Double) : Shape_2()

fun calculateArea(s: Shape_2): Double {
    when (s) {
        is Circle_3    -> { return s.radius * s.radius * kotlin.math.PI }
        is Rectangle_3 -> { return s.width * s.height }
    }
}


/*
* `companion object`
* */
class Zombie {
    companion object {
        fun spawnHorde(count: Int): List<Zombie> { return listOf() }
    }
}
// Call: Zombie.spwanHorde(10)



/*
         @\_______/@
        @|XXXXXXXX |
       @ |X||    X |
      @  |X||    X |
     @   |XXXXXXXX |
    @    |X||    X |             V
   @     |X||   .X |
  @      |X||.  .X |                      V
 @      |%XXXXXXXX%||
@       |X||  . . X||
        |X||   .. X||                               @     @
        |X||  .   X||.                              ||====%
        |X|| .    X|| .                             ||    %
        |X||.     X||   .                           ||====%
       |XXXXXXXXXXXX||     .                        ||    %
       |XXXXXXXXXXXX||         .                 .  ||====% .
       |XX|        X||                .        .    ||    %  .
       |XX|        X||                   .          ||====%   .
       |XX|        X||              .          .    ||    %     .
       |XX|======= X||============================+ || .. %  ........
===== /            X||                              ||    %
                   X||           /)                 ||    %
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
* */

/*¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/
/*
    ... because it captures how you often use them in Kotlin, even if the
    mechanics are slightly different.


    THE SHORT ANSWER:
    - ARE THEY STRUCTS? Sort of. Like a C++ struct, a Kotlin Enum can hold data
      variables (fields) and methods.
    - THE CATCH: Unlike a struct, you CANNOT create new instances of them
      whenever you want. You are locked into the specific instances you defined
      at the start.

    Think of Kotlin Enum as a STRUCT where you are only allowed to have `const`
    global instances that are defined immediately.



    1. THE "STRUCT" INTUITION (HOLDING DATA)

    In C++,  you might make a struct to hold RGB values:

    ```C++
    // C++ Struct
    struct Color {
        int r, g, b;
    }
    // You can make infinite colors
    Color red  = {255, 0, 0};
    Color blue = {0, 0, 255};
    Color myWeirdColor = {12, 34, 56};
    ```

    In Kotlin, an ENUM does the exact same thing (holds `r, g, b`), but it LOCKS
    the universe so that only the specific colors you listed exist. You cannot
    create `myWeirdColor`.
* */

// Kotlin Enum
enum class Color_(val r: Int, val g: Int, val b: Int) {
    // These are the ONLY instances that will ever exist.
    // They look just like struct initializers!
    RED(255, 0, 0),
    GREEN(0, 255, 0),
    BLUE(0, 0, 255);        // Semicolon ends the list
}

/*
```Kotlin
fun main() {
    // Usage: Accessing data just like a struct member
    val c = Color.RED
    print(c.r)                  // Prints: 255
}
```

    - SIMILAR TO C++ STRUCT: It has properties (`r`, `g`, `b`).
    - DIFFERENT FROM C++ STRUCT: You cannot say `val c = Color(1, 2, 3)`. The#
      constructor is private.
* */




/*¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/
/*
    2. THE "CLASS" POWER (ADDING METHODS)

    In modern C++, structs can have methods. Kotlin Enums can too. This allows
    you to attach logic to these constants.

    EXAMPLE: THE CONSOLE WAR
    Imagine you want to represent gaming consoles. You need to know their brand
    and calculate if they are "retro" (older than 20 years).
* */
enum class Console(val brand: String, val releaseYear: Int) {
    // 1. Define the specific instances (The Data)
    PS5("Sony", 2020),
    SWITCH("Nintendo", 2017),
    N64("Nintendo", 1996),
    SEGA_GENESIS("Sega", 1988);     // End of list

    // 2. Define methods (The Logic)
    fun isRetro(): Boolean {
        val currentYear = 2026
        return (currentYear - releaseYear) > 20
    }

    // 3. You can even generate a description
    fun getDescription(): String = "$brand console was released in $releaseYear."
}

/*
    SUMMARY COMPARISON
    - C++ `struct`
        - HOLDS DATA?       Yes (`int x;`)
        - HAS METHODS?      Yes
        - INSTANCES?        Unlimited (`new Struct()`)

    - C++ `enum` (Traditional)
        - HOLDS DATA?       No (Just Integers)
        - HAS METHODS?      No
        - INSTANCES?        Integers

    - Kotlin `enum class`
        - HOLDS DATA?       Yes (`val x: Int`)
        - HAS METHODS?      Yes
        - INSTANCES?        FIXED SET (`RED`, `BLUE`)
* */



/*
* 3. Collections: The Big Three
Kotlin distinguishes sharply between read-only (immutable) and mutable collections .
*
*
*
* Scenario: You are coding a card game. You need a suit type (HEARTS, CLUBS, etc.). You also want to store the "color" of the suit (Red or Black).
*
*
*
* You have hit on a really deep language design topic. You are right: the reason enum doesn't have unlimited instances is simply semantic definition.
* */





/*¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/




/*¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/
/*¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/






/*¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/
/*¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/





/*
*
* Exhaustive = including or consideing all elements or aspects; fully
*              comprehensive.
*
*
* */