import React from "react";
import {useEffect, useState} from "react";
import UrlToken from "./UrlToken";

const UrlTokenList = (props) => {
    const [urlTokenData, setUrlTokenData] = useState()

    useEffect(() => {
        const headers = {
            HEADER_CONTENT_TYPE: 'application/json',
        };
        fetch(`/api/url-tokens`, {headers})
            .then((response) => response.json())
            .then((data) => {
                console.log(data);
                const {content, totalElements} = data;
                setUrlTokenData(content);
            })
    }, [props]);

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
                <th>UTM Campaign</th>
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