import React from "react";
import NavBar from "../components/NavBar";

function MyBookingsPage() {
  return (
    <div className="flex flex-col gap-10">
      <NavBar />
      <h1>My Listings</h1>
    </div>
  );
}

export default MyBookingsPage;