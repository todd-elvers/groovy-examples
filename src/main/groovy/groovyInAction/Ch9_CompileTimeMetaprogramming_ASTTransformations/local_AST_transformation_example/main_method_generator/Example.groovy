package groovyInAction.Ch9_CompileTimeMetaprogramming_ASTTransformations.local_AST_transformation_example.main_method_generator

import groovyInAction.Ch9_CompileTimeMetaprogramming_ASTTransformations.local_AST_transformation_example.main_method_generator.ast.MainMethod

class Example {

    @MainMethod
    def performTask(){
        println "Hello from the performTask() method!"
    }

    /*
        Given the above annotation, MainMethodTransformation will inject the following code into this class:

            public static void main(String[] args){
               new Example().performTask()
            }
    */

}
