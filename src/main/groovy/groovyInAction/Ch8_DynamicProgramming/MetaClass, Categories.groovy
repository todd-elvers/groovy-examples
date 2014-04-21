package groovyInAction.Ch8_DynamicProgramming
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

 
// all implementing classes share that behavior:
// Do the same as above, but call ExpandoMetaClass.enableGlobally()



/********************/
/*** TimeCategory ***/
/********************/
import groovy.time.TimeCategory

def date
use(TimeCategory) {
    date = 2.weeks.from.today
}
println("2 weeks from today: ${date}")

/*
    Key characteristics of using category classes:
    
    - Category usage is confined to the current thread
    - Category use is non-intrusive
    - If the receiver type refers to a superclass or even an interface, then the method
      will be available in all subclasses/implementors
    - Category methods names can take the form of property accessors (pretending
      property access), operator methods, and GroovyObject methods
    - In places where performance is crucial, use categories with care
    - Categories cannot introduce new state in the receiver object

*/