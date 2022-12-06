const LORAWAN_RSSI_MIN = -150
const LORAWAN_RSSI_MAX = 0

const DEFAULT_RSSI_MIN = LORAWAN_RSSI_MIN
const DEFAULT_RSSI_MAX = LORAWAN_RSSI_MAX

function normalize(value: number, min: number,  max: number): number{
    return (value - min) / (max - min)
}

let closureNormalize: (metric: string, min: number, max: number) => (value: number) => number;

function closureNormalizeLoraWan(metric: string, min: number, max: number){
    return (value: number): number => {
        switch (metric){
            case "RSSI" || "rssi":
                return normalize(value, min, max)
            default:
                return normalize(value, min, max)
        }
    }
}

type normalizeType = ReturnType<typeof closureNormalize>

function getNormalizer(lpwan: string, metric: string): normalizeType {
    switch (lpwan){
        case "lorawan":
            return closureNormalizeLoraWan(metric, LORAWAN_RSSI_MIN, LORAWAN_RSSI_MAX)
        default:
            return closureNormalizeLoraWan(metric, DEFAULT_RSSI_MIN, DEFAULT_RSSI_MAX)
    }
}

const MetricsNormalization = {
    getNormalizer: getNormalizer
}

export default MetricsNormalization