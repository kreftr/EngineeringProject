import {FaStar} from "react-icons/all";

import "./Rating.css"
import {useState} from "react";


function Rating(){

    const [rating, setRating] = useState(null);
    const [hover, setHover] = useState(null);

    return(
        <div className={"RATING-stars-holder"}>
            {[...Array(5)].map((star, i) => {
                const ratingValue = i + 1;

                return(
                    <label>
                        <input className={"RATING-radio-input"} type={"radio"} name={"rating"} value={ratingValue}
                               onClick={()=>setRating(ratingValue)}/>
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