import {Alert, Button, Col, Container, Form, FormControl, FormGroup, Row} from "react-bootstrap";
import "./Login.css"
import axios from "axios";
import React, {useEffect, useState} from "react";
import Cookies from "js-cookie"
import {Navigate} from "react-router-dom";

function Login(){


    const [username,setUsername] = useState();
    const [password,setPassword] = useState();
    const [responseCode, setResponseCode] = useState();

    useEffect(() => {
        if (Cookies.get("authorization")) {
            window.location.replace("/");
        }
    })

    async function handleSubmit(e){
        e.preventDefault()

        await axios.post("http://localhost:8080/login", {
            "username": username,
            "password": password,
        })
            .then(response => {
                setResponseCode(response.status)
                Cookies.set("authorization", response.headers["authorization"])
                Cookies.set("userId", response.data["id"])
            })
            .catch(err => {
                console.log(err.response)
                setResponseCode(err.response.status)
            })
    }

    return(
        <Container className={"LOGIN-container"}>
            <Row>
                <Col className={"col-4"}></Col>
                <Col className={"col-4"}>
                    <Form className={"LOGIN-form"} onSubmit={handleSubmit}>
                        <FormGroup className={"mb-3"}>
                            <Form.Label>Username</Form.Label>
                            <FormControl type={"text"}
                                         value={username}
                                         onChange={(e) => setUsername(e.target.value)}
                                         required/>
                        </FormGroup>
                        <FormGroup className={"mb-5"}>
                            <Form.Label>Password</Form.Label>
                            <FormControl type={"password"}
                                         value={password}
                                         onChange={(e) => setPassword(e.target.value)}
                                         required/>
                        </FormGroup>
                        <Button className={"mb-5"} variant="primary" type="submit">
                            Log In
                        </Button>
                    </Form>
                    {responseCode === 403 ?
                        <Alert variant={"danger"}>
                            <center>
                                Bad credentials
                            </center>
                        </Alert>
                        :responseCode === 500 ?
                        <Alert variant={"danger"}>
                            <center>
                                Server error
                            </center>
                        </Alert>
                        :responseCode === 200 ?
                        <Navigate to={"/"}/>
                        :
                        <></>
                    }
                </Col>
                <Col className={"col-4"}></Col>
            </Row>
        </Container>
    )

}
export default Login