BEGIN;
ALTER TABLE SalesforceConnectorsTab ADD COLUMN IF NOT EXISTS auth_type varchar(32) NOT NULL DEFAULT 'basicAuth';
ALTER TABLE SalesforceConnectorsTab ADD COLUMN IF NOT EXISTS instance_url varchar(1024);
ALTER TABLE SalesforceConnectorsTab ADD COLUMN IF NOT EXISTS client_id text;
ALTER TABLE SalesforceConnectorsTab ADD COLUMN IF NOT EXISTS client_secret text;
COMMIT;