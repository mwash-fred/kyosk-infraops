package app.kyosk.infraops.cars.dto;


import java.time.LocalDateTime;
import java.util.UUID;

public record CarBrandDTO(
        UUID uuid,
        String name,
        String countryOfOrigin,
        Integer foundedYear,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
