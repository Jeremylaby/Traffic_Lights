package pl.jeremy.model.trafficlights;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PolishTrafficLightStatePolicyTest {

    private final TrafficLightStatePolicy policy = new PolishTrafficLightStatePolicy();

    @ParameterizedTest
    @MethodSource("transitions")
    void shouldReturnExpectedNextState(TrafficLightState current, TrafficLightState expectedNext) {
        // when
        TrafficLightState next = policy.nextState(current);

        // then
        assertEquals(expectedNext, next);
    }

    private static Stream<Arguments> transitions() {
        return Stream.of(
                Arguments.of(TrafficLightState.RED, TrafficLightState.GREEN_ARROW),
                Arguments.of(TrafficLightState.GREEN_ARROW, TrafficLightState.RED_YELLOW),
                Arguments.of(TrafficLightState.RED_YELLOW, TrafficLightState.GREEN),
                Arguments.of(TrafficLightState.GREEN, TrafficLightState.YELLOW),
                Arguments.of(TrafficLightState.YELLOW, TrafficLightState.RED));
    }

    @Test
    void shouldCycleBackToRedAfterFullProgram() {
        // given
        List<TrafficLightState> expectedSequence = List.of(
                TrafficLightState.RED,
                TrafficLightState.GREEN_ARROW,
                TrafficLightState.RED_YELLOW,
                TrafficLightState.GREEN,
                TrafficLightState.YELLOW,
                TrafficLightState.RED);

        // when + then
        TrafficLightState state = expectedSequence.get(0);
        for (int i = 1; i < expectedSequence.size(); i++) {
            state = policy.nextState(state);
            assertEquals(expectedSequence.get(i), state, "Mismatch at step " + i);
        }
    }

    @Test
    void roadTrafficLightShouldAdvanceUsingPolicy() {
        // given
        RoadTrafficLight light = new RoadTrafficLight(policy, TrafficLightState.RED);

        // when
        light.setNextState();

        // then
        assertEquals(TrafficLightState.GREEN_ARROW, light.getState());
    }
}
