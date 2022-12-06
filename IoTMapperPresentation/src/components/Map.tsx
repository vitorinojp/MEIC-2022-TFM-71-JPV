import React, {useEffect, useState} from "react";

import {Grid} from "semantic-ui-react";

import "ol/ol.css";
import {Feature} from "ol";
import {Geometry} from "ol/geom";

import { MapMenu } from "./MapMenu";
import InfoController from "../controllers/InfoController"
import MetricsController from "../controllers/MetricsController";
import {IoTMapperHeatMap} from "./IoTMapperHeatMap";

let init: Feature<Geometry>[] = []

const Map = (): JSX.Element => {
    const [blur, setBlur] = useState(0);
    const [radius, setRadius] = useState(8);
    const [activeLpwan, setActiveLpwan] = useState(InfoController.getDefaultLpwanSource())
    const [activeMetric, setActiveMetric] = useState(InfoController.getDefaultMetricSource(activeLpwan))
    const [tracking, setTracking] = useState(false);
    const [feat, setFeat] = useState(init);

    useEffect( ()=> {
        MetricsController
            .getList(activeLpwan)
            .then(feature => {
                setFeat([...feature]);
            })
    }, [activeLpwan])

    return (
        <Grid>
            <Grid.Column width={2}>
                <MapMenu
                    getLPWANOptions={InfoController.getLPWANOptions}
                    setLPWANActive={setActiveLpwan}
                    getMetricsOptions={() => {
                        return InfoController.getMetricOptions(activeLpwan)
                    }}
                    setMetricActive={setActiveMetric}
                    lpwan={activeLpwan}
                    metric={activeMetric}
                    setRadius={setRadius}
                    radius={radius}
                    setBlur={setBlur}
                    blur={blur}
                    setTracking={setTracking}
                    tracking={tracking}
                />
            </Grid.Column>
            <Grid.Column width={14}>
                <IoTMapperHeatMap feat={feat} blur={blur} radius={radius} lpwan={activeLpwan} metric={activeMetric} tracking={tracking}/>
            </Grid.Column>
        </Grid>
    )
}

export default Map;