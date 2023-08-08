import React from "react";
import NavBar from "../components/NavBar";
import ListingsFilter from "../components/ListingsFilter";

function BrowseListingsPage() {
  return (
    <div className="flex flex-col gap-10">
      <NavBar />
      
      <ListingsFilter />
    </div>
  );
}

export default BrowseListingsPage;