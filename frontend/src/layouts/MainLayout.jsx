import { Outlet, Link } from 'react-router-dom';
import { Plane, Compass, CalendarDays, Map, Settings, UserCircle } from 'lucide-react';

export default function MainLayout() {
  return (
    <div className="flex h-screen bg-gray-50">
      {/* Sidebar */}
      <aside className="w-64 bg-white border-r border-gray-200 flex flex-col">
        <div className="p-6 flex items-center gap-3">
          <Plane className="w-8 h-8 text-primary" />
          <span className="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-primary to-blue-600">
            GlobeTrotter
          </span>
        </div>
        
        <nav className="flex-1 px-4 py-4 space-y-2">
          <Link to="/" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-lg hover:bg-gray-100 transition-colors">
            <Compass className="w-5 h-5" />
            Dashboard
          </Link>
          <Link to="/trips" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-lg hover:bg-gray-100 transition-colors">
            <Map className="w-5 h-5" />
            My Trips
          </Link>
          <Link to="/calendar" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-lg hover:bg-gray-100 transition-colors">
            <CalendarDays className="w-5 h-5" />
            Calendar
          </Link>
          <Link to="/admin" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-lg hover:bg-gray-100 transition-colors">
            <Settings className="w-5 h-5" />
            Admin
          </Link>
        </nav>
        
        <div className="p-4 border-t border-gray-200">
          <Link to="/profile" className="flex items-center gap-3 px-3 py-2 text-gray-700 rounded-lg hover:bg-gray-100 transition-colors">
            <UserCircle className="w-5 h-5" />
            Profile
          </Link>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 overflow-auto">
        <Outlet />
      </main>
    </div>
  );
}
