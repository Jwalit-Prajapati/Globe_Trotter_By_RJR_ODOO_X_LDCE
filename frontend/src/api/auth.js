import { apiClient, setAuthToken, removeAuthToken } from './client';

export const login = async (credentials) => {
  const data = await apiClient('/auth/login', {
    method: 'POST',
    body: credentials,
    requiresAuth: false
  });
  if (data.token) {
    setAuthToken(data.token);
  }
  return data;
};

export const signup = async (userData) => {
  const data = await apiClient('/auth/signup', {
    method: 'POST',
    body: userData,
    requiresAuth: false
  });
  if (data.token) {
    setAuthToken(data.token);
  }
  return data;
};

export const logout = () => {
  removeAuthToken();
};
