BEGIN;
CREATE SCHEMA IF NOT EXISTS cloudlogging;
REVOKE ALL ON SCHEMA cloudlogging FROM PUBLIC;
ALTER TABLE public.operationinstancetab ADD COLUMN IF NOT EXISTS cloud_upload_status smallint NOT NULL DEFAULT 0 CHECK(cloud_upload_status IN (0,1,2));
CREATE TABLE IF NOT EXISTS cloudlogging.configuration (
 singleton boolean PRIMARY KEY DEFAULT true CHECK(singleton),
 settings jsonb NOT NULL DEFAULT '{"enabled":false,"type":"sftp","port":22,"environment":"dev","includeHistory":false}',
 revision bigint NOT NULL DEFAULT 0,
 activated_at timestamp without time zone,
 updated_at timestamptz NOT NULL DEFAULT now()
);
INSERT INTO cloudlogging.configuration(singleton) VALUES(true) ON CONFLICT DO NOTHING;
CREATE TABLE IF NOT EXISTS cloudlogging.delivery (
 operation_instance_id bigint PRIMARY KEY,
 upload_id uuid NOT NULL UNIQUE,
 filename text NOT NULL,
 payload jsonb NOT NULL,
 attempts integer NOT NULL DEFAULT 0,
 next_attempt_at timestamptz NOT NULL DEFAULT now(),
 uploaded_at timestamptz,
 last_error text,
 created_at timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS cloudlogging_delivery_pending ON cloudlogging.delivery(next_attempt_at) WHERE uploaded_at IS NULL;
REVOKE ALL ON ALL TABLES IN SCHEMA cloudlogging FROM PUBLIC;
COMMIT;
