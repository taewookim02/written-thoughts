import { Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Home from "./pages/Home";
import UserContainer from "./components/layouts/UserContainer";
import GuestContainer from "./components/layouts/GuestContainer";
import Register from "./pages/Register";
import Entry from "./pages/Entry";
import Error from "./pages/Error";
import RequireAuth from "./components/features/auth/RequireAuth";
import PersistLogin from "./components/features/auth/PersistLogin";

const App = () => {
  return (
    <Routes>
      {/* public routes */}
      <Route element={<GuestContainer />}>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
      </Route>

      {/* private routes */}
      <Route element={<PersistLogin />}>
        <Route element={<RequireAuth />}>
          <Route element={<UserContainer />}>
            <Route path="/entries" element={<Entry />} />
          </Route>
        </Route>
      </Route>

      {/* Catch all */}
      <Route path="*" element={<Error />} />
    </Routes>
  );
};

export default App;
