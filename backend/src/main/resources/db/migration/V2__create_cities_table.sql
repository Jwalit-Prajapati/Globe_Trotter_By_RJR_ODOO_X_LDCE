CREATE TABLE cities (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    country    VARCHAR(255),
    region     VARCHAR(255),
    cost_index INTEGER,
    popularity INTEGER,
    image_url  VARCHAR(255)
);
