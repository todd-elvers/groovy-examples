package te.groovy.examples.gia2.Ch9_ASTTransformations

import groovy.transform.Immutable




/******************/
/*** Delegation ***/
/******************/

// A delegate is a 'has-a' relationship between two classes.
// Typically, one class will contain a reference to another class
// and then also share some of the API with that class.

class NoisySet {
    @Delegate
    Set delegate = new HashSet()

    @Override
    boolean add(element){
        println("adding $element")
        delegate.add(element)
    }

    @Override
    boolean addAll(Collection collection){
        for(def element : collection) {
            println("adding $element")
        }
        delegate.addAll(collection)
    }
}

// We could have simply subclassed HashSet and overridden the two methods, but
// we didn't want to change the .add() method, we just wanted to intercept it.
//
// If we had subclassed HashSet, then calling .addAll() would cause duplicate
// println statements, since HashSet implements .addAll() by calling the .add()
// method. That is an implementation detail that is exposed if we choose to
// subclass instead of delegate.
NoisySet set = new NoisySet()
set.add("hey")
set.addAll(["there","friend"])

// NOTE: If method names collide due to delegation, the first delegate's method will be used






/******************/
/*** Singletons ***/
/******************/

// Generates a 'static final Zeus instance' in the class
// Prevents constructor generation (throws RuntimeException if called)
// Can be used in conjunction with @Lazy to have a lazy-loaded singleton class
@Singleton
class Zeus {
    // ...
}

assert Zeus.instance

try {
    new Zeus()
}
catch (RuntimeException ignored) {
    // ...
}




/********************/
/*** Immutability ***/
/********************/

// Generates a map based constructor, a tuple constructor, and a getter for each property
// Adds @ToString and @EqualsAndHashCode
@Immutable
class Person5 {
    String firstName, lastName
}

def person = new Person5('Hamlet', "D'Arcy")


assert person.firstName == 'Hamlet'

try {
    person.firstName = 'John'
} catch (ReadOnlyPropertyException e) {
    // ...
}