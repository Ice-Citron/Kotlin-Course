/*
WHY DOES THIS ARCHITECTURE WORK? (The Breakdown)
    Here is a high-level explanation of the CS concepts at play:


1. THE TREE SEARCH (`put` and `get`)
    A Binary Search Tree physically organises objects in memory: everything to
    the LEFT is smaller, and everything to the RIGHT is larger.
        * To GET a value, you drop the key in at the root. If it's smaller, go
          left. If it's larger, go right. This is blazingly fast--O(log N) time,
          cutting the remaining search space exactly in half every step.
        * We use iterative `while(true)` loops instead of recursion to prevent
          `StackOverflowError` exceptions if a user feeds us a severely
          unbalanced, lopsided tree.


2. THE ITERATOR (The Stack Trick)
    To return items in ascending order, a BST uses an IN-ORDER TRAVERSAL.
    However, Iterators must be "lazy" (yielding one item at a time, pausing, and
    waiting for the user to call `next()` again).
        * To pause recursion, we manually simulate it using a `Deque` (Stack).
        * By blindly plunging down the left side of the tree (`pushLeftSpine`),
          the item at the very top of the stack is guaranteed to be the smallest
          element in the tree.


------------------
    ... If we want to read this tree in ASCENDING ORDER (In-Order Traversal),
    the result must be: `1, 3, 4, 5, 6, 7`.


THE CORE PROBLEM
    Normally, to print a tree in order, you write a recursive function that goes
    all the way left, prints the node, and then goes right.

    But an `Iterator` cannot do that. An iterator must YIELD one item (e.g., `1`
    ), and then completely PAUSE its execution until the user decides to call
    `next()` again (to get `3`). Standard recursion cannot pause halfway through
    and resume later.

    To solve this, we use a STACK (`Deque`) to act as our "bookmark." It
    remembers exactly where we left off.


STEP-BY-STEP Breakdown of the Paragraph
    ...

    1. "BLINDLY PLUNGING DOWN THE LEFT SIDE" (`pushLeftSpine`)
        When the iterator is created, it starts at the root node (`5`) and goes
        as far left as mathematically possible, pushing every node onto the
        stack as a bookmark.
        * ...
        * Go left. Push `1`.
        * STACK STATE: `[Top: 1, 3, 5]`
          Because we went as far left as possible, the top of the stack (`1`) is
          guaranteed to be the smallest item in the tree.


    2. THE FIRST `next()` CALL
        The user calls `next()`.
        * We `pop()` the top of the stack (`1`).
        * We look at `1`'s right child to see if there is a slightly larger
          number waiting. There is no right child.
        * STACK STATE: `[Top: 3, 5]`
        * We return `1` to the user.


    3. THE SECOND `next()` CALL
        The user calls `next()` again.
        * We `pop()` the top of the stack (`3`).
        * We look at `3`'s right child. It does have one: `4`.
        * Because `4` is larger than `3` but smaller than `5`, we must process
          it next. We run `pushLeftSpine` starting at `4`. It pushes `4`, tries
          to go left, finds nothing, and stops.
        * STACK STATE: `[Top: 4, 5]`
        * We return `3` to the user.


    ......

THE "AHA!" MOMENT
    The stack essentially stores a list of "Roots" that we have bypassed to get
    to the smallest numbers on the left.

    When we pop a node off the stack, we are saying: "I have finished returning
    everything to the left of this node. Now it is this node's turn." Once we
    return that node, the very next numbers in the sequence MUST live in its
    right subtree. But we can't just return the right child immediately--we have
    to find the smallest number in that right subtree. That is why we look at
    the right child and immediately plunge down its left spine!



------------------

3. THE REMOVAL ALGORITHM
    Deleting from a tree leaves a massive hold.
    - CASE 1 & 2 (0 or 1 child): Easy. If `A` points to `B`, and `B` points to
      `C`, deleting `B` just means telling `A` to point directly to `C`.
    - CASE 3 (2 children): The ultimate trick. If a node has two branches, you
      can't just delete it. You have to:
        1. Find the SMALLEST KEY IN THE RIGHT SUBTREE (the "Successor").
        2. COPY its Key and Value into the node you wanted to delete.
        3. DELETE THE ORIGINAL SUCCESSOR NODE instead. (Because the successor
           was the smallest item on its side, it is mathematically guaranteed
           to not have a left child, meaning we can safely delete it using
           our easy "0 or 1 child" helper!).


4. TREE-BACKED HASHMAPS (The Point of the Extension)
    In Strands 1 and 2, if 10,000 hash collisions happened and fell into the
    same bucket, they were chained in a `CustomLinkedList`. Searching a list
    takes O(N) time (10,000 checks).

    By passing `{ TreeBasedMap(comparator) }` as our bucket factory lambda, we
    are creating a modern, high-performance Hashmap. Searching inside a heavily-
    collided bucket drops down to O(log N) time (~14 checks). THIS IS EXACTLY
    HOW STANDARD JAVA 8+ IMPLEMENTS ITS OFFICIAL `java.util.Hashmap` under the
    hood to prevent Hash Collision DDOS attacks!














* */

/* ---- ----    ----    ----    ----    ----    ----    ----    ----    ---- */
/*
-- A deque (double-ended queue, pronounced "deck") is an abstract data type that
   generalises a queue by allowing elements to be added or removed form both the
   front and back ends with O(1) efficiency. It acts as a flexible, hybrid
   structure, supporting both FIFO and LIFO operations.

   KEY ASPECTS OF A DEQUE:
   * OPERATIONS: Supports `push_front`, `push_back`, `pop_front`, and `pop_back`
   * IMPLEMENTATION: Typically implemented using circular arrays or doubly
     linked lists.
   * FLEXIBILITY: It can act as both a stack and a queue.
   * APPLICATIONS: Ideal for sliding window problems, scheduling algorithms, and
     palindrome checks.













* */












