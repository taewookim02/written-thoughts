import { useEffect, useState } from "react";
import useAxiosPrivate from "../hooks/useAxiosPrivate";

const Entry = () => {
  const [entries, setEntries] = useState();
  const axiosPrivate = useAxiosPrivate();

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
      }
    };
    getEntries();

    // cleanup when unmount
    return () => {
      isMounted = false;
      controller.abort();
    };
  }, []);

  useEffect(() => {
    console.log(entries);
  }, [entries]);

  return (
    <>
      {/* TODO: add useAxiosPrivate, navigate, location */}
      <h1>Entry</h1>
      <ul>
        {entries?.length}? (
        {entries?.map((entry, i) => (
          <li
            key={i}
          >{`${entry.title}: ${entry.content}, ${entry.createdAt}`}</li>
        ))}
        ) : <p>No entries to display</p>
      </ul>
    </>
  );
};

export default Entry;
