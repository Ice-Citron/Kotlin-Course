/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    Safe Casts (`as?`): This operator attempts to cast a variable to a given
    type and returns null if the cast is not possible.

    Explicit Casts (`as`): This operator casts a variable to a given type, but
    throws a ClassCastException if the cast is not possible.
* */



/*
    It's completely understandable to feel that "multilingual" brain fog. In
    C++ and Java, the default behavior for nested class is often the opposite
    of Kotlin, which is usually the source of confusion.

    In Kotlin, there are two main ways to put a class inside another class. The
    difference depends entirely on whether you want the "inner" part to be able
    to talk to the "outer" part.


1. NESTED CLASS (The Default)
    If you just write a class inside another class, it is a NESTED CLASS.

    - RELATIONSHIP: It is "decoupled". It belongs to the name of the outer class
      , but it does NOT have access to the outer class's variables or functions.
    - ANALOGY: a house (Outer) and a shed in the yard (Nested). The shed is on
      the property, but it doesn't know what's happening inside the house's
      kitchen.
    - WHY USE IT? To group logic together without creating a "memory leak" or
      unnecessary dependency.


* */
class Outer36 {
    private val secret: String = "Top Secret"

    class Inner36 {
        // val mySecret = secret        // ERROR! ...
    }
}



/*
2. INNER CLASS (THE `inner` KEYWORD)
    If you add the `inner` keyword, it becomes an INNER CLASS.

    - RELATIONSHIP: It carries a reference to the specific instance of the outer
      class that created it. It CAN see all `private` properties of the outer
      class.
    - ...
    - WHY USE IT? This is exactly whjat you need for ...
* */

class Outer57 {
    val mySecret = "TOP SECRET FILE"
    inner class Inner57 {
        val secret = mySecret
    }
}


/*
3. ANONYMOUS  OBJECTS (The "Spec Preferred" way)
    Your lab spec ... mentions that returning an instance of an ANONYMOUS
    OBJECT is preferable for your `iterator()`.
       - These act like INNER CLASSES: they have full access to the outer
         class's properties, but you don't even have to give the class a name.
* */











/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */




















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    A TYPE ALIAS in Kotlin provides an alternative name for an existing type
    without creating a new one. Think of it as a nickname: the compiler treats
    the alias and the original type as identical, but the alias makes your code
    much more readable, especially when dealing with complex generics or
    high-order functions. It is particularly useful for shortening long function
    signatures or specific map entries that you can repeatedly use across your
    project.

    For example, instead of writing out a complex coordinate map every time,
    you can alias it:

```Kotlin
// Define the alias at the top level
typealias CoordinateMap = ListBasedMap<Int, Int>

// Now you can use the nickname instead of the long version
fun findTarget(map: CoordinateMap) {
    ...
}
```

    Behind the scenes, the compiler just swaps `CoordinateMap` back to
    `ListBasedMap<Int, Int>`, so there is zero performance overhead.s
* */




/*
        An alias on a Mac is a small file that acts as a clickable shortcut to
        a real application, file, folder, or disk, allowing quick access from
        anywhere without moving the original file. ...
* */



























/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */






























/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */