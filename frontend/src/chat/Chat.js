import React, {useEffect, useState} from 'react';
import {Col, Container, ListGroupItem, Nav, Row, Tab} from "react-bootstrap";
import Cookies from "js-cookie"
import axios from "axios"
import Conversation from "./Conversation"
import * as SockJS from "sockjs-client";
import * as Stomp from "stompjs";
import './Chat.css'
import Message from "./Message";

function Chat() {

    const [inputValue, setInputValue] = useState("");
    const [conversations, setConversations] = useState([]);
    const [activeConversation, setActiveConversation] = useState(null);
    const [messages, setMessages] = useState([]);
    const [fetchedData, setFetchedData] = useState([])

    const [socket] = useState(new SockJS('http:localhost:8080/conversation/messages'))
    const [stompClient] = useState(Stomp.over(socket))
    const [isStompConnected, setIsStompConnected] = useState(false)
    const [isStompSubscribed, setIsStompSubscribed] = useState(false)

    useEffect(() => {
        axios.get(`http://localhost:8080/conversation/getAllUserConversations`, {
            headers: {
                'Authorization': Cookies.get("authorization")
            }
        }).then((response) => {
            setConversations(response.data);
        }).catch(err => {
            console.log(err.response)
        });
    }, []);

    useEffect(() => {
        stompClient.connect({}, function (frame) {
            console.log("Connected to: " + frame);
            setIsStompConnected(true)
        })
    }, [stompClient]);

    useEffect(() => {
        if (fetchedData !== null && activeConversation !== null) {
            if (fetchedData.conversation_id === activeConversation.conversationId) {
                setMessages(messages => [...messages, fetchedData]);
            }
        }
    }, [fetchedData, activeConversation]);

    function subscribeToStomp() {
        return new Promise( () => {
            stompClient.subscribe("/conversation/messages", function (response) {
                let msgData = JSON.parse(response.body);
                setFetchedData(msgData)
            })
        })
    }

    useEffect(() => {
        if (activeConversation === null) { return; }

        if (isStompConnected === true && isStompSubscribed === false) {
            subscribeToStomp().finally(setIsStompSubscribed(true))
        }

        // get initial messages from database
        axios.get(`http://localhost:8080/conversation/getAllMessages/${activeConversation.conversationId}`, {
            headers: {
                'Authorization': Cookies.get("authorization")
            }
        }).then((response) => {
            setMessages(response.data);
        }).catch(err => {
            console.log(err.response)
        });

    }, [activeConversation]);

    function sendMessage(message) {
        stompClient.send("/messages", {}, JSON.stringify({
            message: message,
            conversation_id: activeConversation.conversationId,
            author_id: Cookies.get("userId")
        }));
        document.getElementById('message_sender_input').value = "";  // clear input
    }

    // disconnect stomp client before page unloads
    window.addEventListener('onbeforeunload', function(e) {
        if (stompClient != null) {
            stompClient.disconnect();
        }
    }, false);

    return (
        <Container className={"mt-5 container"}>
            { conversations ?
                <Tab.Container>
                    <Row>
                        <Col className={"col-4"}>
                            <Nav variant="pills" className="flex-column">
                                {
                                    conversations.map((conversation, key) =>
                                        <Nav.Item key={key} onClick={() => {
                                            setActiveConversation(conversation);
                                        }}
                                        >
                                            <Nav.Link eventKey={conversation.conversationId}>
                                                <Conversation conversation={conversation}/>
                                            </Nav.Link>
                                        </Nav.Item>
                                    )
                                }
                            </Nav>
                        </Col>
                        <Col className={"col-8"}>
                            { activeConversation !== null ?
                                <>
                                    {
                                        messages.map((message, key) =>
                                            <ListGroupItem key={key}>
                                                <Message message={message}/>
                                            </ListGroupItem>
                                        )
                                    }
                                    <input className={"CHAT-input mt-4"} id={"message_sender_input"} type="text"
                                           onChange={event => setInputValue(event.target.value)}
                                           placeholder={"Write message"}
                                           onKeyDown={ event => {
                                               if (event.key === 'Enter') { sendMessage(inputValue) }
                                           }}
                                    />
                                </>
                                :
                                <></>
                            }
                        </Col>
                    </Row>
                </Tab.Container>
                :
                <></>
            }
        </Container>
    );
}
export default Chat;
