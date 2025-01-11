package app.kyosk.infraops.cars.dto;

import app.kyosk.infraops.cars.util.PageLinks;
import app.kyosk.infraops.cars.util.PageMetadata;
import org.springframework.data.domain.Page;

import java.util.List;

public record PagedResponse<T>(
        List<T> content,
        PageMetadata metadata,
        PageLinks links
) {
    public static <T> PagedResponse<T> from(Page<T> page, String baseUrl) {
        return new PagedResponse<>(
                page.getContent(),
                PageMetadata.from(page),
                PageLinks.from(page, baseUrl)
        );
    }
}