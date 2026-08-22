import { BrowserRouter as Router, Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import { Navbar } from './components/layout/Navbar';
import { Login } from './pages/Login';
import { Signup } from './pages/Signup';
import { Dashboard } from './pages/Dashboard';
import { TripDetails } from './pages/TripDetails';
import { PublicTrip } from './pages/PublicTrip';
import { Footer } from './components/layout/Footer';

const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();
  const location = useLocation();
  if (loading) return <div className="container page-wrapper text-center">Loading...</div>;
  if (!isAuthenticated) return <Navigate to="/login" replace state={{ from: location }} />;
  return children;
};

function AppRoutes() {
  return (
    <>
      <Navbar />
      <Routes>
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        
        {/* Protected Routes */}
        <Route path="/dashboard" element={
          <ProtectedRoute>
            <Dashboard />
          </ProtectedRoute>
        } />
        <Route path="/trips/:id" element={
          <ProtectedRoute>
            <TripDetails />
          </ProtectedRoute>
        } />

        {/* Public Routes */}
        <Route path="/public/:slug" element={<PublicTrip />} />
      </Routes>
      <Footer />
    </>
  );
}

function App() {
  return (
    <AuthProvider>
      <Router>
        <AppRoutes />
      </Router>
    </AuthProvider>
  );
}

export default App;
