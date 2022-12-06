import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import 'semantic-ui-css/semantic.min.css';

import MainPage from "./pages/MainPage";
import AboutPage from "./pages/AboutPage";
import HelpPage from "./pages/HelpPage";

export default function Simple(): JSX.Element {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<MainPage/>}/>
                <Route path="/about" element={<AboutPage/>}/>
                <Route path="/help" element={<HelpPage/>}/>
                <Route path='*' element={<Navigate to="/"/>}/>
            </Routes>
        </BrowserRouter>
    );
}