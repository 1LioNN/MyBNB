import React from "react";
import NavBar from "../components/NavBar";
import Register from "../components/Register";

function RegisterPage() {
  return (
    <div className="flex flex-col gap-10">
      <NavBar />
      <Register />
    </div>
  );
}

export default RegisterPage;
