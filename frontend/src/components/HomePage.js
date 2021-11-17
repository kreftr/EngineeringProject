import React from 'react';
import Chat from "./Chat";

function HomePage() {
    const chatStyle = {position: "absolute", margin: "20px", right: "0", bottom: "0"}

    return (
        <div style={chatStyle}>
            <Chat/>
        </div>
    );
}

export default HomePage;
