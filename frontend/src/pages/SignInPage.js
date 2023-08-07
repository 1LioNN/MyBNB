import React from "react";
import NavBar from "../components/NavBar";
import SignIn from "../components/SignIn";

function SignInPage() {

  return (
    <div className="flex flex-col gap-10">
      <NavBar />
      <SignIn />
    </div>
  );
}

export default SignInPage;
