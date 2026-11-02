BEGIN;
SELECT pg_advisory_xact_lock(427042,14);
CREATE TABLE IF NOT EXISTS etlforge_cluster.schedule_revision (
 singleton boolean PRIMARY KEY DEFAULT true CHECK(singleton),
 revision bigint NOT NULL DEFAULT 1
);
INSERT INTO etlforge_cluster.schedule_revision(singleton) VALUES(true) ON CONFLICT DO NOTHING;
CREATE OR REPLACE FUNCTION etlforge_cluster.schedule_changed() RETURNS trigger LANGUAGE plpgsql AS $$
BEGIN
 UPDATE etlforge_cluster.schedule_revision SET revision=revision+1 WHERE singleton;
 RETURN NULL;
END $$;
DO $$ DECLARE name text; BEGIN
 FOREACH name IN ARRAY ARRAY['scheduletab','schedulepropertiestab','operationstab','operationpropertiestab'] LOOP
  EXECUTE format('DROP TRIGGER IF EXISTS etlforge_schedule_changed ON public.%I',name);
  EXECUTE format('CREATE TRIGGER etlforge_schedule_changed AFTER INSERT OR UPDATE OR DELETE OR TRUNCATE ON public.%I FOR EACH STATEMENT EXECUTE FUNCTION etlforge_cluster.schedule_changed()',name);
 END LOOP;
END $$;
COMMIT;
