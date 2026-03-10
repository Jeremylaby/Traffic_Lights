import { useState } from 'react';
import { Crossroad } from '@/components/crossroad/Crossroad.tsx';

import type {CrossroadDto, RoadDirection} from '@/types/simulation.ts';
import {
    CrossroadBox,
    DataPanel, EmptyNote, LightBadge,
    Main,
    MockHint, MoveBadge,
    Page,
    PageTitle, PanelTitle,
    RoadCard, RoadCardHeader, RoadName, SourceBox,
    SourceCode, SourceTitle, StepBar,
    StepBtn, StepCounter,
    SystemLabel,
    TopBar,
    TopLeft, VehicleArrow, VehicleEnd, VehicleId,
    VehicleRow
} from "./CrossroadPreview.style.ts";
import {CROSSROAD_STEPS} from "./mock.ts";




const ALL_DIRECTIONS: RoadDirection[] = ['NORTH', 'SOUTH', 'EAST', 'WEST'];

const DIR_KEY: Record<RoadDirection, keyof CrossroadDto> = {
    NORTH: 'northRoad',
    SOUTH: 'southRoad',
    EAST:  'eastRoad',
    WEST:  'westRoad',
};


const CrossroadPreview = () => {
    const [stepIndex, setStepIndex] = useState(0);

    const total = CROSSROAD_STEPS.length;
    const mock  = CROSSROAD_STEPS[stepIndex];

    return (
        <Page>
            <TopBar>
                <TopLeft>
                    <SystemLabel>▸ Mock Visualizer</SystemLabel>
                    <PageTitle>Crossroad Preview</PageTitle>
                </TopLeft>

                <StepBar>
                    <StepBtn onClick={() => setStepIndex(0)} disabled={stepIndex === 0}>
                        ⟨⟨ Reset
                    </StepBtn>
                    <StepBtn onClick={() => setStepIndex(i => i - 1)} disabled={stepIndex === 0}>
                        ← Prev
                    </StepBtn>
                    <StepCounter>{stepIndex + 1} / {total}</StepCounter>
                    <StepBtn $primary onClick={() => setStepIndex(i => i + 1)} disabled={stepIndex === total - 1}>
                        Next →
                    </StepBtn>
                </StepBar>
            </TopBar>

            <Main>
                <CrossroadBox>
                    <Crossroad
                        north={mock.northRoad}
                        south={mock.southRoad}
                        east={mock.eastRoad}
                        west={mock.westRoad}
                        broken={stepIndex <2}
                    />
                    <MockHint>step {stepIndex + 1} of {total} — edit CROSSROAD_STEPS in mock.ts</MockHint>
                </CrossroadBox>

                <DataPanel>
                    <PanelTitle>Step {stepIndex + 1} State</PanelTitle>

                    {ALL_DIRECTIONS.map(dir => {
                        const road = mock[DIR_KEY[dir]];
                        return (
                            <RoadCard key={dir}>
                                <RoadCardHeader>
                                    <RoadName>{dir}</RoadName>
                                    <LightBadge $state={road.trafficLightState}>
                                        {road.trafficLightState}
                                    </LightBadge>
                                </RoadCardHeader>

                                {road.waitingVehicles.length === 0 ? (
                                    <EmptyNote>no vehicles</EmptyNote>
                                ) : (
                                    road.waitingVehicles.map(v => (
                                        <VehicleRow key={v.vehicleId}>
                                            <VehicleId>{v.vehicleId}</VehicleId>
                                            <VehicleArrow>→</VehicleArrow>
                                            <VehicleEnd>{v.endRoad}</VehicleEnd>
                                            <MoveBadge>{v.movement}</MoveBadge>
                                        </VehicleRow>
                                    ))
                                )}
                            </RoadCard>
                        );
                    })}

                    <SourceBox>
                        <SourceTitle>Step {stepIndex + 1} JSON</SourceTitle>
                        <SourceCode>{JSON.stringify(mock, null, 2)}</SourceCode>
                    </SourceBox>
                </DataPanel>
            </Main>
        </Page>
    );
};
export default CrossroadPreview;