package xyz.petebids.todotxoutbox.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProcessedMessageEntity {

    @Id
    private String hash;

    private String topic;

    private byte[] payload;

    private Long sequence;


}
