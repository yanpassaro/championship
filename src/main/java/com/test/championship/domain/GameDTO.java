package com.test.championship.domain;

public record GameDTO(
        String home,
        String away,
        String date,
        String stadium
) {
}
