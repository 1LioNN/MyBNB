import React from "react";
import NavBar from "../components/NavBar";
import Listings from "../components/Listings";

function MyListingsPage() {
  return (
    <div className="flex flex-col">
      <NavBar />
      <Listings />
    </div>
  );
}

export default MyListingsPage;
