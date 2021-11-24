import React from 'react';
import SearchIcon from "@material-ui/icons/Search";

function SearchBar() {
    const searchBarContainerStyle = {border: "1px solid red", height: 35}
    const inputStyle = {background: "#e0e0e0"}
    const searchIconStyle = {marginTop: "auto", marginBottom: "auto", marginRight: "5px"}

    return (
        <div style={searchBarContainerStyle}>
            <SearchIcon style={searchIconStyle}/>
            <input style={inputStyle} type={'search'} placeholder={'Search'}/>
        </div>
    );
}

export default SearchBar;