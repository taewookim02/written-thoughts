import { Routes, Route } from "react-router-dom";
import Login from "./pages/LoginMain";
import Signup from "./pages/SignupMain";
import Journal from "./pages/JournalMain";
import Home from "./pages/HomeMain";
import Container from "./components/Container";
const App = () => {
  return (
    // <Container>
    <Routes>
      <Route path="/" element={<Home />} />
      <Route element={<Container />}>
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/journal" element={<Journal />} />
      </Route>
    </Routes>
    // </Container>
  );
};

export default App;
