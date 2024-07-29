import React from "react";
import styled from "styled-components";

const MyH1 = styled.h1`
  color: white;
  background-color: red;
`;

const Home = () => {
  return (
    <>
      <MyH1>Hello world</MyH1>
    </>
  );
};

export default Home;
