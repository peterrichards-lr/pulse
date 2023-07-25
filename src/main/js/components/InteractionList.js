import React, {useEffect, useState} from "react";
import Interaction from "./Interaction";
import {Navigate} from "react-router-dom";

const InteractionList = (props) => {
    if (!props.campaignId) {
        return <Navigate to="/"/>
    }

    const [interactionData, setInteractionData] = useState()

    useEffect(() => {
        const headers = {
            HEADER_CONTENT_TYPE: 'application/json',
        };

        fetch(`/api/campaigns/${props.campaignId}/interactions`, {headers})
            .then((response) => response.json())
            .then((data) => {
                const {content, totalElements} = data;
                setInteractionData(content);
            })

    }, [props]);

    const interactions = interactionData && interactionData.map(interaction => {
        return <Interaction key={interaction.id} interaction={interaction}/>
    });
    return (
        <table className="styled-table">
            <thead>
            <tr>
                <th>IP Address</th>
                <th>User Agent</th>
                <th>Type</th>
                <th>Timestamp</th>
            </tr>
            </thead>
            <tbody>
            {interactions}
            </tbody>
        </table>
    )
}

export default InteractionList;