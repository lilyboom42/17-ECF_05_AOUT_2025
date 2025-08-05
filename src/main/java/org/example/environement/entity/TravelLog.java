package org.example.environement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.environement.dto.travellogs.TravellogDtoResponse;
import org.example.environement.entity.enums.TravelMode;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TravelLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "observation_id")
    private Observation observation;

    @Column(nullable = false)
    private Double distanceKm;

    @Enumerated(EnumType.STRING)
    private TravelMode mode;

    private Double estimatedCo2Kg;

    public void calculateEstimatedCo2() {
        double emissionFactor = switch (mode) {
            case WALKING, BIKE -> 0.0;
            case CAR -> 0.22;
            case BUS -> 0.11;
            case TRAIN -> 0.03;
            case PLANE -> 0.259;
        };
        this.estimatedCo2Kg = this.distanceKm * emissionFactor;
    }

    public TravellogDtoResponse entityToDto() {
        return TravellogDtoResponse.builder()
                .id(this.id)
                .distanceKm(this.distanceKm)
                .mode(this.mode.name())
                .estimatedCo2Kg(this.estimatedCo2Kg)
                .build();
    }

}


