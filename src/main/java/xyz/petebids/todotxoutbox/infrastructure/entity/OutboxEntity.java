package xyz.petebids.todotxoutbox.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Setter
@Entity
public class OutboxEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "aggregatetype", updatable = false, nullable = false)
    private String aggregateType;

    @Column(name = "aggregateid", updatable = false, nullable = false)
    private String aggregateId;

    @Column(name = "type", updatable = false, nullable = false)
    private String type;

    @Column(name = "payload")
    private byte[] payload;


}
