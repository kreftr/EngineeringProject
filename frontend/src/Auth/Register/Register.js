import './Register.css';
import React, {Component} from 'react';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import RegisterAlert from "./RegisterAlert.js";

class Register extends Component {

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
            //event.target.email.value,
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
                password: password,
            })
        }).then(function(response) {
            if(response.status === 201) {
                this.showRegisterAlert("success", "User created", response.message);
            } else if (response.status === 422) {
                console.log("User already exists");
            }
            else {
                console.log("User not registered");
            }
        }).catch(function(error){
            console.log("error");
        });
    }

    render() {
        return <div className="Register">
            <Form onSubmit = {this.handleSubmit}>
                <Form.Group controlId="username" size="lg">
                    <Form.Label>Username</Form.Label>
                    <Form.Control autoFocus name="username"/>
                </Form.Group>

                <Form.Group controlId="password" size="lg">
                    <Form.Label>Password</Form.Label>
                    <Form.Control type="password" name="password"/>
                </Form.Group>

                <Button block size = "lg" type = "submit">Register</Button>
            </Form>
        </div>
    }
}

export default Register;