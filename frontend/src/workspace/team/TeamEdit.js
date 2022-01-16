import {Button, Col, Form, Modal, Row} from "react-bootstrap";
import {useParams} from "react-router-dom";
import React, {useState} from "react";
import axios from "axios";
import Cookies from "js-cookie";



function TeamEdit(props) {

    const {id} = useParams();

    const [name, setName] = useState(props.team.name);
    const [description, setDescription] = useState(props.team.description);

    function handleEdit(e){
        e.preventDefault();
        axios.put(`http://localhost:8080/team/edit/${props.team.teamId}`, {
                "projectId": id,
                "name": name,
                "description": description,
                "usernames": []
            }, {headers:{'Authorization': Cookies.get("authorization")}}
        ).then(response => {
            window.location.reload()
        }).catch(err => {
            console.log(err.response)
        })
    }

    function handleRemove(){
        axios.delete(`http://localhost:8080/team/remove/${props.team.teamId}`,
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
                <Modal.Title>Team settings</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form onSubmit={handleEdit}>
                    <Form.Control type="text" className={"mb-2"} placeholder="Name" value={name}
                                  onChange={(e) => setName(e.target.value)} required={true}/>
                    <Form.Control as="textarea" className={"mb-2 TEAM-description-resize"} placeholder="Description" value={description}
                                  maxLength={250} rows={3} onChange={(e) => setDescription(e.target.value)}
                    />
                    <Row className={"mt-3"}>
                        <Col>
                            <center>
                                <Button variant={"danger"} onClick={()=>{handleRemove();}}>Remove</Button>
                            </center>
                        </Col>
                        <Col>
                            <center>
                                <Button type={"submit"}>Save</Button>
                            </center>
                        </Col>
                    </Row>
                </Form>
            </Modal.Body>
            <Modal.Footer>
            </Modal.Footer>
        </>
    );

}
export default TeamEdit;