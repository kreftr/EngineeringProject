import React, {useEffect, useLayoutEffect, useState} from 'react';
import SearchBar from "./SearchBar";
import ConversationList from "./ConversationList";

function handleClick()
{
    return (
        console.log("clicked")
    );
}

function Chat() {
    const windowStyle = {width: 300, height: "100vh", background: "DarkGray"}
    const searchBarStyle = {margin: "0 auto", marginTop: "20px", display: "flex", justifyContent: "center"}
    const conversationWindowStyle = {marginTop: "20px"}

    return (
        <div style={windowStyle}>
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
