import MainContainer from "../components/MainContainer";
import Navbar from "../components/Navbar";
import SignupForm from "../components/form/SignupForm";
const Signup = () => {
  return (
    <>
      <Navbar isSimple={true} />
      <MainContainer>
        <SignupForm></SignupForm>
      </MainContainer>
    </>
  );
};

export default Signup;
