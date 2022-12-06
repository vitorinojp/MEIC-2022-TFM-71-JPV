import {Component} from "react";
import MainMenu from "src/components/MainMenu";


export default class MainPage extends Component {
    render() {
        return (
            <div style={{
                backgroundColor: "#fff",
                margin: 0,
                padding: 0,
                flex: 1
            }}>
                <MainMenu/>

            </div>
        );
    }
}