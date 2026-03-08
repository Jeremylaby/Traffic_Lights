package pl.jeremy.simulation.strategy;

import pl.jeremy.model.crossroad.PolishCrossroad;
import pl.jeremy.simulation.strategy.util.Axis;
import pl.jeremy.simulation.strategy.util.Phase;

public class FeedbackControlTrafficLightStrategy extends AbstractAxisTrafficLightStrategy {
    private static final int MIN_GREEN_TIME = 3;
    private static final int MAX_GREEN_TIME = 10;
    private static final int SUBSTATE_TIME = 1;

    private static final double DECAY = 0.85;
    private static final double ACTIVE_AXIS_SERVICE_PENALTY = 1.2;
    private static final double PASSIVE_SWITCH_THRESHOLD = 1.5;

    private double nsPressure = 0.0;
    private double ewPressure = 0.0;

    /**
     * Simulates a feedback-based traffic light controller. Axis priority is driven by accumulated traffic pressure,
     * using queue lengths and decaying history of a recent load.
     */
    @Override
    public void configure(PolishCrossroad crossroad) {
        initializeCrossroad(crossroad);
        resetBaseState();
        nsPressure = 0.0;
        ewPressure = 0.0;
    }

    @Override
    public void advanceTrafficLights(PolishCrossroad crossroad) {
        if (phase == Phase.GREEN) {
            advanceGreenPhase(crossroad);
        } else {
            advanceTransitionPhase(crossroad);
        }
    }

    private void advanceGreenPhase(PolishCrossroad crossroad) {
        currentGreenTime++;
        convertRedLightsToGreenArrow(crossroad);
        updatePressure(crossroad);

        if (!shouldSwitchAxis(crossroad)) {
            return;
        }

        advanceAllLights(crossroad);
        phase = Phase.TRANSITION;
        currentSubstate = 0;
    }

    private void advanceTransitionPhase(PolishCrossroad crossroad) {
        currentSubstate++;

        if (currentSubstate < SUBSTATE_TIME) {
            return;
        }

        advanceAllLights(crossroad);
        currentSubstate = 0;

        activeAxis = activeAxis.opposite();
        phase = Phase.GREEN;
        currentGreenTime = 0;
    }

    private void updatePressure(PolishCrossroad crossroad) {
        int nsQueue = queueSize(crossroad, Axis.NORTH_SOUTH);
        int ewQueue = queueSize(crossroad, Axis.EAST_WEST);

        nsPressure = nsPressure * DECAY + nsQueue;
        ewPressure = ewPressure * DECAY + ewQueue;

        if (activeAxis == Axis.NORTH_SOUTH) {
            nsPressure = Math.max(0.0, nsPressure - ACTIVE_AXIS_SERVICE_PENALTY);
        } else {
            ewPressure = Math.max(0.0, ewPressure - ACTIVE_AXIS_SERVICE_PENALTY);
        }
    }

    private boolean shouldSwitchAxis(PolishCrossroad crossroad) {
        Axis passiveAxis = activeAxis.opposite();
        int passiveQueue = queueSize(crossroad, passiveAxis);

        if (passiveQueue == 0) {
            return false;
        }

        if (currentGreenTime < MIN_GREEN_TIME) {
            return false;
        }

        if (currentGreenTime >= MAX_GREEN_TIME) {
            return true;
        }

        double activePressure = pressure(activeAxis);
        double passivePressure = pressure(passiveAxis);

        return passivePressure >= activePressure + PASSIVE_SWITCH_THRESHOLD;
    }

    private double pressure(Axis axis) {
        return axis == Axis.NORTH_SOUTH ? nsPressure : ewPressure;
    }
}
