
/*,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/

/*
___________________________________________________
@@@@@@@@@@@@@@@@@@@@@**^^""~~~"^@@^*@*@@**@@@@@@@@@
@@@@@@@@@@@@@*^^'"~   , - ' '; ,@@b. '  -e@@@@@@@@@
@@@@@@@@*^"~      . '     . ' ,@@@@(  e@*@@@@@@@@@@
@@@@@^~         .       .   ' @@@@@@, ~^@@@@@@@@@@@
@@@~ ,e**@@*e,  ,e**e, .    ' '@@@@@@e,  "*@@@@@'^@
@',e@@@@@@@@@@ e@@@@@@       ' '*@@@@@@    @@@'   0
@@@@@@@@@@@@@@@@@@@@@',e,     ;  ~^*^'    ;^~   ' 0
@@@@@@@@@@@@@@@^""^@@e@@@   .'           ,'   .'  @
@@@@@@@@@@@@@@'    '@@@@@ '         ,  ,e'  .    ;@
@@@@@@@@@@@@@' ,&&,  ^@*'     ,  .  i^"@e, ,e@e  @@
@@@@@@@@@@@@' ,@@@@,          ;  ,& !,,@@@e@@@@ e@@
@@@@@,~*@@*' ,@@@@@@e,   ',   e^~^@,   ~'@@@@@@,@@@
@@@@@@, ~" ,e@@@@@@@@@*e*@*  ,@e  @@""@e,,@@@@@@@@@
@@@@@@@@ee@@@@@@@@@@@@@@@" ,e@' ,e@' e@@@@@@@@@@@@@
@@@@@@@@@@@@@@@@@@@@@@@@" ,@" ,e@@e,,@@@@@@@@@@@@@@
@@@@@@@@@@@@@@@@@@@@@@@~ ,@@@,,0@@@@@@@@@@@@@@@@@@@
@@@@@@@@@@@@@@@@@@@@@@@@,,@@@@@@@@@@@@@@@@@@@@@@@@@
"""""""""""""""""""""""""""""""""""""""""""""""""""
* */
/*
    ... ABSTRACTION and POLYMORPHISM. This is the pivotal moment where you stop
    writing code for "this specific list" and start writing code for "any kind
    of list."

    ----------|--<>
    1. THE SCENARIO: "THE COPY-PASTE NIGHTMARE"

    Imagine you have written a useful helper function `combine` that merges two
    `SinglyLinkedLists`. Now you want to merge two `ResizingArrayLists`.

    - THE PROBLEM: Your function specifically asks for `SinglyLinkedList`. If
      you pass it an Array List, you get a TYPE MISMATCH error.
    - THE "BAD" SOLUTION (OVERLOADING): You copy-paste the function 4 times to
      handle every combination:

      1. `Link` + `Link`
      2. `Array` + `Array`
      3. `Link` + `Array`
      4. `Array` + `Link`

    ANALOGY:
    - C++: This is like writing `void print(Dog d)` and `void print(Cat c)`. If
      you get a `Bird`, you have to write `void print(Bird b)`. It's endless
      work.
    - HASKELL: This is like writing a function that works only on `Int`. If you
      want it to work on `Float`, you have to rewrite it.



    --------|-<>
    2. THE SOLUTION: INTERFACES (THE "CONTRACT")

    Instead of writing code for the specific class (the implemenation), you
    write code for the capability (the interface).

    The slide introduces `ImperialMutableList<T>`. This is an INTERFACE.
    - It doesn't have any code (bodies).
    - It just lists the methods a list MUST have: `add`, `get`, `remove`, `size`


    HOW TO IMPLEMENT IT

    ```Kotlin
    // The Contract (Interface)
    interface ImperialMutableList<T> {
        fun add(element: T)
        fun get(index: Int): T
        val size: Int
    }

    // The Signer (Class)
    // We promise that ResizingArrayList follows the rules of
    // ImperialMutableList<T>
    class ResizingArrayList<T> : ImperialMutableList<T> {

        override fun add(element: T) { ... }        // MUST PROVIDE BODY
        override fun get(index: Int) { ... }        // MUST PROVIDE BODY
        override val size: Int = ...
    }
    ```


    ANALOGY:
    - HASKELL: This is exactly like a TYPECLASS (`class Eq a where ...`). If you
      write a function `elem :: Eq a => a -> [a] -> Bool`, it works for any type
      that implements equality. You don't care if `a` is Int or a String, as
      long as it has `==`.              <-- lol can't believe i learned about typeclass and haskell `instance`'s direct RS through kotlin... and reminder that type is just typename alias... whilst data is greater but expensive...
    - C++: This is a PURE VIRTUAL CLASS (ABSTRACT BASE CLASS).

    ```C++
    class List {
        virtual void add(int x) = 0;        // Pure virtual
    };
    ```






                                                           $
   F-15 Eagle                                           .;$$$$
   Drew Shankie                                       .;$$$$$$$
   drewit@hakatac.almanac.bc.ca                     .;$$$$$$$$;
                                                  .;$$$$$$$$$$         ;$,
                                             <---<$$$$$$$$$$$'      .;$$$$'
                                           <---<$$$$$$$$$$$$',     ;$$$$$$'
                                         <--<-$$$$$$$$$$$$$$$$___,$$$$$$$"
                                   ,,,,;;$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"
                             ,,,,,$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$>>>"
                    .,:$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$'''
                    '":$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$...
                             """""$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$>>>
                                   '''''"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$;,
                                         <--<-$$$$$$$$$$$$$$$$~~~~$$$$$$$
                                           <---<$$$$$$$$$$$$,'     $$$$$$$
                                             <---<$$$$$$$$$$$,      "$$$$$,
                                                  "$$$$$$$$$$$.       "$$"
                                                    "$$$$$$$$$;
                                                      "$$$$$$$$
                                                        "$$$$$"
                                                         '$$"

    <----------==<<<<<

    3. THE PAYOFF: UNIVERSAL CLIENT CODE

    Once you have the interface, you change your helper function to accept the
    INTERFACE, not the class.

    - BEFORE (BAD): `fun combine(a: SinglyLinkedList, b: SinglyLinkedList)`
    - AFTER (GOOD): `fun combine(a: ImperialMutableList, b: ImperialMutableList)`

    Now, this single function works for Linked Lists, Array Lists, or any future
    lists you invent. This is POLYMORPHISM.



    >>>>>>=--------->
    4. THE HIDDEN TRAP: PERFORMANCE (`get(i)`)

    The slide inclues a very specific warning about the `combine` function:
    "Exercise: why is this very inefficient?"

    ```Kotlin
    for (index in 0 until second.size) result.add(second.get(index))
    ```

    - FOR ARRAY LISTS: `get(index)` is O(1) (Instant). This loop is fine (O(N)
      total).
    - FOR LINKED LISTS: `get(index)` is O(N) (Slow--it has to walk from the head
      everytime).
        - TOTAL: This loop becomes O(N^2) (Quadratic).

    THE FIX (PREVIEW): The slide mentions "Later, we will achieve this via
    ITERATORS." Iterators remember their position, so you don't have to start
    walking from `head` every single time.



    SUMMARY

    CONCEPT: INTERFACE
    EXPLANATION: A list of methods without bodies. A "promise" of behavior.
    BEST ANALOGY: Haskell TYPECLASSES / C++ ABSTRACT CLASS (Virtual Functions
                  and Methods)

    CONCEPT: CLIENT CODE
    EXPLANATION: Code that uses your classes (e.g., `main` or helper functions).
    BEST ANALOGY: The "Consumer" of your library.

    CONCEPT: POLYMORPHISM
    EXPLANATION: Using a generic variable (`List`) to hold specific objects
                 (`ArrayList`).
    BEST ANALOGY: Treating different shapes as just "Shape".

    CONCEPT: OVERLOADING
    EXPLANATION: Writing the same function multiple times for different types
                 (Bad here).
    BEST ANALOGY: C++ Function Overloading

* */

