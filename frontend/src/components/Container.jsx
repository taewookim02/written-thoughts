import React from "react";
import styled from "styled-components";
import Sidebar from "./Sidebar";
import MainContent from "./MainContent";

const GridContainer = styled.div`
  display: grid;
  grid-template-columns: 80px 1fr;
  min-height: 100vh;
`;

const Container = ({ children }) => {
  return (
    <GridContainer>
      <Sidebar />
      <MainContent>{children}</MainContent>
    </GridContainer>
  );
};

export default Container;
