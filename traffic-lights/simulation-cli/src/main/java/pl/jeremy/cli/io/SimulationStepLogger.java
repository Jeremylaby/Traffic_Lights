package pl.jeremy.cli.io;

import pl.jeremy.model.crossroad.CrossroadDto;
import pl.jeremy.model.vehicle.Vehicle;
import pl.jeremy.simulation.util.StepResult;

public final class SimulationStepLogger {
    public void logStep(StepResult result) {
        System.out.println(render(result));
        System.out.println("-----------------------------");
    }

    public void logAddVehicle(Vehicle vehicle) {
        System.out.println("New Vehicle added: " + vehicle.getId());
    }

    private String render(StepResult stepResult) {
        CrossroadDto c = stepResult.snapshot().crossroad();

        return """
               STEP %d
               Lights: N=%s S=%s E=%s W=%s
               Queues: N=%d S=%d E=%d W=%d
               Left: %s
               """.formatted(
                        stepResult.snapshot().stepNumber(),
                        c.northRoad().trafficLightState(),
                        c.southRoad().trafficLightState(),
                        c.eastRoad().trafficLightState(),
                        c.westRoad().trafficLightState(),
                        c.northRoad().queueSize(),
                        c.southRoad().queueSize(),
                        c.eastRoad().queueSize(),
                        c.westRoad().queueSize(),
                        stepResult.leftVehicles());
    }
}
