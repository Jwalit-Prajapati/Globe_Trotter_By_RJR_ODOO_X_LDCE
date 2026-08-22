import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { getTrips, createTrip } from '../api/trips';
import { getPopularCities } from '../api/cities';
import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';

const MOCK_TRIPS = [
  { id: 1, title: 'Summer in Paris', totalCost: '2500.00' },
  { id: 2, title: 'Tokyo Adventure', totalCost: '3200.00' },
  { id: 3, title: 'Road Trip USA', totalCost: '1500.00' }
];

const MOCK_CITIES = [
  { id: 1, name: 'Mumbai', country: 'India', image: '/images/cities/mumbai.jpeg' },
  { id: 2, name: 'Goa', country: 'India', image: '/images/cities/goa.jpeg' },
  { id: 3, name: 'Agra', country: 'India', image: '/images/cities/agra.jpeg' },
  { id: 4, name: 'Kerala', country: 'India', image: '/images/cities/kerla.jpeg' }
];

export const Dashboard = () => {
  const [trips, setTrips] = useState([]);
  const [popularCities, setPopularCities] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [tripsData, citiesData] = await Promise.all([
          getTrips().catch(() => []),
          getPopularCities().catch(() => [])
        ]);
        setTrips(tripsData && tripsData.length > 0 ? tripsData : MOCK_TRIPS);
        setPopularCities(citiesData && citiesData.length > 0 ? citiesData : MOCK_CITIES);
      } catch (err) {
        console.error('Failed to load dashboard data', err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const handleCreateTrip = async () => {
    try {
      const newTrip = await createTrip({ title: 'New Trip', startDate: new Date().toISOString() });
      setTrips([...trips, newTrip]);
    } catch (err) {
      console.error('Failed to create trip', err);
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
          <Button onClick={handleCreateTrip} className="shadow-md transition-transform hover:-translate-y-1 hover:shadow-lg">+ Plan New Trip</Button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 mb-16">
          {trips.length === 0 ? (
            <div className="col-span-full py-12 text-center bg-bg-secondary rounded-xl border border-dashed border-border-color animate-fade-in delay-400">
              <p className="text-muted text-lg mb-4">You haven't planned any trips yet.</p>
              <Button onClick={handleCreateTrip}>Start Planning</Button>
            </div>
          ) : (
            trips.map((trip, idx) => (
              <div className={`animate-slide-up delay-${(idx % 3) * 150 + 300}`} key={trip.id}>
                <Card className="flex flex-col h-full hover:shadow-xl transition-all duration-300 hover:-translate-y-2" image="https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80">
                  <h3 className="mb-2 text-xl font-bold">{trip.title}</h3>
                  <p className="text-sm text-muted mb-6 font-medium">Est. Budget: ${trip.totalCost || '0.00'}</p>
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
                src={city.image || `https://images.unsplash.com/photo-${1506012787146 + idx * 1000}?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&q=80`} 
                alt={city.name}
                className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-700"
              />
              <div className="absolute inset-0 bg-gradient-to-t from-black/70 to-transparent flex flex-col justify-end p-5 opacity-80 group-hover:opacity-100 transition-opacity duration-300">
                <h4 className="text-white text-xl font-bold mb-1 transform translate-y-2 group-hover:translate-y-0 transition-transform duration-300">{city.name}</h4>
                <p className="text-white/80 text-sm font-medium transform translate-y-4 group-hover:translate-y-0 transition-transform duration-300 opacity-0 group-hover:opacity-100">{city.country}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
