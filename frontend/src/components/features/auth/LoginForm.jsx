import React from "react";
import InputField from "../../common/InputField";
import { StyledForm } from "../../common/StyledForm";
import { StyledSubmitButton } from "../../common/StyledSubmitButton";

const LoginForm = () => {
  return (
    <StyledForm>
      <h1>Login</h1>
      <InputField type="email" name="email" placeholder="Email" />
      <InputField type="password" name="password" placeholder="Password" />
      <StyledSubmitButton type="submit">Login</StyledSubmitButton>
    </StyledForm>
  );
};

export default LoginForm;
