package pl.jeremy.model.road;

import java.util.List;
import pl.jeremy.model.trafficlights.TrafficLightState;
import pl.jeremy.model.vehicle.VehicleDto;

public record RoadDto(
        RoadDirection roadDirection,
        TrafficLightState trafficLightState,
        int queueSize,
        List<VehicleDto> waitingVehicles) {
    public static RoadDto from(SingleLaneRoad road) {

        return new RoadDto(
                road.getRoadDirection(),
                road.getTrafficLight().getState(),
                road.getEntanceLane().getVehicleCount(),
                road.getEntanceLane().getVehicles().stream()
                        .map(VehicleDto::from)
                        .toList());
    }
}
