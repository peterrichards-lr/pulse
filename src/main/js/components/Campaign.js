import React from "react";
import {Link} from "react-router-dom";

const datetimeFormatter = new Intl.DateTimeFormat(undefined, {dateStyle: 'short', timeStyle: 'long', timeZone: 'UTC'});

const Campaign = (props) => {
    const campaign = props.campaign;

    return (
        campaign &&
        <tr data-campaign-id={campaign.id}>
            <td><Link to='/campaign' state={{campaign}}>{campaign.name}</Link></td>
            <td>{campaign.description}</td>
            <td>{datetimeFormatter.format(new Date(campaign.begin))}</td>
            <td>{campaign.end && datetimeFormatter.format(new Date(campaign.end))}</td>
            <td>{campaign.campaignUrl}</td>
            <td>{campaign.status && campaign.status.name}</td>
        </tr>
    )
}

export default Campaign;