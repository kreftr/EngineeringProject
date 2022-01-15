import {Button, Form, Modal} from "react-bootstrap";
import React from "react";


function TeamPanel(){


    return(
        <>
            <Modal.Header closeButton>
                <Modal.Title>Create new team</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>

                    <center>
                        <Button type={"submit"} className={"mt-3"}>Search</Button>
                    </center>
                </Form>
                <hr/>
            </Modal.Body>
        </>
    );

}
export default TeamPanel;

// <Form.Control type="text" className={"mb-2"} placeholder="Team name"
//               onChange={(e) => setInviteTermSearch(e.target.value)}/>
// <Form.Control type="text" placeholder="Description"
//               onChange={(e) => setInviteTermSearch(e.target.value)}/>