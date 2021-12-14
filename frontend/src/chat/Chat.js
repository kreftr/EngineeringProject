import React, {useState} from 'react';
import {Container, ListGroup, ListGroupItem} from "react-bootstrap";
import Message from "./Message"
import useAxiosGet from "../hooks/HttpRequests";
import {useParams} from "react-router-dom";
import Cookies from "js-cookie";
import axios from "axios";
// import "./Message.css"

export default function Chat() {
    let content = null
    let [inputValue, setInputValue] = useState("");
    const {conversation_id} = useParams();
    let messages = useAxiosGet(`http://localhost:8080/conversation/getAllMessages/${conversation_id}`)

    const sendNewMessage = (event) => {
        if (event.key === 'Enter') {
            let author_id = Cookies.get("userId")
            axios.post(`http://localhost:8080/conversation/addMessage/${conversation_id}/${author_id}/${inputValue}`)
            // TODO reload these messages without reloading whole page
        }
    }

    if (messages.data) {
        content =
            <Container className={"container"}>
                <ListGroup className={"list-group"}>
                    {
                        messages.data.map((message, key) =>
                            <ListGroupItem key={key}>
                                <Message message={message}/>
                            </ListGroupItem>
                        )
                    }
                </ListGroup>
                <div id={"message_sender"}>
                    <input id={"message_sender_input"} type="text"
                           onChange={event => setInputValue(event.target.value)} onKeyDown={sendNewMessage}/>
                </div>
            </Container>
    }

    return (
        <div>
            {content}
        </div>
    );
}
