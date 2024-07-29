import { Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Journal from "./pages/Journal";
import Home from "./pages/Home";
import Container from "./components/Container";
const App = () => {
  return (
    // TODO: add container
    // TODO: add navbar?
    <Container>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/journal" element={<Journal />} />
      </Routes>
    </Container>
  );
};

export default App;
