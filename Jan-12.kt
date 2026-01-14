import kotlin.math.PI

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
    2. ENUMS (ENUMERATED CLASSES)

    Enums are for when variable can only be one of a small, fixed set of values
    (e.g. Days of the Week).

    - BASIC SYNTAX: `enum class Direction { NORTH, SOUTH, WEST, EAST }`
    - SUPERPOWER: In Kotlin, enums are full classes. They can have:
        - PROPERTIES: `val abbreviation: String`
        - CONSTRUCTORS: `EAST("E")`
        - METHODS: `fun isVertical(): Boolean`



    3. COLLECTIONS: THE BIG THREE

    Kotlin distinguishes sharply between READ-ONLY (immutable) and MUTABLE
    collections.
    - LIST
        - Read-Only (Default): `listOf(1, 2)`
        - Mutable (Editable): `mutableListOf(1. 2)`
        - Key Feature: Ordered, allows duplicates. Access by index `[0]`.
    - SET
        - Read-Only (Default): `setOf("A", "B")`
        - Mutable (Editable): `mutableSetOf("A", "B")`
        - Key Feature: Unordered, UNIQUE ELEMENTS ONLY. Good for "membership"
          checks.
    - MAP
        - Read-Only (Default): `mapOf("k" to "v")`
        - Mutable (Editable): `mutableMapOf("k" to "v")`
        - Key Feature: Key-Value pairs.



    ----------|-<>
    - GOTCHA: If you assign a `listOf(...)` to a `var`, you can change which
      list variable points to, but you still cannot add items to the list
      itself. To add items, the list object must be a `MutableList`.
* */


enum class Element {
    FIRE, WATER, GRASS;     // Semicolon is required if you add functions below.

    fun beats(other: Element): Boolean = when(this) {
        FIRE  -> other == GRASS
        WATER -> other == FIRE
        GRASS -> other == WATER
    }
}



//Add your code here

//Add your code here
enum class Month_ {
    JAN, FEB, MAR, APR, MAY, JUN,
    JUL, AUG, SEP, OCT, NOV, DEC;

    fun isDuringAcademicYear(): Boolean = when(this) {
        MAR  -> false
        JUL  -> false
        AUG  -> false
        SEP  -> false
        else -> true
    }

    fun isShortMonth(): Boolean = when(this) {
        JAN  -> false
        MAR  -> false
        MAY  -> false
        JUL  -> false
        AUG  -> false
        OCT  -> false
        DEC  -> false
        else -> true
    }
}


fun length_(xs: List<String>): List<Int> = xs.map { it.length }

fun complements_1(nums: List<Int>): List<Pair<Int, Int>> =
    nums.map { Pair(it, 10-it) }


val pairs_ = listOf(Pair(1, 9), Pair(3, 4), Pair(5, 5))

fun matchingTotal_(total: Int, xs: List<Pair<Int, Int>>): List<Pair<Int, Int>> =
    xs.filter { it.first + it.second == total }


/*
    In Kotlin, the difference is simple: YOU SHOULD ALMOST ALWAYS USE `Int`.

    Here is the breakdown of why they are different and what happens under the
    hood:


    1. `Int` (The Kotlin Standard)

    `Int` is a class in Kotlin that acts as a "smart" wrapper.
    - PERFORMANCE: When you use `Int` in your code, the Kotlin compiler
      automatically optimizes it to a primitive Java `int` whenever possible
      (which is efficient and takes up less memory).
    - NULLABILITY: If you make it nullable (`Int?`), Kotlin automatically
      switches to using the boxed `java.lang.Integer` object because primitives
      cannot be null.
    - USAGE: You should use this for 99% of your integer needs.



    2. `Integer` (The Java Class)

    `Integer` is specifically the Java class `java.lang.Integer`.
    - PLATFORM TYPE: In Kotlin, you technically can access `java.lang.Integer`
      directly because Kotlin woks with Java classes, but it is considered a
      "Phantom Type".
    - USAGE: You almost never need to type `Integer` in your Kotlin code. Even
      when interoperating with Java libraries that ask for an `Integer`,
      Kotlin's `Int` handles the conversion for you automatically.


    SUMMARY TABLE
    - `Int`
        - LANGUAGE : Kotlin
        - NULLABLE?: No (unless `Int?`)
        - Compiled to (JVM): `int` (Primitive) usually
    - `Int?`
        - LANGUAGE : Kotlin
        - NULLABLE?: Yes
        - Compiled to (JVM): `Integer` (Boxed Object)
    - `Integer`
        - LANGUAGE : Java
        - NULLABLE?: Yes
        - Compiled to (JVM): `Integer` (Boxed Object)


    KEY TAKEAWAY: Just write `Int`. Kotlin will handle the rest.
* */





