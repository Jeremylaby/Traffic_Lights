import type {SimulationStateResponse} from '../api/types';
import type {VehicleDto} from "@/types/simulation.ts";

export interface StepLogEntry {
    stepNumber: number;
    leftVehicles: VehicleDto[];
}


export type SimulationState = SimulationStateResponse;