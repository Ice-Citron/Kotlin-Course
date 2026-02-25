import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/*
    ... State rollback (fixing your variable inside a `catch` block after a
    failure) is a famously tricky architectural concept. The fact that you
    crushed Coffman Deadlock avoidance in Q2 using `run` and destructuring is
    a big win.

    Let's ... return to Object-Oriented Programming PDF. ... If Part 1 was about
    mathematical rules for subtyping, Part 2 is about what happens when the code
    actually runs in memory. This covers the absolute core of Object-Oriented
    Polymorphism.

    ... breakdown of the 5 core concepts from this section, ...


---
1. APPARENT vs. ACTUAL TYPES (The Stack vs. The Heap)
    Every object reference in Kotlin has two identities:s

    - APPARENT TYPE: The type written in the code (or inferred by the compiler).
      It lives on the Stack, is fixed at compile-time, and NEVER CHANGES. It
      dictates what methods you are allowed to call.s
    - ACTUAL TYPE: The physical object living in the computer's memory (the
      Heap) when the program runs. It can change if the variable is reassigned,
      and it is always a subtype (or the exact same type) of the Apparent Type.

    EXPLAIN BY EXAMPLE:
*/
// APPARENT TYPE: Lamp (Explicitly declared label)
// ACTUAL TYPE: DimmingLamp (The physical object created in memory)
var myLamp_2: Lamp = DimmingLamp()

// myLamp.pressSwitch()         // ALLOWED: The Apparent Type (Lamp) has a switch.

// myLamp.down()                // COMPILER ERROR: The compiler only checks the
                                // Apparent Type (Lamp).

// Reassigning changes the ACTUAL type, but the APPARENT type remains `Lamp`.
/*
fun main() {
    myLamp_2 = Lamp()
}
 */


/*
    - WHEN DO I USE THEM: You use this concept constantly to design flexible
      systems. You declare variables with a broad Apparent Type so they can hold
      many different Actual Types over their lifetime.
    - WHY DO I USE THEM: It gives you strict compiler safety. Even if `myLamp`
      is actually a `DimmingLamp`, because its Apparent type is `Lamp`, the
      compiler will block you from calling `myLamp.down()`. This prevents
      developers from accidentally relying on subclass-specific features when
      writing generic code.
    - WHEN DO I NOT USE THEM AND WHY: Don't mismatch types if you strictly need
      the subclass features right away. If yu are writing a variable to
      explicitly test the dimming hardware, set the Apparent type to
      `DimmingLamp` so you can actually access the `.down()` method.
    - WHY THEM OVER OTHER USE CASES: In dynamic languages (like Python or
      JavaScript), there are no Apparent Types. You can call `.down()` on
      anything, and the app will just violently crash at runtime if the method
      doesn't exist. Kotlin's Apparent Types guarantee safety before you even
      run the code.


---
2. LATE BINDING (Dynamic Method Dispatch)
    If the compiler only checks the Apparent Type to see if a method exists, how
    does it know whose code to run if the child class overrode it? Kotlin uses
    DYNAMIC DISPATCH (Late Binding) to decide at runtime.

...




* */


/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    In Kotlin (and JVM languages generally), LATE BINDING (or dynamic binding)
    refers to the mechanism where the specific method or function to be
    executed is determined at RUNTIME, rather than at compile time.

    Yes, EARLY BINDING (or static binding) is definitely a thing, and it is the
    default behavior for most non-polymorphic function calls. "Normal binding"
    is not a formal technical term, but it is often used informally to describe
    early binding.

    ...


---
1. LATE BINDING (Dynamic Binding / Runtime)
    Late binding occurs when the compiler cannot determine the exact method to
    call because it depends on the concrete type of an object, not the reference
    type.

    - WHEN IT HAPPENS: When calling `open` or `override` methods on an interface
      or class.
    - MECHANISM: The JVM uses a virtual method table (v-table) to look up the
      correct implementation at runtime.
    - EXAMPLE:

```Kotlin
open class Animal {
    open fun sound() = println("Animal makes sound")
}

class Dog : Animal() {
    override fun sound() = println("Dog barks")
}

fun main() {
    val myAnimal: Animal = Dog()        // Reference is Animal, object is Dog
    myAnimal.sound()                // Late Binding: The JVM decides at runtime to call Dog.sound()
}
```


