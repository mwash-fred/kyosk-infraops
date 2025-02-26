package app.kyosk.infraops.cars.services.impl;

import app.kyosk.infraops.cars.dto.CarBrandDTO;
import app.kyosk.infraops.cars.entity.CarBrand;
import app.kyosk.infraops.cars.repository.CarBrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarBrandServiceImplTest {

    @Mock
    private CarBrandRepository carBrandRepository;

    private CarBrandServiceImpl carBrandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        carBrandService = new CarBrandServiceImpl(carBrandRepository);
    }

    // Helper method to generate a sample CarBrandDTO
    private CarBrandDTO createCarBrandDTO(UUID uuid) {
        return new CarBrandDTO(
                uuid,
                "Toyota",
                "Japan",
                1937,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    // Helper method to generate a sample CarBrand entity
    private CarBrand createCarBrand(UUID uuid) {
        return CarBrand.builder()
                .uuid(uuid)
                .name("Toyota")
                .countryOfOrigin("Japan")
                .foundedYear(1937)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testSave() {
        CarBrandDTO carBrandDTO = createCarBrandDTO(UUID.randomUUID());
        CarBrand carBrandEntity = createCarBrand(carBrandDTO.uuid());

        // Mocking the repository save behavior
        when(carBrandRepository.save(any(CarBrand.class))).thenReturn(carBrandEntity);

        // Calling the save method
        CarBrandDTO savedCarBrand = carBrandService.save(carBrandDTO);

        // Assertions
        assertNotNull(savedCarBrand);
        assertEquals(carBrandDTO.name(), savedCarBrand.name());
        assertEquals(carBrandDTO.countryOfOrigin(), savedCarBrand.countryOfOrigin());
        assertEquals(carBrandDTO.foundedYear(), savedCarBrand.foundedYear());

        // Verify the interaction with the repository
        verify(carBrandRepository, times(1)).save(any(CarBrand.class));
    }

    @Test
    void testFindAll() {
        CarBrand carBrandEntity = createCarBrand(UUID.randomUUID());
        List<CarBrand> carBrands = Collections.singletonList(carBrandEntity);

        // Mocking the repository findAll behavior
        when(carBrandRepository.findAll()).thenReturn(carBrands);

        // Calling the findAll method
        List<CarBrandDTO> carBrandDTOs = carBrandService.findAll();

        // Assertions
        assertNotNull(carBrandDTOs);
        assertEquals(1, carBrandDTOs.size());
        assertEquals(carBrandEntity.getName(), carBrandDTOs.get(0).name());
    }

    @Test
    void testFindAllPageable() {
        CarBrand carBrandEntity = createCarBrand(UUID.randomUUID());
        Page<CarBrand> carBrandPage = new PageImpl<>(Collections.singletonList(carBrandEntity));

        // Mocking the repository findAll(Pageable) behavior
        Pageable pageable = PageRequest.of(0, 10);
        when(carBrandRepository.findAll(pageable)).thenReturn(carBrandPage);

        // Calling the findAll method with pagination
        Page<CarBrandDTO> carBrandDTOs = carBrandService.findAll(pageable);

        // Assertions
        assertNotNull(carBrandDTOs);
        assertEquals(1, carBrandDTOs.getContent().size());
        assertEquals(carBrandEntity.getName(), carBrandDTOs.getContent().get(0).name());
    }

    @Test
    void testFindByUuid() {
        UUID uuid = UUID.randomUUID();
        CarBrand carBrandEntity = createCarBrand(uuid);

        // Mocking the repository findByUuid behavior
        when(carBrandRepository.findByUuid(uuid)).thenReturn(Optional.of(carBrandEntity));

        // Calling the findByUuid method
        Optional<CarBrandDTO> carBrandDTO = carBrandService.findByUuid(uuid);

        // Assertions
        assertTrue(carBrandDTO.isPresent());
        assertEquals(carBrandEntity.getName(), carBrandDTO.get().name());
    }

    @Test
    void testFindByUuidNotFound() {
        UUID uuid = UUID.randomUUID();

        // Mocking the repository findByUuid behavior for non-existent UUID
        when(carBrandRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        // Calling the findByUuid method
        Optional<CarBrandDTO> carBrandDTO = carBrandService.findByUuid(uuid);

        // Assertions
        assertFalse(carBrandDTO.isPresent());
    }

    @Test
    void testUpdate() {
        UUID uuid = UUID.randomUUID();
        CarBrand existingCarBrand = createCarBrand(uuid);
        CarBrandDTO updatedCarBrandDTO = createCarBrandDTO(uuid);
        updatedCarBrandDTO = new CarBrandDTO(
                uuid,
                "UpdatedBrand",
                "UpdatedCountry",
                2000,
                existingCarBrand.getCreatedAt(),
                existingCarBrand.getUpdatedAt()
        );

        // Mocking the repository findByUuid and save behavior
        when(carBrandRepository.findByUuid(uuid)).thenReturn(Optional.of(existingCarBrand));
        when(carBrandRepository.save(any(CarBrand.class))).thenReturn(existingCarBrand);

        // Calling the update method
        CarBrandDTO updatedCarBrand = carBrandService.update(uuid, updatedCarBrandDTO);

        // Assertions
        assertNotNull(updatedCarBrand);
        assertEquals(updatedCarBrandDTO.name(), updatedCarBrand.name());
        assertEquals(updatedCarBrandDTO.countryOfOrigin(), updatedCarBrand.countryOfOrigin());

        // Verify the interaction with the repository
        verify(carBrandRepository, times(1)).save(any(CarBrand.class));
    }

    @Test
    void testUpdateCarBrandNotFound() {
        UUID uuid = UUID.randomUUID();
        CarBrandDTO updatedCarBrandDTO = createCarBrandDTO(uuid);

        // Mocking the repository findByUuid behavior for non-existent car brand
        when(carBrandRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        // Calling the update method and expecting an exception
        assertThrows(IllegalArgumentException.class, () -> carBrandService.update(uuid, updatedCarBrandDTO));
    }

    @Test
    void testDeleteByUuid() {
        UUID uuid = UUID.randomUUID();

        // Mocking the repository deleteByUuid behavior
        doNothing().when(carBrandRepository).deleteByUuid(uuid);

        // Calling the delete method
        carBrandService.deleteByUuid(uuid);

        // Verify the interaction with the repository
        verify(carBrandRepository, times(1)).deleteByUuid(uuid);
    }
}
