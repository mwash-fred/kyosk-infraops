package app.kyosk.infraops.cars.controller;

import app.kyosk.infraops.cars.dto.CarBrandDTO;
import app.kyosk.infraops.cars.dto.PagedResponse;
import app.kyosk.infraops.cars.services.CarBrandService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("api/v1/car-brands")
@RequiredArgsConstructor
public class CarBrandController {
    private final CarBrandService carBrandService;

    @PostMapping
    public ResponseEntity<CarBrandDTO> create(@RequestBody CarBrandDTO carBrandDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carBrandService.save(carBrandDTO));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<CarBrandDTO>> getAllPaged(Pageable pageable) {
        return ResponseEntity.ok(PagedResponse.fromPage(carBrandService.findAll(pageable),  ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString()));
    }

    @GetMapping("{uuid}")
    public ResponseEntity<CarBrandDTO> getByUuid(@PathVariable UUID uuid) {
        return carBrandService.findByUuid(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("{uuid}")
    public ResponseEntity<CarBrandDTO> update(@PathVariable UUID uuid, @RequestBody CarBrandDTO carBrandDTO) {
        return ResponseEntity.ok(carBrandService.update(uuid, carBrandDTO));
    }

    @DeleteMapping("{uuid}")
    public ResponseEntity<Void> deleteByUuid(@PathVariable UUID uuid) {
        carBrandService.deleteByUuid(uuid);
        return ResponseEntity.noContent().build();
    }
}
