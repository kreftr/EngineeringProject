import {Col, Container, Row, Form, FloatingLabel, Button, Image, Card} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import axios from "axios";
import "./Search.css"
import default_profile_picture from "../assets/images/default_profile_picture.jpg"
import default_project_picture from "../assets/images/default_project_picture.jpg"

function Search(){

    const [input, setInput] = useState("");
    const [inputType, setInputType] = useState("profile");
    const [categories, setCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState("AGRICULTURAL_ENGINEERING");

    const [profiles,setProfiles] = useState([]);
    const [projects,setProjects] = useState([]);
    const [loadingContent, setLoading] = useState(true);
    const [responseMessage, setMessage]  = useState();

    useEffect(() => {
        axios.get("http://localhost:8080/categories/getAll"
        ).then(response => {
            setCategories(response.data)
        }).catch(err => {
            console.log(err.response)
        })
    }, [])

    function clearData(){
        setProfiles([])
        setProjects([])
        setMessage("")
    }

    async function handleSubmit(e){
        e.preventDefault()
        clearData()

        let url = "http://localhost:8080/"
        if (inputType === "profile") url = `http://localhost:8080/${inputType}?username=${input}`
        else if (inputType === "project") url = `http://localhost:8080/${inputType}/getProjectByName/${input}`
        else if (inputType === "category") url = `http://localhost:8080/project/getProjectByCategory/${selectedCategory}`
        else url = `http://localhost:8080/${inputType}?title=${input}`

        await axios.get(url)
            .then(response => {
                if (inputType === "profile") setProfiles(response.data)
                else if (inputType === "project" || inputType === "category") setProjects(response.data)
            })
            .catch(err => {
                console.log(err.response)
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
                        <Row className={"align-items-center"}>
                            <Col className={"SEARCH-search-button-col"}>
                                <Button type={"submit"} variant={"primary"} size={"lg"}>Search</Button>
                            </Col>
                            <Col className={"col-6"}>
                                { inputType !== "category" && !categories.includes(inputType) ?
                                    <Form.Floating>
                                        <Form.Control value={input}
                                                      onChange={(e) => setInput(e.target.value)}
                                                      type={"text"} placeholder={"sample text"} required/>
                                        <label htmlFor={"floatingInputCustom"}>Search...</label>
                                    </Form.Floating>
                                    :
                                    <FloatingLabel className="mb-3" label={"Choose project category"}>
                                        <Form.Select value={selectedCategory}
                                                     onChange={(e) => setSelectedCategory(e.target.value)}>
                                            { categories.map((category, key) =>
                                                <option key={key} value={category}>{category}</option>
                                            )
                                            }
                                        </Form.Select>
                                    </FloatingLabel>
                                }
                            </Col>
                            <Col className={"col-4"}>
                                <FloatingLabel label={"What are you looking for?"}>
                                    <Form.Select value={inputType}
                                                 onChange={(e) => setInputType(e.target.value)}>
                                        <option value={"profile"}>User</option>
                                        <option value={"project"}>Project</option>
                                        <option value={"category"}>Project by category</option>
                                    </Form.Select>
                                </FloatingLabel>
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
                            { inputType === "profile" ?
                                profiles.map((profile) => {
                                    return (
                                        <div>
                                            <a className={"SEARCH-profile-holder"}
                                               href={`http://localhost:3000/profile/${profile.id}`}>
                                                {profile.profile_photo ?
                                                    <Image className={"SEARCH-profile-pic"}
                                                           src={`http://localhost:8080/photo?filename=${profile.profile_photo}`}
                                                           roundedCircle={true}/>
                                                    :
                                                    <Image className={"SEARCH-profile-pic"}
                                                           src={default_profile_picture} roundedCircle={true}/>
                                                }
                                                <span className={"SEARCH-username"}>{profile.username}</span>
                                                {profile.name ?
                                                    <span
                                                        className={"SEARCH-name-surname"}> {profile.name} {profile.surname} </span>
                                                    :
                                                    <span className={"SEARCH-name-surname"}>&nbsp;</span>
                                                }
                                            </a>
                                        </div>
                                    )})
                            : inputType === "project" || inputType === "category" ?
                                projects.map((project, key) => {
                                    return(
                                        <Card  key={key} className={"mt-3 ml-2 mr-2 flex-box"}>
                                            { project.projectPhoto ?
                                                <Card.Img variant="top" width={"250px"} height={"250px"} src={`http://localhost:8080/photo?filename=${project.projectPhoto}`} />
                                                :
                                                <Card.Img variant="top" width={"250px"} height={"250px"} src={default_project_picture} />
                                            }
                                            <Card.Body>
                                                <Card.Title>
                                                    <div className={"SEARCH-title-height"}>
                                                        {project.title}
                                                    </div>
                                                </Card.Title>
                                                <Card.Text>
                                                    <div className={"SEARCH-intro-height"}>
                                                        {project.introduction.slice(0,150)+"..."}
                                                    </div>
                                                </Card.Text>
                                                <Row className={"mr-2 ml-2"}>
                                                    <Button href={`/project/${project.projectId}`} className={"align-self: flex-end;"} variant="primary">View</Button>
                                                </Row>
                                            </Card.Body>
                                        </Card>
                                    )
                                })
                            :
                                    <></>
                            }
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