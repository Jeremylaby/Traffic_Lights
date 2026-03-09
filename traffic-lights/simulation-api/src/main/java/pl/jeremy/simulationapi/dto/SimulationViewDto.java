package pl.jeremy.simulationapi.dto;

import pl.jeremy.model.crossroad.CrossroadDto;

public record SimulationViewDto(
        String simulationId,
        int stepNumber,
        String strategy,
        CrossroadDto crossroad
) {}
