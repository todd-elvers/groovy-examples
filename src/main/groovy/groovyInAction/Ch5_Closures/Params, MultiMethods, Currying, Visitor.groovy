package groovyInAction.Ch5_Closures
// Methods and Closures differences:
//    - Closures are objects and thus created at runtime
//    - Methods are created at class-generation time
//    - N instances of a given closure may exist at any point
//    - Only 1 instance of a method may exist at any point


/**************************/
/*** CLOSURE PARAMETERS ***/
/**************************/

// Reacting to closure parameter count or type
class ClosureInfoPrinter{
    def countParameters(Closure closureToCall) {
        closureToCall.getParameterTypes().size()
    }
    def getParameterTypes(Closure closureToCall) {
        closureToCall.getParameterTypes()
    }
}
def cip = new ClosureInfoPrinter()
assert cip.countParameters { one, two -> } == 2
assert cip.getParameterTypes { String one, two -> } == [String.class, Object.class]



/*****************************/
/*** MULTIMETHODS CLOSURES ***/
/*****************************/
class MultiMethodExample {
    String mysteryMethod(Object obj) {
        "Object"
    }

    int mysteryMethod(String string) {
        string.length()
    }
    
    int mysteryMethod(List list) {
        list.size()
    }
    
    int mysteryMethod(int x, int y) {
        x+y
    }
}

MultiMethodExample multiMethodExample = new MultiMethodExample()
Closure multi = multiMethodExample.&mysteryMethod

Object object = new Object()
Object stringObj = 'string arg'
Object listObj = ['list', 'of', 'values']
Object intObj1 = 6
Object intObj2 = 8

// Q: How does groovy know which method to call since they're all of type 'Object'?
// A: These objects are all of the same static type, but differ in their dynamic type.
//    Since groovy dispatches on the dynamic type.
assert multi(object)           == "Object"
assert multi(stringObj)        == 10
assert multi(listObj)          == 3
assert multi(intObj1, intObj2) == 14


// Selective method overriding via MultiMethods
class Equalizer {
    boolean equals(Equalizer equalizer) {
        true
    }
}

Object equalizer = new Equalizer()
Object       obj = new Object()

assert  new Equalizer().equals(equalizer)
assert !new Equalizer().equals(obj)


/************************/
/*** CLOSURE CURRYING ***/
/************************/

/* Simple example */
Closure adder = {x, y -> return x+y}
def addOne = adder.curry(1)
assert addOne(5) == 6


/* Complex example */
// The following closure dictates the structure of our 'logger'
def logger = { filterLog, formatLog, appendLog, log ->
    def shouldLog = filterLog(log)
    if(shouldLog) {
        def formattedLog = formatLog(log)
        appendLog(formattedLog)
    }
}

def debugOnlyFilter = { log -> log.toLowerCase().contains('debug') }
def dateFormatter   = { log -> "${new Date()}: $log" }
def consoleAppender = { log -> println log }

// We curry the first three parameters to logger, so 'myLogger' only accepts a log
def myLogger = logger.curry(debugOnlyFilter, dateFormatter, consoleAppender)

myLogger("DEBUG - This statement was printed because it contained 'debug'")
myLogger("This statement will not be printed because it's missing the filter keyword")


/*********************/
/*** Closure Scope ***/
/*********************/

// Birthday context 
// A reference to varible x is passed to the closure, not a copy of variable x
def x = 0
10.times {
    x++
}
assert x == 10





/**********************/
/*** Closure Extras ***/
/**********************/

// The classic 'Accumulator Test'
Closure createAccumulator(int n) { 
    return { int i -> n + i }
}

def accumulator = createAccumulator(4)
assert accumulator(1) == 5


// The 'return' statement
// The return statement only pre-maturely leaves the current evaluation of the closure
// Thus the following statements are equivalent:
[1,2,3,4,5].each { return it } == [1,2,3,4,5].each { it }






/***********************/
/*** Visitor Pattern ***/
/***********************/
// Visitor pattern is useful when you wish to perform some complex action
// on a composite collection of existing classes
// (i.e. Use Visitor pattern when you want to decouple logical code from the
// elements you're using as input)
class Drawing {
    List shapes
    
    def accept(Closure yield) { 
        shapes.each { shape -> 
            shape.accept(yield) 
        } 
    }
}
abstract class Shape {
    abstract def area()
    def accept(Closure yield) { yield(this) }
}
class Square extends Shape {
    def width
    def area() { width**2 }
}
class Circle extends Shape {
    def radius
    def area() { Math.PI * (radius**2) }
}

Drawing drawing = new Drawing(
    shapes: [new Square(width:1), new Circle(radius:1)]
)

// Calculate total area of drawing by leveraging the Visitor Pattern
def total = 0
drawing.accept { shape -> 
    total += shape.area() 
}
println "The shapes in this drawing cover an area of ${total.toString()[0..3]} units" 
println "The individual contributions are: "
drawing.accept { println "\t${it.class.name}:${it.area()}" } 