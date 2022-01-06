import {Button, Col, Image, ListGroupItem, Row} from "react-bootstrap";
import default_project_picture from "../assets/images/default_project_picture.jpg"
import React from "react";
import "./Project.css"
import {FaCogs, FaEye, FaFileAlt} from "react-icons/fa";

function Project(props){

    return(
        <ListGroupItem>
            <Row>
                <Col className={"col-3 PROJECT-thumbnail-section"}>
                    {props.project.projectPhoto ?
                        <Image
                               src={`http://localhost:8080/photo?filename=${props.project.projectPhoto}`}
                               width="200px" height="200px"/>
                        :
                        <Image src={default_project_picture}
                               width="200px" height="200px"/>
                    }
                </Col>
                <Col className={"col-6 PROJECT-content-section"}>
                    <h2>{props.project.title}</h2>
                    <div className={"PROJECT-introduction"}>{props.project.introduction}</div>
                </Col>
                <Col className={"col-3 PROJECT-button-section b"}>
                    <a href={`/project/${props.project.projectId}`}>
                        <Button className={"PROJECT-button mt-3 mb-2"} variant={"primary"}>
                            <FaEye className={"mr-2"} size={35}/>
                            View
                        </Button>
                    </a>
                    <Button className={"PROJECT-button mb-2"} variant={"success"}>
                        <FaFileAlt className={"mr-2"} size={35}/>
                        Workspace
                    </Button>
                    <Button className={"PROJECT-button"} variant={"danger"}>
                        <FaCogs className={"mr-2"} size={35}/>
                        Settings
                    </Button>
                </Col>
            </Row>
        </ListGroupItem>
    );

}
export default Project;