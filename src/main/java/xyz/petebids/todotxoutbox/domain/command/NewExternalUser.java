package xyz.petebids.todotxoutbox.domain.command;

public record NewExternalUser(String id, String firstName, String lastName, String email) {
}
