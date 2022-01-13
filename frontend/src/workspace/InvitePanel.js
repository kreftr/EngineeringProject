import {Button, Col, Form, Image, ListGroup, ListGroupItem, Modal, Row} from "react-bootstrap";
import React, {useState} from "react";
import axios from "axios";
import default_profile_picture from "../assets/images/default_profile_picture.jpg";
import {useParams} from "react-router-dom";
import Cookies from "js-cookie";


function InvitePanel(){

    const {id} = useParams();

    const [inviteTermSearch, setInviteTermSearch] = useState("");
    const [profiles, setProfiles] = useState(null);
    const [message, setMessage] = useState(null);

    function search(e){
        e.preventDefault()
        setProfiles(null)
        axios.get(`http://localhost:8080/profile?username=${inviteTermSearch}`)
        .then(response => {
            setProfiles(response.data)
            setMessage(null)
        }).catch(err => {
            setProfiles([])
            setMessage(err.response.data.message)
            console.log(err.response)
        })
    }

    function invite(userId){
        axios.post(`http://localhost:8080/project/inviteToProject/${id}?userId=${userId}`, null,
            {headers: {'Authorization': Cookies.get("authorization")}
        })
        .then(response => {
            window.location.reload()
        }).catch(err => {
            console.log(err.response)
        })
    }


    return(
        <>
            <Modal.Header closeButton>
                <Modal.Title>Invite to project</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form onSubmit={search}>
                    <Form.Control type="text" placeholder="Search for user"
                                  onChange={(e) => setInviteTermSearch(e.target.value)}/>
                    <center>
                        <Button type={"submit"} className={"mt-3"}>Search</Button>
                    </center>
                </Form>
                <hr/>
                { profiles && profiles.length > 0 ?
                    <ListGroup>
                        { profiles.map((profile, key) =>
                            <ListGroupItem key={key}>
                                <Row>
                                    <Col className={"col-8 MEMBER-container"}>
                                        <a href={`/profile/${profile.id}`}>
                                            { profile.profile_photo ?
                                                <Image src={`http://localhost:8080/photo?filename=${profile.profile_photo}`}
                                                       width={100} height={100} rounded={true}/>
                                                :
                                                <Image src={default_profile_picture}
                                                       width={100} height={100} rounded={true}/>
                                            }
                                        </a>
                                        <h3 className={"ml-3 MEMBER-username"}>{profile.username}</h3>
                                    </Col>
                                    <Col className={"col-4 MEMBER-username"}>
                                        <Row>
                                            <Button variant={"primary"} onClick={()=>{invite(profile.id)}}>Invite</Button>
                                        </Row>
                                    </Col>
                                </Row>
                            </ListGroupItem>
                        )}
                    </ListGroup>
                    : message !== null ?
                    <center>
                        <h3>{message}</h3>
                    </center>
                    :
                    <></>
                }
            </Modal.Body>
        </>
    );
}
export default InvitePanel;