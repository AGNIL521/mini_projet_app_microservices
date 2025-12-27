import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Home from "./pages/Home";
import Products from "./pages/Products";
import Orders from "./pages/Orders";
import Login from "./pages/Login";
import { useAuth } from "./hooks/useAuth";

function App() {
  const { user, role, logout } = useAuth();
  return (
    <Router>
      <div>
        {user && (
          <div style={{ float: "right", margin: 10 }}>
            <span>Welcome, {user} ({role}) </span>
            <button onClick={logout}>Logout</button>
          </div>
        )}
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<Home />} />
          <Route path="/products" element={user ? <Products /> : <Navigate to="/login" />} />
          <Route path="/orders" element={user && role === "CLIENT" ? <Orders /> : <Navigate to="/login" />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
