import {
    RContextType,
    RControl,
    RFeature,
    RGeolocation,
    RLayerHeatmap,
    RLayerHeatmapProps,
    RLayerStamen,
    RMap
} from "rlayers";
import React, {Component} from "react";
import {Vector} from "ol/source";
import {Heatmap as LayerHeatmap} from "ol/layer";
import {Feature, Geolocation} from "ol";
import {Geometry, Point, Polygon} from "ol/geom";
import {fromLonLat} from "ol/proj";
import MetricsNormalization from "../utils/metricsNormalization";
import BaseEvent from "ol/events/Event";

export interface IoTMapperHeatMapProps {
    feat: Feature<Geometry>[],
    blur: number,
    radius: number,
    metric: string,
    lpwan: string,
    tracking: boolean
}

export interface IoTMapperHeatMapState {
    pos: Point,
    acc: Polygon | null
}

const origin = fromLonLat([0, 0]);
const zoom = 4

export class IoTMapperHeatMap extends Component<IoTMapperHeatMapProps, IoTMapperHeatMapState>{
    constructor(props: IoTMapperHeatMapProps) {
        super(props);
        this.state = {
            ...props,
            pos: new Point(origin),
            acc: null as Polygon | null
        }
    }

    changePos(e: BaseEvent) {
        const geoloc = e.target as Geolocation;
        this.setState({
            pos: new Point(geoloc.getPosition() || origin),
            acc: geoloc.getAccuracyGeometry()
        })

        // Low-level access to the OpenLayers API
        this.context.map.getView().fit(geoloc.getAccuracyGeometry(), {
            duration: 250,
            maxZoom: zoom,
        });
    }

    render() {

        const weight = (f: Feature<Geometry>) => {
            let normalizer = MetricsNormalization.getNormalizer(this.props.lpwan, this.props.metric)
            return normalizer( parseFloat(f.get(this.props.metric)) )
        }

        return (
            <RMap className="heat-map" width={"100%"} height={"92vh"} initial={{center: origin, zoom: zoom}}>
                <RControl.RScaleLine/>
                <RControl.RAttribution/>
                <RControl.RZoom/>
                <RControl.RZoomSlider/>
                <RControl.RFullScreen/>
                <RLayerStamen layer="toner"/>
                <RGeolocation
                    key={String(this.props.tracking)}
                    tracking={this.props.tracking}
                    trackingOptions={{ enableHighAccuracy: true }}
                    onChange={this.changePos}
                />
                <IoTMapperHeatMapper blur={this.props.blur} radius={this.props.radius} weight={weight} >
                    {this.props.feat.map( (f) => {
                        return <RFeature key={f.get("uid") + ":" + this.props.metric} feature={f}/>
                    })}
                </IoTMapperHeatMapper>
            </RMap>
        )
    }

}

class IoTMapperHeatMapper extends RLayerHeatmap {
    constructor(props: Readonly<RLayerHeatmapProps>, context: React.Context<RContextType>) {
        super(props, context)
        this.source = new Vector({
            features: this.props.features,
            url: this.props.url,
            format: this.props.format,
            loader: this.props.loader
        });
        this.ol = new LayerHeatmap({source: this.source, ...props});
        this.eventSources = [this.ol, this.source];
        this.source.on('featuresloadend', this.newFeature);
        this.source.on('addfeature', this.newFeature);
        this.attachEventHandlers();
    }
}