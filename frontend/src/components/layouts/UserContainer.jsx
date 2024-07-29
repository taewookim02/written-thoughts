import React from "react";
import styled from "styled-components";
import Sidebar from "./Sidebar";
import MainContent from "./MainContent";
import { Outlet } from "react-router-dom";

const GridContainer = styled.div`
  display: grid;
  grid-template-columns: 80px 1fr;
  min-height: 100vh;
`;

const UserContainer = () => {
  return (
    <GridContainer>
      <Sidebar />
      <MainContent>
        <Outlet />
      </MainContent>
    </GridContainer>
  );
};

export default UserContainer;
