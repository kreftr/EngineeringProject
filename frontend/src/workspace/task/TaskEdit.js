import {Button, Col, Form, Modal, Row} from "react-bootstrap";
import React, {useState} from "react";
import {useParams} from "react-router-dom";
import DateTimePicker from "react-datetime-picker";
import axios from "axios";
import Cookies from "js-cookie";


function TaskEdit(props){

    const {id} = useParams();

    const [name, setName] = useState(props.task.name);
    const [description, setDescription] = useState(props.task.description);
    const [date, setDate] = useState(new Date());

    Date.prototype.addHours = function(h){
        this.setHours(this.getHours()+h);
        return this;
    }

    function handleRemove(){
        axios.delete(`http://localhost:8080/task/remove/${props.task.id}`,
            {headers:{'Authorization': Cookies.get("authorization")}}
        ).then(response => {
            window.location.reload()
        }).catch(err => {
            console.log(err.response)
        })
    }

    function handleEdit(e){
        e.preventDefault();
        let dateString = date.addHours(1).toISOString().substring(0,16);

        axios.put(`http://localhost:8080/task/edit/${props.task.id}`, {
                "projectId": id,
                "name": name,
                "description": description,
                "expirationDate": dateString,
                "userId": null,
                "teamId": null
            }, {headers:{'Authorization': Cookies.get("authorization")}}
        ).then(response => {
            window.location.reload()
        }).catch(err => {
            console.log(err.response)
        })
    }

    function changeStatusToDo(){
        axios.post(`http://localhost:8080/task/editStatus/ToDo/${props.task.id}`, null,
            {headers:{'Authorization': Cookies.get("authorization")}}
        ).then(response => {
            window.location.reload()
        }).catch(err => {
            console.log(err.response)
        })
    }

    function changeStatusInProgress(){
        axios.post(`http://localhost:8080/task/editStatus/InProgress/${props.task.id}`, null,
            {headers:{'Authorization': Cookies.get("authorization")}}
        ).then(response => {
            window.location.reload()
        }).catch(err => {
            console.log(err.response)
        })
    }

    function changeStatusDone(){
        axios.post(`http://localhost:8080/task/editStatus/Done/${props.task.id}`, null,
            {headers:{'Authorization': Cookies.get("authorization")}}
        ).then(response => {
            window.location.reload()
        }).catch(err => {
            console.log(err.response)
        })
    }


    return(
        <>
            <Modal.Header closeButton>
                <Modal.Title>Task settings</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form onSubmit={handleEdit}>
                    <Form.Control type="text" className={"mb-2"} placeholder="Name" value={name}
                                  onChange={(e) => setName(e.target.value)} required={true}/>
                    <Form.Control as="textarea" className={"mb-2 TEAM-description-resize"} placeholder="Description" value={description}
                                  maxLength={250} rows={3} onChange={(e) => setDescription(e.target.value)}
                    />
                    <center>
                        <DateTimePicker className={"mt-2"}
                                        onChange={setDate}
                                        value={date}
                        />
                    </center>
                    <Row className={"mt-3"}>
                        { props.task.status !== "TODO" ?
                            <Col>
                                <Row className={"mr-2 ml-2"}>
                                    <Button variant={"danger"} onClick={() => {changeStatusToDo()}}>To do</Button>
                                </Row>
                            </Col>
                            :
                            <></>
                        }
                        { props.task.status !== "IN_PROGRESS" ?
                            <Col>
                                <Row className={"mr-2 ml-2"}>
                                    <Button variant={"warning"} onClick={()=>{changeStatusInProgress()}}>In progress</Button>
                                </Row>
                            </Col>
                            :
                            <></>
                        }
                        {props.task.status !== "DONE" ?
                            <Col>
                                <Row className={"mr-2 ml-2"}>
                                    <Button variant={"success"} onClick={()=>{changeStatusDone()}}>Done</Button>
                                </Row>
                            </Col>
                            :
                            <></>
                        }
                    </Row>
                    <hr/>
                    <Row>
                        <Col>
                            <Row className={"mr-2 ml-2"}>
                                <Button variant={"danger"} onClick={()=>{handleRemove();}}>Remove</Button>
                            </Row>
                        </Col>
                        <Col>
                            <Row className={"mr-2 ml-2"}>
                                <Button type={"submit"} variant={"primary"}>Save</Button>
                            </Row>
                        </Col>
                    </Row>
                </Form>
            </Modal.Body>
            <Modal.Footer>
            </Modal.Footer>
        </>
    );

}
export default TaskEdit;