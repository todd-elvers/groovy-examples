package te.groovy.examples.gia2.Ch9_ASTTransformations

import groovy.util.logging.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/*

    Logging AST annotations       |   The type of logger they add:
            @Log                  |         java.util.Logger
            @Log4j                |         log4j Logger
            @Slf4j                |         slf4j Logger
            @Commons              |         Apache Commons Logger



    They take one optional parameter:
        the name of the log variable (default = log)

*/

public class JavaLoggingExample {
    private static final Logger log = LoggerFactory.getLogger(JavaLoggingExample);

    public void blowUp() {
        log.debug("This is a log message, let's pretend it's expensive.");
    }

    public void sameMethodAsAboveButWithLessOverhead() {
        if(log.isDebugEnabled()) {
            log.debug("This is how we make it not cost anything in Java.");
        }
    }
}



@Slf4j
class GroovyLogging {

    def blowUp() {
        log.debug("This is not wasteful if we're not on DEBUG level.")
    }

    def blowUpAfterAST() {
        if(log.isDebugEnabled()) {
            log.debug("${42/0}")
        }
    }
}