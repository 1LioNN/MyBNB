import React from "react";
import { useState, useEffect } from "react";
import axios from "axios";
import NavBar from "../components/NavBar";
import ListingInfo from "../components/ListingInfo";
import Reviews from "../components/Reviews";
import { useParams } from "react-router-dom";

function ViewListingPage() {
  // Get params from URL
  const { lid } = useParams();
  const [listing, setListing] = useState({});

  useEffect(() => {
    axios
      .get("http://localhost:8000/listing/" + lid, {
        withCredentials: true,
      })
      .then((response) => {
        setListing(response.data.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [lid]);

  return (
    <div className="flex flex-col">
      <NavBar />
      <div className="flex flex-row h-screen">
        <ListingInfo listing={listing} />
        <Reviews listing={listing} />
      </div>
    </div>
  );
}

export default ViewListingPage;
