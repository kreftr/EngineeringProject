import React, {useEffect, useState} from "react";
import {Container, Nav, Row, Col, Image, Button} from "react-bootstrap";
import default_profile_picture from "../assets/images/default_profile_picture.jpg"
import {FaFlag, FaUserEdit, FaUserPlus} from "react-icons/fa"
import axios from "axios";
import Cookies from "js-cookie"
import {useParams} from "react-router-dom";

import "./Profile.css"

function Profile(){

    const {id} = useParams();

    const [profile, setProfile] = useState([]);
    const [responseCode,setCode] = useState();
    const [loadingContent, setLoading] = useState(true);
    const [responseMessage, setMessage]  = useState("Loading content...");

    useEffect(() => {
        // TODO: FIX React Hook useEffect has a missing dependency: 'id'.

        axios.get(`http://localhost:8080/profile?id=${id}`)
            .then(response => {
                setCode(response.status);
                setProfile(response.data);
                setLoading(false);
            })
            .catch(err => {
                setCode(err.response.status)
                if (err.response.status === 404) setMessage("Profile not found");
                else setMessage("Server error!");
                setLoading(false);
            })
    },[]);


    return(
        <Container className={"PROFILE-profile-container"}>
            {responseCode === 200 && !loadingContent ?
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
                        <div className={"PROFILE-bio"}>{profile.bio}</div>
                        <ul className={"PROFILE-ul-actions"}>
                            { Cookies.get("userId")===id && Cookies.get("authorization") ?
                                <li>
                                    <Button className={"PROFILE-icon-placeholder"}>
                                        <FaUserEdit className={"PROFILE-icon"}/>
                                        <h4>Edit profile</h4>
                                    </Button>
                                </li>
                                :
                                <></>
                            }
                            { Cookies.get("userId")!==id && Cookies.get("authorization") ?
                                <>
                                    <li>
                                        <Button className={"PROFILE-icon-placeholder"}>
                                            <FaUserPlus className={"PROFILE-icon"}/>
                                            <h4>Add friend</h4>
                                        </Button>
                                    </li>
                                    <li>
                                    <Button className={"PROFILE-icon-placeholder"}>
                                    <FaFlag className={"PROFILE-icon"}/>
                                    <h4>Report</h4>
                                    </Button>
                                    </li>
                                </>
                                :
                                <></>
                            }
                        </ul>
                    </Col>
                    <Col className={"col-8"}>
                        <Nav fill variant="tabs" defaultActiveKey="/home">
                            <Nav.Item>
                                <Nav.Link href="/home" active>Projects</Nav.Link>
                            </Nav.Item>
                            <Nav.Item>
                                <Nav.Link eventKey="link-1">Activity</Nav.Link>
                            </Nav.Item>
                        </Nav>
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