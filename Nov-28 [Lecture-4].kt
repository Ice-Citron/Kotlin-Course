/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */
// ENUMS

/*
    - Write an enumerated data type to represent the days of the week
 */
enum class DaysOfWeek{
    MON, TEU, WED, THU, FRI, SAT, SUN
}

/*
    - Add properties to the enumerated objects
    - Add a method that checks if weekend
 */
enum class DaysWithProperties(val fullname: String) {
    MON("Monday"),
    TEU("Tuesday"),
    WED("Wednesday"),
    THU("Thursday"),
    FRI("Friday"),
    SAT("Saturday"),
    SUN("Sunday");

    fun weekendCheck(): Boolean =
        if (this.fullname == "Saturday" || this.fullname == "Sunday") true
        else false
}


enum class Month(val x: Int) {
    JAN(1), FEB(2), MAR(3), APR(4), MAY(5), JUN(6),
    JUL(7), AUG(8), SEP(9), OCT(10), NOV(11), DEC(12);

    fun isDuringAcademicYear(): Boolean = when {
        x == 10 || x == 11 || x == 12 || x == 1
                || x == 2 || x == 3 || x == 4 || x == 5 || x == 6 -> true
        else -> false
    }

    fun isShortMonth(): Boolean = when {
        x == 2 || x == 4 || x == 6 || x == 9 || x == 11 -> true
        else -> false
    }
}

/*
fun main() {
    println(DaysOfWeek.MON)
    println(DaysWithProperties.MON.weekendCheck())

    print(Month.JAN)
}
 */

/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */

/*
fun main() {
    // IMMUTABLE LISTS
    /*
        By default, `listOf()` creates an immutable list--you can read from it
        but can't modify it.
     */
    val listA = listOf("Kotlin", "is")

    // Accessing elements
    val element = listA.get(1)      // "is"
    val element2 = listA[1]         // "is" -- same thing, cleaner syntax

    // Can't modify
    // listA.add("great")           // ERROR -- no such method
    // listA[0] = "Java"            // ERROR -- can't assign


    /*
        Since you can't modify an immutable list, operations return new lists:
     */
    val list1 = listOf("Kotlin", "is")
    val list2 = listA.plus("GREAT")
                    // new list: ["Kotlin", "is", "GREAT]
    val list3 = listA + "INTERESTING"
                    // same thing, using operator

    println(list1)      // ["Kotlin", "is"] -- unchanged
    println(list2)      // ["Kotlin", "is", "GREAT"]

    /*
        This is the FP approach--original data is never mutated.
     */


/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */
    // MUTABLE LISTS
    /*
        If you need to modify a list in place, use `mutableListOf()`:
     */
    val listC = mutableListOf("Kotlin", "is")

    // Now you can modify it
    listC.add("GREAT")      // adds to the end ["Kotlin", "is", "GREAT"]
    listC.add(2, "really")
                // inserts at index 2: ["Kotlin", "is", "really", "GREAT"]


    println(listC)      // ["Kotlin", "is", "really", "GREAT"]
    /*
        Notice `listC` is `val` but we're still modifying it. Remember: `val`
        means the reference can't change (listC can't point to a different list)
        , but the list's contents can change.

            ```kotlin
            val listC = mutableListOf("Kotlin", "is")
            listC.add("GREAT")        // OK — modifying contents
            listC = mutableListOf()   // ERROR — can't reassign val
            ```
     */

    // More mutable operations:
    listC.remove("GREAT")       // removes first occurence
    listC.removeAt(0)             // removes by index
    listC[1] = "awesome"                // replace element at index

    val listD = listC.plus("easy")  // still returns NEW list
    /*
            ```kotlin
            listD.removeAt(2)                       // modifies listD in place
            ```
        Code above doesn't work because. The issue is that `.plus()` from line
        122 always returns an *immutable* `List<T>`, even when called on a
        `MutableList`.

    ```kotlin
    val listC = mutableListOf("Kotlin", "is", "GREAT")    // MutableList<String>
    val listD = listC.plus("easy")      // List<String> - immutable!

    listD.removeAt(2)       // ERROR - listD is not mutable
    ```

        The return type of `plus` is `List<T>`, not `MutableList<T>`. So even
        though `listC` is mutable, the new list is creates isn't.
     */
    // To fix it, convert to mutable:
    val listE = listC.plus("easy").toMutableList()
    listE.removeAt(2)

    // Or add directly to a mutable list:
    val listF = listC.toMutableList()
    listF.add("easy")
    listF.removeAt(2)


/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */
    // WHEN TO USE WHICH?
    /*
        Prefer immutable (`listOf`) by default. Use mutable (`mutableListOf`)
        only when you have a good reason:
     */
    // Immutable - safe, predictable
    val scores = listOf(85, 90, 78)
    val updated = scores + 95           // new list created

    // Mutable - when building up a new list incrementally
    val results = mutableListOf<Int>()
    for (i in 1..10) {
        results.add(i)
    }

    println(listC::class)
}
 */


/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */
// SETS
/*
    A set is a collection with no duplicate and no guaraneteed order:
 */

fun main() {
    val setA = setOf("A", "B", "C", "D", "A", "C")

    println(setA.size)      // 4 -- duplicates removed
    println(setA)           // [A, B, C, D] -- each element appears once

    /*
        Even though we added "A" and "B" twice, the set only keeps one of each.
     */



}

/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */
/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */

