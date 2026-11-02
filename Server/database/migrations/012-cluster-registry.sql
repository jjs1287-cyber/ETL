BEGIN;
SELECT pg_advisory_xact_lock(427042,12);
CREATE SCHEMA IF NOT EXISTS etlforge_cluster;
CREATE TABLE IF NOT EXISTS etlforge_cluster.configuration (
 singleton boolean PRIMARY KEY DEFAULT true CHECK(singleton),
 cluster_id uuid NOT NULL DEFAULT gen_random_uuid(),
 cluster_name text NOT NULL DEFAULT 'ETLForge',
 revision bigint NOT NULL DEFAULT 1,
 heartbeat_timeout_seconds integer NOT NULL DEFAULT 60 CHECK(heartbeat_timeout_seconds BETWEEN 30 AND 600),
 updated_at timestamptz NOT NULL DEFAULT now()
);
INSERT INTO etlforge_cluster.configuration(singleton) VALUES(true) ON CONFLICT DO NOTHING;
CREATE TABLE IF NOT EXISTS etlforge_cluster.node (
 node_id uuid PRIMARY KEY,
 hostname text NOT NULL,
 display_name text NOT NULL,
 api_url text NOT NULL DEFAULT '',
 build text NOT NULL,
 registered_at timestamptz NOT NULL DEFAULT now()
);
CREATE TABLE IF NOT EXISTS etlforge_cluster.service_heartbeat (
 node_id uuid NOT NULL REFERENCES etlforge_cluster.node(node_id),
 service text NOT NULL CHECK(service IN ('Scheduler')),
 incarnation uuid NOT NULL,
 process_id integer NOT NULL,
 started_at timestamptz NOT NULL DEFAULT now(),
 heartbeat_at timestamptz NOT NULL DEFAULT now(),
 PRIMARY KEY(node_id,service)
);
CREATE OR REPLACE FUNCTION etlforge_cluster.scheduler_heartbeat(p_node uuid,p_host text,p_incarnation uuid,p_pid integer)
RETURNS void LANGUAGE plpgsql AS $$
DECLARE changed integer;
BEGIN
 INSERT INTO etlforge_cluster.node(node_id,hostname,display_name,build)
 VALUES(p_node,p_host,p_host,'42.0.0.0') ON CONFLICT(node_id) DO NOTHING;
 IF NOT EXISTS(SELECT 1 FROM etlforge_cluster.node WHERE node_id=p_node AND hostname=p_host) THEN
  RAISE EXCEPTION 'ETLForge node identity belongs to a different hostname; generate a new NodeId';
 END IF;
 INSERT INTO etlforge_cluster.service_heartbeat(node_id,service,incarnation,process_id)
 VALUES(p_node,'Scheduler',p_incarnation,p_pid)
 ON CONFLICT(node_id,service) DO UPDATE SET incarnation=EXCLUDED.incarnation,process_id=EXCLUDED.process_id,
 started_at=CASE WHEN service_heartbeat.incarnation=EXCLUDED.incarnation THEN service_heartbeat.started_at ELSE now() END,
 heartbeat_at=now()
 WHERE service_heartbeat.incarnation=EXCLUDED.incarnation OR service_heartbeat.heartbeat_at < now()-make_interval(secs=>(SELECT heartbeat_timeout_seconds FROM etlforge_cluster.configuration WHERE singleton));
 GET DIAGNOSTICS changed=ROW_COUNT;
 IF changed<>1 THEN RAISE EXCEPTION 'Another live Scheduler is using this ETLForge NodeId'; END IF;
END $$;
COMMIT;
