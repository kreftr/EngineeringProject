import React from "react";
import {Container, Row, Col, Tabs, Tab, ListGroup, ListGroupItem,} from "react-bootstrap";
import "./Terms.css"

function Terms(){

    return (
        <Container>
            <Row>
                <Col className={"col-1"}/>
                <Col className={"col-10"}>
                    <ul className={"TERMS_list"}>
                        <li className={"TERMS_list_element"}><p>Usernames cannot contain curses, adverts or any other content that may offend community</p></li>
                        <li className={"TERMS_list_element"}><p>It is strongly forbidden to upload racist content, pornography or any stuff similar to that</p></li>
                        <li className={"TERMS_list_element"}><p>Only one account per user is allowed, multi accounts will be banned</p></li>
                        <li className={"TERMS_list_element"}><p>Users can not impersonate others</p></li>
                        <li className={"TERMS_list_element"}><p>Administration does not take responsibility for content uploaded by users</p></li>
                        <li className={"TERMS_list_element"}><p>We reserve rights to delete or edit any content on a webpage</p></li>
                        <li className={"TERMS_list_element"}><p>It is forbidden to interrupt webpage behaviour by no means</p></li>
                        <li className={"TERMS_list_element"}><p>It is not allowed to post anything you have no rights to</p></li>
                        <li className={"TERMS_list_element"}><p>We do not take any responsibility for user data loss, neither in case of database fault, nor theft in teams</p></li>
                        <li className={"TERMS_list_element"}><p>Any terms violation will result account blocked, without possibility to restore that status</p></li>
                        <li className={"TERMS_list_element"}><p>These terms may be changed later</p></li>
                    </ul>
                </Col>
                <Col className={"col-1"}/>
            </Row>
        </Container>
    );
}
export default Terms;