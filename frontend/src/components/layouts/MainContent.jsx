import styled from "styled-components";

const MainContentContainer = styled.div`
  padding: 24px;
`;

const MainContent = ({ children }) => {
  return <MainContentContainer>{children}</MainContentContainer>;
};

export default MainContent; // Used in UserContainer.jsx
