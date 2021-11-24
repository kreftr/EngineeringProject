import React from 'react';
import Chat from "./Chat";

function HomePage() {
    const chatStyle = {position: "fixed", right: "0", top: "0", bottom: "0", marginRight: "20px", marginBottom: "20px"}

    return (
        <div style={chatStyle}>
            <Chat/>
        </div>
    );
}

export default HomePage;