// hello

/*
                      ##
   F-15 Eagle      ###+++#
   Root             #+H+++++H#
   root@rivendel.com #HX++++++H#
                ##    #XHHHHHHHHHH#
               ##++#    #+H++++++++X#
               #++++++#  #+H+++++++++X+#
                #+++++++# #+HX++++++++++X+#
                ##HH##XXXXXXXXHX+++++++++XXX###.
                 .##HHHHHHXXXXXXXXXX+++++++++#.####
                  ##H........H.......HHHHHXXXXXXHH#HH##XXXX++
                      ###H++++###++.........###++.......+X#.#+X+#HXX++++++++++
                  ##H........H.......HHHHHXXXXXXHH#HH##XXXX++
                 .##HHHHHHXXXXXXXXXX+++++++++#.####
                ##HH##XXXXXXXXHX+++++++++XXX###.
                #+++++++# #+HX++++++++++X+#
               #++++++#  #+H+++++++++X+#
               ##++#   #+H+++++++++X#
                ##    #XHHHHHHHHHH#
                     #HX++++++H#
                    #+H+++++H#
                   ###+++#
                      ##

* */


/*
    ... from specific list implementations to a SHARED INTERFACE. This allows
    you to write one function that works for both `SinglyLinkedList` and
    `ResizingArrayList`.


    1. THE NEW INTERFACE FILE

    Create a new file (e.g., `ImperialMutableList.kt`) and define the contract
    that both list must satisfy.

    SOURCE:

    ```Kotlin
    interface ImperialMutableList<T> {
        val size: Int

        fun get(index: Int): T
        fun add(element: T)
        fun add(index: Int, element: T)
        fun clear()
        fun contains(element: T): Boolean
        fun removeAt(index: Int): T
        fun remove(element: T): Boolean
        fun set(index: Int, element: T): T
    }
    ```



    <--------==<<<
    2. UPDATE `ResizingArrayList`

    Go to your existing `ResizingArrayList` class and update the header to
    implement this new interface. You shouldn't need to change the bodies of
    your functions (since you already implemented them), but you must add the
    `override` keyword if you haven't already.

    ```Kotlin
    // Add `: ImperialMutableList<T>` to the end of the class line
    class ResizingArrayList<T>(private val initialCapacity: Int = 16)
        : ImperialMutableList<T> {

        // Ensure all these methods now have the `override` keyword
        override var size: Int = 0
            private set

        override fun get(index: Int): T { ... }
        override fun add(element: T) { ... }
        // ... and so on for all methods listed in the interface
    }
    ```

    (Note: you should also do the exact same update for your `SinglyLinkedList`
    class so that it also implements `ImperialMutableList<T>`.)

* */



