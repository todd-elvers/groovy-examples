package te.groovy.examples.gia2.Ch8_DynamicProgramming
import groovy.time.TimeCategory
/**************************************/
/*** Some Capabilities of MetaClass ***/
/**************************************/

MetaClass mc = String.metaClass
final Object[] NO_ARGS = []

println "How many methods of String.toString() accept no arguments? - ${mc.respondsTo("toString", NO_ARGS).size()}"
println "How many properties are on String? - ${mc.properties.size()}"
println "How many methods are on String? - ${mc.methods.size()}"
println "How many meta-methods are on String? - ${mc.metaMethods.size()}"
println "What is the result of invoking the no-arg .toString() method on 'a'? - ${mc.invokeMethod('a', 'toString', NO_ARGS)}"
println "What is the result of invoking the static .valueOf() method on the String class w/ args 123? - ${mc.invokeStaticMethod(String, "valueOf", 123)}"
println "What is the result of invoking the no-arg String constructor? - \"${mc.invokeConstructor(NO_ARGS)}\""



/*
     Main Implementations of MetaClass 
        - MetaClassImpl   : the default meta class, used in the vast majority of cases
        - ExpandoMetaClass: used to expand state and behavior
        - ProxyMetaClass  : used to decorate a meta class with interception capabilities
 
*/

/**********************/ 
/*** ProxyMetaClass ***/
/**********************/
class InspectMe {
    int outerMethod() {
        return innerMethod()
    }
    private int innerMethod() {
        return 1
    }
}

// Create an instance of ProxyMetaClass w/ a TracingInterceptor
def tracer = new TracingInterceptor(writer: new StringWriter())
def proxyMetaClass = ProxyMetaClass.getInstance(InspectMe)
proxyMetaClass.interceptor = tracer

// Inject ProxyMetaClass into InspectMe
InspectMe inspectMe = new InspectMe(metaClass: proxyMetaClass)
// Call to instance of InspectMe still functions normally
assert inspectMe.outerMethod() == 1


// Retrieve the method trace from the ProcyMetaClass (we remove the package name here to clarify the output)
String methodTrace = tracer.writer.toString().replaceAll("te.groovy.examples.gia2.Ch8_DynamicProgramming.", "")

assert methodTrace == """\
before InspectMe.outerMethod()
  before InspectMe.innerMethod()
  after  InspectMe.innerMethod()
after  InspectMe.outerMethod()
"""

/* Alternate invocation style */
proxyMetaClass.use(inspectMe) {
    inspectMe.outerMethod()    // proxy in use
}
// proxy no longer in use






/*************************/
/*** MetaClass Builder ***/
/*************************/
String shiftCipher(String string, int numCharactersToShiftBy) {
    // Iterate over the characters of 'string' and shift their characters by 'numCharactersToShiftBy'
    string.collect { theChar ->
        (theChar as char) + numCharactersToShiftBy as char
    }.join('')
}

String.metaClass {
    encode { numCharactersToShiftBy ->
        shiftCipher(delegate, numCharactersToShiftBy)
    }
    decode { numCharactersToShiftBy ->
        shiftCipher(delegate, -numCharactersToShiftBy)
    }
}

assert "ABC".encode(1) == "BCD"
assert "ABC".encode(1).decode(1) == "ABC"






/***************************************/
/*** MetaClass changes: superclasses ***/
/***************************************/
class MySuperGroovy {}
class MySubGroovy extends MySuperGroovy {}

// Modifying the super class's metaClass also adds that function to any subclasses
MySuperGroovy.metaClass.added = {-> true}
assert new MySubGroovy().added()

// Modifying an interface or it's MetaClass and having all implementing
// classes share that behavior is the same as above, but requires calling:
// ExpandoMetaClass.enableGlobally()







/**********************************************************/
/*** MetaClass injection of operator & MOP hook methods ***/
/**********************************************************/

String.metaClass {
    rightShiftUnsigned = { prefix ->
        delegate.replaceAll(~/\w+/) { prefix + it }
    }
    methodMissing = { String name, args ->
        delegate.replaceAll(name, args[0])
    }
}
String people = "Dierk,Guillaume,Paul,Hamlet,Jon"
people >>>= "\n    "
people    = people.Dierk('Mittie').Guillaume('Mr.G')

assert people == '''
    Mittie,
    Mr.G,
    Paul,
    Hamlet,
    Jon'''

/*

   MetaClass takeaways:

      - All method calls from Groovy code go through the meta class
      - Meta classes can change - for all instances of a class or per single instance
      - Meta classes changes affect all future instances in all running threads
      - Meta classes allow non-intrusive changes in both Groovy and Java code as long
        as the caller is Groovy. We can even change access to final classes like java.lang.String
      - Meta class changes can well take the form of property accessors (pretending property access),
        operator methods, GroovyObject methods, or MOP hook methods
      - ExpandoMetaClass makes meta class modifications more convenient
      - Meta class changes are best applied only once - preferably at application startup time


 */




/********************/
/*** TimeCategory ***/
/********************/
def date
use(TimeCategory) {
    date = 2.weeks.from.today
}
println("2 weeks from today: ${date}")

/*

    Category class takeaways:

      - Classes not written in Groovy (e.g. Collections) can still be used as categories
      - Category usage is confined to the current thread
      - Category use is non-intrusive
      - If the receiver type refers to a superclass or even an interface, then the method
        will be available in all subclasses/implementors
      - Category methods names can take the form of property accessors (pretending
        property access), operator methods, and GroovyObject methods
      - In places where performance is crucial, use categories with care
      - Categories cannot introduce new state in the receiver object
      - If two or more Categories conflict on a method name, the latest Category wins


*/