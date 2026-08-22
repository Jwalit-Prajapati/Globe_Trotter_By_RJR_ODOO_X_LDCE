import { apiClient } from './client';

// Note: /api/cities/** requires authentication (not in the backend's public endpoint list).
export const searchCities = (name = '', country = '') => {
  const params = new URLSearchParams();
  if (name) params.append('name', name);
  if (country) params.append('country', country);
  const query = params.toString();
  return apiClient(`/cities${query ? `?${query}` : ''}`);
};

export const getPopularCities = () => apiClient('/cities/popular');

export const getCityById = (cityId) => apiClient(`/cities/${cityId}`);
