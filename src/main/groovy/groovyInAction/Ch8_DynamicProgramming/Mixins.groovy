package groovyInAction.Ch8_DynamicProgramming

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
    def minus(int num) {
        removeAll { it % num == 0 }
        return this
    }
}

List.mixin(EvenSieve, MinusSieve)
assert [1,9] - 3 == [1]
assert (0..10).toList().removeAllEvenElements().minus(3).minus(5) == [1, 7]