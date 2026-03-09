package pl.jeremy.cli.io;

import pl.jeremy.model.vehicle.Vehicle;
import pl.jeremy.simulation.util.SimulationSnapshot;
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
        SimulationSnapshot s = stepResult.snapshot();

        return """
               STEP %d
               Lights: N=%s S=%s E=%s W=%s
               Queues: N=%d S=%d E=%d W=%d
               Left: %s
               """.formatted(
                        stepResult.stepNumber(),
                        s.northLight(),
                        s.southLight(),
                        s.eastLight(),
                        s.westLight(),
                        s.northQueue(),
                        s.southQueue(),
                        s.eastQueue(),
                        s.westQueue(),
                        stepResult.leftVehicles());
    }
}
