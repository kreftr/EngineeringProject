import {Badge, Col, Image, ListGroup, Row} from "react-bootstrap";
import default_project_picture from "../assets/images/default_project_picture.jpg";
import default_profile_picture from "../assets/images/default_profile_picture.jpg";
import "./HomePage.css";
import React, {useEffect} from "react";
import {FaStar} from "react-icons/all";


function ShownProjets(props){

    useEffect(() => {
        console.log(props.project.authorPhoto)
    }, [])

    function profilePhoto(){
        return (
            <a href={`/profile/${props.project.authorId}`}>
                { props.project.authorPhoto ?
                    <Image
                        className={"HOMEPAGE-project-avatar"}
                        src={`http://localhost:8080/photo?filename=${props.project.authorPhoto}`}
                        width="50px" height="50px" rounded={true}/>
                    :
                    <Image className={"HOMEPAGE-project-avatar"} src={default_profile_picture} width="50px" height="50px" rounded={true}/>
                }
            </a>
        );
    }


    return(
        <Row>
            <Col className={"col-3 HOMEPAGE-thumbnail-section HOMEPAGE-clickable"} onClick={() => {window.location.replace(`/project/${props.project.projectId}`)}}>
                { props.project.projectPhoto ?
                    <Image
                        className={"HOMEPAGE-project-projectphoto"}
                        src={`http://localhost:8080/photo?filename=${props.project.projectPhoto}`}
                        width="200px" height="200px"/>
                    :
                    <Image className={"HOMEPAGE-project-projectphoto"} src={default_project_picture} width="200px" height="200px"/>
                }
            </Col>
            <Col className={"col-4 HOMEPAGE-project-ranking-content-section HOMEPAGE-clickable"}>
                <h2 className={"HOMEPAGE-project-title"}>{props.project.title}</h2>
                <div className={"HOMEPAGE-project-introduction"}>{props.project.introduction}</div>
            </Col>
            <Col className={"col-2"}>
                <h3 className={"HOMEPAGE-project-author-text"}>Author: { profilePhoto() }</h3>
                <h3>Rating: <Badge className={"HOMEPAGE-project-rating-badge"} pill bg="primary">{props.project.averageRating ?
                    props.project.averageRating : "No Votes"}
                    <FaStar className={"ml-1"}/>
                </Badge></h3>
                <h3 className={"HOMEPAGE-project-votes-text"}>Votes: {props.project.numberOfVotes}</h3>
            </Col>
            <Col className={"col-3"}>
                <h3>Categories: </h3>
                <ListGroup.Item className={"HOMEPAGE-project-categories"}>
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