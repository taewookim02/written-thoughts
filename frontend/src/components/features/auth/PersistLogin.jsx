import { Outlet } from "react-router-dom";
import { useState, useEffect } from "react";
import useRefreshToken from "../../../hooks/useRefreshToken";
import useAuth from "../../../hooks/useAuth";

const PersistLogin = () => {
  const [isLoading, setIsLoading] = useState(true);
  const refresh = useRefreshToken();
  const { auth, persist } = useAuth();

  useEffect(() => {
    let isMounted = true;

    const verifyRefreshToken = async () => {
      try {
        await refresh();
      } catch (err) {
        console.error(err);
      } finally {
        if (isMounted) setIsLoading(false);
      }
    };

    // avoid unwanted call to verifyRefreshToken()
    if (!auth?.accessToken && persist) {
      verifyRefreshToken();
    } else {
      setIsLoading(false);
    }
    return () => (isMounted = false);
  }, [auth, persist, refresh]); // TODO: ensure these dependencies are appropriate

  return (
    <>{!persist ? <Outlet /> : isLoading ? <p>Loading..</p> : <Outlet />}</>
  );
};

export default PersistLogin;
