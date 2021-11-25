import Profile from "./profile/Profile";
import React, {Component} from "react";
import {Container, Nav, Navbar} from "react-bootstrap";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";


class App extends Component{
  render() {
    return (
        <Router>
          <Navbar bg="light" expand="lg">
            <Container>
              <Navbar.Brand href="#home">ProjectName</Navbar.Brand>
              <Navbar.Toggle aria-controls="basic-navbar-nav" />
              <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="me-auto">
                  <Nav.Link href="#home">My profile</Nav.Link>
                  <Nav.Link href="#link">Friends</Nav.Link>
                  <Nav.Link href="#link">Chat</Nav.Link>
                  <Nav.Link href="/search">Search</Nav.Link>
                </Nav>
              </Navbar.Collapse>
            </Container>
          </Navbar>
          <Routes>
            <Route path={"profile/:id"} element={<Profile/>}/>

          </Routes>
        </Router>
    )
  };
}

export default App;
