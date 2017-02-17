package te.groovy.examples.gia2.Ch7_DynamicObjectOrientation



/**********************************/
/*** Meta-Object-Protocol (MOP) ***/
/**********************************/

// All calls that originate from Groovy code are routed through the MOP


// All classes made in Groovy are constructed by the GroovyClassGenerator such 
// that they implement GroovyObject (w/ some default implementation).
// GroovyObject has the following methods:
interface GroovyObject {
    Object    invokeMethod(String name, Object args);
    Object    getProperty(String property);
    boolean   setProperty(String property, Object newValue);
    MetaClass getMetaClass();
    void      setMetaClass(MetaClass metaClass);
}

/*   Groovy having a default implementation for GroovyObject is possible because
     Groovy objects extend GroovyObjectSupport which itself implements GroovyObject.
    
     public abstract class GroovyObjectSupport implements GroovyObject {
        public Object invokeMethod(String name, Object args) {
            return getMetaClass().invokeMethod(this, name, args);
        } 
        
        ...
     }
 */





// Every instance of a GroovyObject has an association with MetaClass.
// Metaclass has the following methods:
class MetaClass {
    Object invokeMethod(Object obj, String methodName, Object args)          {}
    Object invokeMethod(Object obj, String methodName, Object[] args)        {}
    Object invokeStaticMethod(Object obj, String methodName, Object[] args)  {}
    Object invokeConstructor(Object[] args)                                  {}
}
    
    
// MetaClass is stored in and retrieved from a central store, the MetaClassRegistry.
// MetaClassRegistry has the following methods:
class MetaClassRegistry {
    MetaClass getMetaClass(clazz)       {}
    void setMetaClass(clazz, metaClass) {}
}    

// We can fool the MOP into thinking a class that was actually compiled by Java was
// compiled by Groovy.  We only need to implement GroovyObject or, more conveniently,
// extend GroovyObjectSupport






/********************************************/
/*** Groovy's MOP Method Invocation Logic ***/
/********************************************/

/*
    Let 'Cat' be a class and let 'cat' be an instantiation of that class.
    Let a no-arg function called 'meow' exist as a method and a closure on Cat.
    Let there also be a no-arg 'meow' closure on the metaClass of 'Cat' and of 'cat'.

    Then calling 'cat.meow()' would cause Groovy to do the following:
        1.) Check cat.metaClass for a compatible 'meow' closure
        2.) If no closure is found, Groovy then checks Cat.metaClass for a compatible 'meow' closure
        3.) If no closure is found, Groovy then checks cat for a compatible 'meow' method
        4.) If no method is found, Groovy then checks cat for a compatible 'meow' closure
        5.) If no closure is found, a MissingMethodException is thrown

    Put more concisely, the order in which Groovy would check for compatible method/closure signatures:
        1.) cat.metaClass.meow = {}
        2.) Cat.metaClass.meow = {}
        3.) cat.meow()
        4.) cat.meow = {}
        5.) MissingMethodException is thrown

*/
class Cat {
    // Method on object of Cat
    String meow() {
        "Call returned from: cat.meow()"
    }

    // Closure on object of Cat
    Closure<String> meow = {
        "Call returned from: cat.meow = {}"
    }
}

// Without messing with the metaClass, the method takes precedence
// over the closure with the same arguments
assert new Cat().meow() == "Call returned from: cat.meow()"

// Adding a closure onto the MetaClass of the class of Cat means all instances
// of 'Cat' that are instantiated after this call will have that method
Cat.metaClass.meow = { "Call returned from Class's metaClass: Cat.metaClass.meow = {}" }

Cat cat = new Cat()
assert cat.meow() == "Call returned from Class's metaClass: Cat.metaClass.meow = {}"


// Adding a closure onto the MetaClass of an instance of Cat means
// only that instance receives the method
cat.metaClass.meow = { "Call returned from instance's metaClass: cat.metaClass.meow = {}" }
assert cat.meow() == "Call returned from instance's metaClass: cat.metaClass.meow = {}"

println cat.meow()

   