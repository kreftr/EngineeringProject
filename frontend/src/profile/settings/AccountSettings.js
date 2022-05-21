import {Alert, Button, Col, Form, Row, Modal} from "react-bootstrap";
import React, {useEffect, useRef, useState} from "react";
import axios from "axios";
import Cookies from "js-cookie";
import {Checkbox} from "@mui/material";


function AccountSettings(){

    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const [newPassword, setNewPassword] = useState();
    const [confirmNewPassword, setConfirmNewPassword] = useState();
    const [responseMessage, setResponseMessage] = useState();
    const [responseCode, setCode] = useState();
    const [emailNotifications, setEmailNotifications] = useState(false);
    const [isNotificationLoading, setIsNotificationLoading] = useState(false);

    useEffect(() => {
        axios.get("http://localhost:8080/user/getEmailNotification", {headers:{
                'Authorization': Cookies.get("authorization")
            }}).then(response => {
                setEmailNotifications(response.data);

        }).catch(err => {
            console.log(err.response)
        }).finally(() => {
                setIsNotificationLoading(true)
            })
    },[]);

    useEffect(() => {
        axios.post(`http://localhost:8080/user/updateEmailNotification/${emailNotifications}`, {}, {
            headers: {
                'Authorization': Cookies.get("authorization")
            }
        })
            .catch(err => {
                console.log(err.response)
            })
    },[emailNotifications]);

    async function handleSubmit(e){
        e.preventDefault();

        if (newPassword === confirmNewPassword) {

            await axios.post("http://localhost:8080/user/changePassword", {
                "newPassword": newPassword
            }, {
                headers: {
                    'Authorization': Cookies.get("authorization")
                }
            }).then(res => {
                setCode(res.status)
                setResponseMessage(res.data.message)
                console.log(res)
            }).catch(err => {
                setCode(err.response.status)
                console.log(err.response)
                if (err.response.status === 400) {
                    setResponseMessage("*"+err.response.data.error)
                } else {
                    setResponseMessage("SERVER ERROR")
                }
            })
        }
        else {
            setCode(404)
            setResponseMessage("*Passwords are not the same")
        }
    }

    async function deleteAccount(e){
        e.preventDefault();

        await axios.post("http://localhost:8080/user/deleteAccount", null, {
            headers: {
                'Authorization': Cookies.get("authorization")
            }
        }).then(res => {
            Cookies.remove("userId")
            Cookies.remove("authorization")
            window.location.replace("/login")
        }).catch(err => {
            console.log(err.response)
        })
    }

    return(

        <>
            <h3>Notifications</h3>
            { isNotificationLoading ?
                <>
                    <Checkbox id={"ACCOUNT_SETTINGS-notifications-email"}
                              checked={emailNotifications} onChange={() => {setEmailNotifications(!emailNotifications)}}/>
                    <label id={"ACCOUNT_SETTINGS-notifications-email-text"}>Email notifications</label>
                </>
                :
                <></>
            }

            <Form onSubmit={handleSubmit}>
                <h3>Change password</h3>
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
                <Row>
                    <Col></Col>
                    <Col className={"SETTINGS-save-button-col"}>
                        <Button className={"SETTINGS-save-button"}
                                variant="primary" type="submit">
                            Change password
                        </Button>
                    </Col>
                    <Col></Col>
                </Row>
                <Row className={"mt-4 mb-4"}>
                    <Col className={"col-3"}></Col>
                    <Col className={"col-6"}>
                        { responseMessage && responseCode === 200 ?
                            <Alert variant={"success"}>
                                <center>
                                    {responseMessage}
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
                    <Col className={"col-3"}></Col>
                </Row>
                <h3>Delete account</h3>
                <Row className={"mt-4"}>
                    <Col className={"SETTINGS-save-button-col"}>
                        <Button className={"SETTINGS-save-button"} variant="danger" onClick={handleShow}>
                            Delete account
                        </Button>
                        <Modal show={show} onHide={handleClose}>
                            <Modal.Header closeButton>
                                <Modal.Title>Account deletion</Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                                <h5>Are you sure you want to delete your account?</h5>
                                Deleting your account is permanent and will remove all content including comments,
                                avatars and profile settings.
                            </Modal.Body>
                            <Modal.Footer>
                                <Button variant="secondary" onClick={handleClose}>
                                    Close
                                </Button>
                                <Button variant="danger" onClick={deleteAccount}>
                                    Delete
                                </Button>
                            </Modal.Footer>
                        </Modal>
                    </Col>
                    <Col></Col>
                    <Col></Col>
                </Row>
            </Form>
        </>
    )
}

export default AccountSettings