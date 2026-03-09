package pl.jeremy.api.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jeremy.api.dto.request.AddVehicleRequest;
import pl.jeremy.api.dto.request.CreateSimulationRequest;
import pl.jeremy.api.dto.response.CreateSimulationResponse;
import pl.jeremy.api.dto.response.SimulationStateResponse;
import pl.jeremy.api.dto.response.StepResponse;
import pl.jeremy.api.service.SimulationService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    @GetMapping("/modes")
    public ResponseEntity<List<String>> getModes() {
        return ResponseEntity.ok(simulationService.getAvailableModes());
    }

    @PostMapping("/simulations")
    public ResponseEntity<CreateSimulationResponse> createSimulation(
            @RequestBody @Valid CreateSimulationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(simulationService.createSimulation(request.strategy()));
    }

    @GetMapping("/simulations/{simulationId}")
    public ResponseEntity<SimulationStateResponse> getState(@PathVariable String simulationId) {
        return ResponseEntity.ok(simulationService.getState(simulationId));
    }

    @PostMapping("/simulations/{simulationId}/step")
    public ResponseEntity<StepResponse> step(@PathVariable String simulationId) {
        return ResponseEntity.ok(simulationService.step(simulationId));
    }

    @PostMapping("/simulations/{simulationId}/vehicles")
    public ResponseEntity<SimulationStateResponse> addVehicle(
            @PathVariable String simulationId, @RequestBody @Valid AddVehicleRequest request) {
        return ResponseEntity.ok(simulationService.addVehicle(simulationId, request));
    }

    @DeleteMapping("/simulations/{simulationId}")
    public ResponseEntity<?> deleteSimulation(@PathVariable String simulationId) {
        simulationService.deleteSimulation(simulationId);
        return ResponseEntity.ok("Simulation deleted.");
    }
}
