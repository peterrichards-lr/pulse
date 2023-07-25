import React from "react";
import InteractionList from "../components/InteractionList";
import {Navigate, useLocation} from "react-router-dom";
import CampaignUrlTokens from "../components/CampaignUrlTokens";

const datetimeFormatter = new Intl.DateTimeFormat(undefined, {dateStyle: 'short', timeStyle: 'long', timeZone: 'UTC'});

const Campaigns = () => {
    const location = useLocation();
    const campaign = location.state ? location.state.campaign : undefined;

    if (!campaign) {
        return <Navigate to="/"/>
    }

    return (<>
        <h1>Campaign</h1>
        <dl className="info-grid">
            <dt>Name</dt>
            <dd className="info-item">{campaign.name}</dd>
            <dt>Description</dt>
            <dd className="info-item">{campaign.description}</dd>
            <dt>Status</dt>
            <dd className="info-item">{campaign.status && campaign.status.name}</dd>
            <dt>Begin</dt>
            <dd className="info-item">{datetimeFormatter.format(new Date(campaign.begin))}</dd>
            <dt>End</dt>
            <dd className="info-item">{campaign.end ? datetimeFormatter.format(new Date(campaign.end)) : "N/A"}</dd>
            <dt>Campaign URL</dt>
            <dd className="info-item">{campaign.campaignUrl}</dd>
        </dl>
        <h2>URL Tokens</h2>
        <CampaignUrlTokens campaignId={campaign.id}/>
        <h2>Interactions</h2>
        <InteractionList campaignId={campaign.id}/>
    </>)
};

export default Campaigns;