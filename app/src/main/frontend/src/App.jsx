import { useEffect, useState } from "react";

function App() {
  const [hello, setHello] = useState("");
  const fetchData = async () => {
    try {
      const res = await fetch("http://localhost:8080/app/api/test", {
        mode: "cors",
      });
      if (!res.ok) {
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      const data = await res.json();
      setHello(data.message);
    } catch (error) {
      console.log("err:", error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <div className="App">
      <p>{hello ? hello : "loading.."}</p>
    </div>
  );
}

export default App;
