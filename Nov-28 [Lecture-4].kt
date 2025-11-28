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

/*
fun main() {
    val setA = setOf("A", "B", "C", "D", "A", "C")

    println(setA.size)      // 4 -- duplicates removed
    println(setA)           // [A, B, C, D] -- each element appears once

    /*
        Even though we added "A" and "B" twice, the set only keeps one of each.
     */
    // Checking m embership is fast (O(1) average)
    println("A" in setA)    // true
    println("Z" in setA)    // false

    // Set operations
    val set1 = setOf(1, 2, 3)
    val set2 = setOf(2, 3, 4)

    set1 union set2         // {1, 2, 3, 4}
    set1 intersect set2     // {2, 3}
    set1 subtract set2      // {1}

    /*
        Like lists, there's also `mutableSetOf()` if you need to modify it:
     */
    val mutableSet = mutableSetOf("A", "B")
    mutableSet.add("C")
    mutableSet.remove("A")


/* ------ ------ ------ ------     ------ ------ ------ ------     ------  */
    // LISTS WITH MAP AND `it`
    /*
        The `map` function transforms every element in a list:
     */
    val nums = listOf(1, 2, 3, 4, 5, 6)
    fun square(x: Int): Int = x * x

    // Three ways to write the same thing:

    // 1. Function reference
    nums.map(::square)

    // 2. Explicit lambda parameter
    nums.map { x -> x * x}

    // 3. Using 'it' - implicit parameter name. other var name seem to work too
    nums.map { it * it }


    /*
        The problem is your `listOf(1..6)` creates a list containing one
        element--the range itself, not the numbers.

    ```kotlin
    val nums = listOf(1..6)    // List<IntRange> - one element: the range object
    val nums = (1..6).toList() // List<Int> - six elements: [1, 2, 3, 4, 5, 6]
    ```

        So when you try to map `square` over it, you're trying to square a range
        , not integers.

        Fix:
    ```kotlin
    val nums = (1..6).toList()
    fun square(x: Int): In = x * x
    nums.map(::square)      //  [1, 4, 9, 16 ...]
    ```
     */


/* ------ ------ ------ ------     ------ ------ ------ ------     ------  */
    // WHAT DOES `::square` look like?

    /*
        It's a reference to a function. You can see its type:
     */
    fun square2(x: Int): Int = x * x
    val f: (Int) -> Int = ::square2     // f holds a reference to the function

    // These are all EQUIVALENT:
    square2(5)       // call directly
    f(5)               // call via reference
    ::square2.invoke(5)     // explicit invoke

    // You can pass it around
    nums.map(::square)
    nums.map(f)
    nums.map { x -> square(x) }


/* ------ ------ ------ ------     ------ ------ ------ ------     ------  */
    // WHAT IS `it`?
    /*
        When a lambda has exactly one parameter, you can skip naming it and use
        `it` instead:
     */
    // These are equivalent
    val g1: (Int) -> Int = {x -> x * 2}
    val g2: (Int) -> Int = { it * 2}
    val g3: (Int) -> Int = { x * 2}

    /*
        It's just shorthand. Use it when the meaning is obvious; use explicit
        names when clarity helps:
     */
    // `it` is fine here -- obviously the number
    nums.filter { it > 3 }

    // Explicit name is clearer here
    class Person2(val age: Int, val city: String)

    val people = mutableListOf<Person2>()
    people.filter { person -> person.age > 18 && person.city == "London" }


/* ------ ------ ------ ------     ------ ------ ------ ------     ------  */
    // MORE COLLECTION OPERATIONS
    val nums1 = listOf(1, 2, 3, 4, 5, 6)

    // filter - keep elements matching a condition
    nums1.filter { it > 3}
    nums1.filter { it % 2 == 0 }

    // map - transform each element
    nums1.map { it * 2 }

    // chaining
    nums1.filter {it > 2}
        .map { it * it }

    // reduce - combine all elements
    nums1.reduce { acc, x -> acc + x}

    // fold - like reduce but with initial value
    nums1.fold(0) { acc, x -> acc + x}
    nums1.fold(1) { acc, x -> acc * x}

    // other useful ones
    nums1.first()
    nums1.last()
    nums1.take(3)       // [1, 2, 3]
    nums1.drop(3)       // [4, 5, 6]
    nums.any { it > 5}     // true
    nums.all { it > 0}     // true
    nums.none { it < 0}    // true
}
 */

fun lengths2(s: List<String>): List<Int> {
    val lista = mutableListOf<Int>()
    for (i in s) lista.add(i.length)
    return lista
}

fun complements2(x: List<Int>): List<Pair<Int, Int>> {
    val lista = mutableListOf<Pair<Int, Int>>()
    for (i in x) lista.add(i to 10-i)
    return lista
}

val pairs3 = listOf(1 to 9, 3 to 4, 5 to 5)
fun matchingTotal3(total: Int, pairs: List<Pair<Int, Int>>)
        : List<Pair<Int, Int>>  {
    return pairs.filter { it -> (it.first + it.second == total) }
}

fun main() {
    print(lengths2(listOf("hello", "jbshaxasbhj", "raemus", "cd")))
}
/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */
/* ------ ------ ------ ------ ------ ------ ------ ------ ------ ------ */

