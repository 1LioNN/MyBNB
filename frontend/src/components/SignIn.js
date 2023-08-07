import React from "react";
import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
function SignIn() {

  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const submitHandler = (e) => {
    e.preventDefault();
    const formData = {
      username,
      password,
    };

    axios
      .post("http://localhost:8000/user/signin", formData)
      .then((res) => {
        console.log(res);
        if (res.status === 200) {
          navigate("/profile");
        } else {
          alert("Error");
        }
      })
      .catch((err) => {
        alert("Invalid Credentials");
      });
  };

  const navigateToHome = () => {
    navigate("/");
  }

  const navigateToRegister = () => {
    navigate("/register");
  }

  return (
    <div className="flex flex-row justify-center">
      <div className="flex flex-col">
        <div className="flex flex-row text-3xl font-bold mb-4 w-[22rem] justify-center">
          <div>Sign In to MyBNB</div>
        </div>
        <div className="pb-4 font-bold cursor-pointer hover:underline text-cyan-600" onClick={navigateToHome}> Back to Home</div>
        <div>
          <form
            className="flex flex-col font-semibold text-md gap-2"
            onSubmit={submitHandler}
          >
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
            <button
              type="submit"
              className="bg-cyan-600 h-12 text-white rounded-md p-1"
            >
              Sign In
            </button>
          </form>
          <div className="font-semibold"> Don't have an account? <span className="text-cyan-600 font-bold hover:cursor-pointer" onClick={navigateToRegister}> Register Now! </span></div>
        </div>
      </div>
    </div>
  );
}

export default SignIn;
