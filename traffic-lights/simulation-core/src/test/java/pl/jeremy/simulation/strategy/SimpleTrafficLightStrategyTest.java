package pl.jeremy.simulation.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import pl.jeremy.model.crossroad.PolishCrossroad;
import pl.jeremy.model.road.RoadDirection;
import pl.jeremy.model.trafficlights.TrafficLightState;

class SimpleTrafficLightStrategyTest {

    @Test
    void configureSetsInitialStatesNSGreenEWRed() {
        PolishCrossroad crossroad = new PolishCrossroad();
        SimpleTrafficLightStrategy strategy = new SimpleTrafficLightStrategy();

        strategy.configure(crossroad);

        // Initial setup should be: North+South = GREEN, East+West = RED
        assertEquals(TrafficLightState.GREEN, state(crossroad, RoadDirection.NORTH));
        assertEquals(TrafficLightState.GREEN, state(crossroad, RoadDirection.SOUTH));
        assertEquals(TrafficLightState.RED, state(crossroad, RoadDirection.EAST));
        assertEquals(TrafficLightState.RED, state(crossroad, RoadDirection.WEST));
    }

    @Test
    void afterOneStepRedBecomesGreenArrowWhileGreenStaysGreen() {
        PolishCrossroad crossroad = new PolishCrossroad();
        SimpleTrafficLightStrategy strategy = new SimpleTrafficLightStrategy();
        strategy.configure(crossroad);

        step(strategy, crossroad);

        // For RED lights: the strategy immediately advances them (RED -> next state).
        // For GREEN lights: they should remain GREEN until currentGreenTime reaches GREEN_TIME.
        assertEquals(TrafficLightState.GREEN_ARROW, state(crossroad, RoadDirection.EAST));
        assertEquals(TrafficLightState.GREEN_ARROW, state(crossroad, RoadDirection.WEST));

        assertEquals(TrafficLightState.GREEN, state(crossroad, RoadDirection.NORTH));
        assertEquals(TrafficLightState.GREEN, state(crossroad, RoadDirection.SOUTH));
    }

    @Test
    void afterSixStepsGreenAndGreenArrowAdvanceToYellowAndRedYellow() {
        PolishCrossroad crossroad = new PolishCrossroad();
        SimpleTrafficLightStrategy strategy = new SimpleTrafficLightStrategy();
        strategy.configure(crossroad);

        // Important detail:
        // currentGreenTime is incremented AFTER changeLights() is applied.
        // With GREEN_TIME=5, the transition happens at the beginning of the 6th step.
        steps(strategy, crossroad, 6);

        // NS: GREEN -> YELLOW
        assertEquals(TrafficLightState.YELLOW, state(crossroad, RoadDirection.NORTH));
        assertEquals(TrafficLightState.YELLOW, state(crossroad, RoadDirection.SOUTH));

        // EW: GREEN_ARROW -> RED_YELLOW
        assertEquals(TrafficLightState.RED_YELLOW, state(crossroad, RoadDirection.EAST));
        assertEquals(TrafficLightState.RED_YELLOW, state(crossroad, RoadDirection.WEST));
    }

    @Test
    void yellowAndRedYellowHoldForOneExtraStepThenAdvanceOnNextDueToSubstateCounter() {
        PolishCrossroad crossroad = new PolishCrossroad();
        SimpleTrafficLightStrategy strategy = new SimpleTrafficLightStrategy();
        strategy.configure(crossroad);

        // After 6 steps we are in: NS=YELLOW, EW=RED_YELLOW
        steps(strategy, crossroad, 6);

        // Step 7:
        // currentSubstate is incremented after changeLights(), so at this point it's not yet >= SUBSTATE_TIME
        // for the checks inside changeLights(). Result: substates do NOT advance here.
        step(strategy, crossroad);
        assertEquals(TrafficLightState.YELLOW, state(crossroad, RoadDirection.NORTH));
        assertEquals(TrafficLightState.YELLOW, state(crossroad, RoadDirection.SOUTH));
        assertEquals(TrafficLightState.RED_YELLOW, state(crossroad, RoadDirection.EAST));
        assertEquals(TrafficLightState.RED_YELLOW, state(crossroad, RoadDirection.WEST));

        // Step 8:
        // Now the internal counter makes currentSubstate >= SUBSTATE_TIME during changeLights(),
        // so substates advance:
        // YELLOW -> RED, RED_YELLOW -> GREEN
        step(strategy, crossroad);
        assertEquals(TrafficLightState.RED, state(crossroad, RoadDirection.NORTH));
        assertEquals(TrafficLightState.RED, state(crossroad, RoadDirection.SOUTH));
        assertEquals(TrafficLightState.GREEN, state(crossroad, RoadDirection.EAST));
        assertEquals(TrafficLightState.GREEN, state(crossroad, RoadDirection.WEST));
    }

