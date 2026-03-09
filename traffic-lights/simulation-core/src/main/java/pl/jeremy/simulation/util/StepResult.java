package pl.jeremy.simulation.util;

import java.util.List;
import pl.jeremy.model.vehicle.VehicleDto;

public record StepResult(int stepNumber, List<VehicleDto> leftVehicles, SimulationSnapshot snapshot) {}
