import React, {useEffect, useState} from 'react';
import {Accordion, Badge, Col, Container, Form, ListGroup, ListGroupItem, Row} from "react-bootstrap";
import Cookies from "js-cookie";
import Friend from "./Friend";
import axios from "axios";
import Invitation from "./Invitation";
import {useParams} from "react-router-dom";


function FriendsList() {

    const {id} = useParams();

    const [searchTerm, setSearchTerm] = useState("");

    //Friends
    const [friends, setFriends] = useState([])
    const [friendsLoading, setFriendsLoading] = useState(true)
    const [friendsCode, setFriendsCode] = useState(null)

    //Pending
    const [pending, setPending] = useState([]);
    const [pendingLoading, setPendingLoading] = useState(true);
    const [pendingCode, setPendingCode] = useState(null);


    useEffect(() => {

        axios.get(`http://localhost:8080/friends/getAllFriends/${id}`, {
                headers: {
                    'Authorization': Cookies.get("authorization")
                }
            }).then(response => {
                setFriendsCode(response.status);
                setFriends(response.data);
                setFriendsLoading(false);
            })
            .catch(err => {
                setFriendsCode(err.response.status)
                setFriendsLoading(false);
            })

        if (Cookies.get("authorization") !== null && Cookies.get("userId") === id){
            axios.get(`http://localhost:8080/friends/getAllPending`, {
                headers: {
                    'Authorization': Cookies.get("authorization")
                }
            }).then(response => {
                setPendingCode(response.status);
                setPending(response.data);
                setPendingLoading(false);
            })
                .catch(err => {
                    setPendingCode(err.response.status)
                    setPendingLoading(false);
                })
        }

    },[id])


    return (
        <Container className={"container mt-5"}>
            { pending.length > 0 && !pendingLoading && pendingCode === 200 && id === Cookies.get("userId") ?
                <Row>
                    <Col className={"col-2"}></Col>
                    <Col className={"col-8"}>
                        <Accordion defaultActiveKey="0" flush={true}>
                            <Accordion.Item eventKey="0">
                                <Accordion.Header>
                                    <h3>
                                        Friend invitations <Badge bg="primary">{pending.length}</Badge>
                                    </h3>
                                </Accordion.Header>
                                <hr/>
                                <Accordion.Body>
                                    <ListGroup className={"list-group"}>
                                        {
                                            pending.map((friend, key) =>
                                                <ListGroupItem key={key}>
                                                    <Invitation friend={friend}/>
                                                    <hr/>
                                                </ListGroupItem>
                                            )
                                        }
                                    </ListGroup>
                                </Accordion.Body>
                            </Accordion.Item>
                        </Accordion>
                    </Col>
                    <Col className={"col-2"}></Col>
                </Row>
                :
                <></>
            }
            <Row>
                <Col className={"col-2"}></Col>
                <Col className={"col-8"}>
                    <Row>
                        <Col>
                            <h1>
                                Friends <Badge bg="secondary">{friends.length}</Badge>
                            </h1>
                        </Col>
                        <Col>
                            <Form>
                                <Form.Control type="text" placeholder="Search friend"
                                              onChange={(e) => setSearchTerm(e.target.value)}/>
                            </Form>
                        </Col>
                    </Row>
                    <hr/>
                    { friendsCode === 200 && !friendsLoading ?
                        <ListGroup className={"list-group"}>
                            {
                                friends.filter(f => searchTerm === "" ||
                                    f.username.toLowerCase().includes(searchTerm.toLowerCase()))
                                    .map((friend, key) =>
                                    <ListGroupItem key={key}>
                                        <Friend friend={friend}/>
                                        <hr/>
                                    </ListGroupItem>
                                )
                            }
                        </ListGroup>
                        :
                        <></>
                    }
                </Col>
                <Col className={"col-2"}></Col>
            </Row>
        </Container>
    );
}

export default FriendsList;
