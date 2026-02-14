/*
1. THE STANDARD WAY (What you likely need)
   This is the pattern used in `ArrayList` implementations and what your lecture
   slides use. You create an array of nulls (typed as `Any?`) and cast it to
   your generic type `Array<T?>`.

```Kotlin
class ResizingArrayList<T>(initialCapacity: Int) {

    // The backing field.
    // We start with an array of nulls (Any?) and cast it to Array<T?>.
    // The `?` is critical because the empty slots are literally null.
    @Suppress("UNCHECKED_CAST")     // Optional: Hides the warning
    private var elements: Array<T?> =
        arrayOfNulls<Any?>(initialCapacity) as Array<T?>

    fun add(element: T) {
        // ... resizing logic ...
        elements[size] = element
        size++
    }
}
```




2. THE "COPY" WAY (resizing)
   When you resize, you do the same thing: create a new generic array, cast it,
   and copy data over.
```Kotlin
private fun resize(newCapacity: Int) {
    // 1. Create new bigger array of "Any?"#
    // 2. Cast it to "Array<T?>" so it fits our variable
    val newElements: Array<T?> = arrayOfNulls(newCapacity) as Array<T?>

    // 3. Copy manually (or use System.arraycopy / copyInto)
    for (i in 0 until size) {              // i in 0..<size
        newElements[i] = elements[i]
    }

    elements = newElements
}
```





3. THE "REIFIED" WAY (Advanced / Optional)
    If you ever see `inline fun <reified T>`, this is the only time you can
    actually use `arrayOf<T>()` directly without the `Any?` trick. This is rare
    for class internals but common for helper functions.

```Kotlin
// This works because `reified` keeps the type T alive at runtime.
inline fun <reified T> createArray(size: Int): Array<T?> {
    return arrayOfNulls<T>(size)        // No cast needed!
}
```


   VERDICT:
   Your syntax `arrayOfNulls<Any?>(size) as Array<T?>` is CORRECT for standard
   generic classes where `T` is erased. You just have to live with the
   "Unchecked cast" warning (or suppress it).



    The `reified` keyword in Kotlin is a special feature that allows you to
    access type information at runtime within inline functions. Normally, due to
    Java's type erasure, generic type information is lost at runtime. But with
    reified, Kotlin gives you a way to work around this limitation.
* */





