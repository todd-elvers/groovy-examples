package te.groovy.examples.gia2.Ch8_DynamicProgramming

// Hook methods are not static, they are instance methods

/****************************/
/*** methodMissing() Hook ***/
/****************************/
class MiniGorm {
    def database = []
    
    def methodMissing(String name, Object args) {
        database.find {
            it[name.toLowerCase()-'findby'] == args[0]
        }
    }
}

def people = new MiniGorm()
def todd  = [first:'Todd', last: 'Elvers']
def brock = [first:'Brock', last: 'Bigard']
people.database << todd << brock

assert people.findByFirst('Todd') == todd
assert people.findByLast('Bigard') == brock




/******************************/
/*** propertyMissing() Hook ***/
/******************************/
class PropPretender {
    def propertyMissing(String name) {
        return "accessed non-existent property '$name'"
    }
}

def propPretender = new PropPretender()
assert propPretender.lilJohn == "accessed non-existent property 'lilJohn'"




/*********************************************************/
/*** Closure Property Pattern + propertyMissing() Hook ***/
/*********************************************************/
class DynamicPretender {
    Closure whatToDo = { name -> "accessed $name" }
    def propertyMissing(String propName) {
        whatToDo(propName)
    }
}
def dynamicPretender = new DynamicPretender()
assert dynamicPretender.hello == 'accessed hello'

dynamicPretender.whatToDo = { name -> name.size() }
assert dynamicPretender.hello == 5

// Note: in classic Java programming, the behavior of a class never changes and
// the behavior never changes for all objects of the class. The best one could
// do in java is to use the Strategy Pattern to switch between objects that
// behave differently.

/* 
    Q: What does implementing GroovyObject do to a class?
    A: As soon as a class implements GroovyObject, the following rules apply:
    
    1.)  Every access to a property calls the getProperty() method
    2.)  Every modification of a property calls the setProperty() method
    3.)  Every call to an unknown method calls invokeMethod().  If the method
         is known, invokeMethod() is only called if the class implements GroovyObject
         and the marker interface GroovyInterceptable
*/    


/**************************/
/*** getProperty() Hook ***/
/**************************/
class AllowNoArgMethodsToBeCalledWithoutParenthesis {
    def getProperty(String propertyName) {
        // If there is a property with this name in the class, return it
        if(metaClass.hasProperty(this, propertyName)) {
            return metaClass.getProperty(this, propertyName)
        }

        // Otherwise try and invoke a method with that name and no arguments
        invokeMethod(propertyName, null)
    }
}    

class PropUser extends AllowNoArgMethodsToBeCalledWithoutParenthesis {
    boolean existingProperty = true
}

def user = new PropUser()

// Calls to properties that exist function as expected
assert user.existingProperty

// Since .toString() is a no-arg method, and user.toString
// is interpreted as user.getProperty('toString'), our
// method above intercepts it and invokes it like a method.
assert user.toString() == user.toString






