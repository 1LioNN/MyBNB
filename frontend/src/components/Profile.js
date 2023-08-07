import React from "react";
import { useState, useEffect } from "react";
import axios from "axios";
import { useAuth } from "../Utils/AuthContext";

function Profile(props) {
  const auth = useAuth();
  const [profile , setProfile] = useState({});
  console.log(auth.user);
  console.log(sessionStorage.getItem("user"));
  useEffect(() => {
    axios
      .get("http://localhost:8000/user/"+ auth.user, {
        withCredentials: true,
      })
      .then((response) => {
        setProfile(response.data.data);
      })
      .catch((err) => {
        console.log(err);
      });
  } , [auth.user]);
  
  //Hide the first 6 digits of the SIN
  const hideSin = (sin) => {
    if (sin === undefined) return;
    const sinStr = String(sin);
    return "X".repeat(6)+ sinStr.slice(6);
  }

  const hideCreditCard = (creditCard) => {
    if (creditCard === undefined) return;
    const creditCardStr = String(creditCard);
    return "X".repeat(12)+ creditCardStr.slice(12);
  }

  console.log(profile);
//Display user information

  return (<div className="flex flex-col justify-center pt-1 p-24 gap-7 w-2/3">
    <div className="font-bold text-4xl">User Profile </div>
    <div className="text-xl"><span className="font-semibold">Name: </span> {profile.name}</div>
    <div className="text-xl"><span className="font-semibold">Username: </span> {profile.username}</div>
    <div className="text-xl"><span className="font-semibold">Address: </span> {profile.address}</div>
    <div className="text-xl"><span className="font-semibold">Birthday: </span> {profile.birthday}</div>
    <div className="text-xl"><span className="font-semibold">SIN: </span> {hideSin(profile.SIN)}</div>
    <div className="text-xl"><span className="font-semibold">Occupation: </span> {profile.occupation}</div>
    {profile.type !== "host" && 
    <div className="text-xl"><span className="font-semibold">Credit Card Number: </span> {hideCreditCard(profile.credit_number)}</div>}
    <div className="text-xl"><span className="font-semibold">Account Type: </span> {profile.userType}</div>
  </div>);
}

export default Profile;
