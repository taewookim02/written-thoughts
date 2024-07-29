import { Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Home from "./pages/Home";
import UserContainer from "./components/layouts/UserContainer";
import GuestContainer from "./components/layouts/GuestContainer";
import Register from "./pages/Register";
import Entry from "./pages/Entry";
import Error from "./pages/Error";

const App = () => {
  return (
    // <Container>
    <Routes>
      {/* The pages that the guests can see */}
      <Route element={<GuestContainer />}>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/*" element={<Error />} />
      </Route>
      {/* The pages that the users can see */}
      <Route element={<UserContainer />}>
        <Route path="/entries" element={<Entry />} />
      </Route>
    </Routes>
    // </Container>
  );
};

export default App;
