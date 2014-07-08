package groovyInAction.Ch9_CompileTimeMetaprogramming_ASTTransformations.local_AST_transformation_example

class Greeter {

    @Main
    def greet(){
        println "Hello from the greet() method!"
    }



    /*
        Given the above annotation, MainMethodTransformation will inject the following code into this class:

            public static void main(String[] args){
               new Greeter().greet()
            }

    */
}
