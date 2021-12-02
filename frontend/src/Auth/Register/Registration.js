import './Registration.css';
import React, {Component} from 'react';
import {Row, Col, Container, Form, Button} from "react-bootstrap";
import RegisterAlert from "./RegisterAlert.js";

class Registration extends Component {

    constructor(props) {
        super(props);
        this.RegisterAlert = React.createRef();
    }

    showRegisterAlert(variant, heading, message) {
        this.RegisterAlert.current.setVariant(variant)
        this.RegisterAlert.current.setHeading(heading)
        this.RegisterAlert.current.setMessage(message)
        this.RegisterAlert.current.setVisible(true)
    }

    handleSubmit = event => {
        event.preventDefault();
        this.registerUser(
            event.target.username.value,
            event.target.email.value,
            event.target.password.value,
            event.target.matchingPassword.value
        );
    }

    registerUser(username, email, password, matchingPassword) {
        fetch('http://localhost:8080/registration', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                email: email,
                password: password,
                matchingPassword: matchingPassword
            })
        }).then(function(response) {
            if(response.status === 201) {
                this.showRegisterAlert("success!", "User created.", response.message);
            } else if (response.status === 409) {
                this.showRegisterAlert("danger!!", "User already exists.", response.message)
            }
            else {
                this.showRegisterAlert("danger!!", "Something went wrong..", response.status)
            }
        }.bind(this)).catch(function(error){
            this.showRegisterAlert("danger", "Something went wrong", "!!!!!")
        }.bind(this));
    }

    render() {
        return(
            <Container className={"h-100"} fluid>
                <Row className={"h-100"}>
                    <Col sm={4} className={"registration-col"}>

                        <h1>Registration</h1>

                        <Form className={"registration-form"} onSubmit={this.submit}>
                            <Form.Group className={"form-group"} controlId={"username"} size={"lg"}>
                                <Form.Label>Username</Form.Label>
                                <Form.Control name={"username"} required/>
                            </Form.Group>

                            <Form.Group className={"form-group"} controlId={"email"} size={"lg"}>
                                <Form.Label>Email</Form.Label>
                                <Form.Control type={"email"} name={"email"} required/>
                            </Form.Group>

                            <Form.Group className={"form-group"} controlId={"password"} size={"lg"}>
                                <Form.Label>Password</Form.Label>
                                <Form.Control type={"password"} name={"password"} required/>
                            </Form.Group>

                            <Form.Group className={"form-group"} controlId={"matchingPassword"} size={"lg"}>
                                <Form.Label>Confirm password</Form.Label>
                                <Form.Control type={"password"} name={"matchingPassword"} required/>
                            </Form.Group>

                            <br/>

                            <div className={"d-grid gap-2"}>
                                <Button variant="primary" type="submit">
                                    Submit
                                </Button>
                            </div>
                        </Form>

                        <RegisterAlert ref={this.registerAlert}/>
                    </Col>
                </Row>
            </Container>
        )
    }
}

export default Registration;