---
2. EARLY BINDING (Static Binding / Compile Time)
    Early binding occurs when the compiler has enough information to resolve the
    method call to a specific implementation during the compilation phase.

    - WHEN IT HAPPENS: When calling `private`, `final` (default in Kotlin), or
      `static` (companion object) methods.
    - ADVANTAGE: Faster performance because there is no runtime lookup.
    - EXAMPLE:

```Kotlin
class Calculator {
    fun add(a: Int, b: Int) = a + b             // Early binding
}
```








* */












/*
*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*
*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*

######   #######  #######  #######    ##      ##  #########  ##      ##
#######  #######  #######  #######    ###     ##  #########  ##      ##
##   ##  ##       ##   ##  ##         ####    ##  ##         ##  ##  ##
##   ##  #####    #######  #####      ## ##   ##  ######     ##  ##  ##
##   ##  #####    #######  #####      ##  ##  ##  ######     ##  ##  ##
##   ##  ##       ##   ##  ##         ##   ## ##  ##         ##  ##  ##
#######  #######  ##   ##  ##         ##    ####  #########  ##########
######   #######  ##   ##  ##         ##      ##  #########  ##########


           ##      ##  #######  ########  ##        ######
           ##      ##  #######  ########  ##        #######
           ##  ##  ##  ##   ##  ##    ##  ##        ##   ##
           ##  ##  ##  ##   ##  #######   ##        ##   ##
           ##  ##  ##  ##   ##  ######    ##        ##   ##
           ##  ##  ##  ##   ##  ##  ##    ##        ##   ##
           ##########  #######  ##   ##   ########  #######
           ##########  #######  ##    ##  ########  ######

*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*
*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*
* */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */

/*
    ...

    - WHEN DO I USE THEM: This happens automatically in Kotlin whenever you call
      an `open`/`override` method.
    - WHY DO I USE THEM: It allows your code to trigger different behaviors from
      a single, generic line of code. You can pass a `DimmingLamp`, a
      `StrobeLamp`, or a `HeatLamp` into `manipulateLamp()`, and the correct
      specific code will run without you needing to change the function.
    - WHEN DO I NOT USE THEM AND WHY: Dynamic dispatch has a microscopic
      performance penalty because the CPU has to do a lookup at runtime to find
      the correct method address. If a method should never be overridden, remove
      the `open` keyword so it becomes `final`.
    - WHY THEM OVER OTHER USE CASES: Without Dynamic Dispatch, you would have to
      write massive `if/else` or `when` blocks to manually check the object's
      type and execute the right logic for every single subclass in existence.


---
3. THE TRAP: STATIC DISPATCH FOR EXTENSION FUNCTIONS
    Static methods use Dynamic Dispatch (checking the Actual Type). However,
    EXTENSION FUNCTIONS use STATIC DISPATCH. This means the code to execute
    is determined at compile-time based purely on the APPARENT TYPE. The actual
    object in memory is completely ignored.

```Kotlin

open class A
class B : A()

// Extension functions defined outside the cklass
fun A.sayGoodbye() = println("Goodbye from A")
fun B.sayGoodbye() = println("Goodbye from B")

fun main() {
    val myObject: A = B()       // Apparent = A, Actual = B

    // STATIC DISPATCH! The compiler strictly uses the Apparent Type (A).
    myObject.sayGoodbye()               // Prints: "Goodbye from A"
}
```

    - WHEN DO I USE THEM: Understand this rule so you don't introduce bugs when
      adding utility functions to classes you don't own (like extending Kotlin's
      built-in `String` class).
    - WHY DO I USE THEM: Static Dispatch is incredibly fast because the computer
      mapped out exactly which line of code to jump to before you even ran the
      app.
    - WHEN DO I NOT USE THEM AND WHY: NEVER use extension functions to try and
      override or mimic Polymorphism. As shown above, they do not care about the
      Actual type in memory. If you need a child class to behave differently,
      use standard class inheritance and `override` functions.
    - WHY THEM OVER OTHER USE CASES: Extension functions literally just compile
      down to static helper functions. They don't physically belong to the
      object, which is why they only look at the Apparent type.
* */















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
4. PROGRAMMING AGAINST AN INTERFACE
    This is one of the most important best practices in software engineering.
    When declaring variables or function parameters, always use the broadest
    interface (like `MutableList`), not the specific concrete class (like
    `ArrayList`).

    EXPLAIN BY EXAMPLE:

