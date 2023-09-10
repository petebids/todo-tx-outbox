package xyz.petebids.todotxoutbox.application.rest.impl;

import com.github.f4b6a3.uuid.UuidCreator;
import org.apache.kafka.clients.producer.internals.BuiltInPartitioner;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class PartitionerTest {


    @Test
    void testPartitionBalance() {

        new HashMap<>();

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


    void run() {

    }

    class FooBar {
        private int n;
        private final Object lock = new Object();

        private volatile boolean fooNext = true;

        public FooBar(int n) {
            this.n = n;
        }

        public void foo(Runnable printFoo) throws InterruptedException {

            for (int i = 0; i < n; i++) {
                synchronized (lock) {
                    if (fooNext) {
                        // printFoo.run() outputs "foo". Do not change or remove this line.
                        printFoo.run();
                        fooNext = false;
                    } else {
                        i--;
                    }
                }
            }
        }

        public void bar(Runnable printBar) throws InterruptedException {

            for (int i = 0; i < n; i++) {
                synchronized (lock) {
                    if (!fooNext) {
                        // printBar.run() outputs "bar". Do not change or remove this line.
                        printBar.run();
                        fooNext = true;
                    } else {
                        i--;
                    }
                }
            }
        }

    }


    @Test
    void testProgram() {

        assertEquals(-1, (Program.binarySearch(new int[]{1, 5, 23, 111}, 35)));


        assertEquals(0, (Program.binarySearch(new int[]{0, 1, 21, 33, 45, 45, 61, 71, 72, 73}, 0)));
    }




    public class WeightedCollection<E> {

        private NavigableMap<Integer, E> map = new TreeMap<Integer, E>();
        private Random random;
        private int total = 0;

        public WeightedCollection() {
            this(new Random());
        }

        public WeightedCollection(Random random) {
            this.random = random;
        }

        public void add(int weight, E object) {
            if (weight <= 0) return;
            total += weight;
            map.put(total, object);
        }

        public E next() {
            int value = random.nextInt(total) + 1; // Can also use floating-point weights
            return map.ceilingEntry(value).getValue();
        }

    }

}


