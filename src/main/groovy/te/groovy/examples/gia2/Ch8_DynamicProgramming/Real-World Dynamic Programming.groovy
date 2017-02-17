package te.groovy.examples.gia2.Ch8_DynamicProgramming

import java.awt.*



/********************************/
/*** Calculating with metrics ***/
/********************************/

// Goal = to be able to simply write "1.m + 20.cm - 8.mm"
Number.metaClass {
    getMm = { delegate          }
    getCm = { delegate *  10.mm }
    getM  = { delegate * 100.cm }
}

assert (1.m + 20.cm - 8.mm) == 1.192.m





/***************************************************/
/*** Replacing constructors with factory methods ***/
/***************************************************/

// Goal = change all classes so that they can be constructed
// with a static factory method called 'make'
Class.metaClass.make = { Object[] args ->
    delegate.metaClass.invokeConstructor(*args)
}

assert new HashMap()        == HashMap.make()
assert new Integer(42)      == Integer.make(42)
assert new Dimension(2, 3)  == Dimension.make(2, 3)




/************************************************************/
/*** Method aliasing and undoing meta class modifications ***/
/************************************************************/

// Suppose someone made a terrible change to the .size() method...
String.metaClass {
    size = { -> delegate.length() * 2 }
}

// Now .size() doesn't return the right value!
assert "abc".size() == 6


// To fix the above issue, proceed as follows:

MetaClass oldMetaClass = String.metaClass   // #1 - Store old meta class
MetaMethod alias = String.metaClass         // #2 - Store meta method
        .metaMethods
        .find { it.name == 'size' }

String.metaClass {
    oldSize = { -> alias.invoke delegate }
    size    = { -> oldSize() * 2 }
}

// Still not the correct value, but the functionality hasn't changed
assert "abc".size() == 6

// We've added .oldSize() to String's meta class, which does give us the correct value
assert "abc".oldSize() == 3


if (oldMetaClass.is(String.metaClass)) {
    String.metaClass {                      // #3 - Reverse modification
        size = { -> alias.invoke delegate }
        oldSize = { -> throw new UnsupportedOperationException() }
    }
} else {
    String.metaClass = oldMetaClass         // #4 - Reset meta class
}

assert "abc".size() == 3            // Finally, the meta class modification has been undone