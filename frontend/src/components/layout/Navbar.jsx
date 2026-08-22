import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './Navbar.css';

export const Navbar = () => {
  const { isAuthenticated, handleLogout } = useAuth();
  const navigate = useNavigate();

  const onLogout = () => {
    handleLogout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="container navbar-container">
        {/* Logo */}
        <Link to="/" className="navbar-logo">
          <svg viewBox="0 0 32 32" xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="presentation" focusable="false" style={{ display: 'block', height: '32px', width: '32px', fill: 'currentColor' }}>
            <path d="M16 1c2.008 0 3.463.963 4.751 3.269l.533 1.025c1.954 3.83 6.114 12.54 7.1 14.836l.145.353c.667 1.591.91 2.472.96 3.396l.011.415.001.228c0 4.062-2.877 6.478-6.357 6.478-2.224 0-4.556-1.258-6.709-3.386l-.257-.26-.172-.179h-.011l-.176.185c-2.044 2.1-4.267 3.42-6.414 3.615l-.28.019-.267.006C5.377 31 2.5 28.584 2.5 24.522l.005-.469c.026-.928.23-1.768.83-3.244l.216-.524c.966-2.298 5.038-10.898 6.964-14.7l.564-1.077C12.443 2.052 13.928 1 16 1zm0 2c-1.239 0-2.053.539-2.987 2.21l-.523 1.008c-1.926 3.776-6.06 12.43-7.031 14.736l-.226.544c-.473 1.152-.66 1.802-.683 2.479l-.004.226C4.5 27.164 6.34 29 8.856 29c1.933 0 3.892-1.15 5.86-3.084l.325-.333.5-.544.5.544c1.986 1.954 3.972 3.123 5.922 3.085l.261-.013c2.424-.187 4.276-2.072 4.276-4.133l-.005-.27c-.035-.747-.234-1.488-.8-2.883l-.146-.354c-.958-2.247-5.072-10.87-7.007-14.67l-.548-1.054C17.957 3.636 17.18 3 16 3z"></path>
          </svg>
          <span className="hidden md:inline">GlobeTrotter</span>
        </Link>

        {/* Center Search Pill */}
        <div className="navbar-search hidden md:flex">
          <div className="search-pill">
            <span className="search-text">Anywhere</span>
            <span className="search-divider"></span>
            <span className="search-text">Any week</span>
            <span className="search-divider"></span>
            <span className="search-text text-muted font-normal">Add guests</span>
            <div className="search-icon-btn">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
            </div>
          </div>
        </div>

        {/* Right Menu */}
        <div className="navbar-links">
          {isAuthenticated ? (
            <>
              <Link to="/dashboard" className="nav-link hidden md:inline">Dashboard</Link>
              <div className="profile-menu">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="mr-2"><line x1="3" y1="12" x2="21" y2="12"></line><line x1="3" y1="6" x2="21" y2="6"></line><line x1="3" y1="18" x2="21" y2="18"></line></svg>
                <div className="avatar-circle">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="var(--text-muted)" stroke="none"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>
                </div>
              </div>
              <button onClick={onLogout} className="btn-ghost ml-2 text-sm">Logout</button>
            </>
          ) : (
            <>
              <Link to="/login" className="nav-link">Log in</Link>
              <Link to="/signup" className="btn btn-primary ml-2 rounded-full px-6">Sign Up</Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};
