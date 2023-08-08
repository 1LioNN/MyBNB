import React from "react";
import { useNavigate } from "react-router-dom";

function FilteredListings(props) {
  const navigate = useNavigate();
  const { listings } = props.listings;
  console.log(listings);


  const navigateToBooking = (id) => {
    navigate("/browse/" + id);
  };

  return (
    <div className="flex flex-col flex-wrap gap-1 p-24 pt-5">
      {listings &&
        listings.map((listing) => (
          <div
            className="flex flex-col border p-4 h-40 w-[64rem] rounded-md"
            key={listing.idlisting}
          >
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
              <p>Price /day: ${listing.price_per_day}</p>
              <div className="ml-auto">
                <button
                  className="text-white bg-cyan-600 rounded-lg p-2 ml-auto mr-10 pl-5 pr-5 hover:bg-cyan-800"
                  onClick={() => navigateToBooking(listing.idlistings) }
                >
                  <span className="text-lg font-semibold ml-auto">
                    Book Listing
                  </span>
                </button>
              </div>
            </div>
          </div>
        ))}
    </div>
  );
}

export default FilteredListings;
