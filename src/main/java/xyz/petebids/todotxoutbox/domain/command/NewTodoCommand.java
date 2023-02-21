package xyz.petebids.todotxoutbox.domain.command;

import jakarta.validation.constraints.NotNull;

public record NewTodoCommand(@NotNull String details, @NotNull String creator) {
}
