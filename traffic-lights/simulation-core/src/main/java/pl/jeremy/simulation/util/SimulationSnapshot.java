package pl.jeremy.simulation.util;

import pl.jeremy.model.crossroad.PolishCrossroad;
import pl.jeremy.model.road.RoadDirection;

public record SimulationSnapshot(
        int northQueue,
        int southQueue,
        int eastQueue,
        int westQueue,
        String northLight,
        String southLight,
        String eastLight,
        String westLight) {
    public static SimulationSnapshot from(PolishCrossroad crossroad) {
        return new SimulationSnapshot(
                crossroad.getRoad(RoadDirection.NORTH).getEntanceLane().getVehicleCount(),
                crossroad.getRoad(RoadDirection.SOUTH).getEntanceLane().getVehicleCount(),
                crossroad.getRoad(RoadDirection.EAST).getEntanceLane().getVehicleCount(),
                crossroad.getRoad(RoadDirection.WEST).getEntanceLane().getVehicleCount(),
                crossroad
                        .getRoad(RoadDirection.NORTH)
                        .getTrafficLight()
                        .getState()
                        .name(),
                crossroad
                        .getRoad(RoadDirection.SOUTH)
                        .getTrafficLight()
                        .getState()
                        .name(),
                crossroad
                        .getRoad(RoadDirection.EAST)
                        .getTrafficLight()
                        .getState()
                        .name(),
                crossroad
                        .getRoad(RoadDirection.WEST)
                        .getTrafficLight()
                        .getState()
                        .name());
    }
}
