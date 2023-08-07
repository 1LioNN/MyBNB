import React from "react";
import { useState, useEffect } from "react";
import axios from "axios";
import { useAuth } from "../Utils/AuthContext";
import { useNavigate } from "react-router-dom";

function ListingInfo(props) {
  const listing = props.listing;
  const auth = useAuth();
  const [start_date, setStartDate] = useState("");
  const [end_date, setEndDate] = useState("");
  const [price_per_day, setPricePerDay] = useState(0.0);

  useEffect(() => {
    setStartDate(listing.start_date);
    setEndDate(listing.end_date);
    setPricePerDay(listing.price_per_day);
  }, [listing]);

  const deleteListing = () => {
    console.log(listing);
  };

  const updateListing = (e) => {
    e.preventDefault();

    if (start_date === "") {
      setStartDate(listing.start_date);
    }
    if (end_date === "") {
      setEndDate(listing.end_date);
    }
    if (price_per_day === "") {
      setPricePerDay(listing.price_per_day);
    }
    const data = {
      start_date: start_date,
      end_date: end_date,
      price_per_day: parseFloat(price_per_day),
    };
    console.log(data);
    axios
      .patch("http://localhost:8000/listing/" + listing.idlisting, data, {
        withCredentials: true,
      })
      .then((response) => {
        console.log(response);
      })
      .catch((err) => {
        console.log(err);
      });

    console.log(data);
  };

  return (
    <div className="flex flex-col ml-20 pt-1 p-24 gap-7 w-2/3 mt-7">
      <div className="font-bold text-2xl">Listing Info </div>
      <div className="text-xl">
        <span className="font-semibold">Address: </span> {listing.address}
      </div>
      <div className="text-xl">
        <span className="font-semibold">City: </span> {listing.city}
      </div>
      <div className="text-xl">
        <span className="font-semibold">Country: </span> {listing.country}
      </div>
      <div className="text-xl">
        <span className="font-semibold">Postal Code: </span>{" "}
        {listing.postal_code}
      </div>

      <div className="text-xl">
        <span className="font-semibold">Type: </span> {listing.type}
      </div>
      <form className="flex flex-col gap-2" onSubmit={updateListing}>
        <label className="text-xl font-semibold">Start Date: </label>
        <input
          name="start_date"
          type="date"
          value={start_date}
          onChange={(e) => setStartDate(e.target.value)}
          className="border-gray-400 border-[1px] rounded-md p-1 w-1/4"
        />
        <label className="text-xl font-semibold">End Date: </label>
        <input
          name="end_date"
          type="date"
          value={end_date}
          onChange={(e) => setEndDate(e.target.value)}
          className="border-gray-400 border-[1px] rounded-md p-1 w-1/4"
        />
        <label className="text-xl font-semibold">Price per day: </label>
        <input
          name="price_per_day"
          type="number"
          value={price_per_day}
          onChange={(e) => setPricePerDay(e.target.value)}
          className="border-gray-400 border-[1px] rounded-md p-1 w-1/4"
        />
        <button
          className="text-white border-2 border-blue-500 rounded-lg bg-blue-500 hover:bg-blue-900 pb-4 p-3 w-48"
          type="submit"
        >
          Update Listing
        </button>
      </form>
      {listing.userType !== "admin" && auth.user !== listing.idlisting && (
        <button
          className="text-white border-2 border-red-500 rounded-lg bg-red-500  hover:bg-red-900 pb-4 p-3 w-48"
          onClick={deleteListing}
        >
          Delete Listing
        </button>
      )}
    </div>
  );
}

export default ListingInfo;
