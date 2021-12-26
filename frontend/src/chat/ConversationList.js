import React, {useEffect, useState} from 'react';
import {Col, Container, Nav, Row, Tab} from "react-bootstrap";
import Cookies from "js-cookie"
import axios from "axios"
import Conversation from "./Conversation"
import Chat from "./Chat";
import {useParams} from "react-router-dom";


function ConversationList() {

    const {id} = useParams();

    const [conversations, setConversations] = useState([]);
    const [conversation, setConversation] = useState(null);

    useEffect(() => {

        axios.get(`http://localhost:8080/conversation/getAllUserConversations`, {
            headers: {
                'Authorization': Cookies.get("authorization")
            }
        }).then((response) => {
            setConversations(response.data);
            console.log(conversations)
        }).catch(err => {
            console.log(err.response)
        });

    }, []);

    return (
            <Container className={"mt-5 container"}>
                { conversations ?
                    <Tab.Container defaultActiveKey={id}>
                        <Row>
                            <Col className={"col-4"}>
                                <Nav variant="pills" className="flex-column">
                                    {
                                        conversations.map((conversation, key) =>
                                            <Nav.Item>
                                                <Nav.Link eventKey={conversation.conversationId}
                                                          onClick={() => {setConversation(conversation);}}>
                                                    <Conversation conversation={conversation}/>
                                                </Nav.Link>
                                            </Nav.Item>
                                        )
                                    }
                                </Nav>
                            </Col>
                            <Col className={"col-8"}>
                                { conversation !== null ?
                                    <Tab.Content>
                                        {
                                            conversations.map((conversation, key) =>
                                                <Tab.Pane eventKey={conversation.conversationId}>
                                                    <Chat conversation={conversation}/>
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
