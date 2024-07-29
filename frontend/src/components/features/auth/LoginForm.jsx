import React, { useState } from "react";
import InputField from "../../common/InputField";
import { StyledForm } from "../../common/StyledForm";
import { StyledSubmitButton } from "../../common/StyledSubmitButton";

const LoginForm = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();

    const payload = {
      email,
      password,
    };

    const url = "http://127.0.0.1:8080/api/v1/auth/authenticate";
    const response = await fetch(url, {
      method: "POST",
      mode: "cors",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    });
    const data = await response.json();
    console.log(data);
    // TODO: handle successful login
    // TODO: add data.access_token and data.refresh_token somewhere
    // TODO: handle exceptions
  };

  return (
    <StyledForm onSubmit={handleLogin}>
      <h1>Login</h1>
      <InputField
        type="email"
        name="email"
        placeholder="Email"
        required={true}
        onChange={(e) => setEmail(e.target.value)}
      />
      <InputField
        type="password"
        name="password"
        placeholder="Password"
        required={true}
        onChange={(e) => setPassword(e.target.value)}
      />
      <StyledSubmitButton type="submit">Login</StyledSubmitButton>
    </StyledForm>
  );
};

export default LoginForm;
