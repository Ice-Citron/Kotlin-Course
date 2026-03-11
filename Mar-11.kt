/*
    ... you are experiencing the exact panic that the examiners expect students
    to feel when they hit this question.

    To answer ... NO, YOU DO NOT NEED TO MEMORISE THIS BLACK MAGIC. You wouldn't
    have to guess this in the exam because the SPEC ACTUALLY GIVES YOU THE
    EXACT LINE OF CODE TO COPY AND PASTE ...


IS THERE ANY OTHER WAY TO WRITE THIS?
    In the real world, yes. If you were building this app for a company, you
    would use a Kotlin annotation like `@JvmName("InterpreterUtils")` at the top
    of your Kotlin file, which would let you call `InterpreterUtils.step()` in
    Java. Or, you could have just put the `step()` method inside the `Stmt`
    interface itself instead of making it an extension function.
* */




/*
    ... conquered the hardest conceptual hurdle in concurrent programming! It is
    completely normal that you haven't seen this before--multi-threading is
    usually introduced right about now in computing degrees because...

    ... breakdown... of why new version fixed the bug, followed by a crash
    course on how Java's `Thread` system actually works under the hood.


PART 1: WHY THE `while` LOOP FIXES THE PROGRAM
    To understand why the old code failed, you have to remember what your
    `step()` function actually does.

    When you call `threadBody.step(store)`, it executes EXACTLY ONE LINE OF CODE
    (like `x = 5`), and then it returns the NEXT statement in the chain.


THE OLD VERSION (The "Read One Page and Quit" Bug):
    Your old code called `step()`, executed the first line, and threw away the
    returned next line. Because there was no loop, the JVM reached the closing
    brace `}` of the `run()` method. When a thread reaches the end of `run()`,
    IT DIES PERMANENTLY. Your threads were only executing line 1 and then
    committing in-memory suicide.


THE NEW VERSION (THE "READ UNTIL THE END" FIX):

```Kotlin
var currentStatement: Stmt? = threadBody
while (currentStatement != null) { ... }
```

    This version acts like a bookmark.
        1. It looks at the current line.
        2. It executes it inside the lock.
        3. It catches the next line and updates the bookmark
           (`currentStatement = ...`).
        4. The `while` loop keeps the `run()` method alive, feeding the next
           line back to the top to be executed.
        5. It only stops and lets the thread die when `step()` finally returns
           `null` (meaning the program is over).


---
PART 2: THE JAVA `Thread` CRASH COURSE
    When you write normal, sequential code, you have one single "worker" (The
    Main Thread) executing code from top to bottom. Multithreading allows you
    to hire additional workers to do tasks simultaneously. Here is the manual on
    how to control them...


    1. THE BLUEPRINT (`Runnable` & `run()`)
        A `Thread` is just a dumb worker; it doesn't know what to do until you
        give it instructions. The `Runnable` interface is the instruction manual
        . When you implement `Runnable`, you are forced to write a `run()`
        method. WHATEVER IS INSIDE `run()` IS THE ONLY THING THAT THREAD WILL
        EVER DO.

    2. THE WORKER (`val t = Thread(executor)`)
        When you pass your `ProgramExecutor` into `Thread()`, you are officially
        hiring a new background worker and handing them your specific
        instruction manual. At this exact moment, the thread exists in memory,
        but IT IS NOT RUNNING YET. It is just standing there waiting for the
        boss to say "go".

```Kotlin
    override fun run() {
        var currentStmt: Stmt? = threadBody
        while (currentStmt != null) {
            Thread.sleep(pauseMs)
            lock.withLock {
                currentStmt = currentStmt?.step(store)
            }
        }
    }
```


    3. THE IGNITION (`t.start()`)
        This is the most crucial method to understand. Calling `.start()` tells
        the JVM to actually allocate CPU time, wake the worker up, and tell them
        to begin executing their `run()` method in the background.
            * THE CLASSIC TRAP: A lot of beginners accidentally call `t.run()`
              instead of `t.start()`. If you call `.run()`, your main thread
              just executes the code normally, blocking everything else. You
              must call `.start()` to branch off into true concurrent background
              work.

    4. THE PAUSE BUTTON (`Thread.sleep(pauseMs)`)
        This tells the currently executing thread to completely stop working for
        a set number of milliseconds.
            * WHY IT MATTERS FOR YOUR LAB: Because you put `sleep()` OUTSIDE the
              `withLock` block, Thread A can go to sleep, which allows Thread B
              to grab the lock, execute a line of code, release the lock, and go
              to sleep itself.

    5. THE WAITING ROOM (`t.join()`)
        Your `ConcurrentProgram.execute()` method is being run by the "Main Boss
        Thread". The boss loops through and `start()`s all the background
        workers.

        If you didn't have `.joint()`, the boss would immediately









```
    fun execute(initialStore: Map<String, Int>): Map<String, Int> {
        val finalStore: MutableMap<String, Int> = initialStore.toMutableMap()
        val threads: MutableList<Thread> = mutableListOf()

        for (i in 0 until threadBodies.size) {
            val executor = ProgramExecutor(
                threadBodies[i],
                pauseVals[i],
                finalStore,
                lock
            )
            val thread = Thread(executor)
            threads.add(thread)
            thread.start()
        }

        // 2. Wait for everyone to finish
        threads.map { it.join() }
        return finalStore
    }
```

    5. THE WAITING ROOM (`t.join()`)                            <-- huh so this reminds me of .sync() or .async() in JS where we syncs things up to make sure...
        Your `ConcurrentProgram.execute()` method is being run by the "Main Boss
        Thread". The boss loops through and `start()`s all the background
        workers.

        If you didn't have `.join()`, the boss would immediately move to the
        next line of code, return the `finalStore` (which would be empty
        because the workers haven't finished yet!), and exit the program.

        Calling `t.join()` makes the Main Boss Thread stop and wait. It
        literally means: "Do not execute the next line of code until thread `t`
        has completely finished its `run()` method and died." By looping through
        and joining all of them, you guarantee the `finalStore` isn't returned
        until every single concurrent program is fully finished.












* */












