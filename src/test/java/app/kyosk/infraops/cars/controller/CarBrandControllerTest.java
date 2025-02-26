package app.kyosk.infraops.cars.controller;

import app.kyosk.infraops.cars.dto.CarBrandDTO;
import app.kyosk.infraops.cars.services.CarBrandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CarBrandControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CarBrandService carBrandService;

    @InjectMocks
    private CarBrandController carBrandController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(carBrandController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void createCarBrand_ShouldReturnCreatedCarBrand() throws Exception {
        UUID uuid = UUID.randomUUID();
        CarBrandDTO carBrandDTO = new CarBrandDTO(uuid, "Toyota", "Japan", 1937, null, null);
        when(carBrandService.save(any(CarBrandDTO.class))).thenReturn(carBrandDTO);

        mockMvc.perform(post("/api/v1/car-brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carBrandDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(uuid.toString()))
                .andExpect(jsonPath("$.name").value("Toyota"));
    }

    @Test
    void getAllCarBrandsPaged_ShouldReturnPagedResponse() throws Exception {
        UUID uuid = UUID.randomUUID();
        CarBrandDTO carBrandDTO = new CarBrandDTO(uuid, "Toyota", "Japan", 1937, null, null);
        PageRequest pageable = PageRequest.of(0, 10);
        Page<CarBrandDTO> page = new PageImpl<>(List.of(carBrandDTO), pageable, 1);

        when(carBrandService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(get("/api/v1/car-brands")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].name").value("Toyota"))
                .andExpect(jsonPath("$.links.self").value("http://localhost/api/v1/car-brands?page=0&size=10"));
    }

    @Test
    void getCarBrandByUuid_ShouldReturnCarBrand() throws Exception {
        UUID uuid = UUID.randomUUID();
        CarBrandDTO carBrandDTO = new CarBrandDTO(uuid, "Toyota", "Japan", 1937, null, null);
        when(carBrandService.findByUuid(uuid)).thenReturn(Optional.of(carBrandDTO));

        mockMvc.perform(get("/api/v1/car-brands/" + uuid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(uuid.toString()))
                .andExpect(jsonPath("$.name").value("Toyota"));
    }

    @Test
    void getCarBrandByUuid_ShouldReturnNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(carBrandService.findByUuid(uuid)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/car-brands/" + uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCarBrand_ShouldReturnUpdatedCarBrand() throws Exception {
        UUID uuid = UUID.randomUUID();
        CarBrandDTO updatedCarBrandDTO = new CarBrandDTO(uuid, "Updated Toyota", "Updated Japan", 1938, null, null);
        when(carBrandService.update(eq(uuid), any(CarBrandDTO.class))).thenReturn(updatedCarBrandDTO);

        mockMvc.perform(put("/api/v1/car-brands/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCarBrandDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Updated Toyota"));
    }

    @Test
    void deleteCarBrandByUuid_ShouldReturnNoContent() throws Exception {
        UUID uuid = UUID.randomUUID();
        doNothing().when(carBrandService).deleteByUuid(uuid);

        mockMvc.perform(delete("/api/v1/car-brands/" + uuid))
                .andExpect(status().isNoContent());

        verify(carBrandService, times(1)).deleteByUuid(uuid);
    }
}