import React, {useEffect, useState} from "react";
import Campaign from "./Campaign";

const CampaignList = (props) => {
    const [campaignData, setCampaignData] = useState()

    useEffect(() => {
        const headers = {
            HEADER_CONTENT_TYPE: 'application/json',
        };
        fetch('/api/campaigns', {headers})
            .then((response) => response.json())
            .then((data) => {
                const {content, totalElements} = data;
                setCampaignData(content);
            })
    }, [props]);

    const campaigns = campaignData && campaignData.map(campaign => {
        return <Campaign key={campaign.id} campaign={campaign}/>
    });
    return (
        <table className="styled-table">
            <thead>
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Begin</th>
                <th>End</th>
                <th>Campaign URL</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody>
            {campaigns}
            </tbody>
        </table>
    )
}

export default CampaignList;