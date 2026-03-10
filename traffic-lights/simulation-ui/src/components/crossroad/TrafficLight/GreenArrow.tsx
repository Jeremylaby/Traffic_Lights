import {DIM, GREEN_ARROW_SIZE} from "./config.ts";

interface GreenArrowProps {
    x: number;
    y: number;
    active: boolean;
}

const GreenArrow = ({ active, x, y, }: GreenArrowProps) => {

    const color = active ? '#4ade80' : DIM;
    const panelW = GREEN_ARROW_SIZE;
    const panelH = GREEN_ARROW_SIZE;
    const cx = x + panelW / 2;
    const cy = y + panelH / 2;

    const shaftX1 = x + 4;
    const shaftX2 = cx + 2;
    const shaftY  = cy;
    const tipX    = x + panelW - 3;

    const headPoints = `${tipX},${cy} ${shaftX2},${cy - 4} ${shaftX2},${cy + 4}`;

    return (
        <g style={{
            filter: active ? `drop-shadow(0 0 4px ${color})` : 'none',
            transition: 'filter 0.3s ease',
        }}>
            <rect
                x={x} y={y} width={panelW} height={panelH} rx={3} ry={3}
                fill="#1a1a1a" stroke="#444" strokeWidth={1}
            />
            <line
                x1={shaftX1}
                y1={shaftY}
                x2={shaftX2}
                y2={shaftY}
                stroke={color}
                strokeWidth={2}
                strokeLinecap="round"
            />
            <polygon points={headPoints} fill={color} />
        </g>
    );
};
export default GreenArrow;