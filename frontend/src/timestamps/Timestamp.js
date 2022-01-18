import React, {useEffect, useState} from "react";


function Timestamp(props) {
    const [id, setId] = useState(null)
    const [description, setDescription] = useState("")
    const [timeStart, setTimeStart] = useState(null)
    const [timeEnd, setTimeEnd] = useState(null)
    const [projectName, setProjectName] = useState("")
    const [participantId, setParticipantId] = useState(null)

    useEffect(() => {
        setId(props.timeData.id)
        setDescription(props.timeData.description)
        setTimeStart(props.timeData.timeStart)
        setTimeEnd(props.timeData.timeEnd)
        setProjectName(props.timeData.projectName)
        setParticipantId(props.timeData.participantId)
    }, [])

    // converts date to format hh:mm:ss
    function prettifyDate(prePrettifiedDate) {
        const date = new Date(prePrettifiedDate)
        return date.toLocaleTimeString(navigator.language, {
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        })
    }

    // subtracts first date from second date and returns it in ms
    function diffBetweenDates(first, second) {
        const firstDate = new Date(first)
        const secondDate = new Date(second)
        return Math.abs(secondDate - firstDate)  // to make subtraction work no matter parameter order
    }

    // TODO looks like timeEnd - timeStart is not displayed properly, needs further testing
    return (
        <>
            { participantId !== null ?
                <div>
                    <p>{projectName}</p>
                    <p>{description}</p>
                    <p>{prettifyDate(timeStart)}</p>
                    <p>{prettifyDate(timeEnd)}</p>
                    <p>{prettifyDate(diffBetweenDates(timeEnd, timeStart))}</p>
                </div>
                :
                <></>
            }
        </>
    )
} export default Timestamp