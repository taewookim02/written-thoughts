import React from "react";
import { StyledForm } from "../../common/StyledForm";
import InputField from "../../common/InputField";
import { StyledSubmitButton } from "../../common/StyledSubmitButton";

const RegisterForm = () => {
  return (
    <StyledForm>
      <h1>Register</h1>
      <InputField type="email" name="email" placeholder="Email" />
      <InputField type="password" name="password" placeholder="Password" />
      <InputField
        type="password"
        name="confirmPassword"
        placeholder="Confirm password"
      />
      <StyledSubmitButton type="submit">Register</StyledSubmitButton>
    </StyledForm>
  );
};

export default RegisterForm;
