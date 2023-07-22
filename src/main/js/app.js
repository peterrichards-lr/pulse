import * as halHttp from 'hal-http-client';
import React, { useEffect, useState } from 'react';
import { createRoot } from 'react-dom/client';

const datetimeFormatter = new Intl.DateTimeFormat('en-GB', { dateStyle: 'short', timeStyle: 'short', timeZone: 'UTC' });

const App = (props) => {
    const [campaigns, setCampaigns] = useState()

    const hal = halHttp.create( {
        headers: { 'accept-language': 'de' },
        on: {
            // globally define how responses for status codes should be handled that were not handled locally
            '5xx': ( data, response ) => {
                console.error( `Caught unhandled 5xx error (status: ${response.status})` );
            }
        }
    } );

    useEffect(() => {
        hal.get('/api/campaigns')
            .on({
            '200': hal.thenFollowAll( 'campaigns' )
            }).on({
            '200':hal.thenFollow( 'status' )
            }).on({
            '200': carList => {
                console.log(carList);
            }
            });
    }, [props]);

    return (
        <CampaignList campaigns={campaigns} />
    );
}

class CampaignList extends React.Component{
    render() {
        const campaigns =  this.props.campaigns && this.props.campaigns.map(campaign =>
            <Campaign key={campaign._links.self.href} campaign={campaign}/>
        );
        return (
            <table>
                <tbody>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Begin</th>
                    <th>End</th>
                    <th>Campaign URL</th>
                    <th>Status</th>
                </tr>
                {campaigns}
                </tbody>
            </table>
        )
    }
}

class Campaign extends React.Component{
    render() {
        return (
            this.props.campaign &&
            <tr>
                <td>{this.props.campaign.name}</td>
                <td>{this.props.campaign.description}</td>
                <td>{datetimeFormatter.format(new Date(this.props.campaign.begin))}</td>
                <td>{datetimeFormatter.format(new Date(this.props.campaign.end))}</td>
                <td>{this.props.campaign.campaignUrl}</td>
                <td>{this.props.campaign.status}</td>
            </tr>
        )
    }
}

const domNode = document.getElementById('react')
createRoot(domNode).render(
    <App />
)