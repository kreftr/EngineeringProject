import {Badge, Button, Col, Image, ListGroupItem, Row} from "react-bootstrap";
import default_project_picture from "../assets/images/default-project-picture.jpg"
import React from "react";
import "./Project.css"
import {FaEye, FaRegEdit, FaUserPlus} from "react-icons/fa";

function Project(props){

    return(
        <ListGroupItem>
            <Row>
                <Col className={"col-4"}>
                    {props.project.projectPhoto ?
                        <Image className={"ml-3"}
                               src={`http://localhost:8080/photo?filename=${props.project.projectPhoto}`}
                               width="200px" height="200px"/>
                        :
                        <Image className={"ml-3"} src={default_project_picture}
                               width="200px" height="200px"/>
                    }
                </Col>
                <Col className={"col-5"}>
                    <h2>{props.project.title}</h2>
                    <div className={"PROJECT-introduction"}>{props.project.introduction}</div>
                </Col>
                <Col className={"col-3 PROJECT-button-section"}>
                    <a href={`/project/${props.project.projectId}`}>
                        <Button className={"PROJECT-button mt-3 mb-2"} variant={"primary"}>
                            <FaEye className={"mr-2"} size={35}/>
                            View
                        </Button>
                    </a>
                    <Button className={"PROJECT-button mb-2"} variant={"success"}>
                        <FaUserPlus className={"mr-2"} size={35}/>
                        Invite
                    </Button>
                    <Button className={"PROJECT-button"} variant={"warning"}>
                        <FaRegEdit className={"mr-2"} size={35}/>
                        Settings
                    </Button>
                </Col>
            </Row>
            <Row className={"mt-1"}>
                <Col>
                    {
                        props.project.categories.map((category, key) =>
                            <Badge key={key} className={"mr-2"} bg="primary">{category}</Badge>
                        )
                    }
                </Col>
            </Row>
        </ListGroupItem>
    );

}
export default Project;