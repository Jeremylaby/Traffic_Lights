import {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import type {TrafficLightMode} from "@/types/simulation.ts";
import {
    Card,
    CardBody,
    CardDesc,
    CardHeader,
    CardName,
    CardTag,
    Cursor,
    Desc,
    Eyebrow,
    GlobalStyle,
    GridBg,
    Left,
    Meta,
    MetaRow,
    RadioDot,
    Right,
    Root,
    RunBtn,
    Scanline,
    SectionLabel,
    Title
} from "./StrategyPage.style.tsx";
import {MODES, RISK_COLOR} from "./config.ts";


const StrategyPage = () => {
    const [selected, setSelected] = useState<TrafficLightMode | null>(null);
    const navigate = useNavigate();

    const handleRun = () => {
        if (!selected) return;
        navigate('/simulation', {state: {mode: selected}});
    };

    const active = MODES.find(m => m.id === selected);

    return (
        <>
            <GlobalStyle/>
            <Root>
                <GridBg/>
                <Scanline/>

                <Left>
                    <div>
                        <Eyebrow>PL / SIM / v1.0</Eyebrow>
                        <Title>
                            Polish<br/>
                            Traffic<br/>
                            <span>Light</span><br/>
                            <p>Simulation</p>
                        </Title>
                        <Desc>
                            A discrete-step simulation of a Polish four-way intersection.
                            Vehicles follow right-of-way law, priority scoring, and
                            axis-alternation logic. Choose a signal control mode to begin.
                            <Cursor/>
                        </Desc>
                    </div>

                    <Meta>
                        <MetaRow>4-way intersection</MetaRow>
                        <MetaRow>Polish right-of-way rules</MetaRow>
                        <MetaRow>5 control modes</MetaRow>
                        <MetaRow>Discrete step simulation</MetaRow>
                    </Meta>
                </Left>

                <Right>
                    <SectionLabel>SELECT CONTROL MODE — {MODES.length} AVAILABLE</SectionLabel>

                    {MODES.map((mode, i) => (
                        <Card
                            key={mode.id}
                            $active={selected === mode.id}
                            $color={RISK_COLOR[mode.risk]}
                            $index={i}
                            onClick={() => setSelected(mode.id)}
                        >
                            <CardBody>
                                <CardHeader>
                                    <CardName $active={selected === mode.id}>
                                        {mode.label}
                                    </CardName>
                                    <CardTag $color={RISK_COLOR[mode.risk]} $active={selected === mode.id}>
                                        {mode.tag}
                                    </CardTag>
                                </CardHeader>
                                <CardDesc $active={selected === mode.id}>
                                    {mode.description}
                                </CardDesc>
                            </CardBody>
                            <RadioDot $active={selected === mode.id} $color={RISK_COLOR[mode.risk]}/>
                        </Card>
                    ))}

                    <RunBtn $enabled={!!selected} onClick={handleRun}>
                        {active ? `Run ${active.label} →` : 'Select a mode'}
                    </RunBtn>
                </Right>
            </Root>
        </>
    );
};

export default StrategyPage;