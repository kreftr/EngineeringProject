import {Accordion, Button, Col, Image, ListGroup, ListGroupItem, Modal, Row} from "react-bootstrap";
import default_profile_picture from "../../assets/images/default_profile_picture.jpg";
import React, {useState} from "react";
import {FaCogs, FaMinusSquare, FaUserPlus} from "react-icons/fa";
import TeamEdit from "./TeamEdit";
import AddMember from "./AddMember";
import axios from "axios";
import Cookies from "js-cookie";
import {useParams} from "react-router-dom";


function Team(props){

    const {id} = useParams();

    const[showSettings, setShowSettings] = useState(false);
    const handleCloseSettings = () => setShowSettings(false);
    function handleShowSettings(e) {
        e.stopPropagation();
        setShowSettings(true);
    }

    const[showAddMember, setShowAddMember] = useState(false);
    const handleCloseAddMember = () => setShowAddMember(false);
    function handleShowAddMember(e) {
        e.stopPropagation();
        setShowAddMember(true);
    }

    function handleMemberRemoval(userId){
        axios.delete(`http://localhost:8080/team/removeMember/${props.team.teamId}/${userId}/${id}`,
            {headers:{'Authorization': Cookies.get("authorization")}}
        ).then(response => {
            window.location.reload()
        }).catch(err => {
            console.log(err.response)
        })
    }


    return(
        <Accordion>
            <Modal show={showSettings} onHide={handleCloseSettings}>
               <TeamEdit team={props.team}/>
            </Modal>
            <Modal show={showAddMember} onHide={handleCloseAddMember}>
                <AddMember team={props.team} members={props.members} teamId={props.team.teamId}/>
            </Modal>
            <Accordion.Item eventKey="0">
                <Accordion.Header>
                       <Col className={"col-9"}>
                           <h2>{props.team.name}</h2>
                           <span className={"TEAM-description"}>
                                {props.team.description}
                            </span>
                       </Col>
                       <Col className={"col-2 TEAM-test"}>
                           {props.role !== "PARTICIPANT" ?
                               <>
                                   <Button className={"TEAM-header-button mr-4"} onClick={(e) => {handleShowAddMember(e);}}>
                                       <FaUserPlus size={40}/>
                                   </Button>
                                   <Button onClick={(e) => {handleShowSettings(e);}}>
                                       <FaCogs size={40}/>
                                   </Button>
                               </>
                               :
                               <></>
                           }
                       </Col>
                </Accordion.Header>
                <Accordion.Body>
                    {props.team.members.length > 0 ?
                        <ListGroup>
                            {props.team.members.map((member, key) =>
                                <div className={"mb-2"}>
                                    <ListGroupItem key={key}>
                                        <Row className={"TEAM-test"}>
                                            <Col className={"col-2"}>
                                                <a href={`/profile/${member.userId}`} className={"mr-3"}>
                                                    { member.profilePhoto ?
                                                        <Image src={`http://localhost:8080/photo?filename=${member.profilePhoto}`}
                                                               width={100} height={100} rounded={true}/>
                                                        :
                                                        <Image src={default_profile_picture}
                                                               width={100} height={100} rounded={true}/>
                                                    }
                                                </a>
                                            </Col>
                                            <Col className={"col-8"}>
                                                <h1>{member.username}</h1>
                                            </Col>
                                            <Col className={"col-2"}>
                                                <Button variant={"danger"} onClick={()=>{handleMemberRemoval(member.userId);}}>
                                                    <FaMinusSquare size={30}/>
                                                </Button>
                                            </Col>
                                        </Row>
                                    </ListGroupItem>
                                </div>
                            )
                            }
                        </ListGroup>
                        :
                        <></>
                    }
                </Accordion.Body>
            </Accordion.Item>
        </Accordion>
    );

}
export default Team;