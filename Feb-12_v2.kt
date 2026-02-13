
/*
   Let's continue with TERM 2 LECTURE 3, covering pages 21-42. This section is
   essentially a "How-To" guide to building a professional-grade Singly Linked
   List, focusing heavily on ENCAPSULATION (hiding the messy details) and
   ALGORITHMS (how to actually move the pointers).


PART 3: REFINING THE CLASS STRUCTURE (S 21-26)
   We left off with the idea of a `Node` and a `Linkedlist`. But how do we
   organise them?


   1. THE "NAIVE" APPROACH (P22)
      You could just define a `class Node` and a `class SinglyLinkedList`.
      - THE PROBLEM: Anyone can create a `Node`! We only want nodes to exist
        inside our list mechanism. We don't want external code messing with
        them.

   2. THE "NESTED" SOLUTION (P23-26)
      The best practice is to make `Node` a PRIVATE NESTED CLASS inside
      `SinglyLinkedList`.
      - WHY?
         1. HIDING DETAILS: The outside world sees `SinglyLinkedList<T>`, but
            they never see `Node<T>`. They just add/remove items.
         2. NAMESPACE PROTECTION: You can have a `Node` inside `LinkedList` and
            a different `Node` inside `Tree` without them clashing.

      - CODE STRUCTURE:
```Kotlin
class SinglyLinkedList<T> {
    // Nested private class: Only SinglyLinkedList can see this!
    private class Node<T>(
        var element: T,
        var next: Node<T>? = null
    )

    private var head: Node<T>? = null
    private var tail: Node<T>? = null
    var size: Int = 0
        private set


    private fun add(element: T): Unit {
        size++
        val newNode: Node<T> = Node(element)

        if (head == null) {
            // Empty list case
            head = newNode
            tail = newNode
            return
        }

        // Non-empty list case
        tail!!.next = newNode    // Link the old tail to the new one
        tail = newNode           // Update the tail pointer
    }
}
```




PART 4: ADDING ITEMS (P 27-31)
   Adding to the end is generally efficient because we keep a reference to
   `tail`.

   THE ALGORITHM:
      1. Create a new node.
      2. If the list is EMPTY: Point `head` and `tail` to the new node.
      3. If NOT EMPTY:
         - Link the old tail's `next` to the new node.
         - Update the `tail` reference to point to the new node.
      4. Increment `size`.
* */



