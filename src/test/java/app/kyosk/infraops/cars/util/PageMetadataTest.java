package app.kyosk.infraops.cars.util;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PageMetadataTest {

    @Test
    void testFrom() {
        // Create a PageRequest for pagination
        PageRequest pageRequest = PageRequest.of(1, 5);

        // Create a mock page of 10 elements
        Page<String> page = new PageImpl<>(Collections.singletonList("Car"), pageRequest, 15);

        // Call the from() method to convert Page to PageMetadata
        PageMetadata metadata = PageMetadata.from(page);

        // Assertions
        assertNotNull(metadata);
        assertEquals(1, metadata.getPageNumber());
        assertEquals(5, metadata.getPageSize());
        assertEquals(15, metadata.getTotalElements());
        assertEquals(3, metadata.getTotalPages());
        assertFalse(metadata.isFirst());
        assertFalse(metadata.isLast());
        assertTrue(metadata.isHasNext());
        assertTrue(metadata.isHasPrevious());
    }

    @Test
    void testFromFirstPage() {
        // Create a PageRequest for the first page
        PageRequest pageRequest = PageRequest.of(0, 5);

        // Create a mock page of 10 elements
        Page<String> page = new PageImpl<>(Collections.singletonList("Car"), pageRequest, 10);

        // Call the from() method to convert Page to PageMetadata
        PageMetadata metadata = PageMetadata.from(page);

        // Assertions
        assertNotNull(metadata);
        assertTrue(metadata.isFirst());
        assertFalse(metadata.isLast());
        assertTrue(metadata.isHasNext());
        assertFalse(metadata.isHasPrevious());
    }

    @Test
    void testFromLastPage() {
        // Create a PageRequest for the last page
        PageRequest pageRequest = PageRequest.of(1, 5);

        // Create a mock page of 10 elements, so the last page will have 1 element
        Page<String> page = new PageImpl<>(Collections.singletonList("Car"), pageRequest, 10);

        // Call the from() method to convert Page to PageMetadata
        PageMetadata metadata = PageMetadata.from(page);

        // Assertions
        assertNotNull(metadata);
        assertFalse(metadata.isFirst());
        assertTrue(metadata.isLast());
        assertFalse(metadata.isHasNext());
        assertTrue(metadata.isHasPrevious());
    }

    @Test
    void testEmptyPage() {
        // Create a PageRequest for the first page
        PageRequest pageRequest = PageRequest.of(0, 5);

        // Create an empty mock page
        Page<String> page = new PageImpl<>(Collections.emptyList(), pageRequest, 0);

        // Call the from() method to convert Page to PageMetadata
        PageMetadata metadata = PageMetadata.from(page);

        // Assertions
        assertNotNull(metadata);
        assertEquals(0, metadata.getPageNumber());
        assertEquals(5, metadata.getPageSize());
        assertEquals(0, metadata.getTotalElements());
        assertEquals(0, metadata.getTotalPages());
        assertTrue(metadata.isFirst());
        assertTrue(metadata.isLast());
        assertFalse(metadata.isHasNext());
        assertFalse(metadata.isHasPrevious());
    }
}
