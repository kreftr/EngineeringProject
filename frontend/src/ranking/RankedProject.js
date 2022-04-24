import {Badge, Col, Image, ListGroup, Row} from "react-bootstrap";
import default_project_picture from "../assets/images/default_project_picture.jpg";
import default_profile_picture from "../assets/images/default_profile_picture.jpg";
import "./Ranking.css"
import React from "react";
import {FaStar} from "react-icons/all";


function RankedProject(props){

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
                <Col className={"col-3 RANKING-thumbnail-section RANKING-clickable"} onClick={() => {window.location.replace(`/project/${props.project.projectId}`)}}>
                    {props.project.projectPhoto ?
                        <Image
                            src={`http://localhost:8080/photo?filename=${props.project.projectPhoto}`}
                            width="200px" height="200px"/>
                        :
                        <Image src={default_project_picture}
                               width="200px" height="200px"/>
                    }
                </Col>
                <Col className={"col-4"}>
                    <h3>Rating: <Badge pill bg="primary">{props.project.averageRating ? props.project.averageRating : "No Votes"}<FaStar className={"ml-1"}/></Badge></h3>
                    <h3>Votes: {props.project.numberOfVotes}</h3>
                    <h3>Author: {profilePhoto()}</h3>
                    <ListGroup.Item className={"PROJECT-catergory-list"}>
                        {
                            props.project.categories.sort().map((category, key) =>
                                <Badge key={key} className={"mr-1"} bg="primary">{category}</Badge>
                            )
                        }
                    </ListGroup.Item>
                </Col>
                <Col className={"col-5 PROJECT-content-section RANKING-clickable"} onClick={() => {window.location.replace(`/project/${props.project.projectId}`)}}>
                    <h2>{props.project.title}</h2>
                    <div className={"PROJECT-introduction"}>{props.project.introduction}</div>
                </Col>
            </Row>
    );

}
export default RankedProject;