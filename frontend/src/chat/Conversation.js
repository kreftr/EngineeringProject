import React, {useEffect, useState} from 'react';
import Cookies from "js-cookie";
import {Image} from "react-bootstrap";
import {Link} from "react-router-dom";
import "./Conversation.css"
import axios from "axios";


export default function Conversation(props) {
    const conversation_id = props.conversation.id
    const first_user_id = props.conversation.first_user.id
    const second_user_id = props.conversation.second_user.id
    const cookie_user_id = Number(Cookies.get("userId"))

    const [recentMessage, setRecentMessage] = useState([])
    const [recentMessageCode, setRecentMessageCode] = useState(null)
    const [recentMessageLoading, setRecentMessageLoading] = useState(true)

    useEffect(() => {
        axios.get(`http://localhost:8080/conversation/getRecentMessage/${conversation_id}`).then(response => {
            setRecentMessageCode(response.status);
            setRecentMessage(response.data);
            setRecentMessageLoading(false);
        })
        .catch(err => {
            setRecentMessageCode(err.response.status)
            setRecentMessageLoading(false);
        })
    }, [conversation_id])

    // find out which id is associated with our friend
    let friend_user_data = null
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

    return (
        <Link to={`/chat/${conversation_id}`}>
            <div>
                <Image className={"conversation_avatar"} src={`http://localhost:8080/photo?filename=${friend_avatar_path}`}
                       roundedCircle={true} width="35px" height="35px"/>
                <p className={"conversation_nickname"}>{friend_username}</p>

                { recentMessageCode === 200 && !recentMessageLoading ?
                <p className={"conversation_recent_msg_content"}>{recentMessage.content}</p>
                :
                <></>
                }
            </div>
        </Link>
    )
}