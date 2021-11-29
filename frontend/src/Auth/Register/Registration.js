import './Registration.css';
import React, {Component} from 'react';
import {Row, Col, Container, Form, Button, Carousel, Image} from "react-bootstrap";

import RegistrationAlert from "./RegistrationAlert.js";

class Registration extends Component {

    constructor(props) {
        super(props);
        this.registerAlert = React.createRef();
    }

    showRegisterAlert(variant, heading, message) {
        this.registerAlert.current.setVariant(variant)
        this.registerAlert.current.setHeading(heading)
        this.registerAlert.current.setMessage(message)
        this.registerAlert.current.setVisible(true)
    }

    handleSubmit = event => {
        event.preventDefault();
        this.registerUser(
            event.target.username.value,
            event.target.email.value,
            event.target.password.value
        );
    }

    registerUser(username, password) {
        fetch('http://localhost:8080/registration', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                email: email,
                password: password
            })
        }).then(function(response) {
            if(response.status === 201) {
                this.showRegisterAlert("success!", "User created.", response.message);
            } else if (response.status === 409) {
                this.showRegistrationAlert("danger!!", "User already exists.", "response.message")
            }
            else {
                this.showRegistrationAlert("danger!!", "Something went wrong..", response.status)
            }
        }.bind(this)).catch(function(error){
            this.showRegistrationAlert("danger", "Something went wrong", "!!!!!")
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

                        <RegistrationAlert ref={this.registrationAlert}/>
                    </Col>

                    <Col sm={8}>
                        <Carousel controls={false}>
                            <Carousel.Item className={"h-100 w-100"} interval={4000}>
                                <Image className={"d-block w-100 h-100"} src={require("./images/team.jpg")}/>
                                <Carousel.Caption>
                                    <h1>Slide 1</h1>
                                    <p>Nulla vitae elit libero, a pharetra augue mollis interdum.</p>
                                </Carousel.Caption>
                            </Carousel.Item>
                            <Carousel.Item className={"h-100 w-100"} interval={4000}>
                                <Image className={"d-block w-100 h-100"} src={require("./images/creative.jpg")}/>
                                <Carousel.Caption>
                                    <h1>Slide 2</h1>
                                    <p>Nulla vitae elit libero, a pharetra augue mollis interdum.</p>
                                </Carousel.Caption>
                            </Carousel.Item>
                            <Carousel.Item className={"h-100 w-100"} interval={4000}>
                                <Image className={"d-block w-100 h-100"} src={require("./images/problem.jpg")}/>
                                <Carousel.Caption>
                                    <h1>Slide 3</h1>
                                    <p>Nulla vitae elit libero, a pharetra augue mollis interdum.</p>
                                </Carousel.Caption>
                            </Carousel.Item>
                        </Carousel>
                    </Col>
                </Row>
            </Container>
        )
    }
}

export default Registration;