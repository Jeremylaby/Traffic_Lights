package pl.jeremy.cli.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import pl.jeremy.cli.dto.SimulationOutputDto;
import pl.jeremy.cli.dto.StepStatusDto;
import pl.jeremy.simulation.util.StepResult;

public class JsonOutputWriter {
    private final ObjectMapper objectMapper;

    public JsonOutputWriter() {
        this.objectMapper = new ObjectMapper();
    }

    private void write(Path outputPath, SimulationOutputDto output) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputPath.toFile(), output);
    }

    public void write(Path outputPath, List<StepResult> stepResults) throws IOException {
        List<StepStatusDto> statuses = stepResults.stream()
                .map(stepResult -> new StepStatusDto(stepResult.leftVehicleIds()))
                .toList();
        write(outputPath, new SimulationOutputDto(statuses));
    }
}
