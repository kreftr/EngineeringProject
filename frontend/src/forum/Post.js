import {Col, Container, Image, ListGroupItem, Row} from "react-bootstrap";
import {useParams} from "react-router-dom";
import profile from "../profile/Profile"
import default_profile_picture from "../assets/images/default_profile_picture.jpg"
import {useEffect, useState} from "react";
import axios from "axios";
import Cookies from "js-cookie";
import Moment from 'moment';
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
                    <a href={`/profile/${props.post.userId}`}>
                        {   props.post.userPhoto ?
                            <Image src={`http://localhost:8080/photo?filename=${props.post.userPhoto}`}
                                   roundedCircle={true}
                                   width = "50px"
                                    height={"50px"}/>
                            :
                            <Image
                            src={default_profile_picture}
                            roundedCircle={true}/>
                        }
                    </a>
                    {props.post.userName}
                </Col>
                <Col className={"col-4"}>
                    {props.post.datee}
                </Col>
            </Row>
        </ListGroupItem>
    );
}



export default Post;