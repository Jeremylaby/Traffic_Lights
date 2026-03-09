package pl.jeremy.simulationapi.dto;

public record CreateSimulationResponse(
        String id
        SimulationViewDto snapshot
) {
}
