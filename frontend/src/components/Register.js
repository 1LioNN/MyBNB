import React from "react";
import { useState } from "react";
import { useAuth } from "../Utils/AuthContext";
import { useNavigate } from "react-router-dom";

function Register() {
  const auth = useAuth();
  const navigate = useNavigate();
  const [name, setName] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [address, setAddress] = useState("");
  const [birthday, setBirthday] = useState("");
  const [occupation, setOccupation] = useState("");
  const [sin, setSin] = useState(null);
  const [type, setType] = useState("renter");
  const [credit_number, setCreditNumber] = useState("");
  const [credit_password, setCreditPassword] = useState("");

  const changeType = (e) => {
    setType(e.target.value);
    console.log(type);
  };

  const submitHandler = (e) => {
    e.preventDefault();
    const formData = {
      name,
      username,
      password,
      address,
      birthday,
      occupation,
      sin: parseInt(sin),
    };

    if (type === "renter") {
      formData["credit_number"] = credit_number;
      formData["credit_password"] = credit_password;
    }

    auth.signup(formData, (status, data) => {
      if (status === 200) {
        alert("Successfully registered!");
        navigate("/signin");
      } else {
        alert("Invalid information");
      }
    });
  };

  const navigateToHome = () => {
    navigate("/");
  };

  const navigateToSignIn = () => {
    navigate("/signin");
  };

  return (
    <div className="flex flex-row justify-center">
      <div className="flex flex-col">
        <div className="flex flex-row text-3xl font-bold mb-4 w-[22rem] justify-center">
          <div>Register for an Account</div>
        </div>
        <div
          className="pb-4 font-bold cursor-pointer hover:underline text-cyan-600"
          onClick={navigateToHome}
        >
          {" "}
          Back to Home
        </div>
        <div>
          <form
            className="flex flex-col font-semibold text-md gap-2"
            onSubmit={submitHandler}
          >
            <label>Basic information</label>
            <input
              type="text"
              id="name"
              name="name"
              minLength={1}
              maxLength={255}
              placeholder="Full Name"
              required
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="border-gray-400 border-[1px] rounded-md p-1"
            ></input>
            <input
              type="text"
              id="username"
              name="username"
              minLength={1}
              maxLength={255}
              placeholder="Username"
              required
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="border-gray-400 border-[1px] rounded-md p-1"
            ></input>
            <input
              type="password"
              id="password"
              name="password"
              minLength={1}
              maxLength={255}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Password"
              required
              className="border-gray-400 border-[1px] rounded-md p-1"
            ></input>
            <label>Personal Information</label>
            <input
              type="text"
              id="address"
              name="address"
              maxLength={255}
              minLength={1}
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              placeholder="Address"
              required
              className="border-gray-400 border-[1px] rounded-md p-1"
            ></input>
            <input
              type="date"
              id="birthday"
              name="birthday"
              required
              maxLength={255}
              value={birthday}
              onChange={(e) => setBirthday(e.target.value)}
              placeholder="Birthday"
              className="border-gray-400 border-[1px] rounded-md p-1"
            ></input>
            <input
              type="text"
              inputMode="numeric"
              id="sin"
              name="sin"
              minLength={9}
              maxLength={9}
              value={sin}
              onChange={(e) => setSin(e.target.value)}
              placeholder="SIN Number"
              required
              className="border-gray-400 border-[1px] rounded-md p-1"
            ></input>
            <input
              type="text"
              id="occupation"
              name="occupation"
              minLength={1}
              maxLength={255}
              value={occupation}
              onChange={(e) => setOccupation(e.target.value)}
              placeholder="Occupation"
              required
              className="border-gray-400 border-[1px] rounded-md p-1"
            ></input>
            <label>Account Type</label>
            <select
              id="type"
              name="type"
              className="border-gray-400 border-[1px] rounded-md p-1"
              value={type}
              onChange={changeType}
              required
            >
              <option value="renter">Renter</option>
              <option value="host">Host</option>
            </select>
            {type === "renter" && (
              <>
                <label>Payment Information</label>
                <input
                  type="text"
                  id="cardnumber"
                  name="cardnumber"
                  minLength={16}
                  maxLength={16}
                  placeholder="Card Number"
                  value={credit_number}
                  required
                  onChange={(e) => setCreditNumber(e.target.value)}
                  className="border-gray-400 border-[1px] rounded-md p-1"
                ></input>
                <input
                  type="text"
                  id="expiry"
                  name="expiry"
                  maxLength={5}
                  placeholder="Expiry Date"
                  className="border-gray-400 border-[1px] rounded-md p-1"
                ></input>
                <input
                  type="text"
                  id="cvv"
                  name="cvv"
                  minLength={3}
                  maxLength={3}
                  value={credit_password}
                  onChange={(e) => setCreditPassword(e.target.value)}
                  placeholder="CVV"
                  required
                  className="border-gray-400 border-[1px] rounded-md p-1"
                ></input>
              </>
            )}
            <button
              type="submit"
              className="bg-cyan-600 h-12 text-white rounded-md p-1"
            >
              Register
            </button>
          </form>
          <div className="font-semibold">
            {" "}
            Already have an account?{" "}
            <span
              className="text-cyan-600 font-bold hover:cursor-pointer"
              onClick={navigateToSignIn}
            >
              {" "}
              Sign In!{" "}
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Register;
