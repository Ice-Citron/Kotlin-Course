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



























* */