/*,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/
/*,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/



/*

                      .      .'
                       :`...' `.,'  '
                   `.  ' .**.  ; ; ':
                   ` ``:`****,'  .' :
                 ..::.  ``**":.''   `.
               .:    `: ; `,'        :
                 `:    `   :         ;
                   :   :   :        ;
                   :    :   :     .:
                    :    :   :..,'  ``::.
                     `....:..'  ..:;''
                     .:   . ...::::
                    ,'''''``:::::::
                              `::::
                                `::.
                                 `::
                          . ,.    ::::'      ,..
                        .'.'  ``.  ::      .'.. `.
                       '        .: ::    ,'.'     .
                     .' ,'    .::::::   ,.'    .:::.
                   .' .'  ..:'     ::: .,   .;'     ~
                  ,;::;.::''        ::.:..::'
                 ~                  ::;'
                                    ::
                                  ,:::
                                    ::.
                                    `::
                                     ::
                                     ::
                                     ::
                                     ::
                                     ::
* */
/*
    ... we are moving from DEFINING the contract (the Interface) to SIGNING and
    ENFORCING it (Implementation).

    ... walkthrough of the implementation details, the syntax rules, and the
    common pitfalls shown...


    ------
    1. SIGNING THE CONTRACT: `override`

    When `ResizingArrayList` decides to implement `ImperialMutableList`, it must
    explicitly declare that it is replacing the interface's empty "promises"
    with real code.

    - THE SYNTAX:
        1. Add `: ImperialMutableList<T>` to the class header.
        2. Add the `override` keyword to EVERY SINGLE PROPERTY AND METHOD
           defined in the interface.

    ANALOGY:
    - C++: This is identical to the `override` keyword introduced in C++11. It
      tells the compiler "I intend to replace a virtual function from the parent
      ." If the parent changes and your function doesn't match anymore, the
      compiler throws an error (safety).
    - HASKELL: This is similar to defining an `instance`. You are saying
      `instance ImperialMutableList ResizingArrayList where ...`.
* */
// The Promise
interface ImperialMutableList2<T> {
    val size: Int           // Abstract property
    fun add(element: T)     // Abstract method
}

