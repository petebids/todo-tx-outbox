package xyz.petebids.todotxoutbox.application.rest.impl;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SpringBootTest(properties = {"application.kafka.enabled=false"})
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnableAutoConfiguration(exclude = {KafkaAutoConfiguration.class,JpaRepositoriesAutoConfiguration.class})
public @interface RestApiTest {
}
