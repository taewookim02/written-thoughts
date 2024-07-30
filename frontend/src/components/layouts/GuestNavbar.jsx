import { Link } from "react-router-dom";
import styled from "styled-components";

const NavbarContainer = styled.div`
  display: flex;
  padding: 16px;
  gap: 24px;
  align-items: center;
  justify-content: end;

  @media (max-width: 768px) {
    gap: 16px;
  }
  @media (max-width: 392px) {
    gap: 8px;
  }
`;

const LogoLink = styled(Link)`
  margin-right: auto;
`;

const GuestNavbar = () => {
  return (
    <>
      <NavbarContainer>
        <LogoLink to="/">Logo</LogoLink>
        <Link to="/login">Login</Link>
        <Link to="/register">Register</Link>
        <Link to="/entries">Entries</Link>
      </NavbarContainer>
    </>
  );
};

export default GuestNavbar;
