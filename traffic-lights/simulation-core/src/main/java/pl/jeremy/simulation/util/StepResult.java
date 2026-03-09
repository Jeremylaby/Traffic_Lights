package pl.jeremy.simulation.util;

import java.util.List;
import pl.jeremy.model.vehicle.VehicleDto;
import pl.jeremy.simulation.SimulationSnapshot;

public record StepResult(List<VehicleDto> leftVehicles, SimulationSnapshot snapshot) {}
