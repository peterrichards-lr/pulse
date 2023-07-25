import React, {useEffect, useState} from "react";
import UrlTokenList from "./UrlTokenList";

const AllUrlTokens = (props) => {
    const [urlTokenData, setUrlTokenData] = useState()

    useEffect(() => {
        const headers = {
            HEADER_CONTENT_TYPE: 'application/json',
        };
        fetch(`/api/url-tokens`, {headers})
            .then((response) => response.json())
            .then((data) => {
                const {content, totalElements} = data;
                setUrlTokenData(content);
            })
    }, [props]);

    return <UrlTokenList urlTokens={urlTokenData}/>
}

export default AllUrlTokens;