/*
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣄⡤⠄⢀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⣿⡿⢂⠁⠐⠬⠐⣤⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣾⣿⣿⢃⠇⡀⠀⠀⠁⠈⢆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⣿⣿⢯⡙⢦⠐⠀⠀⠀⠀⠀⠳⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣿⣿⣟⢧⡙⠦⡑⢠⠀⠀⠀⠀⠠⡑⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣾⣿⣿⡝⢮⡑⢣⠜⡠⢊⠀⠀⠀⠀⠀⠈⢦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⣿⣿⠿⣜⢣⠜⣡⢊⡔⢡⠊⠄⡀⠀⠀⠀⠀⠡⢀⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣿⣿⣟⡻⣌⢣⠚⢤⠒⡌⢆⡉⠆⠄⡀⠀⠀⠀⠀⠂⠉⠀⠀⠀⠂⠒⠐⠒⠒⠒⠒⠒⠛⠠⢠⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⢀⠀⠀⠀⢀⣀⣰⣾⣿⣿⢟⢮⡱⢊⠦⡙⢢⠍⡜⡄⣃⠎⡰⢠⠁⠄⠀⠀⠀⠀⡀⠠⠐⡀⢂⠐⡀⠂⠄⠂⠀⠀⠀⠳⠙⠆⠀
⠀⠀⠀⠀⠀⠀⣀⣀⣴⣴⣶⣾⣿⠿⢿⠻⣙⠎⡞⢢⠱⣉⠦⡑⢣⠜⡰⡘⢄⡚⢄⠣⡘⢄⠣⡈⠤⢁⠤⠁⢆⠡⢂⠆⡰⠡⢌⠠⢁⠂⠀⠀⠀⢸⡀
⠀⠀⣤⣼⣾⣿⠿⡛⠿⣉⠖⡌⢆⡙⢢⡙⢤⢋⠜⡡⢓⡰⢌⡱⢃⢎⠱⣘⠢⢜⡠⢣⢑⠊⡔⢡⠊⡔⡨⡑⢌⠢⣅⠪⠔⣡⠢⡑⠂⠌⡀⢀⠀⣸⠁
⠀⣼⣿⣿⡟⣆⢣⡙⠦⢡⠎⡔⡊⢔⠣⡘⡔⡌⣊⠱⢌⢢⢃⠖⣉⠆⡓⢤⢋⠤⢣⠱⡨⠱⡘⢢⠱⡘⢤⡑⣊⠱⡠⢃⠭⢄⠣⢌⠱⡈⠔⢢⡼⠃⠀
⢰⣿⣿⣿⡽⣌⠧⡘⢥⢃⠎⡴⢉⢆⢣⠱⡘⠤⣑⠪⠜⣂⠎⡜⠤⡙⣌⠲⢌⡊⢥⠒⠥⢣⢡⢃⢎⡑⠦⡘⢤⠣⣑⡉⢲⢈⡱⢈⠆⡱⣨⠏⠐⠀⠀
⠘⣿⣿⣿⣿⣽⢮⣙⢦⣉⠞⣄⠫⣄⠣⣃⠕⡣⢆⡙⠦⣑⠪⠜⢢⠱⢄⡃⡒⢌⠢⢍⢊⡑⠦⣉⠦⣘⢡⡙⢂⡓⢤⡘⢡⠂⢆⢡⢊⡴⠓⠂⠀⠀⠀
⠀⠘⢿⣿⣿⣿⣿⣿⡶⣍⡞⡤⢓⠬⣑⠢⢍⡒⠥⢚⡰⠡⢎⡙⢢⢉⡒⠌⡜⢢⢉⠆⢣⢘⠢⣅⠲⢡⠆⡱⢃⠜⡠⠘⡄⡉⢆⣸⠞⠁⠀⠀⠀⠀⠀
⠀⠀⠀⠉⠻⣿⣿⣿⣿⣿⣾⣱⢏⡲⣡⢋⠦⣉⢎⡱⢢⠙⡤⢃⠣⠎⠴⣉⠴⡁⠎⢬⡁⢎⡱⢄⠫⡔⢊⡱⢈⠒⠠⢁⠐⠈⡴⠃⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠈⠙⢿⣿⣿⣿⣿⣯⣷⡱⢎⡲⢡⠎⡔⢣⡉⢖⡩⠜⣉⠖⡡⢆⠹⣘⠢⢜⢂⠖⠬⡑⠬⣁⠦⠡⢈⠀⠀⠀⢸⠁⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠐⠀⠈⠻⢿⣿⣿⣿⣿⣧⢣⢇⡚⢬⠱⡘⠦⣘⠱⡌⠲⡑⣌⠣⢆⡙⢢⠩⣌⠣⢍⠲⡐⡌⡑⠀⠀⠀⠀⢨⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠹⣿⣿⣿⣷⣫⢦⡙⣂⠧⣑⠣⣌⠣⡜⣡⠓⡤⠓⡬⡘⣅⠣⢆⡹⢨⠱⣁⠆⡡⠂⠀⠀⠀⠀⡆⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⣿⣿⣿⡗⣦⠓⣌⠲⣡⠚⣄⠳⡘⢤⠓⣌⠳⡰⢱⡈⠞⢤⢃⢣⢓⡰⢊⠔⠡⠀⠀⠀⠀⠸⡆⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⡟⣦⢋⡔⢣⢆⡹⢄⢣⡙⠦⣉⢆⢣⡑⢣⠜⣩⠒⣌⢆⢣⢒⡩⢌⠡⠂⠀⠀⠀⠀⢣⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣟⡴⢋⡜⣡⠒⡜⡌⢦⡑⣣⠜⣸⠢⡝⣬⢚⡤⡛⣔⢪⡑⢎⠴⡉⢆⠡⠀⠀⠀⠀⠈⡄⡄⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⣿⣿⣿⡷⢎⡳⣘⠤⣋⠴⣉⠦⡱⢆⡻⣔⣫⡼⣖⣯⣶⣽⢬⡳⣜⢣⢎⡕⢪⠔⡡⠀⠀⠀⠀⢱⡀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⣿⣿⣿⡯⣇⠳⡥⢚⠤⣓⢬⠲⣙⣬⢳⡽⣾⣿⣿⣿⣿⣿⣿⣿⣾⣯⣾⣜⣧⡚⡴⡀⠄⠀⠈⠀⡇⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢈⣿⣿⣿⡷⣭⠳⡜⣩⠖⡱⢎⡳⣭⢶⣻⡿⢟⠏⠉⠉⢛⠻⢿⣿⣿⣿⣿⣿⣷⣿⣶⡽⣘⢆⡡⣰⠃⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⣿⣿⡲⣝⡼⣱⡚⣝⢮⣷⣻⢾⠻⠈⠀⠀⠀⠀⠀⠀⠀⠀⠉⠛⠻⠿⣿⣿⣿⣿⣽⣮⡷⠇⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢻⣿⣿⣿⣿⣟⣶⣣⢿⣽⡾⠯⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠙⠩⠏⠀⠉⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⠿⣿⣿⣿⣿⣿⠻⠍⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠁⠀⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
* */

