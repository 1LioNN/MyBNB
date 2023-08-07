import React from "react";
import NavBar from "../components/NavBar";
import Bookings from "../components/Bookings";

function MyBookingsPage() {
  return (
    <div className="flex flex-col gap-10">
      <NavBar />
      <Bookings/>
    </div>
  );
}

export default MyBookingsPage;