import { createContext, useContext, useState } from "react";
import axios from "axios";
const AuthContext = createContext();

export function useAuth() {
  return useContext(AuthContext);
}

export function AuthProvider({ children }) {
  const storedUser = JSON.parse(sessionStorage.getItem("user"));
  let [user, setUser] = useState(storedUser || null);

  const signup = async (formData, callback) => {
    axios
      .post("http://localhost:8000/user/signup", formData, {
        withCredentials: true,
      })
      .then((response) => {
        const status = response.status;
        const resData = response.data;
        callback(status, resData);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  let login = async (formData, callback) => {
    axios
      .post("http://localhost:8000/user/signin", formData, {
        withCredentials: true,
      })
      .then((response) => {
        const status = response.status;
        const resData = response.data.user;
        if (status === 200) {
          setUser(resData);
          sessionStorage.setItem("user", JSON.stringify(resData));
        } else {
          setUser(null);
        }
        callback(status, resData);
      })
      .catch((err) => {
        console.log(err);
        alert("Invalid username or password");
      });
  };

  let logout = async (callback) => {
    axios
      .post("http://localhost:8000/user/signout", null, {
        withCredentials: true,
      })
      .then((response) => {
        const status = response.status;
        const resData = response.data;

        if (status === 200) {
          setUser(null);
          sessionStorage.removeItem("user");
        }
        callback(status, resData);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  let value = { user, login, signup, logout };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
