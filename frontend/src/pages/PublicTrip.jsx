import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { getPublicTrip } from '../api/trips';
import { Card } from '../components/ui/Card';

export const PublicTrip = () => {
  const { slug } = useParams();
  const [trip, setTrip] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTrip = async () => {
      try {
        const data = await getPublicTrip(slug);
        setTrip(data);
      } catch (err) {
        console.error('Failed to load public trip', err);
        setError('Trip not found or not public.');
      } finally {
        setLoading(false);
      }
    };
    fetchTrip();
  }, [slug]);

  if (loading) return <div className="container page-wrapper text-center">Loading...</div>;
  if (error) return <div className="container page-wrapper text-center text-danger">{error}</div>;
  if (!trip) return <div className="container page-wrapper text-center">Trip not found</div>;

  return (
    <div className="animate-fade-in pb-16">
      {/* Public Hero Section */}
      <div className="relative h-96 flex items-center justify-center text-white" style={{
        backgroundImage: "linear-gradient(rgba(0,0,0,0.3), rgba(0,0,0,0.6)), url('https://images.unsplash.com/photo-1488085061387-422e29b40080?ixlib=rb-4.0.3&auto=format&fit=crop&w=2000&q=80')",
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundAttachment: 'fixed'
      }}>
        <div className="text-center z-10 px-4">
          <span className="badge bg-white/20 text-white border-none mb-4 uppercase tracking-widest backdrop-blur-md">Shared Itinerary</span>
          <h1 className="text-5xl md:text-6xl font-bold mb-4 drop-shadow-lg text-white">{trip.title}</h1>
          <p className="text-xl md:text-2xl font-medium drop-shadow-md text-white/90">A curated journey planned with GlobeTrotter</p>
        </div>
      </div>

      <div className="container mt-12">
        <div className="text-center mb-12 max-w-2xl mx-auto">
          <h2 className="text-3xl font-bold mb-4">The Route</h2>
          <p className="text-muted text-lg">Explore the incredible destinations and activities planned for this adventure.</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {trip.stops && trip.stops.map((stop, idx) => (
            <Card key={idx} className="flex flex-col h-full border-none shadow-lg hover:shadow-xl transition-shadow duration-300" image={`https://images.unsplash.com/photo-${1506012787146 + idx * 1000}?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80`}>
              <div className="flex justify-between items-start mb-4">
                <h3 className="text-2xl font-bold text-text-primary">{stop.city}</h3>
                <span className="badge badge-accent bg-accent-primary/10 text-accent-primary border-none">{stop.dates}</span>
              </div>
              
              <div className="flex flex-col gap-3 mt-4">
                <h4 className="text-sm font-semibold text-muted uppercase tracking-wider flex items-center gap-2">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline></svg>
                  Highlights
                </h4>
                {stop.activities && stop.activities.length > 0 ? (
                  <div className="flex flex-wrap gap-2">
                    {stop.activities.map((act, i) => (
                      <span key={i} className="px-3 py-1 bg-bg-secondary text-text-secondary rounded-full text-sm font-medium">
                        {act.name}
                      </span>
                    ))}
                  </div>
                ) : (
                  <p className="text-sm text-muted italic">Exploring the city...</p>
                )}
              </div>
            </Card>
          ))}
        </div>
        
        {(!trip.stops || trip.stops.length === 0) && (
          <div className="text-center py-12 bg-bg-secondary rounded-xl border border-dashed border-border-color">
            <p className="text-muted text-lg">This itinerary doesn't have any destinations yet.</p>
          </div>
        )}
        
        <div className="mt-20 text-center">
          <p className="text-muted mb-4">Want to plan a trip like this?</p>
          <a href="/" className="btn btn-primary rounded-full px-8 py-3 text-lg">Start Planning Your Own</a>
        </div>
      </div>
    </div>
  );
};
