BEGIN;
SELECT pg_advisory_xact_lock(427042,16);
ALTER TABLE cloudlogging.delivery
 ADD COLUMN IF NOT EXISTS lease_owner uuid,
 ADD COLUMN IF NOT EXISTS lease_generation bigint NOT NULL DEFAULT 0,
 ADD COLUMN IF NOT EXISTS lease_until timestamptz;
CREATE INDEX IF NOT EXISTS cloudlogging_delivery_claimable
 ON cloudlogging.delivery(next_attempt_at,operation_instance_id)
 WHERE uploaded_at IS NULL AND attempts<5;
COMMIT;
