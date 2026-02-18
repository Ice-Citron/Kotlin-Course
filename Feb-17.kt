/*
6. Overriding Properties

   Just like you can override functions in subclasses, you can override `val`
   and `var` properties to add custom behaviour when they are accessed or
   changed.

   EXPLAIN BY EXAMPLE:
* */
open class Cell(open var value: Int)

class BackedUpCell(value: Int) : Cell(value) {
    private val backups = mutableListOf<Int>()

    override var value: Int
        get() = super.value
        set(newValue) {
            backups.add(0, super.value)     // Save the old value first!
            super.value = newValue                          // Then update the parent's value
        }
}

/*
   - WHEN DO I USE THEM: When a subclass needs to "spy" on or alter the
     getting/setting of a property that it inherited from a parent class.
   - WHY DO I USE THEM: It allows you to add features like logging, backing up,
     or validation without having to touch or modify the original parent class.
   - WHEN DO I NOT USE THEM AND WHY: Never forgot to use the `super.` keyword
     inside your setter! If you wrote `value = newValue` inside the setter, the
     setter would call itself infinitely until the app crashes with a
     `StackOverflowError`.
   - WHY THEM OVER OTHER USE CASES: In Java, properties (fields) cannot be
     overridden, only methods. Kotlin allows you to keep the clean
     `cell.value = 5` syntax while doing complex polymorphic logic under the
     hood.
* */



/* ---- ---- ---- ----      ---- ---- ---- ----      ---- ---- ---- ----   */
/* ---- ---- ---- ----      ---- ---- ---- ----      ---- ---- ---- ----   */
/*
7. STRATEGY PATTERN vs. INHERITANCE
   Instead of creating massive trees of abstract classes and subclasses
   (Inheritance), you can just pass a function into a class to tell it how to
   behave (Strategy).

EXPLAIN BY EXAMPLE:
* */
// INHERITANCE WAY (Heavy/Rigid)
abstract class GridWorld { abstract fun handleOverrun() }
class DailyGridWorld : GridWorld() {
    override fun handleOverrun() = throw Exception("Dead")
}

// STRATEGY WAY (Lightweight/Flexible)
class GridWorld_(
    val handleOverrun: (Pair<Int, Int>) -> Pair<Int, Int>   // <--- Strategy Function
)

// Usage: Just pass a lambda function during creation!
val deadlyWorld_ = GridWorld_(handleOverrun = { throw Exception("Dead") })
val boundedWorld_ = GridWorld_(handleOverrun = { it -> Pair(0, 0) })


/*
   - WHEN DO I USE THEM: When you have a single class that needs to behave in
     slightly different ways, but creating a whole new subclass feels like
     overkill.
   - WHY DO I USE THEM: Composition over Inheritance. It prevents "Class
     Explosion" (where you end up with 50 subclass files). It allows you to
     change a strategy at runtime (e.g., passing a new function to change a
     game's rules mid-game). You cannot change an object's inherited subclass
     while the app is running.
   - WHEN DO I NOT USE THEM AND WHY: If the `DeadlyWorld` requires entirely
     different class properties, entirely different internal variables, and
     dozens of different methods compared to `BoundedWorld`, then passing a
     single Strategy function isn't enough. You should use standard inheritance.
   - WHY THEM OVER OTHER USE CASES: Kotlin treats functions as "first-class
     citizens". You can pass a lambda `{ ... }` directly into the constructor,
     saving you from writing dozens of boilerplate class files just to change
     one behavior.
* */




/* ---- ---- ---- ----      ---- ---- ---- ----      ---- ---- ---- ----   */
/* ---- ---- ---- ----      ---- ---- ---- ----      ---- ---- ---- ----   */













/* ---- ---- ---- ----      ---- ---- ---- ----      ---- ---- ---- ----   */
/* ---- ---- ---- ----      ---- ---- ---- ----      ---- ---- ---- ----   */












/* ---- ---- ---- ----      ---- ---- ---- ----      ---- ---- ---- ----   */
/* ---- ---- ---- ----      ---- ---- ---- ----      ---- ---- ---- ----   */













/* ---- ---- ---- ----      ---- ---- ---- ----      ---- ---- ---- ----   */
/* ---- ---- ---- ----      ---- ---- ---- ----      ---- ---- ---- ----   */