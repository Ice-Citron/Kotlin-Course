/*
3. OVERRIDING PROPERTIES and `super`
    Just like you can override functions in a subclass, you can override `val`
    and `var` properties to inject custom logic whenever a property is accessed
    (`get`) or changed (`set`),

    EXPLAIN BY EXAMPLE:
* */
open class Cell2(open var value: Int)

class BackedUpCell2(value: Int) : Cell2(value) {

    private val history: MutableList<Int> = mutableListOf()

    // Overriding the property!
    override var value: Int
        get() = super.value     // Read the parent's actual value
        set(newValue) {
            history.add(0, super.value) // Save the old value first!
            super.value = newValue
        }
}


/*
    - WHEN DO I USE THEM: When a subclass needs to track, validate, or react to
     changes happening to a property it inherited from a parent class.
    - WHY DO I USE THEM: It allows you to add powerful features (like undo
      histories, validation checks, or UI updates) while keeping the clean
      syntax of simply typing `cell.value = 5`.
    - WHEN DO I NOT USE THEM AND WHY: Never forget the `super` keyword! If you
      write `value = newValue` inside the setter, the setter will recursively
      call itself forever until the app crashes with a `StackOverflowError`. You
      must route the data to the parent's backing field using `super`.
    - WHY THEM OVER OTHER USE CASES: In Java, fields (variables) cannot be
      overridden, which forces developers to write bloated `getValue()` and
      `setValue()` methods everywhere. Kotlin handles this gracefully at the
      language level.
* */





/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
4. ALTERNATIVES TO INHERITANCE: The Strategy Pattern
    Inheritance (creating subclasses) is rigid. Sometimes, instead of creating a
    massive tree of abstract classes and subclasses, it is vastly cleaner to
    abandon inheritance entirely and just pass a function into a generic class
    to tell it how to behave.
* */

// THE OLD WAY: Rigid Inheritance
abstract class GridWorld2 {
    abstract fun handleOverrun(pos: Pair<Int, Int>): Pair<Int, Int>
}
class DeadlyGridWorld2 : GridWorld2() {
    override fun handleOverrun(pos: Pair<Int, Int>): Pair<Int, Int> = throw Exception("You died!")
}
class BouncingGridWorld2 : GridWorld2() {
    override fun handleOverrun(pos: Pair<Int, Int>): Pair<Int, Int> = Pair(0, 0)
}


// THE NEW WAY: The Strategy Pattern (Passing behavior as a variable)
class GridWorld3(
    val handleOverrun: (Pair<Int, Int>) -> Pair<Int, Int>
    // <-- The Strategy (a function property)
)

fun main73() {
    // We create drastically different worlds using the EXACT SAME class via factories!
    val deadlyWorld: GridWorld3 = GridWorld3(handleOverrun = { throw Exception("Fell off the map!") })
    val boundedWorld: GridWorld3 = GridWorld3(handleOverrun = { position -> Pair(0, 0) })
}

/*
    - WHEN DO I USE THEM: When you have a single class that needs to behave in a
      few different ways, but creating a whole new subclass file for each
      variation feels like overkill.
    - WHY DO I USE THEM: ...
* */













/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */














/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */














/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */














/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */














/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */


/*
            Gemini said: https://gemini.google.com/app/61f5e835c1f55bc4
                I completely understand why it feels confusing! You have actually nailed the three foundational classes related to inheritance, but that isn't quite the whole picture.

                Kotlin introduces several extra "specialty" classes. Instead of making you write hundreds of lines of boilerplate code (like you would in traditional Java), Kotlin gives you specific keywords to handle common scenarios instantly.

                Here is your complete cheat sheet for every type of class in Kotlin:
* */
