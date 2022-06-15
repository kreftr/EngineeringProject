import React, {useEffect, useState} from "react";
import Cookies from "js-cookie";
import axios from "axios";
import {Button, Col, FloatingLabel, Form, FormControl, Image, InputGroup, Row} from "react-bootstrap";
import default_project_picture from "../assets/images/default_project_picture.jpg";
import {FaFacebookSquare, FaGithubSquare, FaKickstarter, FaYoutube} from "react-icons/all";
import {useParams} from "react-router-dom";


function EditProjectSettings(){

    const {id} = useParams();

    const [img, setImg] = useState(null);
    const [title, setTitle] = useState();
    const [introduction, setIntroduction] = useState();
    const [access, setAccess] = useState();
    const [description, setDescription] = useState();
    const [allCategories, setAllCategories] = useState([]);
    const [projectCategories, setProjectCategories] = useState([]);
    const [youtubeLink, setYTLink] = useState();
    const [facebookLink, setFBLink] = useState();
    const [githubLink, setGitLink] = useState();
    const [kickstarterLink, setKickLink] = useState();

    useEffect(() => {
        axios.get(`http://localhost:8080/project/getProjectById/${id}`)
            .then(response => {
                setImg(response.data.projectPhoto);
                setTitle(response.data.title);
                setIntroduction(response.data.introduction);
                setAccess(response.data.access);
                setDescription(response.data.description);
                setProjectCategories(response.data.categories);
                setYTLink(response.data.youtubeLink);
                setFBLink(response.data.facebookLink);
                setGitLink(response.data.githubLink);
                setKickLink(response.data.kickstarterLink);
            })
            .catch(err => {
                console.log(err.response)
            })

        axios.get("http://localhost:8080/categories/getAll", {headers:{
                'Authorization': Cookies.get("authorization")
            }}).then(response => {
            setAllCategories(response.data)
        }).catch(err => {
            console.log(err.response)
        })

    },[]);

    function handleImageChange(e){
        setImg(URL.createObjectURL(e.target.files[0]));
    }

    async function handleProjectEdit(e){

        e.preventDefault();

        let bodyFormData = new FormData();
        bodyFormData.append("projectRequest", new Blob(
            [JSON.stringify({
                "title":title,
                "introduction":introduction,
                "description":description,
                "category": projectCategories,
                "access":access,
                "youtubeLink":youtubeLink,
                "facebookLink":facebookLink,
                "githubLink":githubLink,
                "kickstarterLink":kickstarterLink
            })],
            { type: "application/json"}))

        let fileChooserPhoto = document.getElementById("fileChooser").files[0]
        bodyFormData.append("projectPhoto", fileChooserPhoto);

        await axios.post(`http://localhost:8080/project/editProject/${id}`, bodyFormData, { headers:{
                'Authorization': Cookies.get("authorization")
            }})
        .catch(err => {
            console.log(err.response)
            console.log(err)
        }).finally(() => {
                window.location.replace("/projects")
            })
    }

    function handleCategories(e){
        if (projectCategories.find(x => x === e.target.value)){
            setProjectCategories(projectCategories.filter(x => x !== e.target.value))
        }
        else {
            setProjectCategories([...projectCategories, e.target.value])
        }
    }

    return(
            <Form onSubmit={handleProjectEdit}>
                <Row>
                    <Col>
                        <Row className={"PROJECT_LIST-create-project-pic"}>
                            { img === null ?
                                <Image className={"PROJECT_LIST-create-project-pic"}
                                       src={default_project_picture} width={325} height={325}/>
                                :
                                <>
                                    { !img.startsWith("blob") ?
                                        <Image className={"PROJECT_LIST-create-project-pic"}
                                               src={`http://localhost:8080/photo?filename=${img}`} width={325} height={325}
                                        />
                                        :
                                        <Image className={"PROJECT_LIST-create-project-pic"}
                                               src={`${img}`} width={325} height={325}
                                        />
                                    }
                                </>
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
                                          value={title}
                                          onChange={(e)=>{setTitle(e.target.value)}} required/>
                        </FloatingLabel>
                        <FloatingLabel label="Introduction">
                            <Form.Control
                                value={introduction}
                                as="textarea"
                                placeholder="Short project description"
                                style={{ resize: 'none', height: '243px' }}
                                onChange={(e)=>{setIntroduction(e.target.value)}}
                                required
                            />
                        </FloatingLabel>
                        <Form.Select className={"mt-3"} value={access}>
                            <option value="PUBLIC" onClick={(e)=>{setAccess(e.target.value)}}>Public</option>
                            <option value="PRIVATE" onClick={(e)=>{setAccess(e.target.value)}}>Private</option>
                            <option value="PROTECTED" onClick={(e)=>{setAccess(e.target.value)}}>Protected</option>
                        </Form.Select>
                    </Col>
                </Row>
                <Row>
                    <FloatingLabel label="Description">
                        <Form.Control
                            value={description}
                            as="textarea"
                            placeholder="Description"
                            style={{ resize: 'none', height: '300px' }}
                            onChange={(e)=>{setDescription(e.target.value)}}
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
                                value={youtubeLink}
                                placeholder="Youtube Link"
                                type={"text"}
                                onChange={(e)=>{setYTLink(e.target.value)}}
                            />
                        </InputGroup>
                        <InputGroup className="mb-3">
                            <InputGroup.Text><FaFacebookSquare size={15}/></InputGroup.Text>
                            <FormControl
                                value={facebookLink}
                                placeholder="Facebook Link"
                                type={"text"}
                                onChange={(e)=>{setFBLink(e.target.value)}}
                            />
                        </InputGroup>
                        <InputGroup className="mb-3">
                            <InputGroup.Text><FaGithubSquare size={15}/></InputGroup.Text>
                            <FormControl
                                value={githubLink}
                                placeholder="Github Link"
                                type={"text"}
                                onChange={(e)=>{setGitLink(e.target.value)}}
                            />
                        </InputGroup>
                        <InputGroup className="mb-3">
                            <InputGroup.Text><FaKickstarter size={15}/></InputGroup.Text>
                            <FormControl
                                value={kickstarterLink}
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
                                        allCategories.map((category, key) =>
                                            <Form.Check className={"mt-3"}
                                                        key={key}
                                                        type={"checkbox"}
                                                        label={category}
                                                        value={category}
                                                        name="group1"
                                                        defaultChecked={projectCategories.includes(category)}
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