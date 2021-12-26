import React from 'react';
import {Button, Col, Image, Row} from "react-bootstrap";
import Cookies from "js-cookie";
import axios from "axios";
import "./Friend.css"
import default_profile_picture from "../assets/images/default_profile_picture.jpg"
import {FaEnvelope, FaUserMinus} from "react-icons/fa";
import {useParams} from "react-router-dom";

function Friend(props) {

    const {id} = useParams();

    function removeFriend(){
        axios.delete(`http://localhost:8080/friends/deleteById/${props.friend.id}`, {
            headers: {
                'Authorization': Cookies.get("authorization")
            }
        }).then(response => {
            window.location.reload();
        }).catch(err => {
            console.log(err.response)
        })
    }


    return (
        <Row className={"INVITATION-container"}>
            <Col className={"col-2"}>
                <a href={`/profile/${props.friend.id}`} style={{ textDecoration: 'none' }}>
                    { props.friend.profilePhoto ?
                        <Image className={"ml-3"} src={`http://localhost:8080/photo?filename=${props.friend.profilePhoto}`}
                               roundedCircle={true} width="100px" height="100px"/>
                        :
                        <Image className={"ml-3"} src={default_profile_picture}
                               roundedCircle={true} width="100px" height="100px"/>
                    }
                </a>
            </Col>
            <Col className={"col-4"}>
                <a href={`/profile/${props.friend.id}`} style={{ textDecoration: 'none' }}>
                    <div className={"ml-3"}>
                        <h2>{props.friend.username}</h2>
                        { props.friend.name || props.friend.surname ?
                            <h4 className={"INVITATION-name-surname"}>{props.friend.name} {props.friend.surname}</h4>
                            :
                            <></>
                        }
                    </div>
                </a>
            </Col>
            <Col className={"col-6"}>
                {Cookies.get("userId") === id ?
                    <div className={"INVITATION-buttons-container"}>
                        <Button type="button" variant="primary FRIEND-button"
                                href={`/conversations/${props.friend.conversationId}`} size={"lg"}>
                            <FaEnvelope className={"mr-2"} size={50}/>
                            <h4>Chat</h4>
                        </Button>
                        <Button variant="danger FRIEND-button" size={"lg"} onClick={removeFriend}>
                            <FaUserMinus className={"mr-2"} size={50}/>
                            <h4>Remove</h4>
                        </Button>
                    </div>
                    :
                    <></>
                }
            </Col>
        </Row>
    );
}
export default Friend;