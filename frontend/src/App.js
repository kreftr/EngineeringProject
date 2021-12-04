import "bootstrap/dist/css/bootstrap.min.css"
import Profile from "./profile/Profile";
import React, {Component} from "react";
import {Container, Nav, Navbar} from "react-bootstrap";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Search from "./search/Search";
import Registration from "./Auth/Register/Registration";
import Login from "./Auth/Login/Login";
import Chat from "./chat/Chat"
import Friends from "./friends/Friends"


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
                  <Nav.Link href="/friends">Friends</Nav.Link>
                  <Nav.Link href="/chat">Chat</Nav.Link>
                  <Nav.Link href="/search">Search</Nav.Link>
                  <Nav.Link href="/registration">Register</Nav.Link>
                  <Nav.Link href="/login">Login</Nav.Link>
                </Nav>
              </Navbar.Collapse>
            </Container>
          </Navbar>
          <Routes>
            <Route path={"profile/:id"} element={<Profile/>}/>
            <Route path={"friends"} element={<Friends/>}/>
            <Route path={"chat"} element={<Chat/>}/>
            <Route path={"search"} element={<Search/>}/>
            <Route path={"registration"} element={<Registration/>}/>
            <Route path={"login"} element={<Login/>}/>
          </Routes>
        </Router>
    )
  };
}

export default App;
