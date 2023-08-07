import React from "react";
import { useState, useEffect } from "react";
import axios from "axios";
import { useAuth } from "../Utils/AuthContext";
import { useNavigate } from "react-router-dom";

function Profile(props) {
  const navigate = useNavigate();

  const profile = props.user;
    const auth = useAuth();

  const deleteAccount = () => {
    axios
      .delete("http://localhost:8000/user/" + props.user.uid, {
        withCredentials: true,
      })
      .then((response) => {
        auth.logout();
        navigate("/");
      })
      .catch((err) => {
        console.log(err);
      });
  };
  //Hide the first 6 digits of the SIN
  const hideSin = (sin) => {
    if (sin === undefined) return;
    const sinStr = String(sin);
    return "X".repeat(6) + sinStr.slice(6);
  };

  const hideCreditCard = (creditCard) => {
    if (creditCard === undefined) return;
    const creditCardStr = String(creditCard);
    return "X".repeat(12) + creditCardStr.slice(12);
  };

  //Display user information

  return (
    <div className="flex flex-col ml-20 pt-1 p-24 gap-7 w-2/3 mt-7">
      <div className="font-bold text-2xl">User Profile </div>
      <div className="text-xl">
        <span className="font-semibold">Name: </span> {profile.name}
      </div>
      <div className="text-xl">
        <span className="font-semibold">Username: </span> {profile.username}
      </div>
      <div className="text-xl">
        <span className="font-semibold">Address: </span> {profile.address}
      </div>
      <div className="text-xl">
        <span className="font-semibold">Birthday: </span> {profile.birthday}
      </div>
      <div className="text-xl">
        <span className="font-semibold">SIN: </span> {hideSin(profile.SIN)}
      </div>
      <div className="text-xl">
        <span className="font-semibold">Occupation: </span> {profile.occupation}
      </div>
      {profile.userType !== "host" && (
        <div className="text-xl">
          <span className="font-semibold">Credit Card Number: </span>{" "}
          {hideCreditCard(profile.credit_number)}
        </div>
      )}

      <div className="text-xl">
        <span className="font-semibold">Account Type: </span> {profile.userType}
      </div>
      {profile.userType !== "admin" && (auth.user === profile.uid) && (
        <button
          className="text-white border-2 border-red-500 rounded-lg bg-red-500  hover:bg-red-900 pb-4 p-3 w-48 mt-40"
          onClick={deleteAccount}
        >
          Delete Account
        </button>
      )}
    </div>
  );
}

export default Profile;
