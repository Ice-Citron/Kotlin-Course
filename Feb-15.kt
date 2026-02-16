/*
   The OPEN-CLOSED PRINCIPLE (OCP) is one of the most famous rules in SWE--it is
   the "O" in the SOLID principles of Object-Oriented Design.

   It is absolutely not just a pedantic convention; it exists to solve massive,
   real-world headaches on large codebases.

   Let's break it down exactly as you asked: first defining the principle, then
   working backwards to see why the `enum`/`switch` approach violates it, and
   finally looking at the concrete architectural advantages.


---
1. WHAT IS THE OPEN-CLOSED PRINCIPLE?
   The principle states:
      "Software entities (classes, modules, functions) should be OPEN FOR
      EXTENSION, but CLOSED FOR MODIFICATION."

   - OPEN FOR EXTENSION: As the requirements of your app grow, you should be
     able to make the software do new things (like adding a brand-new type of
     game world).
   - CLOSED FOR MODIFICATION: You should be able to achieve this extension
     WITHOUT TOUCHING THE EXISTING, ALREADY-WORKING, ALREADY-TESTED SOURCE CODE.


   THE HARDWARE ANALOGY: Think of a computer motherboard's USB port.
      If you want to add a new peripheral (a webcam, a weird custom joystick),
      the computer is open for extension--you just plug it in. But the computer
      is closed for modification--you don't have to open the PC case, take out
      a soldering iron, and physically wire the new device directly to the
      motherboard's circuity to make it work.


---
2. WORKING BACKWARDS: WHY THE `enum`/`when` VIOLATES OCP
   Let's look at the "Enum Anti-Pattern" used in the first attempt at `GridWorld`:
```Kotlin
enum class WorldKind { BOUNDED, DEADLY, RANDOM }

class GridWorld(val kind: WorldKind) {
    fun updatePosition(newPos: Pair<Int, Int>) {
        // ... if player walks off the edge:
        when (kind) {
            WorldKind.BOUNDED -> doNothing()
            WorldKind.DEADLY  -> die()
            WorldKind.RANDOM  -> teleport()
        }
    }
}
```
   Imagine this code is written, fully tested, and shipped to production.

   Now, your boss comes to you and says: "We need a new world type: a `TORUS`
   world that wraps around like Pac-Man."

   To implement this, what files do you have to open and edit?
   1. You must open the file with the `enum` and modify it to add `TORUS`.
   2. You must scroll down into the core `GridWorld` class, find the `when`
      statement, and MODIFY it to inject a brand-new
      `WorldKind.TORUS -> wrapAround()` branch.

   THE VIOLATION:










* */


/*
            - pedantic = excessively concerned with minor details or rules;
              overscrupulous.
* */










/*
1. The "Enum Anti-Pattern" (Slides 35–46)
Imagine GridWorld can have different behaviors when the player walks off the edge:
                        https://gemini.google.com/app/ba78c2da177713b1


* */