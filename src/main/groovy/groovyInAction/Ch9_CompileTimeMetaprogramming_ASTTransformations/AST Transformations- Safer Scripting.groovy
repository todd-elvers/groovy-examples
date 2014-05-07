package groovyInAction.Ch9_CompileTimeMetaprogramming_ASTTransformations

import groovy.transform.ConditionalInterrupt
import groovy.transform.ThreadInterrupt
import groovy.transform.TimedInterrupt

import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException



// This annotation adds a field to store the time at which this class was instantiated,
// and then checks that against the maximum run time and throws an exception if necessary.
// The run time is checked:
//      - On the first line of every method
//      - On the first line of every closure
//      - Within every iteration of a loop
@TimedInterrupt(value = 5L, unit = TimeUnit.SECONDS)
class Script {
    def errorCount
    def longOperation = {
        while(true) {
            // ...
        }
    }
}

def script = new Script()

try {
    script.longOperation()
} catch (TimeoutException ex) {
    println("Timeout occurred!")
}




// This annotation adds a check to Thread.currentThread().isInterrupted() status to allow
// the class to respect the isInterrupted() flag so that execution can be halted if necessary.
// The isInterrupted() flag is checked:
//      - On the first line of every method
//      - On the first line of every closure
//      - Within every iteration of a loop
@ThreadInterrupt
class LongRunningScript {
    // ...
}




// This annotation adds a custom check to determine if it should interrupt execution or not.
// This check is done:
//      - On the first line of every method
//      - On the first line of every closure
//      - Within each iteration of a loop
@ConditionalInterrupt({ errorCount >= 10 })
class ConditionalScript {
    def errorCount = 0

    void someFunction() {
        1000.times {
            errorCount++
        }
    }
}

def conditionalScript = new ConditionalScript()

println("hey")
try {
    conditionalScript.someFunction()
} catch (InterruptedException ex) {
    println("Interrupting - conditional check failed.")
}