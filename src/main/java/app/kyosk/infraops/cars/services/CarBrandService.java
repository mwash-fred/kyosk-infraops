package app.kyosk.infraops.cars.services;

import app.kyosk.infraops.cars.dto.CarBrandDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarBrandService {

    CarBrandDTO save(CarBrandDTO carBrandDTO);

    List<CarBrandDTO> findAll();

    Page<CarBrandDTO> findAll(Pageable pageable);

    Optional<CarBrandDTO> findByUuid(UUID uuid);

    CarBrandDTO update(UUID id, CarBrandDTO updatedCarBrandDTO);

    void deleteByUuid(UUID uuid);
}
