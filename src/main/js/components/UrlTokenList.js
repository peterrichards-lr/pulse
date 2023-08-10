import React from "react";
import UrlToken from "./UrlToken";

const UrlTokenList = (props) => {
    const urlTokenData = props.urlTokens;

    const urlTokens = urlTokenData && urlTokenData.map(urlToken => {
        return <UrlToken key={urlToken.token} urlToken={urlToken}/>
    });
    return (
        <table className="styled-table">
            <thead>
            <tr>
                <th>Url Token</th>
                <th>Campaign</th>
                <th>Campaign Status</th>
                <th>UTM Content</th>
                <th>UTM Medium</th>
                <th>UTM Source</th>
                <th>UTM Term</th>
            </tr>
            </thead>
            <tbody>
            {urlTokens}
            </tbody>
        </table>
    )
}

export default UrlTokenList