import axios from "axios";
import {Button, Container, Col, Form, ListGroup, ListGroupItem, Modal, Row} from "react-bootstrap";
import {useEffect, useState} from "react";
import "./Forum.css";
import Post from "./Post";
import Cookies from "js-cookie";
import {Alert} from "@mui/material";

function Forum(props){
    const [posts, setPosts] = useState([]);
    
    //Post variable manipulation
    const [title, setTitle] = useState(null);
    const [text, setText] = useState(null);
    
    //when page loads don't show the modal instantaneously
    const [show, setShow] = useState(false);
    //when activated stop showing
    const handleClose = () => setShow(false);
    //when activated start showing
    const handleShow = () => setShow(true);

    const [responseMessage, setResponseMessage] = useState();
    const [responseCode, setResponseCode] = useState();
    
    //get today's date
    //var today = new Date();
    //var date =  '-' + today.getDate() + '-' + (today.getMonth()+1) + today.getFullYear();
    function forumSubmit(e) {
        e.preventDefault();
        axios.post("http://localhost:8080/post/addPost", {
            "title": title,
            "text": text
        }, {headers:{
                'Authorization':Cookies.get("authorization")
            }})
            .then(response => {
                setResponseCode(response.status)
                setResponseMessage(response.data.message)
                axios.get(`http://localhost:8080/post/getRecentPosts`, {
                    headers: {
                        'Authorization': Cookies.get("authorization")
                    }
                }).then(response => {
                    setPosts(response.data)
                    handleClose()
                }).catch(err => {
                    console.log(err.response)
                })
            })
            .catch(err => {
                console.log(err.response)
                setResponseCode(err.response.status)
                if(err.response.status === 500) {
                    setResponseMessage("*" + err.response.data.error)
                }
                else setResponseMessage("*" + err.response.data.error)
                
            })
    }
    
    useEffect(() => {
        axios.get(`http://localhost:8080/post/getRecentPosts`, {
            headers: {
                'Authorization': Cookies.get("authorization")
            }
        }).then(response => {
            setPosts(response.data)
        }).catch(err => {
            console.log(err.response)
        })
    }, [])
    
    return(
        <Container className={"FORUM-container"}>
            <Row>
                <Button variant="primary FORUM-button" onClick={handleShow}>
                    Add Post
                </Button>
                <Button variant="danger FORUM-button" hidden={true}>
                    Delete Post
                </Button>
            </Row>
            <Row>
                <Col className={"col-4"}>
                    <h4>
                        Posts
                    </h4>
                </Col>
                <Col className={"col-4"}>
                    <h4>
                        Author
                    </h4>
                </Col>
                <Col className={"col-2"}>
                    <h4>
                        Date
                    </h4>
                </Col>
            </Row>
            <Row>
                {posts.length > 0 ?
                    <ListGroup className={"list-group"}>
                        {
                            posts.map((post, key) =>
                                <ListGroupItem key={key}>
                                    <Post post={post}/>
                                </ListGroupItem>
                            )
                        }
                    </ListGroup>
                    :
                    <h3 className={"mt-3"}>No posts found</h3>
                }
            </Row>
            
            <Row>
                <Col className={"col-3"}/>
                <Col className={"col-3"}>
                </Col>
            </Row>
            <Modal 
                show={show} 
                onHide={handleClose}
                backdrop="static"
            >
                <Modal.Header closeButton>
                    <Modal.Title></Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form onSubmit={forumSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Set title</Form.Label>
                            <Form.Control type="text" placeholder="Enter title" 
                                onChange={(e)=>{setTitle(e.target.value)}} 
                                required>
                            </Form.Control>
                            
                        </Form.Group>
                        <Form.Group>
                            <Form.Label>Set text</Form.Label>
                            <Form.Control 
                                as="textarea"
                                style={{ resize: 'none', height:'200px'}}
                                onChange={(e)=>{setText(e.target.value)}}
                                      required/>
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
                            <center> Nice post!</center>
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

export default Forum;