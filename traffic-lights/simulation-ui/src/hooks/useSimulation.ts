import { useCallback, useState } from 'react';
import { simulationApi } from '../api/simulationApi';
import type {
    AddVehicleRequest,
} from '../api/types';
import { isApiError } from '../api/simulationApi';
import type {SimulationState, StepLogEntry} from "./types.ts";
import type {TrafficLightMode} from "../types/simulation.ts";


export function useSimulation() {
    const [simulation, setSimulation] = useState<SimulationState | null>(null);
    const [stepLog, setStepLog] = useState<StepLogEntry[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const clearError = useCallback(() => setError(null), []);

    const createSimulation = useCallback(async (strategy: TrafficLightMode) => {
        setLoading(true);
        setError(null);
        try {
            const response = await simulationApi.createSimulation(strategy);
            setSimulation({
                simulationId: response.id,
                strategy: response.strategy,
                snapshot: response.snapshot,
            });
            setStepLog([]);
        } catch (e) {
            setError(isApiError(e) ? e.message : 'Failed to create simulation');
        } finally {
            setLoading(false);
        }
    }, []);

    const step = useCallback(async () => {
        if (!simulation) return;
        setLoading(true);
        setError(null);
        try {
            const response = await simulationApi.step(simulation.simulationId);
            const { stepResult } = response;

            setSimulation(prev =>
                prev ? { ...prev, snapshot: stepResult.snapshot } : prev,
            );

            setStepLog(prev => [
                ...prev,
                {
                    stepNumber: stepResult.snapshot.stepNumber,
                    leftVehicles: stepResult.leftVehicles,
                },
            ]);
        } catch (e) {
            setError(isApiError(e) ? e.message : 'Failed to advance step');
        } finally {
            setLoading(false);
        }
    }, [simulation]);

    const addVehicle = useCallback(
        async (vehicle: AddVehicleRequest) => {
            if (!simulation) return;
            setLoading(true);
            setError(null);
            try {
                const response = await simulationApi.addVehicle(simulation.simulationId, vehicle);
                setSimulation(prev =>
                    prev ? { ...prev, snapshot: response.snapshot } : prev,
                );
            } catch (e) {
                setError(isApiError(e) ? e.message : 'Failed to add vehicle');
            } finally {
                setLoading(false);
            }
        },
        [simulation],
    );

    const deleteSimulation = useCallback(async () => {
        if (!simulation) return;
        setLoading(true);
        setError(null);
        try {
            await simulationApi.deleteSimulation(simulation.simulationId);
            setSimulation(null);
            setStepLog([]);
        } catch (e) {
            setError(isApiError(e) ? e.message : 'Failed to delete simulation');
        } finally {
            setLoading(false);
        }
    }, [simulation]);

    const isActive = simulation !== null;

    return {
        simulation,
        stepLog,
        loading,
        error,
        isActive,
        createSimulation,
        step,
        addVehicle,
        deleteSimulation,
        clearError,
    } as const;
}