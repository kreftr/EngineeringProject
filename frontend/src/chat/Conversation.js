import React from 'react';
import Cookies from "js-cookie";
import useAxiosGet from "../hooks/HttpRequests";
import {Image} from "react-bootstrap";
import {Link} from "react-router-dom";

export default function Conversation(props) {
    const conversation_id = props.conversation.id
    let friend_user_data = null

    const first_user_id = props.conversation.first_user.id
    const second_user_id = props.conversation.second_user.id
    const cookie_user_id = Number(Cookies.get("userId"))

    // find out which id is associated with our friend
    if (first_user_id === cookie_user_id) {
        friend_user_data = props.conversation.second_user
    } else if (second_user_id === cookie_user_id) {
        friend_user_data = props.conversation.first_user
    }

    let friend_avatar_path = null
    let friend_username = null
    if (friend_user_data) {
        friend_avatar_path = friend_user_data.profile.photo.fileName
        friend_username = friend_user_data.profile.name
    }

    const recent_message = useAxiosGet(`http://localhost:8080/conversation/getRecentMessage/${conversation_id}`)
    let recent_message_content = null
    if (recent_message.data) {
        recent_message_content = recent_message.data.content
    }

    return (
        <Link to={`/chat/${conversation_id}`}>
            <div>
                <p className={"conversation_nickname"}>{friend_username}</p>
                <Image className={"conversation_avatar"} src={`http://localhost:8080/photo?filename=${friend_avatar_path}`}
                       roundedCircle={true} width="35px" height="35px"/>
                <p>{recent_message_content}</p>
            </div>
        </Link>
    )
}