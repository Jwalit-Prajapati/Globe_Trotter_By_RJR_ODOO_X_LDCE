CREATE TABLE activities (
    id                BIGSERIAL PRIMARY KEY,
    city_id           BIGINT NOT NULL REFERENCES cities (id),
    name              VARCHAR(255) NOT NULL,
    category          VARCHAR(255),
    duration_minutes  INTEGER,
    estimated_cost    NUMERIC(12, 2),
    description       VARCHAR(2000),
    image_url         VARCHAR(255)
);

CREATE INDEX idx_activities_city_id ON activities (city_id);
