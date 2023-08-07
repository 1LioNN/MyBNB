import React from "react";
import { useNavigate } from "react-router-dom";

function NavBar() {
  const navigate = useNavigate();

  const clickHandler = () => {
    console.log("clicked");
    navigate("/");
  };

  return (
    <div className="flex flex-row  h-24 w-fullfont-semibold items-center  pb-1 bg-cyan-600 text-white font-extrabold pl-5 ">
      <div onClick={() => clickHandler()} className="hover:cursor-pointer text-xl">MyBNB</div>
    </div>
  );
}

export default NavBar;
