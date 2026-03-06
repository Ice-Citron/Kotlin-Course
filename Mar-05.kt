/*
    ... DELEGATION refers to a design pattern where one object (the "wrapper")
    handles a request by passing it off to another object (the "target") that
    actually contains the logic.

    For your `LockedMap`, you aren't writing new map logic; you are just
    "wrapping" an existing map to make it thread-safe using a lock.


---
1. HOW DELEGATION WORKS HERE
    Imagine `LockedMap` is like a security guard standing in front of a vault
    (`targetMap`).

    1. A user asks the guard to "get" an item.
    2. The guard ACQUIRES THE LOCK (opens the security door).
    3. The guard DELEGATES the request to the vault: `targetMap.get(key)`.
    4. Once the vault provides the item, the guard RELEASES THE LOCK (closes the
       door) and hands the item to the user.


2. CODE COMPARISON: NORMAL vs. DELEGATED
    Without Delegation (Standard Map):
    * You would have to write the logic for how to find a key in a list or hash
      table.

    With Delegation (`LockedMap`):
    * You just call the same method on the internal map.

```Java
// Simplified example of delegation in LockedMap
@Override
public V get(K key) {
    lock.lock();        // 1. Acquire the lock before the call
    try {
        return targetMap.get(key);  // 2. DELEGATION: Ask the target map
    } finally {
        lock.unlock()   // 3. Release the lock after return
    }
}
```


3. WHY USE DELEGATION FOR `LockedMap`?

    * SINGLE RESPONSIBILITY: The `targetMap` (like your `ListBasedMap`) focuses
      only on storing data. The `LockedMap` focuses only on synchronisation.
    * FLEXIBILITY: You can wrap any map implementation with this `LockedMap`
      code, and it will immediately become thread-safe without you having to
      rewrite the storage logic.


4. THE `iterator()` EXCEPTION
    The spec notes that every method should be delegated EXCEPT FOR `iterator()`
    . This is because returning a raw iterator from a locked map is dangerous--
    if the map is modified by another thread while someone is halfway through
    iterating, the program will crash. You'll likely need to create a
    "LockedIterator" or a snapshot to handle this safely.







*/




/*
    Atomic properties in Kotlin (and Java) are special variables that guarantees
    ATOMICITY, meaning operations on them happen as a single, indivisible unit.
    In a multi-threaded environment, they prevent "race conditions" where two
    threads try update a value at the exact same time, causing data loss.


1. WHY ARE THEY USED?
    Standard variables like `var count = 0` are not thread-safe because an
    operation like `count++` is actually three separate steps in the CPU:

    1. READ the current value.
    2. ADD 1 to it.
    3. WRITE it back to memory.

    If two threads do this simultaneously, they might both read `0` and `1`, and
    both write `1` back, losing one of the increments. Atomic types solve this
    by performing all three steps as one "locked" operation at the hardware
    level.


2. HOW ARE THEY USED?
   In Kotlin, you typically use the Java `java.util.concurrent.atomic` classes.

   EXAMPLE: A Thread-Safe Counter
```Kotlin
import java.util.concurrent.atomic.AtomicIntegers

class Analytics {
    // Instead of var count = 0
    private val visitorCount = AtomicInteger(0)

    // Atomic increment (Thread-safe)
    fun increment() = visitorCount.incrementAndGet()

    fun getCount(): Int = visitorCount.get()
}
```


3. WHEN TO USE THEM
    - SIMPLE COUNTERS/FLAGS: Use them for shared counters, IDs, or boolean
      status flags (e.g., `AtomicBoolean`).
    - HIGH PERFORMANCE: Use them when you want thread safety without the heavy
      overhead of a full `Lock` or `synchronized` block. They use "Lock-Free"
      algorithms that are much faster for simple data.
    - REFERENCE SWAPPING: Use `AtomicReference<T>` if you need to atomically
      update an entire object.


4. WHEN NOT TO USE THEM
    - MULTIPLE RELATED VARIABLES: If you need to update two different variables
      together (e.g., updating a `Latitude` and `Longitude` at the same time),
      atomics won't help. You need a `Lock` or your `LockedMap` structure to
      ensure the whole brick is protected.
    - COMPLEX LOGIC: If you have to check a condition, then do a calculation,
      then update (Check-Then-Acts), simple atomics can still have race
      conditions between the "check" and the "act".
    - LOCAL VARIABLES: Don't use them for variables only used by a single
      thread; it adds unnecessary performance costs.


    ---
    Feature // Atomic Types // Locks (like `LockedMap`)
    SCOPE // Single Variable // Entire code blocks or collections
    SPEED // Very Fast (Hardware level) // Slower (Software overhead)
    COMPLEXITY // Simple values only // Any complex logic













* */









/*
    ... A Tree-Based Map is a completely different way of organising data.


1. VISUALISING A SINGLE NODE
    In your Hash Map, a node in your linked list just held a `ValueNode` or a
    generic `Entry`. In a Tree-Based Map, every single "node" (or circle) in
    the tree holds a specific package: A KEY-VALUE PAIR.
        - EXAMPLE NODE: `[Key: "Charlie", Value: 21]`


2. VISUALISING THE TREE SHAPE (The Sorting Rule)
    Imagine an upside-down tree. You start at the top (the Root node) and branch
    downwards. The tree builds itself using one strict rule based ONLY ON THE
    KEYS:
    - Any key that is "smaller" (e.g., alphabetically earlier) goes to the LEFT.
    - Any key that is "larger" (e.g., alphabetically larger) goes to the RIGHT.

























*/

