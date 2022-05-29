import {Button, Col, Form, Row} from "react-bootstrap";
import React, {useCallback, useEffect, useState} from "react";
import axios from "axios";
import Cookies from "js-cookie";
import File from "./File";
import {useDropzone} from 'react-dropzone'
import {FaFileAlt, FaFolderOpen} from "react-icons/fa";
import './File.css'


function FilesPanel(props){

    const id = props.projectId
    const memberRole = props.memberRole

    const [fileTermSearch, setFileTermSearch] = useState("");
    const [files, setFiles] = useState([]);

    const onDrop = useCallback(acceptedFiles => {

        acceptedFiles.map((file) =>
            {
                let bodyFormData = new FormData();
                bodyFormData.append("file", file);

                // TODO przerobic backend zeby potrafil przyjac cala tablice plikow na raz

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

    }, [])
    const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

    const [renderedObjects, setRenderedObjects] = useState([])
    const [renderPath, setRenderPath] = useState([]);  // where we are actually

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


    // useEffect(() => {
    //
    //     files.map((file) => {
    //         const url = file.fileName
    //         const urlArray = url.split("/")
    //         console.log(urlArray)
    //
    //         const fileTitle = urlArray[urlArray.length - 1]  // last element of array is our file
    //         urlArray.pop()
    //
    //         urlArray.map((element) => {  // create folder structure
    //             if (files.some(e => e.fileName !== element)) {  // folder does not exist
    //                 return (
    //                     <>
    //                         <div>
    //                             <Button>
    //                                 <FaFolderOpen color={"gray"} className={"mb-2 FILE-hover"} size={150} onClick={() => {}}/>
    //                                 <h4>Folder</h4>
    //                             </Button>
    //                         </div>
    //                     </>
    //                 )
    //
    //             }
    //
    //         })
    //
    //
    //
    //         }
    //     )
    //
    // },[files])


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
            <div {...getRootProps()}>
                <input {...getInputProps()} />
            </div>  {/* TODO przesun koniec diva nizej zeby odblokowac dzialanie on dropa */}
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
                        <h1>Currently there are no files</h1>
                    </center>
                }

            {/*TODO guziki nie moga odpalac drag and dropa*/}


        </>
    );

}
export default FilesPanel;