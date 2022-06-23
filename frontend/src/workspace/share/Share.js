import React, {useState} from "react";
import {Button} from "react-bootstrap";
import './Share.css'

function Share(props){

    const [projectId] = useState(props.projectId)

    function prepareHrefUrl() {
        return `https://www.facebook.com/sharer/sharer.php?u=https://EngineeringProject/project/${projectId}`
    }

    return (
        <Button className={'SHARE-button'}>
            <a href={prepareHrefUrl()} target="_blank">
                Share on facebook
            </a>
        </Button>
    )

}
export default Share;