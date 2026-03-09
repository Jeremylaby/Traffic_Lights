package pl.jeremy.api.dto.request;

import jakarta.validation.constraints.NotNull;
import pl.jeremy.model.road.RoadDirection;

public record AddVehicleRequest(
        String vehicleId,
        @NotNull(message = "startRoad is required") RoadDirection startRoad,
        @NotNull(message = "endRoad is required") RoadDirection endRoad) {}
