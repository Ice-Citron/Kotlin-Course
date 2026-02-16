/*
    It is completely normal that these concepts feel a bit weird coming from
    C++.

    In C++, if you define a class inside another class (e.g.,
    `class Outer { class Inner{}; };`, it is purely a scoping mechanism
    (`Outer::Inner`). The `Inner` class has NO IDEA about the actual instance of
    `Outer` unless you manually pass a pointer to it (like `Inner(Outer* ptr)`).

    Kotlin and Java handle nesting very differently. Let's break down both Inner
    Classes and Anonymous Objects with examples, rules and trade-offs.

    ---



    1. NESTED CLASSES vs. `inner` CLASSES

       In Kotlin, a standard nested class (no keyword) acts exactly like C++. It
       cannot access the outer class's member variables.

       But if you add the `inner` keyword, Kotlin does something magic under the
       hood: IT GIVES THE INNER CLASS A HIDDEN POINTER (a `this` reference) to
       the specific instance of the outer class that created it.



        THE EXAMPLE
           Imagine a `Network` that has a private `isConencted` state.
```Kotlin
class Network(val name: String) {
    private var isConnected = true

    // 1. NESTED CLASS (No `inner` keyword) - Like C++
    class Packet(val payload: String) {
        fun printInfo() {
            // ERROR: Cannot access `name` or `isConnected`.
            // A Packet doesn't know WHICH Network created it!
        }
    }

    // 2. INNER CLASS (Has `inner` keyword)
    // Because of `inner`, every Connection holds a hidden reference to its
    // parents Network
    inner class Connection {
        fun checkStatus() {
            // SUCCESS! We can access the outer class's private state directly!
            // Under the hood, the compiler translates this to
            // `this@Network.isConnected`
            if (isConnected) println("Connected to $name")
        }
    }
}

fun main() {
    // 1. You create a Nested class using the Class name (no instance needed)
    val myPacket = Network.Packet("data")

    // 2. You MUST use an instance of the outer class to create an Inner class
    val myNetwork = Network("HomeWiFi")
    val myConnection = myNetwork.Connection()
    myConnection.checkStatus()
}
```

    WHEN & WHY TO USE `inner`
    - WHEN: When the nested object logically "belongs" to a specific instance of
      the outer class AND absolutely must read or modify the outer class's
      private state to do its job.
    - WHY OVER ALTERNATIVES: It saves you from writing C++ style boilerplate
      where you manually pass `this` into the constructor and store it as a
      member variable. Kotlin wires it up safely for you.

    WHEN NOT TO USE `inner`
    - WHEN: The nested class is just a static helper data structure (like the
      `Packet` above, or a `Node` inside a `LinkedList`). A `Node` just holds
      data and a pointer to the next node; it doesn't need to know the overall
      `size` of the list.
    - Why NOT (The Danger): MEMORY LEAKS AND BLOAT! Because of that hidden
      pointer, if an `inner` class instance is passed around and lives longer
      than the outer class, the garbage collector cannot destroy the outer
      class. Furthermore, if you have 1 million `Node`s, making them `inner`
      means you are storing 1 million redundant pointers to the outer list in
      memory. If it doesn't need outer state, omit `inner`.



---
2. ANONYMOUS OBJECTS (`object : Interface`)
   In C++, if you have an interface (a class with pure virtual functions) and
   you need an object that implements it, you must define a whole new named
   class, inherit from the interface, implement the methods, and then
   instantiate it.

   In Kotlin, if you only need an implementation EXACTLY ONCE, you can skip
   naming the class entirely using an OBJECT EXPRESSION. It literally means:
   "Create an instance of a nameless class that implements this interface right
   here, right now."


THE EXAMPLE
    Imagine you have an interface for handling UI clicks.

```Kotlin
interface ClickListener {
    fun onPress()
    fun onRelease()
}

class Button {
    fun setListener(listener: ClickListener) { ... }

    fun setup() {
        // We need a listener right here, but we don't want to create a whole
        // new class just for this one specific button's behavior.

        // We create an anonymous object directly inline:
        setListener(object: ClickListener {
            override fun onPress() { println("Button pressed down") }
            override fun onRelease() { println("Button released") }
        })
    }
}
```

   Crucial Detail: If you create an anonymous object inside a method of an outer
   class (like `setup()` above), it automatically acts exactly like an `inner`
   class--it captures a hidden pointer to the outer class so it can access
   private outer variables!


   WHEN && WHY TO USE ANONYMOUS OBJECTS
      - WHEN: You need an object that implements an interface (or inherits from
        an abstract class), but you will NEVER NEED TO INSTANTIATE IT A SECOND
        TIME anywhere else in your codebase.
      - WHY OVER ALTERNATIVES:
         1. ZERO NAMESPACE POLLUTION: You don't have to invent useless names
            like `MySpecificButtonClickListener` for a class you will never
            reuse.
         2. ULTIMATE ENCAPSULATION: Because the class literally has no name, it
            is mathematically impossible for another programmer to accidentally
            instantiate it somewhere else and cause bugs.

   WHEN NOT TO USE ANONYMOUS OBJECTS:
      - WHEN YOU NEED MULTIPLE INSTANCES: You cannot reuse an anonymous object
        (you'd have to copy-paste the block). If you need it twice, give it a
        name and make it an `inner class`.
      - WHEN THE CODE IS HUGE: If the implementation takes 50+ lines of code, a
        huge block of nameless object code makes the surrounding function
        incredibly hard to read. Move it to a named class.#
      - WHEN THE INTERFACE ONLY HAS ONE METHOD: If an interface only has one
        method, Kotlin allows you to just pass a `{ lambda }` (similar to
        `std::function`), which is much cleaner. We use `object` for things like
        `Iterator` because it has two methods (`hasNext` and `next`), so a
        single lambda won't work.









               .
              /=\\
             /===\ \
            /=====\' \
           /=======\'' \
          /=========\ ' '\
         /===========\''   \
        /=============\ ' '  \
       /===============\   ''  \
      /=================\' ' ' ' \
     /===================\' ' '  ' \
    /=====================\' '   ' ' \
   /=======================\  '   ' /
  /=========================\   ' /
 /===========================\'  /
/=============================\/


            #####
        ######
     ########
   ########             *
  ########
 #########
 #########     *
#########
#########
#########                  *
 ########
  #########      *
   ########
    ########
      ########          *
         ######
             #####





TYING IT BACK TO THE LECTURE (The `Iterator` evolution)
   The lecture takes you through an evolution to show why Anonymous Objects are
   the ultimate final form for an `Iterator`.
      1. FIRST ATTEMPT (C++ style): Make a standalone named class:
         `class ArrayListIterator(val list: List)`.
      2. SECOND ATTEMPT (`inner` class):
         Make a `private inner class ArrayListIterator`.
         - Problem: It automatically accesses the list's array (Great!), but
           it still has a name. Another programmer could accidentally write
           `ArrayListIterator().next()` somewhere else inside the list class and
           create a bug.
```Kotlin
class ResizingArrayList<T> {
    private var size = 10
    private var elements = arrayOf(...)

    override fun iterator(): Iterator<T> {
        // return a nameless object that implements Iterator.
        // It acts like an inner class, so it can see `size` and `elements`
        // directly!
        return object : Iterator<T> {
            private var index = 0
            override fun hasNext() = index < size
            override fun next() = elements[index++]
        }
    }
}
```

   By using an anonymous object, the iterator's state and logic are perfectly
   encapsulated right where they are needed, with zero risk of misuse.





    Now that you understand how to elegantly create an iterator using an
    anonymous object, let's look at the payoff.



PART 3: THE POWER OF `for-in` LOOPS AND FUNCTIONAL EXTENSIONS.
   This final section shows how Kotlin uses your newly created `Iterator` to
   unlock powerful language features and functional programming patterns.


1. THE LINKED LIST ITERATOR (S 45-46)
   Just like the array list, we use an ANONYMOUS OBJECT to implement the
   iterator for the `SinglyLinkedList`. Instead of an integer index, it tracks
   the C++ equivalent of a node pointer:

```Kotlin
class SinglyLinkedList<T> : ImperialMutableList<T> {

    private var head: Node<T>? = null

    override fun iterator(): Iterator<T> = object : Iterator<T> {
        // Starts at the 'head' of the outer SinglyLinkedList.
        // Because it's an anonymous object, it directly accesses the private
        // `head`!
        private var nextNode: Node<T>? = head

        override fun hasNext(): Boolean = nextNode != null

        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()

            val result = nextNode!!.element     // Grab the data
            nextNode = nextNode!!.next          // Advance the pointer
            return result
        }
    }
}
```

   2. FIXING THE $O(N^2)$ BUG (S 47-48)
      Now we can fix the terrible performance of our `combine` function (which
      merges two lists). Initially, we were using a `for` loop with $O(N)$
      `get(index)` calls, resulting in quadratic time for linked lists. Now, we
      use the iterator to achieve $O(1)$ sequential access:
```Kotlin
val iterator = first.iterator()
while (iterator.hasNext()) {
    result.add(iterator.next())     // O(1) retrieval per step!
}
```

      The Catch: This manual C++ style `while` loop syntax is clunk and
      error-prone. If you accidentally call `iterator.next()` twice inside the
      loop, you skip elements and risk crashing the program if you hit the end
      of the list.


3. THE `for-in` LOOP MAGIC (S 49-53)     [CORE KOTLIN IDIOM]
   In C++, if you define `begin()` and `end()` for a class, you unlock the
   range-based for loop (`for (auto& x: container)`). Kotlin has the exact same
   concept, but it looks for the `iterator()` method.

   THE RULE: If a class provides a method named `iterator()` that returns an
   `Iterator<T>`, and you mark it with the `operator` keyword, Kotlin allows
   you to use the `for (element in collection)` syntax!

```Kotlin
interface ImperialMutableList<T> {
    // The `operator` keyword is the magic bridge!
    operator fun iterator(): Iterator<T>
}
```

   Now, the compiler automatically translates this beautiful, safe syntax:

```Kotlin
for (element in first) {
    result.add(element)
}
```
   ... into the clunky `while (iterator.hasNext())` loop under the hood.

   This instantly solves our $O(N^2)$ `combine` function and allows us to easily
   fix the `addAll` default method in our interface safely, guaranteeing linear
   time regardless of whether the underlying data structure is an Array or a
   Linked List.



4. FUNCTIONAL EXTENSIONS: Map, Filter, Zip, Reduce (S 54-66)
   Because every list now has an efficient iterator, we can build standard
   functional operations (similar to C++ `<algorithm>` and `<numeric>`
   headers like `std::transform`, `std::copy_if`, or `std::accumulate`) as
   EXTENSION METHODS.

   The slides present these as exercises to write in a separate file. Because
   you are extending `ImperialMutableList`, you don't know the underlying data
   structure--but thanks to the iterator via the `for` loop, you don't care!
   Here is what they look like idiomatically in Kotlin:
      - `map` (Transforms `T` to `U` - like `std::transform`):

```Kotlin
fun <T, U> ImperialMutableList<T>.map(transform: (T) -> U): ImpperialMutableList<U> {
    val result = SinglyLinkedList<U>()
    for (item in this) {        // `this` implicitly uses your iterator!
        result.add(transform(item))
    }
    return result
}
```

   - `filter` (Keeps items matching a condition - like `std::copy_if`):

```Kotlin
fun <T> ImperialMutableList<T>.filter(predicate: (T) -> Boolean): ImperialMutableList<T> {
    val result = SinglyLinkedList<T>()
    for (item in this) {
        if (predicate(item)) result.add(item)
    }
    return result
}
```


    - `reduce` (Accumulates a single value - like `std::accumulate`):
      Note: We can't use a `for` loop easily here because we need the very first
      element to serve as the starting total. So we drop down to the manual
      iterator.
```Kotlin
fun <T> ImperialMutableList<T>.reduce(accumulator: (acc: T, item: T) -> T): T {
    val iterator = this.iterator()
    if (!iterator.hasNext()) throw NoSuchElementException("Empty list")

    var total: T = iterator.next()     // Grab the first element
    while (iterator.next()) {
        total: T = accumulator(total, iterator.next())
    }
    return total
}
```
   (Note: Kotlin's standard library already provides highly optimised versions
   of `map`, `filter`, `zip`, and `reduce` for its built-in collections out of
   the box. These exercises just show you how they are built under the hood
   using Iterators.)







---
SUMMARY OF T2L6
   You've just leveled up your Kotlin architecture! You now know:
   1. ITERATORS: State machines (`hasNext()` and `next()`) that solve the
      $O(N^2)$ Linked List traversal trap.
   2. INNER CLASSES: Nested classes marked with `inner` that holds a hidden
      `this` pointer to their outer instance.
   3. ANONYMOUS OBJECTS: The cleanest way to implement single-use interfaces
      (`object : Iterator`) securely without polluting the namespace.
   4. `operator fun iterator()`: This specific signature required to unlock
      Kotlin's native `for (x in list)` syntax.
   5. FUNCTIONAL EXTENSIONS: How iterators serve as the backbone for generic
      operations like `map`, `filter`, and `reduce`.



PART 1 (S2-34): INHERITANCE BASICS, `open`, and VISIBILITY MODIFIERS
   For a C++ developer, Kotlin's inheritance model will feel conceptually
   familiar, but with one massive philosophical difference: Kotlin is strictly
   "closed" by default. You must explicitly grant permission for a class to be
   inherited or a method to be overridden.

1. THE `open` KEYWORD (Final-by-Default) (S2-15)
   In C++, you can inherit from any class unless it is explicitly maked `final`.
   In Kotlin, classes are locked down by default to prevent the "fragile base
   class" problem.

   To allow a class to be subclassed, you must mark it wih the `open` keyword.
   Similarly, methods in Kotlin are non-virtual (`final`) by default. To allow
   a subclass to override a method, you must mark the base method as `open`
   (like C++ `virtual`).

```Kotlin
// `open` means "this class can be inherited from"
open class Lamp(private var isOn: Boolean) {

    // `open` means this method can be overridden (dynamic dispatch)
    open fun pressSwitch() {
        isOn = !isOn
    }
}
```



2. EXTENDING A CLASS AND OVERRIDING METHODS (S 16-18)
   To inherit from a class, you use a colon `:` followed by the base class
   constructor invocation (this acts exactly like a C++ member initializer
   list calling the base constructor).

   When overriding an `open` method, the subclass must explicitly use the
   `override` keyword (similar to C++11's `override`, but strictly mandatory).
   You use `super` just like you do in C++ or Java to invoke the base class
   implementation.

```Kotlin
// DimmingLamp inherits from Lamp.
// We pass `isOn` directly up tot he Lamp constructor.
class DimmingLamp(isOn: Boolean) : Lamp(isOn) {
    private var brightness: Int = if (isOn) 10 else 0

    // Must use the `override` keyword!
    override fun pressSwitch() {
        super.pressSwitch()         // Calls Lamp's pressSwitch()
        brightness = if (isOn) 10 else 0
    }
}
```


3. VISIBILITY AND `protected` (S 19-27) [IDIOMATIC KOTLIN]
   Just like C++, a subclass cannot access `private` members of its base class.
   If `DimmingLamp` needs to know if the lamp `isOn`, making it `public`
   destroys encapsulation.

   Kotlin provides the exact same `protected` keyword as C++: visible only to
   the class and its subclasses.


THE "KOTLIN WAY" FOR ENCAPSULATION:
   If `Lamp` makes `isOn` pure `protected`, `DimmingLamp` can read it, but it
   can also write to it (e.g., `isOn = true`), potentially bypassing internal
   logic. If you want a subclass to only read a property, Kotlin allows you to
   split the visibility of the getter and the setter!

```Kotlin
open class Lamp(isOn: Boolean) {
    // Subclasses can READ `isOn`, but ONLY Lamp can WRITE to it.
    protected var isOn: Boolean = isOn
        private set
}
```

   This is highly idiomatic and saves you from writing clunky
   `protected boolean getIsOn()` boilerplate.



4. INHERITANCE TERMINOLOGY (S 28-31)
   Just a quick terminology alignment (Kotlin uses standard OOP terms
   interchangeably):
   - SUPERCLASS: Base class, Parent class
   - SUBCLASS: Derived class, Child Class
   - Inheritance is TRANSITIVE (if C extends B, and B extends A, C is an
     indirect subclass of A).
   - Subclasses inherit all `public` and `protected` members.


5. INTRO TO GridWorld (S 32-34)
   This section closes by visually introducing a 2D game called `GridWorld`. A
   skeleton player navigates a grid with various terrains (Water, Rocks, Forest,
   Swamp). This visually sets up the architecture problem we will solve in the
   next section: How do we cleanly program what happens when the player walks
   off the edge of the map?














PART 2 (S 35-66): THE EXTENSIBILITY PROBLEM AND ABSTRACT CLASSES.
   (Note: Since this lecture is exactly 66 long, this second part will take us
   all the way to the end of this document!)

   This section tackles a classic architectural problem: How do you write logic
   that handles multiple different behaviors without writing massive,
   unmaintainable `switch` statements.

   For a C++ developer, you will immediately recognise the progression from an#
   `enum`/`switch` to a virtual method, and finally to a pure virtual function
   (`= 0`).



1. THE "ENUM ANTI-PATTERN" (S 35-46)
   Imagine `GridWorld` can have different behaviors when the player walks off
   the edge:
      - BOUNDED: Hitting the edge does nothing (blocks movement).
      - DEADLY: Hitting the edge throws an exception (kills the player).
      - RANDOM: Hitting the edge teleports the player randomly.

   A novice approach is to pass an `enum` into the `GridWorld` class and use a
   `when` statement (Kotlin's `switch`):
```Kotlin
enum class WorldKind { BOUNDED, DEADLY, RANDOM }

class GridWorld(private val worldKind: WorldKind, ...) {
    private fun updatePosition(newPosition: Pair<Int, Int>) {
        // ... if off grid:
        when (worldKind) {
            WorldKind.BOUNDED -> return     // Do nothing
            WorldKind.DEADLY -> throw DeadPlayerException("Fell off world!")
            WorldKind.RANDOM -> position = randomPosition()
        }
    }
}
```

   THE FATAL FLAW: What if you want to add a `Torus` world (wrapping around the
   edges like Pac-Man)? You are forced to open up the core `GridWorld` class,
   add to the enum, and add a branch to the `when` statement. This violates the
   OPEN-CLOSED PRINCIPLE (classes should be open for extension, but closed
   for modification).


2. THE FIRST INHERITANCE ATTEMPT (S 47-54)
   To make it extensible, we drop the enum, mark `GridWorld` as `open`, and
   delegate the specific logic to subclasses via an `open` method. Because the
   base class doesn't know what to do, it provides a "dummy" implementation.

```Kotlin
open class GridWorld(...) {
    // Virtual function with a "dummy" default implementation
    protected open fun handleOverrun(newPosition: Pair<Int, Int>)
        : Pair<Int, Int> = throw NotImplementedError("
                                This method should be provided by subclasses")
}
```
   Subclasses like `BoundedGridWorld` then inherit this and `override` the
   method to provide their specific behavior.




KOTLIN TIP: Narrowing to `Nothing`




















* */