/*
  _--_                                     _--_
/#()# #\         0             0         /# #()#\
|()##  \#\_       \           /       _/#/  ##()|
|#()##-=###\_      \         /      _/###=-##()#|
 \#()#-=##  #\_     \       /     _/#  ##=-#()#/
  |#()#--==### \_    \     /    _/ ###==--#()#|
  |#()##--=#    #\_   \!!!/   _/#    #=--##()#|
   \#()##---===####\   O|O   /####===---##()#/
    |#()#____==#####\ / Y \ /#####==____#()#|
     \###______######|\/#\/|######______###/
        ()#O#/      ##\_#_/##      \#O#()
       ()#O#(__-===###/ _ \###===-__)#O#()
      ()#O#(   #  ###_(_|_)_###  #   )#O#()
      ()#O(---#__###/ (_|_) \###__#---)O#()
      ()#O#( / / ##/  (_|_)  \## \ \ )#O#()
      ()##O#\_/  #/   (_|_)   \#  \_/#O##()
       \)##OO#\ -)    (_|_)    (- /#OO##(/
        )//##OOO*|    / | \    |*OOO##\\(
        |/_####_/    ( /X\ )    \_####_\|
       /X/ \__/       \___/       \__/ \X\
      (#/                               \#)
* */



/*¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/
/*

    - TO START THE INTERACTIVE SHELL (REPL): ...

    ```Bash
    kotlinc -Xrepl
    ```

    This gives you the `>>>` prompt where you can type code like `1 + 1` or
    define functions on the fly.



    2. HOW TO RUN A FILE (THE PYTHON WAY)

    If you have a file named `hello.kt`, you can't just run `kotlin hello.kt`
    directly like Python. You have two options: COMPILE THEN RUN (Standard) or
    RUN AS SCRIPT (Quick).


    OPTION A: RUN AS A SCRIPT (Closest to Python)

    If you rename your file to end in `.kts` (Kotlin SCRIPT), you can run it
    instantly without compiling manually.
      1. Create/Rename your file to `main.kts`.
      2. Run it:

      ```Bash
      kotlin -script main.kts
      ```
      Note: This is slower for big projects but perfect for revision notes.


    OPTION B: COMPILE AND RUN (THE "REAL" WAY)

    This is what actually happens under the hood. It creates a `.jar` file (Java
    Archive).

      1. COMPILE:
      ```Bash
      kotlinc hello.kt -include-runtime -d hello.jar
      ```
      (Translation: Compile `hello.kt`, include the standard library so it works
      anywhere, and dump it into `hello.jar`)


      2. RUN:
      ```Bash
      java -jar hello.jar
      ```


      3. WHY IS IT LIKE THIS? (THE "WHY" VS PYTHON/HASKELL)
        - PYTHON is interpreted line-by-ine
        - HASKELL (GHCi) compiles to bytecode on the fly effectively.
        - KOTLIN runs on the JVM (Java Virtual Machine). It has to translate
          your code into Java Bytecode (`.class` files) before the JVM can even
          look at it.


      SUMMARY FOR YOUR REVISION SESSIONS: If you just want to write code and run
      it immediately without setup:

      1. Name your files `.kts` (e.g., `notes.kts`).
      2. Run them with `kotlin - script notes.kts`.
      3. Ignore IntelliJ for now.

* */




/*
  _--_                                     _--_
/#()# #\         0             0         /# #()#\
|()##  \#\_       \           /       _/#/  ##()|
|#()##-=###\_      \         /      _/###=-##()#|
 \#()#-=##  #\_     \       /     _/#  ##=-#()#/
  |#()#--==### \_    \     /    _/ ###==--#()#|
  |#()##--=#    #\_   \!!!/   _/#    #=--##()#|
   \#()##---===####\   O|O   /####===---##()#/
    |#()#____==#####\ / Y \ /#####==____#()#|
     \###______######|\/#\/|######______###/
        ()#O#/      ##\_#_/##      \#O#()
       ()#O#(__-===###/ _ \###===-__)#O#()
      ()#O#(   #  ###_(_|_)_###  #   )#O#()
      ()#O(---#__###/ (_|_) \###__#---)O#()
      ()#O#( / / ##/  (_|_)  \## \ \ )#O#()
      ()##O#\_/  #/   (_|_)   \#  \_/#O##()
       \)##OO#\ -)    (_|_)    (- /#OO##(/
        )//##OOO*|    / | \    |*OOO##\\(
        |/_####_/    ( /X\ )    \_####_\|
       /X/ \__/       \___/       \__/ \X\
      (#/                               \#)
* */


