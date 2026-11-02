BEGIN;
SELECT pg_advisory_xact_lock(427042,15);
CREATE SCHEMA IF NOT EXISTS etlforge_security;
CREATE TABLE IF NOT EXISTS etlforge_security.key_vault (
 key_id uuid PRIMARY KEY,
 key_type text NOT NULL CHECK(key_type IN ('ssh','pgp')),
 key_name text NOT NULL UNIQUE,
 fingerprint text NOT NULL DEFAULT '',
 public_key text NOT NULL DEFAULT '',
 has_private boolean NOT NULL,
 encrypted_payload text NOT NULL,
 encryption_version integer NOT NULL DEFAULT 1 CHECK(encryption_version=1),
 updated_at timestamptz NOT NULL DEFAULT now()
);
REVOKE ALL ON SCHEMA etlforge_security FROM PUBLIC;
REVOKE ALL ON ALL TABLES IN SCHEMA etlforge_security FROM PUBLIC;
COMMIT;
