import { apiClient } from './client';

export const getTrips = () => apiClient('/trips');
export const createTrip = (data) => apiClient('/trips', { method: 'POST', body: data });
export const getTripById = (id) => apiClient(`/trips/${id}`);
export const deleteTrip = (id) => apiClient(`/trips/${id}`, { method: 'DELETE' });
export const getTripBudget = (id) => apiClient(`/trips/${id}/budget`);
export const shareTrip = (id) => apiClient(`/trips/${id}/share`, { method: 'POST' });
export const getPublicTrip = (slug) => apiClient(`/public/${slug}`, { requiresAuth: false });
