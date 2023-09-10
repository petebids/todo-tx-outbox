package xyz.petebids.todotxoutbox.domain.model;

import java.util.List;

public record Page<T>(List<T> items, String next) {
}
