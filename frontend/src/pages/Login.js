import React from "react";
import keycloak from "../keycloak";

const Login = () => (
  <div style={{ textAlign: "center", marginTop: 100 }}>
    <h2>Login Required</h2>
    <button onClick={() => keycloak.login()}>Login with Keycloak</button>
  </div>
);

export default Login;
