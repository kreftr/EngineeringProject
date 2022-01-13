import {Badge, Col, Image, ListGroupItem, Row} from "react-bootstrap";
import default_profile_picture from "../assets/images/default_profile_picture.jpg"
import "./Member.css"

function Member(props){


    return(
        <ListGroupItem className={"mt-3"}>
            <Row>
                <Col className={"MEMBER-container"}>
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
            </Row>
        </ListGroupItem>
    );


}
export default Member