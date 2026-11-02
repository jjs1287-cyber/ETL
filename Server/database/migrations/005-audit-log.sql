BEGIN;
CREATE SCHEMA IF NOT EXISTS audit;
REVOKE ALL ON SCHEMA audit FROM PUBLIC;
CREATE TABLE IF NOT EXISTS audit.schema_version (
    version integer PRIMARY KEY,
    installed_at timestamptz NOT NULL DEFAULT clock_timestamp()
);
CREATE TABLE IF NOT EXISTS audit.settings (
    singleton boolean PRIMARY KEY DEFAULT true CHECK (singleton),
    detail_days integer NOT NULL DEFAULT 30 CHECK (detail_days BETWEEN 1 AND 36500),
    header_days integer NOT NULL DEFAULT 365 CHECK (header_days BETWEEN 1 AND 36500),
    retention_enabled boolean NOT NULL DEFAULT false,
    updated_at timestamptz NOT NULL DEFAULT clock_timestamp(),
    updated_by text NOT NULL,
    CHECK (header_days >= detail_days)
);
INSERT INTO audit.settings(singleton,updated_by) VALUES(true,'installation') ON CONFLICT DO NOTHING;
CREATE TABLE IF NOT EXISTS audit.project_baseline (
    project_guid uuid PRIMARY KEY,
    normalization_version text NOT NULL,
    key_version text NOT NULL,
    established_at timestamptz NOT NULL DEFAULT clock_timestamp()
);
CREATE TABLE IF NOT EXISTS audit.object_state (
    project_guid uuid NOT NULL,
    object_type integer NOT NULL CHECK (object_type > 0),
    object_guid uuid NOT NULL,
    object_name text NOT NULL,
    normalization_version text NOT NULL,
    key_version text NOT NULL,
    fingerprint varchar(64) NOT NULL CHECK (fingerprint ~ '^[0-9a-f]{64}$'),
    protected_snapshot jsonb NOT NULL,
    visible_snapshot jsonb NOT NULL,
    revision bigint NOT NULL CHECK (revision > 0),
    is_deleted boolean NOT NULL DEFAULT false,
    PRIMARY KEY(project_guid,object_type,object_guid)
);
CREATE TABLE IF NOT EXISTS audit.event (
    event_id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    deployment_id uuid NOT NULL,
    project_guid uuid NOT NULL,
    project_name text NOT NULL,
    object_guid uuid NOT NULL,
    object_type integer NOT NULL CHECK (object_type > 0),
    object_type_name text NOT NULL,
    object_name text NOT NULL,
    action text NOT NULL CHECK (action IN ('Created','Updated','Deleted')),
    user_id bigint NOT NULL,
    user_name text NOT NULL,
    deployed_at timestamptz NOT NULL DEFAULT clock_timestamp(),
    previous_fingerprint varchar(64) CHECK (previous_fingerprint ~ '^[0-9a-f]{64}$'),
    current_fingerprint varchar(64) CHECK (current_fingerprint ~ '^[0-9a-f]{64}$'),
    revision bigint NOT NULL CHECK (revision > 0),
    CHECK ((action='Created' AND previous_fingerprint IS NULL AND current_fingerprint IS NOT NULL)
        OR (action='Updated' AND previous_fingerprint IS NOT NULL AND current_fingerprint IS NOT NULL AND previous_fingerprint<>current_fingerprint)
        OR (action='Deleted' AND previous_fingerprint IS NOT NULL AND current_fingerprint IS NULL)),
    UNIQUE(deployment_id,project_guid,object_type,object_guid)
);
CREATE TABLE IF NOT EXISTS audit.event_detail (
    event_id bigint PRIMARY KEY REFERENCES audit.event(event_id) ON DELETE CASCADE,
    previous_value jsonb,
    current_value jsonb,
    changed_paths jsonb NOT NULL CHECK (jsonb_typeof(changed_paths)='array')
);
CREATE TABLE IF NOT EXISTS audit.settings_event (
    event_id bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    changed_at timestamptz NOT NULL DEFAULT clock_timestamp(),
    user_id bigint NOT NULL,
    user_name text NOT NULL,
    previous_value jsonb NOT NULL,
    current_value jsonb NOT NULL
);
CREATE INDEX IF NOT EXISTS audit_event_project_time_idx ON audit.event(project_guid,deployed_at DESC,event_id DESC);
CREATE INDEX IF NOT EXISTS audit_event_object_time_idx ON audit.event(project_guid,object_type,object_guid,deployed_at DESC,event_id DESC);
CREATE INDEX IF NOT EXISTS audit_event_time_idx ON audit.event(deployed_at,event_id);
REVOKE ALL ON ALL TABLES IN SCHEMA audit FROM PUBLIC;
REVOKE ALL ON ALL SEQUENCES IN SCHEMA audit FROM PUBLIC;
INSERT INTO audit.schema_version(version) VALUES(1) ON CONFLICT DO NOTHING;
COMMIT;
