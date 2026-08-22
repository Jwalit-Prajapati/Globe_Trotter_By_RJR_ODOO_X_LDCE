import { apiClient } from './client';

// Stops
export const addStop = (tripId, data) => apiClient(`/trips/${tripId}/stops`, { method: 'POST', body: data });
export const setStopCosts = (stopId, data) => apiClient(`/stops/${stopId}/costs`, { method: 'PUT', body: data });
export const deleteStop = (stopId) => apiClient(`/stops/${stopId}`, { method: 'DELETE' });
export const reorderStop = (stopId, data) => apiClient(`/stops/${stopId}/order`, { method: 'PUT', body: data });

// Activities
export const searchActivities = (cityId, category = '') => {
  const params = new URLSearchParams();
  if (cityId) params.append('cityId', cityId);
  if (category) params.append('category', category);
  return apiClient(`/activities?${params.toString()}`);
};
export const attachActivityToStop = (stopId, data) => apiClient(`/stops/${stopId}/activities`, { method: 'POST', body: data });
export const detachActivity = (stopActivityId) => apiClient(`/stop-activities/${stopActivityId}`, { method: 'DELETE' });