```Kotlin

class DataStore {
    // BAD: Tight Coupling. Code relies on ArrayList specifically.
    private val badData: ArrayList<Int> = ArrayList()

    // GOOD: Loose Coupling. Code only relies on the MutableList contract.
    private val goodData: MutableList<Int> = ArrayList()
}

// GOOD: The function only demands the `List` service.
fun filterWords(words: List<String>) { ... }
```

    - WHEN DO I USE THEM: Whenever you declare properties, function parameters,
      or return types. Always ask yourself: "Do I specifically need an
      `ArrayList`, or do I just need something that acts like a generic
      `MutableList`?"
    - WHY DO I USE THEM: MAINTAINABILITY. If tomorrow you decide a `LinkedList`
      performs better than an `ArrayList` for your specific dataset, you only
      have to change the constructor `= LinkedList()`. Because the Apparent
      Type is `MutableList`, the rest of your app doesn't care and nothing
      breaks.
    - WHEN DO I NOT USE THEM AND WHY: You only program against a concrete class
      if you specifically need a method that only exists in that class. For
      example, `ArrayList` has a method called `trimToSize()`. `MutableList`
      does not.
    - WHY THEM OVER OTHER USE CASES: Tight coupling (hardcoding specific
      classes) makes code brittle. Programming against interfaces creates a
      "pluggable" architecture where underlying components can be swapped out
      easily.
* */












/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
5. SMART CASTS (`is`) AND THEIR LIMITS
    If we program against `MutableList` to be safe, but we suddenly realise we
    desperately need to call `ArrayList`'s exclusive `trimToSize()` method,
    how do we do it without breaking our architecture? We use the `is` keyword.

EXPLAIN BY EXAMPLE:
* */

fun main2() {
    val list: MutableList<String> = ArrayList()

    if (list is ArrayList) {
        // SMART CAST! The compiler tem[orarily upgrades the Apparent Type to
        // ArrayList inside these brackets. We can now safely call specific
        // methods!
        list.trimToSize()
    }

    // THE LIMITATION:
    var mutableLamp: Lamp = DimmingLamp()       // <-- ah it's because mutableLamp is a `var`
    if (mutableLamp is DimmingLamp) {           // <-- so it seems that the trick of smart casting only works for `val`!
        // mutableLamp.down()       // COMPILER ERROR! Smart cast to `DimmingLamp` is impossible.
    }
}
/*
    - WHEN DO I USE THEM: Use `is` when you have a broad Apparent Type but need
      to temporarily unlock the specific powers of an Actual subclass safely.
    - WHY DO I USE THEM: It saves you from writing clunky manual casting code.
      The compiler proves the type is safe, and automatically adapts.
    - WHEN DO I NOT USE THEM AND WHY: As shown in... SMART CASTS WILL FAIL ON
      MUTABLE `var` PROPERTIES! Why? Because another thread might change
      `mutableLamp` to a standard `Lamp` in the split-second between the `if`
      check and the `.down()` call. The compiler recognises this danger and
      refuses to smart-cast a `var`.
    - WHY THEM OVER OTHER USE CASES: In Java, you'd have to write
      `if (list instanceOf arrayList) { ((ArrayList)list).trimToSize() }`.
      Kotlin's smart casts eliminate this awful, repetitive boilerplate.
* */












/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    It is completely fine that state rollback tripped you up! Fixing your
    variables inside a `catch` block after a failure (State Hygiene) is a
    famously tricky architectural concept that even senior devs get wrong.

    ...

    If Part 1 was about the mathematical rules of types, Part 2 is about WHAT
    HAPPENS WHEN THE CODE ACTUALLY RUNS IN MEMORY. Because you are getting
    stronger, I am giving you 5 advanced scenarios right away.

    These questions fuse APPARENT vs. ACTUAL TYPES, CONCURRENCY (LOCKS), and
    SCOPE FUNCTIONS together. ... toned down the hints. Focus on how the
    compiler reads the types vs. how the JVM executes them.
