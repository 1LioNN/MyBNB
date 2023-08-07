import axios from "axios";
import React from "react";
import { useState } from "react";
import { useAuth } from "../Utils/AuthContext";
import Report from "./Report";

function ReportsFilter() {
  const [reportType, setReportType] = useState("1");
  const [country, setCountry] = useState("CA");
  const [city, setCity] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [postalCode, setPostalCode] = useState("");
  const [year, setYear] = useState("");
  const [data, setData] = useState([]);
  const auth = useAuth();
  console.log(auth.user);

  const changeType = (e) => {
    setReportType(e.target.value);
    console.log(reportType);
  };

  const generateReport = (e) => {
    e.preventDefault();
    // Set form data for each report type
    let query = "http://localhost:8000/report/";
    if (reportType === "1") {
      query += "1";
      query += "?start_date=" + startDate;
      query += "&end_date=" + endDate;
      query += "&city=" + city;
      console.log(query);
    } else if (reportType === "2") {
      query += "2";
      query += "?start_date=" + startDate;
      query += "&end_date=" + endDate;
      query += "&postal_code=" + postalCode;
      console.log(query);
    } else if (reportType === "3") {
      query += "3";
      query += "?country=" + country;
      console.log(query);
    } else if (reportType === "4") {
      query += "4";
      query += "?city=" + city;
      query += "&country=" + country;
      console.log(query);
    } else if (reportType === "5") {
      query += "5";
      query += "?city=" + city;
      query += "&country=" + country;
      query += "&postal_code=" + postalCode;
      console.log(query);
    } else if (reportType === "6") {
      query += "6";
      query += "?country=" + country;
      console.log(query);
    } else if (reportType === "7") {
      query += "7";
      query += "?city=" + city;
      console.log(query);
    } else if (reportType === "8") {
      query += "8";
      query += "?country=" + country;
      console.log(query);
    } else if (reportType === "9") {
      query += "9";
      query += "?city=" + city;
      console.log(query);
    } else if (reportType === "10") {
      query += "10";
      query += "?start_date=" + startDate;
      query += "&end_date=" + endDate;
      console.log(query);
    } else if (reportType === "11") {
      query += "11";
      query += "?start_date=" + startDate;
      query += "&end_date=" + endDate;
      query += "&city=" + city;
      console.log(query);
    } else if (reportType === "12") {
      query += "12";
      query += "?year=" + year;
      console.log(query);
    } else if (reportType === "13") {
      query += "13";
      query += "?year=" + year;
      console.log(query);
    } else if (reportType === "14") {
      query += "14";
      console.log(query);
    } else {
      console.log("Invalid report type");
    }

    // Send request to backend
    axios
      .get(query, { withCredentials: true })
      .then((res) => {
        console.log(res.data);
        setData(res.data.results);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <div className="flex flex-col">
      <h1 className="p-5 text-xl">Select Report Type</h1>
      <form className="pl-5 text-lg" onSubmit={generateReport}>
        <select
          className="p-2 rounded-md border-[1px] border-black"
          onChange={changeType}
        >
          <option value="1">Report 1</option>
          <option value="2">Report 2</option>
          <option value="3">Report 3</option>
          <option value="4">Report 4</option>
          <option value="5">Report 5</option>
          <option value="6">Report 6</option>
          <option value="7">Report 7</option>
          <option value="8">Report 8</option>
          <option value="9">Report 9</option>
          <option value="10">Report 10</option>
          <option value="11">Report 11</option>
          <option value="12">Report 12</option>
          <option value="13">Report 13</option>
          <option value="14">Report 14</option>
        </select>
        <div className="flex flex-row mt-2 gap-10">
          {(reportType === "1" ||
            reportType === "2" ||
            reportType === "10" ||
            reportType === "11") && (
            <div className="flex flex-row mt-2 gap-2">
              <div className="flex flex-col">
                <label className="text-base">Start Date</label>
                <input
                  type="date"
                  className="border-[1px] border-black text-base p-2 rounded-lg"
                  name="startDate"
                  required
                  onChange={(e) => setStartDate(e.target.value)}
                />
              </div>
              <div className="flex flex-col">
                <label className="text-base">End Date</label>
                <input
                  type="date"
                  className="border-[1px] border-black text-base p-2 rounded-lg"
                  name="startDate"
                  required
                  onChange={(e) => setEndDate(e.target.value)}
                />
              </div>
            </div>
          )}
          {(reportType === "1" ||
            reportType === "4" ||
            reportType === "5" ||
            reportType === "7" ||
            reportType === "9" ||
            reportType === "11") && (
            <div className="flex flex-col mt-2">
              <label className="text-base">City</label>
              <input
                type="text"
                className="border-[1px] border-black text-base p-2 rounded-lg"
                name="city"
                required
                onChange={(e) => setCity(e.target.value)}
              />
            </div>
          )}
          {(reportType === "3" ||
            reportType === "4" ||
            reportType === "5" ||
            reportType === "6" ||
            reportType === "8") && (
            <div className="flex flex-col mt-2">
              <label className="text-base">Country</label>
              <select
                type="text"
                className="border-[1px] border-black text-base p-2 rounded-md"
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
            </div>
          )}
          {(reportType === "2" || reportType === "5") && (
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
          {(reportType === "12" || reportType === "13") && (
            <div className="flex flex-col mt-2">
              <label className="text-base">Year</label>
              <input
                type="text"
                numeric
                maxLength={4}
                className="border-[1px] border-black text-base p-2 rounded-lg"
                name="year"
                required
                onChange={(e) => setYear(e.target.value)}
              />
            </div>
          )}
        </div>
        <button
          className="p-2 mt-2 bg-blue-500 rounded-lg text-white text-base"
          type="submit"
        >
          Generate Report
        </button>
      </form>
      < Report data={data} />
    </div>
  );
}

export default ReportsFilter;
