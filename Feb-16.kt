import java.util.concurrent.locks.ReentrantLock
import javax.naming.AuthenticationException

/*
   You cannot read or write professional, idiomatic Kotlin without mastering
   SCOPE FUNCTIONS.

   In traditional programming, if you want to perform 5 operations on an object,
   you have to repeat its name 5 times.

   A SCOPE FUNCTION creates a temporary "bubble" (a scope) in your code. Inside
   this bubble, the object becomes the center of attention. You can interact
   with it directly without repeating its name.

   They do not introduce any new technical capabilities that you couldn't
   achieve with normal variables; they exist purely to improve architectural
   readability and safety.

   Kotlin has FIVE scope functions: `let`, `apply`, `also`, `run` and `with`.

   To understand which one to use, you only need to memorise a 2x2 grid based
   on 2 questions:
      1. HOW DO I REFER TO THE OBJECT INSIDE THE BUBBLE? (Do I act like I am
         inside the object using `this`, or is the object handed to me as a
         parameter using `it`?)
      2. WHAT DOES THE BUBBLE SPIT OUT AT THE END? (Does it hand back the object
         itself, or does it spit out a new computed result?)



---
THE CHEAT SHEET MATRIX:

   Function // Inside the bubble, the object is: // What the bubble returns // Primary Use Case

   `apply` // `this` (you can omit `this.`) // THE OBJECT ITSELF //
       Object configuration (Builder pattern)
   `let` // `it` (you can rename it) // LAMBDA RESULT //
       Null-checks (`?.let`) && Mapping
   `also` // `it` (you can rename it) // THE OBJECT ITSELF //
       Sneaking in side-effects (Logging)
   `run` // `this` (you can omit `this.`) // LAMBDA RESULT //
       Setup + Calculation
   `with` // `this` (you can omit `this.`) // LAMBDA RESULT //
       Grouping calls on an existing object



---
1. `apply` (THE CONFIGURATOR)
   - OBJECT REFERENCE: `this`
   - RETURNS: The original object.

   When and Why to use it:
      You use `apply` for OBJECT CONFIGURATION. Whenever you are building a
      brand-new object and need to set multiple properties immediately after
      creating it, use `apply`.
* */

/*
class Server (
    var port: Int = 0,
    var host: String = ""
) {
    fun start(): Int = 0
}

fun main() {
    // The Old Way:
    val server = Server()
    server.port = 8000
    server.host = "localhost"
    server.start()

    // The Kotlin `apply` Way:
    val server2 = Server().apply{
        // Inside this bubble, we act as if we are INSIDE the Server class!
        port = 8000                 // `this.port` is implied
        host = "localhost"
        start()
    }       // It automatically returns the fully configured `server` object
}
 */





/*
2. `let` (The Null-Savior & Transformer)
   - OBJECT REFERENCE: `it`
   - RETURNS: The result of the last line of code in the bubble.

   WHEN AND WHY TO USE IT:
   95% of the time, `let` is used for NULL-SAFETY. If you have an object that
   might be missing (`null`), you use the safe-call operator `?.let`. It
   translates to: "If this object physically exists in memory, step into this
   bubble and let me use it. If it is null, skip this entire block."
* */

/*
var userEmail: String? = "admin@ic.ac.uk"

fun main() {
    // The Old Way:
    if (userEmail != null) {
        val length = userEmail?.length
        println("Length is $length")
    }

    // The Kotlin `let` Way:
    val emailLength = userEmail?.let { email ->
        // We only enter this bubble if userEmail is NOT null.
        // We renamed `it` to `email` so it is crystal clear.
        println("Processing email: $email")

        email.length // The last line is what gets returned and saved to
                     // `val emailLength`
    }
}
 */









/*
3. `also` (THE SIDE-EFFECT/OBSERVER)
   - OBJECT REFERENCE: `it`
   - RETURNS: The original object.

   WHEN AND WHY TO USE IT:
      You use `also` when you are in the middle of a chain of operations, and
      you want to sneak in a SIDE-EFFECT (like logging, printing, or validation)
      without interrupting the flow of data. It literally translates to: "Here
      is my object, and ALSO do this quick thing with it, then hand the object
      right back."
* */

