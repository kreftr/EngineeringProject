import {Button, Col, Form, ListGroup, Modal, Row, Tab} from "react-bootstrap";

import "./AdminPanel.css"
import axios from "axios";
import Cookies from "js-cookie";
import React, {useState} from "react";

function Reports(props) {

    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const [reasoning, setReasoning] = useState("");
    const [reportId, setReportId] = useState();
    const [entityId, setEntityId] = useState();
    const [entityType, setEntityType] = useState("");
    const [duration, setDuration] = useState(1);

    function handleBan(rId, eId, eT) {
        setReportId(rId);
        setEntityId(eId);
        setEntityType(eT);
        handleShow();
    }

    function removeReport(id) {
        axios.delete(`//localhost:8080/report?id=${id}`,
            {headers: {'Authorization': Cookies.get("authorization")}})
            .then(response =>{
                window.location.reload()
            })
            .catch(err => {
                window.alert(err.response.data.message)
            })
    }

    function ban() {

        axios.post(`//localhost:8080/blockade`,
            {
                "reportId" : reportId,
                "entityType" : entityType,
                "entityId" : entityId,
                "daysOfBlockade" : duration,
                "reasoning" : reasoning
            },
            {headers: {'Authorization': Cookies.get("authorization")}})
            .then(response =>{
                window.location.reload()
            })
            .catch(err => {
                window.alert(err.response.data.message)
            })
    }

    return(
        <Tab.Container>
            <Row>
                <Col sm={4}>
                    { props.reports.map((report,key) => {
                        return (
                            <ListGroup>
                                <ListGroup.Item action href={"#"+report.id}>
                                    ID: {report.id} {report.creationTime.slice(0, 16).replace("T", " ")}
                                </ListGroup.Item>
                            </ListGroup>
                        );
                    })
                    }
                </Col>
                <Col sm={8}>
                    <Tab.Content>
                        { props.reports.map((report,key) => {
                                return (
                                    <Tab.Pane key={key} eventKey={"#"+report.id}>
                                        <div className={"ADMIN-report-container"}>
                                            <h3>Report type {report.entityType}</h3>
                                            { report.entityType == "USER" ?
                                                <a href={`localhost:3000/profile/${report.entityId}`}>LINK TO PROFILE</a>
                                                :
                                                <a href={`localhost:3000/project/${report.entityId}`}>LINK TO PROJECT</a>
                                            }
                                            <h5>Reporter id: {report.userId}</h5>
                                            {report.reasoning}
                                            <Row className={"mt-3"}>
                                                <Col>
                                                    <center><Button variant="primary" onClick={(e)=>{removeReport(report.id);}}>Reject</Button></center>
                                                </Col>
                                                <Col>
                                                    <center><Button variant="danger" onClick={(e) => {handleBan(report.id, report.entityId, report.entityType);}}>BAN</Button></center>
                                                </Col>
                                            </Row>
                                        </div>
                                    </Tab.Pane>
                                );
                            })
                        }
                        <Modal show={show} onHide={handleClose}>
                            <Modal.Header closeButton>
                                <Modal.Title>BAN</Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                                <Form.Label>Reason for BAN</Form.Label>
                                <Form.Control as="textarea" value={reasoning}
                                              onChange={(e) => {setReasoning(e.target.value)}}
                                              rows={3} required/>
                                <Form.Group className="mt-2" controlId="exampleForm.ControlTextarea1">
                                    <Form.Label>Duration</Form.Label>
                                    <Form.Control type="number" min={1} value={duration} onChange={(e)=>{setDuration(Number(e.target.value));}}/>
                                </Form.Group>
                            </Modal.Body>
                            <Modal.Footer>
                                <Button variant="danger" onClick={(e) => {ban();}}>
                                    BAN!
                                </Button>
                            </Modal.Footer>
                        </Modal>
                    </Tab.Content>
                </Col>
            </Row>
        </Tab.Container>
    );
}
export default Reports;