import React, {useEffect, useState} from "react";


function Clock(props) {
    const [timeCounter, setTimeCounter] = useState(0);  // time counted in seconds
    const [timerStarted, setTimerStarted] = useState(false)
    const [intervalId, setIntervalId] = useState(null)

    useEffect(() => {
        setTimerStarted(props.timerStarted)
    }, [props])

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
    }, [timerStarted, intervalId])


    return (
        <center>
            <h1>
                {timeCounter}
            </h1>
        </center>
    )
} export default Clock