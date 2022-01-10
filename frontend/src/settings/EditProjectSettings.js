import React, {useEffect, useState} from "react";
import Cookies from "js-cookie";
import axios from "axios";
import {Alert, Button, Col, FloatingLabel, Form, FormControl, Image, InputGroup, Modal, Row} from "react-bootstrap";
import default_project_picture from "../assets/images/default-project-picture.jpg";
import {FaFacebookSquare, FaGithubSquare, FaKickstarter, FaYoutube} from "react-icons/all";
import {useParams} from "react-router-dom";

function EditProjectSettings(){

    const {id} = useParams();

    const [img, setImg] = useState();
    const [title, setTitle] = useState();
    const [introduction, setIntroduction] = useState();
    const [access, setAccess] = useState();
    const [description, setDescription] = useState();
    const [category, setCategory] = useState();
    const [ytLink, setYTLink] = useState();
    const [fbLink, setFBLink] = useState();
    const [gitLink, setGitLink] = useState();
    const [kickLink, setKickLink] = useState();

    //Created projects
    const [projects, setProjects] = useState([]);
    //Projects categories
    const [categories, setCategories] = useState([]);

    useEffect(() => {

        axios.get("http://localhost:8080/categories/getAll", {headers:{
                'Authorization': Cookies.get("authorization")
            }}).then(response => {
            setCategories(response.data)
        }).catch(err => {
            console.log(err.response)
        })

        axios.get(`http://localhost:8080/project/getProjectById/${id}`, {headers:{
                'Authorization': Cookies.get("authorization")
            }}).then(response => {
            setProjects(response.data)
        }).catch(err => {
            console.log(err.response)
        })

        axios.get(`http://localhost:8080/project/getProjectById/${id}`)
            .then(response => {
                setImg(response.data.profile_photo);
                setTitle(response.data);
                setIntroduction(response.data);
                setAccess(response.data);
                setDescription(response.data);
                setCategory(response.data);
                setYTLink(response.data);
                setFBLink(response.data);
                setGitLink(response.data);
                setKickLink(response.data);
            })
            .catch(err => {
                console.log(err.response)
            })
    },[]);

    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    function handleImageChange(e){
        setImg(URL.createObjectURL(e.target.files[0]));
    }

    function handleCategories(e){
        if (category.find(x => x === e.target.value)){
            setCategory(category.filter(x => x !== e.target.value))
        }
        else {
            setCategory([...category, e.target.value])
        }
    }

    return(
            <Form onSubmit={handleClose}>
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
                <Row>
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
                <Row className={"mt-3"}>
                    <hr/>
                    {/*{ projectCreationMessage && responseCode === 400 ?*/}
                    {/*    <Alert variant={"danger"}>*/}
                    {/*        <center>*/}
                    {/*            {projectCreationMessage}*/}
                    {/*        </center>*/}
                    {/*    </Alert>*/}
                    {/*    : projectCreationMessage ?*/}
                    {/*        <Alert variant={"danger"}>*/}
                    {/*            <center>*/}
                    {/*                {projectCreationMessage}*/}
                    {/*            </center>*/}
                    {/*        </Alert>*/}
                    {/*        :*/}
                    {/*        <></>*/}
                    {/*}*/}
                    <Col>
                        <Button type="submit" variant="primary" className={"q"}>
                            Save
                        </Button>
                    </Col>
                </Row>
                <Row className={"mt-3"}></Row>
            </Form>
    );
}
export default EditProjectSettings