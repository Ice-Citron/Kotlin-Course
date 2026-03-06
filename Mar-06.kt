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

/* ---- ----    ----    ----    ----    ----    ----    ----    ----    ---- */
/* ---- ----    ----    ----    ----    ----    ----    ----    ----    ---- */
/*
    In this context, `>>` is the BITWISE RIGHT SHFIT operator.

    ... mathematically it looks like "significantly greater than," but in
    programming, it is used to move the individual bits of a number to the
    right.


THE "COLOR" PACKING" CONTEXT
    In many computer system (like the one in your lab), a single `int` is used
    to store an entire color. Since an `int` has 32 bits, it "packs" the Red,
    Green, and Blue values inside is like this:

    Bits 31-24 // Bits 23-16 // Bits 15-8 // Bits 7-0
    Alpha (Transparency) // RED // GREEN // BLUE


WHAT `>>` IS DOING HERE
    To get the individual colors back out of that one big number, you have to
    "shift" the bits so that the color you want moves into the last 8 positions
    (the 0-255 range).

    1. BLUE (`rgb & 0xff`): Blue is already at the very end. We don't shift at
       all. We just use `& 0xff` to ignore everything else.
    2. GREEN (`rgb >> 8`): We shift the bits 8 places to the right. This
       "pushes" the Blue bits off the edge and moves the Green bits into the
       "Blue slot".


...

WHY THE `& 0xff`?
    After you shift, there might still be some "Alpha" bits hanging around to
    the left of your color. The `& 0xff` (which is `11111111` in binary) acts
    like a MASK. It says: "Only keep the last 8 bits and turn everything else to
    zero."

    SUMMARY: `>>` is a way of "extracting" pieces of data that are crammed into
    a single number. It is must faster for a CPU to do this than to perform
    division or more complex math!








* */
/* ---- ----    ----    ----    ----    ----    ----    ----    ----    ---- */
/* ---- ----    ----    ----    ----    ----    ----    ----    ----    ---- */
/*
    Implementing the `PictureProcessor.main` method involves two primary
    responsibilities: PARSING THE COMMAND-LINE ARGUMENTS (`String[] args`) to
    identify the requested operation and its parameters, and COORDINATING THE
    IMAGE PROCESSING by invoking the appropriate methods in the `Picture` class.


1. UNDERSTANDING `String[] args`
    When you run a Java program from the terminal, any text you type after the
    class name is automatically bundled into the `args` array.

        * INDEXING: `args[0]` is always the first word after the class name (the
          command).
        * PARSING: You use standard array indexing to access these strings. For
          example, in `invert <output> <output>`, `args[1]` is the input path
          and `args[2]` is the output path.


2. RECOMMENDED PROGRAM STRUCTURE
    To keep your code organised, follow the "suggested design" from the spec:
    keep parsing logic in `main` and transformation logic inside the `Picture`
    class.

    Inside `PictureProcessor.java`:

```java
    public static void main(String[] args) {
        if (args.length == 0) return;

        String command = args[0];       // e.g., "rotate"

        // Use a switch statement to handle different commands
        switch (command) {
            case "invert":
                // invert <input> <output>
                Picture pic = new Picture(args[1]);     // Load
                pic.invert();
                pic.saveAs(args[2]);
                break;

            case "rotate":
                // rotate <angle> <input> <output>
                int angle = Integer.parseInt(args[1]);
                Picture toRotate = new Picture(args[2]);
                // If rotating changes dimension, you might need a helper that
                // returns a new Picture
                Picture rotated = toRotate.rotate(angle);
                break;

            // Add cases for grayscale, flip, blend, and blur...
        }
    }
```


3. HANDLING TOOL CALLS & PARAMETER EXTRACTION
    To use `args` effectively for different tool-like functionalities, you must
    account for VARIABLE PARAMETER COUNTS:
        * FIXED PARAMETERS: For `invert`, you know exactly where the input and
          output live (`args[1]` and `args[2]`).
        * VARIABLE PARAMETERS (`blend`): The `blend` command can have any number
          of input images. You would parse it by taking everything from
          `args[1]` up to `args[args.length - 2]` as inputs, and
          `args[args.length - 1]` as the output destination.
        * VALIDATION: Professional implementations check `args.length` before
          accessing an index to prevent `ArrayIndexOutOfBoundsException`.


4. SUMMARY CHECKLIST FOR YOUR WORKFLOW

    1. SKELETON: Create the `switch` block in `PictureProcessing.main` to catch
       all possible command strings (invert, grayscale, etc.).
    2. STUBS: Create corresponding empty methods in `Picture.java` (e.g.,
       `public void invert()`) so your `main` method can compile.
    3. IMPLEMENTATION: Fill in the pixel-processing logic for each
       transformation one by one.
    4. TESTING: Run the provided `PictureProcessorTest` frequently to ensure
       each command works as expected.





* */
























/* ---- ----    ----    ----    ----    ----    ----    ----    ----    ---- */
/* ---- ----    ----    ----    ----    ----    ----    ----    ----    ---- */




























/* ---- ----    ----    ----    ----    ----    ----    ----    ----    ---- */
/* ---- ----    ----    ----    ----    ----    ----    ----    ----    ---- */
























/* ---- ----    ----    ----    ----    ----    ----    ----    ----    ---- */
/* ---- ----    ----    ----    ----    ----    ----    ----    ----    ---- */

















/* ---- ----    ----    ----    ----    ----    ----    ----    ----    ---- */
/* ---- ----    ----    ----    ----    ----    ----    ----    ----    ---- */














