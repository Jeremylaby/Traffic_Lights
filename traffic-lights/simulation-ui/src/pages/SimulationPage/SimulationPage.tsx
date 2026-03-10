import {useCallback, useEffect, useMemo} from 'react';
import {useLocation, useNavigate} from 'react-router-dom';
import {Crossroad} from '@/components/crossroad/Crossroad.tsx';
import {useSimulation} from '@/hooks/useSimulation.ts';
import type {TrafficLightMode, TrafficLightState} from '@/types/simulation.ts';

import {
    BackBtn,
    CanvasArea,
    CanvasFrame,
    CtrlBtn,
    CtrlSep,
    EmptyState,
    ErrorMsg,
    Footer,
    GlobalStyle,
    GridBg,
    Header,
    HeaderLabel,
    HeaderLeft,
    HeaderMode,
    HeaderTitle,
    LoadingDot,
    LogEntry,
    Main,
    ModeTag,
    QueueBar,
    QueueCount,
    QueueDir,
    QueueFill,
    QueueItem,
    QueueTop,
    Root,
    Sidebar,
    SidebarLabel,
    SidebarSection,
    StepBtn,
    StepCounter,
    StepNum,
    StepOf
} from "./SimulationPage.style.ts";
import AddVehiclePanel from "./components/AddVehiclePanel.tsx";


interface LocationState {
    mode: TrafficLightMode;
}


const LIGHT_LABEL: Record<TrafficLightState, string> = {
    GREEN: 'GRN',
    RED: 'RED',
    YELLOW: 'YEL',
    RED_YELLOW: 'R+Y',
    GREEN_ARROW: 'GRN↗',
};

const pad = (n: number) => String(n).padStart(2, '0');


const SimulationPage = () => {
    const location = useLocation();
    const navigate = useNavigate();


    const mode = useMemo(
        () => (location.state as LocationState)?.mode ?? 'SIMPLE',
        [location.state],
    );
    const broken = mode === 'BROKEN';

    const {
        simulation,
        stepLog,
        loading,
        error,
        isActive,
        createSimulation,
        step,
        deleteSimulation,
        addVehicle,

    } = useSimulation();

    useEffect(() => {
        createSimulation(mode);
    }, [mode, createSimulation]);

    const handleBack = useCallback(async () => {
        await deleteSimulation();
        navigate('/');
    }, [deleteSimulation, navigate]);

    const snapshot = simulation?.snapshot;
    const crossroad = snapshot?.crossroad;
    const stepNumber = snapshot?.stepNumber ?? 0;
    const directions = ['NORTH', 'SOUTH', 'EAST', 'WEST'] as const;

    return (
        <>
            <GlobalStyle/>
            <Root>
                <GridBg/>

                <Header>
                    <HeaderLeft>
                        <BackBtn onClick={handleBack}>← Back</BackBtn>
                        <HeaderTitle>
                            <HeaderLabel>Polish Traffic Simulation</HeaderLabel>
                            <HeaderMode>Intersection Control</HeaderMode>
                        </HeaderTitle>
                        <ModeTag>{mode}</ModeTag>
                        {error && <ErrorMsg>⚠ {error}</ErrorMsg>}
                    </HeaderLeft>

                    <StepCounter>
                        <StepNum>{pad(stepNumber)}</StepNum>
                        <StepOf>steps</StepOf>
                    </StepCounter>
                </Header>

                {/* Main */}
                <Main>

                    {/* Sidebar */}
                    <Sidebar>
                        <AddVehiclePanel addVehicle={addVehicle} isActive={isActive} loading={loading}/>

                        <SidebarSection>
                            <SidebarLabel>Queue Status</SidebarLabel>
                            {crossroad
                                ? directions.map(dir => {
                                    const road = crossroad[`${dir.toLowerCase()}Road` as keyof typeof crossroad];
                                    const light = road.trafficLightState;
                                    const count = road.queueSize;
                                    const pct = Math.min(count / 5, 1) * 100;
                                    const high = count > 3;

                                    return (
                                        <QueueItem key={dir}>
                                            <QueueTop>
                                                <QueueDir>{dir}</QueueDir>
                                                {LIGHT_LABEL[light]}
                                            </QueueTop>
                                            <QueueBar>
                                                <QueueFill $pct={pct} $high={high}/>
                                            </QueueBar>
                                            <QueueCount $high={high}>
                                                {count} vehicle{count !== 1 ? 's' : ''}
                                            </QueueCount>
                                        </QueueItem>
                                    );
                                })
                                : <QueueDir style={{color: '#2A2A38', fontSize: '11px'}}>
                                    Initializing...
                                </QueueDir>
                            }
                        </SidebarSection>


                        {stepLog.length > 0 && (
                            <SidebarSection>
                                <SidebarLabel>Released</SidebarLabel>
                                {[...stepLog].reverse().slice(0, 6).map(entry => (
                                    <LogEntry key={entry.stepNumber}>
                                        <span>#{pad(entry.stepNumber)}</span>{' '}
                                        {entry.leftVehicles.length > 0
                                            ? entry.leftVehicles.map(v => v.vehicleId).join(', ')
                                            : '—'
                                        }
                                    </LogEntry>
                                ))}
                            </SidebarSection>
                        )}
                    </Sidebar>

                    {/* Canvas */}
                    <CanvasArea>
                        {crossroad ? (
                            <CanvasFrame>
                                <Crossroad
                                    north={crossroad.northRoad}
                                    south={crossroad.southRoad}
                                    east={crossroad.eastRoad}
                                    west={crossroad.westRoad}
                                    broken={broken}
                                />
                            </CanvasFrame>
                        ) : (
                            <EmptyState>
                                {loading
                                    ? <><LoadingDot>◆</LoadingDot> Connecting to simulation...</>
                                    : '— No active simulation —'
                                }
                            </EmptyState>
                        )}
                    </CanvasArea>

                </Main>

                {/* Footer */}
                <Footer>
                    <CtrlBtn
                        onClick={() => createSimulation(mode)}
                        $disabled={loading}
                        disabled={loading}
                        title="Restart simulation"
                    >
                        ↺
                    </CtrlBtn>

                    <CtrlSep/>

                    <StepBtn
                        onClick={step}
                        $disabled={!isActive || loading}
                        disabled={!isActive || loading}
                    >
                        {loading ? '...' : 'Next Step →'}
                    </StepBtn>
                </Footer>

            </Root>
        </>
    );
};

export default SimulationPage;