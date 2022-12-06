import React, {Component} from 'react'
import {Icon, Menu, MenuItemProps} from 'semantic-ui-react'
import {Link} from "react-router-dom";

interface IMainMenuProps {
}

interface IMainMenuState {
    activeItem?: string;
}

export default class MainMenu extends Component<IMainMenuProps, IMainMenuState> {

    constructor(props: IMainMenuProps) {
        super(props);
        this.state = {
            activeItem: undefined
        };

    }

    handleItemClick = (e: React.MouseEvent<HTMLAnchorElement, MouseEvent>, {name}: MenuItemProps) => this.setState({activeItem: name})

    render() {
        return (
            <div style={{
                width: "100%",
                minHeight: "6vh",
                boxSizing: "border-box",
                justifyContent: "space-between",
                alignItems: "center",
                transition: "all .7s ease-in"
            }}>
                <Menu fixed='top'>
                    <Menu.Item
                        header
                        as={Link}
                        to={"/"}>
                        <Icon name='wifi'/>
                        IoTMapper
                    </Menu.Item>
                    <Menu.Item
                        as={Link}
                        name='About Us'
                        to="about"
                    />
                    <Menu.Item
                        as={Link}
                        name='Help!'
                        to="help"
                    />
                </Menu>
            </div>
        )
    }
}