package te.groovy.examples.gia2.Ch9_ASTTransformations

import groovy.transform.*

import static java.sql.DriverManager.getConnection



/****************/
/*** ToString ***/
/****************/
// Automatically generates the .toString() method
@ToString(includePackage = false, includeNames = true)
class PersonWithToString {
    String first, last
}

def p = new PersonWithToString(first:'John', last:'Doe')

println p.toString()






/***************************/
/*** Equals and HashCode ***/
/***************************/
// Automatically generates the .equals() and .hashCode() methods
@EqualsAndHashCode
class PersonWithEqualsAndHashCode {
    String first, last
}

def p1 = new PersonWithEqualsAndHashCode(first:'John', last: 'Doe')
def p2 = new PersonWithEqualsAndHashCode(first:'John', last: 'Doe')

assert p1 == p2






/*************************/
/*** Tuple Constructor ***/
/*************************/
// Adds all possible constructors
@TupleConstructor
@ToString(includePackage = false)
class PersonWithTupleConstructor {
    String first, last
}
def tuplePerson1 = new PersonWithTupleConstructor('John', 'Doe')
def tuplePerson2 = new PersonWithTupleConstructor('John')

[tuplePerson1, tuplePerson2].each {
    println("$it")
}

// NOTE: This will not do anything if a constructor is already present, unless you specify 'force=true'












/************/
/*** Lazy ***/
/************/
// @Lazy delays instantiation of an object until it is first referenced (aka lazy loading)
class ConnectionWrapper {
    @Lazy
    def connection = getConnection('jdbc:odbc:dummy', 'sa', '')
}


assert new ConnectionWrapper()

/*
    NOTE: By default this is NOT thread-safe. To make this thread-safe, add the 'transient' keyword.
    The 'transient' keyword normally marks a field as not serializable, but @Lazy
    makes it act differently by instead initializing the field from within a
    synchronized double-checked lock block.
 */




/***************************/
/*** Inherit Constructors **/
/***************************/
// Generates matching constructors for every super class constructor
@InheritConstructors
class MyCustomException extends Exception {

}

assert new MyCustomException("Custom Exception").message == "Custom Exception"




/*****************/
/*** Canonical ***/
/*****************/
// Adds the following annotations: @ToString, @EqualsAndHashCode, @TupleConstructor
@Canonical
class Pet {
    String type, name
}

def cat = new Pet('Cat', 'Whiskers')
def dog = new Pet('Dog')

assert cat.type == "Cat"
assert dog.type == "Dog"
assert cat.toString().split('\\.').last() == 'Pet(Cat, Whiskers)'