package pl.jeremy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import pl.jeremy.cli.dto.CommandDto;
import pl.jeremy.cli.dto.SimulationInputDto;
import pl.jeremy.cli.io.JsonInputReader;
import pl.jeremy.cli.io.JsonOutputWriter;
import pl.jeremy.model.vehicle.Vehicle;
import pl.jeremy.simulation.CrossroadSimulation;
import pl.jeremy.simulation.PolishVehicleReleasePolicy;
import pl.jeremy.simulation.strategy.SimpleTrafficLightStrategy;
import pl.jeremy.simulation.util.StepResult;

public class SimulationE2ECliTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldProcessGivenScenario() throws Exception {
        Path inputPath = Path.of("src/test/resources/input.json");
        Path expectedOutputPath = Path.of("src/test/resources/expected-output.json");
        Path actualOutputPath = Files.createTempFile("simulation-output", ".json");

        JsonInputReader inputReader = new JsonInputReader();
        JsonOutputWriter outputWriter = new JsonOutputWriter();

        SimulationInputDto input = inputReader.read(inputPath);

        CrossroadSimulation simulation =
                new CrossroadSimulation(new SimpleTrafficLightStrategy(), new PolishVehicleReleasePolicy());

        List<StepResult> stepResults = new ArrayList<>();

        for (CommandDto command : input.commands()) {
            switch (command.type()) {
                case "addVehicle" ->
                    simulation.addVehicle(new Vehicle(command.vehicleId(), command.startRoad(), command.endRoad()));
                case "step" -> stepResults.add(simulation.step());
                default -> throw new IllegalArgumentException("Unknown command: " + command.type());
            }
        }

        outputWriter.write(actualOutputPath, stepResults);

        JsonNode expected = objectMapper.readTree(Files.readString(expectedOutputPath));
        JsonNode actual = objectMapper.readTree(Files.readString(actualOutputPath));

        assertEquals(expected, actual);
    }
}
