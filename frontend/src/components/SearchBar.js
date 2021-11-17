import React from 'react';
import {Button} from "@material-ui/core";

function SearchBar() {
    const searchBarStyle = {width: 190, height: 35, background: "#e0e0e0"}
    const searchBarButtonStyle = {background: "gray", width: 25, marginLeft: "15px"}

    return (
        <>
            <input style={searchBarStyle} type={'search'} placeholder={'Search'}/>
            <Button style={searchBarButtonStyle} variant="primary">Search</Button>
        </>
    );
}

export default SearchBar;