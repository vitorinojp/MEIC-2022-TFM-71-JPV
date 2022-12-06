import MetricsService from "../services/metricsService";
import {Feature} from "ol";
import {Point} from "ol/geom";
import {fromLonLat} from "ol/proj";

const metricsService = MetricsService

function getFromMeasuresByName(measures: Array<any>,  name: string): any {
    return measures.find( item =>{
        return( item?.name === name)
    })
}

async function getList(lpwan: string): Promise<Feature<Point>[]> {
    let empty: Feature<Point>[] = []
    return await metricsService
        .getList(lpwan)
        .then( list =>
            {
                try{
                    if (list == null)
                        return []
                    else
                        return list.map<Feature<Point>>(item => {
                            return new Feature({
                                geometry: new Point(fromLonLat(item?.location?.geometry?.coordinates)), //
                                uid: item?.id,
                                RSSI: getFromMeasuresByName( item?.measures, "rssi")?.value,
                                SIGNAL_RSSI: getFromMeasuresByName( item?.measures, "signal_rssi")?.value,
                                CHANNEL_RSSI: getFromMeasuresByName( item?.measures, "channel_rssi")?.value,
                                RSSI_STANDARD_DEVIATION: getFromMeasuresByName( item?.measures, "rssi_standard_deviation")?.value,
                                SNR: getFromMeasuresByName( item?.measures, "snr")?.value
                            })
                        })
                } catch (e){
                    console.log("Unable to parse receive metrics list, with reason: " + e)
                }

                return empty
            })
}

const MetricsController = {
    getList: getList
}

export default MetricsController