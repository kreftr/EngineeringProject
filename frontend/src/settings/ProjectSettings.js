import React from "react";
import {Container,Row, Col, Tabs, Tab,} from "react-bootstrap";

import "./ProjectSettings.css"
import DeleteProjectSettings from "./DeleteProjectSettings";
import EditProjectSettings from "./EditProjectSettings";

function ProjectSettings(){

    return(
        <Container className={"PROJECTSETTINGS-project-container"}>
            <Row>
                <Col className={"col-2"}></Col>
                <Col className={"col-8"}>
                    <Tabs defaultActiveKey="Project details" className="mb-5" fill>
                        <Tab eventKey="Project details" title="Project details">
                            <EditProjectSettings/>
                        </Tab>
                        <Tab eventKey="Deleting Project" title="Deleting Project">
                            <DeleteProjectSettings/>
                        </Tab>
                    </Tabs>
                </Col>
                <Col className={"col-2"}></Col>
            </Row>
        </Container>
    );
}
export default ProjectSettings;