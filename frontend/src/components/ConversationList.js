import React, {useEffect, useState} from 'react';

function ConversationList() {
    const conversationListStyle = { width: 300, height: 260, background: "#e0e0e0" }

    const [messages, setMessages] = useState("");
    useEffect(() => {
        const url = 'http://localhost:3000/conversation/getAllMessages';
        const fetchData = async () => {
            try {
                const response = await fetch(url);
                const json = await response.json();
                console.log(json);
                setMessages(json.messages)
            } catch (error) {
                console.log("error", error);
            }
        };

        fetchData();
    }, []);

    return (
        <div style={conversationListStyle}>
            {messages}
        </div>
    );
}

export default ConversationList;