import React from "react";
import NavBar from "../components/NavBar";
import { useNavigate } from "react-router-dom";

function WelcomePage() {
    const navigate = useNavigate();

    const SignInClickHandler = () => {
        navigate("/signin");
    };

    const RegisterClickHandler = () => {    
        navigate("/register");
    };


    return (
        <div className="flex flex-col">
            <NavBar />
            <div className="flex flex-col justify-center w-screen p-48 gap-24">
                <div className="flex flex-row justify-center text-4xl font-bold">
                    Welcome to MyBNB!
                </div>
                <div className="flex flex-row justify-center text-2xl">
                    Please sign in or register to continue.
                </div>
                <div className="flex flex-row justify-center gap-32 font-bold text-2xl">
                    <button className="text-white border-2 border-cyan-600 rounded-lg bg-cyan-600 hover:text-cyan-600 hover:bg-white pb-4 p-3 w-32"
                    onClick={() => RegisterClickHandler()}>
                        Register
                    </button>
                    <button className="text-cyan-600 border-2 border-cyan-600 rounded-lg p-3 pb-4 w-32 hover:text-white  hover:bg-cyan-600"
                    onClick={() => SignInClickHandler()}>
                        Sign In
                    </button>
                </div>
            </div>
        </div>
    );
    }

export default WelcomePage;