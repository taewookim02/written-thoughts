import Navbar from "../components/Navbar";
import LoginForm from "../components/login/LoginForm";
import MainContainer from "../components/MainContainer";
const Login = () => {
  return (
    <>
      <Navbar isSimple={true}></Navbar>
      <MainContainer>
        <LoginForm />
      </MainContainer>
    </>
  );
};

export default Login;
