package groovyInAction.Ch5_Closures

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

