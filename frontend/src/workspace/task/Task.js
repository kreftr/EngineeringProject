import {Accordion, Button, Col, Image, ListGroup, ListGroupItem, Modal, Row} from "react-bootstrap";
import TeamEdit from "../team/TeamEdit";
import AddMember from "../team/AddMember";
import {FaCogs, FaMinusSquare, FaUserPlus} from "react-icons/fa";
import default_profile_picture from "../../assets/images/default_profile_picture.jpg";
import React, {useState} from "react";
import "./Task.css"
import EditTask from "./TaskEdit";

function Task(props){

    const [showEdit, setShowEdit] = useState(false);
    const handleCloseEdit = () => setShowEdit(false);
    const handleShowEdit = () => setShowEdit(true);


    return(
        <Accordion className={"TASK-width"}>
            <Modal show={showEdit} onHide={handleCloseEdit}>
                <EditTask task={props.task}/>
            </Modal>
            <Accordion.Item eventKey="0">
                <Accordion.Header>
                    <h5 className={"TASK-name-wrap"}>{props.task.name}</h5>
                </Accordion.Header>
                <Accordion.Body>
                    { props.task.teamId !== null ?
                        <>
                            <h5>Assigned to team:</h5>
                            {props.task.teamName}
                        </>
                        :
                        <>
                            <h5>Assigned to user:</h5>
                            { props.task.profilePhoto ?
                                <Image className={"mr-2"} src={`http://localhost:8080/photo?filename=${props.task.profilePhoto}`}
                                       width={30} height={30} rounded={true}/>
                                :
                                <Image className={"mr-2"} src={default_profile_picture}
                                       width={30} height={30} rounded={true}/>
                            }
                            {props.task.username}
                        </>
                    }
                    <hr/>
                    <div>Creation date: {props.task.creationDate}</div>
                    <div>Expiration date: <b>{props.task.expirationDate}</b></div>
                    <hr/>
                    <div className={"TASK-name-wrap"}>
                        {props.task.description}
                    </div>
                    { props.role !== "PARTICIPANT" ?
                        <Row className={"mt-3"}>
                            <Button variant={"primary"} onClick={(e)=>{handleShowEdit(e);}}>
                                <FaCogs size={30}/>
                            </Button>
                        </Row>
                        :
                        <></>
                    }
                </Accordion.Body>
            </Accordion.Item>
        </Accordion>
    );

}
export default Task;