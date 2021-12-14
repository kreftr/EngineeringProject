import React from 'react';
import {Container, ListGroup, ListGroupItem} from "react-bootstrap";
import Cookies from "js-cookie"
import useAxiosGet from "../hooks/HttpRequests";
import Conversation from "./Conversation"
// import "./Message.css"

export default function ConversationList(props) {
    let content = null
    const cookie_user_id = Number(Cookies.get("userId"))
    let conversations = useAxiosGet(`http://localhost:8080/conversation/getAllUserConversations/${cookie_user_id}`)

    if (conversations.data) {
        content =
            <Container className={"container"}>
                <ListGroup className={"list-group"}>
                    {
                        conversations.data.map((conversation, key) =>
                            <ListGroupItem key={key}>
                                <Conversation conversation={conversation}/>
                            </ListGroupItem>
                        )
                    }
                </ListGroup>
            </Container>
    }

    return (
        <div>
            {content}
        </div>
    );
}
