
import styled from 'styled-components';
import type {TrafficLightState} from "@/types/simulation.ts";
import TrafficLight from "@/components/crossroad/TrafficLight/TrafficLight.tsx";

const STATES: TrafficLightState[] = [
    'RED',
    'RED_YELLOW',
    'GREEN_ARROW',
    'GREEN',
    'YELLOW',
];

const Page = styled.div`
    background: #111;
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 40px;
    gap: 40px;
    font-family: monospace;
    color: #fff;
`;

const Title = styled.h1`
    color: #aaa;
    font-size: 14px;
    letter-spacing: 2px;
    text-transform: uppercase;
    margin: 0;
`;

const Row = styled.div`
    display: flex;
    gap: 40px;
    align-items: flex-end;
`;

const Cell = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 12px;
`;

const Label = styled.span`
    font-size: 11px;
    color: #666;
    text-transform: uppercase;
    letter-spacing: 1px;
`;

const PreviewPage = ()=> {
    return (
        <Page>
            <Title>Component Preview — TrafficLight</Title>

            <Row>
                {STATES.map(state => (
                    <Cell key={state}>
                        <svg width={24} height={72} overflow="visible">
                            <TrafficLight state={state} />
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
                                <TrafficLight state="GREEN" rotation={rotation} />
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
                            <TrafficLight state={state} broken={true} />
                        </svg>
                        <Label>{state}</Label>
                    </Cell>
                ))}
            </Row>
        </Page>
    );
};
export default PreviewPage;