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


















explain in detail how does interfaces work, it's key methods, key uses. when to
use it, etc.
explain through REAL CODE example when possible
            https://gemini.google.com/app/bd659ecdaa303cef
* */