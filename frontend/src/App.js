import { Routes, Route } from "react-router-dom";
import { AuthProvider } from "./Utils/AuthContext";
import WelcomePage from "./pages/WelcomePage";
import SignIn from "./pages/SignInPage";
import Register from "./pages/RegisterPage";
import Profile from "./pages/ProfilePage";

function App() {
  return (
    <AuthProvider>
      <div>
        <Routes>
          <Route path="/" element={<WelcomePage />} />
          <Route path="/register" element={<Register />} />
          <Route path="/signin" element={<SignIn />} />
          <Route path="/profile" element={<Profile />} />
        </Routes>
      </div>
    </AuthProvider>
  );
}

export default App;