// We want to create a user, log it to the console, and return it
fun createUser(): User{
    return User(35).also {
        // `it` is the User. We log it, but `also` ignores the print statement
        // and safely returns the `User` object anyway!
    }
}








/*
4. `run` (THE CALCULATOR)
   - OBJECT REFERENCE: `this`
   - RETURNS: The result of the last line of code.

   WHEN AND WHY TO USE IT:
      `run` is a hybrid. It is basically `apply` and `let` mashed together. You
      use it when you need to configure an object (using `this`), but at the
      end of the configuration, you want to spit out a MATH CALCULATION OR A
      BOOLEAN, not the object itself.
* */

/*
class PasswordValidator(
    var minimumLength: Int = 0,
    var requireNumbers: Boolean = false
) {
    fun validate(input: String): Boolean = true
}


fun main() {
    val password = PasswordValidator()

    val isValid: Boolean = password.run {
        // `this` is the PasswordValidator
        minimumLength = 8
        requireNumbers = true

        // The last line calculates a boolean and returns it to `isValid`
        validate("MySecret123")
    }
}
 */

/*----------------------------------------------------------------------------*/
/*----------------------------------------------------------------------------*/


/*
5. `with` (THE GROUPER)
   - OBJECT REFERENCE: `this`
   - RETURNS: The result of the last line of code.

WHEN AND WHY TO USE IT:
   `with` is almost identical to `run`, ut it is written differently. It is not
   an extension function (you don't write `object.with { ... }`). You pass the
   object into it: `with(object) { }`.

   You can use this when you already have a pre-existing object (that is
   definitely not null), and you just want to group a bunch of method calls
   together purely for visual readability. It translate to: "With this object,
   do the following..."
* */
class DatabaseConnection() {
    fun connect() {}
    fun authenticate(name: String, pwd: String) {}
    fun fetchData() {}
    fun disconnect() {}
}
val database = DatabaseConnection()

/*
fun main() {
    with(database) {
        connect()
        authenticate("admin", "1234")
        fetchData()
        disconnect()
    }
}
 */





/*
WHEN NOT TO USE SCOPE FUNCTIONS (The Anti-Patterns)
   Scope functions are addictive. Once you learn them, you will want to use them
   everywhere. DO NOT DO THIS. Used poorly, they create a completely unreadable
   mess. Here is where you should strictly avoid them:

   1. "SPAGHETTI SCOPING" (NESTING)
      If you put a `let` inside an `apply` inside a `run`, the cognitive load on
      the human brian reading the code becomes impossible.
```Kotlin
// AWFUL CODE:
window.apply {
    title = "Main menu"
    user?.let {
        database.run {
            // Wait, what does `this` mean right now? Is it the window? The
            // database??? or what. What is `it`? The user?
            save(it.name)

        }
    }
}
```

    THE RULE: Never nest scope functions that use `this`. if you absolute must
    nest functions that use `it`, explicitly rename `it` (e.g.,
    `user?.let { current -> ... }`) so you know exactly what data you are
    touching. When in doubt, just use normal variables.



---
2. OVERCOMPLICATING SIMPLE `if/else` LOGIC
   People often try to be "clever" by replacing standard `if (x != null)` blocks
   with scope functions and the Elvis operator `?:`.

```Kotlin
// BAD (Mentally exhausting to read):

...

// GOOD (Boring, but instantly readable):
if (user != null) {
    process(user)
} else {
    showError()
}
```




3. WHEN `this` CAUSES "SHADOWING"
   If your outer class has a property called `name`, and you use `apply` on an
   object that also has a property called `name`, they shadow each other. The
   compiler (and human readers) will get confused about which `name` you are
   assigning.

   THE FIX: If there is a naming collision, drop `apply`/`run` and use
   `let`/`also` instead. Writing `it.name` explicitly separates the object's
   variable from your class's variable.
* */
















/*----------------------------------------------------------------------------*/
/*----------------------------------------------------------------------------*/


