import {Col, Image, ListGroupItem, Row} from "react-bootstrap";
import default_profile_picture from "../assets/images/default_profile_picture.jpg"
import "./Post.css";

function Post(props) {
    return (
        <ListGroupItem>
            <Row>
                <Col className={"col-4"} >
                   <h3 className={"FORUM-content"}>{props.post.title}</h3>
                    {props.post.text}
                </Col>
                <Col>
                    <a href={`/profile/${props.post.userId}`} className={"mr-3"}>
                        {   props.post.userPhoto ?
                            <Image src={`http://localhost:8080/photo?filename=${props.post.userPhoto}`}
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
                    {props.post.userName}
                </Col>
                <Col className={"col-4 POST-date"}>
                    {props.post.datee}
                </Col>
            </Row>
        </ListGroupItem>
    );
}



export default Post;