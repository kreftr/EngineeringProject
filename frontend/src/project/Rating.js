import {FaStar} from "react-icons/all";

import "./Rating.css"
import {useEffect, useState} from "react";
import axios from "axios";
import {useParams} from "react-router-dom";
import Cookies from "js-cookie";


function Rating(){

    const {id} = useParams();
    const [rating, setRating] = useState(null);
    const [hover, setHover] = useState(null);

    useEffect(() => {

        axios.get(`http://localhost:8080/project/getMyRating/${id}`,{
            headers: {
                'Authorization': Cookies.get("authorization")
            }
        }).then(response => {
            setRating(response.data)
        }).catch(err => {
            console.log(err.response)
        })

    },[])

    function vote(rating){
        console.log("XD")
        axios.post(`http://localhost:8080/project/rateProject/${id}?rating=${rating}`,null,{
            headers: {
                'Authorization': Cookies.get("authorization")
            }
        }).then(response => {
            window.location.reload();
        }).catch(err => {
            console.log(err.response)
        })
    }


    return(
        <div className={"RATING-stars-holder"}>
            {[...Array(5)].map((star, i) => {
                const ratingValue = i + 1;

                return(
                    <label key={i}>
                        <input className={"RATING-radio-input"} type={"radio"} name={"rating"} value={ratingValue}
                               onClick={()=>{setRating(ratingValue); vote(ratingValue);}}/>
                        <FaStar className={"RATING-star"} color={ratingValue <= (hover || rating) ? "#ffc107" : "#e4e5e9"}
                                onMouseEnter={()=>setHover(ratingValue)}
                                onMouseLeave={()=>setHover(null)}
                                size={50}/>
                    </label>
                );
            })}
            { rating ?
                <div className={"ml-4"}>
                      <h1>{rating}/5</h1>
                </div>
            :
                <></>
            }
        </div>
    );
};
export default Rating