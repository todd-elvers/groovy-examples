package groovyInAction.Ch9_CompileTimeMetaprogramming_ASTTransformations.local_AST_transformation_example.log_message_generator.ast

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotatedNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.messages.SyntaxErrorMessage
import org.codehaus.groovy.syntax.SyntaxException
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class LoggingASTTransformation implements ASTTransformation {

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        if(invalidParameters(nodes)) return

        AnnotationNode annotationInformation = nodes[0] as AnnotationNode
        AnnotatedNode annotatedNode = nodes[1] as AnnotatedNode

        if(annotatedNode instanceof MethodNode){
            MethodNode methodNode = annotatedNode as MethodNode
            if (methodNode.code instanceof BlockStatement) {
                Statement startPrintlnMessage = createPrintlnStatement("Starting $methodNode.name")
                Statement endPrintlnMessage = createPrintlnStatement("Ending $methodNode.name")

                BlockStatement blockStatement = methodNode.code as BlockStatement
                blockStatement.statements.add(0, startPrintlnMessage)
                blockStatement.statements.add(endPrintlnMessage)
            }
        } else {
            injectError(source, annotatedNode, "Found @${WithLogging.simpleName} in an unsupported location. This annotation only works on methods.")
        }
    }

    private static boolean invalidParameters(ASTNode[] nodes){
        !nodes || !nodes[0] || !nodes[1] || !(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof AnnotatedNode)
    }

    private static injectError(SourceUnit sourceUnit, ASTNode astNode, String message){
        final SyntaxException syntaxException = new SyntaxException(message, astNode.getLineNumber(), astNode.getColumnNumber())
        final SyntaxErrorMessage syntaxErrorMessage = new SyntaxErrorMessage(syntaxException, sourceUnit)
        sourceUnit.getErrorCollector().addError(syntaxErrorMessage, true)
    }

    private static ExpressionStatement createPrintlnStatement(String message) {
        return new ExpressionStatement(
                new MethodCallExpression(
                        new VariableExpression("this"),
                        new ConstantExpression("println"),
                        new ArgumentListExpression(
                                new ConstantExpression(message)
                        )
                )
        );
    }
}
