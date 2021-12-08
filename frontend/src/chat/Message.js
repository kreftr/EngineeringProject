import React from 'react';
import {Image} from "react-bootstrap";
import useAxiosGet from "../hooks/HttpRequests";

export default function Message(props) {
    let message_date = new Date(props.message.message_date)
    let message_date_short = message_date.toLocaleDateString(navigator.language, {hour: '2-digit', minute:'2-digit'});

    let message_content = props.message.content
    let message_author_id = props.message.user.id

    let message_author_profile = useAxiosGet(`http://localhost:8080/profile?id=${message_author_id}`).data

    let message_nickname = null
    let message_avatar_path = null
    if (message_author_profile) {
        message_nickname = message_author_profile.name
        message_avatar_path = message_author_profile.profile_photo
    }

    return (
        <div>
            <Image className={"message_avatar"} src={`http://localhost:8080/photo?filename=${message_avatar_path}`}
                   roundedCircle={true} width="35px" height="35px"/>
            <p className={"message_nickname"}>{message_nickname}</p>
            <p className={"message_date"}>{message_date_short}</p>
            <p className={"message_content"}>{message_content}</p>
        </div>
    )
}