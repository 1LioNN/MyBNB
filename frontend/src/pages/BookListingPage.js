import React from "react";
import NavBar from "../components/NavBar";
import BookingForm from "../components/BookingForm";
import Reviews from "../components/Reviews";
import { useParams } from "react-router-dom";
import axios from "axios";
import { useState, useEffect } from "react";

function BookListingsPage() {

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
        <BookingForm listing={listing} />
        <Reviews listing={listing} />
        </div>
    </div>
  );
}

export default BookListingsPage;