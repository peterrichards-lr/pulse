import React from "react";

const datetimeFormatter = new Intl.DateTimeFormat(undefined, {dateStyle: 'short', timeStyle: 'long', timeZone: 'UTC'});

const Interaction = (props) => {
    const interaction = props.interaction;

    return (
        interaction &&
        <tr data-interaction-id={interaction.id}>
            <td>{interaction.ipAddress}</td>
            <td>{interaction.userAgent}</td>
            <td>{interaction.type}</td>
            <td>{datetimeFormatter.format(new Date(interaction.timestamp))}</td>
        </tr>
    )
}

export default Interaction;