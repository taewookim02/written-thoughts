import NavLink from "./NavLink";
import NavLogo from "./NavLogo";
const Navbar = ({ isSimple }) => {
  return (
    <nav className="flex gap-4 p-4 min-h-20 items-center">
      <NavLogo />
      {!isSimple && (
        <>
          <NavLink href="./login">Login</NavLink>
          <NavLink href="./signup">Sign up</NavLink>
        </>
      )}
    </nav>
  );
};

export default Navbar;
