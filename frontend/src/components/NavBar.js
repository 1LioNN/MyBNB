import React from "react";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import { useAuth } from "../Utils/AuthContext";
import axios from "axios";
import { useEffect } from "react";
import { useState } from "react";

function NavBar() {
  const auth = useAuth();
  const navigate = useNavigate();
  const [type, setType] = useState("");

  useEffect(() => {
    if (auth.user === null) {
      return;
    }
    axios
      .get("http://localhost:8000/user/" + auth.user, {
        withCredentials: true,
      })
      .then((response) => {
        setType(response.data.data.userType);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [auth.user]);

  const clickHandler = () => {
    if (auth.user === null) {
      navigate("/");
    } else {
      navigate("/profile");
    }
  };

  const signOut = () => {
    auth.logout(() => {
      navigate("/");
    });
  };
  

  return (
    <div className="flex flex-row  h-24 w-fullfont-semibold items-center  pb-1 bg-cyan-600 text-white pl-5 gap-12 ">
      <div
        onClick={() => clickHandler()}
        className="hover:cursor-pointer text-xl  font-extrabold"
      >
        MyBNB
      </div>

      {auth.user !== null && (type !== "host") && (
        <Link
          to="/listings"
          className="hover:cursor-pointer hover:underline text-xl ml-5 font-semibold"
        >
          Listings
        </Link>
      )}

      {auth.user !== null && (
        <button
          className="text-cyan-600 bg-white rounded-lg p-2 ml-auto mr-10 pl-5 pr-5 hover:bg-neutral-200"
          onClick={signOut}
        >
          Sign Out
        </button>
      )}
    </div>
  );
}

export default NavBar;
