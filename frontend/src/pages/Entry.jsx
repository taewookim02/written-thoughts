import { useEffect } from "react";
import axios from "../api/axios";
import useAuth from "../hooks/useAuth";
const Entry = () => {
  const { auth } = useAuth();
  console.log(auth);
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get("/entry/user", {
          withCredentials: true,
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${auth.accessToken}`,
          },
        });
        console.log(response.data);
      } catch (error) {
        console.err(error);
      }
    };
    fetchData();
  }, []);

  return (
    <>
      {/* TODO: add useAxiosPrivate, navigate, location */}
      <h1>Entry</h1>
    </>
  );
};

export default Entry;
