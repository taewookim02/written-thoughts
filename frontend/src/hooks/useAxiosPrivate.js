import { axiosPrivate } from "../api/axios";
import { useEffect } from "react";
import useRefreshToken from "./useRefreshToken";
import useAuth from "./useAuth";

// to attach interceptors to axiosPrivate
const useAxiosPrivate = () => {
  const refresh = useRefreshToken(); // useRefreshToken sets new accessToken to auth
  const { auth } = useAuth();

  useEffect(() => {
    const requestIntercept = axiosPrivate.interceptors.request.use(
      (config) => {
        // if the request headers doesn't have Authorization
        if (!config.headers["Authorization"]) {
          // first attempt
          config.headers["Authorization"] = `Bearer ${auth?.accessToken}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    const responseIntercept = axiosPrivate.interceptors.response.use(
      (response) => response,
      async (error) => {
        // when access_token expired
        const prevRequest = error?.config;
        if (error?.response?.status === 403 && !prevRequest?.sent) {
          // sent restricts infinite calls
          prevRequest.sent = true;
          try {
            const newAccessToken = await refresh(); // this is undefined
            console.log("useAxiosPrivate: " + newAccessToken);
            prevRequest.headers["Authorization"] = `Bearer ${newAccessToken}`;
            return axiosPrivate(prevRequest);
          } catch (err) {
            console.error("useAxiosPrivate refresh token error: ", err);
            return Promise.reject(err);
          }
        }
        return Promise.reject(error);
      }
    );

    // clean up
    return () => {
      axiosPrivate.interceptors.request.eject(requestIntercept);
      axiosPrivate.interceptors.response.eject(responseIntercept);
    };
  }, [auth, refresh]);

  return axiosPrivate;
};

export default useAxiosPrivate;
