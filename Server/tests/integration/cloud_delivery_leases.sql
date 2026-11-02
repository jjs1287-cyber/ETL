\set ON_ERROR_STOP on
BEGIN;
DO $$
DECLARE
 instance_id bigint;
 first_owner uuid := gen_random_uuid();
 second_owner uuid := gen_random_uuid();
 generation bigint;
 changed integer;
BEGIN
 SELECT operation_instance_id INTO instance_id FROM public.operationinstancetab ORDER BY operation_instance_id LIMIT 1;
 IF instance_id IS NULL THEN RAISE EXCEPTION 'An operation instance fixture is required'; END IF;

 INSERT INTO cloudlogging.delivery(operation_instance_id,upload_id,filename,payload,next_attempt_at)
 VALUES(instance_id,gen_random_uuid(),'lease-test','{}',now())
 ON CONFLICT(operation_instance_id) DO UPDATE SET uploaded_at=NULL,attempts=0,next_attempt_at=now(),lease_owner=NULL,lease_until=NULL;

 UPDATE cloudlogging.delivery SET lease_owner=first_owner,lease_generation=lease_generation+1,lease_until=now()+interval '2 minutes'
 WHERE operation_instance_id=instance_id RETURNING lease_generation INTO generation;

 UPDATE cloudlogging.delivery SET uploaded_at=now()
 WHERE operation_instance_id=instance_id AND lease_owner=second_owner AND lease_generation=generation;
 GET DIAGNOSTICS changed=ROW_COUNT;
 IF changed<>0 THEN RAISE EXCEPTION 'A non-owner acknowledged a delivery'; END IF;

 UPDATE cloudlogging.delivery SET lease_until=now()-interval '1 second' WHERE operation_instance_id=instance_id;
 UPDATE cloudlogging.delivery SET lease_owner=second_owner,lease_generation=lease_generation+1,lease_until=now()+interval '2 minutes'
 WHERE operation_instance_id=instance_id AND lease_until<now() RETURNING lease_generation INTO generation;

 UPDATE cloudlogging.delivery SET uploaded_at=now()
 WHERE operation_instance_id=instance_id AND lease_owner=first_owner AND lease_generation=generation-1;
 GET DIAGNOSTICS changed=ROW_COUNT;
 IF changed<>0 THEN RAISE EXCEPTION 'A stale generation acknowledged a delivery'; END IF;

 UPDATE cloudlogging.delivery SET uploaded_at=now(),lease_owner=NULL,lease_until=NULL
 WHERE operation_instance_id=instance_id AND lease_owner=second_owner AND lease_generation=generation;
 GET DIAGNOSTICS changed=ROW_COUNT;
 IF changed<>1 THEN RAISE EXCEPTION 'Current delivery owner could not acknowledge'; END IF;
 RAISE NOTICE 'PASS: cloud delivery lease owner and generation fencing';
END $$;
ROLLBACK;
