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

    const [socket] = useState(new SockJS('http:localhost:8080/conversation'))
    const [stompClient] = useState(Stomp.over(socket))


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
        if (activeConversation === null) { return; }

        if (stompClient != null) {
            stompClient.disconnect();
            console.log("disconnected");
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

        // connect to chat
        stompClient.connect({}, function (frame) {
            console.log("Connected to: " + frame);
            stompClient.subscribe("/" + activeConversation.conversationId, function (response) {
                let data = JSON.parse(response.body);
                setMessages([...messages, data])  // TODO test it
                // console.log(data.message, data.author_id, data.conversation_id)
            });
        });
    }, [activeConversation]);

    function sendMessage(message) {
        stompClient.send("/" + activeConversation.conversationId, {}, JSON.stringify({
            message: message,
            conversation_id: activeConversation.conversationId,
            author_id: activeConversation.userId
        }));
    }


    return (
            <Container className={"mt-5 container"}>
                { conversations ?
                    <Tab.Container>
                        <Row>
                            <Col className={"col-4"}>
                                <Nav variant="pills" className="flex-column">
                                    {
                                        conversations.map((conversation, key) =>
                                            <Nav.Item key={key}>
                                                <Nav.Link eventKey={conversation.conversationId}
                                                          onClick={() => {
                                                              setActiveConversation(conversation);
                                                          }}>
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
