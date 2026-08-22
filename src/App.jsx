import { BrowserRouter, Routes, Route } from 'react-router-dom';
import MainLayout from './layouts/MainLayout';
import Login from './pages/Login';
import Signup from './pages/Signup';
import Dashboard from './pages/Dashboard';
import CreateTrip from './pages/CreateTrip';
import TripList from './pages/TripList';
import ItineraryBuilder from './pages/ItineraryBuilder';
import ItineraryView from './pages/ItineraryView';
import CitySearch from './pages/CitySearch';
import ActivitySearch from './pages/ActivitySearch';
import TripBudget from './pages/TripBudget';
import TripCalendar from './pages/TripCalendar';
import SharedItinerary from './pages/SharedItinerary';
import Profile from './pages/Profile';
import AdminDashboard from './pages/AdminDashboard';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public Routes */}
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/shared/:id" element={<SharedItinerary />} />
        
        {/* Protected Routes inside MainLayout */}
        <Route path="/" element={<MainLayout />}>
          <Route index element={<Dashboard />} />
          <Route path="trips" element={<TripList />} />
          <Route path="trips/create" element={<CreateTrip />} />
          <Route path="trips/:id/build" element={<ItineraryBuilder />} />
          <Route path="trips/:id/view" element={<ItineraryView />} />
          <Route path="trips/:id/budget" element={<TripBudget />} />
          <Route path="cities" element={<CitySearch />} />
          <Route path="activities" element={<ActivitySearch />} />
          <Route path="calendar" element={<TripCalendar />} />
          <Route path="profile" element={<Profile />} />
          <Route path="admin" element={<AdminDashboard />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
