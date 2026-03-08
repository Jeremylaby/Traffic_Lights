package pl.jeremy.simulation.strategy;

import pl.jeremy.model.crossroad.PolishCrossroad;
import pl.jeremy.simulation.strategy.util.Phase;

public class AdaptiveTrafficLightStrategy extends AbstractAxisTrafficLightStrategy {
    private static final int MIN_GREEN_TIME = 3;
    private static final int MAX_GREEN_TIME = 8;
    private static final int SUBSTATE_TIME = 1;
    private static final int QUEUE_ADVANTAGE_TO_SWITCH = 2;

    /**
     * Simulates an adaptive traffic light controller. Green time is adjusted to current queue lengths, while keeping
     * minimum and maximum phase durations.
     */
    @Override
    public void configure(PolishCrossroad crossroad) {
        initializeCrossroad(crossroad);
        resetBaseState();
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

    private boolean shouldSwitchAxis(PolishCrossroad crossroad) {
        if (currentGreenTime < MIN_GREEN_TIME) {
            return false;
        }

        int activeQueue = queueSize(crossroad, activeAxis);
        int passiveQueue = queueSize(crossroad, activeAxis.opposite());

        if (passiveQueue == 0) {
            return false;
        }

        if (activeQueue == 0) {
            return true;
        }

        if (currentGreenTime >= MAX_GREEN_TIME) {
            return true;
        }

        return passiveQueue >= activeQueue + QUEUE_ADVANTAGE_TO_SWITCH;
    }
}
