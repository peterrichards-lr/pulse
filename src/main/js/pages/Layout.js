import React from "react";
import {Link, Outlet} from "react-router-dom";

const Layout = () => {
    return (
        <>
            <nav>
                <ul>
                    <li>
                        <Link to="/">Campaigns</Link>
                    </li>
                    <li>
                        <Link to="/url-tokens">Url Tokens</Link>
                    </li>
                </ul>
            </nav>

            <Outlet/>
        </>
    )
};

export default Layout;