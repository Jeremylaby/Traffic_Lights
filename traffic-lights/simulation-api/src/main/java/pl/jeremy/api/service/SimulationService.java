package pl.jeremy.api.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jeremy.api.dto.request.AddVehicleRequest;
import pl.jeremy.api.dto.response.CreateSimulationResponse;
import pl.jeremy.api.dto.response.SimulationStateResponse;
import pl.jeremy.api.dto.response.StepResponse;
import pl.jeremy.api.service.util.SimulationRegistry;
import pl.jeremy.api.service.util.SimulationSession;
import pl.jeremy.model.vehicle.Vehicle;
import pl.jeremy.simulation.CrossroadSimulation;
import pl.jeremy.simulation.factory.SimulationStrategyFactory;
import pl.jeremy.simulation.factory.TrafficLightMode;
import pl.jeremy.simulation.util.StepResult;

@Service
@RequiredArgsConstructor
public class SimulationService {

    private final SimulationRegistry registry;

    public List<String> getAvailableModes() {
        return Arrays.stream(TrafficLightMode.values()).map(Enum::name).toList();
    }

    public SimulationStateResponse getState(String simulationId) {
        SimulationSession session = registry.getOrThrow(simulationId);
        return toStateResponse(session);
    }

    public CreateSimulationResponse createSimulation(String strategyRaw) {
        TrafficLightMode strategy = TrafficLightMode.from(strategyRaw);

        CrossroadSimulation simulation = SimulationStrategyFactory.create(strategy);
        String simulationId = UUID.randomUUID().toString();

        SimulationSession session = new SimulationSession(simulationId, strategy, simulation);
        registry.save(session);

        return new CreateSimulationResponse(simulationId, strategy, simulation.snapshot());
    }

    public StepResponse step(String simulationId) {
        SimulationSession session = registry.getOrThrow(simulationId);
        CrossroadSimulation simulation = session.simulation();

        StepResult result = simulation.step();

        return new StepResponse(simulationId, session.strategy(), result);
    }

    public SimulationStateResponse addVehicle(String simulationId, AddVehicleRequest request) {
        SimulationSession session = registry.getOrThrow(simulationId);
        CrossroadSimulation simulation = session.simulation();

        String vehicleId = (request.vehicleId() != null && !request.vehicleId().isBlank())
                ? request.vehicleId()
                : UUID.randomUUID().toString();

        simulation.addVehicle(new Vehicle(vehicleId, request.startRoad(), request.endRoad()));

        return toStateResponse(session);
    }

    public void deleteSimulation(String simulationId) {
        registry.getOrThrow(simulationId);
        registry.delete(simulationId);
    }

    private SimulationStateResponse toStateResponse(SimulationSession session) {
        CrossroadSimulation simulation = session.simulation();
        return new SimulationStateResponse(session.simulationId(), session.strategy(), simulation.snapshot());
    }
}
