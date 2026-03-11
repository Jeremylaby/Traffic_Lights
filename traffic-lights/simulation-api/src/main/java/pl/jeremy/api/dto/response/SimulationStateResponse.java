package pl.jeremy.api.dto.response;

import pl.jeremy.simulation.SimulationSnapshot;
import pl.jeremy.simulation.factory.TrafficLightMode;

public record SimulationStateResponse(String simulationId, TrafficLightMode strategy, SimulationSnapshot snapshot) {}
