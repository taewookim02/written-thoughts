import React from "react";
import { Outlet } from "react-router-dom";
import styled from "styled-components";
import GuestNavbar from "./GuestNavbar";
const MainContent = styled.div`
  width: 100%;
  margin: 0 auto;
  max-width: 880px;
  min-height: 100vh;
  display: grid;
  grid-template-rows: min-content 1fr;
  align-items: center;
`;
const GuestContainer = () => {
  return (
    <MainContent>
      <GuestNavbar />
      <Outlet />
    </MainContent>
  );
};

export default GuestContainer;
