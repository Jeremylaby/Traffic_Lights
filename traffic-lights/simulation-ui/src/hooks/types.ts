import type { SimulationStateResponse, VehicleDto } from '../api/types';

export interface StepLogEntry {
    stepNumber: number;
    leftVehicles: VehicleDto[];
}


export type SimulationState = SimulationStateResponse;