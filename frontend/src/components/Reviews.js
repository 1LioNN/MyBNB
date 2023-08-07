import React from "react";
import { useState, useEffect } from "react";
import { useAuth } from "../Utils/AuthContext";
import axios from "axios";

function Reviews(props) {
  const [comments, setComments] = useState([]);
  const auth = useAuth();
  const listing = props.listing;
  try {
    useEffect(() => {
      if (props.listing.idlisting !== undefined) {
        axios
          .get("http://localhost:8000/listing/review/" + listing.idlisting, {
            withCredentials: true,
          })
          .then((response) => {
            setComments(response.data.reviews);
            console.log(response.data.comments);
          })
          .catch((err) => {
            console.log(err);
          });
      }
    }, [props.user]);

    return (
      <div className="flex flex-col w-1/3 mt-7 text-xl  mr-12 gap-5">
        <div className="font-bold text-2xl">Reviews </div>
        <div className="min-h-[55%] overflow-auto">
          {comments !== "undefined" &&
            comments.map(
              (comment) =>
                comment !== null &&
                (console.log(comment),
                (
                  <div
                    className="flex flex-col border-2 border-black p-2 mb-2 w-96 h-40 gap-1 rounded-md"
                    key={comment.idreview}
                  >
                    <div className="flex flex-row">
                      <div className="font-semibold">
                        User{comment.iduser}
                      </div>
                      <div className="font-bold ml-auto mr-2">
                        Rating: {comment.rating}
                      </div>
                    </div>

                    <div className="text-base">{comment.content}</div>
                  </div>
                ))
            )}
        </div>
      </div>
    );
  } catch (err) {
    window.location.reload();
    console.log(err);
  }
}

export default Reviews;
