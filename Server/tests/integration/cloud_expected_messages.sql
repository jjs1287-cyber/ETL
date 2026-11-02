SELECT json_build_object('instance',d.operation_instance_id::text,'message',(SELECT COALESCE(string_agg(message,E'\n' ORDER BY operation_message_id),'') FROM operationmessagetab m WHERE m.operation_instance_id=d.operation_instance_id))::text
FROM cloudlogging.delivery d ORDER BY d.operation_instance_id;
