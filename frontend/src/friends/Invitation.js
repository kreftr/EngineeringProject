import React from 'react';
import {Button, Col, Image, Row} from "react-bootstrap";
import default_profile_picture from "../assets/images/default_profile_picture.jpg"
import "./Friend.css"
import Cookies from "js-cookie";
import "./Invitation.css"
import axios from "axios";


function Invitation(props) {


    function accept(){
        axios.post(`http://localhost:8080/friends/acceptFriend/${props.friend.id}`,{}, {
            headers: {
                'Authorization': Cookies.get("authorization")
            }
        }).then(response => {
            window.location.reload();
        }).catch(err => {
            console.log(err.response)
        })
    }

    function reject(){
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
                    <div className={"INVITATION-buttons-container"}>
                        <Button variant="success INVITATION-button" onClick={accept} size={"lg"}>Accept</Button>
                        <Button variant="danger INVITATION-button" onClick={reject} size={"lg"}>Reject</Button>
                    </div>
                </Col>
        </Row>
    );
}
export default Invitation;
