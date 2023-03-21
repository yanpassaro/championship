package com.test.championship.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PlayerDTO(
        @NotNull
        @Size(min = 3, max = 50)
        String name,
        @NotNull
        @Size(max = 2)
        String position,
        @NotNull
        @Size(min = 3, max = 50)
        String nacionality,
        @NotNull
        int age,
        @NotNull
        int number
) {
}
