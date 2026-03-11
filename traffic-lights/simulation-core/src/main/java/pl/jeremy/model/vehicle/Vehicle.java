package pl.jeremy.model.vehicle;

import pl.jeremy.model.road.RoadDirection;

public class Vehicle {
    private final String id;
    private final VehicleRoute route;

    public Vehicle(String id, RoadDirection startRoad, RoadDirection endingRoadDirection) {
        this.id = id;
        this.route = new VehicleRoute(startRoad, endingRoadDirection);
    }

    public String getId() {
        return id;
    }

    public RoadDirection getStartRoad() {
        return route.startRoad();
    }

    public RoadDirection getEndRoad() {
        return route.endRoad();
    }

    public VehicleRoute getRoute() {
        return route;
    }
    // TODO: add some toString
}
