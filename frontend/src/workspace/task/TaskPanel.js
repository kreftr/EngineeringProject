import {Button, Form, Image, ListGroup, ListGroupItem, Modal, Tab, Tabs} from "react-bootstrap";
import default_profile_picture from "../../assets/images/default_profile_picture.jpg";
import React, {useState} from "react";
import {useParams} from "react-router-dom";
import DateTimePicker from "react-datetime-picker";
import axios from "axios";
import Cookies from "js-cookie";


function TaskPanel(props){

    const {id} = useParams();

    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [date, setDate] = useState(new Date());
    const [participant, setParticipant] = useState(null);
    const [t, setTeam] = useState(null);


    function handleMemberChange(e){
        setParticipant(e.target.value)
        setTeam(null)
    }

    function handleTeamChange(e){
        setTeam(e.target.value)
        setParticipant(null)
    }

    function disableTeams(){
        if (participant) return true;
        else return false;
    }

    function disableMembers(){
        if (t) return true;
        else return false;
    }

    Date.prototype.addHours= function(h){
        this.setHours(this.getHours()+h);
        return this;
    }

    function createTask(e){
        e.preventDefault()
        let dateString = date.addHours(1).toISOString().substring(0,16);

        if (participant && t === null){
            axios.post("http://localhost:8080/task/addForParticipant", {
                "projectId": id,
                "name": name,
                "description": description,
                "expirationDate": dateString,
                "userId": participant,
                "teamId": null
            }, {headers:{'Authorization': Cookies.get("authorization")}}
            ).then(response => {
                window.location.reload()
            }).catch(err => {
                console.log(err.response)
            })
        }
        else if (t && participant === null){
            axios.post("http://localhost:8080/task/addForTeam", {
                    "projectId": id,
                    "name": name,
                    "description": description,
                    "expirationDate": dateString,
                    "userId": null,
                    "teamId": t
                }, {headers:{'Authorization': Cookies.get("authorization")}}
            ).then(response => {
                window.location.reload()
            }).catch(err => {
                console.log(err.response)
            })
        }
    }


    return(
        <>
            <Modal.Header closeButton>
                <Modal.Title>Create new task</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form onSubmit={createTask}>
                    <Form.Control type="text" className={"mb-2"} placeholder="Name"
                                  onChange={(e) => setName(e.target.value)} required={true}/>
                    <Form.Control as="textarea" className={"mb-2 TEAM-description-resize"} placeholder="Description"
                                  maxLength={250} rows={3} onChange={(e) => setDescription(e.target.value)}
                    />
                    <center>
                        <DateTimePicker className={"mt-2"}
                                        onChange={setDate}
                                        value={date}
                        />
                    </center>
                    <center>
                        <h4 className={"mt-4"}>Assign team or project member to task</h4>
                    </center>
                    <hr/>
                    <Tabs defaultActiveKey="teams" className="mb-3" fill>
                        <Tab eventKey="teams" title="Teams" disabled={disableTeams()}>
                            <ListGroup className={"TEAM-members-select"}>
                                { props.teams.map((team, key) =>
                                    <div>
                                        <ListGroupItem className={"mb-1 TEAM-test"}>
                                            <h5>
                                                <Form.Check className={"mt-1 mr-2"}
                                                            key={key}
                                                            type={"radio"}
                                                            label={team.name}
                                                            value={team.teamId}
                                                            onClick={(e) => {handleTeamChange(e);}}
                                                            checked={Number(t) === Number(team.teamId)}
                                                />
                                            </h5>
                                        </ListGroupItem>
                                    </div>
                                )
                                }
                            </ListGroup>
                        </Tab>
                        <Tab eventKey="participants" title="Participants" disabled={disableMembers()}>
                            <ListGroup className={"TEAM-members-select"}>
                                { props.members.map((member, key) =>
                                    <div>
                                        <ListGroupItem className={"mb-1 TEAM-test"}>
                                            <h5>
                                                <Form.Check className={"mt-1 mr-2"}
                                                            key={key}
                                                            type={"radio"}
                                                            label={member.username}
                                                            value={member.userId}
                                                            onClick={(e) => {handleMemberChange(e);}}
                                                            checked={Number(participant) === Number(member.userId)}
                                                />
                                            </h5>
                                            <a href={`/profile/${member.userId}`}>
                                                { member.profilePhoto ?
                                                    <Image src={`http://localhost:8080/photo?filename=${member.profilePhoto}`}
                                                           width={30} height={30} rounded={true}/>
                                                    :
                                                    <Image src={default_profile_picture}
                                                           width={30} height={30} rounded={true}/>
                                                }
                                            </a>
                                        </ListGroupItem>
                                    </div>
                                )
                                }
                            </ListGroup>
                        </Tab>
                    </Tabs>
                    <center>
                        <Button type={"submit"} className={"mt-3"}>Create</Button>
                    </center>
                </Form>
                <hr/>
            </Modal.Body>
        </>
    );

}
export default TaskPanel;