import { Routes, Route } from "react-router-dom";
import WelcomePage from "./pages/WelcomePage";
import SignIn from "./pages/SignInPage";
import Register from "./pages/RegisterPage";
import Profile from "./pages/ProfilePage";

function App() {
  return (
    <div>
      <Routes>
        <Route path="/" element={<WelcomePage />} />
        <Route path="/register" element={<Register />} />
        <Route path="/signin" element={<SignIn />} />

      </Routes>
    </div>
  );
}

export default App;
