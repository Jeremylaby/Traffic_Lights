package pl.jeremy.simulation;

import java.util.List;
import pl.jeremy.model.crossroad.PolishCrossroad;
import pl.jeremy.model.vehicle.Vehicle;

public interface VehicleReleasePolicy {
    List<Vehicle> selectVehiclesToRelease(PolishCrossroad crossroad);
}
