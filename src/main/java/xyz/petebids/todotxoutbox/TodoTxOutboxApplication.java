package xyz.petebids.todotxoutbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class TodoTxOutboxApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoTxOutboxApplication.class, args);
    }

}
