export type RoadDirection = 'NORTH' | 'SOUTH' | 'EAST' | 'WEST';

export type TrafficLightState =
    | 'RED'
    | 'RED_YELLOW'
    | 'GREEN_ARROW'
    | 'GREEN'
    | 'YELLOW';

export type Movement = 'LEFT' | 'STRAIGHT' | 'RIGHT' | 'UTURN';

export type TrafficLightMode =
    | 'SIMPLE'
    | 'BROKEN'
    | 'ADAPTIVE'
    | 'PROPORTIONAL'
    | 'FEEDBACK';

export interface VehicleDto {
    vehicleId: string;
    startRoad: RoadDirection;
    endRoad: RoadDirection;
    movement: Movement;
}

export interface RoadDto {
    roadDirection: RoadDirection;
    trafficLightState: TrafficLightState;
    queueSize: number;
    waitingVehicles: VehicleDto[];
}

export interface CrossroadDto {
    northRoad: RoadDto;
    southRoad: RoadDto;
    eastRoad: RoadDto;
    westRoad: RoadDto;
}

export interface SimulationSnapshot {
    stepNumber: number;
    crossroad: CrossroadDto;
}

export interface StepResult {
    leftVehicles: VehicleDto[];
    snapshot: SimulationSnapshot;
}


