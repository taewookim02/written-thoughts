import { useState } from "react";
import { StyledForm } from "../../common/StyledForm";
import InputField from "../../common/InputField";
import { StyledSubmitButton } from "../../common/StyledSubmitButton";
import axios from "../../../api/axios";

const REGISTER_URL = "/auth/register";
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
    const payload = {
      email,
      password,
      confirmPassword,
    };
    try {
      const response = await axios.post(REGISTER_URL, JSON.stringify(payload), {
        headers: { "Content-Type": "application/json" },
        withCredentials: true,
      });
      console.log(response.data);
      console.log(response.access_token);
      console.log(response);
      // TODO: add data.access_token and data.refresh_token somewhere
      // TODO: handle status 409 (UserAlreadyExists)
    } catch (err) {
      if (!err?.response) {
        console.log("No server response");
      } else if (err.response?.status === 409) {
        alert("Username taken");
      } else {
        alert("Registration Failed");
      }
    }
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
