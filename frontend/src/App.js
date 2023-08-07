import { Routes, Route } from "react-router-dom";
import { AuthProvider } from "./Utils/AuthContext";
import WelcomePage from "./pages/WelcomePage";
import SignIn from "./pages/SignInPage";
import Register from "./pages/RegisterPage";
import Profile from "./pages/ProfilePage";
import MyListingsPage from "./pages/MyListingsPage";
import BrowseListingsPage from "./pages/BrowseListingsPage";
import CreateListingPage from "./pages/CreateListingPage";
import BookListingPage from "./pages/BookListingPage";
import MyBookingsPage from "./pages/MyBookingsPage";
import ReportsPage from "./pages/ReportsPage";
import ViewListingPage from "./pages/ViewListingPage";

function App() {
  return (
    <AuthProvider>
      <div>
        <Routes>
          <Route path="/" element={<WelcomePage />} />
          <Route path="/register" element={<Register />} />
          <Route path="/signin" element={<SignIn />} />
          <Route path="/profile/:uid" element={<Profile />} />
          <Route path="/listings" element={<MyListingsPage />} />
          <Route path="/listings/:lid" element={<ViewListingPage />} />
          <Route path="/create" element={<CreateListingPage />} />
          <Route path="/browse" element={<BrowseListingsPage />} />
          <Route path="/browse/:lid" element={<BookListingPage />} />
          <Route path="/bookings" element={<MyBookingsPage />} />
          <Route path="/reports" element={<ReportsPage />} />
        </Routes>
      </div>
    </AuthProvider>
  );
}

export default App;
