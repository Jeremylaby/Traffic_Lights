package pl.jeremy.api.service.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import pl.jeremy.api.exceptions.SimulationNotFoundException;

@Component
public class SimulationRegistry {

    private final Map<String, SimulationSession> sessions = new ConcurrentHashMap<>();

    public void save(SimulationSession session) {
        sessions.put(session.simulationId(), session);
    }

    public SimulationSession getOrThrow(String simulationId) {
        SimulationSession session = sessions.get(simulationId);
        if (session == null) {
            throw new SimulationNotFoundException(simulationId);
        }
        return session;
    }

    public void delete(String simulationId) {
        sessions.remove(simulationId);
    }
}
