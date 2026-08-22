import { apiClient } from './client';

export const searchCities = (query, country = '') => {
  const params = new URLSearchParams();
  if (query) params.append('q', query);
  if (country) params.append('country', country);
  return apiClient(`/cities?${params.toString()}`);
};

export const getPopularCities = () => apiClient('/cities/popular');
