import { apiClient } from './client';

export const getTrips = () => apiClient('/trips');

// data: { name, description?, startDate?, endDate?, budgetLimit?, isPublic? }
export const createTrip = (data) => apiClient('/trips', { method: 'POST', body: data });

export const getTripById = (id) => apiClient(`/trips/${id}`);

export const updateTrip = (id, data) => apiClient(`/trips/${id}`, { method: 'PUT', body: data });

export const deleteTrip = (id) => apiClient(`/trips/${id}`, { method: 'DELETE' });

export const shareTrip = (id) => apiClient(`/trips/${id}/share`, { method: 'POST' });

export const getPublicTrip = (slug) => apiClient(`/trips/public/${slug}`, { requiresAuth: false });

export const copyPublicTrip = (slug) => apiClient(`/trips/copy/${slug}`, { method: 'POST' });

export const getTripBudget = (id) => apiClient(`/budgets/trip/${id}`);
