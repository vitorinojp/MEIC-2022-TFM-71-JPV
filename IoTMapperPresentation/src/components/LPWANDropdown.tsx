import {Component, SyntheticEvent} from 'react'
import {Dropdown, DropdownItemProps, DropdownProps} from 'semantic-ui-react'
import {MapStateSetActiveType} from './MapsCommons';


export interface ILPWANDropdownProps {
    getOptions: () => string[],
    setActive: MapStateSetActiveType
    value: string
}

export interface ILPWANDropdownState {
    isFetching: boolean,
    multiple: boolean,
    search: boolean,
    searchQuery?: string
}

export class LPWANDropdown extends Component<ILPWANDropdownProps, ILPWANDropdownState> {

    constructor(props: ILPWANDropdownProps) {
        super(props)

        this.state = {
            isFetching: false,
            multiple: true,
            search: true,
            searchQuery: undefined
        }

    }

    handleChange = (e: SyntheticEvent<HTMLElement, Event>, {value}: DropdownProps) => {
        if (value === undefined) {
            return
        } else {
            this.props.setActive(value.toString())
        }
    }

    handleSearchChange = (e: SyntheticEvent<HTMLElement, Event>, {searchQuery}: DropdownProps) => this.setState({searchQuery: searchQuery})

    fetchOptions = () => {
        this.setState({isFetching: true})

        setTimeout(() => {
            this.setState({isFetching: false})
        }, 500)
    }

    populateOptions(options: () => string[]): DropdownItemProps[] {
        return options().map((option, index) => (
            {key: index, value: option, text: option}
        ));
    }

    render() {
        const {isFetching, search}: ILPWANDropdownState = this.state

        return (
            <Dropdown
                fluid
                selection
                multiple={false}
                search={search}
                options={this.populateOptions(this.props.getOptions)}
                value={this.props.value}
                placeholder='Select Data Source'
                onChange={this.handleChange}
                onSearchChange={this.handleSearchChange}
                disabled={isFetching}
                loading={isFetching}
            />
        )
    }
}