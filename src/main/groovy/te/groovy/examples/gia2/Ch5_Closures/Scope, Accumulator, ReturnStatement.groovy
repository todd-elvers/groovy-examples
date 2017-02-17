package te.groovy.examples.gia2.Ch5_Closures

/*********************/
/*** Closure Scope ***/
/*********************/

// A reference to variable x is passed to the closure, not a copy of variable x
// (aka Birthday context)
def x = 0
10.times {
    x++
}
assert x == 10



/********************************/
/*** Closure Accumulator Test ***/
/********************************/

//// The classic 'Accumulator Test'
Closure createAccumulator(int n) {
    return { int i -> n + i }
}

def accumulator = createAccumulator(4)
assert accumulator(1) == 5



/********************************/
/*** Closure Return Statement ***/
/********************************/
// The return statement only pre-maturely leaves the current evaluation of the closure
// Thus the following statements are equivalent:
[1, 2, 3, 4, 5].each { return it } == [1, 2, 3, 4, 5].each { it }


