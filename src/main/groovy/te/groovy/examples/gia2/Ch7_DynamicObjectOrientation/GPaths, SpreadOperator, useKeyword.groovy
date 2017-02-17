package te.groovy.examples.gia2.Ch7_DynamicObjectOrientation
/**************/
/*** GPATHS ***/
/**************/

/*
    SPREAD-DOT OPERATOR vs. DOT OPERATOR
    
    list.property    <==>    list.collect { item -> item?.property }
    list.*member     <==>    list.collect { item -> item?.member }
    
*/

// Here we call the .name property on list 'methods' to get a list of method names
List getterMethodNames = this.class.methods.name.grep(~/get.*?/).sort()
getterMethodNames = getterMethodNames - "getList"
assert getterMethodNames ==  ["getBinding", "getClass", "getMetaClass", "getProperty"]



/***********************/
/*** SPREAD OPERATOR ***/
/***********************/

// Spread operator w/ Lists
def getList = {
    return [1,2,3]
}
def sum = { a, b ,c ->
    return a + b + c
}
List list = getList()
assert 6 == sum(*list)

// Spread operator w/ Ranges
def range = (1..3)
assert [0, *range] == [0,1,2,3]

// Spread operator w/ Maps
def map = [b:2, c:3]
assert [a:1, *:map] == [a:1, b:2, c:3]



/*********************/
/*** use() KEYWORD ***/
/*********************/

/*  
    WHEN TO UTILIZE THE use KEYWORD:
    
    1.) To provide special-purpose methods or one-time method/operator overriding
    2.) To provide additional methods to a library
    3.) To provide a collection of methods on different receivers that work together
        e.g. a new encryptWrite() method on java.io.Outputstream and a new
        decryptRead() on java.io.Inputstream
    4.) Where java uses the Decorator Pattern, but w/o hassle of relay methods
    5.) To split an overly large class into a core class and multiple aspect 
        categories that are used with the core class as needed.
        
*/

/*  
    USAGE NOTES:

    1.) The type a category method accepts is of the type of their first
        parameter (usually named 'self') unless the first parameter is
        of type 'Object', then the category method applies to all objects
    2.) Never use a primitive for the type of the 'self' parameter in a
        category method, instead use the box'ed primitive (ie Integer, not int)
    3.) You can supply as many Category objects in a use-statement as you want
    
*/



/*** EXAMPLE 1 ***/
@Category(String)
class StringCalculationCategory {
    def plus(String operand) {
        try {
            return this.toInteger() + operand.toInteger()
        } catch(all) {
            return (this << operand).toString()
        }
    }
}

// Inside of this use-block StringCalculationCategory's .plus() method is called
use(StringCalculationCategory) {
    assert '1' + '0' == 1
    assert '1' + '1' == 2
    assert 'x' + '1' == 'x1'
}

// Outside of the use-block String's .plus() method is called
assert '1' + '1' == '11'




/*** EXAMPLE 2 ***/
@Category(Object)
class PersistenceCategory {
    String save() {
        return "saved"
    }
}

class InitializerCategory {
    static int init(Integer self) {
        return -1
    }
    
    static String init(String self) {
        return "string"
    }
}

use(PersistenceCategory, InitializerCategory) {
    def someObject
    int someInt
    String someString = ""

    assert someObject.save() == "saved"
    assert    someInt.save() == "saved"
    assert someString.save() == "saved"
    
    assert    someInt.init() == -1   
    assert someString.init() == "string"
}


