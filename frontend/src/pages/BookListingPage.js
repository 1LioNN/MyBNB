import React from "react";
import NavBar from "../components/NavBar";

function BookListingsPage() {
  return (
    <div className="flex flex-col gap-10">
      <NavBar />
      <h1>My Listings</h1>
    </div>
  );
}

export default BookListingsPage;