import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

function Dashboard() {
  const [users, setUsers] = useState([]);
  const [isAdmin, setIsAdmin] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token) {
      navigate("/login");
      return;
    }

    try {
      const decoded = jwtDecode(token);
      const role = decoded.roles;
      console.log(decoded);
      if (role === "ROLE_ADMIN") {
        setIsAdmin(true);
        fetchAllUsers(token);
      }
    } catch (error) {
      console.error("Invalid token");
      navigate("/login");
    }
  }, [navigate]);

  const fetchAllUsers = (token) => {
    axios.get("http://localhost:8080/auth/users/all", {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(response => setUsers(response.data))
      .catch(error => console.error("Error fetching users:", error));
  };

  return (
    <div className="d-flex justify-content-center align-items-center min-vh-100" style={{ width: "100vw" }}>
      <div className="card p-4 shadow-lg" style={{ width: "50rem" }}>
        <h2 className="text-center mb-3">Dashboard</h2>

        {isAdmin ? (
          <>
            <h4 className="text-center">All Registered Users</h4>
            <div className="table-responsive">
              <table className="table table-bordered table-striped text-center">
                <thead className="table-dark">
                  <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Role</th>
                  </tr>
                </thead>
                <tbody>
                  {users.length > 0 ? (
                    users.map((u) => (
                      <tr key={u.id}>
                        <td>{u.id}</td>
                        <td>{u.username}</td>
                        <td>{u.email}</td>
                        <td>{u.role}</td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="4" className="text-center">No users found</td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </>
        ) : (
          <h4 className="text-center text-danger">You are not an admin</h4>
        )}

        <button
          className="btn btn-danger w-100 mt-3"
          onClick={() => {
            localStorage.removeItem("token");
            navigate("/login");
          }}
        >
          Logout
        </button>
      </div>
    </div>
  );
}

export default Dashboard;
