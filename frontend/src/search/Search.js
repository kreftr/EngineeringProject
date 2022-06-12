import {Col, Container, Row, Form, FloatingLabel, Button, Image, Card, Pagination} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import axios from "axios";
import "./Search.css"
import default_profile_picture from "../assets/images/default_profile_picture.jpg"
import default_project_picture from "../assets/images/default_project_picture.jpg"
import Cookies from "js-cookie";

function Search(){

    const [input, setInput] = useState("");
    const [inputType, setInputType] = useState("profile");
    const [categories, setCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState("AGRICULTURAL_ENGINEERING");

    const [profiles,setProfiles] = useState([]);
    const [projects,setProjects] = useState([]);
    const [loadingContent, setLoading] = useState(true);
    const [responseMessage, setMessage]  = useState();


    //Pagination
    const [pageNumber, setPageNumber] = useState(1);
    const [projectsNumber, setProjectsNumber] = useState();
    const [profilesNumber, setProfilesNumber] = useState();

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
        setPageNumber(1)
        let url = "http://localhost:8080/"
        if (inputType === "profile") getProfilesPagination(input)
        else if (inputType === "project") getProjectsPagination(input)
        else if (inputType === "category") {
            url = `http://localhost:8080/project/getProjectByCategory/${selectedCategory}`
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
        }
        setLoading(false)
    }

    async function getProjectsPagination(title){
        clearData();

        await axios.get(`http://localhost:8080/project/getProjectsNumberByTitle/${title}`
        ).then(response => {
            setProjectsNumber(response.data)
        })
        .catch(err => {
            if (err.response.status === 404) setMessage(err.response.data.message)
            else setMessage("Server error!")
        })

        await axios.get(`http://localhost:8080/project/getProjectByNameWithPagination/${title}?pageNumber=1&pageSize=3`
        ).then(response => {
            setProjects(response.data)
        })
        .catch(err => {
            if (err.response.status === 404) setMessage(err.response.data.message)
            else setMessage("Server error!")
        })
    }

    async function getProfilesPagination(username) {
        clearData();

        await axios.get(`http://localhost:8080/profile/number?username=${username}`
        ).then(response => {
            setProfilesNumber(response.data)
        })
        .catch(err => {
            if (err.response.status === 404) setMessage(err.response.data.message)
            else setMessage("Server error!")
        })

        await axios.get(`http://localhost:8080/profile/pagination?username=${username}&pageNumber=1&pageSize=3`
        ).then(response => {
            setProfiles(response.data)
        })
        .catch(err => {
            if (err.response.status === 404) setMessage(err.response.data.message)
            else setMessage("Server error!")
        })
    }

    async function getProfilesByPage(i) {
        setLoading(true)
        await axios.get(`http://localhost:8080/profile/pagination?username=${input}&pageNumber=${i}&pageSize=3`
        )
        .then(response => {
            setProfiles(response.data)
            setLoading(false)
        })
        .catch(err => {
            if (err.response.status === 404) setMessage(err.response.data.message)
            else setMessage("Server error!")
        })
    }

    async function getProjectsByPage(i) {
        setLoading(true)
        await axios.get(`http://localhost:8080/project/getProjectByNameWithPagination/${input}?pageNumber=${i}&pageSize=3`
        )
        .then(response => {
            setProjects(response.data)
            setLoading(false)
        })
        .catch(err => {
            if (err.response.status === 404) setMessage(err.response.data.message)
            else setMessage("Server error!")
        })
    }

    function renderPagination(pageSize) {
        let x = 0;
        if (inputType === "profile") x = profilesNumber / pageSize
        else if (inputType === "project") x = projectsNumber / pageSize
        return(
            <Pagination className={"mt-4 d-flex justify-content-center"}>
                {[...Array(Math.ceil(x))].map((x, i) =>
                    <Pagination.Item key={i} active={i+1 === pageNumber} onClick={(e) => {
                        setPageNumber(i+1);
                        if (inputType === "profile") {
                            setProjects([])
                            getProfilesByPage(i+1)
                        }
                        else if (inputType === "project") {
                            setProfiles([])
                            getProjectsByPage(i+1);
                        }
                    }}>{i+1}</Pagination.Item>
                )}
            </Pagination>
        );
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
                                                 onChange={(e) => {
                                                     clearData()
                                                     setPageNumber(1)
                                                     setInputType(e.target.value)
                                                 }}>
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
                                                <Card.Img style={{width:"160px", height:"160px", margin:"auto", marginTop:"15px"}} variant="top" src={`http://localhost:8080/photo?filename=${project.projectPhoto}`} />
                                                :
                                                <Card.Img style={{width:"160px", height:"160px", margin:"auto", marginTop:"15px"}} variant="top" src={default_project_picture} />
                                            }
                                            <Card.Body>
                                                <Card.Title>
                                                    <div className={"SEARCH-title-height"}>
                                                        {project.title}
                                                    </div>
                                                </Card.Title>
                                                <Card.Text>
                                                    <div className={"SEARCH-intro-height"}>
                                                        {project.introduction}
                                                    </div>
                                                </Card.Text>
                                                <Row className={"mr-2 ml-2"}>
                                                    <Button style={{marginTop:"20px"}} href={`/project/${project.projectId}`} className={"align-self: flex-end;"} variant="primary">View</Button>
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
                    { projects.length || profiles.length ?
                        renderPagination(3)
                        :
                        <></>
                    }
                </Col>
                <Col className={"col-1"}></Col>
            </Row>
        </Container>
    );
}
export default Search