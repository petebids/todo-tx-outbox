package xyz.petebids.todotxoutbox.application.kafka.annotation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Custom annotation used to prevent autowiring of kafka components where they are not desired
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@ConditionalOnProperty(
        value = "application.kafka.enabled",
        havingValue = "true",
        matchIfMissing = true)
public @interface KafkaComponent {
}
