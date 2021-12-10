import {useParams} from "react-router-dom";
import {Col, Row, Container, Form, Button, Alert} from "react-bootstrap";
import React, {useState} from "react";
import axios from "axios";

import "./Recovery.css"


function Recovery(){

    const {token} = useParams();

    const [newPassword, setNewPassword] = useState();
    const [confirmNewPassword, setConfirmNewPassword] = useState();
    const [responseMessage, setResponseMessage] = useState();
    const [responseCode, setCode] = useState();

    async function resetPassword(e){
        e.preventDefault();
        if (newPassword === confirmNewPassword) {
            await axios.post(`http://localhost:8080/recovery/reset?token=${token}`, {
                "newPassword": newPassword
            }).then(res => {
                setCode(res.status)
                setResponseMessage(res.data.message)
            }).catch(err => {
                if (err.response.status === 400) {
                    setResponseMessage("*" + err.response.data.error)
                } else if (err.response.status === 403) {
                    setResponseMessage("*" + err.response.data.message)
                } else {
                    setResponseMessage("SERVER ERROR")
                }
                setCode(err.response.status)
            })
        }
        else {
            setCode(404)
            setResponseMessage("*Passwords are not the same")
        }
    }


    return(
        <Container className={"RECOVERY-container"}>
            <Row>
                <Col className={"col-4"}></Col>
                <Col className={"col-4"}>
                    <Form className={"mb-4"} onSubmit={resetPassword}>
                        <h3>Reset password</h3>
                        <Form.Group className="mb-3 mt-4" controlId="formPassword">
                            <Form.Label>New password</Form.Label>
                            <Form.Control type="password" placeholder="Enter new password" value={newPassword}
                                          onChange={(e) => setNewPassword(e.target.value)} required/>
                            <Form.Text className="text-muted">
                                *The password must be 8 to 24 characters long, including at least one uppercase and lowercase letter, number, and special character.
                            </Form.Text>
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formPassword">
                            <Form.Label>Confirm new password</Form.Label>
                            <Form.Control type="password" placeholder="Confirm new password" value={confirmNewPassword}
                                          onChange={(e) => setConfirmNewPassword(e.target.value)} required/>
                            <Form.Text className="text-muted">
                                *Passwords must be the same
                            </Form.Text>
                        </Form.Group>
                        <Button className={"SETTINGS-save-button"}
                                variant="danger" type="submit">
                            Reset password
                        </Button>
                    </Form>
                    { responseMessage && responseCode === 200 ?
                        <Alert variant={"success"}>
                            <center>
                                {responseMessage}
                                <br/>
                                <Alert.Link href={"/login"}>LOG IN</Alert.Link>
                            </center>
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
                </Col>
                <Col className={"col-4"}></Col>
            </Row>
        </Container>
    )

}
export default Recovery