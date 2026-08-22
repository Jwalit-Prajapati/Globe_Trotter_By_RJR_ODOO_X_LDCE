CREATE TABLE stop_activities (
    id              BIGSERIAL PRIMARY KEY,
    stop_id         BIGINT NOT NULL REFERENCES stops (id),
    activity_id     BIGINT NOT NULL REFERENCES activities (id),
    day_date        DATE NOT NULL,
    scheduled_time  TIME,
    cost            NUMERIC(12, 2) NOT NULL
);

CREATE INDEX idx_stop_activities_stop_id ON stop_activities (stop_id);
CREATE INDEX idx_stop_activities_activity_id ON stop_activities (activity_id);
