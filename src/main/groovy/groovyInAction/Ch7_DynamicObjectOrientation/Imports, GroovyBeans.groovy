package groovyInAction.Ch7_DynamicObjectOrientation
/***********************************/
/*** DEFAULT GROOVY FILE IMPORTS ***/
/***********************************/
import java.lang.*             
import java.util.*              
import java.io.*              
import java.net.*             
import groovy.lang.*             
import groovy.util.*             
import java.math.BigInteger             
import java.math.BigDecimal



/***********************/
/*** IMPORT ALIASING ***/
/***********************/
// This can be useful for conveying intent (e.g. alias Map to BackpackContents, etc.)
import java.util.ArrayList as OrigArrayList

class SpecialList extends OrigArrayList {
    void replaceAllElementsWithNull() {
        (0..< this.size()).each { index ->
            this.set(index, null)
        }
    }
}    

SpecialList list = []

list << 1
list << 2
assert list == [1,2]

list.replaceAllElementsWithNull()
assert list == [null, null]



/*******************/
/*** GROOVYBEANS ***/
/*******************/

// Printing properties of a GroovyBean dynamically
class SomeClass {
    int     somePropertyWithType       // property
    def     somePropertyWithoutType    // property
    public  someField                  // public field or instance variable
    private somePrivateField           // private field or instance variable
}

// Q: What's the difference between a property and a field?
// A: Groovy treats fields as properties if they don't specify their
//    visibility (i.e. when they don't declare 'public' or 'private', etc.)
def someClass = new SomeClass()
def store = []
someClass.properties.each { property ->
    store += property.key
    store += property.value
}

assert store.contains('somePropertyWithoutType')
assert store.contains('somePropertyWithType')
assert store.contains('someField') == false
assert store.contains('somePrivateField') == false
assert store.contains('class')
assert someClass.properties.size() == 3

// Accessing a field of a GroovyBean directly (not through generated getter/setter)
someClass.@someField = 'some value'