package pl.jeremy.api.exceptions;

public class SimulationNotFoundException extends RuntimeException {
    public SimulationNotFoundException(String id) {
        super("Simulation with id " + id + " not found");
    }
}
