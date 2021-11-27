import {Col, Container, Row, Form, FloatingLabel, Button, Image} from "react-bootstrap";
import React, {useState} from "react";
import axios from "axios";
import "./Search.css"
import default_profile_picture from "../assets/images/default_profile_picture.jpg"

function Search(){

    const [input, setInput] = useState("");
    const [inputType, setInputType] = useState("profile");

    const [profiles,setProfiles] = useState([]);
    const [projects,setProjects] = useState([]);
    const [problems,setProblems] = useState([]);
    const [loadingContent, setLoading] = useState(true);
    const [responseMessage, setMessage]  = useState();

    function clearData(){
        setProfiles([])
        setProjects([])
        setProblems([])
        setMessage("")
    }

    async function handleSubmit(e){
        e.preventDefault()
        clearData()

        let url = "http://localhost:8080/"
        if (inputType === "profile") url = `http://localhost:8080/${inputType}?username=${input}`
        else url = `http://localhost:8080/${inputType}?title=${input}`

        await axios.get(url)
            .then(response => {
                if (inputType === "profile") setProfiles(response.data)
                else if (inputType === "project") setProjects(response.data)
                else setProblems(response.data)
            })
            .catch(err => {
                if (err.response.status === 404) setMessage(err.response.data.message)
                else setMessage("Server error!")
            })
        setLoading(false)
    }

    return(
        <Container className={"SEARCH-search-container"}>
            <Row>
                <Col className={"col-2"}></Col>
                <Col className={"col-8"}>
                    <Form onSubmit={handleSubmit}>
                        <Row>
                            <Col className={"col-8"}>
                                <Form.Floating className="mb-3">
                                    <Form.Control value={input}
                                                  onChange={(e) => setInput(e.target.value)}
                                                  type={"text"} placeholder={"sample text"}/>
                                    <label htmlFor={"floatingInputCustom"}>Search...</label>
                                </Form.Floating>
                            </Col>
                            <Col className={"col-4"}>
                                <FloatingLabel label={"What are you looking for?"}>
                                    <Form.Select value={inputType}
                                                 onChange={(e) => setInputType(e.target.value)}>
                                        <option value={"profile"}>User</option>
                                        <option value={"project"} disabled={true}>Project</option>
                                        <option value={"problem"} disabled={true}>Problem</option>
                                    </Form.Select>
                                </FloatingLabel>
                            </Col>
                        </Row>
                        <Row>
                            <Col className={"SEARCH-search-button-col"}>
                                <Button type={"submit"} variant={"primary"} size={"lg"}>Search</Button>
                            </Col>
                        </Row>
                    </Form>
                </Col>
                <Col className={"col-2"}></Col>
            </Row>
            {responseMessage ?
                <h1 className={"SEARCH-message"}>{responseMessage}</h1>
                :
                <span/>
            }
            <Row>
                <Col className={"col-1"}></Col>
                <Col className={"col-10"}>
                    {!loadingContent ?
                        <div className={"SEARCH-grid-container"}>
                            {profiles.map((profile) => {
                                return (
                                    <div>
                                        <a className={"SEARCH-profile-holder"} href={`http://localhost:3000/profile/${profile.id}`}>
                                            {profile.profile_photo ?
                                                <Image className={"SEARCH-profile-pic"}
                                                    src={`http://localhost:8080/photo?filename=${profile.profile_photo}`}
                                                    roundedCircle={true}/>
                                                :
                                                <Image className={"SEARCH-profile-pic"} src={default_profile_picture} roundedCircle={true}/>
                                            }
                                            <span className={"SEARCH-username"}>{profile.username}</span>
                                            {profile.name ?
                                                <span className={"SEARCH-name-surname"}> {profile.name} {profile.surname} </span>
                                                :
                                                <span className={"SEARCH-name-surname"}>&nbsp;</span>
                                            }
                                        </a>
                                    </div>
                                )
                            })}
                        </div>
                        :
                        <div></div>
                    }
                </Col>
                <Col className={"col-1"}></Col>
            </Row>
        </Container>
    );
}
export default Search