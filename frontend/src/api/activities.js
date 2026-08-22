import { apiClient } from './client';

export const searchActivities = (cityId, category = '') => {
  const params = new URLSearchParams();
  if (cityId) params.append('cityId', cityId);
  if (category) params.append('category', category);
  const query = params.toString();
  return apiClient(`/activities${query ? `?${query}` : ''}`);
};

export const getActivityById = (activityId) => apiClient(`/activities/${activityId}`);
