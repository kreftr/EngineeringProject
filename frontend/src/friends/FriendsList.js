import React from 'react';
import {Container, ListGroup, ListGroupItem} from "react-bootstrap";
import useAxiosGet from "../hooks/HttpRequests";
import Cookies from "js-cookie";
import Friend from "./Friend";

function FriendsList() {
    let content = null
    const cookie_user_id = Number(Cookies.get("userId"))
    let friends = useAxiosGet(`http://localhost:8080/friends/getAllFriends/${cookie_user_id}`)

    if (friends.data) {
        content =
            <Container className={"container"}>
                <ListGroup className={"list-group"}>
                    {
                        friends.data.map((friend, key) =>
                            <ListGroupItem key={key}>
                                <Friend friend={friend}/>
                            </ListGroupItem>
                        )
                    }
                </ListGroup>
            </Container>
    }

    return (
        <div>
            {content}
        </div>
    );
}

export default FriendsList;
