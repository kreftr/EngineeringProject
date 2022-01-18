import React, {useEffect, useState} from "react";


function Clock(props) {
    const [timeCounter, setTimeCounter] = useState(0);  // time counted in seconds
    const [timerStarted, setTimerStarted] = useState(false)
    const [intervalId, setIntervalId] = useState(null)

    useEffect(() => {
        setTimerStarted(props.timerStarted)
    }, [props])

    useEffect(() => {  // send time upwards to parent
        props.onTimeChange(timeCounter)
    }, [timeCounter])

    useEffect(() => {
        if (timerStarted) {
            let intervalId = setInterval(() => {
                setTimeCounter(timeCounter => timeCounter + 1);
            }, 1000);
            setIntervalId(intervalId)
        }
        else if (!timerStarted){
            clearInterval(intervalId)
            setTimeCounter(0)
        }
    }, [timerStarted])


    return (
        <div>
            {timeCounter}
        </div>
    )
} export default Clock