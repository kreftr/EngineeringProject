import "./ProjectWorkspace.css"
import {Col, Container, Nav, Row, Tab} from "react-bootstrap";
import {useParams} from "react-router-dom";


function ProjectWorkspace(){

    const {id} = useParams();

    return(
        <Container className={"mt-5"}>
            <Row>
                <Tab.Container id="left-tabs-example" defaultActiveKey="first">
                    <Row>
                        <Col>
                            <Nav variant="pills" className="flex-column">
                                <Nav.Item>
                                    <Nav.Link className={"mb-3"} eventKey="first">News</Nav.Link>
                                </Nav.Item>
                                <Nav.Item>
                                    <Nav.Link className={"mb-3"} eventKey="second">Tasks</Nav.Link>
                                </Nav.Item>
                                <Nav.Item>
                                    <Nav.Link className={"mb-3"} eventKey="third">Files</Nav.Link>
                                </Nav.Item>
                                <Nav.Item>
                                    <Nav.Link className={"mb-3"} eventKey="fourth" disabled={true}>Workspace</Nav.Link>
                                </Nav.Item>
                                <Nav.Item>
                                    <Nav.Link className={"mb-3"} eventKey="fifth" disabled={true}>Teams</Nav.Link>
                                </Nav.Item>
                                <Nav.Item>
                                    <Nav.Link className={"mb-3"} eventKey="sixth">Members</Nav.Link>
                                </Nav.Item>
                                <Nav.Item>
                                    <Nav.Link className={"mb-3"} eventKey="seventh">Project panel</Nav.Link>
                                </Nav.Item>
                            </Nav>
                        </Col>
                        <Col sm={9}>
                            <Tab.Content>
                                <Tab.Pane eventKey="first">
                                    Hello
                                </Tab.Pane>
                                <Tab.Pane eventKey="second">
                                    World
                                </Tab.Pane>
                            </Tab.Content>
                        </Col>
                    </Row>
                </Tab.Container>
            </Row>
        </Container>
    );

}
export default ProjectWorkspace;