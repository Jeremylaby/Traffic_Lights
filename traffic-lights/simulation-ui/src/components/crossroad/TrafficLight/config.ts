import type {TrafficLightState} from "@/types/simulation.ts";

export const DIM = '#2a2a2a';
const RED_COLOR = '#ff2d2d'
const YELLOW_COLOR = '#ffd700';
const GREEN_COLOR = '#4ade80';
export const LIGHT_CONFIG: Record<
    TrafficLightState,
    { red: string; yellow: string; green: string; arrowActive: boolean }
> = {
    RED:         { red: RED_COLOR, yellow: DIM,       green: DIM,       arrowActive: false },
    RED_YELLOW:  { red: RED_COLOR, yellow:YELLOW_COLOR, green: DIM,       arrowActive: false },
    GREEN_ARROW: { red: RED_COLOR, yellow: DIM,       green: DIM,       arrowActive: true  },
    GREEN:       { red: DIM,       yellow: DIM,       green: GREEN_COLOR, arrowActive: false },
    YELLOW:      { red: DIM,       yellow: YELLOW_COLOR, green: DIM,       arrowActive: false },
};

export const GREEN_ARROW_SIZE = 20