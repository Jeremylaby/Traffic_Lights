import type {RoadDirection} from "@/types/simulation.ts";

export const DIRECTIONS: RoadDirection[] = ['NORTH', 'SOUTH', 'EAST', 'WEST'];

interface Section {
    title: string;
    vehicleCount: number;
}

export const SECTIONS: Section[] = [
    {title: 'Road Preview — 4 pojazdy', vehicleCount: 4},
    {title: 'Road Preview — overflow (6 pojazdów)', vehicleCount: 6},
    {title: 'Road Preview — pusta droga', vehicleCount: 0},
];