import styled from "styled-components";

export const StyledForm = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: 90%;
  max-width: 512px;
  margin: 2rem auto;
  padding: clamp(20px, 5%, 60px);
  border: 1px solid black;
  border-radius: 8px;
  /* cant i just margin-top: 100px here? what else  */

  @media (max-width: 768px) {
    width: 95%;
  }
`;
