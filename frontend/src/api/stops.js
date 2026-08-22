import { apiClient } from './client';

// data: { tripId, cityId, startDate?, endDate?, orderIndex?, transportCost?, stayCost?, mealCost? }
export const createStop = (data) => apiClient('/stops', { method: 'POST', body: data });

export const getStopsForTrip = (tripId) => apiClient(`/stops/trip/${tripId}`);

export const getStopById = (stopId) => apiClient(`/stops/${stopId}`);

// data must include the full StopRequest shape (tripId, cityId, dates, costs, orderIndex)
export const updateStop = (stopId, data) => apiClient(`/stops/${stopId}`, { method: 'PUT', body: data });

export const deleteStop = (stopId) => apiClient(`/stops/${stopId}`, { method: 'DELETE' });
