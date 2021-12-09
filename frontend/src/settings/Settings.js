import React, {useEffect, useState} from "react";
import {Container, Form, Row, Col, Image, Button, Tabs, Tab, Alert} from "react-bootstrap";
import default_profile_picture from "../assets/images/default_profile_picture.jpg"
import axios from "axios";
import Cookies from "js-cookie"

import "./Settings.css"
import AccountSettings from "./AccountSettings";

function Settings(){


    const [profilePhoto, setProfilePhoto] = useState();
    const [name, setName] = useState();
    const [surname, setSurname] = useState();
    const [bio, setBio] = useState();

    const [responseCode,setCode] = useState();
    const [loadingContent, setLoading] = useState(true);

    const [editCode, setEditCode] = useState(200)
    const [editMessage, setEditMessage] = useState()

    useEffect(() => {

        if (!Cookies.get("userId")) window.location.replace("/");

        axios.get(`http://localhost:8080/profile?id=${Cookies.get("userId")}`)
            .then(response => {
                setCode(response.status);
                setProfilePhoto(response.data.profile_photo);
                setName(response.data.name);
                setSurname(response.data.surname);
                setBio(response.data.bio);
                setLoading(false);
            })
            .catch(err => {
                setCode(err.response.status)
                setLoading(false);
            })
    },[]);


    async function handleProfileEdit(e){
        e.preventDefault();

        let photo = document.getElementById("fileChooser").files[0]
        let bodyFormData = new FormData();
        bodyFormData.append("editRequest", new Blob(
            [JSON.stringify({"name":name, "surname":surname, "bio":bio})],
            { type: "application/json"}))
        bodyFormData.append("profilePhoto", photo);

        await axios.post("http://localhost:8080/profile", bodyFormData, {headers:{
                'Authorization': Cookies.get("authorization")
        }}).then(res => {
            setEditCode(res.status)
            window.location.reload();
        }).catch(err => {
            setEditCode(err.response.status)
            if (err.response.status === 400) setEditMessage("*"+err.response.data.error)
            else setEditMessage("SERVER ERROR!")
        })
    }


    return(
        <Container className={"SETTINGS-profile-container"}>
            {responseCode === 200 && !loadingContent ?
                <Row>
                    <Col className={"col-2"}></Col>
                    <Col className={"col-8"}>
                        <Tabs defaultActiveKey="Profile details" className="mb-5">
                            <Tab eventKey="Profile details" title="Profile details">
                                <Form onSubmit={handleProfileEdit}>
                                    <Row className={"mt-3 mb-3"}>
                                        <Col className={"SETTINGS-info-col"}>
                                            {!profilePhoto  ?
                                                <Image className={"SETTINGS-profile-pic"} src={default_profile_picture} roundedCircle={true}/>
                                                :
                                                <Image className={"SETTINGS-profile-pic"}
                                                       src={`http://localhost:8080/photo?filename=${profilePhoto}`}
                                                       roundedCircle={true}/>
                                            }
                                            <Form.Group className="mb-3 mt-3">
                                                <Form.Control type="file" size="sm" accept="image/*" id="fileChooser"
                                                              onChange={(e) => console.log(e.target.value)}/>
                                            </Form.Group>
                                        </Col>
                                        <Col>
                                            <Row className={"mb-3"}>
                                                <Col>
                                                    <Form.Label>Name</Form.Label>
                                                    <Form.Control value={name} maxLength={20}
                                                                  onChange={(e) => setName(e.target.value)}/>
                                                </Col>
                                                <Col>
                                                    <Form.Label>Surname</Form.Label>
                                                    <Form.Control value={surname} maxLength={20}
                                                                  onChange={(e) => setSurname(e.target.value)}/>
                                                </Col>
                                            </Row>
                                            <Form.Group className="mb-3">
                                                <Form.Label>Description</Form.Label>
                                                <Form.Control  className={"SETTINGS-textarea"} as="textarea" maxLength={250} rows={6} value={bio}
                                                               onChange={(e) => setBio(e.target.value)}/>
                                            </Form.Group>
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Col></Col>
                                        <Col className={"SETTINGS-save-button-col"}>
                                            <Button className={"SETTINGS-save-button"}
                                                    variant="primary" type="submit">
                                                Save
                                            </Button>
                                        </Col>
                                        <Col></Col>
                                    </Row>
                                    {editCode !== 200 ?
                                        <Row className={"mt-4"}>
                                            <Col></Col>
                                            <Col className={"SETTINGS-save-button-col"}>
                                                <Alert variant={"danger"}>
                                                    <center>
                                                        {editMessage}
                                                    </center>
                                                </Alert>
                                            </Col>
                                            <Col></Col>
                                        </Row>
                                        :
                                        <></>
                                    }
                                </Form>
                            </Tab>
                            <Tab eventKey="profile" title="Account settings">
                                <AccountSettings/>
                            </Tab>
                        </Tabs>
                    </Col>
                    <Col className={"col-2"}></Col>
                </Row>
                    :
                    <></>
            }
        </Container>
    );
}
export default Settings;