    @Test
    void greenArrowAfterRed() {
        PolishCrossroad crossroad = new PolishCrossroad();
        SimpleTrafficLightStrategy strategy = new SimpleTrafficLightStrategy();
        strategy.configure(crossroad);

        // Drive to the state after 8 steps:
        // NS=RED, EW=GREEN (as validated in the previous test)
        steps(strategy, crossroad, 8);

        assertEquals(TrafficLightState.RED, state(crossroad, RoadDirection.NORTH));
        assertEquals(TrafficLightState.RED, state(crossroad, RoadDirection.SOUTH));

        // Next step: any RED light advances immediately (RED -> GREEN_ARROW)
        step(strategy, crossroad);

        assertEquals(TrafficLightState.GREEN_ARROW, state(crossroad, RoadDirection.NORTH));
        assertEquals(TrafficLightState.GREEN_ARROW, state(crossroad, RoadDirection.SOUTH));
    }

    @Test
    void fullCycleReturnsToInitialLightLayout() {
        PolishCrossroad crossroad = new PolishCrossroad();
        SimpleTrafficLightStrategy strategy = new SimpleTrafficLightStrategy();
        strategy.configure(crossroad);

        // Snapshot of the initial light layout right after configure()
        var initial = snapshot(crossroad);

        // Find when (and if) the light layout repeats.
        int stepsToRepeat = -1;
        for (int i = 1; i <= 100; i++) {
            step(strategy, crossroad);
            if (snapshot(crossroad).equals(initial)) {
                stepsToRepeat = i;
                break;
            }
        }

        assertNotEquals(
                -1, stepsToRepeat, "Light layout never returned to the initial configuration within 100 steps.");

        // With GREEN_TIME=5 and SUBSTATE_TIME=1 in this exact implementation,
        // the initial layout repeats after 14 steps.
        assertEquals(
                14,
                stepsToRepeat,
                "Unexpected full cycle length (did you change GREEN_TIME/SUBSTATE_TIME or state policy?).");
    }

    private static java.util.List<TrafficLightState> snapshot(PolishCrossroad crossroad) {
        // Always snapshot directions in a fixed order to make comparisons deterministic.
        return java.util.List.of(
                state(crossroad, RoadDirection.NORTH),
                state(crossroad, RoadDirection.SOUTH),
                state(crossroad, RoadDirection.EAST),
                state(crossroad, RoadDirection.WEST));
    }

    // ===== Helpers =====

    private static TrafficLightState state(PolishCrossroad crossroad, RoadDirection dir) {
        return crossroad.getRoad(dir).getTrafficLight().getState();
    }

    private static void steps(SimpleTrafficLightStrategy strategy, PolishCrossroad crossroad, int n) {
        for (int i = 0; i < n; i++) {
            step(strategy, crossroad);
        }
    }

    /**
     * Helper that works whether your strategy currently has step(crossroad) or you later refactor it to step() (to
     * match the interface).
     */
    private static void step(SimpleTrafficLightStrategy strategy, PolishCrossroad crossroad) {
        try {
            Method m = strategy.getClass().getMethod("step", PolishCrossroad.class);
            m.invoke(strategy, crossroad);
        } catch (NoSuchMethodException e) {
            // If you refactor to step() (no arguments), we still want the tests to work.
            try {
                Method m = strategy.getClass().getMethod("step");
                m.invoke(strategy);
            } catch (Exception ex) {
                throw new RuntimeException("Cannot invoke step() on strategy", ex);
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot invoke step(crossroad) on strategy", e);
        }
    }
}
