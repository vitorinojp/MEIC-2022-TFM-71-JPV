const envSettings = window as any;

// Backend configs
const BACKEND_PROTOCOL = "http://"
const BACKEND_HOST = "localhost"
const BACKEND_PORT = "8080"

// Local configs
const {PORT, REACT_APP_PORT} = process.env
const port = PORT || REACT_APP_PORT || "3000"

const backendProtocol = (): string => process.env.REACT_APP_BACKEND_PROTOCOL || envSettings.BACKEND_PROTOCOL || BACKEND_PROTOCOL
const backendHost = (): string  => process.env.REACT_APP_BACKEND_HOST || envSettings.BACKEND_HOST || BACKEND_HOST
const backendPort = (): string  => process.env.REACT_APP_BACKEND_PORT  || envSettings.BACKEND_PORT || BACKEND_PORT
const backendURL = (): string  => backendProtocol() + backendHost() + ":" + backendPort()

const Configuration = {
    backendProtocol: backendProtocol,
    backendHost: backendHost,
    backendPort: backendPort,
    backendURL: backendURL,

    port: port
}

export default Configuration