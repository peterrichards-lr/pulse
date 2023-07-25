import React, {useEffect, useState} from "react";
import UrlTokenList from "./UrlTokenList";
import {Navigate} from "react-router-dom";

const CampaignUrlTokens = (props) => {
    if (!props.campaignId) {
        return <Navigate to="/"/>
    }

    const [urlTokenData, setUrlTokenData] = useState()

    useEffect(() => {
        const headers = {
            HEADER_CONTENT_TYPE: 'application/json',
        };
        fetch(`/api/campaigns/${props.campaignId}/url-tokens`, {headers})
            .then((response) => response.json())
            .then((data) => {
                const {content, totalElements} = data;
                setUrlTokenData(content);
            })
    }, [props]);

    return <UrlTokenList urlTokens={urlTokenData}/>
}

export default CampaignUrlTokens;