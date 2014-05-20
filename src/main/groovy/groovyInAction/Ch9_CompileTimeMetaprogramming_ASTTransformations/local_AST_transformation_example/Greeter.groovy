package groovyInAction.Ch9_CompileTimeMetaprogramming_ASTTransformations.local_AST_transformation_example

class Greeter {

    @Main
    def greet(){
        println "Hello from the greet() method!"
    }

    /*
       What will be created after the AST transformation is applied:

       public static void main(String[] args){
           new Greeter().greet()
       }

    */
}
