BEGIN;
CREATE TABLE IF NOT EXISTS projectvariablestab (
 projectvariable_id integer PRIMARY KEY,
 projectvariable_name varchar(256),
 default_value text NOT NULL DEFAULT ''
);
CREATE TABLE IF NOT EXISTS guidsforprojectvariablestab (guid varchar(36) PRIMARY KEY, item_id integer UNIQUE NOT NULL);
CREATE TABLE IF NOT EXISTS interchangeprojectprojectvariablestab (interchangeproject_id integer NOT NULL, item_id integer NOT NULL);
CREATE TABLE IF NOT EXISTS userpermissionsforprojectvariablestab (LIKE userpermissionsforscriptstab INCLUDING ALL);
CREATE TABLE IF NOT EXISTS grouppermissionsforprojectvariablestab (LIKE grouppermissionsforscriptstab INCLUDING ALL);
CREATE TABLE IF NOT EXISTS projectvariablepropertiestab (
 projectvariableproperties_id serial PRIMARY KEY,
 projectvariable_id integer NOT NULL,
 keyname varchar(256) NOT NULL,
 keyvalue text NOT NULL
);
CREATE INDEX IF NOT EXISTS projectvariableproperties_item_idx ON projectvariablepropertiestab(projectvariable_id);
CREATE OR REPLACE VIEW guidsforprojectvariablesview AS
 SELECT g.guid, x.*, g.item_id FROM projectvariablestab x JOIN guidsforprojectvariablestab g ON g.item_id=x.projectvariable_id;
DO $$
BEGIN
 IF to_regclass('audit.capture_configuration') IS NOT NULL THEN
  UPDATE audit.capture_configuration SET scope='Operations, Scripts, XSLT transforms and Project Variables'
   WHERE singleton AND scope='Operations, Scripts and XSLT transforms';
 END IF;
END $$;
COMMIT;
