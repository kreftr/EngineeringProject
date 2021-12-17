import {useEffect, useState} from "react";
import axios from "axios";
import {useParams} from "react-router-dom";
import "./Verification.css"
import {Alert, Col, Container, Row} from "react-bootstrap";

function Verification(){

    const {token} = useParams();

    const [responseMessage, setResponseMessage] = useState()
    const [responseCode, setResponseCode] = useState()

    useEffect(() => {
        axios.get(`http://localhost:8080/registration/verify?token=${token}`)
            .then(response => {
                setResponseCode(response.status)
                setResponseMessage(response.data.message)
            })
            .catch(err => {
                setResponseCode(err.response.status)
                setResponseMessage(err.response.data.message)
            })
    }, [token])


    return(
        <Container className={"mt-5"}>
            <Row>
                <Col className={"col-4"}></Col>
                <Col className={"col-4"}>
                    {responseMessage && responseCode===200 ?
                        <Alert variant={"success"}>
                            <center>
                                {token}<br/>
                                {responseMessage}<br/>
                                <Alert.Link href="/login">CLICK HERE TO LOG IN</Alert.Link>
                            </center>
                        </Alert>
                        :
                        <Alert variant={"danger"}>
                            <center>
                                {token}<br/>
                                {responseMessage}<br/>
                            </center>
                        </Alert>
                    }
                </Col>
                <Col className={"col-4"}></Col>
            </Row>
        </Container>
    );
}

export default Verification