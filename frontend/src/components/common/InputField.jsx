import React from "react";
import styled from "styled-components";

const StyledInput = styled.input`
  width: 100%;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid black;
  font-size: 16px;
`;

const InputField = ({ type, name, placeholder, required, onChange }) => {
  return (
    <StyledInput
      type={type}
      name={name}
      placeholder={placeholder}
      required={required}
      onChange={onChange}
    />
  );
};

export default InputField;
