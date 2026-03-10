import type {TrafficLightState} from "@/types/simulation.ts";
import TrafficLight from "@/components/crossroad/TrafficLight/TrafficLight.tsx";
import {VehicleQueue} from "@/components/crossroad/VehicleQueue/VehicleQueue.tsx";
import {Cell, Label, Page, Row, Title} from "./PreviewPage.style.ts";

const STATES: TrafficLightState[] = [
    'RED',
    'RED_YELLOW',
    'GREEN_ARROW',
    'GREEN',
    'YELLOW',
];


const PreviewPage = () => {
    return (
        <Page>
            <Title>Component Preview — TrafficLight</Title>

            <Row>
                {STATES.map(state => (
                    <Cell key={state}>
                        <svg width={24} height={72} overflow="visible">
                            <TrafficLight state={state}/>
                        </svg>
                        <Label>{state}</Label>
                    </Cell>
                ))}
            </Row>

            <Title>Rotations — GREEN</Title>
            <Row>
                {[0, 90, 180, 270].map(rotation => (
                    <Cell key={rotation}>
                        <svg width={80} height={80} overflow="visible">
                            <g transform="translate(40,40)">
                                <TrafficLight state="GREEN" rotation={rotation}/>
                            </g>
                        </svg>
                        <Label>{rotation}°</Label>
                    </Cell>
                ))}
            </Row>
            <Title>Component Preview — TrafficLight Broken</Title>

            <Row>
                {STATES.map(state => (
                    <Cell key={state}>
                        <svg width={24} height={72} overflow="visible">
                            <TrafficLight state={state} broken={true}/>
                        </svg>
                        <Label>{state}</Label>
                    </Cell>
                ))}
            </Row>
            <Row>
                <svg width={200} height={200} overflow="visible">
                    <VehicleQueue
                        vehicles={[
                            {vehicleId: 'v1', startRoad: 'SOUTH', endRoad: 'NORTH', movement: 'STRAIGHT'},
                            {vehicleId: 'v2', startRoad: 'SOUTH', endRoad: 'EAST', movement: 'LEFT'},
                            {vehicleId: 'v3', startRoad: 'SOUTH', endRoad: 'WEST', movement: 'RIGHT'},
                            {vehicleId: 'v4', startRoad: 'SOUTH', endRoad: 'NORTH', movement: 'STRAIGHT'},
                            {vehicleId: 'v5', startRoad: 'SOUTH', endRoad: 'NORTH', movement: 'STRAIGHT'},
                            {vehicleId: 'v6', startRoad: 'SOUTH', endRoad: 'NORTH', movement: 'STRAIGHT'},
                        ]}
                    />
                </svg>
            </Row>
        </Page>
    );
};
export default PreviewPage;