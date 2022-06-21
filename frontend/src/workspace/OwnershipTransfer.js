import {
    Button,
    Badge,
    Col,
    Container,
    Form,
    ListGroup,
    Modal,
    Nav,
    Row,
    Tab,
    Tabs,
    ListGroupItem, Image
} from "react-bootstrap";
import default_profile_picture from "../assets/images/default_profile_picture.jpg";
import React, {useState} from "react";
import axios from "axios";
import Cookies from "js-cookie";
import "./OwnershipTransfer.css"

function OwnershipTransfer(props){
    const [projectId] = useState(props.projectId)
    const [chosenOwnerId, setChosenOwnerId] = useState(Cookies.get("userId"));


    function transferOwnership(e){
        e.preventDefault()
        console.log(chosenOwnerId)

        axios.post(`http://localhost:8080/project/transferOwnership/${projectId}/${chosenOwnerId}`,
            { headers:{'Authorization': Cookies.get("authorization")}}
        ).then(response => {
            window.location.reload()
        }).catch(err => {
            console.log(err.response)
        })
    }

    return(
        <>
        <Modal.Header closeButton>
            <Modal.Title>Select member to transfer ownership</Modal.Title>
        </Modal.Header>
        <Modal.Body>
        <ListGroup className={"OWNERSHIP-members-select"} onSubmit={transferOwnership}>
            { props.members.map((member, key) =>
                <div key={key}>
                    <ListGroupItem className={"mb-1 OWNERSHIP-test"}>
                        <h5>
                            <Form.Check className={"mt-1 mr-2"}
                                        label={member.username}
                                        key={key}
                                        type={"radio"}
                                        value={member.username}
                                        name={"potentialOwners"}
                                        onChange={() => { setChosenOwnerId(member.userId)} }
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
            )}
        </ListGroup>
        <center>
            <Button type={"submit"} className={"mt-3"}>Transfer ownership</Button>
        </center>
        <hr/>
        </Modal.Body>
    </>
    );

}
export default OwnershipTransfer;
