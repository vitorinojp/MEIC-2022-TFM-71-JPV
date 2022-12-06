import LpwanService from "../services/lpwanService"

function getDefaultLpwanSource(): string {
    return LpwanService.getDefaultLpwanSource()
}

function getLPWANOptions(): string[] {
    return LpwanService.getLpwanOptions()
}

function getDefaultMetricSource(lpwan: string): string {
    return LpwanService.getDefaultMetricSource(lpwan)
}

function getMetricOptions(lpwan: string): string[] {
    return LpwanService.getMetricOptions(lpwan)
}

const InfoController = {
    getDefaultLpwanSource: getDefaultLpwanSource,
    getLPWANOptions: getLPWANOptions,
    getDefaultMetricSource: getDefaultMetricSource,
    getMetricOptions: getMetricOptions
}

export default InfoController;