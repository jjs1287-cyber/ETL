BEGIN;
CREATE TABLE IF NOT EXISTS xslttransformstab (
 xslttransform_id integer PRIMARY KEY,
 xslttransform_name varchar(256),
 xslt text NOT NULL
);
CREATE TABLE IF NOT EXISTS guidsforxslttransformstab (guid varchar(36) PRIMARY KEY, item_id integer UNIQUE NOT NULL);
CREATE TABLE IF NOT EXISTS interchangeprojectxslttransformstab (interchangeproject_id integer NOT NULL, item_id integer NOT NULL);
CREATE TABLE IF NOT EXISTS userpermissionsforxslttransformstab (LIKE userpermissionsforscriptstab INCLUDING ALL);
CREATE TABLE IF NOT EXISTS grouppermissionsforxslttransformstab (LIKE grouppermissionsforscriptstab INCLUDING ALL);
CREATE TABLE IF NOT EXISTS xslttransformpropertiestab (
 xslttransformproperties_id serial PRIMARY KEY,
 xslttransform_id integer NOT NULL,
 keyname varchar(256) NOT NULL,
 keyvalue text NOT NULL
);
CREATE INDEX IF NOT EXISTS xslttransformproperties_item_idx ON xslttransformpropertiestab(xslttransform_id);
CREATE OR REPLACE VIEW guidsforxslttransformsview AS
 SELECT g.guid, x.*, g.item_id FROM xslttransformstab x JOIN guidsforxslttransformstab g ON g.item_id=x.xslttransform_id;
COMMIT;
