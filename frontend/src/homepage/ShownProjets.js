import {Badge, Col, Image, ListGroup, Row} from "react-bootstrap";
import default_project_picture from "../assets/images/default_project_picture.jpg";
import default_profile_picture from "../assets/images/default_profile_picture.jpg";
import "./HomePage.css";
import React from "react";
import {FaStar} from "react-icons/all";


function ShownProjets(props){

    function profilePhoto(){
        return(
            <a href={`/profile/${props.project.authorId}`}>
                {props.project.authorPhoto ?
                    <Image
                        src={`http://localhost:8080/photo?filename=${props.project.authorPhoto}`}
                        width="50px" height="50px" rounded={true}/>
                    :
                    <Image src={default_profile_picture} width="50px" height="50px" rounded={true}/>
                }
            </a>
        );
    }

    return(
        <Row>
            <Col className={"col-2 HOMEPAGE-ranking-thumbnail HOMEPAGE-clickable"}onClick={() => {window.location.replace(`/project/${props.project.projectId}`)}}>
                {props.project.projectPhoto ?
                    <Image
                        src={`http://localhost:8080/photo?filename=${props.project.projectPhoto}`}
                        width="200px" height="200px"/>
                    :
                    <Image src={default_project_picture}
                           width="200px" height="200px"/>
                }
            </Col>
            <Col className={"col-4 HOMEPAGE-ranking-content-section HOMEPAGE-clickable"}>
                <h2 className={"HOMEPAGE-project-title"}>{props.project.title}</h2>
                <div className={"HOMEPAGE-project-introduction"}>{props.project.introduction}</div>
            </Col>
            <Col className={"col-3"}>
                <h3>Author: {profilePhoto()}</h3>
                <h3>Rating: <Badge pill bg="primary">{props.project.averageRating ?
                    props.project.averageRating : "No Votes"}
                    <FaStar className={"ml-1"}/>
                </Badge></h3>
                <h3>Votes: {props.project.numberOfVotes}</h3>
            </Col>
            <Col className={"col-3"}>
                <h3>Categories: </h3>
                <ListGroup.Item>
                    {
                        props.project.categories.map((category, key) =>
                            <Badge key={key} className={"mr-1"} bg="primary">{category}</Badge>
                        )
                    }
                </ListGroup.Item>
            </Col>
        </Row>
    );
}
export default ShownProjets;