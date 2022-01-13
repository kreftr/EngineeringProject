import React, {useEffect, useState} from "react";
import Cookies from "js-cookie";
import axios from "axios";
import {Alert, Button, Col, Form, Image, Row} from "react-bootstrap";
import default_profile_picture from "../../assets/images/default_profile_picture.jpg";


function ProfileSettings(){

    const [categories, setCategories] = useState([]);

    const [profilePhoto, setProfilePhoto] = useState();
    const [name, setName] = useState();
    const [surname, setSurname] = useState();
    const [bio, setBio] = useState();
    const [profileCategories, setProfileCategories] = useState([]);

    const [responseCode,setCode] = useState();
    const [loadingContent, setLoading] = useState(true);

    const [editCode, setEditCode] = useState(200)
    const [editMessage, setEditMessage] = useState()


    useEffect(() => {

        if (!Cookies.get("userId")) window.location.replace("/");

        axios.get("http://localhost:8080/categories/getAll", {headers:{
                'Authorization': Cookies.get("authorization")
        }}).then(response => {
            setCategories(response.data)
        }).catch(err => {
            console.log(err.response)
        })

        axios.get(`http://localhost:8080/profile?id=${Cookies.get("userId")}`)
            .then(response => {
                setCode(response.status);
                setProfilePhoto(response.data.profile_photo);
                setName(response.data.name);
                setSurname(response.data.surname);
                setBio(response.data.bio);
                setProfileCategories(response.data.categories);
                setLoading(false);
            })
            .catch(err => {
                setCode(err.response.status)
                setLoading(false);
            })
    },[]);


    async function handleProfileEdit(e){
        e.preventDefault();

        let photo = document.getElementById("fileChooser").files[0]
        let bodyFormData = new FormData();
        bodyFormData.append("editRequest", new Blob(
            [JSON.stringify({"name":name, "surname":surname, "bio":bio, "categories": profileCategories})],
            { type: "application/json"}))
        bodyFormData.append("profilePhoto", photo);

        await axios.post("http://localhost:8080/profile", bodyFormData, {headers:{
                'Authorization': Cookies.get("authorization")
            }}).then(res => {
            setEditCode(res.status)
            window.location.reload();
        }).catch(err => {
            setEditCode(err.response.status)
            if (err.response.status === 400) setEditMessage("*"+err.response.data.error)
            else setEditMessage("SERVER ERROR!")
        })
    }

    function handleCategoryChange(e){
        if (profileCategories.includes(e.target.value)){
            setProfileCategories(profileCategories.filter(x => x !== e.target.value))
        }
        else {
            setProfileCategories([...profileCategories, e.target.value])
        }
    }


    return(
        <div>
            {responseCode === 200 && !loadingContent ?
                <Form onSubmit={handleProfileEdit}>
                    <Row className={"mt-3 mb-3"}>
                        <Col className={"SETTINGS-info-col"}>
                            {!profilePhoto ?
                                <Image className={"SETTINGS-profile-pic"}
                                       src={default_profile_picture} roundedCircle={true}/>
                                :
                                <Image className={"SETTINGS-profile-pic"}
                                       src={`http://localhost:8080/photo?filename=${profilePhoto}`}
                                       roundedCircle={true}/>
                            }
                            <Form.Group className="mb-3 mt-3">
                                <Form.Control type="file" size="sm" accept="image/*"
                                              id="fileChooser"
                                              onChange={(e) => console.log(e.target.value)}/>
                            </Form.Group>
                        </Col>
                        <Col>
                            <Row className={"mb-3"}>
                                <Col>
                                    <Form.Label>Name</Form.Label>
                                    <Form.Control value={name} maxLength={20}
                                                  onChange={(e) => setName(e.target.value)}/>
                                </Col>
                                <Col>
                                    <Form.Label>Surname</Form.Label>
                                    <Form.Control value={surname} maxLength={20}
                                                  onChange={(e) => setSurname(e.target.value)}/>
                                </Col>
                            </Row>
                            <Form.Group className="mb-3">
                                <Form.Label>Description</Form.Label>
                                <Form.Control className={"SETTINGS-textarea"} as="textarea"
                                              maxLength={250} rows={6} value={bio}
                                              onChange={(e) => setBio(e.target.value)}/>
                            </Form.Group>
                        </Col>
                    </Row>
                    {editCode !== 200 ?
                        <Row className={"mt-4"}>
                            <Col/>
                            <Col>
                                <Alert variant={"danger"}>
                                    <center>
                                        {editMessage}
                                    </center>
                                </Alert>
                            </Col>
                            <Col/>
                        </Row>
                        :
                        <></>
                    }
                    <Row>
                        <Col>
                            <h5>Field of interest</h5>
                            <hr/>
                            <div className={"SETTINGS-grid-container mb-5"}>
                                {
                                    categories.map((category, key) =>
                                        <Form.Check className={"mt-3"}
                                                    key={key}
                                                    type={"checkbox"}
                                                    label={category}
                                                    value={category}
                                                    name="group1"
                                                    defaultChecked={profileCategories.includes(category)}
                                                    onClick={(e) => {handleCategoryChange(e)}}
                                        />
                                    )
                                }
                            </div>
                        </Col>
                    </Row>
                    <Row>
                        <Col/>
                        <Col className={"SETTINGS-save-button-col"}>
                            <Button className={"SETTINGS-save-button mb-3"}
                                    variant="primary" type="submit">
                                Save
                            </Button>
                        </Col>
                        <Col/>
                    </Row>
                </Form>
                :
                <></>
            }
        </div>
    );

}
export default ProfileSettings;