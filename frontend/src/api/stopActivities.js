import { apiClient } from './client';

// data: { stopId, activityId, dayDate, scheduledTime?, cost }
export const addActivityToStop = (data) => apiClient('/stop-activities', { method: 'POST', body: data });

export const getActivitiesForStop = (stopId) => apiClient(`/stop-activities/stop/${stopId}`);

export const getStopActivityById = (stopActivityId) => apiClient(`/stop-activities/${stopActivityId}`);

export const updateStopActivity = (stopActivityId, data) =>
  apiClient(`/stop-activities/${stopActivityId}`, { method: 'PUT', body: data });

export const removeActivityFromStop = (stopActivityId) =>
  apiClient(`/stop-activities/${stopActivityId}`, { method: 'DELETE' });
