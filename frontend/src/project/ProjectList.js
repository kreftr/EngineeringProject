import {
    Alert,
    Badge,
    Button,
    Col,
    Container,
    FloatingLabel,
    Form,
    FormControl,
    Image,
    InputGroup,
    ListGroup,
    ListGroupItem,
    Modal,
    Row
} from "react-bootstrap";
import {FaFacebookSquare, FaGithubSquare, FaKickstarter, FaRegPlusSquare, FaSistrix, FaYoutube} from "react-icons/all";
import React, {useEffect, useState} from "react";
import default_project_picture from "../assets/images/default_project_picture.jpg"
import "./ProjectList.css"
import axios from "axios";
import Cookies from "js-cookie";
import Project from "./Project";
import {FaCogs, FaEye, FaFileAlt} from "react-icons/fa";


function ProjectList(){

    const [projectCreationMessage, setProjectCreationMessage] = useState()
    const [responseCode, setResponseCode] = useState()

    //Created projects
    const [projects, setProjects] = useState([]);
    //Joined projects
    const [joinedProjects, setJoinedProjects] = useState([]);
    //Projects categories
    const [categories, setCategories] = useState([]);
    //Proposed projects
    const [proposed, setProposed] = useState([]);

    //Proposed projects window
    const [showProposedProjects, setShowProposedProjects] = useState(false);
    const handleProposedProjectsClose = () => setShowProposedProjects(false);
    const handleProposedProjectsShow = () => setShowProposedProjects(true);

    //Project creation window
    const [showProjectCreation, setShowProjectCreation] = useState(false);
    const handleProjectCreationClose = () => setShowProjectCreation(false);
    const handleProjectCreationShow = () => setShowProjectCreation(true);
    function handleImageChange(e){
        setImg(URL.createObjectURL(e.target.files[0]));
    }

    //Project form inputs
    const [img, setImg] = useState(null);
    const [title, setTitle] = useState(null);
    const [introduction, setIntroduction] = useState(null);
    const [access, setAccess] = useState("PUBLIC");
    const [description, setDescription] = useState(null);
    const [category, setCategory] = useState([]);
    const [ytLink, setYTLink] = useState(null);
    const [fbLink, setFBLink] = useState(null);
    const [gitLink, setGitLink] = useState(null);
    const [kickLink, setKickLink] = useState(null);


    useEffect(() => {

        axios.get("http://localhost:8080/categories/getAll", {headers:{
                'Authorization': Cookies.get("authorization")
        }}).then(response => {
            setCategories(response.data)
        }).catch(err => {
            console.log(err.response)
        })

        axios.get(`http://localhost:8080/project/getAllProjects/${Cookies.get("userId")}`, {headers:{
                'Authorization': Cookies.get("authorization")
        }}).then(response => {
            setProjects(response.data)
        }).catch(err => {
            console.log(err.response)
        })

        axios.get("http://localhost:8080/project/getAllProjectsWhereIsMember", {headers:{
                'Authorization': Cookies.get("authorization")
        }}).then(response => {
            setJoinedProjects(response.data)
        }).catch(err => {
            console.log(err.response)
        })

        axios.get(`http://localhost:8080/project/getProposed`, {headers:{
                'Authorization': Cookies.get("authorization")
        }}).then(response => {
            setProposed(response.data)
        }).catch(err => {
            console.log(err.response)
        })

    }, [])

    function handleCategories(e){
        if (category.find(x => x === e.target.value)){
            setCategory(category.filter(x => x !== e.target.value))
        }
        else {
            setCategory([...category, e.target.value])
        }
    }

    function projectSubmit(e){
        e.preventDefault()

        let photo = document.getElementById("fileChooser").files[0]
        let bodyFormData = new FormData();
        bodyFormData.append("projectRequest", new Blob(
            [JSON.stringify({
                "title": title,
                "introduction": introduction,
                "description": description,
                "category": category,
                "access": access,
                "youtubeLink": ytLink,
                "facebookLink": fbLink,
                "githubLink": gitLink,
                "kickstarterLink": kickLink
            })],
            { type: "application/json"}))
        bodyFormData.append("projectPhoto", photo);

        axios.post("http://localhost:8080/project/createProject", bodyFormData, {headers:{
                'Authorization': Cookies.get("authorization")
            }})
            .then(response => {
                window.location.reload();
            })
            .catch(err => {
                setResponseCode(err.response.status)
                if (err.response.status === 400) setProjectCreationMessage("*"+err.response.data.error)
                else setProjectCreationMessage("SERVER ERROR!")
                console.log(err.response)
            })
    }


    return(
        <Container>
            <Row className={"mt-5"}>
                <Col className={"col-2"}/>
                <Col className={"col-8"}>
                    <Row>
                        <Col className={"PROJECT_LIST-buttons-col"}>
                            <Button variant={"primary"} onClick={handleProjectCreationShow}>
                                <FaRegPlusSquare size={50} className={"mr-2"}/>
                                Create project
                            </Button>
                            <Modal show={showProjectCreation} onHide={handleProjectCreationClose} size={"lg"}>
                                <Modal.Header closeButton>
                                    <Modal.Title>New Project</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>
                                    <Form onSubmit={projectSubmit}>
                                        <Row>
                                            <Col>
                                                <Row className={"PROJECT_LIST-create-project-pic"}>
                                                    { img === null ?
                                                        <Image className={"PROJECT_LIST-create-project-pic"}
                                                               src={default_project_picture} width={325} height={325}/>
                                                        :
                                                        <Image className={"PROJECT_LIST-create-project-pic"}
                                                               src={img} width={325} height={325}/>
                                                    }
                                                </Row>
                                                <Row>
                                                    <Form.Group className="mb-3 mt-3">
                                                        <Form.Control id={"fileChooser"} accept="image/*" type="file" onChange={handleImageChange}/>
                                                    </Form.Group>
                                                </Row>
                                            </Col>
                                            <Col>
                                                <FloatingLabel label="Title">
                                                    <Form.Control className={"mb-4"} type="text" placeholder="Project title"
                                                    onChange={(e)=>{setTitle(e.target.value)}} required/>
                                                </FloatingLabel>
                                                <FloatingLabel label="Introduction">
                                                    <Form.Control
                                                        as="textarea"
                                                        placeholder="Short project description"
                                                        style={{ resize: 'none', height: '243px' }}
                                                        onChange={(e)=>{setIntroduction(e.target.value)}}
                                                        required
                                                    />
                                                </FloatingLabel>
                                                <Form.Select className={"mt-3"}>
                                                    <option value="PUBLIC" onClick={(e)=>{setAccess(e.target.value)}}>Public</option>
                                                    <option value="PRIVATE" onClick={(e)=>{setAccess(e.target.value)}}>Private</option>
                                                    <option value="PROTECTED" onClick={(e)=>{setAccess(e.target.value)}}>Protected</option>
                                                </Form.Select>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <FloatingLabel label="Description">
                                                <Form.Control
                                                    as="textarea"
                                                    placeholder="Description"
                                                    style={{ resize: 'none', height: '300px' }}
                                                    onChange={(e)=>{setDescription(e.target.value)}}
                                                    required
                                                />
                                            </FloatingLabel>
                                        </Row>
                                        <Row className={"mt-3"}>
                                            <Col>
                                                <h3>Media</h3>
                                                <hr/>
                                                <InputGroup className="mb-3">
                                                    <InputGroup.Text><FaYoutube size={15}/></InputGroup.Text>
                                                    <FormControl
                                                        placeholder="Youtube Link"
                                                        type={"text"}
                                                        onChange={(e)=>{setYTLink(e.target.value)}}
                                                    />
                                                </InputGroup>
                                                <InputGroup className="mb-3">
                                                    <InputGroup.Text><FaFacebookSquare size={15}/></InputGroup.Text>
                                                    <FormControl
                                                        placeholder="Facebook Link"
                                                        type={"text"}
                                                        onChange={(e)=>{setFBLink(e.target.value)}}
                                                    />
                                                </InputGroup>
                                                <InputGroup className="mb-3">
                                                    <InputGroup.Text><FaGithubSquare size={15}/></InputGroup.Text>
                                                    <FormControl
                                                        placeholder="Github Link"
                                                        type={"text"}
                                                        onChange={(e)=>{setGitLink(e.target.value)}}
                                                    />
                                                </InputGroup>
                                                <InputGroup className="mb-3">
                                                    <InputGroup.Text><FaKickstarter size={15}/></InputGroup.Text>
                                                    <FormControl
                                                        placeholder="Kickstarter Link"
                                                        type={"text"}
                                                        onChange={(e)=>{setKickLink(e.target.value)}}
                                                    />
                                                </InputGroup>
                                            </Col>
                                            <Col>
                                                <h3>Categories</h3>
                                                <hr/>
                                                <Row>
                                                    <Col className={"PROJECT-categories"}>
                                                        <div key={`default-checkbox`} className="mb-3">
                                                            {
                                                                categories.map((category, key) =>
                                                                    <Form.Check className={"mt-3"}
                                                                                key={key}
                                                                                type={"checkbox"}
                                                                                label={category}
                                                                                value={category}
                                                                                name="group1"
                                                                                onClick={(e) => {handleCategories(e)}}
                                                                    />
                                                                )
                                                            }
                                                        </div>
                                                    </Col>
                                                </Row>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <hr/>
                                            { projectCreationMessage && responseCode === 400 ?
                                                <Alert variant={"danger"}>
                                                    <center>
                                                        {projectCreationMessage}
                                                    </center>
                                                </Alert>
                                                : projectCreationMessage ?
                                                <Alert variant={"danger"}>
                                                    <center>
                                                        {projectCreationMessage}
                                                    </center>
                                                </Alert>
                                                :
                                                <></>
                                            }
                                            <Col>
                                                <Button className={"q"} variant="secondary" onClick={handleProjectCreationClose}>
                                                    Close
                                                </Button>
                                            </Col>
                                            <Col>
                                                <Button type="submit" variant="primary" className={"q"}>
                                                    Create
                                                </Button>
                                            </Col>
                                        </Row>
                                    </Form>
                                </Modal.Body>
                            </Modal>
                            <Modal show={showProposedProjects} onHide={handleProposedProjectsClose} size={"lg"}>
                                <Modal.Header closeButton>
                                    <Modal.Title>Projects you may like</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>
                                    <ListGroup className={"PROJECT-proposed mt-3"}>
                                        { proposed.length === 0 ?
                                            <center>
                                                <h2>We couldn't find proper project for you</h2>
                                                <p>Select in the profile settings, categories of projects that interest you or use the search engine.</p>
                                            </center>
                                            :
                                            <></>
                                        }
                                        {
                                            proposed.map((project, key) =>
                                                <ListGroupItem key={key}>
                                                    <a className={"PROFILE-href-styling"} href={`/project/${project.projectId}`}>
                                                        <Row>
                                                            <Col className={"col-3 PROJECT-thumbnail-section"}>
                                                                {project.projectPhoto ?
                                                                    <Image
                                                                        src={`http://localhost:8080/photo?filename=${project.projectPhoto}`}
                                                                        width="180px" height="180px"/>
                                                                    :
                                                                    <Image src={default_project_picture}
                                                                           width="180px" height="180px"/>
                                                                }
                                                            </Col>
                                                            <Col className={"col-9 PROJECT-content-section"}>
                                                                <h2>{project.title}</h2>
                                                                <div className={"PROJECT-introduction"}>{project.introduction}</div>
                                                            </Col>
                                                        </Row>
                                                        <div className={"mt-1"}>
                                                            { project.categories.map((category, key) =>
                                                                <Badge pill key={key} className={"ml-1"} bg="primary">{category}</Badge>
                                                            )
                                                            }
                                                        </div>
                                                    </a>
                                                    <hr/>
                                                </ListGroupItem>
                                            )
                                        }
                                    </ListGroup>
                                    <Row>
                                        <Col>
                                            <Button className={"q mt-2"} variant="secondary" onClick={handleProposedProjectsClose}>
                                                Close
                                            </Button>
                                        </Col>
                                        <Col>
                                            <Button className={"q mt-2"} variant="primary" href={"/search"}>
                                                <FaSistrix size={20} className={"mr-2"}/>
                                                Go to search bar
                                            </Button>
                                        </Col>
                                    </Row>
                                </Modal.Body>
                            </Modal>
                        </Col>
                        <Col className={"PROJECT_LIST-buttons-col"}>
                            <Button variant={"primary"} onClick={handleProposedProjectsShow}>
                                <FaSistrix size={50} className={"mr-2"}/>
                                Join project
                            </Button>
                        </Col>
                        <hr className={"mt-5 mb-4"}/>
                    </Row>
                    <Row>
                        <h2>Invitations to project</h2>
                        <span>...</span>
                        <hr className={"mt-4 mb-4"}/>
                    </Row>
                    { projects.length > 0 ?
                        <Row>
                            <h2>Created projects</h2>
                                <ListGroup className={"mt-3"}>
                                    {
                                        projects.map((project, key) =>
                                            <ListGroupItem key={key}>
                                                <Project project={project}/>
                                            </ListGroupItem>
                                        )
                                    }
                                </ListGroup>
                            <hr className={"mt-4 mb-4"}/>
                        </Row>
                        :
                        <h3 className={"mt-3"}>No projects found</h3>
                    }
                    { joinedProjects.length > 0 ?
                        <Row>
                            <h2>Joined projects</h2>
                                <ListGroup className={"mt-3"}>
                                    {
                                        joinedProjects.map((project, key) =>
                                            <ListGroupItem key={key}>
                                                <Project project={project}/>
                                            </ListGroupItem>
                                        )
                                    }
                                </ListGroup>
                            <hr className={"mt-4 mb-4"}/>
                        </Row>
                        :
                        <></>
                    }
                </Col>
                <Col className={"col-2"}/>
            </Row>
        </Container>
    );

}
export default ProjectList;