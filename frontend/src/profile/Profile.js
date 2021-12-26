import React, {useEffect, useState} from "react";
import {Container, Tabs, Row, Col, Image, Button, Tab} from "react-bootstrap";
import default_profile_picture from "../assets/images/default_profile_picture.jpg"
import {FaFlag, FaCog, FaUserPlus, FaUserMinus, FaUserClock, FaUserFriends} from "react-icons/fa"
import axios from "axios";
import Cookies from "js-cookie"
import {useParams} from "react-router-dom";

import "./Profile.css"

function Profile(){

    const {id} = useParams();

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
                        <ul className={"PROFILE-ul-actions"}>
                            { Cookies.get("userId")===id && Cookies.get("authorization") ?
                                <li>
                                    <a href={"/profile/settings"}>
                                        <Button className={"PROFILE-icon-placeholder"}>
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
                                        { friendStatus === "NOT_FRIEND" ?
                                            <Button className={"PROFILE-icon-placeholder"} onClick={addFriend}>
                                                <FaUserPlus className={"PROFILE-icon"}/>
                                                <h4>Add friend</h4>
                                            </Button>
                                        : friendStatus === "FRIEND" ?
                                            <Button variant={"danger"} className={"PROFILE-icon-placeholder"} onClick={removeFriend}>
                                                <FaUserMinus className={"PROFILE-icon"}/>
                                                <h5>Remove friend</h5>
                                            </Button>
                                        : friendStatus === "PENDING" ?
                                            <Button variant={"secondary"} className={"PROFILE-icon-placeholder"} disabled={true}>
                                                <FaUserClock className={"PROFILE-icon"}/>
                                                <h5>Pending friend request</h5>
                                            </Button>
                                        :
                                            <></>
                                        }
                                    </li>
                                    <li>
                                        <a href={`/friends/${id}`}>
                                            <Button className={"PROFILE-icon-placeholder"}>
                                                <FaUserFriends className={"PROFILE-icon"}/>
                                                <h4>Friends</h4>
                                            </Button>
                                        </a>
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
                        <Tabs defaultActiveKey="Projects" className="mb-5" fill>
                            <Tab eventKey="Projects" title="Projects">
                            </Tab>
                            <Tab eventKey="Activity" title="Activity">
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