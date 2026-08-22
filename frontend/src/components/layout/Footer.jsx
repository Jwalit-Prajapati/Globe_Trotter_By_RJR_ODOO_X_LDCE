import { Link } from 'react-router-dom';
import './Footer.css';

export const Footer = () => {
  return (
    <footer className="footer">
      <div className="container">
        <div className="footer-grid">
          <div className="footer-brand">
            <Link to="/" className="footer-logo">
              GlobeTrotter
            </Link>
            <p className="footer-desc">
              Your ultimate travel companion. Plan, budget, and explore the world with ease and elegance.
            </p>
          </div>
          
          <div className="footer-links-group">
            <h4 className="footer-heading">Support</h4>
            <ul className="footer-links">
              <li><Link to="#">Help Center</Link></li>
              <li><Link to="#">Safety Information</Link></li>
              <li><Link to="#">Cancellation Options</Link></li>
            </ul>
          </div>
          
          <div className="footer-links-group">
            <h4 className="footer-heading">Company</h4>
            <ul className="footer-links">
              <li><Link to="#">About Us</Link></li>
              <li><Link to="#">Careers</Link></li>
              <li><Link to="#">Investors</Link></li>
            </ul>
          </div>

          <div className="footer-links-group">
            <h4 className="footer-heading">Discover</h4>
            <ul className="footer-links">
              <li><Link to="#">Destinations</Link></li>
              <li><Link to="#">Travel Guides</Link></li>
              <li><Link to="#">Community</Link></li>
            </ul>
          </div>
        </div>
        
        <div className="footer-bottom">
          <p>&copy; {new Date().getFullYear()} GlobeTrotter, Inc. All rights reserved.</p>
          <div className="footer-bottom-links">
            <Link to="#">Privacy</Link>
            <span className="dot">•</span>
            <Link to="#">Terms</Link>
            <span className="dot">•</span>
            <Link to="#">Sitemap</Link>
          </div>
        </div>
      </div>
    </footer>
  );
};
