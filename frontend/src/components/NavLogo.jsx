import logo from "../logo.svg";

const NavLogo = () => {
  return (
    <a className="w-10 grid place-items-center mr-auto" href="/">
      <img src={logo} alt="logo" className="max-w-8 h-auto " />
    </a>
  );
};

export default NavLogo;
