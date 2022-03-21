import {Button, Col, Image, ListGroup, ListGroupItem, Modal, Row} from "react-bootstrap";
import React from "react";
import default_profile_picture from "../../assets/images/default_profile_picture.jpg";
import {FaPlusSquare} from "react-icons/all";
import axios from "axios";
import Cookies from "js-cookie";
import {useParams} from "react-router-dom";


function AddMember(props){

    const {id} = useParams();

    function handleAddMember(userId){
        axios.post(`http://localhost:8080/team/addMember/${props.teamId}/${userId}/${id}`, null,
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
                <Modal.Title>Add member</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <ListGroup className={"TEAM-members-add"}>
                    { props.members.filter(m => !props.team.members.map(n => n.username).includes(m.username))
                        .map((member, key) =>
                        <div key={key}>
                            <ListGroupItem className={"mb-1 TEAM-test"}>
                                <Row className={"TEAM-test"}>
                                    <Col className={"col-3"}>
                                        <a href={`/profile/${member.userId}`} className={"mr-3"}>
                                            { member.profilePhoto ?
                                                <Image src={`http://localhost:8080/photo?filename=${member.profilePhoto}`}
                                                       width={80} height={80} rounded={true}/>
                                                :
                                                <Image src={default_profile_picture}
                                                       width={80} height={80} rounded={true}/>
                                            }
                                        </a>
                                    </Col>
                                    <Col className={"col-7"}>
                                        <h1>{member.username}</h1>
                                    </Col>
                                    <Col className={"col-2"}>
                                        <Button onClick={() => {handleAddMember(member.userId);}}>
                                            <FaPlusSquare size={30}/>
                                        </Button>
                                    </Col>
                                </Row>
                            </ListGroupItem>
                        </div>
                    )
                    }
                </ListGroup>
            </Modal.Body>
            <Modal.Footer>
            </Modal.Footer>
        </>
    );

}
export default AddMember;