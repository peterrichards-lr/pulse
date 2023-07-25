import React, {StrictMode} from 'react';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {createRoot} from 'react-dom/client';
import Layout from "./pages/Layout";
import Campaigns from "./pages/Campaigns";
import NoMatch from "./pages/NoMatch";
import UrlTokens from "./pages/UrlTokens";
import Campaign from "./pages/Campaign";

const App = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Layout/>}>
                    <Route index element={<Campaigns/>}/>
                    <Route path="url-tokens" element={<UrlTokens/>}/>
                    <Route path="campaign" element={<Campaign/>}/>
                    <Route path="*" element={<NoMatch/>}/>
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

const domNode = document.getElementById('react')
createRoot(domNode).render(
    <StrictMode>
        <App/>
    </StrictMode>
)