/*
    Since you are used to immediate feedback of `python` and `ghci`, the
    standard `kotlinc` can feel "blind". However, it is actually a very powerful
    tool for exams if you know how to force it to give you information.

    Here is the rundown of EXACTLY what you can do with it and the specific
    tricks to replicate the "discovery" features you miss from Haskell/Python.



    1. THE THREE MODES OF `kotlinc`

    - SCRIPT MODE
        - Command: `kotlinc -script note.kts`
        - Best For: Running quick practice files without compiling.
        - Python/Haskell Equivalent: `python note.py`
    - REPL MODE
        - Command: `kotlinc -Xrepl`
        - Best For: Testing single lines of logic, verifying syntax.
        - Python/Haskell Equivalent: `ghci` or `python` shell
    - COMPILE MODE
        - Command: `kotlinc file.kt -include-runtime -d out.jar`
        - Best For: Building the final "app" (likely not needed for revision).
        - Python/Haskell Equivalent: `ghc file.hs -o out`
* */
/*¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/



/*
    2. EXAM SURVIVAL TOOLS IN THE REPL (`-Xrepl`)

    You asked specifically about "tools in `kotlinc`" that help if you forget a
    method. The standard REPL is less chatty than GHCi, so you have to use
    KOTLIN'S REFLECTION CAPABILITIES to make it talk to you.


    THE "I FORGOT THE METHOD NAME" TRICK (Equivalent to Python's `dir()`)

    If you know you have a String but forgot if the method is called `upper`,
    `toUpper`, or `toUpperCase`, you can inspect the class directly in the REPL.

    ...
    ```kotlinc -Xrepl
    >>> String::class.members.forEach { println(it.name) }
    ```


                                    >>> String::class.members.forEach { println(it.name) }
                                    length
                                    chars
                                    codePoints
                                    compareTo
                                    describeConstable
                                    equals
                                    formatted
                                    get
                                    indent
                                    indexOf
                                    indexOf
                                    plus
                                    resolveConstantDesc
                                    splitWithDelimiters
                                    strip
                                    stripIndent
                                    stripLeading
                                    stripTrailing
                                    subSequence
                                    toString
                                    transform
                                    translateEscapes
                                    getChars
                                    hashCode
                                    isEmpty
                                    >>>


    This lists EVERY available method on the String class.


    ADVANCED VERSION (FILTER BY NAME): If you know it has "case" in the name:

    ```kotlinc -Xrepl
    >>> String::class.java.methods.filter { "case" in it.name.lowercase() }.map{ println(it.name) }
    // Output:
    // toUpperCase
    // toLowerCase
    // ...
    ```


                            >>> String::class.java.methods.filter { "case" in it.name.lowercase() }.map{ println(it.name) }
                            toLowerCase
                            toLowerCase
                            toUpperCase
                            toUpperCase
                            equalsIgnoreCase
                            compareToIgnoreCase
                            res2: kotlin.collections.List<kotlin.Unit> = [kotlin.Unit, kotlin.Unit, kotlin.Unit, kotlin.Unit, kotlin.Unit, kotlin.Unit]


* */



/*¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/
/*

    You are asking about the "interactive exploration" features that functional
    programmers love in GHCi (Haskell). While Kotlin has a compiler and a REPL,
    the "canonical" way yo get this information is shifted heavily toward
    IntelliJ IDEA's STATIC ANALYSIS rather than command-line directives.

    Here is the translation guide from GHCi commands to the Kotlin ecosystem,
    followed by the deep dive into `null` and `Unit`.


    PART 1: GHCi EQUIVALENTS IN KOTLIN

    The standard `kotlinc` REPL is often considered basic (and sometimes
    deprecated in favor of scripting). ... Most developers, however, rely on IDE
    shortcuts.

    - GHCi Command: `:type`
    - Kotlin (IntelliJ): Ctrl + Shift + P
    - Description: Shows the inferred type of the expression under the cursor or
      in the shell.

            (use Cmd + 7 on MacOS to trigger `STRUCTURE TOOL WINDOW`)
                        alternatively: VIEW | TOOL WINDOWS
            (USE Cmd + F12 on MacOS to trigger `STRUCTURE `)
                        alternatively: NAVIGATE | FILE STRUCTURE

    - GHCi Command: `:browse`
    - Kotlin (IntelliJ): Structure View (Alt + 7)




* */



/*
*
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


/*
            (base) ┌─(.../Year 1/Spring Term/Lecture Notes/40009 - Computing Practical/Kotlin/Kotlin-Liberty)───(administrator@StarForge-MacBook-Pro:s003)─┐
            └─(00:07:26 on main ✹)──> kotlinc Jan-12.kt -include-runtime -d Jan-12.jar                                                ──(Tue,Jan13)─┘
            (base) ┌─(.../Year 1/Spring Term/Lecture Notes/40009 - Computing Practical/Kotlin/Kotlin-Liberty)───(administrator@StarForge-MacBook-Pro:s003)─┐
            └─(00:07:46 on main ✹ ✭)──> java -jar Jan-12.jar                                                                          ──(Tue,Jan13)─┘
            no main manifest attribute, in Jan-12.jar
* */



/*¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/
/*¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/



class `Jan-12` {

}