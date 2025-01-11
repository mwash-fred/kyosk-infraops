package app.kyosk.infraops.cars.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PagedResponse<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last,
        String nextLink,
        String prevLink,
        String firstLink,
        String lastLink
) {

    public static <T> PagedResponse<T> fromPage(Page<T> page, String baseUrl) {
        String nextLink = page.hasNext() ? baseUrl + "?page=" + (page.getNumber() + 1) + "&size=" + page.getSize() : null;
        String prevLink = page.hasPrevious() ? baseUrl + "?page=" + (page.getNumber() - 1) + "&size=" + page.getSize() : null;
        String firstLink = baseUrl + "?page=0&size=" + page.getSize();
        String lastLink = baseUrl + "?page=" + (page.getTotalPages() - 1) + "&size=" + page.getSize();
        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                nextLink,
                prevLink,
                firstLink,
                lastLink
        );
    }
}