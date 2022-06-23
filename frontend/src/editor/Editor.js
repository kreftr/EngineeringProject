import React from 'react'
import "quill/dist/quill.snow.css"
import { useQuill } from 'react-quilljs';
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {Button, Col, Modal, Row} from "react-bootstrap";
import "./Editor.css"
import axios from "axios";
import Cookies from "js-cookie";

function Editor() {

    const {id} = useParams();
    const { quill, quillRef } = useQuill({theme: "snow", modules:{toolbar: false}});

    const [message, setMessage] = useState();
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);

    React.useEffect(() => {
        var reader = new FileReader();
        if (!Cookies.get("authorization")){
            window.location.replace("/");
        } else {

            axios.get(`http://localhost:8080/file/isLocked?fileId=${id}`,
                {headers: {'Authorization': Cookies.get("authorization")}
            }).then(response => {
                if(response.data) {
                    window.history.back();
                }
                else {
                    axios.post(`http://localhost:8080/file/toggleLock/${id}/${true}`,{},
                        {headers:{'Authorization': Cookies.get("authorization")}
                        }).then(() => {
                    }).catch(err => {
                        console.log(err.response)
                    })
                }
            }).catch(err => {
                window.history.back();
            })

            axios.get(`http://localhost:8080/file/download?fileId=${id}`,
                {headers: {'Authorization': Cookies.get("authorization")}, responseType: 'blob'
            }).then(response => {
                reader.readAsText(response.data);
            }).catch(err => {
                window.history.back();
            })
        }

        if (quill) {
            reader.onload = () => {
                quill.disable();
                quill.setText(reader.result)
                quill.enable();
            };
        }
    }, [quill]);

    function unlock() {
        axios.post(`http://localhost:8080/file/toggleLock/${id}/${false}`, {},
            {headers:{'Authorization': Cookies.get("authorization")}
        }).then((response) => {
            window.history.back();
        }).catch(err => {
            console.log(err.response)
        })
    }

    function saveTextFile(content) {
        axios.post(`http://localhost:8080/file/edit?fileId=${id}`, {"content":content},
            {headers: {'Authorization': Cookies.get("authorization")}
        }).then(response => {
            unlock();
        }).catch(err => {
            setMessage("Error, file couldn't be saved");
        })
    }

    return(
        <Row className={"mt-3"}>
            <div className={"container"}>
                <div ref={quillRef}/>
            </div>
            <Col/>
            <Col className={"mt-3 mb-5"}>
                <Row>
                    <Col>
                        <center>
                            <Button className={"buttons"} variant={"danger"} onClick={()=>{unlock();}}>Discard</Button>
                        </center>
                    </Col>
                    <Col>
                        <center>
                            <Button className={"buttons"} onClick={()=>{saveTextFile(quill.getText());}}>Save</Button>
                        </center>
                    </Col>
                </Row>
            </Col>
            <Col/>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>File status</Modal.Title>
                </Modal.Header>
                <Modal.Body>{message}</Modal.Body>
            </Modal>
        </Row>
    );
}
export default Editor;