/*
   ... covers a fantastic lecture on ADVANCED OBJECT-ORIENTED DESIGN, SUBTYPING
   , AND POLYMORPHISM in Kotlin.

   ... 7 CORE CONCEPTS.


---
1. SUBTYPING AND TYPE HIERARCHIES (`Any`, `Nothing`, and `?`)
   Subtyping is the mathematical rule that if a system expects a broad type
   (Supertype), you can safely provide a more specific type (Subtype) because
   the Subtype offers at least the same behaviors/services.
      - `Any` is the ultimate supertype (everything is a subtype of it).
      - `Nothing` is the ultimate subtype (it is a subtype of everything, but
        has non actual values).

   EXPLAIN BY EXAMPLE:
* */

// 1. Any (Top of hierarchy)
val item: Any = "Hello"     // OK: String is a subtype of Any.

// 2. Nullability
var nullableName: String? = "Alice"     // OK: String is a subtype of String? (Nullable)

// 3. Nothing (Bottom of hierarchy)
fun crashApp(): Nothing {
    throw Exception("Fatal Error")      // Never returns successfully
}
val score: Int = if (true) 100 else crashApp()  // OK: Nothing is a valid subtype


/*
   - WHEN DO I USE THEM: You rely on subtyping implicitly anytime you pass an
     object into a function that asks for a broader category (e.g., passing an
     `Int` to a function expecting a `Number`). Use `Nothing` specifically for
     functions that always throw exceptions or loop infinitely.
   - WHY DO I USE THEM: It allows you to write highly reusable code. A function
     that accepts `Number` can process integers and decimals without needing to
     be rewritten.
   - WHEN DO I NOT USE THEM AND WHY: Do not use `Any` as a crutch just because
     you are too lazy to figure out the strict type. You will lose all compiler
     safety. Also, remember that `String?` is NOT a subtype of `String`. A
     nullable type offers a smaller service than a guaranteed type, so you
     cannot pass a nullable variable into a function expecting a strict,
     non-null variable.
   - WHY THEM OVER OTHER USE CASES: Kotlin's type hierarchy is vastly superior
     to Java's because it natively factors in NULLABILITY at compile-time,
     mathematically proving your code won't throw random `NullPointerException`.
* */









/*----------------------------------------------------------------------------*/
/*----------------------------------------------------------------------------*/
/*
2. APPARENT vs. ACTUAL TYPES && LATE BINDING (Dynamic Dispatch)

   When you create a variable, it actuall has two types: the type you declare it
   as (APPARENT TYPE), and the type of the object physically living in memory
   when the code runs (ACTUAL TYPE).

   EXPLAIN BY EXAMPLE
* */
open class Lamp {
    open fun pressSwitch() = println("Standard Lamp")
}

class DimmingLamp : Lamp() {
    override fun pressSwitch() = println("Dimming Lamp")
    fun dimDown() = println("Dimming ...")
}

// APPARENT TYPE: Lamp (Determined at Compile-Time)
// ACTUAL TYPE: DimmingLamp (Determined at Run-Time)
val myLamp: Lamp = DimmingLamp()

/*
fun main() {
    // 1. What methods can I call? (Dictated by APPARENT type)
    myLamp.pressSwitch()            // OK (Lamp has this)
    // myLamp.dimDown()         // ERROR: Compiler only knows this is a "Lamp". ...
                                // subclass methods aren't seen

    // 2. Whose code is executed? (Dictated by ACTUAL type via "Late Binding")
    myLamp.pressSwitch()            // ...
}
 */

/*
   - WHEN DO I USE THEM: You use this heavily in Polymorphism--treading a bunch
     of different subclasses as their generic parent class (like looping through
     a `List<Lamp>`).
   - WHY DO I USE THEM: It allows LATE BINDING (Dynamic Dispatch). The program
     waits until the exact moment the code runs to check the actual object in
     memory and runs its specific, overridden version of the method.
   - WHEN DO I NOT USE THEM AND WHY: Be very careful with EXTENSION FUNCTIONS.
     Extension functions use Static Dispatch (they are bound at compile time
     based strictly on the Apparent type). If you write an extension function
     for `Lamp`, calling it on `myLamp`  will trigger `Lamp`'s extension, not
     `DimmingLamp`'s!
   - WHY THEM OVER OTHER USE CASES: Separating apparent and actual types
     prevents you from having to write massive, ugly `if/else` checks everywhere
     to figure out what specific object you are holding before telling it what
     to do.
* */




