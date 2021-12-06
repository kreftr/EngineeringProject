import React from 'react';

export default function Message(props) {
    let message_nickname = "Placeholder name"
    let message_date = new Date(props.message.message_date)
    let message_date_short = message_date.toLocaleDateString(navigator.language, {hour: '2-digit', minute:'2-digit'});
    let message_content = props.message.content

    return (
        <div>
            <p className={"message_nickname"}>{message_nickname}</p>
            <p className={"message_date"}>{message_date_short}</p>
            <p className={"message_content"}>{message_content}</p>
        </div>
    )
}