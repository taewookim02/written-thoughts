import styled from "styled-components";
import useLogout from "../hooks/useLogout";
import { useNavigate, Link } from "react-router-dom";
const Home = () => {
  const logout = useLogout();
  const navigate = useNavigate();

  const signOut = async () => {
    await logout();
    navigate("/");
  };
  return (
    <>
      {/* hero section */}
      {/* cta */}
      {/* features */}
      {/* cta */}
      {/* footer */}
      <div>
        <h1>Home</h1>
        <button onClick={signOut}>Sign out</button>
      </div>
    </>
  );
};

export default Home;
