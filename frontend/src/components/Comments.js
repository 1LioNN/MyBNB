import React from "react";
import { useState, useEffect } from "react";
import { useAuth } from "../Utils/AuthContext";
import axios from "axios";

function Comments(props) {
  const [comments, setComments] = useState([]);
  const auth = useAuth();
  const profile = props.user;
  try {
    useEffect(() => {
      if (props.user.uid !== undefined) {
        axios
          .get("http://localhost:8000/user/comment/" + profile.uid, {
            withCredentials: true,
          })
          .then((response) => {
            setComments(response.data.comments);
          })
          .catch((err) => {
            console.log(err);
          });
      }
    }, [props.user]);

    const createComment = (e) => {
      e.preventDefault();
      const comment = {
        content: e.target.content.value,
        rating: parseFloat(e.target.rating.value),
        commentee: profile.uid,
      };
      axios
        .post("http://localhost:8000/comment", comment, {
          withCredentials: true,
        })
        .then((response) => {
          setComments([...comments, response.data.comment]);
        })
        .catch((err) => {
          console.log(err);
        });
    };

    return (
      <div className="flex flex-col w-1/3 mt-7 text-xl  mr-12 gap-5">
        <div className="font-bold text-4xl">Comments </div>
        <div className="min-h-[55%] overflow-auto">
          {comments !== "undefined" &&
            comments.map(
              (comment) =>
                comment !== null &&
                (console.log(comment),
                (
                  <div
                    className="flex flex-col border-2 border-black p-2 mb-2 w-96 h-40 gap-1 rounded-md"
                    key={comment.idcomment}
                  >
                    <div className="flex flex-row">
                      <div className="font-semibold">Host</div>
                      <div className="font-bold ml-auto mr-2">
                        Rating: {comment.rating}
                      </div>
                    </div>

                    <div className="text-base">{comment.content}</div>
                  </div>
                ))
            )}
        </div>
        <form className="flex flex-col w-96 gap-1" onSubmit={createComment}>
          <textarea
            required
            rows={3}
            maxLength={255}
            placeholder="Leave a comment"
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
      </div>
    );
  } catch (err) {
    window.location.reload();
    console.log(err);
  }
}

export default Comments;
