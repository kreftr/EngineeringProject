import "bootstrap/dist/css/bootstrap.min.css"
import Profile from "./profile/Profile";
import React, {Component} from "react";
import {Container, Nav, Navbar, Button} from "react-bootstrap";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";

import Search from "./search/Search";
import Registration from "./registration/Registration";
import Verification from "./registration/Verification";

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
              <Nav className="me-auto">
                  <Nav.Link>Log In</Nav.Link>
                  <Button href={"/registration"} variant="primary">Sign In</Button>
              </Nav>
            </Container>
          </Navbar>
          <Routes>
            <Route path={"profile/:id"} element={<Profile/>}/>
            <Route path={"search"} element={<Search/>}/>
            <Route path={"registration"} element={<Registration/>}/>
            <Route path={"verification/:token"} element={<Verification/>}/>
          </Routes>
        </Router>
    )
  };
}

export default App;
