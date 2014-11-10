package groovyInAction.Ch9_CompileTimeMetaprogramming_ASTTransformations.local_AST_transformation_example.log_message_generator

import groovy.transform.CompileStatic

@CompileStatic
class ExampleRunner {

    // To run this example, compile the child 'ast' directory and then run the following:
    static void main(String... args){
        new Example().performTask()
    }

}
