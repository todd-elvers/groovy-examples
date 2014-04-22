package groovyInAction.Ch8_DynamicProgramming

// The view of the author is that good object-oriented design only uses inheritance where there
// is a true "is-a" relationship between subclass and superclass

/*********************************/
/*** Using Mixins With Testing ***/
/*********************************/

// The following is considered "intrusive" since it requires
// that we change the code of the class receiving the new features
@Mixin(MessageFeature)
class FirstTest extends GroovyTestCase {
    void testWithMixinUsage(){
        message = "Called from Test"
        assertMessage "Called from Test"
    }
}

class MessageFeature {
    def message
    void assertMessage(String msg) { 
        assertEquals msg, message
    }
}
    
    
// The following is considered "non-intrusive" since it does not
// require that we change the code of the class 
// receiving the new features
class EvenSieve {
    def removeAllEvenElements() {
        removeAll { it % 2 == 0 }
        return this
    }
}
class MinusSieve {
    // Removes all elements divisible by 'num'
    def minus(int num) {
        removeAll { it % num == 0 }
        return this
    }
}

List.mixin(EvenSieve, MinusSieve)
assert [1, 9] - 3 == [1]
assert (0..10)
        .toList()
        .removeAllEvenElements()
        .minus(3)
        .minus(5) == [1, 7]

/*

    Mixins are generally used for sharing features while not modifying any existing behavior of the receiver.

    Mixin takeaways:

        - Mixins don't utilize inheritance so there's no "is-a" relationship and no polymorphism.

        - The mixing-in of new features always happens in traceable sequence and, in case of conflicts, the latest
          addition wins (They work like meta class changes in that respect).

        - You can instantiate objects from a blend of many classes.
            + The object's state and behavior encompasses all properties and methods of all mixed classes

        - There is an intrusive use with the '@Mixin' class annotation and a non-intrusive use with the 'mixin' method
          on classes.
            + Both alternatives happen at runtime, but '@Mixin' happens at class construction time in a static block

        - Mixins are visible in all threads.

        - There are no restrictions with respect to what methods to mix in.  Property accessors, operator methods,
          GroovyObject methods, and even MOP hook methods all work fine.

        - You can mix into superclasses and interfaces.

        - A Mixin can override a method of a previous Mixin, but no methods in the meta class.

        - There is no per-instance Mixin.  You can only mix into classes and meta classes.  To achieve the effect of
          per-instance Mixin, you can mix into a per-instance meta class.

        - Mixins cannot easily be un-done.

 */


/*

    MOP Priorities:
        1st - Category class
        2nd - The MetaClass
        3rd - The Mixins

    (This only applies to methods that are defined for the same class and have the same parameter types)


 */