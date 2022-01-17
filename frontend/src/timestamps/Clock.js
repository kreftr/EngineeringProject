import React, {useEffect, useState} from "react";


function Clock(props) {

    const [timeCounter, setTimeCounter] = useState(0);  // time counted in seconds
    const [timerStarted, setTimerStarted] = useState(false)

    let intervalId = null;

    useEffect(() => {
        setTimerStarted(props.timerStarted)
    }, [props])

    useEffect(() => {
        console.log(timerStarted)
        if (timerStarted) {
            intervalId = setInterval(() => {
                setTimeCounter(timeCounter => timeCounter + 1);
            }, 1000);
        }
        else {
            clearInterval(intervalId)
            intervalId = null
            setTimeCounter(0)
        }
    }, [timerStarted])


    return (
        <div>
            {timeCounter}
        </div>
    )





} export default Clock