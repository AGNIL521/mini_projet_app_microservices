import { useEffect, useState } from "react";
import keycloak from "../keycloak";

export function useAuth() {
  const [user, setUser] = useState(null);
  const [role, setRole] = useState(null);

  useEffect(() => {
    if (!keycloak.authenticated) {
      keycloak.init({ onLoad: "login-required" }).then(auth => {
        if (auth) {
          setUser(keycloak.tokenParsed?.preferred_username);
          const roles = keycloak.tokenParsed?.realm_access?.roles || [];
          setRole(roles.includes("ADMIN") ? "ADMIN" : roles.includes("CLIENT") ? "CLIENT" : null);
        } else {
          setUser(null);
          setRole(null);
        }
      });
    } else {
      setUser(keycloak.tokenParsed?.preferred_username);
      const roles = keycloak.tokenParsed?.realm_access?.roles || [];
      setRole(roles.includes("ADMIN") ? "ADMIN" : roles.includes("CLIENT") ? "CLIENT" : null);
    }
  }, []);

  const logout = () => keycloak.logout();

  return { user, role, logout };
}
