import React from "react";
import { useNavigate } from "react-router-dom";
import ListingCards from "./MyListingCards";

function Listings() {
    const navigate = useNavigate();

    const navigateToForm = () => {
        navigate("/create");
    }

  return (
    <div className="flex flex-col">
      <div className="flex flex-row p-5 gap-24 justify-center ">
        <div className="font-bold text-2xl">My Listings</div>
        <button 
        className="p-2 bg-blue-500 rounded-lg text-white text-base"
        onClick={navigateToForm}>
          Create Listing
        </button>
      </div>
      <div className="flex flex-row justify-center">
        
        <ListingCards />
        </div>
    </div>
  );
}

export default Listings;
