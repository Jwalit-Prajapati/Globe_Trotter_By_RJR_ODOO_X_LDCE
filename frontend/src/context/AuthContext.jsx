import { createContext, useContext, useState } from 'react';
import { getAuthToken, getStoredUser, removeAuthToken, removeStoredUser } from '../api/client';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => getStoredUser());
  const [loading] = useState(false);
  const [isAuthenticated, setIsAuthenticated] = useState(() => !!getAuthToken());

  const handleLogin = (userData) => {
    setIsAuthenticated(true);
    setUser(userData);
  };

  const handleLogout = () => {
    removeAuthToken();
    removeStoredUser();
    setIsAuthenticated(false);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, isAuthenticated, loading, handleLogin, handleLogout }}>
      {children}
    </AuthContext.Provider>
  );
};

// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => useContext(AuthContext);
