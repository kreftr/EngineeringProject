import axios from "axios";
import Cookies from "js-cookie";
import {Button, Col, Form, Modal, Row} from "react-bootstrap";
import React, {useState} from "react";
import {useParams} from "react-router-dom";



function DeleteProjectSettings(){


    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const {id} = useParams()

    async function deleteProject(e){
        e.preventDefault();

        await axios.delete(`http://localhost:8080/project/deleteProject/${id}`,{headers:{'Authorization': Cookies.get("authorization")}
        })
            .then(res => {
            window.location.replace("/projects")
        }).catch(err => {
            console.log(err.response)
        })
    }

    return(
        <Form>
            <h3>Delete project</h3>
            <Row className={"mt-4"}>
                <Col className={"SETTINGS-save-button-col"}>
                    <Button className={"SETTINGS-save-button"} variant="danger" onClick={handleShow}>
                        Delete project
                    </Button>
                    <Modal  show={show} onHide={handleClose}>
                        <Modal.Header closeButton>
                            <Modal.Title>Project deletion</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                            <h5>Are you sure you want to delete your project?</h5>
                            Deleting your project is permanent and will remove all content.
                        </Modal.Body>
                        <Modal.Footer>
                            <Button variant="secondary" onClick={handleClose}>
                                Close
                            </Button>
                            <Button variant="danger" onClick={deleteProject}>
                                Delete
                            </Button>
                        </Modal.Footer>
                    </Modal>
                </Col>
                <Col></Col>
                <Col></Col>
            </Row>
        </Form>
    )

}
export default DeleteProjectSettings;