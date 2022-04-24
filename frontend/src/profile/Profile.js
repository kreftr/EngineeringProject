import React, {useEffect, useState} from "react";
import {Container, Tabs, Row, Col, Image, Button, Tab, Badge, Card} from "react-bootstrap";
import default_profile_picture from "../assets/images/default_profile_picture.jpg"
import default_project_picture from "../assets/images/default_project_picture.jpg"
import {FaCog, FaUserPlus, FaUserMinus, FaUserClock, FaUserFriends} from "react-icons/fa"
import axios from "axios";
import Cookies from "js-cookie"
import {useParams} from "react-router-dom";

import "./Profile.css"


function Profile(){

    const {id} = useParams();

    const [projects, setProjects] = useState([]);

    const [friendStatus, setFriendStatus] = useState(null);

    const [profile, setProfile] = useState([]);
    const [profileCode,setProfileCode] = useState();
    const [loadingContent, setLoading] = useState(true);
    const [responseMessage, setMessage]  = useState("Loading content...");

    useEffect(() => {
        //Load profile
        axios.get(`http://localhost:8080/profile?id=${id}`)
            .then(response => {
                setProfileCode(response.status);
                setProfile(response.data);
                setLoading(false);
            })
            .catch(err => {
                setProfileCode(err.response.status)
                if (err.response.status === 404) setMessage("Profile not found");
                else setMessage("Server error!");
                setLoading(false);
            })

        //Load user's projects
        axios.get(`http://localhost:8080/project/getAllProjects/${id}`,
            {headers: {'Authorization': Cookies.get("authorization")}
        }).then(response => {
            setProjects(response.data)
        }).catch(err => {
            console.log(err.response)
        })

        //Check if is friend
        if (Cookies.get("userId")!==id){
            axios.get(`http://localhost:8080/friends/getFriendStatus/${id}`,
                {headers:{'Authorization': Cookies.get("authorization")}
            })
                .then(response  => {
                    setFriendStatus(response.data)
                })
                .catch(err => {
                    console.log(err.response)
                })
        }
    },[id]);

    function addFriend(){
        axios.post(`http://localhost:8080/friends/addFriend/${id}`,{},
            {headers:{'Authorization': Cookies.get("authorization")}
            })
            .then(response => {
                setFriendStatus("PENDING")
            })
            .catch(err =>  {
                setFriendStatus(null)
                console.log(err.response)
            });
    }

    function removeFriend(){
        axios.delete(`http://localhost:8080/friends/deleteById/${id}`, {
            headers: {
                'Authorization': Cookies.get("authorization")
            }
        }).then(response => {
            setFriendStatus("NOT_FRIEND")
        }).catch(err => {
            console.log(err.response)
        })
    }


    return(
        <Container className={"PROFILE-profile-container"}>
            {profileCode === 200 && !loadingContent ?
                <Row>
                    <Col className={"col-4 PROFILE-info-col"}>
                        {!profile.profile_photo  ?
                            <Image className={"PROFILE-profile-pic"} src={default_profile_picture} roundedCircle={true}/>
                            :
                            <Image className={"PROFILE-profile-pic"}
                                   src={`http://localhost:8080/photo?filename=${profile.profile_photo}`}
                                   roundedCircle={true}/>
                        }
                        <span className={"PROFILE-username"}>{profile.username}</span>
                        <span className={"PROFILE-name-surname"}>{profile.name} {profile.surname}</span>
                        <span className={"PROFILE-bio mt-2 mb-3"}>{profile.bio}</span>
                        {profile.categories.length > 0 ? <h4>Interested in:</h4> : <></>}
                        { profile.categories.length > 0 ?
                            <div className={"mb-2 ml-5 mr-5"}>
                            { profile.categories.map((category, key) =>
                                <Badge key={key} className={"mr-1"} bg="primary">{category}</Badge>
                                )}
                            </div>
                            :
                            <></>
                        }
                        <ul className={"PROFILE-ul-actions"}>
                            { Cookies.get("userId")===id && Cookies.get("authorization") ?
                                <li>
                                    <a href={"/profile/settings"}>
                                        <Button className={"PROFILE-button-small"}>
                                            <FaCog className={"PROFILE-icon"}/>
                                            <h4>Settings</h4>
                                        </Button>
                                    </a>
                                </li>
                                :
                                <></>
                            }
                            { Cookies.get("userId")!==id && Cookies.get("authorization") ?
                                <>
                                    <li>
                                        <a href={`/friends/${id}`}>
                                            <Button className={"PROFILE-button-small"}>
                                                <FaUserFriends className={"PROFILE-icon"}/>
                                                <h4>Friends</h4>
                                            </Button>
                                        </a>
                                    </li>
                                    <li>
                                        { friendStatus === "NOT_FRIEND" ?
                                            <Button className={"PROFILE-button-big"} onClick={addFriend}>
                                                <FaUserPlus className={"PROFILE-icon"}/>
                                                <h4>Add friend</h4>
                                            </Button>
                                        : friendStatus === "FRIEND" ?
                                            <Button variant={"danger"} className={"PROFILE-button-big"} onClick={removeFriend}>
                                                <FaUserMinus className={"PROFILE-icon"}/>
                                                <h5>Remove friend</h5>
                                            </Button>
                                        : friendStatus === "PENDING" ?
                                            <Button variant={"secondary"} className={"PROFILE-button-big"} disabled={true}>
                                                <FaUserClock className={"PROFILE-icon"}/>
                                                <h5>Pending request</h5>
                                            </Button>
                                        :
                                            <></>
                                        }
                                    </li>
                                </>
                                :
                                <></>
                            }
                        </ul>
                    </Col>
                    <Col className={"col-8"}>
                        <Tabs defaultActiveKey="Projects" className="mb-5" fill>
                            <Tab eventKey="Projects" title="Projects">
                                { projects.length > 0 ?
                                    <div className={"mr-5 ml-5 PROFILE-projects-window"}>
                                        { projects.map((project, key) =>
                                            <Card key={key} className={"mb-3"}>
                                                <Card.Body>
                                                    <a className={"PROFILE-href-styling"} href={`/project/${project.projectId}`}>
                                                        <Row>
                                                            <Col className={"col-2"}>
                                                                { project.projectPhoto ?
                                                                    <Image className={"PROFILE-project-image"} src={`http://localhost:8080/photo?filename=${project.projectPhoto}`} width={125} height={125}/>
                                                                    :
                                                                    <Image src={default_project_picture} width={125} height={125}/>
                                                                }
                                                            </Col>
                                                            <Col className={"col-10 PROFILE-project-text-padding"}>
                                                                <Row>
                                                                    { project.title.length > 49 ?
                                                                        <h5>{project.title.slice(0,49)+"..."}</h5>
                                                                        :
                                                                        <h5>{project.title}</h5>
                                                                    }

                                                                </Row>
                                                                <Row className={"PROFILE-project-introduction"}>
                                                                    { project.introduction.length > 350 ?
                                                                        <span>{project.introduction.slice(0, 350)+"..."}</span>
                                                                        :
                                                                        <span>{project.introduction}</span>
                                                                    }
                                                                </Row>
                                                            </Col>
                                                        </Row>
                                                    </a>
                                                </Card.Body>
                                            </Card>
                                        )
                                        }
                                    </div>
                                    :
                                    <center>
                                        <h2>{profile.username} does not have any projects</h2>
                                    </center>
                                }
                            </Tab>
                        </Tabs>
                    </Col>
                </Row>
                :
                <Row>
                    <Col className={"PROFILE-info-col"}>
                        {!loadingContent ?
                            <Image className={"PROFILE-profile-pic"}
                                   src={"https://c.tenor.com/kHcmsxlKHEAAAAAC/rock-one-eyebrow-raised-rock-staring.gif"}
                                   roundedCircle={true}/> : <span/>
                        }
                        <h1>{responseMessage}</h1>
                    </Col>
                </Row>
            }
        </Container>
    );
}
export default Profile;