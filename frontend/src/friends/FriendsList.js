import React, {useEffect, useState} from 'react';
import {Container, ListGroup, ListGroupItem} from "react-bootstrap";
import Cookies from "js-cookie";
import Friend from "./Friend";
import axios from "axios";


function FriendsList() {
    const cookie_user_id = Number(Cookies.get("userId"))
    const [friends, setFriends] = useState([])
    const [friendsLoading, setFriendsLoading] = useState(true)
    const [friendsCode, setFriendsCode] = useState(null)

    useEffect(() => {
        axios.get(`http://localhost:8080/friends/getAllFriends/${cookie_user_id}`)
            .then(response => {
                setFriendsCode(response.status);
                setFriends(response.data);
                setFriendsLoading(false);
            })
            .catch(err => {
                setFriendsCode(err.response.status)
                setFriendsLoading(false);
            })
    }, [cookie_user_id])

    return (
        <Container className={"container"}>
            { friendsCode === 200 && !friendsLoading ?
                <ListGroup className={"list-group"}>
                    {
                        friends.map((friend, key) =>
                            <ListGroupItem key={key}>
                                <Friend friend={friend}/>
                            </ListGroupItem>
                        )
                    }
                </ListGroup>
                :
                <></>
            }
        </Container>
    );
}

export default FriendsList;
