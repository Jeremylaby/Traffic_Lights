package pl.jeremy.simulation.strategy;

import pl.jeremy.model.crossroad.PolishCrossroad;
import pl.jeremy.simulation.strategy.util.Axis;
import pl.jeremy.simulation.strategy.util.Phase;

public class ProportionalAllocationTrafficLightStrategy extends AbstractAxisTrafficLightStrategy {
    private static final int MIN_GREEN_TIME = 2;
    private static final int MAX_GREEN_TIME = 10;
    private static final int SUBSTATE_TIME = 1;
    private static final int DEFAULT_GREEN_TIME = 5;
    private static final int RECALCULATION_PERIOD_CYCLES = 2;

    private int fullCycles = 0;
    private int nsPlannedGreen = DEFAULT_GREEN_TIME;
    private int ewPlannedGreen = DEFAULT_GREEN_TIME;
    private int currentGreenTime = 0;
    private int currentSubstate = 0;
    private int targetGreenTime = DEFAULT_GREEN_TIME;

    private Axis activeAxis = Axis.NORTH_SOUTH;
    private Phase phase = Phase.GREEN;

    /**
     * Simulates proportional green-time allocation between both axes. Green durations are planned from relative queue
     * sizes and recalculated periodically after full traffic light cycles.
     */
    @Override
    public void configure(PolishCrossroad crossroad) {
        initializeCrossroad(crossroad);
        resetBaseState();

        fullCycles = 0;
        nsPlannedGreen = DEFAULT_GREEN_TIME;
        ewPlannedGreen = DEFAULT_GREEN_TIME;

        recalculateGreenTimes(crossroad);
        updateTargetGreenTime();
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

        if (activeAxis == Axis.NORTH_SOUTH) {
            fullCycles++;

            if (fullCycles >= RECALCULATION_PERIOD_CYCLES) {
                recalculateGreenTimes(crossroad);
                fullCycles = 0;
            }
        }

        updateTargetGreenTime();
    }

    private boolean shouldSwitchAxis(PolishCrossroad crossroad) {
        int activeQueue = queueSize(crossroad, activeAxis);
        int passiveQueue = queueSize(crossroad, activeAxis.opposite());

        if (passiveQueue == 0) {
            return false;
        }

        if (activeQueue == 0) {
            return true;
        }

        return currentGreenTime >= targetGreenTime;
    }

    private void recalculateGreenTimes(PolishCrossroad crossroad) {
        int nsQueue = queueSize(crossroad, Axis.NORTH_SOUTH);
        int ewQueue = queueSize(crossroad, Axis.EAST_WEST);
        int totalQueue = nsQueue + ewQueue;

        if (totalQueue == 0) {
            nsPlannedGreen = DEFAULT_GREEN_TIME;
            ewPlannedGreen = DEFAULT_GREEN_TIME;
            return;
        }
        double nsShare = (double) nsQueue / totalQueue;
        int nsVal = (int) Math.round(MIN_GREEN_TIME + nsShare * (MAX_GREEN_TIME - MIN_GREEN_TIME));
        nsPlannedGreen = nsVal;
        ewPlannedGreen = Math.max(MAX_GREEN_TIME - nsVal, MIN_GREEN_TIME);
    }

    private void updateTargetGreenTime() {
        targetGreenTime = activeAxis == Axis.NORTH_SOUTH ? nsPlannedGreen : ewPlannedGreen;
    }
}
