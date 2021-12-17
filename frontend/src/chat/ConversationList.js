import React, {useEffect} from 'react';
import {Container, ListGroup, ListGroupItem} from "react-bootstrap";
import Cookies from "js-cookie"
import axios from "axios"
import Conversation from "./Conversation"


export default function ConversationList() {
    const cookie_user_id = Number(Cookies.get("userId"))
    const [conversations, setConversations] = React.useState([]);

    useEffect(() => {
        axios.get(`http://localhost:8080/conversation/getAllUserConversations/${cookie_user_id}`).then((response) => {
            setConversations(response.data);
        });
    }, [cookie_user_id]);

    return (
        <Container className={"container"}>
            <ListGroup className={"list-group"}>
                {
                    conversations.map((conversation, key) =>
                        <ListGroupItem key={key}>
                            <Conversation conversation={conversation}/>
                        </ListGroupItem>
                    )
                }
            </ListGroup>
        </Container>
    );
}
