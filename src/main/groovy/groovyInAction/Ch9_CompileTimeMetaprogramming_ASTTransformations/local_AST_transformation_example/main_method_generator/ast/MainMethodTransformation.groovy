package groovyInAction.Ch9_CompileTimeMetaprogramming_ASTTransformations.local_AST_transformation_example.main_method_generator.ast

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

/********************************************/
/*** Configuring the transformation class ***/
/********************************************/

// The following annotation tells the compiler when to apply our transformation
// The CompilePhase must be 'Semantic Analysis' or later (otherwise the @Main annotation won't be compiled yet)
@GroovyASTTransformation(phase = CompilePhase.INSTRUCTION_SELECTION)
public class MainMethodTransformation implements ASTTransformation {


    /*
     * Generally, 'nodes' is utilized for local transformations and 'source' for global transformations.
     *
     * The parameter 'nodes' is comprised of two elements:
     *      - Element 0 is the AnnotationNode that triggered this annotation to be activated.
     *      - Element 1 is the AnnotatedNode decorated by a class, such as MethodNode, ClassNode, FieldNode, etc.
     *
     * For local transformations this method is invoked each time our @Main annotation is encountered.
     * For global transformations this method is invoked for each SourceUnit (which is typically a source file).
     *
     * For clarification see {@link org.codehaus.groovy.transform.ASTTransformation}.
     */
    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        // Use guard clauses here as a form of defensive programming (omitted for clarity)

        MethodNode annotatedMethod = nodes[1] as MethodNode
        ClassNode declaringClass = annotatedMethod.declaringClass
        MethodNode mainMethod = makeMainMethod(annotatedMethod)
        declaringClass.addMethod(mainMethod)
    }


    private static MethodNode makeMainMethod(MethodNode source) {
        String className = source.declaringClass.name
        String methodName = source.name

        CompilePhase compilePhase = CompilePhase.INSTRUCTION_SELECTION

        List<ASTNode> nodes = new AstBuilder().buildFromString(compilePhase, """\
            package $source.declaringClass.packageName

            class $source.declaringClass.nameWithoutPackage {
                public static void main(String[] args){
                    new $className().$methodName()
                }
            }
        """)

        /**
         * //TODO: Finish clarifying these comments
         * The list of ASTNode objects 'nodes' has BlockStatement and ClassNode objects in it.  It has one
         * ClassNode object for each class declared in .buildFromString() and a variable number of
         * BlockStatement objects.
         *
         * See {@link org.codehaus.groovy.ast.builder.AstStringCompiler} for the implementation of .buildFromString().
         */
        def classNode = nodes[1] as ClassNode

        return classNode.methods.find { it.name == 'main' }
    }
}