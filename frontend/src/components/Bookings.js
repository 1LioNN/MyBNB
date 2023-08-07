import React from "react";
import { useNavigate } from "react-router-dom";
import BookingCards from "./MyListingCards";
import { useEffect, useState } from "react";
import { useAuth } from "../Utils/AuthContext";
import axios from "axios";

function Listings() {
  const navigate = useNavigate();
  const auth = useAuth();
  const [bookings, setBookings] = useState([]);
  const [type, setType] = useState("");

  useEffect(() => {
    if (auth.user === null) {
      return;
    }
    axios
      .get("http://localhost:8000/user/bookings/" + auth.user, {
        withCredentials: true,
      })
      .then((response) => {
        setBookings(response.data.bookings);
        getUserType();
      })
      .catch((err) => {
        console.log(err);
      });
  }, [auth.user]);

  const getUserType = () => {
    axios
      .get("http://localhost:8000/user/" + auth.user, {
        withCredentials: true,
      })
      .then((response) => {
        setType(response.data.data.userType);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const updateStatus = (idbookings) => {
    let status = 0;
    if (type === "renter") {
      status = 2;
    } else if (type === "host") {
      status = 3;
    } else {
      return;
    }
    const form = {
      status: status,
    };
    axios
      .patch("http://localhost:8000/booking/" + idbookings, form, {
        withCredentials: true,
      })
      .then((response) => {
        setBookings(
          bookings.map((booking) => {
            if (booking.idbookings === idbookings) {
              booking.status = status;
            }
            return booking;
          })
        );
        console.log(response.data);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const getStatus = (status) => {
    if (status === 1) {
      return "Booked";
    } else if (status === 2) {
      return "Cancelled by Renter";
    } else if (status === 3) {
      return "Cancelled by Host";
    } else if (status === 4) {
      return "Attended";
    }
  };

  return (
    <div className="flex flex-col">
      <div className="flex flex-row p-5 gap-24 justify-center ">
        <div className="font-bold text-2xl">My Bookings</div>
      </div>
      <div className="flex flex-row justify-center">
        <div className="flex flex-col flex-wrap gap-1 p-24 pt-5">
          {bookings.map((booking) => (
            <div
              className="flex flex-col border p-4 h-40 w-[64rem] rounded-md"
              key={booking.idbookings}
            >
              <div className="flex flex-row gap-2 items-center">
                <span className="text-xl font-bold">
                  Booked Listing: {booking.idlisting}
                </span>
                <span className="text-lg font-semibold ml-auto">
                  Status: {getStatus(booking.status)}
                </span>
              </div>
              <div className="flex flex-row gap-10 mt-auto text-lg">
                <p>Start Date: {booking.start_date}</p>
                <p>End Date: {booking.end_date}</p>
                <p>Total Cost: ${booking.total_cost}</p>
                {booking.status === 1 && (
                  <div className="ml-auto">
                    <button
                      className="text-white bg-red-400 rounded-lg p-2 ml-auto mr-10 pl-5 pr-5 hover:bg-red-800"
                      onClick={() => updateStatus(booking.idbookings)}
                    >
                      <span className="text-lg font-semibold ml-auto">
                        Cancel Booking
                      </span>
                    </button>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Listings;
