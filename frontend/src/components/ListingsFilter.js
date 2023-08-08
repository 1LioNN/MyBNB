import axios from "axios";
import React from "react";
import { useState } from "react";
import { useAuth } from "../Utils/AuthContext";
import FilteredListings from "./FilteredListings";

function ListingsFilter() {
  const [filterType, setFilterType] = useState("1");
  const [data, setData] = useState([]);
  const [longi, setLongi] = useState(0);
  const [lat, setLat] = useState(0);
  const [start_date, setStartDate] = useState("");
  const [end_date, setEndDate] = useState("");
  const [distance, setDistance] = useState(5);
  const [address, setAddress] = useState("");
  const [postal_code, setPostalCode] = useState("");
  const [min_price, setMinPrice] = useState(0);
  const [max_price, setMaxPrice] = useState(9000);
  const [isOrder, setIsOrder] = useState(true);
  const [amenities, setAmenities] = useState([]);
  const [orderByPrice, setOrderByPrice] = useState("ASC");

  const auth = useAuth();

  const changeType = (e) => {
    setFilterType(e.target.value);
    //Clear current table
    setData([]);
  };
  console.log(orderByPrice);

  const changeAmenities = (e) => {
    const value = parseInt(e.target.value, 10);
    setAmenities((prev) =>
      e.target.checked ? [...prev, value] : prev.filter((x) => x !== value)
    );
  };

  const changeOrder = (e) => {
    console.log(e.target.value);
    if (e.target.value === "NONE") {
      setIsOrder(false);
    } else {
      setIsOrder(true);
      setOrderByPrice(e.target.value);
    }
  };
  console.log(amenities);
  const filterListings = (e) => {
    e.preventDefault();
    // Set form data for each report type
    let query = "http://localhost:8000/listing/filter?";

    const queryString = [];

    if (filterType === "1") {
      queryString.push("longitude=" + longi);
      queryString.push("latitude=" + lat);
      queryString.push("distance=" + distance);
    } else if (filterType === "2") {
      queryString.push("address=" + address);
    } else if (filterType === "3") {
      queryString.push("postal_code=" + postal_code);
    }

    if (start_date !== "") {
      queryString.push("start_date=" + start_date);
    }
    if (end_date !== "") {
      queryString.push("end_date=" + end_date);
    }
    if (min_price !== "") {
      queryString.push("min_price=" + min_price);
    }
    if (max_price !== "") {
      queryString.push("max_price=" + max_price);
    }
    if (isOrder) {
      queryString.push("orderByPrice=" + orderByPrice);
    }
    if (amenities.length !== 0) {
      queryString.push("amenities=" + amenities);
    }
    console.log(query + queryString.join("&"));

    // Send request to backend
    axios.get(query + queryString.join("&"), {}).then((res) => {
      console.log(res.data);
      setData(res.data);
    });
  };

  return ( 
    <div className="flex flex-col ml-auto mr-auto">
      <h1 className="p-5 text-xl">Select Filter Type </h1>
      <form className="pl-5 text-lg" onSubmit={filterListings}>
        <select
          className="p-2 rounded-md border-[1px] border-black"
          onChange={changeType}
        >
          <option value="1">Longitude and Latitude</option>
          <option value="2">Address</option>
          <option value="3">Postal Code</option>
          <option value="4">None</option>
        </select>

        <div className="flex flex-row mt-2 gap-7 flex-wrap">
          {filterType === "1" && (
            <div className="flex flex-row mt-2 gap-2">
              <div className="flex flex-col">
                <label className="text-base">Longitude</label>
                <input
                  type="number"
                  step="any"
                  className="border-[1px] border-black text-base p-2 rounded-lg"
                  name="longi"
                  required
                  onChange={(e) => setLongi(e.target.value)}
                />
              </div>
              <div className="flex flex-col">
                <label className="text-base">Latitude</label>
                <input
                  type="number"
                  step="any"
                  className="border-[1px] border-black text-base p-2 rounded-lg"
                  name="lat"
                  onChange={(e) => setLat(e.target.value)}
                />
              </div>
              <div className="flex flex-col">
                <label className="text-base">Distance</label>
                <input
                  type="number"
                  step="any"
                  className="border-[1px] border-black text-base p-2 rounded-lg"
                  name="endDate"
                  onChange={(e) => setDistance(e.target.value)}
                />
              </div>
            </div>
          )}
          {filterType === "2" && (
            <div className="flex flex-col mt-2">
              <label className="text-base">Postal Code</label>
              <input
                type="text"
                className="border-[1px] border-black text-base p-2 rounded-lg"
                name="postal_code"
                required
                onChange={(e) => setPostalCode(e.target.value)}
              />
            </div>
          )}
          {filterType === "3" && (
            <div className="flex flex-col mt-2">
              <label className="text-base">Address</label>
              <input
                type="text"
                className="border-[1px] border-black text-base p-2 rounded-lg"
                name="address"
                required
                onChange={(e) => setAddress(e.target.value)}
              />
            </div>
          )}
          <div className="flex flex-row mt-2 gap-2">
            <div className="flex flex-col">
              <label className="text-base">Start Date</label>
              <input
                type="date"
                className="border-[1px] border-black text-base p-2 rounded-lg"
                name="startDate"
                onChange={(e) => setStartDate(e.target.value)}
              />
            </div>
            <div className="flex flex-col">
              <label className="text-base">End Date</label>
              <input
                type="date"
                className="border-[1px] border-black text-base p-2 rounded-lg"
                name="endDate"
                onChange={(e) => setEndDate(e.target.value)}
              />
            </div>
          </div>
          <div className="flex flex-row mt-2 gap-2">
            <div className="flex flex-col">
              <label className="text-base">Min Price ($)</label>
              <input
                type="number"
                className="border-[1px] border-black text-base p-2 rounded-lg"
                name="min_price"
                onChange={(e) => setMinPrice(e.target.value)}
              />
            </div>
            <div className="flex flex-col">
              <label className="text-base">Max Price ($)</label>
              <input
                type="number"
                className="border-[1px] border-black text-base p-2 rounded-lg"
                name="max_price"
                onChange={(e) => setMaxPrice(e.target.value)}
              />
            </div>
          </div>

          <div className="flex flex-col mt-2">
            <label className="text-base">Order By Price</label>
            <select
              type="text"
              className="border-[1px] border-black text-base p-2 rounded-md"
              name="orderByPrice"
              onChange={changeOrder}
            >
              <option value="ASC">ASC</option>
              <option value="DESC">DESC</option>
              <option value="NONE">NONE</option>
            </select>
          </div>
        </div>
        <label className="font-bold mr-4">Amenities:</label>
        <label className="font-semi-bold"> Essentials</label>
        <div className=" flex flex-wrap w-96 gap-2 justify-between">
          <label className="font-medium">
            <input
              type="checkbox"
              value={1}
              checked={amenities.includes(1)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Wifi</span>
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={2}
              checked={amenities.includes(2)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Washer</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={3}
              checked={amenities.includes(3)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Air conditioning</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={4}
              checked={amenities.includes(4)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Dedicated workspace</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={5}
              checked={amenities.includes(5)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Hair dryer</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={6}
              checked={amenities.includes(6)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Kitchen</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={7}
              checked={amenities.includes(7)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Dryer</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={8}
              checked={amenities.includes(8)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Heating</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={9}
              checked={amenities.includes(9)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">TV</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={10}
              checked={amenities.includes(10)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Iron</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
        </div>
        <label className="font-semi-bold"> Features</label>
        <div className=" flex flex-wrap w-96 gap-2 justify-between">
          <label className="font-medium">
            <input
              type="checkbox"
              value={11}
              checked={amenities.includes(11)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Pool</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={12}
              checked={amenities.includes(12)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Free parking</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={13}
              checked={amenities.includes(13)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Crib</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={14}
              checked={amenities.includes(14)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">BBQ grill</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={15}
              checked={amenities.includes(15)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Indoor fireplace</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={16}
              checked={amenities.includes(16)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Hot tub</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={17}
              checked={amenities.includes(17)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">EV Charger</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={18}
              checked={amenities.includes(18)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Gym</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={19}
              checked={amenities.includes(19)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Breakfast</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={20}
              checked={amenities.includes(20)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Smoking allowed</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
        </div>
        <label className="font-semi-bold"> Location </label>
        <div className=" flex flex-wrap w-96 gap-2 justify-between">
          <label className="font-medium">
            <input
              type="checkbox"
              value={21}
              checked={amenities.includes(21)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Beachfront</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={22}
              checked={amenities.includes(22)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Waterfront</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={23}
              checked={amenities.includes(23)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Ski-in/ski-out</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
        </div>
        <label className="font-semi-bold"> Safety </label>
        <div className=" flex flex-wrap w-96 gap-2 justify-between">
          <label className="font-medium">
            <input
              type="checkbox"
              value={24}
              checked={amenities.includes(24)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Smoke alarm</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
          <label className="font-medium">
            <input
              type="checkbox"
              value={25}
              checked={amenities.includes(25)}
              onChange={changeAmenities}
              className="form-checkbox h-5 w-5 text-cyan-600"
            />
            <span className="ml-2">Carbon monoxide alarm</span>
            {/* Add more amenities checkboxes as needed */}
          </label>
        </div>

        <button
          className="p-2 mt-2 bg-blue-500 rounded-lg text-white text-base"
          type="submit"
        >
          Filter Listings
        </button>
      </form>
      <FilteredListings listings={data} />
    </div>
  );
}

export default ListingsFilter;
