// import { useEffect, useState } from "react";
// import axios from "axios";
// import { useNavigate } from "react-router-dom";
// import { jwtDecode } from "jwt-decode";
//
// function Dashboard() {
//   const [users, setUsers] = useState([]);
//   const [orders, setOrders] = useState([]);
//   const [isAdmin, setIsAdmin] = useState(false);
//   const navigate = useNavigate();
//
//   useEffect(() => {
//     const token = localStorage.getItem("token");
//
//     if (!token) {
//       navigate("/login");
//       return;
//     }
//
//     try {
//       const decoded = jwtDecode(token);
//       const role = decoded.roles;
//       const userId = decoded.sub; // Extract user ID from token
//
//       if (role === "ROLE_ADMIN") {
//         setIsAdmin(true);
//         fetchAllUsers(token);
//         fetchAllOrders(token);
//       } else {
//         fetchUserOrders(token, userId);
//       }
//     } catch (error) {
//       console.error("Invalid token");
//       navigate("/login");
//     }
//   }, [navigate]);
//
//   const fetchAllUsers = (token) => {
//     axios.get("http://localhost:8080/auth/users/all", {
//       headers: { Authorization: `Bearer ${token}` },
//     })
//     .then(response => setUsers(response.data))
//     .catch(error => console.error("Error fetching users:", error));
//   };
//
//   const fetchAllOrders = (token) => {
//     axios.get("http://localhost:8080/orders", {
//       headers: { Authorization: `Bearer ${token}` },
//     })
//     .then(response => setOrders(response.data))
//     .catch(error => console.error("Error fetching orders:", error));
//   };
//
//   const fetchUserOrders = (token, userId) => {
//     axios.get(`http://localhost:8080/orders/user`, {
//       headers: { Authorization: `Bearer ${token}` },
//     })
//     .then(response => setOrders(response.data))
//     .catch(error => console.error("Error fetching user orders:", error));
//   };
//
//   return (
//     <div className="d-flex justify-content-center align-items-center min-vh-100" style={{ width: "100vw" }}>
//       <div className="card p-4 shadow-lg" style={{ width: "50rem" }}>
//         <h2 className="text-center mb-3">Dashboard</h2>
//
//         {isAdmin ? (
//           <>
//             <h4 className="text-center">All Registered Users</h4>
//             <div className="table-responsive">
//               <table className="table table-bordered table-striped text-center">
//                 <thead className="table-dark">
//                   <tr>
//                     <th>ID</th>
//                     <th>Username</th>
//                     <th>Email</th>
//                     <th>Role</th>
//                   </tr>
//                 </thead>
//                 <tbody>
//                   {users.length > 0 ? (
//                     users.map((u) => (
//                       <tr key={u.id}>
//                         <td>{u.id}</td>
//                         <td>{u.username}</td>
//                         <td>{u.email}</td>
//                         <td>{u.role}</td>
//                       </tr>
//                     ))
//                   ) : (
//                     <tr>
//                       <td colSpan="4" className="text-center">No users found</td>
//                     </tr>
//                   )}
//                 </tbody>
//               </table>
//             </div>
//
//             <h4 className="text-center mt-4">All Orders</h4>
//           </>
//         ) : (
//           <h4 className="text-center">Your Orders</h4>
//         )}
//
//         <div className="table-responsive">
//           <table className="table table-bordered table-striped text-center">
//             <thead className="table-dark">
//               <tr>
//                 <th>Order ID</th>
//                 <th>User</th>
//                 <th>Product</th>
//                 <th>Price</th>
//                 <th>Quantity</th>
//               </tr>
//             </thead>
//             <tbody>
//               {orders.length > 0 ? (
//                 orders.map((order) => (
//                   <tr key={order.id}>
//                     <td>{order.id}</td>
//                     <td>{isAdmin ? order.user.username : order.username}</td>
//                     <td>{order.product}</td>
//                     <td>{order.price}</td>
//                     <td>{order.quantity}</td>
//                   </tr>
//                 ))
//               ) : (
//                 <tr>
//                   <td colSpan="4" className="text-center">No orders found</td>
//                 </tr>
//               )}
//             </tbody>
//           </table>
//         </div>
//
//         <button
//           className="btn btn-danger w-100 mt-3"
//           onClick={() => {
//             localStorage.removeItem("token");
//             navigate("/login");
//           }}
//         >
//           Logout
//         </button>
//       </div>
//     </div>
//   );
// }
//
// export default Dashboard;


