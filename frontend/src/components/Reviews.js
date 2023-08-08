import React from "react";
import { useState, useEffect } from "react";
import { useAuth } from "../Utils/AuthContext";
import axios from "axios";

function Reviews(props) {
  const [comments, setComments] = useState([]);
  const auth = useAuth();
  const listing = props.listing;
  console.log(listing);
  // get url of current page
  const url = window.location.href;
  const urlArray = url.split("/");
  const page = urlArray[urlArray.length - 2];
  console.log(page);

  const createReview = (e) => {
    e.preventDefault();
    const form = {
      content: e.target.content.value,
      rating: parseFloat(e.target.rating.value),
      idlisting: listing.idlisting,
    };

    axios
      .post("http://localhost:8000/review", form, {
        withCredentials: true,
      })
      .then((response) => {
        setComments([...comments, response.data.review]);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  try {
    useEffect(() => {
      console.log(props.listing.idlisting);
      if (props.listing.idlisting !== undefined) {
        axios
          .get("http://localhost:8000/listing/review/" + listing.idlisting, {
            withCredentials: true,
          })
          .then((response) => {
            setComments(response.data.reviews);
            console.log(response.data.reviews);
          })
          .catch((err) => {
            console.log(err);
          });
      }
    }, [props.listing.idlisting]);

    return (
      <div className="flex flex-col w-1/3 mt-5 text-xl  mr-12 gap-5">
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
                      <div className="font-semibold">User{comment.iduser}</div>
                      <div className="font-bold ml-auto mr-2">
                        Rating: {comment.rating}
                      </div>
                    </div>

                    <div className="text-base">{comment.content}</div>
                  </div>
                ))
            )}
        </div>
        {page === "browse" && (
          <form className="flex flex-col w-96 gap-1" onSubmit={createReview}>
            <textarea
              required
              rows={3}
              maxLength={255}
              placeholder="Leave a Review"
              className="border-[1px] border-black p-2 rounded-lg"
              name="content"
            />
            <input
              type="number"
              placeholder="Rating"
              max={5}
              className="border-[1px] border-black p-2 rounded-lg"
              name="rating"
            />

            <button
              type="submit"
              className="mt-3 bg-blue-500 hover:bg-blue-700 text-white font-bold py-3 px-4 rounded-lg"
            >
              Submit
            </button>
          </form>
        )}
      </div>
    );
  } catch (err) {
    //reload page if error
    window.location.reload();
    console.log(err);
  }
}

export default Reviews;
