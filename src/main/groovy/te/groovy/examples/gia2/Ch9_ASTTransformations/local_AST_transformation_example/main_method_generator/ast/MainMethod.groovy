package te.groovy.examples.gia2.Ch9_ASTTransformations.local_AST_transformation_example.main_method_generator.ast

import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/*************************************************/
/*** Configuring the transformation annotation ***/
/*************************************************/

@Retention(RetentionPolicy.SOURCE)                                    // Do not persist this annotation through to the final class file
@Target([ElementType.METHOD])                                         // Only allow this annotation on methods
@GroovyASTTransformationClass(classes = [MainMethodTransformation])   // Point this annotation to the correct transformation
public @interface MainMethod {}