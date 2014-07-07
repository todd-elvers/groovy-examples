package newInGroovy2_3




// All of the following examples work, but Intellij 13.1.3 misreads @Builder and thinks all the listed
// attributes MUST be specified and highlights it red (Severity: Error) otherwise.  Not only is this a
// nuisance but some of the ways to configure @Builder don't allow you to set all attributes.
// Essentially you cannot please the IDE, but the code executes flawlessly.
//
// This will likely be resolved soon, at which time I'll uncomment the following.











/*
    The @Builder AST transformation simplifies creating objects that contain setters that return
    instances of themselves to allow for chaining commands.

    These are known as 'fluent interfaces' and are typically used in the Java Builder pattern like:
        Person p = new Person.PersonBuilder().withFirstName('John')
                                             .withLastName('Smith')
                                             .build()

    Since there are different ways to implement the Java Builder pattern, there are different strategies
    to implement the @Builder AST transformation.  Below are examples of the four different strategies
    present in Groovy v2.3.3.
*/



///*
//    SimpleStrategy
//
//    This strategy just adds the appropriate getter/setter that returns an instance of itself.
//    Something to the effect of:
//        public Person setFirst(java.lang.String first) {
//            this.first = first
//            return this
//        }
//*/
//@Builder(builderStrategy = SimpleStrategy)
//class Person1 {
//    String first
//    String last
//    Integer born
//}
//
//def p1 = new Person1().setFirst('Johnny')
//                      .setLast('Depp')
//                      .setBorn(1963)
//assert "$p1.first $p1.last" == 'Johnny Depp'
//
//
//// One can optionally set the prefix (default = 'set')
//@Builder(builderStrategy = SimpleStrategy, prefix = "")
//class Person2 {
//    String first
//    String last
//    Integer born
//}
//
//def p2 = new Person2().first('Johnny')
//                      .last('Depp')
//                      .born(1963)
//assert "$p2.first $p2.last" == 'Johnny Depp'
//
//
//
//
///*
//    ExternalStrategy
//
//    With this strategy, you create a builder and annotate it to indicate what object it builds
//*/
//class Person3{
//    String first
//    String last
//    Integer born
//}
//
//@Builder(builderStrategy=ExternalStrategy, forClass=Person3)
//class PersonBuilder { }
//
//def p3 = new PersonBuilder().first('Johnny')
//                            .last('Depp')
//                            .born(1963)
//                            .build()
//assert "$p3.first $p3.last" == 'Johnny Depp'
//
//
//
//
///*
//    DefaultStrategy
//
//    With this strategy, you simply annotate the given class with @Builder and an internal static class
//    is injected into that class (default name is 'builder') and that inner static class does all the building
//*/
//@Builder
//class Person4 {
//    String firstName
//    String lastName
//    Integer born
//}
//
//def p4 = Person4.builder()
//                .firstName("Johnny")
//                .lastName("Depp")
//                .born(1963)
//                .build()
//assert "$p4.firstName $p4.lastName" ==  'Johnny Depp'
//
//
//// Even more customization is possible:
//@Builder(buildMethodName='make', builderMethodName='maker', prefix='with', excludes='age')
//class Person5 {
//    String firstName
//    String lastName
//    int yearOfBirth
//}
//
//def p5 = Person5.maker()
//                .withFirstName("Johnny")
//                .withLastName("Depp")
//                .withYearOfBirth(1963)
//                .make()
//assert "$p5.firstName $p5.lastName" ==  'Johnny Depp'
//
//
//
//
//
///*
//    InitializerStrategy
//
//    With this strategy, your class gets one public constructor and that constructor takes a 'fully set'
//    initializer.  It will also have a factory method to create the initializer.
//    If using @CompileStatic with this strategy, then all properties MUST be set in the initializer,
//    otherwise a compilation error will occur.
//*/
//
//@Builder(builderStrategy=InitializerStrategy)
//class Person6 {
//    String firstName
//    String lastName
//    int yearOfBirth
//}
//
//def p6 = new Person6(
//            Person6.createInitializer()
//                   .firstName('Johnny')
//                   .lastName('Depp')
//                   .yearOfBirth(1963)
//)
//assert "$p6.firstName $p6.lastName" ==  'Johnny Depp'