import React, {useEffect, useLayoutEffect, useState} from 'react';
import {Button, ButtonGroup} from "@material-ui/core";
import SearchBar from "./SearchBar";
import ToggleButtonGroup from 'react-bootstrap/ToggleButtonGroup'
import {ToggleButton} from "react-bootstrap";
import ConversationList from "./ConversationList";

function handleClick()
{
    return (
        console.log("clicked")
    );
}

function Chat() {
    const windowStyle = {width: 300, height: 380, background: "DarkGray", padding: "20px"}
    const searchScopeBoxStyle = {display: "flex", justifyContent: "center"}
    const searchScopeFriendsButtonStyle = {width: 125, height: 45, background: "red", marginRight: "9px"}
    const searchScopeAllButtonStyle = {width: 125, height: 45, background: "green", marginLeft: "9px"}
    const searchBarStyle = {margin: "0 auto", marginTop: "20px", display: "flex", justifyContent: "center"}
    const conversationWindowStyle = {marginTop: "20px"}

    let searchScopeValue = 1

    return (
        // TODO script these toggle buttons
        // TODO move search bar button to SearchBar.js
        <div style={windowStyle}>
            <ButtonGroup style={searchScopeBoxStyle} type="checkbox" value={searchScopeValue} onChange={handleClick}>
                <Button style={searchScopeFriendsButtonStyle} variant="primary" value={1}>Friends</Button>
                <Button style={searchScopeAllButtonStyle} variant="primary" value={2}>All</Button>
            </ButtonGroup>

            <div style={searchBarStyle}>
                <SearchBar/>
            </div>

            <div style={conversationWindowStyle}>
                <ConversationList/>
            </div>
        </div>
    );
}

export default Chat;
