import { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getTripById, deleteTrip, getTripBudget, shareTrip } from '../api/trips';
import { createStop, deleteStop } from '../api/stops';
import { searchCities } from '../api/cities';
import { searchActivities } from '../api/activities';
import { addActivityToStop, removeActivityFromStop } from '../api/stopActivities';
import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import { Modal } from '../components/ui/Modal';

export const TripDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [trip, setTrip] = useState(null);
  const [budget, setBudget] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [shareInfo, setShareInfo] = useState(null);

  // Add stop modal
  const [showStopModal, setShowStopModal] = useState(false);
  const [citySearch, setCitySearch] = useState('');
  const [cityResults, setCityResults] = useState([]);
  const [stopForm, setStopForm] = useState({ cityId: '', startDate: '', endDate: '' });
  const [stopSubmitting, setStopSubmitting] = useState(false);
  const [stopError, setStopError] = useState('');

  // Add activity modal
  const [activityStopId, setActivityStopId] = useState(null);
  const [activityResults, setActivityResults] = useState([]);
  const [activityForm, setActivityForm] = useState({ activityId: '', dayDate: '', scheduledTime: '', cost: '' });
  const [activitySubmitting, setActivitySubmitting] = useState(false);
  const [activityError, setActivityError] = useState('');

  const fetchTripDetails = useCallback(async () => {
    try {
      const [tripData, budgetData] = await Promise.all([
        getTripById(id),
        getTripBudget(id).catch(() => null),
      ]);
      setTrip(tripData);
      setBudget(budgetData);
    } catch (err) {
      console.error('Failed to load trip', err);
      setError(err.message || 'Failed to load trip');
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect -- initial data load on mount
    fetchTripDetails();
  }, [fetchTripDetails]);

  const handleDelete = async () => {
    if (window.confirm('Are you sure you want to delete this trip?')) {
      try {
        await deleteTrip(id);
        navigate('/dashboard');
      } catch (err) {
        console.error('Failed to delete', err);
        alert(err.message || 'Failed to delete trip');
      }
    }
  };

  const handleShare = async () => {
    try {
      const data = await shareTrip(id);
      setShareInfo(data);
    } catch (err) {
      console.error('Failed to share', err);
      alert(err.message || 'Failed to share trip');
    }
  };

  // --- Add Stop ---
  const openStopModal = () => {
    setStopForm({ cityId: '', startDate: '', endDate: '' });
    setStopError('');
    setCitySearch('');
    setCityResults([]);
    setShowStopModal(true);
  };

  const handleCitySearch = async (e) => {
    e.preventDefault();
    try {
      const results = await searchCities(citySearch);
      setCityResults(results || []);
    } catch (err) {
      setStopError(err.message || 'Failed to search cities');
    }
  };

  const handleAddStop = async (e) => {
    e.preventDefault();
    if (!stopForm.cityId) {
      setStopError('Please select a city');
      return;
    }
    setStopSubmitting(true);
    setStopError('');
    try {
      await createStop({
        tripId: Number(id),
        cityId: Number(stopForm.cityId),
        startDate: stopForm.startDate || undefined,
        endDate: stopForm.endDate || undefined,
        orderIndex: trip.stops ? trip.stops.length : 0,
      });
      setShowStopModal(false);
      await fetchTripDetails();
    } catch (err) {
      setStopError(err.message || 'Failed to add destination');
    } finally {
      setStopSubmitting(false);
    }
  };

  const handleDeleteStop = async (stopId) => {
    if (!window.confirm('Remove this destination from the trip?')) return;
    try {
      await deleteStop(stopId);
      await fetchTripDetails();
    } catch (err) {
      alert(err.message || 'Failed to remove destination');
    }
  };

  // --- Add Activity ---
  const openActivityModal = async (stop) => {
    setActivityStopId(stop.id);
    setActivityForm({ activityId: '', dayDate: stop.startDate || '', scheduledTime: '', cost: '' });
    setActivityError('');
    try {
      const results = await searchActivities(stop.city?.id);
      setActivityResults(results || []);
    } catch (err) {
      setActivityError(err.message || 'Failed to load activities');
    }
  };

  const closeActivityModal = () => setActivityStopId(null);

  const handleAddActivity = async (e) => {
    e.preventDefault();
    if (!activityForm.activityId || !activityForm.dayDate) {
      setActivityError('Please select an activity and date');
      return;
    }
    setActivitySubmitting(true);
    setActivityError('');
    try {
      await addActivityToStop({
        stopId: activityStopId,
        activityId: Number(activityForm.activityId),
        dayDate: activityForm.dayDate,
        scheduledTime: activityForm.scheduledTime || undefined,
        cost: activityForm.cost ? Number(activityForm.cost) : 0,
      });
      setActivityStopId(null);
      await fetchTripDetails();
    } catch (err) {
      setActivityError(err.message || 'Failed to add activity');
    } finally {
      setActivitySubmitting(false);
    }
  };

  const handleRemoveActivity = async (stopActivityId) => {
    try {
      await removeActivityFromStop(stopActivityId);
      await fetchTripDetails();
    } catch (err) {
      alert(err.message || 'Failed to remove activity');
    }
  };

  if (loading) return <div className="container page-wrapper text-center">Loading...</div>;
  if (error) return <div className="container page-wrapper text-center text-danger">{error}</div>;
  if (!trip) return <div className="container page-wrapper text-center">Trip not found</div>;

  return (
    <div className="animate-fade-in pb-16">
      {/* Cover Image */}
      <div className="w-full h-72 md:h-96 overflow-hidden relative">
        <img src="https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?ixlib=rb-4.0.3&auto=format&fit=crop&w=2000&q=80" alt="Trip Cover" className="w-full h-full object-cover" />
        <div className="absolute inset-0 bg-black/20"></div>
      </div>

      <div className="container mt-8">
        <div className="flex flex-col md:flex-row justify-between items-start md:items-end mb-8 gap-4">
          <div>
            <h1 className="text-4xl font-bold mb-2">{trip.name}</h1>
            {trip.description && <p className="text-muted mb-2">{trip.description}</p>}
            <p className="text-muted text-lg font-medium flex items-center gap-2">
              <span className="w-2 h-2 rounded-full bg-success inline-block"></span>
              {trip.startDate && trip.endDate ? `${trip.startDate} – ${trip.endDate}` : 'Planning Phase'}
            </p>
          </div>
          <div className="flex gap-3">
            <Button variant="secondary" onClick={handleShare} className="shadow-sm">Share Trip</Button>
            <Button variant="danger" onClick={handleDelete} className="shadow-sm">Delete</Button>
          </div>
        </div>

        {shareInfo && (
          <div className="badge badge-accent mb-6" style={{ display: 'block', padding: '0.75rem 1rem' }}>
            Public link: <a href={`/public/${shareInfo.publicSlug}`}>{shareInfo.shareUrl || `${window.location.origin}/public/${shareInfo.publicSlug}`}</a>
          </div>
        )}

        <hr />

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-12 mt-8">
          {/* Main Content: Itinerary */}
          <div className="lg:col-span-2 flex flex-col gap-8">
            <div className="flex justify-between items-center">
              <h2 className="text-2xl font-bold">Your Itinerary</h2>
              <Button variant="secondary" onClick={openStopModal}>+ Add Destination</Button>
            </div>
            {trip.stops && trip.stops.length > 0 ? (
              <div className="flex flex-col gap-6 relative">
                {trip.stops.map((stop, idx) => (
                  <div key={stop.id} className="flex gap-6">
                    <div className="flex flex-col items-center">
                      <div className="w-4 h-4 rounded-full bg-accent-primary mt-1 z-10 ring-4 ring-white"></div>
                      {idx !== trip.stops.length - 1 && <div className="w-0.5 h-full bg-border-color mt-2"></div>}
                    </div>
                    <Card className="flex-1 card-borderless p-0 pb-6 border-b border-border-color rounded-none shadow-none hover:shadow-none hover:-translate-y-0">
                      <div className="flex justify-between items-start mb-4">
                        <h3 className="text-xl font-bold">{stop.city?.name}{stop.city?.country ? `, ${stop.city.country}` : ''}</h3>
                        <div className="flex items-center gap-3">
                          <span className="badge font-medium">
                            {stop.startDate && stop.endDate ? `${stop.startDate} - ${stop.endDate}` : 'No dates'}
                          </span>
                          <button type="button" className="btn-ghost text-sm" onClick={() => handleDeleteStop(stop.id)}>Remove</button>
                        </div>
                      </div>
                      <div className="flex flex-col gap-3">
                        <h4 className="text-sm font-semibold text-muted uppercase tracking-wider">Planned Activities</h4>
                        <div className="flex flex-wrap gap-2">
                          {stop.activities && stop.activities.map(sa => (
                            <div key={sa.id} className="px-3 py-1.5 bg-bg-secondary border border-border-color rounded-md text-sm font-medium flex items-center gap-2">
                              {sa.activity?.name}
                              <button type="button" className="modal-close" style={{ fontSize: '1rem' }} onClick={() => handleRemoveActivity(sa.id)} aria-label="Remove activity">&times;</button>
                            </div>
                          ))}
                        </div>
                        <Button variant="ghost" className="self-start mt-1 text-accent-primary" onClick={() => openActivityModal(stop)}>+ Add an activity</Button>
                      </div>
                    </Card>
                  </div>
                ))}
              </div>
            ) : (
              <div className="py-12 text-center bg-bg-secondary rounded-xl border border-dashed border-border-color">
                <p className="text-muted text-lg mb-4">No destinations added to this trip yet.</p>
                <Button onClick={openStopModal}>Add First Destination</Button>
              </div>
            )}
          </div>

          {/* Sticky Sidebar: Budget */}
          <div>
            <div className="sticky top-24">
              <Card className="shadow-lg border-border-color">
                <h2 className="text-xl font-bold mb-6">Budget Estimate</h2>
                {budget ? (
                  <div className="flex flex-col gap-4">
                    <div className="flex justify-between items-center text-lg">
                      <span className="text-text-primary">Transport</span>
                      <span className="font-medium">${budget.transportCost ?? 0}</span>
                    </div>
                    <div className="flex justify-between items-center text-lg">
                      <span className="text-text-primary">Stays</span>
                      <span className="font-medium">${budget.stayCost ?? 0}</span>
                    </div>
                    <div className="flex justify-between items-center text-lg">
                      <span className="text-text-primary">Meals</span>
                      <span className="font-medium">${budget.mealCost ?? 0}</span>
                    </div>
                    <div className="flex justify-between items-center text-lg">
                      <span className="text-text-primary">Activities</span>
                      <span className="font-medium">${budget.activityCost ?? 0}</span>
                    </div>
                    <hr className="my-2" />
                    <div className="flex justify-between items-center text-xl font-bold">
                      <span>Total</span>
                      <span>${budget.totalCost ?? 0}</span>
                    </div>
                    {budget.budgetLimit != null && (
                      <div className="flex justify-between items-center text-sm text-muted">
                        <span>Remaining of ${budget.budgetLimit}</span>
                        <span>${budget.remainingBudget ?? 0}</span>
                      </div>
                    )}
                  </div>
                ) : (
                  <div className="text-center py-6">
                    <p className="text-muted mb-4">Budget information is not available.</p>
                  </div>
                )}
              </Card>
            </div>
          </div>
        </div>
      </div>

      {/* Add Destination Modal */}
      {showStopModal && (
        <Modal title="Add a destination" onClose={() => setShowStopModal(false)}>
          <form onSubmit={handleCitySearch} className="flex gap-2 mb-4">
            <Input
              label="Search cities"
              id="city-search"
              value={citySearch}
              onChange={(e) => setCitySearch(e.target.value)}
              placeholder="e.g. Paris"
            />
            <Button type="submit" variant="secondary" style={{ height: 'fit-content', marginTop: '1.6rem' }}>Search</Button>
          </form>

          {cityResults.length > 0 && (
            <div className="flex flex-col gap-2 mb-4" style={{ maxHeight: '200px', overflowY: 'auto' }}>
              {cityResults.map((city) => (
                <label key={city.id} className="flex items-center gap-2" style={{ cursor: 'pointer' }}>
                  <input
                    type="radio"
                    name="cityId"
                    value={city.id}
                    checked={String(stopForm.cityId) === String(city.id)}
                    onChange={(e) => setStopForm({ ...stopForm, cityId: e.target.value })}
                  />
                  {city.name}{city.country ? `, ${city.country}` : ''}
                </label>
              ))}
            </div>
          )}

          <form onSubmit={handleAddStop} className="flex flex-col gap-2">
            {stopError && <div className="badge badge-danger mb-2 w-full text-center py-2">{stopError}</div>}
            <div className="form-row">
              <Input
                label="Start date"
                id="stop-start"
                type="date"
                value={stopForm.startDate}
                onChange={(e) => setStopForm({ ...stopForm, startDate: e.target.value })}
              />
              <Input
                label="End date"
                id="stop-end"
                type="date"
                value={stopForm.endDate}
                onChange={(e) => setStopForm({ ...stopForm, endDate: e.target.value })}
              />
            </div>
            <Button type="submit" disabled={stopSubmitting} className="w-full mt-2">
              {stopSubmitting ? 'Adding...' : 'Add Destination'}
            </Button>
          </form>
        </Modal>
      )}

      {/* Add Activity Modal */}
      {activityStopId && (
        <Modal title="Add an activity" onClose={closeActivityModal}>
          <form onSubmit={handleAddActivity} className="flex flex-col gap-2">
            {activityError && <div className="badge badge-danger mb-2 w-full text-center py-2">{activityError}</div>}
            <div className="input-group">
              <label className="input-label" htmlFor="activity-select">Activity</label>
              <select
                id="activity-select"
                className="select-field"
                value={activityForm.activityId}
                onChange={(e) => {
                  const selected = activityResults.find(a => String(a.id) === e.target.value);
                  setActivityForm({
                    ...activityForm,
                    activityId: e.target.value,
                    cost: selected?.estimatedCost != null ? String(selected.estimatedCost) : activityForm.cost,
                  });
                }}
                required
              >
                <option value="">Select an activity</option>
                {activityResults.map((activity) => (
                  <option key={activity.id} value={activity.id}>
                    {activity.name}{activity.category ? ` (${activity.category})` : ''}
                  </option>
                ))}
              </select>
              {activityResults.length === 0 && (
                <p className="text-sm text-muted mt-2">No activities found for this city yet.</p>
              )}
            </div>
            <div className="form-row">
              <Input
                label="Date"
                id="activity-date"
                type="date"
                value={activityForm.dayDate}
                onChange={(e) => setActivityForm({ ...activityForm, dayDate: e.target.value })}
                required
              />
              <Input
                label="Time"
                id="activity-time"
                type="time"
                value={activityForm.scheduledTime}
                onChange={(e) => setActivityForm({ ...activityForm, scheduledTime: e.target.value })}
              />
            </div>
            <Input
              label="Cost ($)"
              id="activity-cost"
              type="number"
              min="0"
              step="0.01"
              value={activityForm.cost}
              onChange={(e) => setActivityForm({ ...activityForm, cost: e.target.value })}
            />
            <Button type="submit" disabled={activitySubmitting} className="w-full mt-2">
              {activitySubmitting ? 'Adding...' : 'Add Activity'}
            </Button>
          </form>
        </Modal>
      )}
    </div>
  );
};
