import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { login } from '../api/auth';
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
      <div className="split-image-side"></div>
    </div>
  );
};
