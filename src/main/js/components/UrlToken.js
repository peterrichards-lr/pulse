import React from "react";
const UrlToken = (props) => {
    const urlToken = props.urlToken;

    return (
        urlToken &&
        <tr data-interaction-id={urlToken.token}>
            <td>{urlToken.token}</td>
            <td>{urlToken.campaign.name}</td>
            <td>{urlToken.campaign.status && urlToken.campaign.status.name}</td>
            <td>{urlToken.acquisition && urlToken.acquisition.campaign}</td>
            <td>{urlToken.acquisition && urlToken.acquisition.content}</td>
            <td>{urlToken.acquisition && urlToken.acquisition.medium}</td>
            <td>{urlToken.acquisition && urlToken.acquisition.source}</td>
            <td>{urlToken.acquisition && urlToken.acquisition.term}</td>
        </tr>
    )
}

export default UrlToken