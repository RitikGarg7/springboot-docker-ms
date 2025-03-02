// import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
// import Login from "./pages/Login";
// import Register from "./pages/Register";
// import Dashboard from "./pages/Dashboard";
//
// const PrivateRoute = ({ element }) => {
//   return localStorage.getItem("token") ? element : <Navigate to="/login" />;
// };
//
// function App() {
//   return (
//     <Router>
//       <Routes>
//         <Route path="/login" element={<Login />} />
//         <Route path="/register" element={<Register />} />
//         <Route path="/dashboard" element={<PrivateRoute element={<Dashboard />} />} />
//       </Routes>
//     </Router>
//   );
// }
//
// export default App;

import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";

const PrivateRoute = ({ element, allowedRoles }) => {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  if (!token) return <Navigate to="/login" />;
  if (!allowedRoles.includes(role)) return <Navigate to="/dashboard" />;

  return element;
};

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/" element={<Register />} />
        <Route path="/dashboard" element={<PrivateRoute element={<Dashboard />} allowedRoles={["ROLE_ADMIN", "ROLE_USER"]} />} />
      </Routes>
    </Router>
  );
}

export default App;
