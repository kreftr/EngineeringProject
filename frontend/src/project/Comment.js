import {Col, Image, ListGroupItem, Row} from "react-bootstrap";
import default_profile_picture from "../assets/images/default_profile_picture.jpg";

function Comment(props) {
    return (
        <ListGroupItem>
            <Row>
                <Col className={"col-4"} >
                    {props.comment.text}
                </Col>
                <Col>
                    <a href={`/profile/${props.comment.userId}`}>
                        {   props.comment.userPhoto ?
                            <Image src={`http://localhost:8080/photo?filename=${props.comment.userPhoto}`}
                                   roundedCircle={true}
                                   width = "50px"
                                   height = "50px"/>
                            :
                            <Image
                                src={default_profile_picture}
                                roundedCircle={true}
                                width = "50px"
                                height = "50px"/>
                        }
                    </a>
                    {props.comment.userName}
                </Col>
                <Col className={"col-4"}>
                    {props.comment.datee}
                </Col>
            </Row>
        </ListGroupItem>
    )
}

export default Comment;