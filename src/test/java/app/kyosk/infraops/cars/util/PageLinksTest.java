package app.kyosk.infraops.cars.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class PageLinksTest {

    @Mock
    private Page<?> page;

    private static final String BASE_URL = "http://example.com/cars";

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFromWithNoPreviousPage() {
        // Mock a page with 10 items, on page 0 with 5 items per page and no previous pages
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<String> mockPage = new PageImpl<>(Arrays.asList("Car1", "Car2", "Car3", "Car4", "Car5"), pageRequest, 15);

        when(page.getSize()).thenReturn(5);
        when(page.getNumber()).thenReturn(0);
        when(page.hasPrevious()).thenReturn(false);
        when(page.hasNext()).thenReturn(true);
        when(page.getTotalPages()).thenReturn(3);

        // Call the method
        PageLinks pageLinks = PageLinks.from(mockPage, BASE_URL);

        // Assertions
        assertEquals("http://example.com/cars?page=0&size=5", pageLinks.getFirst());
        assertEquals("http://example.com/cars?page=0&size=5", pageLinks.getSelf());
        assertNull(pageLinks.getPrevious());  // Since there's no previous page
        assertEquals("http://example.com/cars?page=1&size=5", pageLinks.getNext());
        assertEquals("http://example.com/cars?page=2&size=5", pageLinks.getLast());
    }

    @Test
    void testFromWithPreviousPage() {
        // Mock a page with 10 items, on page 1 with 5 items per page
        PageRequest pageRequest = PageRequest.of(1, 5);
        Page<String> mockPage = new PageImpl<>(Arrays.asList("Car6", "Car7", "Car8", "Car9", "Car10"), pageRequest, 15);

        when(page.getSize()).thenReturn(5);
        when(page.getNumber()).thenReturn(1);
        when(page.hasPrevious()).thenReturn(true);
        when(page.hasNext()).thenReturn(true);
        when(page.getTotalPages()).thenReturn(3);

        // Call the method
        PageLinks pageLinks = PageLinks.from(mockPage, BASE_URL);

        // Assertions
        assertEquals("http://example.com/cars?page=0&size=5", pageLinks.getFirst());
        assertEquals("http://example.com/cars?page=1&size=5", pageLinks.getSelf());
        assertEquals("http://example.com/cars?page=0&size=5", pageLinks.getPrevious());  // Previous page link
        assertEquals("http://example.com/cars?page=2&size=5", pageLinks.getNext());
        assertEquals("http://example.com/cars?page=2&size=5", pageLinks.getLast());
    }

    @Test
    void testFromWithLastPage() {
        // Mock a page with 10 items, on page 2 (last page) with 5 items per page
        PageRequest pageRequest = PageRequest.of(2, 5);
        Page<String> mockPage = new PageImpl<>(Arrays.asList("Car11", "Car12", "Car13", "Car14", "Car15"), pageRequest, 15);

        when(page.getSize()).thenReturn(5);
        when(page.getNumber()).thenReturn(2);
        when(page.hasPrevious()).thenReturn(true);
        when(page.hasNext()).thenReturn(false);
        when(page.getTotalPages()).thenReturn(3);

        // Call the method
        PageLinks pageLinks = PageLinks.from(mockPage, BASE_URL);

        // Assertions
        assertEquals("http://example.com/cars?page=0&size=5", pageLinks.getFirst());
        assertEquals("http://example.com/cars?page=2&size=5", pageLinks.getSelf());
        assertEquals("http://example.com/cars?page=1&size=5", pageLinks.getPrevious());
        assertNull(pageLinks.getNext());  // No next page
        assertEquals("http://example.com/cars?page=2&size=5", pageLinks.getLast());
    }

    @Test
    void testFromWithUrlContainingQueryParameters() {
        // Mock a page with query parameters in the URL
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<String> mockPage = new PageImpl<>(Arrays.asList("Car1", "Car2", "Car3"), pageRequest, 15);

        // URL with query parameters
        String urlWithParams = "http://example.com/cars?param=value&another=123";

        // Call the method
        PageLinks pageLinks = PageLinks.from(mockPage, urlWithParams);

        // Assertions - verify that query params were removed
        assertEquals("http://example.com/cars?page=0&size=5", pageLinks.getFirst());
        assertEquals("http://example.com/cars?page=0&size=5", pageLinks.getSelf());
        assertNull(pageLinks.getPrevious());
        assertEquals("http://example.com/cars?page=1&size=5", pageLinks.getNext());
        assertEquals("http://example.com/cars?page=2&size=5", pageLinks.getLast());
    }

    @Test
    void testFromWithUrlEndingWithSlash() {
        // Mock a page with URL ending with slash
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<String> mockPage = new PageImpl<>(Arrays.asList("Car1", "Car2", "Car3"), pageRequest, 15);

        // URL ending with slash
        String urlWithSlash = "http://example.com/cars/";

        // Call the method
        PageLinks pageLinks = PageLinks.from(mockPage, urlWithSlash);

        // Assertions - verify that trailing slash was removed
        assertEquals("http://example.com/cars?page=0&size=5", pageLinks.getFirst());
        assertEquals("http://example.com/cars?page=0&size=5", pageLinks.getSelf());
        assertNull(pageLinks.getPrevious());
        assertEquals("http://example.com/cars?page=1&size=5", pageLinks.getNext());
        assertEquals("http://example.com/cars?page=2&size=5", pageLinks.getLast());
    }

    @Test
    void testFromWithUrlContainingBothQueryParamsAndSlash() {
        // Mock a page with URL having both query params and ending with slash
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<String> mockPage = new PageImpl<>(Arrays.asList("Car1", "Car2", "Car3"), pageRequest, 15);

        // URL with both query params and trailing slash
        String complexUrl = "http://example.com/cars/?param=value";

        // Call the method
        PageLinks pageLinks = PageLinks.from(mockPage, complexUrl);

        // Assertions - verify both issues were fixed
        assertEquals("http://example.com/cars?page=0&size=5", pageLinks.getFirst());
        assertEquals("http://example.com/cars?page=0&size=5", pageLinks.getSelf());
        assertNull(pageLinks.getPrevious());
        assertEquals("http://example.com/cars?page=1&size=5", pageLinks.getNext());
        assertEquals("http://example.com/cars?page=2&size=5", pageLinks.getLast());
    }
}