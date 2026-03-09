package pl.jeremy.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateSimulationRequest(
        @NotBlank(message = "strategy is required") String strategy) {}
