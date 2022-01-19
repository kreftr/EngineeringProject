import React, {useEffect, useState} from 'react';
import Cookies from "js-cookie";
import {Col, Image} from "react-bootstrap";
import default_profile_picture from "../assets/images/default_profile_picture.jpg"
import "./Conversation.css"
import axios from "axios";


function Conversation(props) {

    const [recentMessage, setRecentMessage] = useState([])
    const [recentMessageCode, setRecentMessageCode] = useState(null)
    const [recentMessageLoading, setRecentMessageLoading] = useState(true)

    useEffect(() => {
        axios.get(`http://localhost:8080/conversation/getRecentMessage/${props.conversation.conversationId}`, {
            headers: {
                'Authorization': Cookies.get("authorization")
            }
        }).then(response => {
            setRecentMessageCode(response.status);
            setRecentMessage(response.data);
            setRecentMessageLoading(false);
        })
        .catch(err => {
            setRecentMessageCode(err.response.status)
            setRecentMessageLoading(false);
        })
    }, [props.conversation.conversationId])



    return (
        <div className={"mb-2 mt-2 CONVERSATION-holder"}>
            <Col className={"col-4"}>
                {props.conversation.profilePhoto !== null ?
                    <Image
                        src={`http://localhost:8080/photo?filename=${props.conversation.profilePhoto}`}
                        roundedCircle={true} width="100px" height="100px"/>
                    :
                    <Image src={default_profile_picture}
                           roundedCircle={true} width="100px" height="100px"/>
                }
            </Col>
            <Col className={"col-8"}>
                { !recentMessageLoading && recentMessageCode === 200 ?
                    <div>
                        <h2>{props.conversation.username}</h2>
                        <div>{recentMessage.date}</div>
                        <span>{recentMessage.username}: {recentMessage.content}</span>
                    </div>
                    :
                    <div>
                        <h2>{props.conversation.username}</h2>
                        <div>Click here and start conversation</div>
                    </div>
                }
            </Col>

        </div>
    )
}
export default Conversation;