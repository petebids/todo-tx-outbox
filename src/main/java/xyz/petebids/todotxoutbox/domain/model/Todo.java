package xyz.petebids.todotxoutbox.domain.model;

import java.util.UUID;

public record Todo(UUID id, String details, Boolean completed, String creator) {
}
