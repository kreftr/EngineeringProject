import { Container, Col, ListGroup, ListGroupItem, Row} from "react-bootstrap";
import "./HomePage.css";
import React, {useEffect, useState} from "react";
import axios from "axios";
import ShownProjets from "./ShownProjets";
import Cookies from "js-cookie"

function Homepage() {

    const [best, setBest] = useState([]);

    useEffect(() => {

        if (Cookies.get("authorization")) {
            axios.get(`http://localhost:8080/project/homepageRecommended/${Cookies.get("userId")}`,{headers:{
                    'Authorization': Cookies.get("authorization")
                }})
                .then(response => {
                    setBest(response.data)
                })
                .catch(err => {
                    console.log(err.response)
                })
        }
        else {
            axios.get(`http://localhost:8080/project/randomRecommended`)
                .then(response => {
                    setBest(response.data)
                })
                .catch(err => {
                console.log(err.response)
                })
        }


    },[])

    return (
        <Container className={"homePage-container"}>
            <Row>
                <Col className={"col-1"}/>
                <Col className={"col-10"}>
                        <>
                            { Cookies.get("authorization") ?
                                <>
                                    <h1>Welcome back!</h1>
                                    <p className={"HOMEPAGE-introduction-to-projects-logged"}>
                                        Your personal recomended projects based on your interest.
                                        We hope your find something interesting.
                                    </p>
                                </>
                                :
                                <>
                                    <h1>Welcome to our page!</h1>
                                    <p className={"HOMEPAGE-introduction-to-site"}>
                                    On our site you can connect in projects from various fields of science.
                                    You will find people with similar interests and passion that want to create something amazing.
                                    </p>
                                    <p className={"HOMEPAGE-introduction-to-projects"}>
                                        Here are some interesting projects from our website.
                                    </p>
                                </>
                            }

                            <h1 className={"HOMEPAGE-title mb-4"}>Recommended Projects</h1>
                            <hr/>
                            { best.length > 0 ?
                                <ListGroup className={"HOMEPAGE-projects-list mt-3"} as="ol" numbered={true}>
                                {
                                    best.map((project, key) =>
                                        <>
                                            <ListGroupItem key={key} className={"mb-3"} as="li">
                                                <ShownProjets project={project}/>
                                            </ListGroupItem>
                                        </>
                                    )
                                }
                                </ListGroup>
                                :
                                <center>
                                <h2>There are currently recommendations for your</h2>
                                </center>
                            }
                        </>
                </Col>
                <Col className={"col-1"}/>
            </Row>
        </Container>
    );
}

export default Homepage;