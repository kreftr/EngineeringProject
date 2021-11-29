import {Col, Row, Container, Form, Button, Alert} from "react-bootstrap";
import "./Registration.css"
import {useState} from "react";
import axios from "axios";

function Registration(){

    const [username,setUsername] = useState();
    const [email,setEmail] = useState();
    const [password,setPassword] = useState();
    const [confirmPassword,setConfirmPassword] = useState();

    const [responseMessage,setResponseMessage] = useState();
    const [responseCode, setResponseCode] = useState();

    async function handleSubmit(e){
        e.preventDefault()

        await axios.post("http://localhost:8080/registration", {
            "username": username,
            "email": email,
            "password": password,
            "confirmPassword": confirmPassword
        })
            .then(response => {
                setResponseMessage(response.data.message)
                setResponseCode(response.status)
            })
            .catch(err => {
                if(err.response.status === 409) setResponseMessage(err.response.data.message)
                else setResponseMessage("SERVER ERROR!")
                setResponseCode(err.response.status)
            })
    }

    return(
        <Container className={"REGISTRATION-container"}>
            <Row>
                <Col className={"col-4"}></Col>
                <Col className={"col-4"}>
                    <Form className={"REGISTRATION-form"} onSubmit={handleSubmit}>
                        <Form.Group className="mb-3" controlId="formUsername">
                            <Form.Label>Username</Form.Label>
                            <Form.Control type="text" placeholder="Enter username" value={username}
                                          onChange={(e) => setUsername(e.target.value)} required/>
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formEmail">
                            <Form.Label>Email address</Form.Label>
                            <Form.Control type="email" placeholder="Enter email" value={email}
                                          onChange={(e) => setEmail(e.target.value)} required/>
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formPassword">
                            <Form.Label>Password</Form.Label>
                            <Form.Control type="password" placeholder="Enter Password" value={password}
                                          onChange={(e) => setPassword(e.target.value)} required/>
                        </Form.Group>
                        <Form.Group className="mb-5" controlId="formConfirmPassword">
                            <Form.Label>Confirm password</Form.Label>
                            <Form.Control type="password" placeholder="Confirm password" value={confirmPassword}
                                          onChange={(e) => setConfirmPassword(e.target.value)} required/>
                        </Form.Group>
                        <Button className={"mb-5"} variant="primary" type="submit">
                            Sign In
                        </Button>
                    </Form>
                    {responseMessage && responseCode === 201 ?
                        <Alert variant={"success"}>
                            <center>
                                Your account has been successfully created!<br/>
                                Check your email, activate your account and<br/>
                                <Alert.Link href="/login">LOG IN</Alert.Link>
                            </center>
                        </Alert>
                        : responseMessage ?
                        <Alert variant={"danger"}>
                            <center>
                                {responseMessage}
                            </center>
                        </Alert>
                        :
                        <></>
                    }
                </Col>
                <Col className={"col-4"}></Col>
            </Row>
        </Container>
    );
}

export default Registration