import React, {useEffect, useState} from 'react';
import {Button, Image} from "react-bootstrap";
import Cookies from "js-cookie";
import axios from "axios";
import {Link} from "react-router-dom";
import "./Friend.css"


export default function Friend(props) {
    const first_user_id = props.friend.firstUser.id
    const second_user_id = props.friend.secondUser.id
    const cookie_user_id = Number(Cookies.get("userId"))

    const [conversation, setConversation] = useState("")
    const [conversationLoading, setConversationLoading] = useState(true)
    const [conversationCode, setConversationCode] = useState(null)

    useEffect(() => {
        axios.get(`http://localhost:8080/conversation/findConversationByUserId/${first_user_id}/${second_user_id}`)
            .then(response => {
                setConversationCode(response.status);
                setConversation(response.data);
                setConversationLoading(false);
            })
            .catch(err => {
                setConversationCode(err.response.status)
                setConversationLoading(false);
            })
        }, [first_user_id, second_user_id])

    // if conversation doesnt exist, create it
    if (!conversationLoading && conversationCode === 404) {
        axios.post(`http://localhost:8080/conversation/createConversation/${first_user_id}/${second_user_id}`).then()
        axios.get(`http://localhost:8080/conversation/findConversationByUserId/${first_user_id}/${second_user_id}`)
            .then(response => {
                setConversationCode(response.status);
                setConversation(response.data);
                setConversationLoading(false);
            })
            .catch(err => {
                setConversationCode(err.response.status)
                setConversationLoading(false);
            })
    }

    let friend_id = null
    let friend_user_data = null

    function handleRemoval() {
        axios.delete(`http://localhost:8080/friends/deleteById/${friend_id}`).then()
    }

    // find out which id is associated with our friend
    if (first_user_id === cookie_user_id) {
        friend_id = second_user_id
        friend_user_data = props.friend.secondUser
    } else if (second_user_id === cookie_user_id) {
        friend_id = first_user_id
        friend_user_data = props.friend.firstUser
    }

    let friend_avatar_path = null
    let friend_nickname = null
    let friend_name = null
    let friend_surname = null
    if (friend_user_data) {
        friend_avatar_path = friend_user_data.profile.photo.fileName
        friend_nickname = friend_user_data.username
        friend_name = friend_user_data.profile.name
        friend_surname = friend_user_data.profile.surname
    }

    return (
        <div>
            <Link to={`/profile/${friend_id}`}>
                <Image className={"friend_avatar"} src={`http://localhost:8080/photo?filename=${friend_avatar_path}`}
                       roundedCircle={true} width="35px" height="35px"/>
                <div className={"friend_names_div"}>
                    <p className={"friend_nickname"}>{friend_nickname}</p>
                    <span className={"friend_name"}>{friend_name}</span>
                    <span className={"friend_surname"}>{friend_surname}</span>
                </div>
            </Link>
            <div className={"friend_buttons_div"}>
                <Link to={`/conversations/${conversation.id}`}><Button variant="primary" className={"friend_message_btn"}>Message</Button></Link>
                <Button variant="primary" className={"friend_remove_btn"} onClick={handleRemoval}>Remove friend</Button>
            </div>
        </div>
    );
}
