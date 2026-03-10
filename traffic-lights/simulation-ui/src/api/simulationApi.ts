import type {
    AddVehicleRequest,
    CreateSimulationRequest,
    CreateSimulationResponse,
    SimulationStateResponse,
    StepResponse,
    ApiError,
} from './types';
import type {TrafficLightMode} from "../types/simulation.ts";

const BASE_URL = import.meta.env.VITE_API_URL ;

async function request<T>(
    path: string,
    options: RequestInit = {},
): Promise<T> {
    const response = await fetch(`${BASE_URL}${path}`, {
        headers: {
            'Content-Type': 'application/json',
            ...options.headers,
        },
        ...options,
    });

    if (!response.ok) {
        const error = await response.json().catch(() => ({ detail: response.statusText }));
        throw { status: response.status, message: error.detail ?? 'Unknown error', body: error } satisfies ApiError;
    }

    const text = await response.text();
    return text ? (JSON.parse(text) as T) : (undefined as T);
}

export function isApiError(e: unknown): e is ApiError {
    return typeof e === 'object' && e !== null && 'status' in e;
}


export const simulationApi = {
    getModes(): Promise<TrafficLightMode[]> {
        return request<TrafficLightMode[]>('/api/modes');
    },
    createSimulation(strategy: TrafficLightMode): Promise<CreateSimulationResponse> {
        const body: CreateSimulationRequest = { strategy };
        return request<CreateSimulationResponse>('/api/simulations', {
            method: 'POST',
            body: JSON.stringify(body),
        });
    },
    getState(simulationId: string): Promise<SimulationStateResponse> {
        return request<SimulationStateResponse>(`/api/simulations/${simulationId}`);
    },
    step(simulationId: string): Promise<StepResponse> {
        return request<StepResponse>(`/api/simulations/${simulationId}/step`, {
            method: 'POST',
        });
    },
    addVehicle(
        simulationId: string,
        vehicle: AddVehicleRequest,
    ): Promise<SimulationStateResponse> {
        return request<SimulationStateResponse>(`/api/simulations/${simulationId}/vehicles`, {
            method: 'POST',
            body: JSON.stringify(vehicle),
        });
    },
    deleteSimulation(simulationId: string): Promise<void> {
        return request<void>(`/api/simulations/${simulationId}`, {
            method: 'DELETE',
        });
    },
};