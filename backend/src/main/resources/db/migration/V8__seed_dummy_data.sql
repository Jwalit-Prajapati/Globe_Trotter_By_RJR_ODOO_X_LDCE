-- Dummy/demo data for local development.
-- Demo login: demo@globetrotter.com / password123

INSERT INTO users (name, email, password_hash, role, created_at) VALUES
    ('Demo Traveler', 'demo@globetrotter.com', '$2b$10$KGJPa22M.QCZk/XN.pp.EeMsqY79kbcGwfIW6ARKrMbjw.wsIbhh2', 'USER', now());

INSERT INTO cities (name, country, region, cost_index, popularity, image_url) VALUES
    ('Ahmedabad',  'India', 'Gujarat',      45, 70, '/images/cities/ahmedabad.jpeg'),
    ('Baroda',     'India', 'Gujarat',      40, 55, '/images/cities/baroda.jpeg'),
    ('Kutch',      'India', 'Gujarat',      35, 65, '/images/cities/kutch.jpeg'),
    ('Goa',        'India', 'Goa',          60, 95, '/images/cities/goa.jpeg'),
    ('Mount Abu',  'India', 'Rajasthan',    40, 60, '/images/cities/mountabu.jpeg'),
    ('Rajasthan',  'India', 'Rajasthan',    50, 85, '/images/cities/rajasthan.jpeg'),
    ('Mumbai',     'India', 'Maharashtra',  70, 90, '/images/cities/mumbai.jpeg'),
    ('Agra',       'India', 'Uttar Pradesh',45, 88, '/images/cities/agra.jpeg'),
    ('Kerala',     'India', 'Kerala',       55, 92, '/images/cities/kerala.jpeg'),
    ('Tirupati',   'India', 'Andhra Pradesh',40, 75, '/images/cities/tirupati.jpeg'),
    ('Leh Ladakh', 'India', 'Ladakh',       65, 80, '/images/cities/lehladakh.jpeg');

INSERT INTO activities (city_id, name, category, duration_minutes, estimated_cost, description, image_url)
SELECT c.id, a.name, a.category, a.duration_minutes, a.estimated_cost, a.description, c.image_url
FROM cities c
JOIN (VALUES
    ('City Sightseeing Tour', 'Sightseeing', 180, 800.00,  'Guided tour of the city''s must-see landmarks.'),
    ('Local Cuisine Walk',    'Food',        120, 500.00,  'Sample the best local street food and specialties.')
) AS a(name, category, duration_minutes, estimated_cost, description) ON TRUE
WHERE c.name IN ('Ahmedabad', 'Baroda', 'Kutch', 'Goa', 'Mount Abu', 'Rajasthan', 'Mumbai', 'Agra', 'Kerala', 'Tirupati', 'Leh Ladakh');

INSERT INTO trips (user_id, name, description, start_date, end_date, budget_limit, is_public, created_at)
SELECT u.id, 'Gujarat & Rajasthan Getaway', 'A demo trip covering Ahmedabad, Mount Abu and Goa.', DATE '2026-10-01', DATE '2026-10-08', 25000.00, FALSE, now()
FROM users u WHERE u.email = 'demo@globetrotter.com';

INSERT INTO stops (trip_id, city_id, start_date, end_date, order_index, transport_cost, stay_cost, meal_cost)
SELECT t.id, c.id, s.start_date, s.end_date, s.order_index, s.transport_cost, s.stay_cost, s.meal_cost
FROM trips t
JOIN (VALUES
    ('Ahmedabad', DATE '2026-10-01', DATE '2026-10-03', 1, 1500.00, 3000.00, 1200.00),
    ('Mount Abu',  DATE '2026-10-03', DATE '2026-10-05', 2, 1200.00, 3500.00, 1000.00),
    ('Goa',        DATE '2026-10-05', DATE '2026-10-08', 3, 4000.00, 6000.00, 2500.00)
) AS s(city_name, start_date, end_date, order_index, transport_cost, stay_cost, meal_cost) ON TRUE
JOIN cities c ON c.name = s.city_name
WHERE t.name = 'Gujarat & Rajasthan Getaway';

INSERT INTO stop_activities (stop_id, activity_id, day_date, scheduled_time, cost)
SELECT st.id, act.id, st.start_date, TIME '10:00:00', act.estimated_cost
FROM stops st
JOIN trips t ON t.id = st.trip_id AND t.name = 'Gujarat & Rajasthan Getaway'
JOIN activities act ON act.city_id = st.city_id AND act.name = 'City Sightseeing Tour';
