import React, {useEffect, useState} from 'react';
import {Container, ListGroup, ListGroupItem} from "react-bootstrap";
import Message from "./Message"
import Cookies from "js-cookie";
import axios from "axios";
import "./Chat.css"
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';

function Chat(props) {
    const [inputValue, setInputValue] = useState("");
    const [messages, setMessages] = useState([])
    const [messagesLoading, setMessagesLoading] = useState(true)
    const [messagesCode, setMessagesCode] = useState(null)
    const [stompClient, setStompClient] = useState(null)

    useEffect(() => {
        setStompClient(Stomp.over('ws://localhost:8080/chat'))
        stompClient.connect({}, function() {
            stompClient.subscribe('/topic/messages', function(messageOutput) {
                showNewMessage(messageOutput.body)
            })
        })
    }, [])

    function showNewMessage(messageOutput) {
        console.log(messageOutput)
    }

    const sendNewMessage = (event) => {
        if (event.key === 'Enter') {
            stompClient.send("/app/chat", {}, JSON.stringify({
                'conversation_id': props.conversation.conversationId,
                'author_id': Cookies.get("userId"), 'text': inputValue
            }))
            document.getElementById("message_sender_input").value = ""  // clear input after
        }
    }

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
                               onChange={event => setInputValue(event.target.value)} onKeyDown={sendNewMessage}
                               placeholder={"Write message"}/>
                    </div>
                </div>
                :
                <></>
            }
        </Container>
    );
}
export default Chat;
