import React from 'react';
import {Button, Container, Image} from "react-bootstrap";
import Cookies from "js-cookie";
import axios from "axios";
import useAxiosGet from "../hooks/HttpRequests";
import {Link} from "react-router-dom";
import "./Friend.css"


export default function Friend(props) {
    const first_user_id = props.friend.firstUser.id
    const second_user_id = props.friend.secondUser.id
    const cookie_user_id = Number(Cookies.get("userId"))

    let friend_id = null
    let friend_user_data = null

    function handleRemoval() {
        axios.delete(`http://localhost:8080/friends/deleteById/${friend_id}`)
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
                <p className={"friend_nickname"}>{friend_nickname}</p>
                <p className={"friend_name"}>{friend_name}</p>
                <p className={"friend_surname"}>{friend_surname}</p>
            </Link>
            <div>
                <Link to={`/chat/${friend_id}`}><Button variant="primary">Message</Button></Link>
                <Button variant="primary" onClick={handleRemoval}>Remove friend</Button>
            </div>
        </div>
    );
}
