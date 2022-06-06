import {Col, Container, ListGroup, ListGroupItem, Row} from "react-bootstrap";
import "./Ranking.css"
import React, {useEffect, useState} from "react";
import axios from "axios";
import RankedProject from "./RankedProject";

function Ranking(){

    const [best, setBest] = useState([]);

    useEffect(() => {

        axios.get(`http://localhost:8080/project/ranking`
        ).then(response => {
            setBest(response.data)
        }).catch(err => {
            console.log(err.response)
        })

    },[best])


    return(
        <Container className={"mt-3 mb-3"}>
            <Row>
                <Col className={"col-2"}/>
                <Col className={"col-8"}>
                    <h1 className={"RANKING-title mb-4"}>Top 10 Projects</h1>
                    <hr/>
                    { best.length > 0 ?
                        <ListGroup className={"RANKING-projects-list mt-3"} as="ol" numbered={true}>
                            {
                                best.map((project, key) =>
                                    <>
                                        <ListGroupItem key={key} className={"mb-3"} as="li">
                                            <RankedProject project={project}/>
                                        </ListGroupItem>
                                        <div/>
                                    </>
                                )
                            }
                        </ListGroup>
                        :
                        <center>
                            <h2>There are currently no voted projects</h2>
                        </center>
                    }
                </Col>
                <Col className={"col-2"}/>
            </Row>
        </Container>
    );
}
export default Ranking;