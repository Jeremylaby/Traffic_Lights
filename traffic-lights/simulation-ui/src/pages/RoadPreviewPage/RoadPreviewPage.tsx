import {Road} from '@/components/crossroad/Road/Road.tsx';

import {Cell, Label, Page, Row, Title} from "@/pages/PreviewPage/PreviewPage.style.ts";
import {DIRECTIONS, SECTIONS} from "./config.ts";
import {mockVehicles, roadOrigin, svgSize} from "./util.ts";


const RoadPreviewPage = () => {
    return (
        <Page>
            {SECTIONS.map(({title, vehicleCount}) => (
                <section key={title} style={{display: 'contents'}}>
                    <Title>{title}</Title>
                    <Row>
                        {DIRECTIONS.map(dir => {
                            const {w, h} = svgSize(dir, vehicleCount);
                            const {x, y} = roadOrigin(dir, vehicleCount);
                            const vehicles = mockVehicles(dir, vehicleCount);

                            return (
                                <Cell key={dir}>
                                    <svg
                                        width={w}
                                        height={h}
                                        overflow="visible"
                                        style={{border: '1px solid #1f2937', borderRadius: 4}}
                                    >
                                        <Road
                                            direction={dir}
                                            vehicles={vehicles}
                                            x={x}
                                            y={y}
                                        />
                                    </svg>
                                    <Label>{dir}</Label>
                                </Cell>
                            );
                        })}
                    </Row>
                </section>
            ))}

        </Page>
    );
};

export default RoadPreviewPage;