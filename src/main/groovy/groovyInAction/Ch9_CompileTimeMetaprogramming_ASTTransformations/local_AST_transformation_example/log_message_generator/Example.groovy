package groovyInAction.Ch9_CompileTimeMetaprogramming_ASTTransformations.local_AST_transformation_example.log_message_generator

import groovyInAction.Ch9_CompileTimeMetaprogramming_ASTTransformations.local_AST_transformation_example.log_message_generator.ast.WithLogging

class Example {

    String instanceVariable = '$instance$'

    @WithLogging
    void performTask(){
        String localVariable = '$local$'

        println "Performing task for 5 seconds..."
        Thread.sleep(5_000)
    }

}
