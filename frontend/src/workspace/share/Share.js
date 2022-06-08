import {Button, Col, Image, Row} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import FacebookLogin from 'react-facebook-login';
import {render} from "react-dom";
import axios from "axios";
import Cookies from "js-cookie";

function Share(props){

    const [loggedStatus, setLoggedStatus] = useState(false)
    const [id, setId] = useState(null)

    useEffect(() => {
        // https://connect.facebook.net/en_US/sdk.js

        axios.get(`https://connect.facebook.net/en_US/sdk.js`)
            .then(response =>{
                eval(response.data);  // run facebook initialisation script
            })
            .catch(err => {
                console.log(err.response)
            })
            .finally(() => {
                window.fbAsyncInit = function() {
                    window.FB.init({
                        appId      : '1896942843849552',
                        cookie     : false,
                        xfbml      : false,
                        version    : 'v2.11'
                    });

                    window.FB.getLoginStatus(function(response) {
                        statusChangeCallback(response);
                    }.bind(this));
                }.bind(this);

                (function(d, s, id) {
                    var js, fjs = d.getElementsByTagName(s)[0];
                    if (d.getElementById(id)) return;
                    js = d.createElement(s); js.id = id;
                    js.src = "https://connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.11&appId=''";
                    fjs.parentNode.insertBefore(js, fjs);
                }(document, 'script', 'facebook-jssdk'))
            })
    }, [])

    function loggedIn(response) {
        setLoggedStatus(true)
        setId(response.authResponse.userID)
    }

    function loggedOut() {
        setLoggedStatus(false)
        window.FB.logout();
    }

    function statusChangeCallback(response) {
        console.log('statusChangeCallback');
        console.log(response);
        if (response.status === 'connected') {
            loggedIn(response)
        } else if (response.status === 'not_authorized') {
            loggedOut()
            console.log(loggedStatus);
        } else {
            loggedOut()
            console.log(loggedStatus);
        }
    }

    function checkLoginState() {
        window.FB.getLoginStatus(function(response) {
            statusChangeCallback(response);
        }.bind(this));
    }

    function handleClick() {
        window.FB.login(() => {checkLoginState()});
        console.log("asd")
        console.log("asd")
        console.log("asd")
        console.log("asd")
    }


    if (loggedStatus === false) {
        return (
            <button onClick={handleClick}
                    className="fb-login-button"
                    data-max-rows="1"
                    data-size="large"
                    data-button-type="continue_with"
                    data-show-faces="false"
                    data-auto-logout-link="true"
                    data-use-continue-as="true"
            />

        );
    } else {
        return (
            <p>Logged in!</p>
        )
    }



}
export default Share;