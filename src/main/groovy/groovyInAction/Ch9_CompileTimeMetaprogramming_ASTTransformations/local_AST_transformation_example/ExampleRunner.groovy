package groovyInAction.Ch9_CompileTimeMetaprogramming_ASTTransformations.local_AST_transformation_example

class ExampleRunner {

/*
    Local transformations...
        - rely on annotations to rewrite Groovy code
        - are easier to write than global transformations because...
            - Groovy takes care of instantiating and invoking your transformation correctly
            - Groovy makes sure to avoid calling it your transformation when it is not needed




    The following is an example of a local AST transformation.

    The transformation @Main on the greet() method of the Greeter class will cause a public static main
    method to be generated at compile time in the Greeter class with the contents: new Greeter().greet()


    For reference:
            Main.groovy                     = the AST transformation annotation
            MainMethodTransformation.groovy = the AST transformation class
            Greeter.groovy                  = the class getting the AST transformation applied to it
 */

    public static void main(String[] args){
        Greeter.main()
    }
}
