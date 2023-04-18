package xyz.petebids.todotxoutbox.infrastructure.event;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.petebids.todotxoutbox.application.kafka.annotation.KafkaConfiguration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OutboxConfiguration {


    @Value("${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    @Bean
    public KafkaAvroDeserializer kafkaAvroDeserializer(SchemaRegistryClient schemaRegistryClient) {

        return new KafkaAvroDeserializer(schemaRegistryClient);
    }

    @Bean
    public KafkaAvroSerializer kafkaAvroSerializer(SchemaRegistryClient schemaRegistryClient) {

        final Map<String, Object> props = new HashMap<>();
        props.put("auto.register.schemas", true);
        props.put("schema.registry.url", schemaRegistryUrl);
        return new KafkaAvroSerializer(schemaRegistryClient, props);
    }

    @Bean
    public SchemaRegistryClient schemaRegistryClient() {
        return new CachedSchemaRegistryClient(schemaRegistryUrl, 100);
    }


}
