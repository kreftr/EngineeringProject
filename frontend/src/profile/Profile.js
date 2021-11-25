import React, {useEffect, useState} from "react";
import {Container, Nav, Row, Col, Image} from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css"
import default_profile_picture from "./assets/images/default_profile_picture.jpg"
import "./Profile.css"
import axios from "axios";
import {useParams} from "react-router-dom";

function Profile(){

    const {id} = useParams();

    const [profile,setProfile] = useState([]);
    const [responseCode,setCode] = useState();
    const [loadingContent, setLoading] = useState(true);
    const [responseMessage, setMessage]  = useState("Loading content...");

    useEffect(() => {
        //TODO: FIX React Hook useEffect has a missing dependency: 'id'.
        // Either include it or remove the dependency array  react-hooks/exhaustive-deps
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
        <Container className={"profile-container"}>
            {responseCode === 200 && !loadingContent ?
                <Row>
                    <Col className={"col-4 info-col"}>
                        {!profile.profile_photo  ?
                            <Image className={"profile-pic"} src={default_profile_picture} roundedCircle={true}/>
                            :
                            <Image className={"profile-pic"}
                                   src={`http://localhost:8080/photo?filename=${profile.profile_photo}`}
                                   roundedCircle={true}/>
                        }
                        <span className={"username"}>{profile.username}</span>
                        <span className={"name-surname"}>{profile.name} {profile.surname}</span>
                        <div className={"bio"}>{profile.bio}</div>
                    </Col>
                    <Col className={"col-8"}>
                        <Nav fill variant="tabs" defaultActiveKey="/home">
                            <Nav.Item>
                                <Nav.Link href="/home" active>Projekty</Nav.Link>
                            </Nav.Item>
                            <Nav.Item>
                                <Nav.Link eventKey="link-1">Aktywność</Nav.Link>
                            </Nav.Item>
                        </Nav>
                    </Col>
                </Row>
                :
                <Row>
                    <Col className={"info-col"}>
                        {!loadingContent ?
                            <Image className={"profile-pic"}
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