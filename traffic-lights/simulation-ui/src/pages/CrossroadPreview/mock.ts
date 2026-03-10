import type {CrossroadDto} from '@/types/simulation.ts';


export const CROSSROAD_STEPS: CrossroadDto[] = [
    // Faza 0 — N i S zawracają, E i W zawracają
    {
        northRoad: {
            roadDirection: 'NORTH',
            trafficLightState: 'GREEN',
            queueSize: 1,
            waitingVehicles: [
                {vehicleId: 'u1', startRoad: 'NORTH', endRoad: 'NORTH', movement: 'UTURN'},
            ],
        },
        southRoad: {
            roadDirection: 'SOUTH',
            trafficLightState: 'GREEN',
            queueSize: 1,
            waitingVehicles: [
                {vehicleId: 'u2', startRoad: 'SOUTH', endRoad: 'SOUTH', movement: 'UTURN'},
            ],
        },
        eastRoad: {
            roadDirection: 'EAST',
            trafficLightState: 'GREEN',
            queueSize: 1,
            waitingVehicles: [
                {vehicleId: 'u3', startRoad: 'EAST', endRoad: 'EAST', movement: 'UTURN'},
            ],
        },
        westRoad: {
            roadDirection: 'WEST',
            trafficLightState: 'GREEN',
            queueSize: 1,
            waitingVehicles: [
                {vehicleId: 'u4', startRoad: 'WEST', endRoad: 'WEST', movement: 'UTURN'},
            ],
        },
    },

// Faza 1 — N i S skręcają w lewo, E i W skręcają w prawo
    {
        northRoad: {
            roadDirection: 'NORTH',
            trafficLightState: 'GREEN',
            queueSize: 1,
            waitingVehicles: [
                {vehicleId: 'l1', startRoad: 'NORTH', endRoad: 'EAST', movement: 'LEFT'},
            ],
        },
        southRoad: {
            roadDirection: 'SOUTH',
            trafficLightState: 'GREEN',
            queueSize: 1,
            waitingVehicles: [
                {vehicleId: 'l2', startRoad: 'SOUTH', endRoad: 'WEST', movement: 'LEFT'},
            ],
        },
        eastRoad: {
            roadDirection: 'EAST',
            trafficLightState: 'GREEN',
            queueSize: 1,
            waitingVehicles: [
                {vehicleId: 'r1', startRoad: 'EAST', endRoad: 'NORTH', movement: 'RIGHT'},
            ],
        },
        westRoad: {
            roadDirection: 'WEST',
            trafficLightState: 'GREEN',
            queueSize: 1,
            waitingVehicles: [
                {vehicleId: 'r2', startRoad: 'WEST', endRoad: 'SOUTH', movement: 'RIGHT'},
            ],
        },
    },
    {
        northRoad: {
            roadDirection: 'NORTH',
            trafficLightState: 'GREEN',
            queueSize: 3,
            waitingVehicles: [
                {vehicleId: 'v1', startRoad: 'NORTH', endRoad: 'EAST', movement: 'LEFT'},
                {vehicleId: 'v2', startRoad: 'NORTH', endRoad: 'EAST', movement: 'LEFT'},
                {vehicleId: 'v3', startRoad: 'NORTH', endRoad: 'WEST', movement: 'RIGHT'},
            ],
        },
        southRoad: {
            roadDirection: 'SOUTH',
            trafficLightState: 'GREEN',
            queueSize: 2,
            waitingVehicles: [
                {vehicleId: 'v4', startRoad: 'SOUTH', endRoad: 'NORTH', movement: 'STRAIGHT'},
                {vehicleId: 'v5', startRoad: 'SOUTH', endRoad: 'WEST', movement: 'LEFT'},
            ],
        },
        eastRoad: {
            roadDirection: 'EAST',
            trafficLightState: 'RED',
            queueSize: 2,
            waitingVehicles: [
                {vehicleId: 'v6', startRoad: 'EAST', endRoad: 'WEST', movement: 'STRAIGHT'},
                {vehicleId: 'v7', startRoad: 'EAST', endRoad: 'NORTH', movement: 'RIGHT'},
            ],
        },
        westRoad: {
            roadDirection: 'WEST',
            trafficLightState: 'RED_YELLOW',
            queueSize: 1,
            waitingVehicles: [
                {vehicleId: 'v8', startRoad: 'WEST', endRoad: 'EAST', movement: 'STRAIGHT'},
            ],
        },
    },

    // ── Krok 1: v1, v4 wyjechały (NS green) ──
    {
        northRoad: {
            roadDirection: 'NORTH',
            trafficLightState: 'GREEN',
            queueSize: 2,
            waitingVehicles: [
                {vehicleId: 'v2', startRoad: 'NORTH', endRoad: 'EAST', movement: 'LEFT'},
                {vehicleId: 'v3', startRoad: 'NORTH', endRoad: 'WEST', movement: 'RIGHT'},
            ],
        },
        southRoad: {
            roadDirection: 'SOUTH',
            trafficLightState: 'GREEN',
            queueSize: 1,
            waitingVehicles: [
                {vehicleId: 'v5', startRoad: 'SOUTH', endRoad: 'WEST', movement: 'LEFT'},
            ],
        },
        eastRoad: {
            roadDirection: 'EAST',
            trafficLightState: 'RED',
            queueSize: 2,
            waitingVehicles: [
                {vehicleId: 'v6', startRoad: 'EAST', endRoad: 'WEST', movement: 'STRAIGHT'},
                {vehicleId: 'v7', startRoad: 'EAST', endRoad: 'NORTH', movement: 'RIGHT'},
            ],
        },
        westRoad: {
            roadDirection: 'WEST',
            trafficLightState: 'GREEN_ARROW',
            queueSize: 1,
            waitingVehicles: [
                {vehicleId: 'v8', startRoad: 'WEST', endRoad: 'EAST', movement: 'STRAIGHT'},
            ],
        },
    },

    // ── Krok 2: NS żółte, EW red-yellow (faza przejściowa) ──
    {
        northRoad: {
            roadDirection: 'NORTH',
            trafficLightState: 'YELLOW',
            queueSize: 2,
            waitingVehicles: [
                {vehicleId: 'v2', startRoad: 'NORTH', endRoad: 'EAST', movement: 'LEFT'},
                {vehicleId: 'v3', startRoad: 'NORTH', endRoad: 'WEST', movement: 'RIGHT'},
            ],
        },
        southRoad: {
            roadDirection: 'SOUTH',
            trafficLightState: 'YELLOW',
            queueSize: 1,
            waitingVehicles: [
                {vehicleId: 'v5', startRoad: 'SOUTH', endRoad: 'WEST', movement: 'LEFT'},
            ],
        },
        eastRoad: {
            roadDirection: 'EAST',
            trafficLightState: 'RED_YELLOW',
            queueSize: 2,
            waitingVehicles: [
                {vehicleId: 'v6', startRoad: 'EAST', endRoad: 'WEST', movement: 'STRAIGHT'},
                {vehicleId: 'v7', startRoad: 'EAST', endRoad: 'NORTH', movement: 'RIGHT'},
            ],
        },
        westRoad: {
            roadDirection: 'WEST',
            trafficLightState: 'RED_YELLOW',
            queueSize: 1,
            waitingVehicles: [
                {vehicleId: 'v8', startRoad: 'WEST', endRoad: 'EAST', movement: 'STRAIGHT'},
            ],
        },
    },

    // ── Krok 3: EW zielone, v6 i v8 wyjechały ──
    {
        northRoad: {
            roadDirection: 'NORTH',
            trafficLightState: 'RED',
            queueSize: 2,
            waitingVehicles: [
                {vehicleId: 'v2', startRoad: 'NORTH', endRoad: 'EAST', movement: 'LEFT'},
                {vehicleId: 'v3', startRoad: 'NORTH', endRoad: 'WEST', movement: 'RIGHT'},
            ],
        },
        southRoad: {
            roadDirection: 'SOUTH',
            trafficLightState: 'RED',
            queueSize: 1,
            waitingVehicles: [
                {vehicleId: 'v5', startRoad: 'SOUTH', endRoad: 'WEST', movement: 'LEFT'},
            ],
        },
        eastRoad: {
            roadDirection: 'EAST',
            trafficLightState: 'GREEN',
            queueSize: 1,
            waitingVehicles: [
                {vehicleId: 'v7', startRoad: 'EAST', endRoad: 'NORTH', movement: 'RIGHT'},
            ],
        },
        westRoad: {
            roadDirection: 'WEST',
            trafficLightState: 'GREEN',
            queueSize: 0,
            waitingVehicles: [],
        },
    },

    // ── Krok 4: nowe v9 dołącza od NORTH, EW żółte ──
    {
        northRoad: {
            roadDirection: 'NORTH',
            trafficLightState: 'GREEN_ARROW',
            queueSize: 3,
            waitingVehicles: [
                {vehicleId: 'v2', startRoad: 'NORTH', endRoad: 'EAST', movement: 'LEFT'},
                {vehicleId: 'v3', startRoad: 'NORTH', endRoad: 'WEST', movement: 'RIGHT'},
                {vehicleId: 'v9', startRoad: 'NORTH', endRoad: 'SOUTH', movement: 'STRAIGHT'},
            ],
        },
        southRoad: {
            roadDirection: 'SOUTH',
            trafficLightState: 'GREEN_ARROW',
            queueSize: 1,
            waitingVehicles: [
                {vehicleId: 'v5', startRoad: 'SOUTH', endRoad: 'WEST', movement: 'LEFT'},
            ],
        },
        eastRoad: {
            roadDirection: 'EAST',
            trafficLightState: 'YELLOW',
            queueSize: 1,
            waitingVehicles: [
                {vehicleId: 'v7', startRoad: 'EAST', endRoad: 'NORTH', movement: 'RIGHT'},
            ],
        },
        westRoad: {
            roadDirection: 'WEST',
            trafficLightState: 'YELLOW',
            queueSize: 0,
            waitingVehicles: [],
        },
    },
];