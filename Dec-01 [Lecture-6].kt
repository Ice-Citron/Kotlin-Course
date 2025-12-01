
/* ------ -SINGAPORE-- ------ ------      ---123- ------       ------ ------ */
// equals() -- TWO KINDS OF EQUALITY
/*
    There are two different questions you can ask about two objects:
    - REFERENTIAL EQUALITY (===): "Are these the exact same object in memory?"
    - STRUCTURAL EQUALITY (==): "Do these objects contain the same data?"
 */

/*
Analogy: Identical Twins

    - Imagine identical twins, Alice and Alice-clone. They look exactly the
      same, have the same height, same eye color, same everything.
      - Referential equality (===): "Are you pointing at the same physical
        person?" No--there are two separate humans.
      - Structural equality (==): "Do they have the same properties?" Yes--same
        name, same appearance, same everything.
 */

class Person10(val name: String, val age: Int)

val alice10 = Person10("Alice", 25)
val alice11 = Person10("Alice", 25)
val alice12 = alice10       // same reference

/*
fun main() {
    // Referential -- same object in memory?
    alice10 === alice11       // false - two different objects
    alice11 === alice12       // true - same object

    // Structural -- same content?
    alice10 == alice11        // false (by default!) - we'll fix this
    alice10 == alice12        // true
}
 */

/*
    Wait, why is `alice10 == alice11` false? They have the same data!
 */


// THE DEFAULT `equals()` PROBLEM
/*
    By default, `==` calls `.equals()`, and the default `.equals()` just checks
    if it's the same object (same as `===`). It doesn't know how to compare your
    custom class's properties.
 */
class Person014(val name: String, val age: Int)

val a01 = Person014("Alice", 25)
val b01 = Person014("Alice", 25)

// a == b           // false -- default equals just checks reference
// a.equals(b)      // false -- same thing


/*
    You have to teach Kotlin how to compare your class by overriding `equals()`:
 */
class Person016(val name: String, val age: Int) {
    override fun equals(other: Any?): Boolean {
        if (other !is Person) return false
        return name == other.name && age == other.age
    }
}

val a016 = Person016("Alice", 25)
val a016 = Person016("Alice", 25)

/* ------ -SINGAPORE-- ------ ------      ---123- ------       ------ ------ */
/* ------ -SINGAPORE-- ------ ------      ---123- ------       ------ ------ */
/* ------ -SINGAPORE-- ------ ------      ---123- ------       ------ ------ */
/* ------ -SINGAPORE-- ------ ------      ---123- ------       ------ ------ */


