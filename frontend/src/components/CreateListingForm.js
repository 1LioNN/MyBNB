import React from "react";
import { useState, useEffect } from "react";
import { useAuth } from "../Utils/AuthContext";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function estimatedIncome(price, amenitiesList, amenities) {
  const amenitiesMatched = amenities.filter((amenity) =>
    amenitiesList.includes(amenity)
  ).length;

  // Calculate the percent income based on the number of amenities matched, capped at 15%
  const incomePercent = Math.min(amenitiesMatched * 3, 15);

  // Calculate the estimated income
  const estimatedIncome = price * (1 + incomePercent / 100);

  return estimatedIncome.toFixed(2);
}

function CreateListingForm() {
  const auth = useAuth();
  const navigate = useNavigate();
  const [type, setType] = useState("");
  const [address, setAddress] = useState("");
  const [longi, setLongitude] = useState("");
  const [lat, setLatitude] = useState("");
  const [postal_code, setPostalCode] = useState("");
  const [city, setCity] = useState("");
  const [country, setCountry] = useState("CA");
  const [price, setPrice] = useState("");
  const [start_date, setStartDate] = useState("");
  const [end_date, setEndDate] = useState("");
  const [amenities, setAmenities] = useState([]);
  const [amenitiesList, setAmenitiesList] = useState([]);
  const [amenitiesListNames, setAmenitiesListNames] = useState([]);
  const [averagePrice, setAveragePrice] = useState(0);

  const changeAmenities = (e) => {
    const value = parseInt(e.target.value, 10);
    setAmenities((prev) =>
      e.target.checked ? [...prev, value] : prev.filter((x) => x !== value)
    );
  };

  const submitHandler = (e) => {
    e.preventDefault();
    const formData = {
      type,
      address,
      longi: parseFloat(longi),
      lat: parseFloat(lat),
      postal_code,
      city,
      country,
      price_per_day: parseFloat(price),
      start_date,
      end_date,
      amenities,
    };
    console.log(formData);
    axios
      .post("http://localhost:8000/listing/", formData, {
        withCredentials: true,
      })
      .then((res) => {
        console.log(res);
        navigate("/listings");
      });
    // You can handle the form submission logic here
  };
  const a = 0;
  console.log(a.toFixed(2));
  useEffect(() => {
    axios
      .get("http://localhost:8000/price?country=" + country, {
        withCredentials: true,
      })
      .then((res) => {
        console.log(res.data);
        setAveragePrice(res.data.average_price.toFixed(2));
        const top5 = res.data.top_amenities.map(
          (amenity) => amenity.idamenities
        );
        setAmenitiesList(top5);
        const top5Names = res.data.top_amenities.map((amenity) => amenity.name);
        setAmenitiesListNames(top5Names);
      });
  }, [country]);
  const estimatedIncomeValue = estimatedIncome(
    averagePrice,
    amenitiesList,
    amenities
  );

  return (
    <div className="flex flex-row justify-center mt-5">
      <div className="flex flex-col">
        <div className="flex flex-row text-3xl font-bold mb-4 w-[22rem]">
          <div>Create a Listing</div>
        </div>
        <div>
          <form className="flex flex-row gap-10" onSubmit={submitHandler}>
            <div className="flex flex-col gap-5">
              <input
                className="border-gray-400 border-[1px] rounded-md p-1"
                type="text"
                placeholder="Type"
                value={type}
                onChange={(e) => setType(e.target.value)}
              />
              <input
                className="border-gray-400 border-[1px] rounded-md p-1"
                type="text"
                placeholder="Address"
                value={address}
                onChange={(e) => setAddress(e.target.value)}
              />
              <input
                className="border-gray-400 border-[1px] rounded-md p-1"
                type="number"
                step="any"
                placeholder="longi"
                value={longi}
                onChange={(e) => setLongitude(e.target.value)}
              />
              <input
                className="border-gray-400 border-[1px] rounded-md p-1"
                type="number"
                step="any"
                placeholder="lat"
                value={lat}
                onChange={(e) => setLatitude(e.target.value)}
              />
              <input
                className="border-gray-400 border-[1px] rounded-md p-1"
                type="text"
                maxLength={6}
                placeholder="City"
                value={city}
                onChange={(e) => setCity(e.target.value)}
              />

              <input
                className="border-gray-400 border-[1px] rounded-md p-1"
                type="text"
                maxLength={6}
                placeholder="Postal Code"
                value={postal_code}
                onChange={(e) => setPostalCode(e.target.value)}
              />
              <label className="text-base">Country</label>
              <select
                type="text"
                className="border-[1px] border-gray-400 text-base p-2 rounded-md"
                name="country"
                required
                onChange={(e) => setCountry(e.target.value)}
              >
                <option value="CA">CA</option>
                <option value="US">US</option>
                <option value="UK">UK</option>
                <option value="FR">FR</option>
                <option value="IT">IT</option>
                <option value="ES">ES</option>
                <option value="DE">DE</option>
                <option value="CN">CN</option>
                <option value="MX">MX</option>
                <option value="BR">BR</option>
              </select>
              <div className="flex flex-col gap-2">
                <div className="font-bold">
                  Average Listing Prices in Your Country: {" $" + averagePrice}
                </div>
                <div className="font-bold">
                  Top 5 Amenities included by other Hosts{" "}
                  {"(+3% income per desireable amenity, up to 15%) "}:
                </div>
                {amenitiesListNames.map((amenity) => (
                  <div>{amenity}</div>
                ))}
                <div className="font-bold">
                  Suggested Price:{" $" + estimatedIncomeValue}
                </div>
              </div>
            </div>
            <div className="flex flex-col gap-5">
              <input
                className="border-gray-400 border-[1px] rounded-md p-1"
                type="date"
                placeholder="Start Date"
                value={start_date}
                onChange={(e) => setStartDate(e.target.value)}
              />
              <input
                className="border-gray-400 border-[1px] rounded-md p-1"
                type="date"
                placeholder="End Date"
                value={end_date}
                onChange={(e) => setEndDate(e.target.value)}
              />

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
              <input
                className="border-gray-400 border-[1px] rounded-md p-1"
                type="number"
                step="any"
                placeholder="Price"
                value={price}
                onChange={(e) => setPrice(e.target.value)}
              />

              <button
                type="submit"
                className="bg-cyan-600 h-12 text-white rounded-md p-1"
              >
                Create Listing
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default CreateListingForm;