/*
3. PROGRAMMING AGAINST AN INTERFACE
   Instead of tying your variable to specific, concrete implementations, you
   should tie them to an interface.

   EXPLAIN BY EXAMPLE:
*/
class DataStore {
    // BAD: Programmed against a concrete implementation
    private val badData: ArrayList<Int> = ArrayList()

    // GOOD: Programmed against an interface
    private val goodData: MutableList<Int> = ArrayList()
}


/*
      - WHEN DO I USE THEM: Whenever you declare variables, function parameters,
        or return types for collections (`List`, `Set`, `Map`) or your own
        custom services.
      - WHY DO I USE THEM: FLEXIBILITY. If tomorrow you realise `LinkedList` is
        more memory efficient than `ArrayList` for your app, you only have to
        change the `= ArrayList()` part. The rest of your app only knows it is a
        generic `MutableList`, so nothing else breaks.
      - WHEN DO I NOT USE THEM AND WHY: If your code absolutely requires a
        specific feature of a concrete class. For example, `ArrayList` has a
        method called `trimToSize()`. `MutableList()` does not. If you
        explicitly need to call `trimToSize()`, your apparent type must be
        `ArrayList`.
      - WHY USE THEM OVER OTHER USE CASES: Hardcoding specific classes tightly
        couples your code. If a function asks for an `ArrayList`, nobody can
        ever pass it a `LinkedList`.



---
4. SMART CASTS (`is`) AND DOWNCASTS (`as`)
   If you programmed against an interface, but you temporarily need a feature
   from the specific actual type, you have to cast it.

   EXPLAIN BY EXAMPLE:
*/
val list_: MutableList<String> = ArrayList()

fun main() {
    // SMART CAST (Safe)
    if (list_ is ArrayList) {
        // Inside this block, the compiler automatically upgrades the Apparent
        // Type to ArrayList. We can now safely call ArrayList-specific features
        list_.trimToSize()
    }

    val arrayList_ = list_ as ArrayList
    arrayList_.trimToSize()
}


/*
   - WHEN DO I USE THEM: Use `is` when you have a broad Apparent type but need
     to temporarily unlock the specific powers of an Actual subclass safely.
   - WHY DO I USE THEM: `is` (Smart Casting) is brilliant because the compiler
     does the work for you. It checks the type, and if true, automatically
     morphs the variable for the rest of the block.
   - WHEN DO I NOT USE THEM AND WHY:
       1. Avoid using `as` (Down-casting). If you use `as ArrayList` and the
          object is actually a `LinkedList`, your application will immediately
          crash with a `ClassCastException`.
       2. Smart Casts (`is`) WILL NOT WORK on mutable `var` properties! Why?
          Because another thread might have changed the variable to a different
          actual type in the split-second between the `if` check and the method
          call.
   - WHY THEM OVER OTHER USE CASES: In Java, you have to manually check and cast
     : `if (x instanceOf String) { ((String)x).length(); }`. Kotlin's smart
     casts eliminate this awful boilerplate,





---
5. ENCAPSULATION: THE "FRIENDS" DESIGN CHALLENGE
   When your class holds a mutable list internally, you want outsiders to be
   able to read it without modifying it.

  EXPLAIN BY EXAMPLE
* */
class User_4(val name: String) {
    // Internal state: Mutable (Can only be changed inside this class)
    private val _friends: MutableSet<User> = mutableSetOf()

    // The "Backing Property" View (External use)
    // Outsiders see a read-only Set.
    val friends: Set<User>
        get() = _friends
}


