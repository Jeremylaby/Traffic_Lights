package pl.jeremy.api.dto.response;

import pl.jeremy.simulation.factory.TrafficLightMode;
import pl.jeremy.simulation.util.StepResult;

public record StepResponse(String simulationId, TrafficLightMode strategy, StepResult stepResult) {}
