
                // ./gradlew lintKotlin
                // ./gradlew formatKotlin

            // MOST IMPORTANTLY IF YOU FORGET, USE:
                // ./gradlew tasks


/*
... solution for the ARRAY-BASED HASHMAPS extension, broken down into the two
required classes, followed by the key insights and techniques you can take away
from it.


1. THE `ArrayBasedMap` CLASS
    The core challenge here is building a map that stores its key-value paris
    inside an array, and dynamically resizing that array when it gets full
    (just like an `ArrayList` does under the hood.)

```Kotlin
package datastructures

class ArrayBasedMap<K, V> : CustomMutableMap<K, V> {

    // We start with a small default capacity
    private var capacity = 8
    private var size = 0

                                // TECHNIQUE 1:
                                // Because of type erasure on the JVM, we cannot directly create an
                                // Array<Entry<K, V>>. Instead, we create an array of `Any?` and cast it
                                // when retrieving elements.
                                // Instead, we create an array of `Any?` and cast it when retrieving
                                // elements.
    // private var entries = arrayOfNulls<Any?>(capacity)      <-- okay apparent `Any?` isn't needed the whole time...

    private var entries: Array<CustomMutableMap.Entry<K, V>?> =
        arrayOfNulls(capacity)

    @Supress("UNCHECKED_CAST")
    private un entryAt(index: Int): CustomMutableMap.Entry<K, V> {
        return entries[index] as CustomMutableMap.Entry<K, V>
    }

    override fun get(key: K): V? {
        for (i in 0 until size) {
            val entry = entryAt(i)
            if (entry.key == key) return entry.value
        }
        return null
    }

    override fun put(key: K, value: V): V? {
        for (i in 0 until size) {
            val entry = entryAt(i)
            if (entry.key == key) {
                val oldValue = entry.value
                entries[i] = CustomMutableMap.Entry(key, value)
                return oldValue
            }
        }

        if (size == capacity) {
            resize()
        }

        entries[size++] = CustomMutableMap.Entry(key, value)
        return null
    }

    override fun remove(key: k) {
        for (i in 0 until size) {
            val entry = entryAt(i)
            if (entry.key == key) {
                val oldValue = entry.values

                for (j in i until size - 1) {
                    entries[j] = entries[j + 1]
                }

                entries[--size] = null

                return oldValue
            }
            return null
        }
    }

    private fun resize() {
        capacity *= 2
        val newEntries = arrayOfNulls<Any?>(capacity)

        for (i in 0 until size) {
            newEntries[i] = entries[i]
        }
        entries = newEntries
    }

    override operator fun iterator(): Iterator<CustomMutableMap.Entry<K, V>> {
        return object : Iterator<CustomMutableMap.Entry<K, V>> {
            private var cursor = 0
            override fun hasNext(): Boolean = cursor < size
            override fun next(): CustomMutableMap.Entry<K, V> {
                if (!hasNext()) throw NoSuchElementException()
                return entryAt(cursor++)
            }
        }
    }
}
```
* */



/*
2. THE `HashmapBackedByArrays` CLASS
    Because of the `GenericHashmap` design mentioned in the spec, implementing
    the actual Hashmap variant becomes incredibly trivial. We just pass the
    constructor of our new `ArrayBasedMap` as a lambda to the `bucketFactory`
    parameter.

```Kotlin
package datastructures

class HashmapBackedByArrays<K, V> : GenericHashmap<K, V>(
    bucketFactory = { ArrayBasedMap<K, V>() }
)
```


---
KEY TRICKS, INSIGHTS, AND TECHNIQUES TO LEARN
    When tackling data structure questions like this, keep these core concepts
    in mind:

    1. TYPE ERASURE & GENERIC ARRAYS
        - THE INSIGHT: In Kotlin (and Java), generics are erased at runtime. The
          JVM doesn't know what `K` or `V` are, which means it cannot allocate
          an array of exactly `Array<CustomMutableMap.Entry<K, V>>` because it
          doesn't know how much memory a completely unknown type needs.
        - THE TECHNIQUE: You bypass this by creating an array `Any?`
          (`arrayOfNulls<Any?>`) and then suppressing compiler warnings with
          `@Suppress("UNCHECKED_CAST")` when you extract elements out of the
          array and cast them back to your target type.

                <-- OKAY WAIT... TURNS OUT APPARENT IN OUR CASE... WE DIDN'T
                    NEED THIS TO BEGIN WITH... BUT STILL USEFUL TO KEEP IN MIND
                    HOW TO WRITE `...<Any?>() ... as Object...`


    2. AMORTIZED DOUBLING (The Resizing Logic)
        - THE INSIGHT: Why we do `capacity *= 2` instead of `capacity += 10`?
          If you add a fixed amount, you are constantly triggering `O(N)` copy
          operation as the array gets massive.
        - THE TECHNIQUE: By doubling the array size, resizing happens
          exponentially less often. Mathematically, this means that even though
          a single resize operation takes `O(N)` time, the average (amortized)
          time to `put` an element remains `O(1)`. Always double dynamic arrays.


    3. SHIFTING AND ARRAY MECHANICS
        - THE INSIGHT: Arrays require contiguous memory. Unlike a
          `CustomLinkedList` where removing an element just means changing a
          pointer, removing an item from the middle of an array leaves a "hole".
        - THE TECHNIQUE: When implementing array-based removals, you must write
          a nested loop to copy every element right of the target index one
          space to the left (`entries[j] = entries[j+1]`). This gives array
          deletions an `O(N)` time complexity compared to the linked list's
          `O(1)` deletion.


    4. GARBAGE COLLECTION PREVENTION (NULLING)
        - THE INSIGHT: When you decrement the `size` variable during a removal,
          your map ignores the old data at the end of the array. However, the
          array itself still holds a reference to that deleted object in memory.
        - THE TECHNIQUE: Always set the now-unused array slot to `null`
          (`entries[size] = null`). This breaks the reference, allowing the
          JVM's GC to free up that memory. Without this, your custom data
          structures will cause memory leaks in production.

    5. THE FACTORY PATTERN
        - THE INSIGHT: Notice how easily `HashmapBackedByArrays` was created.
          This is because the abstract `GenericHashmap` was build using the
          Factory Pattern.
        - THE TECHNIQUE: By accepting a "bucket producer"
          (`bucketFactory = { ArrayBasedMap() }`) rather than hardcoding the
          bucket type, the hashmap code became entirely decoupled from the
          underlying bucket structure. Use passing-functions-as-parameters
          whenever you want a class to dictate when to build something, but
          let the caller dictate what to build.

*/