/*
PART 5: REMOVING ITEM (P 32-38)
   Removing is significantly harder than adding. Why? Because to remove a node,
   you need to update the link in its PREDECESSOR (the node before it).

   THE ALGORITHM (for index > 0):
      1. TRAVERSE: You must start at `head` and loop until you find the node at
         `index - 1` (the predecessor)
      2. UNLINK: Update `predecessor.next` to skip over the target and point
         straight to `target.next`.
      3. GARBAGE COLLECTION: The removed node is now floating in memory with no
         references to it. The garbarge collector will eventually eat it.

   EFFICIENCY CHECK (S38):
      The `removeAt` function requires a helper called `traverseTo(index)`.
      Since this loop runs from 0 to `index`, removing an item is an O(N)
      operation. You have to walk the chain.




PART 6: EFFICIENCY SUMMARY (P 40-42)
   The lecture concludes by comparing the performance of your Singly Linked
   List operations.


OPERATION // EFFICIENCY // REASON
Get(i)     // Slow (O(N)) // You must follow pointers from `head` to `i`
Add(End)   // Fast (O(1)) // We have a `tail` pointer, so no traversal needed.
Add(Start) // Fast (O(1)) // Just update `head`. No traversal needed,
Add(Middle)// Slow (O(N)) // You have to traverse to find the spot.


* */







