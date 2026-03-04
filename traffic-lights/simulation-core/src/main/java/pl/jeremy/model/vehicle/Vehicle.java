package pl.jeremy.model.vehicle;

import pl.jeremy.model.road.RoadDirection;

public class Vehicle {
    private final String id;
    private final RoadDirection startRoad;
    private final RoadDirection endRoad;

    public Vehicle(String id, RoadDirection startRoad, RoadDirection endingRoadDirection) {
        this.id = id;
        this.startRoad = startRoad;
        this.endRoad = endingRoadDirection;
    }
}
