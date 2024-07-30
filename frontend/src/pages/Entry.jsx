import { useEffect, useState } from "react";
import useAxiosPrivate from "../hooks/useAxiosPrivate";
import useAuth from "../hooks/useAuth";
import axios from "../api/axios";
import useRefreshToken from "../hooks/useRefreshToken";
const Entry = () => {
  const [entries, setEntries] = useState();
  const refresh = useRefreshToken();

  useEffect(() => {
    let isMounted = true;
    const controller = new AbortController();

    const getEntries = async () => {
      console.log("in getEntries");
      try {
        const response = await axios.get("/entry/user", {
          withCredentials: true,
          headers: {
            "Content-Type": "application/json",
            // Authorization: `Bearer ${auth.accessToken}`,
          },
          signal: controller.signal,
        });
        console.log(response.data);
        // isMounted
        setEntries(response.data);
      } catch (error) {
        console.error(error);
      }
    };
    getEntries();
    console.log(entries);

    // cleanup when unmount
    return () => {
      isMounted = false;
      controller.abort();
    };
  }, []);

  return (
    <>
      {/* TODO: add useAxiosPrivate, navigate, location */}
      <h1>Entry</h1>
      <ul>
        {/* {entries?.length}? (
        {entries.map((entry, i) => (
          <li
            key={i}
          >{`${entry.title}: ${entry.content}, ${entry.createdAt}`}</li>
        ))}
        ) : <p>No entries to display</p> */}
      </ul>
      <button onClick={() => refresh()}>Refresh</button>
    </>
  );
};

export default Entry;
