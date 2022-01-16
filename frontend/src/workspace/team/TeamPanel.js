import {Button, Form, Image, ListGroup, ListGroupItem, Modal} from "react-bootstrap";
import React, {useState} from "react";
import "./Team.css"
import default_profile_picture from "../../assets/images/default_profile_picture.jpg";
import axios from "axios";
import {useParams} from "react-router-dom";
import Cookies from "js-cookie";

function TeamPanel(props){

    const {id} = useParams();

    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [members, setMembers] = useState([]);


    function handleMemberChange(e){
        if (members.includes(e.target.value)){
            setMembers(members.filter(x => x !== e.target.value))
        }
        else {
            setMembers([...members, e.target.value])
        }
    }

    function createTeam(e){
        e.preventDefault()
        if (name !== ""){
            axios.post("http://localhost:8080/team/create", {
                    "projectId": id,
                    "name": name,
                    "description": description,
                    "usernames": members
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
                <Modal.Title>Create new team</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form onSubmit={createTeam}>
                    <Form.Control type="text" className={"mb-2"} placeholder="Name"
                                   onChange={(e) => setName(e.target.value)} required={true}/>
                    <Form.Control as="textarea" className={"mb-2 TEAM-description-resize"} placeholder="Description"
                                  maxLength={250} rows={3} onChange={(e) => setDescription(e.target.value)}
                    />
                    <center><h4 className={"mt-3"}>Select team members</h4></center>
                    <hr/>
                    <ListGroup className={"TEAM-members-select"}>
                        { props.members.map((member, key) =>
                            <div>
                            <ListGroupItem className={"mb-1 TEAM-test"}>
                                <h5>
                                    <Form.Check className={"mt-1 mr-2"}
                                                key={key}
                                                type={"checkbox"}
                                                label={member.username}
                                                value={member.username}
                                                onClick={(e) => {handleMemberChange(e)}}
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
                    <center>
                        <Button type={"submit"} className={"mt-3"}>Create</Button>
                    </center>
                </Form>
                <hr/>
            </Modal.Body>
        </>
    );

}
export default TeamPanel;