package groovyInAction.Ch9_CompileTimeMetaprogramming_ASTTransformations

import groovy.transform.ConditionalInterrupt

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

println("Executing conditional script...")
try {
    conditionalScript.someFunction()
} catch (InterruptedException ex) {
    println("InterruptedException thrown - conditional check failed.")
}