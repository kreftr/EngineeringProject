import React from 'react';
import {Container, Image, ListGroup, ListGroupItem} from "react-bootstrap";
import Message from "./Message"
import useAxiosGet from "../hooks/HttpRequests";
// import "./Chat.css"

export default function Chat() {
    const url = "http://localhost:8080/conversation/getAllMessages/1"
    let messages = useAxiosGet(url)
    let content = null

    if (messages.error) {
        content = <p>
            Error occured
        </p>
    }

    if (messages.loading) {
        content = <p>
            Loading, please wait...
        </p>
    }

    // TODO put avatar on the left of the image
    // TODO replace placeholder name with actual name from conversation
    if (messages.data) {
        content =
            <Container className={"container"}>
                <ListGroup className={"list-group"}>
                    {
                        messages.data.map((message, key) =>
                            <ListGroupItem key={key}>
                                <div className={"message"}><Image className={"message_avatar"}/>
                                    <Message message={message}/>
                                </div>
                            </ListGroupItem>
                        )
                    }
                </ListGroup>
                <div id={"message_sender"}><input id={"message_sender_input"} type="text"/></div>
            </Container>
    }

    return (
        <div>
            {content}
        </div>
    );
}
