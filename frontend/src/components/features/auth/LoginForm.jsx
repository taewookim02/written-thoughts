import { useState, useEffect } from "react";
import InputField from "../../common/InputField";
import { StyledForm } from "../../common/StyledForm";
import { StyledSubmitButton } from "../../common/StyledSubmitButton";
import useAuth from "../../../hooks/useAuth";
import axios from "../../../api/axios";
import { Link, useNavigate, useLocation } from "react-router-dom";

const LOGIN_URL = "/auth/authenticate";
const LoginForm = () => {
  const { setAuth, persist, setPersist } = useAuth();

  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || "/";

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();

    const payload = {
      email,
      password,
    };

    try {
      const response = await axios.post(LOGIN_URL, JSON.stringify(payload), {
        headers: { "Content-Type": "application/json" },
        withCredentials: true,
      });

      const accessToken = response?.data?.accessToken;
      setAuth({ accessToken });

      // reset states
      setEmail("");
      setPassword("");

      // navigate
      navigate(from, { replace: true });
    } catch (err) {
      alert(err);
      if (!err?.response) {
        alert("No server response");
      } else if (!err.response?.status === 403) {
        alert("403!");
      } else if (err.response?.status === 401) {
        alert("Unauthorized!");
      } else {
        alert("Login failed");
      }
    }
  };

  const togglePersist = () => {
    setPersist((prev) => !prev);
  };

  useEffect(() => {
    localStorage.setItem("persist", persist);
  }, [persist]);

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
      <div className="persistCheck">
        <input
          type="checkbox"
          id="persist"
          onChange={togglePersist}
          checked={persist}
        />
        <label htmlFor="persist">Trust this device</label>
      </div>
    </StyledForm>
  );
};

export default LoginForm;
