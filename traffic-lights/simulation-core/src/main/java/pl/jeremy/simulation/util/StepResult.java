package pl.jeremy.simulation.util;

import java.util.List;

public record StepResult(int stepNumber, List<String> leftVehicleIds, SimulationSnapshot snapshot) {}
