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

- On type inference: Kotlin
 */

/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */


fun main() {
    println(distanceBetween(Pair(3, 2), Pair(5, 20)))

    println(5.squared())
    println(5.squared())
}