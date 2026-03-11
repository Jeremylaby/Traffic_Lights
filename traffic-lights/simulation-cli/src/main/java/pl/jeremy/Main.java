package pl.jeremy;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import pl.jeremy.cli.dto.CommandDto;
import pl.jeremy.cli.dto.SimulationInputDto;
import pl.jeremy.cli.io.JsonInputReader;
import pl.jeremy.cli.io.JsonOutputWriter;
import pl.jeremy.cli.io.SimulationStepLogger;
import pl.jeremy.model.vehicle.Vehicle;
import pl.jeremy.simulation.CrossroadSimulation;
import pl.jeremy.simulation.factory.SimulationStrategyFactory;
import pl.jeremy.simulation.factory.TrafficLightMode;
import pl.jeremy.simulation.util.StepResult;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 2 || args.length > 3) {
            throw new IllegalArgumentException("Usage: <input.json> <output.json> [SIMPLE|BROKEN]");
        }

        Path inputPath = Path.of(args[0]);
        Path outputPath = Path.of(args[1]);
        TrafficLightMode mode = args.length == 3 ? TrafficLightMode.from(args[2]) : TrafficLightMode.SIMPLE;
        SimulationStepLogger stepLogger = new SimulationStepLogger();
        JsonInputReader inputReader = new JsonInputReader();
        JsonOutputWriter outputWriter = new JsonOutputWriter();

        SimulationInputDto input = inputReader.read(inputPath);
        System.out.println("Running simulation with mode: " + mode);
        CrossroadSimulation simulation = SimulationStrategyFactory.create(mode);
        List<StepResult> stepResults = getStepResults(simulation, input, stepLogger);

        outputWriter.write(outputPath, stepResults);
    }

    private static List<StepResult> getStepResults(
            CrossroadSimulation simulation, SimulationInputDto input, SimulationStepLogger stepLogger) {

        List<StepResult> stepResults = new ArrayList<>();

        for (CommandDto command : input.commands()) {
            switch (command.type()) {
                case "addVehicle" -> {
                    Vehicle vehicle = new Vehicle(command.vehicleId(), command.startRoad(), command.endRoad());
                    stepLogger.logAddVehicle(vehicle);
                    simulation.addVehicle(vehicle);
                }
                case "step" -> {
                    StepResult stepResult = simulation.step();
                    stepResults.add(stepResult);
                    stepLogger.logStep(stepResult);
                }
                default -> throw new IllegalArgumentException("Unknown command type: " + command.type());
            }
        }
        return stepResults;
    }
}
