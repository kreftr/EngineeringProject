import "./Workspace.css"
import {Badge, Button, Col, Container, Form, ListGroup, Modal, Nav, Row, Tab} from "react-bootstrap";
import {useParams} from "react-router-dom";
import React, {useEffect, useRef, useState} from "react";
import axios from "axios";
import Cookies from "js-cookie";
import Member from "./Member";
import File from "./File";
import {FaCalendarPlus, FaFileUpload} from "react-icons/all";
import {FaUserFriends, FaUserPlus} from "react-icons/fa";
import InvitePanel from "./InvitePanel";
import TeamPanel from "./team/TeamPanel";
import Team from "./team/Team";
import TaskPanel from "./task/TaskPanel";
import Task from "./task/Task";


function Workspace(){

    const {id} = useParams();
    const [memberRole, setMemberRole] = useState(null)

    //Files section
    const [fileTermSearch, setFileTermSearch] = useState("");
    const [files, setFiles] = useState([]);
    const inputFile = useRef(null);
    const onButtonClick = () => {
        // `current` points to the mounted file input element
        inputFile.current.click();
    };
    function onUpload(){
        let file = document.getElementById("fileChooser").files[0]
        let bodyFormData = new FormData();
        bodyFormData.append("file", file);
        axios.post(`http://localhost:8080/file/upload?projectId=${id}`, bodyFormData,
            {headers:{'Authorization': Cookies.get("authorization")}}
        ).then(response => {
            window.location.reload();
        }).catch(err => {
            console.log(err.response)
        })
    }


    //Members section
    const [showInvitations, setShowInvitations] = useState(false);
    const handleCloseInvitations = () => setShowInvitations(false);
    const handleShowInvitations = () => setShowInvitations(true);
    const [members, setMembers] = useState([]);
    const [memberTermSearch, setMemberTermSearch] = useState("");


    //Teams section
    const [showTeams, setShowTeams] = useState(false);
    const handleCloseTeams = () => setShowTeams(false);
    const handleShowTeams = () => setShowTeams(true);
    const [teams, setTeams] = useState([]);
    const [teamTermSearch, setTeamTermSearch] = useState("");


    //Tasks section
    const [showTasks, setShowTasks] = useState(false);
    const handleCloseTasks = () => setShowTasks(false);
    const handleShowTasks = () => setShowTasks(true);
    const [tasks, setTasks] = useState([]);
    const [taskTermSearch, setTaskTermSearch] = useState("");


    useEffect(() => {

        axios.get(`http://localhost:8080/project/getProjectMembers/${id}`,
            {headers: {'Authorization': Cookies.get("authorization")}
        }).then(response =>{
            setMembers(response.data)
            let userMember = response.data.filter(project => Number(project.userId) == Number(Cookies.get("userId")));
            if (userMember.length > 0) setMemberRole(userMember[0].projectRole)
            else setMemberRole(null)
        })
        .catch(err => {
            console.log(err.response)
            setMemberRole(null)
        })

        axios.get(`http://localhost:8080/project/getProjectFiles/${id}`,
            {headers: {'Authorization': Cookies.get("authorization")}
        }).then(response =>{
            setFiles(response.data)
        })
        .catch(err => {
            console.log(err.response)
        })

        axios.get(`http://localhost:8080/team/getProjectTeams/${id}`,
            {headers: {'Authorization': Cookies.get("authorization")}
        }).then(response =>{
            setTeams(response.data)
        })
        .catch(err => {
            console.log(err.response)
        })

        axios.get(`http://localhost:8080/task/getAllProjectTasks/${id}`,
            {headers: {'Authorization': Cookies.get("authorization")}
        }).then(response =>{
            setTasks(response.data)
            console.log(response.data)
        })
        .catch(err => {
            console.log(err.response)
        })

    },[])


    return(
        <Container className={"mt-5"}>
            <Row>
                {memberRole ?
                    <Tab.Container id="left-tabs-example" defaultActiveKey="first">
                        <Row>
                            <Col>
                                <Nav variant="pills" className="flex-column">
                                    <Nav.Item>
                                        <Nav.Link className={"mb-3"} eventKey="news">News</Nav.Link>
                                    </Nav.Item>
                                    <Nav.Item>
                                        <Nav.Link className={"mb-3"} eventKey="tasks">Tasks</Nav.Link>
                                    </Nav.Item>
                                    <Nav.Item>
                                        <Nav.Link className={"mb-3"} eventKey="files">Files</Nav.Link>
                                    </Nav.Item>
                                    <Nav.Item>
                                        <Nav.Link className={"mb-3"} eventKey="teams">Teams</Nav.Link>
                                    </Nav.Item>
                                    <Nav.Item>
                                        <Nav.Link className={"mb-3"} eventKey="members">Members</Nav.Link>
                                    </Nav.Item>
                                </Nav>
                            </Col>
                            <Col sm={9}>
                                <Tab.Content>
                                    <Tab.Pane eventKey={"news"}>
                                        Hello
                                    </Tab.Pane>
                                    <Tab.Pane eventKey={"tasks"}>
                                        <Row>
                                            <Col>
                                                { memberRole !== 'PARTICIPANT' ?
                                                    <center>
                                                        <Button onClick={() => {handleShowTasks();}}>
                                                            <h4 className={"WORKSPACE-center-upload-button"}>
                                                                <FaCalendarPlus className={"mr-2"} size={35}/>
                                                                Add task
                                                            </h4>
                                                        </Button>
                                                        <Modal show={showTasks} onHide={handleCloseTasks}>
                                                            <TaskPanel members={members} teams={teams}/>
                                                        </Modal>
                                                    </center>
                                                    :
                                                    <></>
                                                }
                                            </Col>
                                            <Col className={"WORKSPACE-center-upload-button"}>
                                                <center>
                                                    <Form>
                                                        <Form.Control type="text" placeholder="Search task"
                                                                      onChange={(e) => setTaskTermSearch(e.target.value)}/>
                                                    </Form>
                                                </center>
                                            </Col>
                                        </Row>
                                        <hr/>
                                        <Row>
                                            <Col>
                                                <center>
                                                    <h1><Badge bg="danger">To do</Badge></h1>
                                                </center>
                                                <hr/>
                                                { tasks.length > 0 ?
                                                    <div>
                                                        { tasks.filter((t)=>{
                                                            if (taskTermSearch === ""){
                                                                return t
                                                            }
                                                            else if (t.name.toLowerCase().includes(taskTermSearch.toLowerCase())){
                                                                return t
                                                            }
                                                        }).filter((t)=>{
                                                            if (t.status === "TODO") return t
                                                        }).map((task, key) =>
                                                            <div className={"mb-2"} key={key}>
                                                                <Task task={task} role={memberRole}/>
                                                            </div>
                                                        )}
                                                    </div>
                                                    :
                                                    <></>
                                                }
                                            </Col>
                                            <Col>
                                                <center>
                                                    <h1><Badge bg="warning">In progress</Badge></h1>
                                                </center>
                                                <hr/>
                                                { tasks.length > 0 ?
                                                    <div>
                                                        { tasks.filter((t)=>{
                                                            if (taskTermSearch === ""){
                                                                return t
                                                            }
                                                            else if (t.name.toLowerCase().includes(taskTermSearch.toLowerCase())){
                                                                return t
                                                            }
                                                        }).filter((t)=>{
                                                            if (t.status === "IN_PROGRESS") return t
                                                        }).map((task, key) =>
                                                            <div className={"mb-2"} key={key}>
                                                                <Task task={task} role={memberRole}/>
                                                            </div>
                                                        )}
                                                    </div>
                                                    :
                                                    <></>
                                                }
                                            </Col>
                                            <Col>
                                                <center>
                                                    <h1><Badge bg="success">Done</Badge></h1>
                                                </center>
                                                <hr/>
                                                { tasks.length > 0 ?
                                                    <div>
                                                        { tasks.filter((t)=>{
                                                            if (taskTermSearch === ""){
                                                                return t
                                                            }
                                                            else if (t.name.toLowerCase().includes(taskTermSearch.toLowerCase())){
                                                                return t
                                                            }
                                                        }).filter((t)=>{
                                                            if (t.status === "DONE") return t
                                                        }).map((task, key) =>
                                                            <div className={"mb-2"} key={key}>
                                                                <Task task={task} role={memberRole}/>
                                                            </div>
                                                        )}
                                                    </div>
                                                    :
                                                    <></>
                                                }
                                            </Col>
                                        </Row>
                                    </Tab.Pane>
                                    <Tab.Pane eventKey={"files"}>
                                        <Row>
                                            <Col>
                                                <center>
                                                    <Button onClick={() => {onButtonClick();}} >
                                                        <h4 className={"WORKSPACE-center-upload-button"}>
                                                            <Form.Control type="file" ref={inputFile} style={{display: 'none'}}
                                                                          id="fileChooser"
                                                                          onChange={(e) => {onUpload();}}/>
                                                            <FaFileUpload className={"mr-2"} size={35}/>
                                                            Upload file
                                                        </h4>
                                                    </Button>
                                                </center>
                                            </Col>
                                            <Col className={"WORKSPACE-center-upload-button"}>
                                                <center>
                                                    <Form>
                                                        <Form.Control type="text" placeholder="Search file"
                                                                      onChange={(e) => setFileTermSearch(e.target.value)}/>
                                                    </Form>
                                                </center>
                                            </Col>
                                        </Row>
                                        <hr/>
                                        { files.length > 0 ?
                                            <div className={"ml-5 WORKSPACE-file-section"}>
                                                { files.filter((f)=>{
                                                    if (fileTermSearch === ""){
                                                        return f
                                                    }
                                                    else if (f.fileName.toLowerCase().includes(fileTermSearch.toLowerCase())){
                                                        return f
                                                    }
                                                }).map((file, key) =>
                                                    <div key={key}>
                                                        <File file={file} role={memberRole}/>
                                                    </div>
                                                )}
                                            </div>
                                            :
                                            <center>
                                                <h1>Currently there are no files</h1>
                                            </center>
                                        }
                                    </Tab.Pane>
                                    <Tab.Pane eventKey={"teams"}>
                                        <Row>
                                            <Col>
                                                { memberRole !== 'PARTICIPANT' ?
                                                    <center>
                                                        <Button onClick={handleShowTeams}>
                                                            <h4 className={"WORKSPACE-center-upload-button"}>
                                                                <FaUserFriends className={"mr-2"} size={35}/>
                                                                Create team
                                                            </h4>
                                                        </Button>
                                                        <Modal show={showTeams} onHide={handleCloseTeams}>
                                                            <TeamPanel members={members}/>
                                                        </Modal>
                                                    </center>
                                                    :
                                                    <></>
                                                }
                                            </Col>
                                            <Col className={"WORKSPACE-center-upload-button"}>
                                                <center>
                                                    <Form>
                                                        <Form.Control type="text" placeholder="Search team"
                                                                      onChange={(e) => setTeamTermSearch(e.target.value)}/>
                                                    </Form>
                                                </center>
                                            </Col>
                                        </Row>
                                        <hr/>
                                        { teams.filter((t)=>{
                                            if (teamTermSearch === ""){
                                                return t
                                            }
                                            else if (t.name.toLowerCase().includes(teamTermSearch.toLowerCase())){
                                                return t
                                            }
                                        }).map((team, key) =>
                                            <div className={"mb-3"}>
                                                <Team team={team} members={members} role={memberRole} key={key}/>
                                            </div>
                                        )
                                        }
                                    </Tab.Pane>
                                    <Tab.Pane eventKey={"members"}>
                                        <Row>
                                            <Col>
                                                { memberRole !== 'PARTICIPANT' ?
                                                    <center>
                                                        <Button onClick={handleShowInvitations}>
                                                            <h4 className={"WORKSPACE-center-upload-button"}>
                                                                <FaUserPlus className={"mr-2"} size={35}/>
                                                                Invite user
                                                            </h4>
                                                        </Button>
                                                        <Modal show={showInvitations} onHide={handleCloseInvitations}>
                                                            <InvitePanel/>
                                                        </Modal>
                                                    </center>
                                                    :
                                                    <></>
                                                }
                                            </Col>
                                            <Col className={"WORKSPACE-center-upload-button"}>
                                                <center>
                                                    <Form>
                                                        <Form.Control type="text" placeholder="Search member"
                                                                      onChange={(e) => setMemberTermSearch(e.target.value)}/>
                                                    </Form>
                                                </center>
                                            </Col>
                                        </Row>
                                        <hr/>
                                        <ListGroup>
                                            { members.filter((m)=>{
                                                if (memberTermSearch === ""){
                                                    return m
                                                }
                                                else if (m.username.toLowerCase().includes(memberTermSearch.toLowerCase())){
                                                    return m
                                                }
                                            }).map((member, key) =>
                                                <div key={key}>
                                                    <Member member={member} role={memberRole}/>
                                                    <div/>
                                                </div>
                                            )}
                                        </ListGroup>
                                    </Tab.Pane>
                                </Tab.Content>
                            </Col>
                        </Row>
                    </Tab.Container>
                    :
                    <h1>You are not a member of this project</h1>
                }
            </Row>
        </Container>
    );

}
export default Workspace;