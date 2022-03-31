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

    },[best])

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
                                        {/*<ListGroupItem>*/}
                                        {/*    <Row>*/}
                                        {/*        <Col className={"col-3 HOMEPAGE-ranking-thumbnail-clickable"}>*/}
                                        {/*            <Image src={default_project_picture}*/}
                                        {/*                   width="200px" height="200px"/>*/}
                                        {/*        </Col>*/}
                                        {/*        <Col className={"col-6 HOMEPAGE-ranking-content-section-clickable"}>*/}
                                        {/*            <h2>Title of the Project</h2>*/}
                                        {/*            <div className={"HOMEPAGE-project-introduction"}>Introduction of the*/}
                                        {/*                project*/}
                                        {/*            </div>*/}
                                        {/*        </Col>*/}
                                        {/*        <Col className={"col-3"}>*/}
                                        {/*            <h3>Rating: <Badge pill bg="primary"><FaStar*/}
                                        {/*                className={"ml-1"}/></Badge></h3>*/}
                                        {/*            <h3>Votes: </h3>*/}
                                        {/*        </Col>*/}
                                        {/*    </Row>*/}
                                        {/*</ListGroupItem>*/}
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