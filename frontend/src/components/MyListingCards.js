import React from "react";
import axios from "axios";
import { useEffect, useState } from "react";
import { useAuth } from "../Utils/AuthContext";
import { Link } from "react-router-dom";

function ListingCards() {
  const auth = useAuth();
  const [listings, setListings] = useState([]);

  useEffect(() => {
    axios
      .get("http://localhost:8000/user/listing/" + auth.user, {
        withCredentials: true,
      })
      .then((response) => {
        setListings(response.data.listings);
        console.log(response.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [auth.user]);

  return (
    <div className="flex flex-col flex-wrap gap-1 p-24 pt-5">
      {listings.length > 0 &&
        listings.map((listing) => (
          <Link
            to={"/listings/" + listing.idlistings}
            className="hover:cursor-pointer"
            key={listing.idlistings}
          >
            <div className="flex flex-col border p-4 h-40 w-[64rem] rounded-md">
              <div className="flex flex-row gap-2 items-center">
                <span className="text-xl font-bold">{listing.type}</span>
                <span className="text-lg font-semibold ml-auto">
                  {listing.address}, {listing.postal_code}, {listing.city},{" "}
                  {listing.country}
                </span>
              </div>
              <div className="flex flex-row gap-10 mt-auto text-lg">
                <p>Start Date: {listing.start_date}</p>
                <p>End Date: {listing.end_date}</p>
                <div className="ml-auto">
                  <p>Price /day: ${listing.price_per_day}</p>
                </div>
              </div>
            </div>
          </Link>
        ))}
    </div>
  );
}

export default ListingCards;
