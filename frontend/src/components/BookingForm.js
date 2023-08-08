import React from "react";
import { useState, useEffect } from "react";
import axios from "axios";
import { useAuth } from "../Utils/AuthContext";
import { useNavigate } from "react-router-dom";
import moment from "moment";

function getRentPeriod(startDate, endDate) {
  if (
    !moment(startDate, "YYYY-MM-DD", true).isValid() ||
    !moment(endDate, "YYYY-MM-DD", true).isValid()
  ) {
    return 1;
  }
  const result =
    Math.round(
      (new Date(endDate) - new Date(startDate)) / (1000 * 60 * 60 * 24)
    ) + 1;
  if (result < 0) {
    return 0;
  }
  return result;
}

function BookingForm(props) {
  const navigate = useNavigate();
  const listing = props.listing;
  const today = new Date();
  const auth = useAuth();
  const [start_date, setStartDate] = useState("");
  const [end_date, setEndDate] = useState("");
  const [price_per_day, setPricePerDay] = useState(0.0);

  const createBooking = (e) => {
    e.preventDefault();
    const data = {
      start_date: start_date,
      end_date: end_date,
      idlisting: listing.idlisting,
    };
    axios
      .post("http://localhost:8000/booking", data, {
        withCredentials: true,
      })
      .then((response) => {
        console.log(response);
        navigate("/bookings");
      })
      .catch((err) => {
        console.log(err);
      });
  };

  useEffect(() => {
    setStartDate(listing.start_date);
    setEndDate(listing.end_date);
    setPricePerDay(listing.price_per_day);
  }, [listing]);

  return (
    <div className="flex flex-row w-2/3">
      <div className="flex flex-col ml-20 pt-1 p-24 gap-7  mt-7">
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
        <div className="text-xl">
          <span className="font-semibold">Price Per Day: </span>{" "}
          {listing.price_per_day}
        </div>
        <div className="text-xl">
          <span className="font-semibold">Start Date </span>{" "}
          {listing.start_date}
        </div>
        <div className="text-xl">
          <span className="font-semibold">End Date </span> {listing.end_date}
        </div>
      </div>
      <div className="flex flex-col ml-20 pt-1 p-24 gap-7  mt-7">
        <div className="font-bold text-2xl">Booking Details </div>
        <form className="flex flex-col gap-2" onSubmit={createBooking}>
          <label className="text-xl font-semibold">Start Date: </label>
          <input
            name="start_date"
            type="date"
            value={start_date}
            onChange={(e) => setStartDate(e.target.value)}
            className="border-gray-400 border-[1px] rounded-md p-1 "
          />
          <label className="text-xl font-semibold">End Date: </label>
          <input
            name="end_date"
            type="date"
            value={end_date}
            onChange={(e) => setEndDate(e.target.value)}
            className="border-gray-400 border-[1px] rounded-md p-1 "
          />
          <label className="text-xl font-semibold">Total Cost: </label>
          <div className="text-cyan-600 font-bold text-xl">
            {" "}
            ${price_per_day * getRentPeriod(start_date, end_date)}
          </div>
          <button
            className="text-white border-2 border-blue-500 rounded-lg bg-blue-500 hover:bg-blue-900 pb-4 p-3 w-48"
            type="submit"
          >
            Confirm Booking
          </button>
        </form>
      </div>
    </div>
  );
}

export default BookingForm;
