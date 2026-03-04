package pl.jeremy.model.vehicle;

import pl.jeremy.model.road.RoadDirection;

public record VehicleRoute(RoadDirection startRoad, RoadDirection endRoad) {}
