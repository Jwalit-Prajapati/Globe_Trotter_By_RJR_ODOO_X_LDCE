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
    <div className="container page-wrapper animate-fade-in">
      <div className="text-center mb-12">
        <h1 className="mb-2">{trip.title}</h1>
        <p className="text-muted text-lg">A journey planned with GlobeTrotter</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {trip.stops && trip.stops.map((stop, idx) => (
          <Card key={idx} className="flex flex-col">
            <h3 className="mb-1 text-accent-primary">{stop.city}</h3>
            <p className="text-sm text-muted mb-4">{stop.dates}</p>
            <div className="flex flex-col gap-2 mt-auto">
              <h4 className="text-sm font-semibold">Planned Activities:</h4>
              <ul className="list-disc pl-5 text-sm text-text-secondary">
                {stop.activities && stop.activities.map((act, i) => (
                  <li key={i}>{act.name}</li>
                ))}
              </ul>
            </div>
          </Card>
        ))}
      </div>
    </div>
  );
};
