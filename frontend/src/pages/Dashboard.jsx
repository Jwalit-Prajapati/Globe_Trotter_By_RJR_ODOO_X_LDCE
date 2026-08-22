import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getTrips, createTrip } from '../api/trips';
import { getPopularCities } from '../api/cities';
import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Input } from '../components/ui/Input';
import { Modal } from '../components/ui/Modal';

const emptyTripForm = { name: '', description: '', startDate: '', endDate: '', budgetLimit: '' };

export const Dashboard = () => {
  const [trips, setTrips] = useState([]);
  const [popularCities, setPopularCities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [loadError, setLoadError] = useState('');

  const [showCreateModal, setShowCreateModal] = useState(false);
  const [tripForm, setTripForm] = useState(emptyTripForm);
  const [creating, setCreating] = useState(false);
  const [createError, setCreateError] = useState('');

  const navigate = useNavigate();

  const loadDashboard = async () => {
    setLoading(true);
    setLoadError('');
    try {
      const [tripsData, citiesData] = await Promise.all([
        getTrips(),
        getPopularCities(),
      ]);
      setTrips(tripsData || []);
      setPopularCities(citiesData || []);
    } catch (err) {
      console.error('Failed to load dashboard data', err);
      setLoadError(err.message || 'Failed to load your trips.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect -- initial data load on mount
    loadDashboard();
  }, []);

  const openCreateModal = () => {
    setTripForm(emptyTripForm);
    setCreateError('');
    setShowCreateModal(true);
  };

  const handleCreateTrip = async (e) => {
    e.preventDefault();
    setCreating(true);
    setCreateError('');
    try {
      const payload = {
        name: tripForm.name,
        description: tripForm.description || undefined,
        startDate: tripForm.startDate || undefined,
        endDate: tripForm.endDate || undefined,
        budgetLimit: tripForm.budgetLimit ? Number(tripForm.budgetLimit) : undefined,
      };
      const newTrip = await createTrip(payload);
      setShowCreateModal(false);
      navigate(`/trips/${newTrip.id}`);
    } catch (err) {
      setCreateError(err.message || 'Failed to create trip');
    } finally {
      setCreating(false);
    }
  };

  if (loading) return <div className="container page-wrapper text-center">Loading...</div>;

  return (
    <div className="animate-fade-in">
      {/* Hero Section */}
      <div className="relative h-80 mb-12 flex items-center justify-center text-white animate-scale-in" style={{
        backgroundImage: "linear-gradient(rgba(0,0,0,0.2), rgba(0,0,0,0.5)), url('https://images.unsplash.com/photo-1507525428034-b723cf961d3e?ixlib=rb-4.0.3&auto=format&fit=crop&w=2000&q=80')",
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      }}>
        <div className="text-center z-10 animate-slide-up delay-150">
          <h1 className="text-4xl md:text-5xl font-bold mb-4 text-white drop-shadow-md">Where to next?</h1>
          <p className="text-lg md:text-xl font-medium drop-shadow-sm opacity-90">Plan your perfect getaway with GlobeTrotter</p>
        </div>
      </div>

      <div className="container page-wrapper pt-0 animate-slide-up delay-300">
        <div className="flex justify-between items-center mb-8">
          <h2 className="text-3xl font-bold">Your Upcoming Trips</h2>
          <Button onClick={openCreateModal} className="shadow-md transition-transform hover:-translate-y-1 hover:shadow-lg">+ Plan New Trip</Button>
        </div>

        {loadError && <div className="badge badge-danger mb-4">{loadError}</div>}

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 mb-16">
          {trips.length === 0 ? (
            <div className="col-span-full py-12 text-center bg-bg-secondary rounded-xl border border-dashed border-border-color animate-fade-in delay-400">
              <p className="text-muted text-lg mb-4">You haven't planned any trips yet.</p>
              <Button onClick={openCreateModal}>Start Planning</Button>
            </div>
          ) : (
            trips.map((trip, idx) => (
              <div className={`animate-slide-up delay-${(idx % 3) * 150 + 300}`} key={trip.id}>
                <Card className="flex flex-col h-full hover:shadow-xl transition-all duration-300 hover:-translate-y-2" image="https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80">
                  <h3 className="mb-2 text-xl font-bold">{trip.name}</h3>
                  <p className="text-sm text-muted mb-2 font-medium">
                    {trip.startDate && trip.endDate ? `${trip.startDate} – ${trip.endDate}` : 'Dates not set'}
                  </p>
                  <p className="text-sm text-muted mb-6 font-medium">
                    Budget: {trip.budgetLimit != null ? `$${trip.budgetLimit}` : 'Not set'} · {trip.stopCount || 0} stop{trip.stopCount === 1 ? '' : 's'}
                  </p>
                  <div className="mt-auto">
                    <Link to={`/trips/${trip.id}`} className="btn btn-secondary w-full text-center hover:bg-bg-tertiary">View Itinerary</Link>
                  </div>
                </Card>
              </div>
            ))
          )}
        </div>

        <h2 className="text-3xl font-bold mb-8 animate-slide-up delay-400">Explore Popular Destinations</h2>
        <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
          {popularCities.map((city, idx) => (
            <div key={city.id} className={`relative rounded-xl overflow-hidden shadow-sm hover:shadow-lg transition-shadow cursor-pointer group h-64 animate-slide-up delay-${(idx % 4) * 75 + 400}`}>
              <img
                src={city.imageUrl || `https://images.unsplash.com/photo-${1506012787146 + idx * 1000}?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&q=80`}
                alt={city.name}
                className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-700"
              />
              <div className="absolute inset-0 bg-gradient-to-t from-black/70 to-transparent flex flex-col justify-end p-5 opacity-80 group-hover:opacity-100 transition-opacity duration-300">
                <h4 className="text-white text-xl font-bold mb-1 transform translate-y-2 group-hover:translate-y-0 transition-transform duration-300">{city.name}</h4>
                <p className="text-white/80 text-sm font-medium transform translate-y-4 group-hover:translate-y-0 transition-transform duration-300 opacity-0 group-hover:opacity-100">{city.country}</p>
              </div>
            </div>
          ))}
          {popularCities.length === 0 && (
            <p className="text-muted col-span-full text-center py-8">No popular destinations yet.</p>
          )}
        </div>
      </div>

      {showCreateModal && (
        <Modal title="Plan a new trip" onClose={() => setShowCreateModal(false)}>
          <form onSubmit={handleCreateTrip} className="flex flex-col gap-2">
            {createError && <div className="badge badge-danger mb-2 w-full text-center py-2">{createError}</div>}
            <Input
              label="Trip name"
              id="trip-name"
              value={tripForm.name}
              onChange={(e) => setTripForm({ ...tripForm, name: e.target.value })}
              required
              placeholder="Summer in Paris"
            />
            <Input
              label="Description"
              id="trip-description"
              value={tripForm.description}
              onChange={(e) => setTripForm({ ...tripForm, description: e.target.value })}
              placeholder="Optional"
            />
            <div className="form-row">
              <Input
                label="Start date"
                id="trip-start"
                type="date"
                value={tripForm.startDate}
                onChange={(e) => setTripForm({ ...tripForm, startDate: e.target.value })}
              />
              <Input
                label="End date"
                id="trip-end"
                type="date"
                value={tripForm.endDate}
                onChange={(e) => setTripForm({ ...tripForm, endDate: e.target.value })}
              />
            </div>
            <Input
              label="Budget limit ($)"
              id="trip-budget"
              type="number"
              min="0"
              step="0.01"
              value={tripForm.budgetLimit}
              onChange={(e) => setTripForm({ ...tripForm, budgetLimit: e.target.value })}
              placeholder="Optional"
            />
            <Button type="submit" disabled={creating} className="w-full mt-2">
              {creating ? 'Creating...' : 'Create Trip'}
            </Button>
          </form>
        </Modal>
      )}
    </div>
  );
};
