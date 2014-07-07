package newInGroovy2_3
import groovy.transform.Field
import groovy.transform.Memoized
import groovy.transform.TailRecursive



/*************************/
/*** Fibonacci example ***/
/*************************/

@Field
static final FIB_NUMBER_TO_CALCULATE = 36
println("Using Fibnocci number: $FIB_NUMBER_TO_CALCULATE")

/*
    Worst answer:

    Normal recursive way to calculate a fibonacci number
        Pros:
            - Simple to read and understand
        Cons:
            - Abysmal runtime
            - Abysmal memory usage

*/
Closure fib
fib = { number ->
    if (number <= 1) {
        number
    } else {
        fib(number - 1) + fib(number - 2)
    }
}

def fibCalcStartTime = System.currentTimeMillis()
def fibCalcResult = fib(FIB_NUMBER_TO_CALCULATE)
println "\nMethod/Closure Name\t\t\t\t|  Runtime (ms) |   Result"
println "fib${'\t' * 8}|\t${System.currentTimeMillis() - fibCalcStartTime}\t\t|\t${fibCalcResult}\t"



/*
    Better answer:  (Introduced in Groovy 1.8)

    Tail-recursive way to calculate a fibonacci number.
        Pros:
            - Great runtime (although some fib numbers will be calculated twice)
            - Great usage of memory due to stack not growing during recursion
        Cons:
            - Only works on closures
            - Requires writing the function in a tail-recursive manner
            - Requires explicit call to .trampoline() inside AND outside of the closure

*/
Closure fibWithTrampoline
fibWithTrampoline = { number, a = 0, b = 1 ->
    if (number == 0) {
        a
    } else {
        fibWithTrampoline.trampoline(number - 1, b, a + b)
    }
}.trampoline()

fibCalcStartTime = System.currentTimeMillis()
fibCalcResult = fibWithTrampoline(FIB_NUMBER_TO_CALCULATE)
println "fibWithTrampoline${'\t' * 4}|\t${System.currentTimeMillis() - fibCalcStartTime}\t\t\t|\t${fibCalcResult}\t"



/*
    Even better answer:  (Introduced in Groovy 2.3)

    Tail-recursive way to calculate a fibonacci number.
        Pros:
            - Great runtime (although some fib numbers will be calculated twice)
            - Great usage of memory due to stack not growing during recursion
            - Faster than previous two examples
            - Works on methods
        Cons:
            - No direct closure support, but converting a method already annotated
              with @TailRecursive to a closure still works
            - Requires writing the function in a tail-recursive manner

*/
@TailRecursive
def fibMethodWithTailRecursion(int number, a = 0, b = 1) {
    if (number == 0) {
        a
    } else {
        fibMethodWithTailRecursion(number - 1, b, a + b)
    }
}

fibCalcStartTime = System.currentTimeMillis()
fibCalcResult = fibMethodWithTailRecursion(FIB_NUMBER_TO_CALCULATE)
println "fibMethodWithTailRecursion${'\t' * 2}|\t${System.currentTimeMillis() - fibCalcStartTime}\t\t\t|\t${fibCalcResult}\t"

Closure fibClosureWithTailRecursion = this.&fibMethodWithTailRecursion
fibCalcStartTime = System.currentTimeMillis()
fibCalcResult = fibClosureWithTailRecursion(FIB_NUMBER_TO_CALCULATE)
println "fibClosureWithTailRecursion${'\t' * 2}|\t${System.currentTimeMillis() - fibCalcStartTime}\t\t\t|\t${fibCalcResult}\t"



/*
    Best answer: (Features from Groovy 2.2 & 2.3)

    Tail-recursive way to calculate a fibonacci number.
        Pros:
            - Best runtime since memoization caches values and prevents duplicate calculations
            - Great usage of memory due
        Cons:
            - No direct closure support, but converting a method already annotated
              with @TailRecursive & @Memoized to a closure still works
            - Requires rewriting of the function

*/
@TailRecursive
@Memoized
def fibMethodWithTailRecAndMemoize(int number, a = 0, b = 1) {
    if (number == 0) {
        a
    } else {
        fibMethodWithTailRecAndMemoize(number - 1, b, a + b)
    }
}

fibCalcStartTime = System.currentTimeMillis()
fibCalcResult = fibMethodWithTailRecAndMemoize(FIB_NUMBER_TO_CALCULATE)
println "fibMethodWithTailRecAndMemoize${'\t' * 1}|\t${System.currentTimeMillis() - fibCalcStartTime}\t\t\t|\t${fibCalcResult}\t\n"




/*************************/
/*** Factorial example ***/
/*************************/

@TailRecursive
def calculateFactorial(BigInteger n, accumulator = 1G) {
    if (n < 2) {
        accumulator
    }
    else {
        calculateFactorial(n - 1, n * accumulator)
    }
}

def factorialExample = calculateFactorial(1000)
println("!1000 = $factorialExample")
assert factorialExample > 10e2566