* */

/*
    In Kotlin, the primary difference (between `open class` and
    `abstract class`) is INSTANTIABILITY and REQUIREMENT: an `abstract` class is
    a "blueprinted" shell that cannot be instantiated on its own and forces
    subclasses to implement any abstract members it contains, whereas an `open`
    class is a fully functional, "finished" class that simply grants permission
    for other classes to inherit from it and override its logic. Essentially,
    use `abstract` when the parent class is too incomplete to exist as an object
    (lke a generic `Drone`), and use `open` when the parent is already a working
    object (like a `StandardInterceptor`) that you just want to allow
    specialised versions of.
* */






/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
// QUESTION 1: The Apparent Type Restriction (`apply` + `Thread`)

open class Job {
    open fun execute() = println("Generic Job")
}

class BackgroundJob : Job() {
    override fun execute() {}
    fun cancel() = println("Cancelled")
}

fun main3() {

    val myJob: Job = BackgroundJob()    // APPARENT: `Job`, ACTUAL: `BackgroundJob`
    Thread() {
        if (myJob is BackgroundJob) myJob.cancel()
    }.apply {
        name = "Worker"
        start()
        join()
    }
}



















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
// QUESTION 2: Late Binding in a Critical Section (`also` + `withLock`)

class JobQueue(
    private val lock: ReentrantLock = ReentrantLock(),
    private val queue: MutableList<Job> = ArrayList()
) {
    fun processAll() {
        lock.withLock {
            // `also` gives us the object to print BEFORE returning it to call
            // `.execute()`     // DYNAMIC DISPATCH: Prints "Background Job"
            // because it looks at the Actual Type!
            queue.map { it.also { println("Processing job...") }.execute() }
        }
    }
}
















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
// QUESTION 3: THE EXTENSION FUNCTION TRAP (`with`)

open class Alert()

class CriticalAlert() : Alert()

fun Alert.sound() = println("Beep")
fun CriticalAlert.sound() = println("NEE NAW NEE NAW!")

fun trigger(alert: Alert) {
    with (alert) { sound()}
}

fun main4() {
    trigger(CriticalAlert())
}








/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
// QUESTION 4: SWAPPABLE IMPLEMENTATIONS (`run` + INTERFACE)

class User25(
    val id: Int,
    val name: String
)

class userRegistry(
    var users: MutableMap<Int, User25>,
    private val lock: ReentrantLock = ReentrantLock()
) {
    fun register(id: Int, name: String): Int {
        return lock.withLock {
            val newSize = run {
                users[id] = User25(id, name)
                users.size
            }
            println("Registered. Total users: $newSize")
            newSize
        }
    }
}

fun main() {
    val map: ConcurrentHashMap<Int, User25> = ConcurrentHashMap()
    val registry: userRegistry = userRegistry(map)
}















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */

open class Lamp2


class DimmingLamp2 : Lamp2() {
    fun down() = println("Dimming")
}

class SmartHome(
    val currentLamp: Lamp2 = DimmingLamp2(),
    private val lock: ReentrantLock = ReentrantLock()
) {
    fun dimIfPossible() {
        lock.withLock {
            currentLamp.let { if (it is DimmingLamp2) it.down() }

            // Pro-tip alternative using the safe-cast operator:#
            // `(currentLamp as? DimmingLamp)?.let { it.down() }`
        }
    }
}














/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
    ... Mastering the "Check-Then-Act" race condition and the Coffman Deadlock
    avoidance puts you way ahead of the curve. ... bridged the gap betweens
    Object-Oriented theory and real-world multithreaded architecture!

    Let's bring it all home. Here is the final breakdown... This section is all
    about HANDLING EDGE CASES SAFELY, PROTECTING YOUR INTERNAL DATA, AND KNOWING
    WHEN NOT TO USE INHERITANCE.


---
1. DOWN-CASTING (`as`) AND ITS DANGERS
    We learned earlier that Smart Casts (`is`) temporarily upgrade an object's
    Apparent Type safely. However, if the compiler refuses to Smart Cast (
    like with a mutable `var` that might be changed by another thread), you can
    forcefully tell the compiler to upgrade the type using a DOWN-CAST (`as`).

    EXPLAIN BY EXAMPLE:
