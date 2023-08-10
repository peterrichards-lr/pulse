import React from "react";
import {Link} from "react-router-dom";

const UrlToken = (props) => {
    const urlToken = props.urlToken;

    return (
        urlToken &&
        <tr data-interaction-id={urlToken.token}>
            <td><a target="_blank" href={"/" + urlToken.token}>{urlToken.token}</a></td>
            <td><Link to='/campaign' state={{campaign: urlToken.campaign}}>{urlToken.campaign.name}</Link></td>
            <td>{urlToken.campaign.status && urlToken.campaign.status.name}</td>
            <td>{urlToken.acquisition && urlToken.acquisition.content}</td>
            <td>{urlToken.acquisition && urlToken.acquisition.medium}</td>
            <td>{urlToken.acquisition && urlToken.acquisition.source}</td>
            <td>{urlToken.acquisition && urlToken.acquisition.term}</td>
        </tr>
    )
}

export default UrlToken