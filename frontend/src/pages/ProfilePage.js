import React from "react";
import NavBar from "../components/NavBar";
import Profile from "../components/Profile";
import Comments from "../components/Comments";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import { useAuth } from "../Utils/AuthContext";

function ProfilePage() {
    //get user profile from params
    const { uid } = useParams();
    const auth = useAuth();
    const [profile, setProfile] = useState({});
    useEffect(() => {
        axios
          .get("http://localhost:8000/user/" + uid, {
            withCredentials: true,
          })
          .then((response) => {
            setProfile(response.data.data);
          })
          .catch((err) => {
            console.log(err);
          });
      }, [uid]);
  return (
    <div className="flex flex-col">
      <NavBar />
      <div className="flex flex-row">
      < Profile  user={profile}/>
        < Comments user={profile} />
    </div>

    </div>
  );
}

export default ProfilePage;