// The Fulfillment
class ResizingArrayList_2<T> : ImperialMutableList2<T> {
    override val size: Int = 0          // You MUST write `override` or it won't compile
    override fun add(element: T) {}
}


/*
    2. THE RULES: ALL OR NOTHING

    The slide shows a compilation error: "Class `ReisizngArrayList` is not
    abstract and does not implement abstract member..."

    - THE RULE: You cannot pick and choose. If the interface lists 5 methods,
      you must implement all 5 (unless you mark your class as `abstract`,
      passing the burden to a subclass).        <-- interesting... abstract class implementing interface... class also need to implement interface virtual functions

* */

/*
    {\o/}_______________________________________________________{\o/}
     )|(                                                         )|(
     ~|~                                        __               ~|~
      |                                     ,,-~  ~-.             |
      |                                    // . -..  ~.           |
      |           ____     .        \     // '  _     }           |
      |          {    }__  :.      __\   //,'.,._ _ ,'            |
      |        _/ ____ \ \ `::       \\~-:-: : : : :\             |
      |       // { __ \ }.} :::      { @|,;: : : : : >-           |
      |      /{,/ /_ \ \ \ }::::     `Y'/=|_\: : : :/             |
      |     {  / // \_\ }//.:::'       /  |  \-===-~              |
      |     ( {_/{(@)} }.) }:<____A___/_A_|___\,_______A_______A__|
      |      \ \\ \\/ / / };::-------v------V------v-------V------|
      |       \/\\_\_/\/_/ }`:`                                   |
      |       {  { ~  } \ /,::'                                   |
      |        \_ \__/  /~,::'                                    |
      |          {____}~ ,::'                                     |
      |                  ~                                        |
    {\o/}_________________________________________________________0 Jittlov
     )|(                                                         /V\
     ~"~                                                          H
                                                                  ^
* */


/*
                for this which is about interface classes...
                ```
                The Rule: You cannot pick and choose. If the interface lists 5 methods, you must implement all 5 (unless you mark your class as abstract, passing the burden to a subclass).
                ```
                i wonder likw what does the subclass look like in this case then?
                ----------------
    THE SHORT ANSWER: The subclass only writes `: Superclass( ... )`. It does
    NOT need to write `: Interface` again.

    Because the Abstract Class already "signed the contract" with the Interface,
    the SubClass just inherits that contract automatically.                 <--- very interesting!! and yes... i would say "Burden Passing" is indeed a very suitable way to describe this!

    Here is the breakdown of how the "Burden Passing" works.


    ------------
    1. THE INTERFACE (THE FULL CONTRACT)

    Imagine an interface with 3 methods.
* */
interface Worker {
    fun eat()
    fun sleep()
    fun work()          // This specific job
}


