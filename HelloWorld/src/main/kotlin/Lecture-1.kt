import kotlin.math.PI

// Kotlin function declaration
fun successor(x: Int): Int {
    return x + 1;    // Function definition
}
fun successor1(x: Int): Int = x + 1
fun successor2(x: Int): Int {
    return x + 2;
}

// Kotlin Boolean syntax
    // Write a function to check if an integer is even
fun isEven(x: Int): Boolean { return x % 2 == 0; }
fun isOdd(x: Int): Boolean  { return x % 2 == 1; }

// Multiple arguments
fun difference(x: Int, y: Int): Int {
    if (x > y) { return x - y; }
    else       { return y - x; }
}

// When conditional logic
/*
* Write a function to return the signature of an integer
*
* Note;
* - The arrow -> operator
* - The necessary else
* - Single expression function
* - The (lack of) styling
* */
fun signum(x: Int): Int {
    when {
        x > 0  -> return 1;
        x == 0 -> return 0;
        else   -> return -1;
    }
}

// Block body
/* Write a function to calculate the number of turns of a measuring wheel per a
*  given distance
* */
fun turns(start: Double, end: Double, radius: Double): Double {
    val kmToM = 1000;
    val distance = (end-start)*kmToM
    return distance/(PI*radius*2);
}

// String
/*
* Write a function that takes a String argument called name and return Hello
* name.
* - Make first letter of name uppercase
* */
fun helloName(name: String): String {
    return "Hello " + name[0].uppercaseChar() + name.substring(1)
}

// String
/*
* Write a function that returns "Hello", "world" or "Hello World" depending on
* what the first letter of the argument is
* */
fun stringMan(str: String): String {
    when {
        str.startsWith('H') -> return "Starts with H"
        str.endsWith('A') -> return "Ends with A"
        else -> return "Lame"
    }
}

fun main() {
    println(successor(2));   // Function call
    println(successor1(2));
    println(successor2(3));

    println(isEven(1));
    println(isEven(2));

    println(difference(999, 34))

    println(signum(-26))

    println(turns(10.0, 50.0, 5.0))

    println(helloName("joanna"))

    println(stringMan("Hello world"))
    println(stringMan("Joanna")) // returns "Lame"  <-- because 'a' /= 'A'
    println(stringMan("JoannA"))
}

// Defining Functions review
/*
* Function declarations
* - Arguments
* - Return types
* - Expressions vs block bodies
*
* Common Basic (native) types
* - Integer, Double, Boolean, String, Pair, List
*
* String manipulation
* */




