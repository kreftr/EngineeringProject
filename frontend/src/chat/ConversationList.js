import React, {useEffect, useState} from 'react';
import {Col, Container, Nav, Row, Tab} from "react-bootstrap";
import Cookies from "js-cookie"
import axios from "axios"
import Conversation from "./Conversation"
import Chat from "./Chat";
import * as SockJS from "sockjs-client";
import * as Stomp from "stompjs";

function ConversationList() {

    const [inputValue, setInputValue] = useState("");
    const [conversations, setConversations] = useState([]);
    const [activeConversation, setActiveConversation] = useState(null);

    const [socket] = useState(new SockJS('http:localhost:8080' + '/chat'))
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

    function connectToChat(selectedUser) {
        console.log("Connecting to chat...")
        stompClient.connect({}, function (frame) {
            console.log("Connected to: " + frame);
            stompClient.subscribe("topic/messages/" + selectedUser, function (response) {
                let data = JSON.parse(response.body);
                console.log(data.content, data.author_id, data.conversation_id)
            });
        });
    }

    function sendMessage(message) {
        stompClient.send("app/chat/" + activeConversation.userId, {}, JSON.stringify({
            content: message,
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
                                                              connectToChat(conversation.userId);
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
                                    <Tab.Content>
                                        {
                                            conversations.map((conversation, key) =>
                                                <Tab.Pane eventKey={conversation.conversationId} key={key}>
                                                    <button onKeyDown={() => connectToChat(activeConversation.userId)}></button>
                                                    <input className={"CHAT-input mt-4"} id={"message_sender_input"} type="text"
                                                           onChange={event => setInputValue(event.target.value)}
                                                           placeholder={"Write message"}
                                                           onKeyDown={ event => {
                                                               if (event.key === 'Enter') { sendMessage(inputValue) }
                                                            }}
                                                    />
                                                </Tab.Pane>
                                            )
                                        }
                                    </Tab.Content>
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
export default ConversationList;
