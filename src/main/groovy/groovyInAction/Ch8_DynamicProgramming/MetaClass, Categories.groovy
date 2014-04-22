package groovyInAction.Ch8_DynamicProgramming
import groovy.time.TimeCategory
/**************************************/
/*** Some Capabilities of MetaClass ***/
/**************************************/

MetaClass mc = String.metaClass
final Object[] NO_ARGS = []

assert     1 == mc.respondsTo("toString", NO_ARGS).size()
assert     3 == mc.properties.size()
assert    74 == mc.methods.size()
assert   252 == mc.metaMethods.size()
assert    '' == mc.invokeMethod('', 'toString', NO_ARGS)
assert  null == mc.invokeStaticMethod(String, "println", NO_ARGS)
assert    '' == mc.invokeConstructor(NO_ARGS)


/*
     Kinds of MetaClasses 
     
     - MetaClassImpl: the default meta class, used in the vast majority of cases
     - ExpandoMetaClass: used to expand state and behavior
     - ProxyMetaClass: used to decorate a meta class with interception capabilities
     - Others used internally and for testing
 
*/

/**********************/ 
/*** ProxyMetaClass ***/
/**********************/
class InspectMe {
    int outer() {
        return inner()
    }
    private int inner() {
        return 1
    }
}

def tracer = new TracingInterceptor(writer: new StringWriter())
def proxyMetaClass = ProxyMetaClass.getInstance(InspectMe)
proxyMetaClass.interceptor = tracer

InspectMe inspectMe = new InspectMe(metaClass: proxyMetaClass)

assert inspectMe.outer() == 1
println("Method trace: \n${tracer.writer.toString()}")
assert tracer.writer.toString() == """\
before groovyInAction.Ch8_DynamicProgramming.InspectMe.outer()
  before groovyInAction.Ch8_DynamicProgramming.InspectMe.inner()
  after  groovyInAction.Ch8_DynamicProgramming.InspectMe.inner()
after  groovyInAction.Ch8_DynamicProgramming.InspectMe.outer()
"""

/* Alternate invocation style */
proxyMetaClass.use(inspectMe) {
    inspectMe.outer()    // proxy in use
}
// proxy no longer in use



/*************************/
/*** MetaClass Builder ***/
/*************************/
def move(string, distance) {
    string.collect {
        (it as char) + distance as char
    }.join('')
}

String ibm = "IBM"
ibm.metaClass {
    shift = -1
    encode {-> move(delegate, shift) }
    decode {-> move(delegate, -shift) }
    getCode {-> encode()}
    getOrig {-> decode()}
}

assert ibm.encode() == "HAL"



/***************************************/
/*** MetaClass changes: superclasses ***/
/***************************************/
class MySuperGroovy {}
class MySubGroovy extends MySuperGroovy {}

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


 * */




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