import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.status.NopStatusListener
import ch.qos.logback.core.status.OnConsoleStatusListener

import static ch.qos.logback.classic.Level.ERROR

statusListener(NopStatusListener)


appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "[%-5level] %logger{36}:%L - %msg%n"
    }
}

root(INFO, ["FILE", "CONSOLE"])

