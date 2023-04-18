package xyz.petebids.todotxoutbox.application.kafka.configuration;

import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import xyz.petebids.todotxoutbox.application.kafka.annotation.KafkaConfiguration;

import java.util.HashMap;
import java.util.Map;

@KafkaConfiguration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String broker;
    @Value("${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    public Map<String, Object> stringConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        props.put(ConsumerConfig.GROUP_ID_CONFIG, "avro");
        props.put("schema.registry.url", schemaRegistryUrl);

        return props;
    }

    @Bean
    public ConsumerFactory<String, String> stringConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(stringConsumerConfigs(), new StringDeserializer(),
                new StringDeserializer());
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> stringStringConcurrentKafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<String, String> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(stringConsumerFactory());
        return containerFactory;
    }

    @Bean(name = "avroConsumerFactory")
    public ConsumerFactory<String, Object> avroConsumerFactory(SchemaRegistryClient schemaRegistryClient) {
        final Map<String, Object> props = stringConsumerConfigs();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");
        return (new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new KafkaAvroDeserializer(schemaRegistryClient)));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> avroContainerFactory(SchemaRegistryClient schemaRegistryClient) {
        final ConcurrentKafkaListenerContainerFactory<String, Object> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(avroConsumerFactory(schemaRegistryClient));
        return containerFactory;
    }

    @Bean
    public ProducerFactory<Object, Object> producerFactory() {

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put("schema.registry.url", schemaRegistryUrl);

        return new DefaultKafkaProducerFactory<>(props);
    }
}
