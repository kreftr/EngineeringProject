import React from "react";
import {Container,Row, Col, Tabs, Tab,} from "react-bootstrap";

import "./Settings.css"
import AccountSettings from "./AccountSettings";
import ProfileSettings from "./ProfileSettings";

function Settings(){

    return(
        <Container className={"SETTINGS-profile-container"}>
            <Row>
                <Col className={"col-2"}></Col>
                <Col className={"col-8"}>
                    <Tabs defaultActiveKey="Profile details" className="mb-5" fill>
                        <Tab eventKey="Profile details" title="Profile details">
                            <ProfileSettings/>
                        </Tab>
                        <Tab eventKey="Account details" title="Account settings">
                            <AccountSettings/>
                        </Tab>
                    </Tabs>
                </Col>
                <Col className={"col-2"}></Col>
            </Row>
        </Container>
    );
}
export default Settings;