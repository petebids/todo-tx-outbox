package xyz.petebids.todotxoutbox.application.kafka.annotation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@ConditionalOnProperty(
        value = "application.kafka.enabled",
        havingValue = "true",
        matchIfMissing = true)
public @interface KafkaConfiguration {
}
