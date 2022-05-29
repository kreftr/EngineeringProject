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
import Recovery from "./recovery/Recovery";
import ProjectView from "./project/ProjectView";
import Chat from "./chat/Chat"
import FriendsList from "./friends/FriendsList"
import Settings from "./profile/settings/Settings"
import ProjectList from "./project/ProjectList";
import Workspace from "./workspace/Workspace";
import Ranking from "./ranking/Ranking";
import Forum from "./forum/Forum";
import HomePage from "./homepage/HomePage";
import ProjectSettings from "./settings/ProjectSettings";
import AdminPanel from "./admin/AdminPanel";
import Thread from "./forum/Thread";


class App extends Component{

    logOut = () =>{
        Cookies.remove("authorization")
        Cookies.remove("userId")
        Cookies.remove("role")
    }

    render() {
        return (
            <>
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
                                        <Nav.Link href={"/projects"}>Projects</Nav.Link>
                                        <Nav.Link href={"/friends/"+Cookies.get("userId")}>Friends</Nav.Link>
                                        <Nav.Link href={"/conversations"}>Chat</Nav.Link>
                                    </>
                                    :
                                    <></>
                                }
                                <Nav.Link href="/search">Search</Nav.Link>
                                <Nav.Link href="/ranking">Ranking</Nav.Link>
                                <Nav.Link href="/forum">Forum</Nav.Link>
                                { Cookies.get("role") == "ADMIN" ?
                                    <Nav.Link href="/admin">Admin Panel</Nav.Link>
                                    :
                                    <></>
                                }
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
                    <Route path={"/"} element={<HomePage/>}/>
                    <Route path={"profile/:id"} element={<Profile/>}/>
                    <Route path={"profile/settings"} element={<Settings/>}/>
                    <Route path={"project/projectSettings/:id"} element={<ProjectSettings/>}/>
                    <Route path={"friends/:id"} element={<FriendsList/>}/>
                    <Route path={"projects"} element={<ProjectList/>}/>
                    <Route path={"project/:id"} element={<ProjectView/>}/>
                    <Route path={"project/:id/workspace"} element={<Workspace/>}/>
                    <Route path={"ranking"} element={<Ranking/>}/>
                    <Route path={"conversations/:id"} element={<Chat/>}/>
                    <Route path={"conversations"} element={<Chat/>}/>
                    <Route path={"search"} element={<Search/>}/>
                    <Route path={"registration"} element={<Registration/>}/>
                    <Route path={"verification/:token"} element={<Verification/>}/>
                    <Route path={"recovery/:token"} element={<Recovery/>}/>
                    <Route path={"login"} element={<Login/>}/>
                    <Route path={"forum"} element={<Forum/>}/>
                    <Route path={"admin"} element={<AdminPanel/>}/>
                    <Route path={"post/:id"} element={<Thread/>}/>
                </Routes>
            </Router>
        </>
        )
    };
}

export default App;