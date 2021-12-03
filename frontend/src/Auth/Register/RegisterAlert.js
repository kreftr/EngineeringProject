import React, {Component} from "react";
import {Alert} from "react-bootstrap";

import "./RegisterAlert.css";

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

    setVisible = (visible) => {
        this.setState({visible: visible});
    }

    setVariant = (variant) => {
        this.setState({variant: variant});
    }

    setHeading = (heading) => {
        this.setState({heading: heading});
    }

    setMessage = (message) => {
        this.setState({message: message});
    }


    render() {
        if(this.state.visible){
            return (
                <div className={"RegisterAlert"}>
                    <Alert variant={this.state.variant} onClose={() => this.setState({visible: false})} dismissible={true}>
                        <Alert.Heading>{this.state.heading}</Alert.Heading>
                        <p>
                            {this.state.message}
                        </p>
                    </Alert>
                </div>
            );
        }
        else return null;
    }
}

export default RegisterAlert