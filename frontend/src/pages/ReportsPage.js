import React from "react";
import NavBar from "../components/NavBar";
import ReportsFilter from "../components/ReportsFilter";

function ReportsPage() {
  return (
    <div className="flex flex-col">
      <NavBar />
      <ReportsFilter />
    </div>
  );
}

export default ReportsPage;