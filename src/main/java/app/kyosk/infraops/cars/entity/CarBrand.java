package app.kyosk.infraops.cars.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "car_brand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="uuid")
    @UuidGenerator
    private UUID uuid;

    @Column(name="name")
    private String name;

    @Column(name="country_of_origin")
    private String countryOfOrigin;

    @Column(name="founded_year")
    private Integer foundedYear;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
