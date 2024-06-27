import Navbar from "../components/Navbar";
import LoginForm from "../components/form/LoginForm";
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
