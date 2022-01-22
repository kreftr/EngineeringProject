import React from 'react';
import {Image} from "react-bootstrap";
import "./Message.css"


export default function Message(props) {
    const message_date = new Date(props.message.date)
    const message_content = props.message.messageContent
    const message_author_nickname = props.message.authorNickname
    const message_avatar_path = props.message.avatar
    const message_date_short = message_date.toLocaleDateString(navigator.language, {hour: '2-digit', minute:'2-digit'});

    return (
        <div>
            {
                <div>
                    <p className={"message_date"}>{message_date_short}</p>
                    <Image className={"message_avatar"} src={`http://localhost:8080/photo?filename=${message_avatar_path}`}
                    roundedCircle={true} width="35px" height="35px"/>
                    <p className={"message_nickname"}>{message_author_nickname}</p>
                    <p className={"message_content"}>{message_content}</p>
                </div>
            }
        </div>
    )
}