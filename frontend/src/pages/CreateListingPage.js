import React from "react";
import NavBar from "../components/NavBar";
import CreateListingForm from "../components/CreateListingForm";

function CreateListingPage() {

  console.log("CreateListingPage.js");
  return (
    <div className="flex flex-col">
      <NavBar />
      <CreateListingForm />
    </div>
  );
}

export default CreateListingPage;
