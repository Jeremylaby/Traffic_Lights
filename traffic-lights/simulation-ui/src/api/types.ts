import type {RoadDirection, SimulationSnapshot, StepResult, TrafficLightMode} from "../types/simulation.ts";

export interface StepResponse {
    simulationId: string;
    strategy: TrafficLightMode;
    stepResult: StepResult;
}

export interface CreateSimulationRequest {
    strategy: TrafficLightMode;
}

export interface AddVehicleRequest {
    vehicleId?: string;
    startRoad: RoadDirection;
    endRoad: RoadDirection;
}

export interface CreateSimulationResponse {
    id: string;
    strategy: TrafficLightMode;
    snapshot: SimulationSnapshot;
}

export interface SimulationStateResponse {
    simulationId: string;
    strategy: TrafficLightMode;
    snapshot: SimulationSnapshot;
}


export type ApiError = {
    status: number;
    message: string;
    body?: ApiErrorBody;
};

type ApiErrorBody = {
    status: number;
    detail?: string
};