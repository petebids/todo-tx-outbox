package xyz.petebids.todotxoutbox.application.rest.impl;

import com.github.f4b6a3.uuid.UuidCreator;
import org.apache.kafka.clients.producer.internals.BuiltInPartitioner;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

public class PartitionerTest {


    @Test
    void testPartitionBalance() {

        Map<Integer, Long> countByPartitionNumber = new HashMap<>();

        IntStream.range(1, 10_000_000).forEach(

                unused -> {
                    UUID uuid = UuidCreator.getTimeOrderedEpoch();
                    final int partition = BuiltInPartitioner.partitionForKey(uuid.toString().getBytes(Charset.defaultCharset()), 20);
                    Long count = countByPartitionNumber.get(partition);

                    if (count == null) {
                        count = 0L;

                    }

                    count++;
                    countByPartitionNumber.put(partition, count);

                }


        );

        countByPartitionNumber.forEach((integer, aLong) -> System.out.println("partition " + integer + " has a count of " + aLong));


    }
}
