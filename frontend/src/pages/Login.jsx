import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { login } from '../api/auth';
import { setAuthToken } from '../api/client';
import { Input } from '../components/ui/Input';
import { Button } from '../components/ui/Button';

export const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  
  const { handleLogin } = useAuth();
  const navigate = useNavigate();

  const onSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      // Hardcoded test user bypass
      if (email === 'test@test.com' && password === 'password') {
        setAuthToken('fake-jwt-token-for-testing');
        handleLogin({ id: 1, name: 'Test User', email: 'test@test.com' });
        navigate('/dashboard');
        return;
      }

      const data = await login({ email, password });
      handleLogin(data.user);
      navigate('/dashboard');
    } catch (err) {
      setError(err.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="split-layout animate-fade-in">
      <div className="split-form-side">
        <div className="auth-form-container">
          <h2 className="auth-title">Welcome Back</h2>
          {error && <div className="badge badge-danger mb-4 w-full text-center py-2">{error}</div>}
          <form onSubmit={onSubmit} className="flex flex-col gap-5">
            <Input 
              label="Email" 
              type="email" 
              id="email" 
              value={email} 
              onChange={e => setEmail(e.target.value)} 
              required 
              placeholder="Enter your email"
            />
            <Input 
              label="Password" 
              type="password" 
              id="password" 
              value={password} 
              onChange={e => setPassword(e.target.value)} 
              required 
              placeholder="Enter your password"
            />
            <Button type="submit" disabled={loading} className="w-full mt-4 py-3 text-lg">
              {loading ? 'Logging in...' : 'Log In'}
            </Button>
          </form>
          <p className="text-center mt-8 text-muted text-sm">
            Don't have an account? <Link to="/signup" className="text-accent font-semibold ml-1">Sign up</Link>
          </p>
        </div>
      </div>
      <div className="split-image-side">
        <div className="slideshow-bg" style={{backgroundImage: "url('https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80')"}}></div>
        <div className="slideshow-bg" style={{backgroundImage: "url('https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80')"}}></div>
        <div className="slideshow-bg" style={{backgroundImage: "url('https://images.unsplash.com/photo-1504280741562-fd0e12a7cea7?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80')"}}></div>
      </div>
    </div>
  );
};
