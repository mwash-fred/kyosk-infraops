package app.kyosk.infraops.cars.repository;

import app.kyosk.infraops.cars.entity.CarBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CarBrandRepository extends JpaRepository<CarBrand, Long> {
    void deleteByUuid(UUID uuid);

    Optional<CarBrand> findByUuid(UUID uuid);
}
