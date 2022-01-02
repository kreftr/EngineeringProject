import {Badge, Button, Col, Container, Image, Row} from "react-bootstrap";
import {FaFacebookSquare, FaGithubSquare, FaKickstarter, FaYoutube} from "react-icons/all";
import "./ProjectView.css"
import default_project_picture from "../assets/images/default-project-picture.jpg"
import default_profile_photo from "../assets/images/default_profile_picture.jpg"
import React, {useEffect, useState} from "react";
import Rating from "./Rating";
import axios from "axios";
import {useParams} from "react-router-dom";


function ProjectView(){

    const {id} = useParams();

    const [project, setProject] = useState();
    const [statusCode, setStatusCode] = useState();

    useEffect(() => {

        axios.get(`http://localhost:8080/project/getProjectById/${id}`
        ).then(response => {
            setProject(response.data)
            setStatusCode(response.status)
            console.log(response.data)
        }).catch(err => {
            console.log(err.response)
            setStatusCode(err.response.status)
        })

    }, [])


    function authorSection(){
        return(
            <div>
                <a className={"PROJECT-VIEW-author-holder"} href={`/profile/${project.authorId}`}>
                    { project.authorPhoto ?
                        <Image className={"PROJECT-VIEW-author-picture"}
                               src={`http://localhost:8080/photo?filename=${project.authorPhoto}`}
                               roundedCircle={true}/>
                        :
                        <Image className={"PROJECT-VIEW-author-picture"}
                        src={default_profile_photo}
                        roundedCircle={true}/>
                    }
                    <h2 className={"ml-2"}>{project.authorUsername}</h2>
                </a>
            </div>
        )
    }



    return(
        <Container className={"mt-5"}>
            <Row>
                <Col className={"col-2"}/>
                <Col className={"col-8"}>
                    { statusCode === 200 ?
                        <div>
                            <Row className={"mb-4"}>
                                <Col>
                                    { project.projectPhoto ?
                                        <div className={"PROJECT-VIEW-project-picture-col"}>
                                            <Image className={"PROJECT-VIEW-project-picture"}
                                                src={`http://localhost:8080/photo?filename=${project.projectPhoto}`}/>
                                        </div>
                                        :
                                        <div className={"PROJECT-VIEW-project-picture-col"}>
                                            <Image className={"PROJECT-VIEW-project-picture"}
                                                src={default_project_picture}/>
                                        </div>
                                    }
                                </Col>
                                <Col>
                                    <div className={"PROJECT-VIEW-base-info"}>
                                        <h1 className={"PROJECT-VIEW-title"}>
                                            {project.title}
                                        </h1>
                                        <hr className={"mb-3"}/>
                                        <h4 className={"PROJECT-VIEW-subtitle"}>
                                            {project.introduction}
                                        </h4>
                                    </div>
                                </Col>
                            </Row>
                            <Row className={"mt-4"}>
                                <Col>
                                    <Row className={"ml-5 mr-5"}>
                                            <Button className={"mb-3"} variant="primary">Join</Button>
                                            <Button variant="primary">Report</Button>
                                    </Row>
                                    <Row className={"mt-3"}>
                                        <Rating/>
                                    </Row>
                                    <Row>
                                        <Col>
                                        {
                                            project.categories.map((category, key) =>
                                                <Badge key={key} className={"mr-1"} bg="primary">{category}</Badge>
                                            )
                                        }
                                        </Col>
                                    </Row>
                                </Col>
                                <Col>
                                    <Row className={"PROJECT-VIEW-author-col mb-4"}>
                                        {authorSection()}
                                    </Row>
                                    <Row>
                                        <Col>
                                            <Row>
                                                <div>
                                                    <Col>
                                                        <h1>81%</h1>
                                                    </Col>
                                                    <Col>
                                                        <span>of voters fond this project interesting</span>
                                                    </Col>
                                                </div>
                                            </Row>
                                        </Col>
                                        <Col>
                                            <span>Creation date: </span>
                                            <h5>{project.creationDate}</h5>
                                        </Col>
                                    </Row>
                                </Col>
                            </Row>
                            <hr className={"mb-4 mt-4"}/>
                            <Row>
                                <h1 className={"mb-4"}>About project</h1>
                                <span>
                                    {project.description}
                                </span>
                            </Row>
                            <hr className={"mb-4 mt-4"}/>
                            <Row>
                                <h1 className={"mb-3"}>Media</h1>
                                <div className={"PROJECT-VIEW-media"}>
                                    {project.youtubeLink ?
                                        <Col>
                                            <a href={`https://${project.youtubeLink}`}>
                                                <FaYoutube color={"black"} size={125}/>
                                            </a>
                                        </Col>
                                        :
                                        <></>
                                    }
                                    { project.githubLink ?
                                        <Col>
                                            <a href={`https://${project.githubLink}`}>
                                                <FaGithubSquare color={"black"} size={125}/>
                                            </a>
                                        </Col>
                                        :
                                        <></>
                                    }
                                    { project.facebookLink ?
                                        <Col>
                                            <a href={`https://${project.facebookLink}`}>
                                                <FaFacebookSquare color={"black"} size={125}/>
                                            </a>
                                        </Col>
                                        :
                                        <Col></Col>
                                    }
                                    { project.kickstarterLink ?
                                        <a href={`https://${project.kickstarterLink}`}>
                                            <Col>
                                                <FaKickstarter color={"black"} size={125}/>
                                            </Col>
                                        </a>
                                        :
                                        <Col></Col>
                                    }
                                </div>
                            </Row>
                            <hr className={"mb-4 mt-4"}/>
                            <Row>
                                <h1>Comments</h1>
                            </Row>
                        </div>
                        :
                        <h2>Project not found</h2>
                    }
                </Col>
                <Col className={"col-2"}/>
            </Row>
        </Container>
    )
}
export default ProjectView