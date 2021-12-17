import React, {useEffect, useState} from 'react';
import {Image} from "react-bootstrap";
import "./Message.css"
import axios from "axios";


export default function Message(props) {
    const message_date = new Date(props.message.message_date)
    const message_content = props.message.content
    const message_author_id = props.message.user.id
    const message_date_short = message_date.toLocaleDateString(navigator.language, {hour: '2-digit', minute:'2-digit'});

    const [messageAuthorProfile, setMessageAuthorProfile] = useState([])
    const [messageAuthorLoading, setMessageAuthorLoading] = useState(true)
    const [messageAuthorCode, setMessageAuthorCode] = useState(null)

    useEffect(() => {
        axios.get(`http://localhost:8080/profile?id=${message_author_id}`).then(response => {
            setMessageAuthorCode(response.status);
            setMessageAuthorProfile(response.data);
            setMessageAuthorLoading(false);
        })
        .catch(err => {
            setMessageAuthorCode(err.response.status)
            setMessageAuthorLoading(false);
        })
    }, [message_author_id])

    return (
        <div>
            { messageAuthorCode === 200 && !messageAuthorLoading ?
                <div>
                    <p className={"message_date"}>{message_date_short}</p>
                    <Image className={"message_avatar"} src={`http://localhost:8080/photo?filename=${messageAuthorProfile.profile_photo}`}
                    roundedCircle={true} width="35px" height="35px"/>
                    <p className={"message_nickname"}>{messageAuthorProfile.name}</p>
                    <p className={"message_content"}>{message_content}</p>
                </div>
                :
                <></>
            }
        </div>
    )
}