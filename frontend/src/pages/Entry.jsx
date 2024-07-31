import { useEffect, useState } from "react";
import useAxiosPrivate from "../hooks/useAxiosPrivate";
import { useNavigate, useLocation } from "react-router-dom";

const Entry = () => {
  const [entries, setEntries] = useState();
  const axiosPrivate = useAxiosPrivate();
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    let isMounted = true;
    const controller = new AbortController();

    const getEntries = async () => {
      try {
        const response = await axiosPrivate.get("/entry/user", {
          signal: controller.signal,
        });
        if (isMounted) {
          setEntries(response.data);
        }
      } catch (error) {
        console.error(error);
        // navigate("/login", { state: { from: location }, replace: true });
        // if (error.code !== "ERR_CANCELED") {
        // }
      }
    };
    getEntries();

    // cleanup when unmount
    return () => {
      isMounted = false;
      controller.abort();
    };
  }, []);

  return (
    <>
      <h1>Entry</h1>
      <ul>
        {entries?.length > 0 ? (
          entries?.map((entry, i) => (
            <li
              key={entry.id}
            >{`${entry.title}: ${entry.content}, ${entry.createdAt}`}</li>
          ))
        ) : (
          <p>No entries to display</p>
        )}
      </ul>
    </>
  );
};

export default Entry;
