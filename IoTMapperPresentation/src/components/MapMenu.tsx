import React, {Component} from 'react'
import {Button, Divider, Icon, Input, InputOnChangeData, Menu, SemanticCOLORS} from 'semantic-ui-react'
import {LPWANDropdown} from './LPWANDropdown'
import {MeasuresDropdown} from './MeasuresDropdown'
import {MapStateSetActiveType} from './MapsCommons'


// Internal state
export interface IHeatMapMenuProps {
    getLPWANOptions: () => string[]
    setLPWANActive: MapStateSetActiveType,
    getMetricsOptions: () => string[]
    setMetricActive: MapStateSetActiveType,
    lpwan: string,
    metric: string,
    setBlur: any,
    blur: number,
    setRadius: any,
    radius: number,
    tracking: boolean,
    setTracking: any
}

export interface IHeatMapMenuState {
    activeItem?: string
    color: SemanticCOLORS | undefined
}

function getColor(active: boolean): SemanticCOLORS | undefined {
    if (active)
        return "blue"
    else
        return undefined
}

export class MapMenu extends Component<IHeatMapMenuProps, IHeatMapMenuState> {
    constructor(props: IHeatMapMenuProps) {
        super(props)
        this.state = {
            color: getColor(this.props.tracking)
        }
    }

    handleBlur = (event: React.ChangeEvent<HTMLInputElement>, data: InputOnChangeData) => this.props.setBlur(parseInt(event.currentTarget.value))
    handleRadius = (event: React.ChangeEvent<HTMLInputElement>, data: InputOnChangeData) => this.props.setRadius(parseInt(event.currentTarget.value))
    handleTracking = () => {
        this.setState({
            color: getColor(!this.props.tracking)
        })
        this.props.setTracking(!this.props.tracking)

    }

    render() {

        return (
            <Menu vertical fluid>
                <Menu.Item name='lpwan'>
                    <Menu.Header>LPWAN</Menu.Header>
                    <LPWANDropdown
                        getOptions={this.props.getLPWANOptions}
                        setActive={this.props.setLPWANActive}
                        value={this.props.lpwan}
                    />
                </Menu.Item>
                <Menu.Item name='metric'>
                    <Menu.Header>Metrics</Menu.Header>
                    <MeasuresDropdown
                        getOptions={this.props.getMetricsOptions}
                        setActive={this.props.setMetricActive}
                        value={this.props.metric}
                    />
                </Menu.Item>
                <Menu.Item name='blur'>
                    <Menu.Header>Blur</Menu.Header>
                    <Input
                        type='range'
                        className='range-slider range-slider--primary w-100'
                        min="0"
                        max="50"
                        id="blur"
                        value={this.props.blur}
                        onChange={this.handleBlur}
                    />
                </Menu.Item>
                <Menu.Item name='radius'>
                    <Menu.Header>Radius</Menu.Header>
                    <Input
                        type='range'
                        className='range-slider range-slider--primary w-100'
                        min="0"
                        max="20"
                        id="radius"
                        value={this.props.radius}
                        onChange={this.handleRadius}
                    />
                </Menu.Item>
                <Divider />
                <Menu.Item name='geolocation' position="right">
                    <Button
                        id="geolocation"
                        onClick={this.handleTracking}
                        active={this.props.tracking}
                        color={this.state.color}
                    >
                        <Icon name="location arrow"/>
                        Use Location
                    </Button>
                </Menu.Item>
            </Menu>
        )
    }
}