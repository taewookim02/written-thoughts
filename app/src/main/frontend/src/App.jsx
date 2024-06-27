import Navbar from "./components/Navbar";

const App = () => {
  const fetchData = async () => {
    const res = await fetch("http://127.0.0.1:8080/app/api/test");
    const data = await res.json();
    console.log(data);
  };
  fetchData();
  // introductory page
  return (
    <>
      <Navbar />
      <div className="App text-3xl font-bold underline">
        <p>hello</p>
      </div>
    </>
  );
};

export default App;