/*
    2. THE ABSTRACT CLASS (THE "PARTIAL" IMPLEMENTATION)

    This class implements the interface but realizes: "I know how to eat and
    sleep, but I don't know what specific `work` to do yet."

    It implements 2 methods and martks itself `abstract` to avoid implementing
    the 3rd.
* */
// 1. It Implements `Worker`
// 2. It makes itself `abstract` so it doesn't crash on the missing `work()`
//    method.
abstract class Human_2 : Worker {

    // Implemented! Burden removed.
    override fun eat() = println("I eat shit lol")

    // Implemented! Burden removed.
    override fun sleep() = println("Zzz... Zzz... Zzz...")

    // MISSING: `work()` is NOT implemented here.
    // The burden is passed down to the children.
}

/*
    3. THE SUBCLASS (THE FINAL IMPLEMENTATION)

    This class extends the `Human`. It does NOT need to mention `Worker`.

    Because `Human` already handled `eat()` and `sleep()`, this class ONLY has
    to implement the one missing method: `work()`.
* */
// Just extends Human(). Automatically becomes a Worker.
class Programmer_20 : Human_2() {

    // Finally implementing the last piece of the puzzle
    override fun work() = println("Am writing code he he he haw")
}



/*
    SUMMARY OF THE "BURDEN"
        1. INTERFACE: "You must do A, B, and C."
        2. ABSTRACT CLASS: "I will do A and B. My children must do C."
        3. SUBCLASS: "I will do C." (And it gets A and B for free).
* */


/*
                `abstract` class cannot be instantiated and must be inherited,
                abstract classes are `open` for extending by default. `open`
                modifier on the class allows inheriting it. If the class has not
                `open` modifier it is considered final and cannot be inherited.
* */
/*
    (\o/)___________________________________________________________(\o/)
    (/|\)                                                           (/|\)
      |                                          .-~~~-.              |
      |                                        /        }             |
      |                                       /      .-~              |
      |                             \        |        }               |
      |             _   __        ___\.~~-.-~|     . -~_              |
      |            / \./  \/\__      { O |  ` .-~.    ;  ~-.__        |
      |        __{^\_ _}_   )  }/^\   ~--~/-|_\|   :   : .-~          |
      |       /  /\_/^\._}_/  //  /     /   |  \~ - - ~               |
      |      (  (__{(@)}\__}.//_/__A__/_A___|__A_\___A______A_____A   |
      |       \__/{/(_)\_}  )\\ \\---v-----V----v----v-----V-----v--- |
      |         (   (__)_)_/  )\ \>                                   |
      |          \__/     \__/\/\/                                    |
      |             \__,--'                                           |
      |                                                               |
    (\o/)___________________________________________________________(\o/)
    (/|\)                                                           (/|\)
* */






/*,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/


/*,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/
/*,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/






/*,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/
/*

* finish PPT-5 kotlin
*
            * DO INFORMATION 2-4 SLIDE EXERCISES <-- ASKS GEMINI 3 TO WRITE EXERCISE AND
            * TEST CASES FOR US
*
* Do PPT-1, PPT-2 for Kotlin
* Do 40017 -> 40008 -> 40018 -> 40005
* */
/*,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸¸,ø¤º°`°º¤ø¤º°`°º¤ø,¸*/


/*
    interface in Kotlin -->>> C++ classes which ONLY have VIRTUAL methods &&
                              attributes

    abstract class in Kotlin -->>> C++ classes with AT LEAST ONE pure virtual
                                   function, but it also has standard variables
                                   (`int x`), constructors, and regular methods.

    -------<<<
    THE "WHY" (THE DIAMOND PROBLEM)
    Why does Kotlin distinguish between them if C++ basically uses `class` for
    both?

    - C++: Allows MULTIPLE INHERITANCE. You can inherit from two Classes that
      both have `int x`. This is messy (ambiguity).
    - KOTLIN: BANS Multiple Inheritance for Classes (State). You can only
      inherit ONE `abstract class` (because it holds state/variables).
    - THE LOOPHOLE: Kotlin ALLOWS Multiple Inheritance for `interfaces`. Since
      interfaces have no state (no variables), there is no conflict.



* */



