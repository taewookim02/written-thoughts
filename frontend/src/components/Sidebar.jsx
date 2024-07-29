import React from "react";
import { Link } from "react-router-dom";
import styled from "styled-components";

const SidebarContainer = styled.nav`
  display: flex;
  flex-direction: column;
  padding: 8px;
  align-items: center;
  justify-content: center;
  gap: 24px;
  border-right: 1px solid black;
`;

const NavContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 24px;
  align-items: center;
`;

const Sidebar = () => {
  return (
    <SidebarContainer>
      {/* logo */}
      <NavContainer>
        <Link to="/entries">Entries</Link>
        <Link to="/entries/public">Public</Link>
        <Link to="/labels">Label</Link>
        <Link to="/lists">List</Link>
      </NavContainer>
      {/* profile */}
    </SidebarContainer>
  );
};

export default Sidebar;
