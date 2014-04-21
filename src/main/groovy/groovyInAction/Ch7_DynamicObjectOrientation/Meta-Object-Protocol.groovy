package groovyInAction.Ch7_DynamicObjectOrientation
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
    Given some class Cat, when you call cat.meow() the following happens:
        1.) Groovy checks the MetaClass of the instance of Cat (ie the 'cat' object) for .meow()
        2.) If no closure is found, Groovy then checks the class's MetaClass for .meow()
        3.) If no closure is found, Groovy then checks the object for a .meow() method
        4.) If no method is found, Groovy then checks the object for a .meow() closure
        5.) If no closure is found, a MissingMethodException is thrown
        
*/
class Cat {    

    // Method on object of Cat
    void meow() {
        println "Meow() called from instance of Cat"
    }
    
    // Closure on object of Cat
    def meow = {
        println "Meow() closure called from instance of Cat"
    }
    
}

// Method on MetaClass of the class of Cat
Cat.metaClass.meow = {
    println "Meow() called from MetaClass of the class Cat"
}   


Cat cat = new Cat()

// Method on MetaClass of an instance of Cat
cat.metaClass.meow = {
    println "Meow() called from MetaClass of an instance of Cat"
}

cat.meow()
   