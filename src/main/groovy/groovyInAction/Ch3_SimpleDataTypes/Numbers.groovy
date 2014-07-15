package groovyInAction.Ch3_SimpleDataTypes


/*************************/
/** ARITHMETIC COERCION **/
/*************************/


/*
    NOTE: For clarity, in the following when I say 'more precise type' I mean the type that requires
          more bits to represent in the JVM.
*/



// ADDITION
// SUBTRACTION
// MULTIPLICATION 
// These operations always return an instance of the more precise type
// (e.g. BigDecimal + Integer = BigDecimal,
//       BigInteger + Integer = BigInteger,
//       BigDecimal + Double = BigDecimal)
// (Exception: Float addition, subtraction & multiplication always return a Double)


// DIVISION
// If any of the arguments are of type Float or Double, the result is of type Double
// Otherwise
//    the result is of type BigDecimal
//    with the maximum precision of both arguments, with half rounded up
//    and normalized (no trailing zeros)
def x = 26
def y = 10

assert x/y == 2.6
assert x/y instanceof java.math.BigDecimal

assert x.intdiv(y) == 2
assert x.intdiv(y) instanceof java.lang.Integer



// POWER
// The power operator will coerce the result to the next best data type that can
// handle the result (in terms of range and precision) in the sequence 
// Integer, BigInteger, Double
assert 2**30 instanceof Integer
assert 2**31 instanceof BigInteger
assert 2**3.5 instanceof Double



// EQUALS
// The equals operator coerces to the less precise type before comparing
BigInteger a = 10
Integer b = 10
assert a == b



/********************/
/** NUMBER METHODS **/
/********************/

// Obvious methods: .times{} .upto(x){} .downto(x){}

// .step(end, stepWidth){}
def result = ""
0.step(0.5, 0.1) { number ->
    result += number + " "
}
assert result == "0 0.1 0.2 0.3 0.4 "