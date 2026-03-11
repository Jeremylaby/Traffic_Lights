package pl.jeremy.model.vehicle;

import pl.jeremy.model.road.RoadDirection;

public record VehicleDto(String vehicleId, RoadDirection startRoad, RoadDirection endRoad, Movement movement) {
    public static VehicleDto from(Vehicle vehicle) {
        return new VehicleDto(
                vehicle.getId(), vehicle.getStartRoad(), vehicle.getEndRoad(), Movement.from(vehicle.getRoute()));
    }
}
