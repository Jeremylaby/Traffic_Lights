import type {TrafficLightState} from '@/types/simulation.ts';
import {Bulb, Housing} from "./TrafficLight.style.ts";
import GreenArrow from "./GreenArrow.tsx";
import {DIM, LIGHT_CONFIG} from "./config.ts";


interface TrafficLightProps {
    state: TrafficLightState;
    rotation?: number;
    broken?: boolean;
}


const TrafficLight = ({state, rotation = 0, broken = false}: TrafficLightProps) => {
    const config = LIGHT_CONFIG[state];

    const cx = 12; // horizontal center
    const r = 7;   // bulb radius
    const redCy = 14;
    const yellowCy = 36;
    const greenCy = 58;

    return (
        <g transform={`rotate(${rotation})`}>
            {/* Housing */}
            <Housing
                x={0}
                y={0}
                width={24}
                height={72}
                rx={4}
                ry={4}
                fill="#1a1a1a"
                stroke="#444"
                strokeWidth={1}
            />
            <Bulb cx={cx} cy={redCy} r={r} $color={broken ? DIM : config.red} $active={config.red == DIM && !broken}/>
            <Bulb cx={cx} cy={yellowCy} r={r} $color={broken ? '#ffd700' : config.yellow}
                  $active={config.yellow == DIM || broken} $blink={broken}/>
            <Bulb cx={cx} cy={greenCy} r={r} $color={broken ? DIM : config.green}
                  $active={config.green == DIM && !broken}/>
            <GreenArrow active={config.arrowActive && !broken} x={28} y={52}/>

        </g>
    );
};
export default TrafficLight;