/*
what's the difference between an interface and abstract classes in kotlin? and
what are their C++/java equivalences meanwhile???
---
   This is a classic interview question and a crucial architectural distinction.
   Since you are building a mental model mapping C++ to Kotlin, let's break it
   down exactly like thath.

   THE CORE DIFFERENCE: STATE
   The single most important difference is STATE (variables/memory).
      - INTERFACE: Cannot store state. It defines BEHAVIOR ("I can fly"). It can
        have properties, but they cannot have "backing fields"--they must be
        calculated on the fly or overridden.
      - ABSTRACT CLASS: Can store state. It defines IDENTITY. It can have normal
        variables with memory allocated to them.




   ---
   1. THE COMPARISON TABLE

Kotlin `interface`
   State (Fields) - NO. (Properties must be stateless/abstract)
   Constructors   - NO. (Cannot be instantiated directly)
   Inheritance    - MULTIPLE. (Class `Dog : Run, Bark`)
   Method Bodies  - YES (Default implementations allowed)
   "Mental Model" - "This object has the CAPABILITY TO..."


Kotlin `abstract class`
   State (Fields) - YES. (Can hold variables/data)
   Constructors   - YES. (Can define `init {}` logic)
   Inheritance    - SINGLE. (Class `Dog : Animal`)
   Method bodies  - YES (Standard methods allowed)
   "Mental Model" - "This object IS A type of..."








2. THE LANGUAGE EQUIVALENTS

   C++ ANALOGY
   C++ does not have a dedicated `interface` keyword, so it makes this
   distinction using VIRTUAL FUNCTIONS.
      - KOTLIN INTERFACES $\approx$ C++ PURE VIRTUAL CLASS (Abstract Base Class
        with no data members.)
         - Contains only `virtual void foo() = 0;`
         - Used to enforce a contract
         - In C++, you can technically put data in a pure virtual class, but if
           you do, you are conceptually treating it as an abstract class.
      - KOTLIN ABSTRACT CLASS $\approx$ C++ ABSTRACT CLASS (Class with at least
        one pure virtual function + data members)
         - Contains `virtual void foo() = 0;` AND `int x;` AND a constructor.
         - Used to share common code and state between subclasses.



3. CODE EXAMPLE: THE "STATE" CONSTRAINT
   This is where the difference becomes real code.

   THE INTERFACE (CAPABILITY):
   Notice `val speed` has no memory. It's just a requirement.

```Kotlin
interface Drivable {
    // Abstract property (Implementer MUST provide this)
    val speed: Int

    // Default implementation (Stateless logic)
    fun drive() {
        println("Driving at ${speed} km/h")
    }
}
```


THE ABSTRACT CLASS (IDENTITY):
   Notice `val name` acts like a normal variable. It stores data.

abstract class Vehicle(val name: String) { // Constructor allowed!
    // State (Memory is allocated for this)
    var fuelLevel: Int = 100

    // Abstract method (Must be overridden)
    abstract fun refuel()

    // Standard method (Inherited)
    fun checkFuel() {
        println("${name} has ${fuelLevel} fuel")
    }
}


   THE CONCRETE CLASS (PUTTING IT TOGETHER):
```Kotlin
// Car IS-A Vehicle (1 parent), but CAN-DO Drivable (multiple interfaces allowed)
class Car(name: String) : Vehicle(name), Drivable {

    // From Interface: We must provide the value/accessor
    override val speed: Int = 120

    // From Abstract Class: We must implement the abstract function
    override fun refuel() {
        fuelLevel = 100         // Accessing state from the abstract parent
    }
}
```

   SUMMARY FOR YOUR MENTAL MAP
      - INTERFACE: Use when you want to describe WHAT SOMETHING CAN DO (e.g.,
        `Movable`, `Clickable`, `Serializable`). You don't care if it's a Car or
        a Button, as long as it has that capability.
      - ABSTRACT CLASS: Use when you want to build a hierarchy of WHAT SOMETHING
        is (e.g., `Animal` -> `Dog`, `Vehicle` -> `Car`) and they share common
        MEMORY/STATE (like `name`, `org`, `position`).









explain in detail how does interfaces work, it's key methods, key uses. when to
use it, etc.
explain through REAL CODE example when possible
            https://gemini.google.com/app/bd659ecdaa303cef

   Let's explore the concept of INTERFACES using the material from LECTURE 4.

   An interface is one of the most powerful tools in OOP because it separates
   WHAT something does from HOW it does it.


1. WHAT IS AN INTERFACE?
   Think of an interface as a CONTRACT or a PROMISE.
   - It lists a set of methods (functions) and properties.
   - It typically does NOT provide the code for them (these are called "abstract
     methods").
   - Any class that "implements" the interface MUST provide the actual code for
     every single method listed.

   THE ANALOGY:
      If you go to a restaurant, the MENU is the interface. It lists "Burger"
      and "Pasta". It doesn't tell you how the chef cooks it. You (the client)
      just knows that if you order a "Burger," you will get a burger.



2. WHY DO WE NEED THEM? (The Problem)
   Without interfaces, your code becomes rigid and repetitive.

   Imagine you have two different list classes:
      1. `SinglyLinkedList` (Chains of nodes)
      2. `ResizingArrayList` (Arrays that grow)

   You want to write a helper function `doesEitherContain` that checks if an
   item exists in two lists.


   WITHOUT INTERFACES:
   You have to write a separate function for every possible combination of lists!
      - `func(list1: Singly, list2: Singly)`
      ...
      - `func(list1: Array, list2: Singly)`

   This leads to massive duplication.



   WITH INTERFACES:
   You create one interface `ImperialMutableList`. Both classes impleemnt it.
   Now you only write ONE function:
      - `func(list1: ImperialMutableList, list2: ImperialMutableList)`


3. REAL CODE EXAMPLE
   Here is how you define, implement, and use an interface based on the
   lecture slides.

   STEP A: DEFINE THE INTERFACE (The Contract)
      We define `ImperialMutableList`. It tells us what a list should do, but
      has no body code (`{...}`).

```Kotlin
interface ImperialMutableList<T> {
    // 1. A property the class must have (read-access)
    val size: Int

    // 2. Methods the class must implement
    fun add(element: T)
    fun get(index: Int): T
    fun remove(element: T): Boolean
    fun clear()
    // ... other methods like set, removeAt, contains
}
```


STEP B: IMPLEMENT THE INTERFACE (The Provider)
   Now, our specific classes sign this contract. They use the `:` syntax to say
   "I implement this" and `override` to provide the logic.

```Kotlin
// The class promises to be an ImperialMutableList
class ResizingArrayList<T> : ImperialMutableList<T> {

    // Fulfilling the `size` promise
    override var size: Int = 0
        private set

    // Fulfilling the `add` promise
    override fun add(element: T) {
        // ... logic to resize array if needed
        // ... logic to insert element
        size++
    }

    // Fulfilling the `get` promise
    override fun get(index: Int): T {
        // ... logic to return element at index ...
        return elements[index]!!
    }

    // ... must implement ALL other methods from the interface ...
}
```



   Note: If `SinglyLinkedList` also implements this interface, it would provide
   completely different logic inside `add` and `get` (using nodes instead of
   arrays), but the method names remain identical.


   STEP C: Use the Interface (The Client)
      This is where the magic happens (Polymorphism). You write code that talks
      to the Interface, not the specific class.




   4. WHEN TO USE INTERFACES?
      1. MULTIPLE IMPLEMENTATIONS: When you have different ways of doing the
         same thing (e.g., `ArrayList` vs `LinkedList`, `TextBox` vs `Image`)
      2. DECOUPLING: When you want your "Client" code (like a
         `DocumentManager`) to not care about the specific details of the
         objects it manages.
      3. TESTING: You can easily swap in a "Fake" or "Mock" implementation of
         an interface during testing.


SUMMARY OF KEY KEYWORDS
   - `interface`: Defines the contract.
   - `:`: Used by a class to implement the interface.
   - `override`: Required before any property or function in the class that
     implements an interface member.





<<<
... that syntax is actually the required standard for GENERIC FUNCTIONS in
Kotlin!

In Kotlin, you must declare the type parameter `<T>` BEFORE the function name.
This allows the compiler to know that `T` is a generic placeholder before it
encounters it in the argument list or return type.


The structure is:
    `fun` + `<Type Parameters>` + `functionName` + `(arguments)`





<<<






... I will frame this in terms you already know.

In C++, you are used to ABSTRACT BASE CLASSES with PURE VIRTUAL FUNCTIONS to
enforce contracts. Kotlin Interfaces are essentially that, but cleaner and with
slightly different rules.


1. THE SETUP: "CLIENT" CODE (S3-4)
   The lecture introduces the concept of a "Client".
      - CLIENT: The code using your class.
      - SERVICE: The public methods your class exposes.

   C++ ANALOGY:
      Think of the INTERFACE as the `.h` (header) file that defines the `public`
      API. The CLASS is the `cpp` implementation. The client should only care
      about the `.h`.


<<<
   2. THE PROBLEM: COMBINATORIAL EXPLOSION (S5-11)
      We have two list implementations from previous lectures:
      1. `SinglyLinkedList<T>` (Node-based)
      2. `ResizingArrayList<T>` (Vector-based)

      We want to write a generic helper `doesEitherContain`.

   ATTEMPT 1: STRICT TYPES (S5)
```Kotlin
fun <T> doesEitherContain(
    first: SinglyLinkedList<T>,
    second: SinglyLinkedList<T>,
    element: T
): Boolean { ... }
```
   - ISSUE: This only works if both inputs are Linked Lists.
   - C++ Equivalent: `void(LinkedList<T>& a, LinkedList<T>& b)`. You can't pass
     a `std::vector` to this.


   ATTEMPT 2: Overloading Hell (S9-11)

   To support mixed inputs (Array + Linked, Linked + Array, Array + Array), you
   would need to write 4 DIFFERENT OVERLOADS of the same function.

   If you add a third list type (e.g., `DoublyLinkedList`), you now need 9
   OVERLOADS.

   This is $O(N^2)$ complexity just for maintaining helper functions.




>>>
   3. THE SOLUTION: THE INTERFACE (S12-14)
      We define a contract that both lists satisfy.

```Kotlin
interface ImperialMutableList<T> {
    val size: Int,
    fun add(element: T)
    fun get(index: Int): T
    // ... no function bodies here!
}
```

      C++ TRANSLATION:
      This is exactly like a pure abstract class:
```Kotlin
template <typename T>
class ImperialMutableList {
public:
    virtual int getSize() const = 0;
    virtual void add(T element) = 0;
    virtual T get(int index) = 0;
    virtual ~ImperialMutablelist() = default;
}
```
   - In Kotlin, methods without bodies are ABSTRACT by default.




>>>
   4. IMPLEMENTING THE INTERFACE (S 15-21)
      Now we modify our classes to sign this contract.

```Kotlin
class ResizingArrayList<T> : ImperialMutableList<T> {

    // 1. Implementing the Property
    // In C++, you'd implement getSize(). In Kotlin, you override the property.
    override var size: int = 0
        private set

    // 2. Implementing the Methods
    override fun add(element: T) {
        // ... implementation ...
    }

    override fun get(index: Int): T {
        return elements[index]!!
    }
}
```
   KEY CONSTRAINTS:
      1. `override` keyword: Mandatory in Kotlin (unlike C++ where it's optional
         but good practice).
      2. COMPLETENESS: You must implement every single method listed in the
         interface. If you miss one, the class remains abstract and cannot be
         instantiated.




5. THE PAYOFF: POLYMORPHISM (S23-25)
   Now we rewrite that helper function once.

```Kotlin
fun <T> doesEitherContain(
    first: ImperialMutableList<T>,      // <-- The Interface
    second: ImperialMutableList<T>,     // <-- The Interface
    element: T
): Boolean {
    return first.contains(element) || second.contains(element)
}
```

   WHY THIS ROCKS:
      - You can pass ...
      - This function doesn't know (or care) how the memory is managed under the
        hood. It just knows it can call `.contains()`.



6. INSTANTIATION RULES (S26-27)
   A classic "gotcha" for beginners (though obvious to a C++ dev):

   ILLEGAL:
   `val list = ImperialMutableList<String>()`       // Error!
      - WHY: You cannot instantiate an interface directly. It has no code! It's
        just a vtable (virtual function table) description.

   LEGAL:
```Kotlin
// Helper function returning the Interface type
fun <T> combine(...): ImperialMutableList<T> {
   val result = SinglyLinkedList<T>()       // Create a CONCRETE class
   // ... fill it ...
   return result        // Return it disguised as the interface
}
```
   - WHY: `SinglyLinkedList` is-a `ImperialMutableList`, so this upcasting is
     valid.



QUICK LOGIC CHECK (C++ Brain):
   In S27, the function `combine` returns `ImperialMutableList<T>`.

   Inside the function, it creates a `SinglyLinkedList`.

   If this were C++, returning a loal object by value like this would slice it
   or be messy without pointers/smart pointers.

   In Kotlin, EVERYTHING IS A REFERENCE (POINTER) implicitly. So when you return
   `result`, you are just returning a pointer to the heap object. No slicing, no
   memory leaks (GC handles it).



   This section (S 28-47) moves away from Lists and introduces a completely new
   scenario: a DOCUMENT MANAGER app. This example demonstrates exactly WHY
   interfaces are necessary for building scalable systems.


   1. THE SCENARIO (THE "BEFORE" WORLD)
      Imagine we are building an app like Microsoft Word, or Google Docs. A
      document contains different elements:
         - TextBox: Has width, height, and text limit.
         - Image: Has width, height, and filename.
         - Menu: Has width, height, and options.

      We need a `DocumentManager` class to store all these items and perform
      calculations (like finding the tallest item on the page).



   THE PROBLEM: DUPLICATION NIGHTMARE
      Without an interface, our `DocumentManager` has to know the specific
      details of every single class.



      ...


      We realise that despite their differences, `TextBox`, `Image`, and `Menu`
      all share one common trait: THEY HAVE DIMENSIONS.

      We define a contract that guarantees this.


      THE INTERFACE:
```Kotlin
interface PageElement {
    val width: Int
    val height: Int
}
```



   THE IMPLEMENTATION:
   Now we update our classes to "sign" this contract. They use `override` to
   prove they have these properties.
```Kotlin
// TextBox promises to have width and height
class TextBox(
    override val width: Int,
    override val height: Int,
    val maxChars: Int               // Specific to TextBox
) : PageElement
```


   ...


4. THE "AFTER" WORLD (POLYMORPHISM)
   Now look at how clean `DocumentManager` becomes. It doesn't care about
   TextBoxes or Images anymore. It only cares about `PageElement`.

   1. SINGLE STORAGE
```Kotlin
class DocumentManager {
    // One list to rule them all
    private val pageElements = mutableSetOf<PageElement>()

    // One method to add anything
    fun addPageElement(element: PageElement) {
        pageElements.add(element)
    }
}
```




2. UNIFIED LOGIC (POLYMORPHISM)
   We can write `maxHeight` once.

```Kotlin
fun maxHeight(): Int {
    // We map every item to its height.
    // The compiler allows `it.height` becuase the Interface guarantees it exists
    return pageElements.map { it.height }.max()
}
```

    THE MAGIC:
    When `it.height` runs:
       - If `it` is a `TextBox`, it grabs the TextBox's height.
       - ... grabs the Image's height.
       - The code treats them uniformly. This behavior is called POLYMORPHISM.




            Polymorphism is a core OOP concept where a single interface, method,
            or operator can represent different underlying forms (data types or
            classes). It allows objects of different classes to be treated as
            instances of a common superclass, enabling specialised behavior
            (overriding) or multiple signatures (overloading).


   SUMMARY OF BENEFITS
      1. SIMPLICITY: We replace 3 lists with 1. We replaced 9 comparison
         functions with 1 generic one
         (`tallerThan(a: PageElement, b: PageElement)`).
      2. EXTENSIBILITY: If you invent a `Video` class tomorrow, you just make it
         implement `PageElement`. You do NOT need to touch a single line of code
         in `DocumentManager`. It will automatically work with `addPageElement`
         and `maxHeight`.


Let's finish L4 by covering DEFAULT METHODS and DEFAULT PROPERTIES (S 53-64).

This section answers a critical question: What if I want to add a helper method
to my Interface without breaking every single class that implements it?

   1. DEFAULT METHODS (S 53-55)
      Traditionally, interfaces only list abstract methods (no code). But Kotlin
      allows you to provide a DEFAULT IMPLEMENTATION right inside the interface.

      THE PROBLEM:
      You realise every list needs an `isEmpty()` method.

      If you add `fun isEmpty(): Boolean` to the interface, EVERY class
      (`ResizingArrayList`, `SinglyLinkedList`) immediately breaks because they
      haven't implemented it yet.


   THE SOLUTION:
      You write the code once in the interface. Since every list has a `size`
      property, you can use that!

```Kotlin
interface ImperialMutableList<T> {
    val size: Int

    // Abstract method (Implementing classes MUST provide this)
    fun add(element: T)

    // Default method (Implementing classes CAN use this as-is)
    fun isEmpty(): Boolean = size <= 0
}
```

   Now, `ResizingArrayList` gets `isEmpty()` for free. It doesn't need to
   change a single line of code.




2. OVERRIDING DEFAULT METHODS FOR SPEED (S 56-57)
   Sometimes, the "default" way of doing things is safe but SLOW.

   EXAMPLE: `addAll`
   We can write a generic `addAll` in the interface that just loops through the
   other list and calls `add` for each item.
```Kotlin
// In the Interface
fun addAll(other: ImperialMutableList<T>) {
    for (i in 0..<other.size) add(other.get(i))     // Calls the abstract add() repeatedly
}
```

   ...


THE OPTIMISATION:
   The `ResizingArrayList` class should OVERRIDE this default method because it
   knows a secret shortcut: it can resize the array ONCE and copy everything
   directly.


```Kotlin
// Inside ResizingArrayList
override fun addAll(other: ImperialMutableList<T>) {
    // 1. Calculate total size needed
    val newSize = size + other.size

    // 2. Resize ONCE if needed (Big efficiency win!)
    if (newSize > elements.size) {
        elements = elements.copyOf(max(newSize, 2 * elements.size))
    }

    // 3. Copy items directly
    for (i in 0 until other.size) {
        elements[size + i] = other.get(i)
    }
    size = newSize
}
```




3. DEFAULT PROPERTIES (S 59-61)
   Just like methods, you can define properties in an interface. However,
   interfaces CANNOT HOLD STATE (they don't have memories). They only compute
   things based on other properties.


   EXAMPLE: `area`
   Every `PageElement` has a width and height. We can automatically provide an
   `area`.

```Kotlin
interface PageElement {
    val width: Int
    val height: Int

    val area: Int
        get() = width * height
}
```

   Now, `TextBox` and `Image` get `.area` automatically without writing any code
   .



4. CACHING EXPENSIVE PROPERTIES (S 62-64)

   What if the calculation is really heavy? (e.g., `calculatePhysics()` or
   `renderShadow()`). The default property in the interface runs the calculation
   EVERY SINGLE TIME you access it.

   To fix this, the implementing class can OVERRIDE the property to store
   (cache) the result.

```Kotlin
class HeavyElement : PageElement {
    // 1. Private variable to store the result (Memory)
    private var cachedArea: Int? = null

    override val width  = 100
    override val height = 200

    // 2. Override the property to use the cache
    override val area: Int
        get() {
            if (cachedArea == null) {
                // "super.area" calls the default calculation (width * height) from the interface
                cachedArea = super.area
            }
            return cachedArea!!
        }
}
```

   Note: This technique relies on `width` and `height` not changing. If they
   change, your cache becomes stale (wrong)!


LECTURE 4 SUMMARY
   1. INTERFACES define a contract (`val size`, `fun add`) without
      implementation details.
   2. POLYMORPHISM allows us to write one function (`maxHeights`) that works
      for `TextBox`, `Image` and `Menu` simultaneously.
   3. DEFAULT METHODS (`isEmpty`) let us add functionality to the interface
      itself to reduce code duplication.
   4. OVERRIDING DEFAULTS allow specific classes (like `ResizingArrayList`) to
      provide faster implementations than the generic default.









   1. OPERATOR OVERLOADING (Making your objects work with `+`, `*`, `[]`)
   2. EXTENSION METHODS (Adding features to classes you didn't write)

---
LECTURE 5: PART 1, OPERATOR OVERLOADING (S 1-21)
   Kotlin allows you to redefine what standard symbols (like `+`, `-`, `*`, `[]`)
   do for your custom classes. This isn't magic; the compiler simply maps
   symbols to specific function names.


   1. THE RULE OF THE `operator` KEYWORD
      To use a symbol, you must define a function with a specific name and mark
      it with `operator`.

`a + b`
    `a.plus(b)`
`a * b`
    `a.times(b)`
`a[i]`
    `a.get(i)`
`a[i] = b`
    `a.set(i, b)`
`a in b`
    `b.contains(a)`

   2. EXAMPLE: THE `Point` CLASS (Math)
      Instead of writing `p1.add(p2)`, we want to write `p1 + p2`.

```Kotlin
data class Point(val x: Int, val y: Int) {

    // 1. Overloading `+`
    operator fun plus(other: Point): Point {
        return Point(this.x + other.x, this.y + other.y)
    }

    // 2. Overloading `*` (Scalar multiplication)
    operator fun times(scale: Int) = Point(this.x * scale, this.y * scale)
}

        ...
```
        Note: Order matters!! `10 * p1` won't work because `Int` doesn't have a
        `times(Point)` function. We fix that in Part 2.




   3. EXAMPLE: ARRAY-LIKE ACCESS `[]`
      You can make your object behave like an array or map using `get` and `set`


```Kotlin
class MutablePoint(var x: Int, var y: Int) {

    // Read: p[0] returns x
    operator fun get(index: Int) {
        return when(index) {
            0 -> x
            1 -> y
            else -> throw IndexOutOfBoundException()
        }
    }


    // Write: p[0] = 10 sets x
    operator fun set(index: Int, value: Int) {
        when(index) {
            0 -> x = value
            1 -> y = value
            else -> throw IndexOutOfBoundsException()
        }
    }
}
```



   4. INFIX FUNCTIONS
      If you want readable code without using symbols, you çan use `infix`.
      This lets you drop the dot and parentheses.

```Kotlin
// Define it
infix fun Point.crossProduct(other: Point): Int { ... }

// Use it
val result = p1 crossProduct p2     // Same as p1.crossProduct(p2)
```


            READY FOR P2?
               This covers EXTENSION METHODS, which is how we solve the problem
               of making `10 * p1` work even though we don't own the `Int` class









⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣿⣿⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⢶⣦⡀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⠤⠒⢀⣿⣿⣿⣧⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣾⣿⠃
⠀⠀⠀⠀⠀⢀⣠⣴⠶⡛⢉⣀⣤⣶⣿⣿⣿⣿⣿⣿⣶⣤⣀⡀⠀⠀⠀⠀⠀⣴⣿⠟⠁⠀
⠀⠀⣀⣤⡾⠟⠋⠀⡈⠉⠙⠛⠻⢿⣿⣿⣿⣿⣿⣿⡿⠟⠛⠋⠉⢀⣠⣴⠟⠋⠁⠀⠀⠀
⢠⣾⡿⠋⠀⠀⠀⠀⡇⠀⠀⠀⠀⠀⠙⣿⣿⣿⡿⠋⠀⢀⣠⠴⠚⠋⠉⠀⠀⠀⠀⠀⠀⠀
⣿⣿⠁⠀⠀⠀⢀⣸⣧⡀⠀⠀⠀⠀⠀⠘⣿⣿⠧⠒⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠈⠛⠳⠦⠀⠈⠙⢿⣿⠛⠉⠀⠀⠀⠀⠀⢻⡟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠈⡇⠀⠀⠀⠀⠀⠀⠀⢸⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠃⠀⠀⠀⠀⠀⠀⠀⠈⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
*/






/*
    Kotlin's EXTENSION METHODS allow you to add new functionality to classes you
    don't own (like `String`, `Int`, or third-party classes) without inheriting
    from them or writing clunky utility wrappers. For a C++ developer, think of
    them as standalone helper functions (like `std::count`), but called using
    member-function syntax (`std.count()`).

    Here is the fast-paced breakdown of S22 through the end of the lecture.



    1. BASIC EXTENSION METHODS (S22-25)
       You typically declare these at the file-level scope.

       SYNTAX: Prefix the function name with the class you want to extend (this
       is called the RECEIVER TYPE).
```Kotlin
// Adds a custom count() to Kotlin's built-in String class
fun String.count(c: Char): Int = this.count { it == c }

// Usage:
println("Hello".count('l'))
```
   - `this`: Inside the extension, `this` refers to the RECEIVING OBJECT (the
     string `"Hello"`).
   - Just like in regular class methods, you can omit `this` when there is no
     ambiguity (e.g., `= count { it == c }`)



            `count()` returns the number of elements in a collection.





















* */