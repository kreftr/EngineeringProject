import "bootstrap/dist/css/bootstrap.min.css"
import React, {Component} from "react";
import {Container, Nav, Navbar, Button} from "react-bootstrap";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Cookies from "js-cookie"

import Profile from "./profile/Profile";
import Search from "./search/Search";
import Registration from "./registration/Registration";
import Verification from "./registration/Verification";
import Login from "./login/Login";
import Chat from "./chat/Chat"
import ConversationList from "./chat/ConversationList"
import Friends from "./friends/Friends"


class App extends Component{

    logOut = () =>{
        Cookies.remove("authorization")
        Cookies.remove("userId")
    }

    render() {
        return (
            <Router>
                <Navbar bg="light" expand="lg">
                    <Container>
                        <Navbar.Brand href="/">ProjectName</Navbar.Brand>
                        <Navbar.Toggle aria-controls="basic-navbar-nav" />
                        <Navbar.Collapse id="basic-navbar-nav">
                            <Nav className="me-auto">
                                { Cookies.get("authorization") ?
                                    <>
                                        <Nav.Link href={"/profile/"+Cookies.get("userId")}>My profile</Nav.Link>
                                        <Nav.Link href="/friends">Friends</Nav.Link>
                                        <Nav.Link href={"/conversations/"+Cookies.get("userId")}>Chat</Nav.Link>
                                    </>
                                    :
                                    <></>
                                }
                                <Nav.Link href="/search">Search</Nav.Link>
                                <Nav.Link href="/ranking">Ranking</Nav.Link>
                                <Nav.Link href="/forum">Forum</Nav.Link>
                                <Nav.Link href="/aboutUs">About us</Nav.Link>
                            </Nav>
                        </Navbar.Collapse>
                        <Nav className="me-auto">
                            { !Cookies.get("authorization") ?
                                <>
                                    <Nav.Link href={"/login"}>Log In</Nav.Link>
                                    <Button href={"/registration"} variant="primary">Sign In</Button>
                                </>
                                :
                                <Button href={"/"} onClick={this.logOut} variant="primary">Log out</Button>
                            }
                        </Nav>
                    </Container>
                </Navbar>
                <Routes>
                    <Route path={"profile/:id"} element={<Profile/>}/>
                    <Route path={"chat/:conversation_id"} element={<Chat/>}/>
                    <Route path={"friends"} element={<Friends/>}/>
                    <Route path={"conversations/:conversation_id"} element={<ConversationList/>}/>
                    <Route path={"search"} element={<Search/>}/>
                    <Route path={"registration"} element={<Registration/>}/>
                    <Route path={"verification/:token"} element={<Verification/>}/>
                    <Route path={"login"} element={<Login/>}/>
                </Routes>
            </Router>
        )
    };
}

export default App;