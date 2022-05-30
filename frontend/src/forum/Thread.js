import axios from "axios";
import React, {useEffect, useState} from "react";
import {Button, Col, Container, Form, Image, ListGroup, ListGroupItem, Modal, Row} from "react-bootstrap";
import default_profile_picture from "../assets/images/default_profile_picture.jpg";
import Cookies from "js-cookie";
import {useParams} from "react-router-dom";

import Comment from "./Comment";
import {Alert} from "@mui/material";

function Thread(props) {

    const {id} = useParams();
    const [comments, setComments] = useState([]);
    const [post, setPost] = useState([]);
    
    const [text, setText] = useState(null);
    
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    
    const [responseMessage, setResponseMessage] = useState();
    const [responseCode, setResponseCode] = useState();
    
    function commentSubmit(e) {
        e.preventDefault();
        axios.post(`http://localhost:8080/comment/createCommentPost/${id}`, {
            "text": text
        }, {headers:{
            'Authorization':Cookies.get("authorization")
            }})
            .then(response => {
                setResponseCode(response.status)
                setResponseMessage(response.data.message)
                axios.get(`http://localhost:8080/post?id=${id}`, {
                }).then(response => {
                    setPost(response.data);
                    handleClose()
                }).catch(err => {
                        if (err.response.status === 404)  setResponseMessage("Post not found");
                        else setResponseMessage("Server error!");
                    })
                axios.get(`http://localhost:8080/comment/getPostComments/${id}`, {
                }).then(response => {
                    setComments(response.data)
                    handleClose()
                }).catch(err => {
                    console.log(err.response)
                })
            }).catch(err => {
                console.log(err.response)
                setResponseCode(err.response.status)
        })
    }
    
    useEffect( () => {
        //Load post informations (PostResponse)
        axios.get(`http://localhost:8080/post?id=${id}`, {
        }).then(response => {
                setPost(response.data);
            }).catch(err => {
                console.log(err.response)
            })
        axios.get(`http://localhost:8080/comment/getPostComments/${id}`, {
        }).then(response => {
            setComments(response.data)
        }).catch(err => {
            console.log(err.response)
        })
    }, [id])
    
    return (
        <Container className={"THREAD-container"}>
            <Row>
                <Col className={"col-4"}>
                    <a href={`/profile/${post.userId}`} className={"mr-3"}>
                        {post.userPhoto ?
                            <Image src={`http://localhost:8080/photo?filename=${post.userPhoto}`}
                                   roundedCircle={true}
                                   width="100px"
                                   height="100px"/>
                            :
                            <Image
                                src={default_profile_picture}
                                roundedCircle={true}
                                width="100px"
                                height="100px"/>
                        }
                    </a>
                </Col>
                <Col>
                    <h1>{post.title}</h1>
                </Col>
            </Row>
            <Row>
                <Col className={"col-4"}>
                    {post.userName}
                </Col>
                <Col>
                    {post.text}
                </Col>
            </Row>
            <Row>
                <Col className={"col-4"}>
                    {post.datee}
                </Col>
            </Row>
            <Row>
                {comments.length > 0 ?
                    <ListGroup className={"list-group"}>
                        {
                            comments.map((comment,key) =>
                                <ListGroupItem key={key}>
                                  <Comment comment={comment}/>
                                </ListGroupItem>
                            )
                        }
                    </ListGroup>
                    :
                    <h5 className={"mt-3"}>No comments found</h5>    
                }
            </Row>
            {Cookies.get("authorization") ?
                <>
                    <Button variant="primary" onClick={handleShow}>
                        Add Comment
                    </Button>
                </>
                :
                <></>
            }
            <Modal
                show={show}
                onHide={handleClose}
                backdrop="static"
            >
                <Modal.Header closeButton>
                    <Modal.Title></Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={commentSubmit}>
                        <Form.Group>
                            <Form.Label>Set text</Form.Label>
                            <Form.Control
                                as="textarea"
                                style={{ resize: 'none', height:'200px'}}
                                onChange={(e)=>{setText(e.target.value)}}
                                required>
                            </Form.Control>
                        </Form.Group>
                        <Row>
                            <Col className={"col-3"}/>
                            <Button variant="secondary" onClick={handleClose} className={"g"}>
                                Close
                            </Button>
                            <Button type="submit" variant="primary" className={"g"}>
                                Save Changes
                            </Button>
                        </Row>
                    </Form>
                    {responseMessage && responseCode === 200 ?
                        <Alert variant={"success"}>
                            <center>Nice comment!</center>
                        </Alert>
                        : responseMessage ?
                            <Alert variant={"danger"}>
                                <center>
                                    {responseMessage}
                                </center>
                            </Alert>
                            :
                            <></>
                    }
                </Modal.Body>
                <Modal.Footer>

                </Modal.Footer>
            </Modal>
        </Container>
    );
}

export default Thread;