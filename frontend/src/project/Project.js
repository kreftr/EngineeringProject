import {Button, Col, Image, Row} from "react-bootstrap";
import default_project_picture from "../assets/images/default_project_picture.jpg"
import React, {useEffect, useState} from "react";
import Cookies from "js-cookie";
import "./Project.css"
import {FaCogs, FaEye, FaFileAlt, FaWindowClose} from "react-icons/fa";
import axios from "axios";

function Project(props){

    function leave(id){
        axios.post(`http://localhost:8080/project/leave/${id}`,null,{headers:{
                'Authorization': Cookies.get("authorization")
        }}).then(response => {
            window.location.reload();
        }).catch(err => {
            console.log(err.response)
        })
    }

    return(
            <Row className={"PROJECT-section"}>
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
                    <h2>{props.project.title.slice(0,22)}</h2>
                    <div className={"PROJECT-introduction"}>
                        {props.project.introduction.length > 150 ? props.project.introduction.slice(0,150)+"..." : props.project.introduction}</div>
                </Col>
                <Col className={"col-3 PROJECT-button-section b"}>
                    <a href={`/project/${props.project.projectId}`}>
                        <Button className={"PROJECT-button mt-3 mb-2"} variant={"primary"}>
                            <FaEye className={"mr-2"} size={35}/>
                            View
                        </Button>
                    </a>
                    <Button className={"PROJECT-button mb-2"} variant={"primary"} href={`/project/${props.project.projectId}/workspace`}>
                        <FaFileAlt className={"mr-2"} size={35}/>
                        Workspace
                    </Button>
                    { props.project.authorId === Number(Cookies.get("userId")) ?
                        <Button className={"PROJECT-button"} variant={"primary"} href={`project/projectSettings/${props.project.projectId}`}>
                            <FaCogs className={"mr-2"} size={35}/>
                            Settings
                        </Button>
                        :
                        <Button className={"PROJECT-button"} variant={"danger"} onClick={() => leave(props.project.projectId)}>
                            <FaWindowClose className={"mr-2"} size={35}/>
                            Leave
                        </Button>
                    }
                </Col>
            </Row>
    );

}
export default Project;