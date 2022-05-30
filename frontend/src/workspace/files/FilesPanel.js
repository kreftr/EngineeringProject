import {Button, Col, Form, Row} from "react-bootstrap";
import React, {useCallback, useEffect, useState} from "react";
import axios from "axios";
import Cookies from "js-cookie";
import File from "./File";
import {useDropzone} from 'react-dropzone'
import {FaFolderOpen} from "react-icons/fa";
import './File.css'


function FilesPanel(props){

    const id = props.projectId
    const memberRole = props.memberRole

    const [fileTermSearch, setFileTermSearch] = useState("");
    const [files, setFiles] = useState([]);

    const [renderedObjects, setRenderedObjects] = useState([])
    const [renderPath, setRenderPath] = useState([]);  // where we are actually

    const onDrop = useCallback(acceptedFiles => {


        acceptedFiles.map((file) =>
            {
                // append to path if we are further in directory
                let folderPath = ""
                renderPath.map((el) => {
                    folderPath += el + "/"
                })

                // delete first slash if it's folder
                let fullPath = folderPath;
                if (file.path.startsWith("/")) {
                    fullPath += file.path.substring(1);
                } else {
                    fullPath += file.path
                }

                let bodyFormData = new FormData();
                bodyFormData.append("file", file);
                bodyFormData.append("fullPath", fullPath)

                axios.post(`http://localhost:8080/file/upload?projectId=${id}`, bodyFormData,
                    {
                        headers:{'Authorization': Cookies.get("authorization")}
                    }).then(() => {

                    axios.get(`http://localhost:8080/project/getProjectFiles/${id}`,
                        {
                            headers: {'Authorization': Cookies.get("authorization")}
                        }).then(response => {
                        setFiles(response.data)
                    }).catch(err => {
                        console.log(err.response)
                    })

                }).catch(err => {
                    console.log(err.response)
                })
            }
        )

    }, [renderPath])
    const {getRootProps, getInputProps} = useDropzone({onDrop, noClick:true})

    useEffect(() => {

        axios.get(`http://localhost:8080/project/getProjectFiles/${id}`,
            {
                headers: {'Authorization': Cookies.get("authorization")}
            }).then(response => {
            setFiles(response.data)
        }).catch(err => {
                console.log(err.response)
            })

    },[id])

    useEffect(() => {

        let allFiles = []
        let allFolders = []

        files.map((file) => {
            const url = file.fileName
            let urlArray = url.split("/")

            // filter by opened folder
            let areArraysEqual = true
            for (let i=0; i<renderPath.length; i++) {
                if (urlArray[i] !== renderPath[i]) {
                    areArraysEqual = false
                    break
                }
            }

            if (areArraysEqual) {

                // delete renderPath from urlArray because we are further in directory
                urlArray.splice(0, renderPath.length)

                // array of mixed type objects: objects -> files, strings -> folders
                if (urlArray[0].split('.').length > 1) {
                    allFiles.push(file)
                } else {
                    allFolders.push(urlArray[0])
                }

            }

        })

        // delete all the same folders in memory
        allFolders = allFolders.filter((e, i, a) => a.indexOf(e) === i)

        setRenderedObjects(allFiles.concat(allFolders))

    },[files, renderPath])


    return (
        <>
            <Row>
                <Col>
                    <Button onClick={() => { setRenderPath(renderPath.slice(0, renderPath.length - 1))} }>
                        Back
                    </Button>
                </Col>
                <Col className={"WORKSPACE-center-searchbar"}>
                    <center>
                        <Form>
                            <Form.Control type="text" placeholder="Search file"
                                          onChange={(e) => setFileTermSearch(e.target.value)}/>
                        </Form>
                    </center>
                </Col>
            </Row>
            <hr/>
            <Row>
                <div {...getRootProps()}>
                    <input {...getInputProps()} />
                    { renderedObjects.length > 0 ?
                        <div className={"ml-5 WORKSPACE-file-section"}>
                            { renderedObjects.map((file, key) =>
                                typeof file === 'string' ?
                                    <div>
                                        <button className={"FILE-folder"}>
                                            <FaFolderOpen color={"gray"} size={150} onClick={() => {
                                                setRenderPath(renderPath => [...renderPath, file]);
                                            }}/>
                                            <h4>{file}</h4>
                                        </button>
                                    </div>
                                    :
                                    <div>
                                        <File file={file} role={memberRole}/>
                                    </div>
                            )}
                        </div>
                        :
                        <center>
                            <h1>Empty directory, drag and drop files here</h1>
                        </center>
                    }
                </div>
            </Row>
        </>
    );

}
export default FilesPanel;