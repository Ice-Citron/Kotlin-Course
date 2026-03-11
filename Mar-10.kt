/*
    Using a `StringBuilder` is actually the most efficient way to do this,
    especially when building large Abstract Syntax Trees.

    In Kotlin, instead of writing `val sb = java.lang.StringBuilder();`, the
    idiomatic approach is to use `buildString { ... }` function. It creates a
    `StringBuilder` for you behind the scenes, lets you call `.append()`
    directly inside the block, and automatically returns the final string when
    the block finishes.

    Here is how both classes look using this technique:
* */

class ToString14 (
    val name: String,
    val expr: String,
    val next: String43?,
) {
    fun toString14(indent: Int): String = buildString {
        val padding = " ".repeat(4)

        // 1. Append the current assignment line
        append(padding)
        append(name)
        append(" = ")
        append(expr)
        append("\n")

        // 2. Append the next statement in the chain, if it exisits
        if (next != null) {
            append(next!!.toString(indent))
        }
    }
}



/*
2. THE `If` CLASS
* */
class String43 {
    fun toString(indent: Int) = ""
}

class toString41(
    val condition: String43,
    val thenStmt: String43,
    val elseStmt: String43? = null,
    val next: String43? = null,
) {
    fun toString41(indent: Int): String {
        return buildString {
            val padding = " ".repeat(indent)

            // 1. Open the `if` statement
            append(padding)
            append("if ($condition) {\n")

            // 2. Add the `then` block
            //    (We don't need a newLine here because the statement inside
            //    will generate its own)
            append(thenStmt.toString(indent + 4))

            // 3. Handle the optional `else` block
            if (elseStmt != null) {
                append(padding)
                append("} else {\n")
                append(elseStmt.toString(indent + 4))
            }

            // 4. Close the entire if/else block
            append("$padding")
            append("}\n")

            // 5. Apend the next statement in the chain, if it exists
            if (next != null)
                append(next.toString(indent))
        }
    }
}


/*
WHY THIS IS A GREAT TECHNIQUE:

    1. NO STRING MATH: You avoid visually confusing `result += ... + ...` math.
    2. PERFORMANCE: In the background, `StringBuilder` manages a single array of
       characters. When you use standard `+` or string templates in a loop or
       recursion, the computer has to destroy and recreate the string in RAM
       over and over again. `StringBuilder` just appends to the end of the
       existing memory block, which is much faster.
    3. NO "DOUBLE NEWLINE" CONFUSION: Because every component explicitly adds
       exactly what it needs, it is much harder to accidentally stack multiple
       `\n` characters next to each other.


* */



















