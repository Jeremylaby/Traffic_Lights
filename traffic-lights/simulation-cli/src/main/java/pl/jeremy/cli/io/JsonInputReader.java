package pl.jeremy.cli.io;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.nio.file.Path;
import pl.jeremy.cli.dto.SimulationInputDto;

public class JsonInputReader {
    private final ObjectMapper objectMapper;

    public JsonInputReader() {
        this.objectMapper = JsonMapper.builder()
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }

    public SimulationInputDto read(Path inputPath) throws IOException {
        return objectMapper.readValue(inputPath.toFile(), SimulationInputDto.class);
    }
}
