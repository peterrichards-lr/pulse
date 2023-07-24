import React from "react";

const datetimeFormatter = new Intl.DateTimeFormat(undefined, {dateStyle: 'short', timeStyle: 'long', timeZone: 'UTC'});

const Campaign = (props) => {
    const campaign = props.campaign;

    return (
        campaign &&
        <tr data-campaign-id={campaign.id}>
            <td>{campaign.name}</td>
            <td>{campaign.description}</td>
            <td>{datetimeFormatter.format(new Date(campaign.begin))}</td>
            <td>{campaign.end && datetimeFormatter.format(new Date(campaign.end))}</td>
            <td>{campaign.campaignUrl}</td>
            <td>{campaign.status && campaign.status.name}</td>
        </tr>
    )
}

export default Campaign;