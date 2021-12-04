import React, {useEffect, useLayoutEffect, useState, Suspense} from 'react';
import {Container, Nav, Image, ListGroup, ListGroupItem, Button, Spinner, Row, Col} from "react-bootstrap";
import axios, { AxiosRequestConfig } from "axios";
// import "./Chat.css"

function Chat() {
    // TODO add conversation id to baseURL and delete it from requests below
    const baseURL = "http://localhost:8080/conversation/";

    const [loadingContent, setLoading] = useState(true);
    const [messages, setMessages] = useState([])
    const [responseStatus, setStatus] = useState();

    // async function renderMessages() {
    //     try {
    //         let res = await axios.get(baseURL + "getAllMessages/1");
    //         let messages = res.data;
    //         return  messages.map((message, i) => {
    //             return (
    //                 <li key={i} className="list-group-item">{message.content}</li>
    //             );
    //         });
    //     } catch (err) {
    //         console.log(err);
    //     }
    // }

    useEffect(() => {
        axios.get("http://localhost:8080/conversation/getAllMessages/1")
            .then(response => {
                setStatus(response.status);
                setMessages(response.data);
                setLoading(false);
            })
            // .catch(err => {
            //     setStatus(err.response.status)
            //     setLoading(false);
            // })
    },[]);

    const mapMessages = () => {
        if (!loadingContent) {
            {messages.map((message) => (
                        <Col>{message.content}</Col>
                    ))}
        }
    }


    return (
        <Container>
            <Row>
                {
                    messages
                }
            </Row>
            }
        </Container>

        // <ul>
        //     {messages.map((message) => (
        //         <li>{message.content}</li>
        //     ))}
        // </ul>



        // <>
        //     {/*{ responseStatus === 200 && !loadingContent ?*/}
        //         <Container>
        //             {messages.length}
        //             {/*{responseStatus}*/}
        //             {/*{messages[0]}*/}
        //         </Container>
        //     {/*}*/}
        //
        //     {/*/!*<Container className={"container"}>*!/*/}
        //     {/*/!*    <ListGroup id={"conversation_list"}>*!/*/}
        //     {/*/!*        <ListGroupItem>*!/*/}
        //
        //     {/*/!*        </ListGroupItem>*!/*/}
        //     {/*/!*        <ListGroupItem>*!/*/}
        //
        //     {/*/!*        </ListGroupItem>*!/*/}
        //     {/*/!*        <ListGroupItem>*!/*/}
        //
        //     {/*/!*        </ListGroupItem>*!/*/}
        //     {/*/!*        <ListGroupItem>*!/*/}
        //
        //     {/*/!*        </ListGroupItem>*!/*/}
        //     {/*/!*    </ListGroup>*!/*/}
        //     {/*/!*    <ListGroup id={"message_list"} className={"list-group"}>*!/*/}
        //     {/*/!*        <ListGroupItem>*!/*/}
        //     {/*/!*            <div className={"message"}><Image className={"message_avatar"}/>*!/*/}
        //     {/*/!*                <p className={"message_nickname"}></p>*!/*/}
        //     {/*/!*                <p className={"message_date"}>30.11.2021</p>*!/*/}
        //     {/*/!*                <p className={"message_content"}>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut*!/*/}
        //     {/*/!*                    ullamcorper blandit sem sit amet molestie. Vivamus lorem orci, efficitur vel convallis vitae,*!/*/}
        //     {/*/!*                    lacinia id orci. Integer vitae diam magna. Vivamus id sapien sed nisl aliquet feugiat vel a*!/*/}
        //     {/*/!*                    enim. Nunc elementum tellus ut eros efficitur, quis venenatis purus ullamcorper. Phasellus sit*!/*/}
        //     {/*/!*                    amet augue auctor, eleifend nisi id, porta neque. Etiam lacinia a mauris vitae lobortis.<br/>*!/*/}
        //     {/*/!*                </p>*!/*/}
        //     {/*/!*            </div>*!/*/}
        //     {/*/!*        </ListGroupItem>*!/*/}
        //     {/*/!*        <ListGroupItem>*!/*/}
        //
        //     {/*/!*        </ListGroupItem>*!/*/}
        //     {/*/!*    </ListGroup>*!/*/}
        //     {/*/!*    <div id={"message_sender"}><input id={"message_sender_input"} type="text"/></div>*!/*/}
        //     {/*/!*</Container>*!/*/}
        // </>
    );
}

export default Chat;
