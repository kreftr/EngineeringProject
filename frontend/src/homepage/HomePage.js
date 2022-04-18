import {Button, Container, Col, Form, ListGroup, ListGroupItem, Modal, Row, Image, Badge} from "react-bootstrap";
import "./HomePage.css";
import React, {useEffect, useState} from "react";
import default_project_picture from "../assets/images/default_project_picture.jpg";
import {FaStar} from "react-icons/all";
import axios from "axios";
import ShownProjets from "./ShownProjets";

function Homepage() {

    const [best, setBest] = useState([]);

    useEffect(() => {

        axios.get(`http://localhost:8080/project/randomRecommended`
        ).then(response => {
            setBest(response.data)
            console.log(best)
        }).catch(err => {
            console.log(err.response)
        })

    },[])

    return (
        <Container className={"homePage-container"}>
            <Row>
                <Col className={"col-1"}/>
                <Col className={"col-10"}>
                    <h1 className={"HOMEPAGE-ranking-title mb-4"}>Recommended Projects</h1>
                    <hr/>
                    {best.length > 0 ?
                        <ListGroup className={"mt-3"} as="ol" numbered={true}>
                            {
                                best.map((project, key) =>
                                    <>
                                        <ListGroupItem key={key} className={"mb-3"} as="li">
                                            <ShownProjets project={project}/>
                                        </ListGroupItem>
                                        <div/>
                                    </>
                                )
                            }
                        </ListGroup>
                        :
                        <center>
                            <h2>There are currently recommendations for your</h2>
                        </center>
                    }
                </Col>
                <Col className={"col-1"}/>
            </Row>
        </Container>
    );
}

export default Homepage;