const NavLink = ({ href, children }) => {
  return (
    <a className="p-2 hover:text-slate-400" href={href}>
      {children}
    </a>
  );
};

export default NavLink;
