import {Alert, Button, Form, FormControl, FormGroup, Spinner} from "react-bootstrap";
import React, {useState} from "react";
import axios from "axios";


function RecoveryForm(){

    const [email, setEmail] = useState();
    const [responseCode, setResponseCode] = useState();
    const [responseMessage, setResponseMessage] = useState();
    const [loading, setLoading] = useState(false);

    async function handleRecovery(e){
        e.preventDefault()
        setLoading(true)

        await axios.post("http://localhost:8080/recovery", {
            "email": email
        })
            .then(response => {
                setResponseCode(response.status)
                setResponseMessage(response.data.message)
            })
            .catch(err => {
                if (err.response.status === 400) {
                    setResponseMessage("*"+err.response.data.error)
                }
                else if (err.response.status === 404 || err.response.status === 409){
                    setResponseMessage("*"+err.response.data.message)
                }
                else {
                    setResponseMessage("SERVER ERROR")
                }
                setResponseCode(err.response.status)
            })

        setLoading(false)
    }


    return(
        <Form className={"LOGIN-form"} onSubmit={handleRecovery}>
            <h3>
                Forgot your password?
            </h3>
            <div>
                Enter your email and we’ll send you a recovery link.
            </div>
            <FormGroup className={"mb-3 mt-4"}>
                <FormControl type={"email"}
                             value={email}
                             onChange={(e) => setEmail(e.target.value)}
                             required/>
            </FormGroup>
            { !loading ?
                <Button className={"mb-4"} variant="primary" type="submit">
                    Send recovery link
                </Button>
                :
                <center>
                    <Spinner animation="border"/>
                </center>
            }
            {responseCode === 200 && responseMessage ?
                    <Alert variant={"success"}>
                        <center>
                            {responseMessage}
                        </center>
                    </Alert>
                : responseMessage ?
                    <Alert variant={"danger"}>
                        <center>
                            {responseMessage}
                        </center>
                    </Alert>
                :
                    <></>
            }
        </Form>
    )

}
export default RecoveryForm