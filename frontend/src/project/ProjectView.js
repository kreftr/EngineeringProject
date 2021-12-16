import {Button, Carousel, Col, Container, Image, Row} from "react-bootstrap";
import {FaFacebookSquare, FaGithubSquare, FaKickstarter, FaYoutube} from "react-icons/all";
import "./ProjectView.css"
import React from "react";
import Rating from "./Rating";




function ProjectView(){

    function x(){
        return(
            <div className={"PROJECT-VIEW-author-holder"}>
                <Image className={"PROJECT-VIEW-author-picture"} src={"https://lacollege.edu/wp-content/uploads/2021/09/blank-profile-picture.png"} roundedCircle={true}></Image>
                <h2 className={"ml-2"}>ExampleUser1</h2>
            </div>
        )
    }



    return(
        <Container className={"mt-5"}>
            <Row>
                <Col className={"col-2"}/>
                <Col className={"col-8"}>
                    <Row className={"mb-4"}>
                        <Col>
                            <div className={"PROJECT-VIEW-project-picture-col"}>
                                <Image
                                    className={"PROJECT-VIEW-project-picture"}
                                    src={"https://5.imimg.com/data5/KW/LH/MY-37697973/parabolic-solar-cooker-500x500.jpg"}/>
                            </div>
                        </Col>
                        <Col>
                            <div className={"PROJECT-VIEW-base-info"}>
                                <h1 className={"PROJECT-VIEW-title"}>
                                    Solar cooker
                                </h1>
                                <hr className={"mb-3"}/>
                                <h4 className={"PROJECT-VIEW-subtitle"}>
                                    A solar cooker is a device which uses the energy of direct sunlight to heat, cook or pasteurize drink and other food materials.
                                    Many solar cookers currently in use are relatively inexpensive, low-tech devices, although some are as powerful or as expensive as traditional stoves.
                                </h4>
                            </div>
                        </Col>
                    </Row>
                    <Row className={"mt-4"}>
                        <Col>
                            <Row className={"ml-5 mr-5"}>
                                    <Button className={"mb-3"} variant="primary">Join</Button>
                                    <Button variant="primary">Report</Button>
                            </Row>
                            <Row className={"mt-3"}>
                                <Rating/>
                            </Row>
                        </Col>
                        <Col>
                            <Row className={"PROJECT-VIEW-author-col mb-4"}>
                                {x()}
                            </Row>
                            <Row>
                                <Col>
                                    <Row>
                                        <div>
                                            <Col>
                                                <h1>81%</h1>
                                            </Col>
                                            <Col>
                                                <span>of voters fond this project interesting</span>
                                            </Col>
                                        </div>
                                    </Row>
                                </Col>
                                <Col>
                                    <span>Start date: </span>
                                    <h5>12-04-2021</h5>
                                    <span>End date: </span>
                                    <h5>27-12-2021</h5>
                                </Col>
                            </Row>
                        </Col>
                    </Row>
                    <hr className={"mb-4 mt-4"}/>
                    <Row>
                        <h1 className={"mb-4"}>About project</h1>
                        <span>
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut non suscipit erat. Pellentesque tempor ultrices justo quis rhoncus. Pellentesque placerat arcu a massa venenatis, et accumsan augue molestie. In felis magna, congue eget tempus ac, tincidunt vel dui. Nulla sit amet dolor mi. Vestibulum nec molestie dui, sit amet pharetra velit. Vestibulum mollis lectus ac felis gravida congue.

                                Donec feugiat quam felis. Nunc lacinia rhoncus orci sit amet facilisis. Proin semper erat at sem bibendum finibus. Nunc commodo felis eget finibus efficitur. Maecenas eget ullamcorper lectus, vitae porttitor erat. Vestibulum fermentum enim ac metus vulputate blandit. Mauris luctus turpis eu leo pretium pharetra.

                                Vestibulum accumsan nec mi eget consectetur. Donec eu ipsum in felis pretium porta at eget mi. Donec maximus elit gravida, mollis mi sed, fringilla nisi. Curabitur sed justo ultricies, ornare justo non, fringilla quam. Donec hendrerit pellentesque augue sit amet sagittis. Donec varius accumsan lacus id aliquet. Mauris vitae lobortis nulla. Sed eros eros, tempor vel lacinia sit amet, hendrerit sed nisi. Etiam lacinia semper lorem, ut ullamcorper urna euismod quis. Sed ultrices ut magna vel tempus. Vivamus dolor magna, dapibus sed facilisis a, vehicula id nunc. Morbi varius tristique elit, sit amet vestibulum massa commodo sed. Praesent dui sem, malesuada in convallis ut, accumsan ut dui. Mauris sodales justo non ante interdum, quis dignissim diam elementum. In sit amet consectetur purus, at luctus enim.

                                Donec aliquet lorem sit amet mauris tincidunt mattis. Pellentesque non aliquet felis. Donec facilisis pretium placerat. Pellentesque quis velit vulputate, blandit purus sed, ornare nisl. Morbi sed neque egestas, placerat urna non, placerat nisi. Aliquam a fringilla justo, a mollis dui. Nam arcu tellus, ultrices sed hendrerit nec, scelerisque eget nibh. Curabitur lobortis convallis augue ac egestas.

                                In vitae dolor luctus, tincidunt ligula feugiat, feugiat elit. Phasellus efficitur tincidunt nisi, sed vestibulum dui facilisis gravida. Fusce fermentum id purus ut venenatis. Duis a nisl eu velit placerat scelerisque. Vestibulum elementum finibus ipsum, vitae accumsan augue sollicitudin at. Suspendisse et odio tristique, rhoncus lorem eu, egestas tortor. Mauris nec finibus lectus. Quisque convallis sit amet eros eget viverra. Vivamus commodo lorem et suscipit.
                        </span>
                    </Row>
                    <hr className={"mb-4 mt-4"}/>
                    <Row>
                        <h1 className={"mb-3"}>Media</h1>
                        <div className={"PROJECT-VIEW-media"}>
                            <Col>
                                <FaYoutube size={125}/>
                            </Col>
                            <Col>
                                <FaGithubSquare size={125}/>
                            </Col>
                            <Col>
                                <FaFacebookSquare size={125}/>
                            </Col>
                            <Col>
                                <FaKickstarter size={125}/>
                            </Col>
                        </div>
                    </Row>
                    <hr className={"mb-4 mt-4"}/>
                    <Row>
                        <h1>Comments</h1>
                    </Row>
                </Col>
                <Col className={"col-2"}/>
            </Row>
        </Container>
    )
}
export default ProjectView