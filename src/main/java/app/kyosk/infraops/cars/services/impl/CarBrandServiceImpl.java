package app.kyosk.infraops.cars.services.impl;

import app.kyosk.infraops.cars.dto.CarBrandDTO;
import app.kyosk.infraops.cars.entity.CarBrand;
import app.kyosk.infraops.cars.repository.CarBrandRepository;
import app.kyosk.infraops.cars.services.CarBrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarBrandServiceImpl implements CarBrandService {

    private final CarBrandRepository carBrandRepository;

    @Override
    public CarBrandDTO save(CarBrandDTO carBrandDTO) {
        CarBrand carBrand = mapToEntity(carBrandDTO);
        carBrand = carBrandRepository.save(carBrand);
        return mapToDTO(carBrand);
    }

    @Override
    public List<CarBrandDTO> findAll() {
        return carBrandRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CarBrandDTO> findAll(Pageable pageable) {
        return carBrandRepository.findAll(pageable).map(this::mapToDTO);
    }

    @Override
    public Optional<CarBrandDTO> findByUuid(UUID uuid) {
        return carBrandRepository.findByUuid(uuid).map(this::mapToDTO);
    }

    @Override
    public CarBrandDTO update(UUID uuid, CarBrandDTO updatedCarBrandDTO) {
        Optional<CarBrand> existingCarBrand = carBrandRepository.findByUuid(uuid);
        if(existingCarBrand.isPresent()) {
            CarBrand carBrand = existingCarBrand.get();
            carBrand.setName(updatedCarBrandDTO.name());
            carBrand.setCountryOfOrigin(updatedCarBrandDTO.countryOfOrigin());
            carBrand.setFoundedYear(updatedCarBrandDTO.foundedYear());
            carBrand = carBrandRepository.save(carBrand);
            return mapToDTO(carBrand);
        }
        throw new IllegalArgumentException("Car brand not found with UUID: " + uuid);
    }

    @Override
    public void deleteByUuid(UUID uuid) {
        carBrandRepository.deleteByUuid(uuid);
    }

    private CarBrand mapToEntity(CarBrandDTO dto) {
        return CarBrand.builder()
                .uuid(dto.uuid())
                .name(dto.name())
                .countryOfOrigin(dto.countryOfOrigin())
                .foundedYear(dto.foundedYear())
                .createdAt(dto.createdAt())
                .updatedAt(dto.updatedAt())
                .build();
    }

    private CarBrandDTO mapToDTO(CarBrand entity) {
        return new CarBrandDTO(
                entity.getUuid(),
                entity.getName(),
                entity.getCountryOfOrigin(),
                entity.getFoundedYear(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
