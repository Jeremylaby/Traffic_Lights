const CAR_COLORS = [
    {fill: '#3b82f6', stroke: '#93c5fd'}, // blue
    {fill: '#ef4444', stroke: '#fca5a5'}, // red
    {fill: '#ef44ab', stroke: '#fca5d1'}, // pink
    {fill: '#22c55e', stroke: '#86efac'}, // green
    {fill: '#f97316', stroke: '#fdba74'}, // orange
];

export function randomCarColor() {
    return CAR_COLORS[Math.floor(Math.random() * CAR_COLORS.length)];
}