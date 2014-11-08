package newInGroovy2_3

import groovy.transform.builder.Builder
import groovy.transform.builder.ExternalStrategy
import groovy.transform.builder.InitializerStrategy
import groovy.transform.builder.SimpleStrategy

/*
    The @Builder AST transformation simplifies building objects following the Java Builder pattern.

    The Java Builder pattern is seen a lot when dealing with immutable objects. Say you want to build an instance
    of your immutable Person class using the above pattern.  Often times you'd see something to the effect of:

                Person person = new Person.PersonBuilder().withFirstName("...")
                                                          .withLastName("...")
                                                          .build()

    How it's done:
        - Person has only one constructor, which is private, that takes an instance of PersonBuilder
        - PersonBuilder is an public static class that resides in the Person class
        - PersonBuilder has setter methods that return an instance of 'this' (ie the instance of PersonBuilder)
          in order to allow for method chaining (http://en.wikipedia.org/wiki/Method_chaining)
        - PersonBuilder has a build method (usually called 'build') that returns an instance of Person by calling
          Person's private constructor with the instance of PersonBuilder we just got done chaining methods on,
          returning an instance of Person with the values we set on PersonBuilder



    One implementation called 'fluent interfaces' (http://en.wikipedia.org/wiki/Fluent_interface) involves method
    chaining on a class to set values, but then ending the chain of method calls with a void method.
    Here is an example of a fluent interface in Java:

                new Email().to("...")
                           .from("...")
                           .withSubject("...")
                           .withBody("...")
                           .send();



    Groovy has its own way to allow for fluent interfaces which is just as easy to read and doesn't require that the
    setters in your class return an instance of 'this'.  In the following example, all the methods modify the state
    of the same Email object like above, but all the methods called are void instead of just the last one:

                new Email().with {
                    to("...")
                    from("...")
                    withSubject("...")
                    withBody("...")
                    send()
                }


    Since there are different ways to implement the Java Builder pattern, there are different strategies
    to implement the @Builder AST transformation.  Below are examples of the four different strategies
    present in Groovy v2.3.3.
*/









// All of the following examples work, but Intellij 13.1.3 reads @Builder and thinks all the listed
// attributes MUST be specified and highlights it red (Severity: Error) otherwise.  Not only is this a
// nuisance but some of the ways to configure @Builder don't allow you to set all attributes.
// Essentially the IDE will complain to you, but the code works fine.



/*
    SimpleStrategy

    This strategy just adds the appropriate getter/setter that returns an instance of itself.
    Something to the effect of:
        public Person setFirst(java.lang.String first) {
            this.first = first
            return this
        }
*/
@Builder(builderStrategy = SimpleStrategy)
class Person1 {
    String first
    String last
    Integer born
}

def p1 = new Person1().setFirst('Johnny')
                      .setLast('Depp')
                      .setBorn(1963)
assert "$p1.first $p1.last" == 'Johnny Depp'


// One can optionally set the prefix (default = 'set')
@Builder(builderStrategy = SimpleStrategy, prefix = "")
class Person2 {
    String first
    String last
    Integer born
}

def p2 = new Person2().first('Johnny')
                      .last('Depp')
                      .born(1963)
assert "$p2.first $p2.last" == 'Johnny Depp'





/*
    ExternalStrategy

    With this strategy, you create a builder and annotate it to indicate what object it builds.

    That builder has all necessary chain-able methods injected into it at compile time.  These methods return
    instances of the enclosing class

    */
class Person3 {
    String first
    String last
    Integer born
}

@Builder(builderStrategy=ExternalStrategy, forClass=Person3)
class PersonBuilder { }

def p3 = new PersonBuilder().first('Johnny')
                            .last('Depp')
                            .born(1963)
                            .build()
assert "$p3.first $p3.last" == 'Johnny Depp'





/*
    DefaultStrategy

    With this strategy, you simply annotate the given class with @Builder and an internal static class
    is injected into that class (default name is 'builder') and that inner static class does all the building
*/
@Builder
class Person4 {
    String firstName
    String lastName
    Integer born
}

def p4 = Person4.builder()
                .firstName("Johnny")
                .lastName("Depp")
                .born(1963)
                .build()
assert "$p4.firstName $p4.lastName" ==  'Johnny Depp'


// Even more customization is possible:
@Builder(buildMethodName='make', builderMethodName='maker', prefix='with', excludes='age')
class Person5 {
    String firstName
    String lastName
    int yearOfBirth
}

def p5 = Person5.maker()
                .withFirstName("Johnny")
                .withLastName("Depp")
                .withYearOfBirth(1963)
                .make()
assert "$p5.firstName $p5.lastName" ==  'Johnny Depp'






/*
    InitializerStrategy

    With this strategy, your class gets one public constructor and that constructor takes a 'fully set'
    initializer.  It will also have a factory method to create the initializer.
    If using @CompileStatic with this strategy, then all properties MUST be set in the initializer,
    otherwise a compilation error will occur.
*/

@Builder(builderStrategy=InitializerStrategy)
class Person6 {
    String firstName
    String lastName
    int yearOfBirth
}

def p6 = new Person6(
            Person6.createInitializer()
                   .firstName('Johnny')
                   .lastName('Depp')
                   .yearOfBirth(1963)
)
assert "$p6.firstName $p6.lastName" ==  'Johnny Depp'