package groovyInAction.Ch9_CompileTimeMetaprogramming_ASTTransformations.local_AST_transformation_example.log_message_generator.ast

import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.SOURCE)      // Only persist through the SOURCE (i.e. discard at compiler phase)
@Target([ElementType.METHOD])
@GroovyASTTransformationClass(classes = [LoggingASTTransformation])
public @interface WithLogging {}