* */
fun main610() {
    val myLamp: Lamp2 = DimmingLamp2()

    // myLamp.down()        // ERROR: Apparent type is Lamp2

    // DOWNCAST: We forcefully tell the compiler, "Trust me, it's a DimmingLamp2"
    val specificLamp = myLamp as DimmingLamp2
    specificLamp.down()     // Works!

    // THE DANGER:
    val standadLamp: Lamp2 = Lamp2()
    // val brokenLamp = standardLamp as DimmingLamp2        // CRASH! ClassCastException
}
/*
    - WHEN DO I USE THEM: When you are mathematically 100% certain that the
      Actual Type in memory is the subclass, but the compiler's strict rules
      prevent a Smart Cast (`is`).
    - WHY DO I USE THEM: It instantly gives you access to the subclass's
      specific methods (like `.down()`).
    - WHEN DO I NOT USE THEM AND WHY: Never use them as a "guess." If you
      downcast an object and you are wrong about its Actual Type, your
      application immediately crashes with a `ClassCastException`. Kotlin allows
      a safer alternative: `as?` (Safe Cast), which returns `null` instead of
      crashing if the cast fails.
    - WHY THEM OVER OTHER USE CASES: Sometimes framework libraries return `Any`
      (like getting data from an old database). You have to downcast it to a
      `String` or a custom class to actually use it.
* */















/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*
2. ENCAPSULATING MUTABLE DATA (The "Friends" Challenge)
    When an object holds a mutable collection (like a list of friends), you want
    clients to be able to see the friends, but not arbitrarily add to them
    without going through your offiial `considerFriendRequest()` method. The
    slides present 3 OPTIONS to solve this.

    EXPLAIN BY EXAMPLE:
* */
class User664(
    val name: String,
    private val _friends: MutableSet<User664> = mutableSetOf()
    // Private internal state (Vulnerable if exposed directly)
) {
    // OPTION 1: The "View" (Fast, but slightly vulnerable)
    // Exposes the exact same list, but masks it behind the read-only `Set` interface
    val friendsOpt1: Set<User664>
        get() = _friends

    // OPTION 2: The "Copy" (100% safe, but uses extra memory)
    // Generates a brand new list every time it is called
    val friendsOpt2: Set<User664>
        get() = _friends.toSet()            // <-- looks like we should just use this one!

    // OPTION 3: The "Wrapper" (Safe and updates instantly, but crashes on
    // mutation attempts). Uses Java's unmodifiable wrapper
    val friendsOpt3: Set<User664>
        get() = java.util.Collections.unmodifiableSet(_friends)
}
/*
    - WHEN DO I USE THEM: Every time your class manages an internal list, map,
      or set that outsiders need to read.
    - WHY DO I USE THEM: It protects your object's internal state. If you expose
      a raw `MutableSet`, anyone can add fake data to your class and bypass your
      logic.
    - WHEN DO I NOT USE THEM AND WHY:
        - Do not rely on OPTION 1 (THE VIEW) if you don't trust the code calling
          it. A malicious developer could forcefully downcast it:
          `(user.friendsOpt1 as mutableSet<User664>).add(hacker)`.
        - Do not use OPTION 2 (THE COPY) if the list is massive (e.g., 100,000
          items) and called frequently, as it duplicates the entire list in
          memory every time it is accessed.
    - WHY THEM OVER OTHER USE CASE: OPTION 3 (The Wrapper) is often the industry
      gold standard for large datasets. It doesn't duplicate memory, but if a
      hacker tries to downcast and modify it, the wrapper actively fights back
      and intentionally throws an `UnsupportedOperationException`.
* */









/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */
/*  ----    ----    ----    ----    ----    ----    ----    ----    ----    */




/*
            Gemini said: https://gemini.google.com/app/61f5e835c1f55bc4
                I completely understand why it feels confusing! You have actually nailed the three foundational classes related to inheritance, but that isn't quite the whole picture.

                Kotlin introduces several extra "specialty" classes. Instead of making you write hundreds of lines of boilerplate code (like you would in traditional Java), Kotlin gives you specific keywords to handle common scenarios instantly.

                Here is your complete cheat sheet for every type of class in Kotlin:
* */
