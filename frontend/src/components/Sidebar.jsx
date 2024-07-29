import React from "react";
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
        <p>Entries</p>
        <p>Public</p>
        <p>Label</p>
        <p>List</p>
      </NavContainer>
      {/* profile */}
    </SidebarContainer>
  );
};

export default Sidebar;
