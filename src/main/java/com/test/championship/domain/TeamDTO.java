package com.test.championship.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TeamDTO(
        @NotNull
        @Size(min = 3, max = 50)
        String name,
        @NotNull
        int titles,
        @NotNull
        @Size(min = 3, max = 50)
        String stadium,
        @NotNull
        Manager manager,
        @NotNull
        List<PlayerDTO> players
) {
}
