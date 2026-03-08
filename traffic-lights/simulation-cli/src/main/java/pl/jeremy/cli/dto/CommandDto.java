package pl.jeremy.cli.dto;

import pl.jeremy.model.road.RoadDirection;

public record CommandDto(String type, String vehicleId, RoadDirection startRoad, RoadDirection endRoad) {}
