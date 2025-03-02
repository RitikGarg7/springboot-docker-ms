// import { useState } from "react";
// import { useNavigate } from "react-router-dom";
// import axios from "axios";
//
// function Login() {
//   const [username, setUsername] = useState("");
//   const [password, setPassword] = useState("");
//   const navigate = useNavigate();
//
//   const handleLogin = async (e) => {
//     e.preventDefault();
//     try {
//       const response = await axios.post("http://localhost:8080/auth/login", { username, password });
//       console.log(response);
//       localStorage.setItem("token", response.data);
//       navigate("/dashboard");
//     } catch (error) {
//       alert("Invalid credentials");
//     }
//   };
//
//   return (
//     <div className="d-flex justify-content-center align-items-center min-vh-100" style={{ width: "100vw" }}>
//       <div className="card p-4 shadow-lg mx-auto" style={{ width: "100%", maxWidth: "400px" }}>
//         <h2 className="text-center mb-4">Login</h2>
//         <form onSubmit={handleLogin}>
//           <div className="mb-3">
//             <label className="form-label">Username</label>
//             <input type="text" className="form-control" value={username} onChange={(e) => setUsername(e.target.value)} required />
//           </div>
//           <div className="mb-3">
//             <label className="form-label">Password</label>
//             <input type="password" className="form-control" value={password} onChange={(e) => setPassword(e.target.value)} required />
//           </div>
//           <button type="submit" className="btn btn-primary w-100">Login</button>
//         </form>
//       </div>
//     </div>
//   );
// }
//
// export default Login;


import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post("http://localhost:8080/auth/login", { username, password });
      const token = response.data;
      localStorage.setItem("token", token);

      const decoded = jwtDecode(token);
      localStorage.setItem("role", decoded.roles);

      navigate("/dashboard");
    } catch (error) {
      alert("Invalid credentials");
    }
  };

  return (
    <div className="d-flex justify-content-center align-items-center min-vh-100" style={{ width: "100vw" }}>
      <div className="card p-4 shadow-lg" style={{ width: "25rem" }}>
        <h2 className="text-center mb-3">Login</h2>
        <form onSubmit={handleLogin}>
          <div className="mb-3">
            <label className="form-label">Username</label>
            <input type="text" className="form-control" value={username} onChange={(e) => setUsername(e.target.value)} required />
          </div>
          <div className="mb-3">
            <label className="form-label">Password</label>
            <input type="password" className="form-control" value={password} onChange={(e) => setPassword(e.target.value)} required />
          </div>
          <button type="submit" className="btn btn-primary w-100">Login</button>
        </form>
      </div>
    </div>
  );
}

export default Login;
