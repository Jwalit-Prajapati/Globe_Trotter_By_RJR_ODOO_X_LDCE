import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getTripById, deleteTrip, getTripBudget, shareTrip } from '../api/trips';
import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';

export const TripDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [trip, setTrip] = useState(null);
  const [budget, setBudget] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchTripDetails = async () => {
      try {
        const [tripData, budgetData] = await Promise.all([
          getTripById(id),
          getTripBudget(id).catch(() => null)
        ]);
        setTrip(tripData);
        setBudget(budgetData);
      } catch (err) {
        console.error('Failed to load trip', err);
      } finally {
        setLoading(false);
      }
    };
    fetchTripDetails();
  }, [id]);

  const handleDelete = async () => {
    if (window.confirm('Are you sure you want to delete this trip?')) {
      try {
        await deleteTrip(id);
        navigate('/dashboard');
      } catch (err) {
        console.error('Failed to delete', err);
      }
    }
  };

  const handleShare = async () => {
    try {
      const data = await shareTrip(id);
      alert(`Trip shared! Link: /public/${data.slug}`);
    } catch (err) {
      console.error('Failed to share', err);
    }
  };

  if (loading) return <div className="container page-wrapper text-center">Loading...</div>;
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
            <h1 className="text-4xl font-bold mb-2">{trip.title}</h1>
            <p className="text-muted text-lg font-medium flex items-center gap-2">
              <span className="w-2 h-2 rounded-full bg-success inline-block"></span> Planning Phase
            </p>
          </div>
          <div className="flex gap-3">
            <Button variant="secondary" onClick={handleShare} className="shadow-sm">Share Trip</Button>
            <Button variant="danger" onClick={handleDelete} className="shadow-sm">Delete</Button>
          </div>
        </div>

        <hr />

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-12 mt-8">
          {/* Main Content: Itinerary */}
          <div className="lg:col-span-2 flex flex-col gap-8">
            <h2 className="text-2xl font-bold">Your Itinerary</h2>
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
                        <h3 className="text-xl font-bold">{stop.city}</h3>
                        <span className="badge font-medium">{stop.dates}</span>
                      </div>
                      <div className="flex flex-col gap-3">
                        <h4 className="text-sm font-semibold text-muted uppercase tracking-wider">Planned Activities</h4>
                        <div className="flex flex-wrap gap-2">
                          {stop.activities && stop.activities.map(act => (
                            <div key={act.id} className="px-3 py-1.5 bg-bg-secondary border border-border-color rounded-md text-sm font-medium">
                              {act.name}
                            </div>
                          ))}
                        </div>
                        <Button variant="ghost" className="self-start mt-1 text-accent-primary">+ Add an activity</Button>
                      </div>
                    </Card>
                  </div>
                ))}
              </div>
            ) : (
              <div className="py-12 text-center bg-bg-secondary rounded-xl border border-dashed border-border-color">
                <p className="text-muted text-lg mb-4">No destinations added to this trip yet.</p>
                <Button>Add First Destination</Button>
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
                      <span className="text-text-primary underline cursor-pointer">Transport</span>
                      <span className="font-medium">${budget.transport}</span>
                    </div>
                    <div className="flex justify-between items-center text-lg">
                      <span className="text-text-primary underline cursor-pointer">Stays</span>
                      <span className="font-medium">${budget.stay}</span>
                    </div>
                    <div className="flex justify-between items-center text-lg">
                      <span className="text-text-primary underline cursor-pointer">Meals & Activities</span>
                      <span className="font-medium">${budget.meals}</span>
                    </div>
                    <hr className="my-2" />
                    <div className="flex justify-between items-center text-xl font-bold">
                      <span>Total</span>
                      <span>${budget.total}</span>
                    </div>
                    <Button className="w-full mt-4 py-3 text-lg">Update Costs</Button>
                  </div>
                ) : (
                  <div className="text-center py-6">
                    <p className="text-muted mb-4">Budget information is not available.</p>
                    <Button variant="secondary" className="w-full">Set Budget</Button>
                  </div>
                )}
              </Card>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
