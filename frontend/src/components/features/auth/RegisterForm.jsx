import React, { useState } from "react";
import { StyledForm } from "../../common/StyledForm";
import InputField from "../../common/InputField";
import { StyledSubmitButton } from "../../common/StyledSubmitButton";

const RegisterForm = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleRegister = async (e) => {
    e.preventDefault();
    if (password !== confirmPassword) {
      alert("Password and confirm password do not match");
      return;
    }
    const url = "http://127.0.0.1:8080/api/v1/auth/register";
    const payload = {
      email,
      password,
      confirmPassword,
    };
    const response = await fetch(url, {
      method: "POST",
      mode: "cors",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    });
    const data = await response.json();
    // TODO: add data.access_token and data.refresh_token somewhere
    // TODO: handle status 409 (UserAlreadyExists)
  };

  return (
    <StyledForm onSubmit={handleRegister}>
      <h1>Register</h1>
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
      <InputField
        type="password"
        name="confirmPassword"
        placeholder="Confirm password"
        required={true}
        onChange={(e) => setConfirmPassword(e.target.value)}
      />
      <StyledSubmitButton type="submit">Register</StyledSubmitButton>
    </StyledForm>
  );
};

export default RegisterForm;
