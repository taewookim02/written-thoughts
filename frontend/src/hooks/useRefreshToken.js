import axios from "../api/axios";
import useAuth from "./useAuth";

// useRefreshToken sets the accessToke in auth state, using refreshTokens via withCredentials to /auth/refresh-token.
const useRefreshToken = () => {
  const { setAuth } = useAuth();

  const refresh = async () => {
    try {
      const response = await axios.get("/auth/refresh-token", {
        withCredentials: true,
      });
      setAuth((prev) => {
        console.log(JSON.stringify(prev)); // debug log
        console.log(response.data.accessToken); // debug log
        return { ...prev, accessToken: response.data.accessToken };
      });
      return response.data.accessToken;
    } catch (error) {
      console.error(error);
    }
  };

  return refresh;
};

export default useRefreshToken;
