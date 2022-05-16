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
    // const [messages, setMessages] = useState([])
    const [messagesLoading, setMessagesLoading] = useState(true)
    const [messagesCode, setMessagesCode] = useState(null)
    // const [stompClient, setStompClient] = useState(null)

    // useEffect(() => {
    //     setStompClient(Stomp.over('ws://localhost:8080/chat'))
    // }, [])
    //
    // useEffect(() => {
    //     if (stompClient !== null) {
    //         stompClient.connect({}, function() {
    //             stompClient.subscribe('/topic/messages', function(messageOutput) { // TODO poprawic na /topic/messages/conversation_id
    //                 showNewMessage(messageOutput.body)
    //             })
    //         })
    //     }
    // }, [stompClient])


    //TODO uncomment new code
    // let stompClient;
    // let selectedUser;
    // function connectToChat(username) {
    //     console.log("connecting to chat...")
    //     let socket = new SockJS('http:localhost:8080' + '/chat');
    //     stompClient = Stomp.over(socket);
    //     stompClient.connect({}, function (frame) {
    //         console.log("connected to: " + frame);
    //         stompClient.subscribe("topic/messages/" + username, function (response) {
    //             let data = JSON.parse(response.body);
    //             console.log(data);
    //             render(data.content, data.conversation_id, data.author_id)
    //         });
    //     });
    // }
    //
    // function render(message, userName, conversation) {
    //     console.log(message, userName, conversation)
    // }
    //
    // function sendMsg(username, message, conversation) {
    //     stompClient.send("app/chat/" + selectedUser, {}, JSON.stringify({
    //         content: message,
    //         conversation_id: conversation,
    //         author_id: username
    //     }));
    // }
    //
    // function sendMessage(message) {
    //     let username = "asd";
    //     let conversation = 1;
    //     sendMsg(username, message, conversation)
    // }





    // function showNewMessage(messageOutput) {
    //     console.log(messageOutput)
    // }

    // const sendNewMessage = (event) => {
    //     if (event.key === 'Enter') {
    //         stompClient.send("/app/chat", {}, JSON.stringify({  // TODO poprawic na /app/chat/conversation_id
    //             'conversation_id': props.conversation.conversationId,
    //             'author_id': Cookies.get("userId"),
    //             'text': inputValue
    //         }))
    //         document.getElementById("message_sender_input").value = ""  // clear input after
    //     }
    // }

    return (
        // <input className={"CHAT-input mt-4"} id={"message_sender_input"} type="text"
        //        onChange={event => setInputValue(event.target.value)} onKeyDown={sendMessage}
        //        placeholder={"Write message"}/>
        <></>


        // <Container className={"container"}>
        //     { messagesCode === 200 && !messagesLoading ?
        //         <div>
        //             {/*<ListGroup className={"list-group CHAT-window"}>*/}
        //             {/*    {*/}
        //             {/*        messages.map((message, key) =>*/}
        //             {/*            <ListGroupItem key={key}>*/}
        //             {/*                <Message message={message}/>*/}
        //             {/*            </ListGroupItem>*/}
        //             {/*        )*/}
        //             {/*    }*/}
        //             {/*</ListGroup>*/}
        //             <div id={"message_sender"}>
        //                 <input className={"CHAT-input mt-4"} id={"message_sender_input"} type="text"
        //                        onChange={event => setInputValue(event.target.value)} onKeyDown={sendMessage}
        //                        placeholder={"Write message"}/>
        //             </div>
        //         </div>
        //         :
        //         <></>
        //     }
        // </Container>
    );
}
export default Chat;
