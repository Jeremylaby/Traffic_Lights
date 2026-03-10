import type {TrafficLightMode} from "@/types/simulation.ts";


export interface ModeOption {
    id: TrafficLightMode;
    label: string;
    tag: string;
    description: string;
    risk: 'low' | 'medium' | 'high';
}

export const RISK_COLOR: Record<ModeOption['risk'], string> = {
    low: '#4ADE80',
    medium: '#FACC15',
    high: '#F87171',
};

export const MODES: ModeOption[] = [
    {
        id: 'SIMPLE',
        label: 'Simple',
        tag: 'Round Robin',
        description: 'Fixed-time controller with constant green and transition durations. Does not inspect queue lengths — predictable and simple, but unresponsive to changing traffic conditions.',
        risk: 'low',
    },
    {
        id: 'BROKEN',
        label: 'Broken',
        tag: 'Fault Mode',
        description: 'All approaches stay green simultaneously. Light control provides no separation — correctness falls entirely on the vehicle release policy, which resolves conflicts and selects a safe subset per step.',
        risk: 'high',
    },
    {
        id: 'ADAPTIVE',
        label: 'Adaptive',
        tag: 'Dynamic Priority',
        description: 'Keeps one axis active but may switch early when the active axis empties or the passive axis holds a clearly larger queue. Enforces min/max green bounds to stay responsive without oscillating.',
        risk: 'low',
    },
    {
        id: 'PROPORTIONAL',
        label: 'Proportional',
        tag: 'Weighted Round Robin',
        description: 'Computes planned green durations proportionally to relative queue sizes, then refreshes that plan after each full cycle. Less reactive than Adaptive, but more stable and easier to reason about over longer runs.',
        risk: 'medium',
    },
    {
        id: 'FEEDBACK',
        label: 'Feedback',
        tag: 'Pressure Control',
        description: 'Maintains internal pressure values per axis with decay on older observations and a service penalty on the active side. The passive axis takes over when its accumulated pressure sufficiently exceeds the active one.',
        risk: 'medium',
    },
];