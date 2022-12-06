function getDefaultLpwanSource(): string {
    return "LoRaWan";
}

function getLpwanOptions(): string[] {
    return [
        "LoRaWan",
        "Nb-Iot"
    ]
}

function getDefaultMetricSource(lpwan: string): string {
    return "RSSI"
}

function getMetricOptions(lpwan: string): string[] {
    if(lpwan === "LoRaWan")
        return [
            "RSSI",
            "SIGNAL_RSSI",
            "CHANNEL_RSSI",
            "RSSI_STANDARD_DEVIATION",
            "SNR"
        ]
    else
        return [
            "RSSI"
        ]
}

const LpwanService = {
    getDefaultLpwanSource: getDefaultLpwanSource,
    getLpwanOptions: getLpwanOptions,
    getDefaultMetricSource: getDefaultMetricSource,
    getMetricOptions: getMetricOptions
}

export default LpwanService;