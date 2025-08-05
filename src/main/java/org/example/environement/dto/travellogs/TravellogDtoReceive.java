package org.example.environement.dto.travellogs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.environement.entity.TravelLog;
import org.example.environement.entity.enums.TravelMode;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TravellogDtoReceive {
    private double distanceKm;
    private String mode;

    public TravelLog dtoToEntity (){
        TravelLog travellog = TravelLog.builder()
                .distanceKm(Double.valueOf(this.getDistanceKm()))
                .mode(TravelMode.valueOf(this.getMode()))
                .build();

        travellog.calculateEstimatedCo2();
        return travellog;
    }
}
