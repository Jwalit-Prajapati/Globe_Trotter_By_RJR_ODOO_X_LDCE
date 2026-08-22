CREATE TABLE trips (
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT NOT NULL REFERENCES users (id),
    name         VARCHAR(255) NOT NULL,
    description  VARCHAR(2000),
    start_date   DATE,
    end_date     DATE,
    budget_limit NUMERIC(12, 2),
    public_slug  VARCHAR(255) UNIQUE,
    is_public    BOOLEAN NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMP
);

CREATE INDEX idx_trips_user_id ON trips (user_id);
