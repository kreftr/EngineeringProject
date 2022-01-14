import {FaFileAlt, FaFileDownload, FaTrashAlt, FaWindowClose} from "react-icons/fa";
import {Button, Col, Image, Modal, Row} from "react-bootstrap";
import "./File.css"
import axios from "axios";
import Cookies from "js-cookie";
import {useState} from "react";
import default_profile_picture from "../assets/images/default_profile_picture.jpg"

function File(props){

    const [show, setShow] = useState(false);
    const [hide, setHide] = useState(false);

    function download(){
        axios.get(`http://localhost:8080/file/download?fileId=${props.file.fileId}`,
            {headers: {'Authorization': Cookies.get("authorization")}, responseType: 'blob'
            }).then(response =>{
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', props.file.fileName);
            document.body.appendChild(link);
            link.click();
        }).catch(err => {
            console.log(err.response)
        })
    }

    function remove(){
        axios.delete(`http://localhost:8080/file/remove?fileId=${props.file.fileId}`,
            {headers: {'Authorization': Cookies.get("authorization")}}
        ).then(response =>{
            window.location.reload()
        }).catch(err => {
            console.log(err.response)
        })
    }


    return(
        <div className={"FILE-center mb-2 mt-4"}>
            { show ?
                <Modal.Dialog>
                    <Modal.Header className={"FILE-hover"} onClick={() => {setShow(false); setHide(false);}}>
                        <Modal.Title>{props.file.fileName}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body className={"FILE-hover"} onClick={() => {setShow(false); setHide(false);}}>
                        <h5>Author: </h5>
                        { props.file.profilePhoto !== null ?
                            <Image className={"mr-1"} src={`http://localhost:8080/photo?filename=${props.file.profilePhoto}`}
                                   height={50} width={50} roundedCircle={true}/>
                            :
                            <Image className={"mr-1"} src={default_profile_picture}
                                   height={50} width={50} roundedCircle={true}></Image>
                        }
                        {props.file.username}
                        <h5>Size: {props.file.size}B</h5>
                        <h5>Upload Date: </h5>
                            {props.file.uploadDate}
                    </Modal.Body>
                    <Modal.Footer>
                        <Row>
                            <Col>
                                { props.role === "OWNER" || props.role === "MODERATOR" || props.file.userId === Cookies.get("userId") ?
                                    <Button variant="danger" onClick={() => {remove();}}>
                                        <FaTrashAlt size={25}/>
                                    </Button>
                                    :
                                    <></>
                                }
                            </Col>
                            <Col>
                                <Button variant="primary" onClick={() => {download();}}>
                                    <FaFileDownload size={25}/>
                                </Button>
                            </Col>
                        </Row>
                    </Modal.Footer>
                </Modal.Dialog>
                :
                <></>
            }
            <FaFileAlt color={"gray"} className={"mb-2 FILE-hover"} size={150} onClick={() => {setShow(true);
                setHide(true);}} hidden={hide}/>
            <h4 hidden={hide}>{props.file.fileName}</h4>
        </div>
    );

}
export default File;