import React from 'react';
import {Image} from "react-bootstrap";
import "./Message.css"
import default_profile_picture from "../assets/images/default_profile_picture.jpg";


export default function Message(props) {
    const message_date = new Date(props.message.messageDate)
    const message_content = props.message.message
    const message_author_nickname = props.message.author_nickname
    const message_avatar_path = props.message.photoPath
    const message_date_short = message_date.toLocaleDateString(navigator.language, {hour: '2-digit', minute:'2-digit'});

    return (
        <div>
            {
                <div>
                    <p className={"message_date"}>{message_date_short}</p>
                    { message_avatar_path !== null ?
                        <Image className={"message_avatar"}
                               src={`http://localhost:8080/photo?filename=${message_avatar_path}`}
                               roundedCircle={true}/>
                        :
                        <Image className={"message_avatar"} src={default_profile_picture} roundedCircle={true}/>
                    }
                    <p className={"message_nickname"}>{message_author_nickname}</p>
                    <p className={"message_content"}>{message_content}</p>
                </div>
            }
        </div>
    )
}