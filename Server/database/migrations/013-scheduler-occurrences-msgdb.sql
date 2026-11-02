BEGIN;
SELECT pg_advisory_xact_lock(427042,13);
CREATE SCHEMA IF NOT EXISTS etlforge_cluster;
CREATE TABLE IF NOT EXISTS etlforge_cluster.schedule_occurrence (
 schedule_id integer NOT NULL,
 operation_id integer NOT NULL,
 scheduled_local timestamp NOT NULL,
 outcome text NOT NULL CHECK(outcome IN ('enqueued','skipped_overlap')),
 recorded_at timestamptz NOT NULL DEFAULT now(),
 PRIMARY KEY(schedule_id,operation_id,scheduled_local)
);
CREATE INDEX IF NOT EXISTS schedule_occurrence_recorded_idx ON etlforge_cluster.schedule_occurrence(recorded_at);
COMMIT;
