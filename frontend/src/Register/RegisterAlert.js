import React, {Component} from "react";
import {Alert} from "react-bootstrap";

import "./RegisterAlert.css"


class RegisterAlert extends Component {

    constructor(props) {
        super(props);

        this.state = {
            visible: this.props.visible,
            variant: this.props.variant,
            heading: this.props.heading,
            message: this.props.message
        }
    }

}