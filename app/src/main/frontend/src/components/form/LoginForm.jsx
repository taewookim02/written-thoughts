import { useState } from "react";

const LoginForm = () => {
  const [id, setId] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await fetch("http://127.0.0.1:8080/app/api/v1/member/login", {
        mode: "cors",
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ id: id, pwd: password }),
      });

      if (res.ok) {
        const data = await res.json();
        console.log("data:", data);
      } else {
        const errorData = await res.json();
        setError(errorData.message || "login failed");
      }
    } catch (e) {
      setError("Network error", e);
    }
  };

  return (
    <>
      <form
        onSubmit={handleSubmit}
        className="login-form flex flex-col max-w-96 mx-auto border border-stone-600 rounded p-12 gap-4"
      >
        <div className="flex flex-col">
          <label htmlFor="id">ID:</label>
          <input
            type="text"
            id="id"
            value={id}
            onChange={(e) => setId(e.target.value)}
            className="border p-1 border-stone-600 rounded"
          />
        </div>
        <div className="flex flex-col">
          <label htmlFor="pwd">Password:</label>
          <input
            type="password"
            id="pwd"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="border p-1 border-stone-600 rounded"
          />
        </div>
        <button type="submit" className="border border-stone-600 rounded">
          Login
        </button>
      </form>
      {error && <p className="error">{error}</p>}
    </>
  );
};

export default LoginForm;