/*  ----    ----    ----    ----    ----    ----    ----    ----    ---- */



/*
    ... a fantastic question, and a verh sharp architectural hunch! ... thinking
    like a senior right now: "Why are we passing a function around when OOP has
    interfaces and inheritance?"

    It is absolutely true that you could try to design this using a `val` of the
    superclass/interface type. However, if you try to build a hashmap that way,
    you will immediately crash into two massive brick walls.

    ... why your idea is incredibly tempting, but why the Factory Pattern (the
    lambda) is ultimately required for this specific data structure.


---
THE PROBLEM WITH `val bucket: CustomMutableMap<K, V>`
    Imagine you redesigned the constructor to take a single instance of a bucket
    instead of a function, like this:

```Kotlin
abstract class GenericHashmap<K, V> {
    private val bucketTemplate: CustomMutableMap<K, V>  // Using an interface val
}
```


BRICK WALL 1: THE "SHARED MEMORY" TRAP
    When you first build your hashmap, you need an array of 32 buckets. If you
    write:

```Kotlin
private var buckets = Array(32) { bucketTemplate }
```
    You did not just create 32 empty buckets. You put the EXACT SAME BUCKET
    OBJECT into all 32 slots. If a collision happens and you put
    `(Key A, Value 1)` into `buckets[0]`, it will magically appear in
    `buckets[1]`, `buckets[2]`, and ... because they all point to the exact same
    memory address. Your entire hashmap devolves into one giant slow list.



BRICK WALL 2: THE "GHOST INSTANTIATION" PROBLEM
    To fix Brick Wall 1, you realise you need 32 distinct, separate buckets. You
    also need to create brand new buckets whenever the hashmap exceeds its load
    factor and calls `resize()`.

    But how do you create a new one? You cannot write this:

```Kotlin
val newBucket = CustomMutablemap<K, V>()
```
    You cannot instantiate an interface. The compiler says, "Wait, do you want
    a ListBasedMap? An ArrayBasedMap? I odon't know how to build a generic
    CustomMutableMap!"

    "Are there other ways besides the Factory Lambda?" ... Yes! The Factory
    Lambda is not the only way, but it is the cleanest. Here are the other ways
    you could solve it without a lambda, and why they are usually worse:


1. THE "PROTOTYPE" PATTERN
    - HOW IT WORKS: `bucketTemplate.clone()`
    - WHY IT'S WORSE: Kotlin doesn't have a clean, universal `.clean()`
      interface for custom collectons. You would have to force every map you
      write to implement a custom `copy()` method.

2. REFLECTION (The Java Way)
    ...

3. THE ENUM FLAG (The Beginner Way)
    You pass a variable telling the map which one to build.
    - HOW IT WORKS: `if (type == "LIST") ListBasedMap() else ArrayBasedMap()`
    - WHY IT'S WORSE: This violates the Open-Closed Principle. If you invent a
      `TreeBasedMap` next week, you have to open up the `GenericHashmap` file
      and modify its `if/else` logic to support your new invention.


WHY THE LAMBDA IS "KOTLIN MAGIC"#
    By passing `() -> CustomMutableMap<K, V>`, you give the `GenericHashmap` an
    infinite, free supply of new buckets. The Hashmap doesn't know how they are
    built, and it doesn't share memory between them. Every time it calls that
    function, a brand new bucket pops out.

    ...











---












* */






/*  ----    ----    ----    ----    ----    ----    ----    ----    ---- */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ---- */
/*
--- Amortised in CS refers to averaging the time complexity of an operation over
    a long sequence of actions, rather than focusing on worst-case of a single
    operation. It spreads the cost of rare, expensive operations across many
    frequent, cheap operations, proving that the average cost per operation is
    low over time.


--- In the context of... the FACTORY PATTERN is a creational design pattern that
    provides an interface for creating objects without specifying the exact
    class of the object that will be created.

    In your code... using the `BucketFactory<K, V>` type alias:
    `typealias BucketFactory<K, V>() = () -> CustomMutableMap<K, V>`

    HOW IT WORKS IN YOUR CODE
        Instead of the `GenericHashmap` deciding exactly which type of map to
        use for its buckets, it asks the "factory" (the `bucketFactory` lambda)
        to produce one whenever needed.

        - ABSTRACTION: The `GenericHashmap` doesn't care if the bucket is a
          `ListBasedMap` or some other implementation; it only cares that it
          fulfills the `CustomMutableMap` interface.
        - FLEXIBILITY: This allows you to swap out the internal storage logic of
          the bucket without changing a single line of code inside the
          `GenericHashmap` class itself.
        - INVERSION OF CONTROL: The responsibility of "how to build a bucket" is
          moved outside of the hash map and given to whoever initializes the
          class.




---

* */















