INSERT INTO operationmessagetab(operation_instance_id,field_name,message)
SELECT max(operation_instance_id),'Cloud logging UTF8 verification',
 'caf'||chr(233)||' '||chr(8364)||' '||chr(20013)||chr(25991)||' '||chr(128512)||E' comma, quotes "test"\nsecond line'
FROM operationinstancetab WHERE operation_id=(SELECT item_id FROM guidsforoperationstab WHERE guid='42420000-0000-4000-8000-000000000003');