/*
   In Kotlin, PROPERTIES are more than just fields; they are a combination of a
   field and its accessors (GETTER and SETTER). When you declare a property like
   `val` or `var`, Kotlin automatically generates these for you under the hood.
   You only need to write out `get()` or `set()` explicitly when you want to add
   custom logic--like validating a value before it's saved or calculating a
   value on the fly instead of storing it in memory.


   THE BACKING PROPERTY PATTERN
   ... shows a classic "Backing Property" pattern used for ENCAPSULATION.
   - `private val _friends`: This is the "source of truth." It is a mutable set
     that only the class itself can modify.
   - `val friends`: This is the public-facing "view". It has a custom `get()`
     that simply returns the private set.
     By exposing it as a read-only `Set` rather than a `MutableSet`, you prevent
     outsiders from bypassing your class logic and adding friends directly.


   CUSTOM SETTERS AND COMPUTED GETTERS
      Custom setters are useful for VALIDATION. For example, in a `User` class,
      you might want to ensure an `age` cannot be negative. The keyword `field`
      is used inside the setter to refer to the actual stored value without
      causing an infinite loop.
* */
class BackingProperty {
    var age: Int = 0
        set(value) {
            if (value >= 0) field = value       // `field` is the actual backing storage
        }

    // A computed getter (no storage used)
    val isAdult: Boolean
        get() = age >= 18
}


/*
KEY USE CASE
   - PROTECTION/VALIDATION: Ensuring a `ResizingArrayList` capacity is never
     negative by checking the value in the setter,
   - COMPUTED DATA: A `PageElement` interface might provide a default `area`
     property that isn't stored, but instead calculated as `width * height`
     every time it is called.
   - LAZY LOADING/CACHING: Storing an expensive calculation in a private
     nullable variable and using a custom `get()` to calculate it only once when
     first requested.
*/






/*----------------------------------------------------------------------------*/
/*----------------------------------------------------------------------------*/
/*
5. ENCAPSULATION: The "Friends" Design Challenge
   When your class holds a mutable list internally, you want outsiders to be
   able to read it without modifying it.

   EXPLAIN BY EXAMPLE:
*/
class User_10(val name: String) {
    // Internal state: Mutable (Can only be changed inside this class)
    private val _friends: MutableSet<User> = mutableSetOf()

    // The "Backing Property" View (External use)
    // Outsiders see a read-only Set.
    val friends: Set<User>
        get() = _friends
}

/*
   - WHEN DO I USE THEM: Every time your class holds mutable collections that
     the outside world needs to access.                 <-- PRIVATE COLLECTIONS?
   - WHY DO I USE THEM: If you exposed a `MutableSet`, any junior dev could
     ... make mistake `myUser.friends.add(fakeUser)` ... By exposing it as a
     `Set` (which has no `.add()` method), you force them to use your official,
     safe methods (like `considerFriendRequest()`).
   - WHEN DO I NOT USE THEM AND WHY: While the Backing Property is fast,
     malicious developers can still bypass it by downcasting:
     `(user.friend as MutableSet).add(hacker)`. If you are writing highly
     secure code, return a complete copy instead (`get() = _friends.toSet()`) so
     outsiders are modifying a clone, not your real data.
   - WHY THEM OVER OTHER USE CASES: Returning a Backing Property view is $O(1)$
     fast and takes zero extra memory. Returning a copy (`.toSet()`) is 100%
     secure but requires duplicate memory, which can lag if the list is massive.




6. Overriding Properties
Just like you can override functions in subclasses, you can override val and var properties to add custom behavior when they are accessed or changed.














* */







/*----------------------------------------------------------------------------*/
/*----------------------------------------------------------------------------*/

/*
                List in Kotlin is an interface that extends the Collection
                interface. Additionally, Kotlin has a MutableList interface to
                modify the elements of a list. The MutableList interface extends
                the MutableCollection interface. The methods within this
                interface allow us to add and remove elements from the list.
* */






/*----------------------------------------------------------------------------*/
/*----------------------------------------------------------------------------*/





/*----------------------------------------------------------------------------*/
/*----------------------------------------------------------------------------*/






/*----------------------------------------------------------------------------*/
/*----------------------------------------------------------------------------*/