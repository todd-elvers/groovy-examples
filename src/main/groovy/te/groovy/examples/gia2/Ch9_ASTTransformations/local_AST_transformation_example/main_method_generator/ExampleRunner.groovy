package te.groovy.examples.gia2.Ch9_ASTTransformations.local_AST_transformation_example.main_method_generator

class ExampleRunner {

/*
    Local transformations...
        - rely on annotations to trigger Groovy classes that then transform the source code at compile time
        - are easier to write than global transformations because...
            - Groovy takes care of instantiating and invoking your transformation correctly
            - Groovy makes sure to avoid calling it your transformation when it is not needed




    The following is an example of a local AST transformation.

    The transformation @MainMethod on the performTask() method of the Example class will cause:

        public static void main(String[] args){
            new Example().performTask()
        }

    to be generated at compile time in the Example class.


    For reference:
            MainMethod.groovy               = the AST transformation annotation
            MainMethodTransformation.groovy = the AST transformation class
            Example.groovy                  = the class getting the AST transformation applied to it
 */


    // To run this example, compile the child 'ast' directory and then run the following:
    public static void main(String[] args){
        Example.main()
    }
}
