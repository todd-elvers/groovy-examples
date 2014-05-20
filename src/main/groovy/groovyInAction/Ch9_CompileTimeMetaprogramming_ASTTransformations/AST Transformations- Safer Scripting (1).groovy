package groovyInAction.Ch9_CompileTimeMetaprogramming_ASTTransformations

import groovy.transform.ThreadInterrupt
import groovy.transform.TimedInterrupt

import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


// This annotation adds a field to store the instantiation time, so that a timeout will
// occur if any of the methods/closures of this class take longer than the specified time.
// The run time is checked:
//      - On the first line of every method
//      - On the first line of every closure
//      - Within every iteration of a loop
@TimedInterrupt(value = 5L, unit = TimeUnit.SECONDS)
class Script {
    def longOperation = {
        while(true) {
            // ...
        }
    }
}

def script = new Script()

println("Sleeping 5 seconds")
sleep(5000)

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




