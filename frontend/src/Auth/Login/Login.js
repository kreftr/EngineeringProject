import './Login.css';
import React, {Component} from "react";
import {Button, Col, Container, Form, Row} from "react-bootstrap";
import LoginAlert from "./LoginAlert.js";


class Login extends Component {

    constructor(props) {
        super(props);
        this.loginAlert = React.createRef();
    }

    showLoginAlert(variant, heading, message) {
        this.loginAlert.current.setVariant(variant)
        this.loginAlert.current.setHeading(heading)
        this.loginAlert.current.setMessage(message)
        this.loginAlert.current.setVisible(true)
    }

    handleSubmit = event => {
        event.preventDefault();
        this.loginUser(
            event.target.username.value,
            event.target.password.value
        );
    }

    loginUser(username, password) {
        fetch("http://localhost:8080/login", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        }).then(function(response) {
            if(response.status === 200) {
                this.showLoginAlert("success!", "Logged in.", response.message);
            } else if (response.status === 409) {
                this.showLoginAlert("Danger!", "Unauthorized Access.", response.message);
            } else if (response.status === 403) {
                this.showLoginAlert("Forbidden!", "Forbidden. I REFUSE!", response.message);
            }
            else {
                this.showLoginAlert("Danger.", "Something went wrong.", response.status);
            }
        }.bind(this)).catch(function(error){
            this.showLoginAlert("Danger.", "Something went wrong", "!!!!!")
        });
    }

    render() {
        return(
            <Container className={"h-50"} fluid>
                <Row className={"h-50"}>
                    <Col sm={4}></Col>
                    <Col sm={3} className={"login-col"}>

                        <h2>Login</h2>

                        <Form className={"login-form"} onSubmit={this.handleSubmit}>
                            <Form.Group className={"form-groupp"} controlID={"username"} size={"lg"}>
                                <Form.Label>Username</Form.Label>
                                <Form.Control name={"username"} required/>
                            </Form.Group>

                            <Form.Group className={"form-groupp"} controlId={"password"} size={"lg"}>
                                <Form.Label>Password</Form.Label>
                                <Form.Control type={"password"} name={"password"} required/>
                            </Form.Group>

                            <div className={"d-grid gap-2"}>
                                <Button variant={"primary"} type={"submit"}>
                                    Submit
                                </Button>
                            </div>

                        </Form>
                    </Col>
                </Row>
            </Container>
        )
    }

}

export default Login;