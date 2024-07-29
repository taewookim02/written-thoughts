import React from "react";
import InputField from "../../common/InputField";
import { StyledForm } from "../../common/StyledForm";
import { StyledSubmitButton } from "../../common/StyledSubmitButton";

const handleLogin = (e) => {
  e.preventDefault();
  console.log("hello");
};

const LoginForm = () => {
  return (
    <StyledForm onSubmit={handleLogin}>
      <h1>Login</h1>
      <InputField
        type="email"
        name="email"
        placeholder="Email"
        required={true}
      />
      <InputField
        type="password"
        name="password"
        placeholder="Password"
        required={true}
      />
      <StyledSubmitButton type="submit">Login</StyledSubmitButton>
    </StyledForm>
  );
};

export default LoginForm;
