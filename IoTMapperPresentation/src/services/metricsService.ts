import Configuration from "../config/Configuration";

const PATH_FOR_METRICS_LIST = "/api/v1/metrics/list/lorawan"

async function getList(lpwan: string): Promise<any[] | null> {
    let limit = 1000
    let offset = 0
    let totalCount = limit + 1

    let list: any[] | null = null

    try{
        while(offset < totalCount){
            let response = await fetch(
                Configuration.backendURL() + PATH_FOR_METRICS_LIST + `?limit=${limit}&offset=${offset}`,
                {
                    method: "GET",
                    mode: "cors"
                })

            let data = await response.json()

            if (response.ok) {
                totalCount = data?.properties?.totalCount
                let partialList = data?.properties?.list

                if (list == null)
                    list = partialList
                else
                    list = [ ...list, ...partialList]

                offset = offset + limit
            } else {
                return Promise.reject(new Error("Could not load list with code: " + response.status + " " + response.statusText))
            }
        }
        return list

    }catch (e: any) {
        return Promise.reject(new Error("Could not load list with Exception: " + e.toString()))
    }

}

export const MetricsService = {
    getList: getList
}

export default MetricsService