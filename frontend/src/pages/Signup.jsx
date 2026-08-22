import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { signup } from '../api/auth';
import { Card } from '../components/ui/Card';
import { Input } from '../components/ui/Input';
import { Button } from '../components/ui/Button';

export const Signup = () => {
  const [name, setName] = useState('');
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
      const data = await signup({ name, email, password });
      handleLogin(data.user);
      navigate('/dashboard');
    } catch (err) {
      setError(err.message || 'Signup failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page-wrapper animate-fade-in">
      <Card className="w-full max-w-md auth-card">
        <h2 className="auth-title">Create Account</h2>
        {error && <div className="badge badge-danger mb-4 w-full text-center py-2">{error}</div>}
        <form onSubmit={onSubmit} className="flex flex-col gap-5">
          <Input 
            label="Name" 
            type="text" 
            id="name" 
            value={name} 
            onChange={e => setName(e.target.value)} 
            required 
            placeholder="John Doe"
          />
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
            placeholder="Create a password"
          />
          <Button type="submit" disabled={loading} className="w-full mt-4 py-3 text-lg">
            {loading ? 'Creating account...' : 'Sign Up'}
          </Button>
        </form>
        <p className="text-center mt-8 text-muted text-sm">
          Already have an account? <Link to="/login" className="text-accent font-semibold ml-1">Log in</Link>
        </p>
      </Card>
    </div>
  );
};