import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

function Dashboard() {
  const [users, setUsers] = useState([]);
  const [orders, setOrders] = useState([]);
  const [isAdmin, setIsAdmin] = useState(false);
  const [newOrder, setNewOrder] = useState({ product: "", price: "", quantity: "" });
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
      const userId = decoded.sub; // Extract user ID from token

      if (role === "ROLE_ADMIN") {
        setIsAdmin(true);
        fetchAllUsers(token);
        fetchAllOrders(token);
      } else {
        fetchUserOrders(token);
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

  const fetchAllOrders = (token) => {
    axios.get("http://localhost:8080/orders", {
      headers: { Authorization: `Bearer ${token}` },
    })
    .then(response => setOrders(response.data))
    .catch(error => console.error("Error fetching orders:", error));
  };

  const fetchUserOrders = (token) => {
    axios.get(`http://localhost:8080/orders/user`, {
      headers: { Authorization: `Bearer ${token}` },
    })
    .then(response => setOrders(response.data))
    .catch(error => console.error("Error fetching user orders:", error));
  };

  const handleOrderSubmit = (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token");

    axios.post("http://localhost:8080/orders", newOrder, {
      headers: { Authorization: `Bearer ${token}` },
    })
//     .then(() => {
//       fetchUserOrders(token);
//       setNewOrder({ product: "", price: "", quantity: "" }); // Reset form
//     })
    .then(response => {
        const newCreatedOrder = response.data; // Get the new order from API response

        // Update orders list immediately without refreshing
        setOrders(prevOrders => [...prevOrders, newCreatedOrder]);

        // Reset the form fields
        setNewOrder({ product: "", price: "", quantity: "" });
      })
    .catch(error => console.error("Error placing order:", error));
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

            <h4 className="text-center mt-4">All Orders</h4>
          </>
        ) : (
          <>
            <h4 className="text-center">Place an Order</h4>
            <form onSubmit={handleOrderSubmit} className="mb-3">
              <div className="mb-2">
                <label className="form-label">Product Name</label>
                <input
                  type="text"
                  className="form-control"
                  value={newOrder.product}
                  onChange={(e) => setNewOrder({ ...newOrder, product: e.target.value })}
                  required
                />
              </div>
              <div className="mb-2">
                <label className="form-label">Price</label>
                <input
                  type="number"
                  className="form-control"
                  value={newOrder.price}
                  onChange={(e) => setNewOrder({ ...newOrder, price: e.target.value })}
                  required
                />
              </div>
              <div className="mb-2">
                <label className="form-label">Quantity</label>
                <input
                  type="number"
                  className="form-control"
                  value={newOrder.quantity}
                  onChange={(e) => setNewOrder({ ...newOrder, quantity: e.target.value })}
                  required
                />
              </div>
              <button type="submit" className="btn btn-success w-100">Place Order</button>
            </form>

            <h4 className="text-center">Your Orders</h4>
          </>
        )}

        <div className="table-responsive">
          <table className="table table-bordered table-striped text-center">
            <thead className="table-dark">
              <tr>
                <th>Order ID</th>
                <th>User</th>
                <th>Product</th>
                <th>Price</th>
                <th>Quantity</th>
              </tr>
            </thead>
            <tbody>
              {orders.length > 0 ? (
                orders.map((order) => (
                  <tr key={order.id}>
                    <td>{order.id}</td>
                    <td>{isAdmin ? order.user.username : "You"}</td>
                    <td>{order.product}</td>
                    <td>{order.price}</td>
                    <td>{order.quantity}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5" className="text-center">No orders found</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>

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
