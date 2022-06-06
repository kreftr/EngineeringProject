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
                        className={"RANKEDPROJECT-avatar"}
                        src={`http://localhost:8080/photo?filename=${props.project.authorPhoto}`} rounded={true}
                    />
                    :
                    <Image className={"RANKEDPROJECT-avatar"} src={default_profile_picture} rounded={true}/>
                }
            </a>
        );
    }

    return(
            <Row>
                <Col className={"col-3 RANKING-thumbnail-section RANKING-clickable"} onClick={() => {window.location.replace(`/project/${props.project.projectId}`)}}>
                    {props.project.projectPhoto ?
                        <Image className={"RANKEDPROJECT-projectphoto"}
                            src={`http://localhost:8080/photo?filename=${props.project.projectPhoto}`}
                        />
                        :
                        <Image className={"RANKEDPROJECT-projectphoto"} src={default_project_picture}/>
                    }
                </Col>
                <Col className={"col-4"}>
                    <h3 className={"RANKEDPROJECT-rating-text"}>Rating:</h3>
                    <Badge className={"RANKEDPROJECT-rating-badge"}
                           pill bg="primary">{props.project.averageRating ? props.project.averageRating : "No Votes"}
                        <FaStar className={"ml-1"}/>
                    </Badge>
                    <h3 className={"RANKEDPROJECT-author-text"}>Author: {profilePhoto()}</h3>
                    <h3 className={"RANKEDPROJECT-votes-text"}>Votes: {props.project.numberOfVotes}</h3>
                    <ListGroup.Item className={"PROJECT-category-list"}>
                        {
                            props.project.categories.sort().map((category, key) =>
                                <Badge key={key} className={"mr-1"} bg="primary">{category}</Badge>
                            )
                        }
                    </ListGroup.Item>
                </Col>
                <Col className={"col-5 PROJECT-content-section RANKING-clickable"} onClick={() => {window.location.replace(`/project/${props.project.projectId}`)}}>
                    <h2>{props.project.title.slice(0,19)}</h2>
                    <div className={"PROJECT-introduction"}>{props.project.introduction.slice(0,150)}</div>
                </Col>
            </Row>
    );

}
export default RankedProject;