/*
⠀⠀⠀⠀⢠⠀⠀⣀⠤⣤⢤⣠⣀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠤⢢⣒⣁⡃⠓⢢⠀⠀⠀⠀
⠀⠀⡰⠒⡄⠀⡜⣠⠏⠁⠀⠀⠀⠘⠃⠦⣀⢀⣠⣰⠢⠚⠋⠉⠀⠀⠸⡄⠀⢣⠀⠀⠀
⠀⠀⠘⠒⠁⣰⢡⢣⢀⢀⢀⢀⣀⠤⠔⢐⣬⢾⣣⣅⡀⡀⠀⢀⢀⠔⢩⠇⠀⠸⠀⠀⠀
⠀⠀⠀⠀⠀⢸⠈⢧⡅⣁⣁⢁⡤⠦⣚⡽⢻⢿⡯⡑⠮⣬⢩⡁⡁⡄⠟⡀⠔⠃⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠓⣄⢀⡁⣀⢁⡴⢼⠁⢀⠟⠀⣣⠑⢄⠀⠀⠉⠈⠉⠈⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⢷⠀⠀⡀⡔⠊⢁⠔⢡⠰⠁⠀⠀⠘⢧⡀⠱⣄⡀⠀⠀⠀⠀⠀⠀⢢⡔⠁
⠀⠀⠀⠀⠀⠀⡀⠒⠁⣀⠒⢁⡼⠋⠀⠀⠀⠀⠀⠈⠲⣄⠱⡙⢤⠀⠀⠀⠀⠐⠁⠘⠀
⠀⠀⠀⠀⢀⠌⠀⢠⠊⠀⣠⠇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⣦⠱⡀⠳⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⡜⠀⠀⡇⠀⣐⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⣇⢱⠀⢣⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⡇⠀⠀⡇⢠⠏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡿⠀⡇⠀⡇⠀⠀⠀⠀⠀
⠀⡄⠀⠀⠘⣄⠀⠑⣏⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡟⠀⢰⠀⠣⠀⠀⠀⠀⠀
⠒⡵⣊⠀⠀⠈⠐⢺⢅⡁⠦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⠀⠀⢸⠀⡎⠀⠀⠀⠀⠀
⠊⢱⠈⠀⠀⠀⠀⡯⠀⠉⢢⡉⢤⠀⠀⠀⠀⠀⠀⠀⠀⣰⠃⠀⠀⡰⡕⠁⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⢻⡀⠀⠀⢱⠈⡆⠀⠀⠀⠀⠀⠀⢠⠎⠀⢀⢴⠋⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⢧⡀⠀⡸⢐⠇⠀⠀⠀⠀⠀⠀⡼⠁⠠⢪⠊⠀⠀⠀⠀⠀⡀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠈⢧⠰⢁⠌⠀⠀⠀⠀⠀⠀⢸⣅⠎⢠⠃⠀⠀⠀⠀⠀⠠⡇⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⢀⠞⣢⠃⠀⠀⠀⠀⠀⠀⠀⣸⠃⢀⠂⠀⠀⠀⠀⠀⠘⢐⡤⠋⠀
⠀⠀⠀⠀⠀⠀⠀⡠⢃⠜⢸⠄⠀⠀⠀⠀⠀⠀⢰⠹⡆⠇⠀⠀⠀⠀⠀⠀⢠⢚⠕⠄⠀
⠀⠀⠀⠀⠀⠀⡜⣠⠃⠀⢸⠅⠀⠀⠀⠀⠀⠀⡇⠀⠽⡆⠀⠀⠀⠀⠀⠀⠀⢐⠅⠀⠀
⠀⠀⠀⠀⠀⣸⢠⠃⠀⠀⣼⠀⠀⠀⠀⠀⠀⠀⡇⠀⠡⠙⣦⠀⠀⠀⠀⠀⠀⠰⠁⠀⠀
⠀⠀⠀⠀⠀⡆⢸⠀⣠⠜⠃⠀⠀⠀⠀⠀⠀⠀⢣⠀⠀⢣⠈⢳⡀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⡀⠀⠀⢃⠘⠴⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⢢⡀⢇⠀⠱⣄⠀⠀⠀⠀⠀⠀⠀
⠐⡅⣈⠇⠀⠀⡿⡙⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠻⢄⠀⠈⠱⡄⠀⠀⠀⠀⠀
⠀⠈⠀⠀⠀⢼⠁⠙⢌⠣⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠸⡱⢀⠀⠙⣢⠀⠀⠀⠀
⠀⠀⠀⠀⢀⡍⠀⠀⠀⠑⣄⠃⢤⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠰⡘⡀⠀⢸⠇⠀⠀⠀
⠀⠀⠀⠀⠐⠃⠀⠀⠀⠀⠈⠢⠀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡇⢰⠀⡼⠁⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠇⠀⢠⠋⠀⠀⠀⠀
* */

fun main() {

}