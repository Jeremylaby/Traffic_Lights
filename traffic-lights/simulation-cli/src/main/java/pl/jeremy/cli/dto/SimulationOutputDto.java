package pl.jeremy.cli.dto;

import java.util.List;

public record SimulationOutputDto(List<StepStatusDto> stepStatuses) {}
