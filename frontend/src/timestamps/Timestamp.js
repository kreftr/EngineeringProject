import React, {useEffect, useState} from "react";
import {Col, ListGroupItem, Row} from "react-bootstrap";
import moment from "moment";


function Timestamp(props) {
    const [description, setDescription] = useState("")
    const [timeStart, setTimeStart] = useState(null)
    const [timeEnd, setTimeEnd] = useState(null)
    const [projectName, setProjectName] = useState("")
    const [participantId, setParticipantId] = useState(null)

    useEffect(() => {
        setDescription(props.timeData.description)
        setTimeStart(props.timeData.timeStart)
        setTimeEnd(props.timeData.timeEnd)
        setProjectName(props.timeData.projectName)
        setParticipantId(props.timeData.participantId)
    }, [props.timeData.description, props.timeData.timeStart, props.timeData.timeEnd,
        props.timeData.projectName, props.timeData.participantId])

    // converts date to format hh:mm:ss
    function prettifyDate(prePrettifiedDate) {
        const date = new Date(prePrettifiedDate)
        return date.toLocaleTimeString([], {
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        })
    }

    // subtracts first date from second date and returns it in HH:mm:ss format
    function diffBetweenDates(first, second) {
        const firstDate = new Date(first)
        const secondDate = new Date(second)
        const ms = Math.abs(secondDate - firstDate)  // to make subtraction work no matter parameter order
        const msRounded = Math.round(ms / 1000) * 1000 + 1000
        const timeDuration = moment.duration(msRounded);
        return timeDuration.hours() + "h " + timeDuration.minutes() + "m " + timeDuration.seconds() + "s"
    }

    return (
        <ListGroupItem>
            { participantId !== null ?
                <Row>
                    <Col className={"col-6"} >
                        <Row className={"row-6"}>
                            <h3 className={"TIMESTAMP-title"}>Project: {projectName}</h3>
                        </Row>
                        <Row className={"row-6"}>
                            <h5 className={"TIMESTAMP-description"}>Description: {description}</h5>
                        </Row>
                    </Col>
                    <Col className={"col-6"}>
                        <Row className={"row-4"}>
                            <p>Task start: {prettifyDate(timeStart)}</p>
                        </Row>
                        <Row className={"row-4"}>
                            <p>Task end: {prettifyDate(timeEnd)}</p>
                        </Row>
                        <Row className={"row-4"}>
                            <p>Total time: {diffBetweenDates(timeEnd, timeStart)}</p>
                        </Row>
                    </Col>
                </Row>
                :
                <></>
            }
        </ListGroupItem>
    )
} export default Timestamp