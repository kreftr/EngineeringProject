import React, {useEffect, useState} from 'react';
import {Container, ListGroup, ListGroupItem} from "react-bootstrap";
import Message from "./Message"
import {useParams} from "react-router-dom";
import Cookies from "js-cookie";
import axios from "axios";
import "./Chat.css"

function Chat(props) {
    const [inputValue, setInputValue] = useState("");
    const [messages, setMessages] = useState([])
    const [messagesLoading, setMessagesLoading] = useState(true)
    const [messagesCode, setMessagesCode] = useState(null)
    const {conversation_id} = useParams();

    useEffect(() => {
        // messages are collected every certain amount of time from server
        const interval = setInterval(() => {
            axios.get(`http://localhost:8080/conversation/getAllMessages/${props.conversation.conversationId}`, {
                headers: {
                    'Authorization': Cookies.get("authorization")
                }
            })
            .then(response => {
                setMessagesCode(response.status);
                setMessages(response.data);
                setMessagesLoading(false);
            })
            .catch(err => {
                setMessagesCode(err.response.status)
                setMessagesLoading(false);
            })
        }, 1000)
        return () => clearInterval(interval)
    }, [conversation_id])

    const sendNewMessage = (event) => {
        if (event.key === 'Enter') {
            let author_id = Cookies.get("userId")
            axios.post(`http://localhost:8080/conversation/addMessage/${props.conversation.conversationId}/${author_id}/${inputValue}`, {}, {
                headers: {
                    'Authorization': Cookies.get("authorization")
                }
            }).then()
            axios.get(`http://localhost:8080/conversation/getAllMessages/${props.conversation.conversationId}`, {
                headers: {
                    'Authorization': Cookies.get("authorization")
                }
            }).then(response => {
                setMessagesCode(response.status);
                setMessages(response.data);
                document.getElementById("message_sender_input").value = ""  // clear input after
            })
        }
    }

    // TODO new messages stack down infinitely, fix this
    return (
        <Container className={"container"}>
            { messagesCode === 200 && !messagesLoading ?
                <div>
                    <ListGroup className={"list-group CHAT-window"}>
                        {
                            messages.map((message, key) =>
                                <ListGroupItem key={key}>
                                    <Message message={message}/>
                                </ListGroupItem>
                            )
                        }
                    </ListGroup>
                    <div id={"message_sender"}>
                        <input className={"CHAT-input mt-4"} id={"message_sender_input"} type="text"
                               onChange={event => setInputValue(event.target.value)} onKeyDown={sendNewMessage}/>
                    </div>
                </div>
                :
                <></>
            }
        </Container>
    );
}
export default Chat;
