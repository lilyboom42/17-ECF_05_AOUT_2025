package org.example.environement.service;

import org.example.environement.dto.travellogs.TravellogDtoResponse;
import org.example.environement.dto.travellogs.TravellogDtoStat;
import org.example.environement.entity.TravelLog;
import org.example.environement.repository.TravelLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TravellogsService {

    private final TravelLogRepository travelLogRepository;

    public TravellogsService(TravelLogRepository travelLogRepository) {
        this.travelLogRepository = travelLogRepository;
    }

    public TravellogDtoStat getStat(long id) {
        TravelLog log = (TravelLog) travelLogRepository.findById(id).orElseThrow(() -> new RuntimeException("Trajet non trouvé pour l'id : " + id));
        return new TravellogDtoStat(
                log.getDistanceKm(),
                log.getEstimatedCo2Kg(),
                log.getMode()
        );
    }

    public List<TravellogDtoResponse> get(int observationId) {
        return travelLogRepository.findByObservation_Id(observationId).stream().map(TravelLog::entityToDto).collect(Collectors.toList());
    }

    public Map<String, TravellogDtoStat> getStatForUserLastMonth(String name) {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusMonths(1).withDayOfMonth(1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<TravelLog> logs = travelLogRepository.findByObserverNameAndDateBetween(name, start, end);

        return logs.stream()
                .collect(Collectors.groupingBy(
                        log -> log.getMode().name(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    double totalDistance = list.stream().mapToDouble(TravelLog::getDistanceKm).sum();
                                    double totalCo2 = list.stream().mapToDouble(TravelLog::getEstimatedCo2Kg).sum();
                                    return new TravellogDtoStat(totalDistance, totalCo2, list.get(0).getMode());
                                }
                        )
                ));
    }
}
