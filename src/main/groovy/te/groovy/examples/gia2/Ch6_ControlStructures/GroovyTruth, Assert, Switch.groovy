package te.groovy.examples.gia2.Ch6_ControlStructures
/*
    GROOVY TRUTH
    
    RUNTIME TYPE           EVALUATION CRITERION REQUIRED FOR TRUTH
    --------------------------------------------------------------
    boolean                Corresponding Boolean value is true
    Matcher                Matcher has a match
    Collection             Collection is non-empty
    Map                    Map is non-empty
    String/GString         String is non-empty
    Number/Character       Value is non-zero
    None of the above      Object reference is non-null
    
*/

// Groovy allows assignments in if-statements if they are surrounded by parenthesis
def x = 2
if((x = 3)) {
    assert true
}
assert x == 3


/*
    SWITCH .isCase() IMPLMENTATIONS
                        
    CLASS         a.isCase(b) IMPLEMENTED AS
    -----------------------------------------------
    Object        a.equals(b)
    Class         a.isInstance(b)
    Collection    a.contains(b)
    Range         a.contains(b)
    Pattern       a.matcher(b.toString()).matches()
    String        (a==null && b==null) || a.equals(b)
    Closure       a.call(b)         
                                    
*/



/*
    ASSERT BEST PRACTICES
    
    1.) Before writing an assertion, let your code fail, and see whether any other
        thrown exception is good enough
    2.) When writing an assertion, let it fail the first time, and see whether the
        failure message is sufficient.  If not, add a message.  Let it fail again to 
        verify that the message is now good enough.
    3.) If you feel you need an assertion to clarify or protect your code, add it
        regardless of the previous rules.
    4.) If you feel you need a message to clarify the meaning or purpose of your
        assertion, add it regardless of the previous rules.

*/

// Error found on page 164 -> It is not necessary to specify a type in a catch(e)