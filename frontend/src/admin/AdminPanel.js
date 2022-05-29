import React, {useEffect, useState} from "react";
import {Button, Col, Container, Form, Row, Tab, Tabs} from "react-bootstrap";

import "./AdminPanel.css"
import Reports from "./Reports";
import axios from "axios";
import Cookies from "js-cookie";
import ProfileAdminSettings from "./ProfileAdminSettings";
import ProjectAdminSettings from "./ProjectAdminSettings";


function AdminPanel() {

    const [reports, setReports] = useState();
    const [loaded, setLoaded] = useState(false);

    const [userId, setUserId] = useState(1);
    const [loadUser, setLoadUser] = useState(false);

    const [projectId, setProjectId] = useState(1);
    const [loadProject, setLoadProject] = useState(false);

    useEffect(() => {
        axios.get(`http://localhost:8080/report`,
            {headers: {'Authorization': Cookies.get("authorization")}})
            .then(response =>{
                setReports(response.data)
                setLoaded(true)
            })
            .catch(err => {
                window.alert(err.response.data.message)
            })
    },[])


    return(
        <Container className={"ADMIN-container"}>
            { loaded ?
                <Row>
                    <Col className={"col-1"}></Col>
                    <Col className={"col-10"}>
                        <Tabs defaultActiveKey="Reports" className="mb-5" fill>
                            <Tab eventKey="Reports" title="Reports">
                                <Reports reports={reports}/>
                            </Tab>
                            <Tab eventKey="Profiles" title="Profiles">
                                {!loadUser ?
                                    <Row>
                                        <Col/>
                                        <Col>
                                            <Form>
                                                <Form.Group className="mt-2">
                                                    <Form.Label>User ID</Form.Label>
                                                    <Form.Control type={"number"} value={userId}
                                                                  onChange={(e)=>{setUserId(Number(e.target.value));}} min={1}/>
                                                </Form.Group>
                                                <center>
                                                    <Button onClick={(e)=>{setLoadUser(true);}} className={"mt-2"} variant={"primary"}>Load</Button>
                                                </center>
                                            </Form>
                                        </Col>
                                        <Col/>
                                    </Row>
                                    :
                                    <>
                                        <center>
                                            <h2>User ID: {userId}</h2>
                                        </center>
                                        <ProfileAdminSettings id={userId}/>
                                        <center>
                                            <Button className={"mb-4"} variant={"danger"} onClick={(e)=>{setLoadUser(false);}}>Cancel</Button>
                                        </center>
                                    </>
                                }
                            </Tab>
                            <Tab eventKey="Projects" title="Projects">
                                {!loadProject ?
                                    <Row>
                                        <Col/>
                                        <Col>
                                            <Form>
                                                <Form.Group className="mt-2">
                                                    <Form.Label>Project ID</Form.Label>
                                                    <Form.Control type={"number"} value={projectId}
                                                                  onChange={(e)=>{setProjectId(Number(e.target.value));}} min={1}/>
                                                </Form.Group>
                                                <center>
                                                    <Button onClick={(e)=>{setLoadProject(true);}} className={"mt-2"} variant={"primary"}>Load</Button>
                                                </center>
                                            </Form>
                                        </Col>
                                        <Col/>
                                    </Row>
                                    :
                                    <>
                                        <center>
                                            <h2>Project ID: {projectId}</h2>
                                        </center>
                                        <ProjectAdminSettings id={projectId}/>
                                        <center>
                                            <Button className={"mb-4"} variant={"danger"} onClick={(e)=>{setLoadProject(false);}}>Cancel</Button>
                                        </center>
                                    </>
                                }
                            </Tab>
                        </Tabs>
                    </Col>
                    <Col className={"col-1"}></Col>
                </Row>
                :
                <></>
            }
        </Container>
    );
}
export default AdminPanel;