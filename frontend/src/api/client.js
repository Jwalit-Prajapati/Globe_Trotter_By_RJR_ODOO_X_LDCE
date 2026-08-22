const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

const TOKEN_KEY = 'token';
const USER_KEY = 'user';

export const getAuthToken = () => localStorage.getItem(TOKEN_KEY);
export const setAuthToken = (token) => localStorage.setItem(TOKEN_KEY, token);
export const removeAuthToken = () => localStorage.removeItem(TOKEN_KEY);

export const getStoredUser = () => {
  const raw = localStorage.getItem(USER_KEY);
  if (!raw) return null;
  try {
    return JSON.parse(raw);
  } catch {
    return null;
  }
};
export const setStoredUser = (user) => localStorage.setItem(USER_KEY, JSON.stringify(user));
export const removeStoredUser = () => localStorage.removeItem(USER_KEY);

export const apiClient = async (endpoint, { body, method = 'GET', headers = {}, requiresAuth = true } = {}) => {
  const url = `${BASE_URL}${endpoint}`;

  const options = {
    method,
    headers: {
      'Content-Type': 'application/json',
      ...headers,
    },
  };

  if (requiresAuth) {
    const token = getAuthToken();
    if (token) {
      options.headers['Authorization'] = `Bearer ${token}`;
    }
  }

  if (body !== undefined) {
    options.body = JSON.stringify(body);
  }

  let response;
  try {
    response = await fetch(url, options);
  } catch (networkError) {
    console.error(`Network error on ${endpoint}:`, networkError);
    throw new Error('Unable to reach the server. Please check your connection and try again.', { cause: networkError });
  }

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    const message = errorData.message
      || (errorData.fieldErrors && Object.values(errorData.fieldErrors)[0])
      || 'API request failed';
    const error = new Error(message);
    error.status = response.status;
    error.fieldErrors = errorData.fieldErrors;
    throw error;
  }

  if (response.status === 204) {
    return null; // No content
  }

  return await response.json();
};
