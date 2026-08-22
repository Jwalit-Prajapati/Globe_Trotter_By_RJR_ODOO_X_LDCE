CREATE TABLE stops (
    id             BIGSERIAL PRIMARY KEY,
    trip_id        BIGINT NOT NULL REFERENCES trips (id),
    city_id        BIGINT NOT NULL REFERENCES cities (id),
    start_date     DATE,
    end_date       DATE,
    order_index    INTEGER,
    transport_cost NUMERIC(12, 2),
    stay_cost      NUMERIC(12, 2),
    meal_cost      NUMERIC(12, 2)
);

CREATE INDEX idx_stops_trip_id ON stops (trip_id);
CREATE INDEX idx_stops_city_id ON stops (city_id);
