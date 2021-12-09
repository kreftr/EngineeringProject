import {Col, Row, Container, Form, Button, Alert, Spinner} from "react-bootstrap";
import "./Registration.css"
import {useState} from "react";
import axios from "axios";

function Registration(){

    const [username,setUsername] = useState();
    const [email,setEmail] = useState();
    const [password,setPassword] = useState();
    const [confirmPassword,setConfirmPassword] = useState();

    const [loading, setLoading] = useState(false);
    const [responseMessage,setResponseMessage] = useState();
    const [responseCode, setResponseCode] = useState();

    async function handleSubmit(e){
        e.preventDefault()
        setLoading(true);
        setResponseMessage(null)

        if (password !== confirmPassword){
            setResponseMessage("*Passwords are not the same")
            return;
        }
        else {
            await axios.post("http://localhost:8080/registration", {
                "username": username,
                "email": email,
                "password": password,
                "confirmPassword": confirmPassword
            })
            .then(response => {
                setResponseMessage(response.data.message)
                setResponseCode(response.status)
                setLoading(false)
            })
            .catch(err => {
                console.log(err.response.data.error)
                if(err.response.status === 409) setResponseMessage(err.response.data.message)
                else if (err.response.status === 400) setResponseMessage("*"+err.response.data.error)
                else setResponseMessage("SERVER ERROR!")
                setResponseCode(err.response.status)
                setLoading(false)
            })
        }
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
                            <Form.Text className="text-muted">
                                *Username must be 6 to 12 characters long and cannot contain special characters.
                            </Form.Text>
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
                            <Form.Text className="text-muted">
                                *The password must be 8 to 24 characters long, including at least one uppercase and lowercase letter, number, and special character.
                            </Form.Text>
                        </Form.Group>
                        <Form.Group className="mb-5" controlId="formConfirmPassword">
                            <Form.Label>Confirm password</Form.Label>
                            <Form.Control type="password" placeholder="Confirm password" value={confirmPassword}
                                          onChange={(e) => setConfirmPassword(e.target.value)} required/>
                        </Form.Group>
                        { !loading ?
                            <Button className={"mb-5"} variant="primary" type="submit">
                                Sign In
                            </Button>
                            :
                            <center>
                                <Spinner animation="border"/>
                            </center>
                        }
                    </Form>
                    {responseMessage && responseCode === 201 ?
                        <Alert variant={"success"}>
                            <center>
                                Your account has been successfully created!<br/>
                                Check your email and activate your account<br/>
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