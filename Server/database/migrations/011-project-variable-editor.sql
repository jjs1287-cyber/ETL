BEGIN;
ALTER TABLE projectvariablestab ADD COLUMN IF NOT EXISTS is_password integer NOT NULL DEFAULT 0 CHECK (is_password IN (0,1));
ALTER TABLE projectvariablestab ADD COLUMN IF NOT EXISTS description text NOT NULL DEFAULT '';
CREATE OR REPLACE VIEW guidsforprojectvariablesview AS SELECT g.guid, x.projectvariable_id, x.projectvariable_name, x.default_value, g.item_id, x.is_password, x.description FROM projectvariablestab x JOIN guidsforprojectvariablestab g ON g.item_id=x.projectvariable_id;
COMMIT;
