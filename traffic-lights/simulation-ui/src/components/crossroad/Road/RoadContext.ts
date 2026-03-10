import {createContext, useContext} from 'react';
import type {RoadDirection} from '@/types/simulation.ts';

interface RoadContextValue {
    direction: RoadDirection;
    roadRotation: number;
}

const RoadContext = createContext<RoadContextValue>({
    direction: 'NORTH',
    roadRotation: 0,
});

export const useRoad = () => useContext(RoadContext);

export default RoadContext;