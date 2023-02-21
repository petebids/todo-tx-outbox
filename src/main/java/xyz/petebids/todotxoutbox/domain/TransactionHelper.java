package xyz.petebids.todotxoutbox.domain;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
public class TransactionHelper {

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public <T> T executeTx(Supplier<T> supplier) {
        return supplier.get();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void executeTx(Runnable runnable) {
        runnable.run();
    }
}
