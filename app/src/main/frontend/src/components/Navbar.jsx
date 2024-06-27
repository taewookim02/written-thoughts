import logo from "../logo.svg";
import NavLink from "./NavLink";
const Navbar = () => {
  return (
    <nav className="flex gap-4 p-4">
      <img src={logo} alt="logo" className="max-w-8 h-auto mr-auto" />
      <NavLink href="./journal">Journal</NavLink>
      <NavLink href="./login">Login</NavLink>
      <NavLink href="./signup">Sign up</NavLink>
    </nav>
  );
};

export default Navbar;
