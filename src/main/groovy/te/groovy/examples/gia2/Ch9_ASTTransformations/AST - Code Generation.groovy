package te.groovy.examples.gia2.Ch9_ASTTransformations

import groovy.transform.*

import static java.sql.DriverManager.getConnection



/****************/
/*** ToString ***/
/****************/
// Automatically generates the .toString() method
@ToString(includePackage = false)
class Person {
    String first, last
}

def p = new Person(first:'John', last:'Doe')

println p.toString()






/***************************/
/*** Equals and HashCode ***/
/***************************/
// Automatically generates the .equals() and .hashCode() methods
@EqualsAndHashCode
class Person1 {
    String first, last
}

def p1 = new Person1(first:'John', last: 'Doe')
def p2 = new Person1(first:'John', last: 'Doe')

assert p1 == p2






/*************************/
/*** Tuple Constructor ***/
/*************************/
// Adds all possible constructors
@TupleConstructor
class Person2 {
    String first, last
}
p1 = new Person2('John', 'Doe')
p2 = new Person2('John')

[p1, p2].each {
//    println("$it.first $it.last")
}





/*****************/
/*** Canonical ***/
/*****************/
// Adds the following annotations: @ToString, @EqualsAndHashCode, @TupleConstructor
@Canonical
class Person3 {
    String first, last
}

p1 = new Person3('John', 'Doe')
p2 = new Person3('John')

assert p1.first == p2.first
assert p1.toString().split('\\.')[-1] == 'Person3(John, Doe)'






/************/
/*** Lazy ***/
/************/
// @Lazy delays instantiation of an object until it is first referenced (aka lazy loading)
class Person4 {
    @Lazy
    def connection = getConnection('jdbc:odbc:dummy', 'sa', '')
}


assert new Person4()
// NOTE: By default this is NOT thread-safe. To make this thread-safe, add the 'transient' keyword.
// The 'transient' keyword normally marks a field as not serializable, but @Lazy
// makes it act differently by instead initializing the field from within a
// synchronized double-checked lock block.




/***************************/
/*** Inherit Constructors **/
/***************************/
// Generates matching constructors for every super class constructor
@InheritConstructors
class MyCustomException extends Exception { }

assert new MyCustomException("Custom Exception").message == "Custom Exception"