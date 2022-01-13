import {Badge, Button, Col, Image, ListGroupItem, Row} from "react-bootstrap";
import default_profile_picture from "../assets/images/default_profile_picture.jpg"
import "./Member.css"
import axios from "axios";
import Cookies from "js-cookie";
import {useParams} from "react-router-dom";

function Member(props){

    const {id} = useParams();

    function promote() {
        axios.post(`http://localhost:8080/project/promoteMember/${props.member.userId}?projectId=${id}`, null,
            {headers:{'Authorization': Cookies.get("authorization")}}
        ).then(response => {
            window.location.reload();
        }).catch(err => {
            console.log(err.response)
        })
    }

    function degrade() {
        axios.post(`http://localhost:8080/project/degradeMember/${props.member.userId}?projectId=${id}`, null,
            {headers:{'Authorization': Cookies.get("authorization")}}
        ).then(response => {
            window.location.reload();
        }).catch(err => {
            console.log(err.response)
        })
    }

    function remove() {
        axios.delete(`http://localhost:8080/project/removeMember/${props.member.userId}?projectId=${id}`,
            {headers:{'Authorization': Cookies.get("authorization")}}
        ).then(response => {
            window.location.reload();
        }).catch(err => {
            console.log(err.response)
        })
    }


    return(
        <ListGroupItem className={"mt-3"}>
            <Row>
                <Col className={"col-7 MEMBER-container"}>
                    <a href={`/profile/${props.member.userId}`}>
                        { props.member.profilePhoto ?
                            <Image src={`http://localhost:8080/photo?filename=${props.member.profilePhoto}`}
                                   width={100} height={100} rounded={true}/>
                            :
                            <Image src={default_profile_picture}
                                   width={100} height={100} rounded={true}/>
                        }
                    </a>
                    <h1 className={"ml-3 MEMBER-username"}>{props.member.username}</h1>
                    { props.member.projectRole === "OWNER" ?
                        <h2 className={"ml-3 MEMBER-username"}>
                            <Badge pill={true} bg="danger">{props.member.projectRole}</Badge>
                        </h2>
                        : props.member.projectRole === "MODERATOR" ?
                        <h2 className={"ml-3 MEMBER-username"}>
                            <Badge pill={true} bg="primary">{props.member.projectRole}</Badge>
                        </h2>
                        :
                        <h2 className={"ml-3 MEMBER-username"}>
                            <Badge pill={true} bg="secondary">{props.member.projectRole}</Badge>
                        </h2>
                    }
                </Col>
                <Col className={"col-5 MEMBER-username"}>
                    <Row>

                        { props.role === "OWNER" && props.member.projectRole === "PARTICIPANT" ?
                            <Col>
                                <Button variant={"primary"} onClick={() => {promote();}}>Promote</Button>
                            </Col>
                            :
                            <></>
                        }
                        { props.role === "OWNER" && props.member.projectRole === "MODERATOR" ?
                            <Col>
                                <Button variant={"warning"} onClick={() => {degrade();}}>Degrade</Button>
                            </Col>
                            :
                            <></>
                        }
                        { props.role === "OWNER" && (props.member.projectRole === "MODERATOR" || props.member.projectRole === "PARTICIPANT") ?
                            <Col>
                                <Button variant={"danger"} onClick={() => {remove();}}>Remove</Button>
                            </Col>
                            : props.role === "MODERATOR" && props.member.projectRole === "PARTICIPANT" ?
                                <Col>
                                    <Button variant={"danger"} onClick={() => {remove();}}>Remove</Button>
                                </Col>
                                :
                                <></>
                        }
                    </Row>
                </Col>
            </Row>
        </ListGroupItem>
    );


}
export default Member