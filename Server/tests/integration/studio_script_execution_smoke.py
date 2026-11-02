"""Verify Studio submission, worker completion and the script's own log output."""
import json
import os
import pathlib
import subprocess
import time
import uuid

root=pathlib.Path('C:/ETL')
manifest=json.loads((root/'Reports/native-script-job-manifest.json').read_text())
uuid.UUID(manifest['operationGuid'])
marker=manifest['marker'].replace("'","''")
env=dict(os.environ,PGPASSFILE=str(root/'Server/private/pgbouncer-dev.pgpass'),PGCLIENTENCODING='UTF8')
classpath=';'.join(map(str,[root/'Server/build-probe/studio-native-integration',root/'Studio/configuration',root/'Studio/dist/lib/*',root/'Studio/lib/*']))
subprocess.run([str(root/'Tools/jdk8u504-b01/bin/java.exe'),'-cp',classpath,'StudioScriptRunSmoke'],check=True)
instance=int((root/'Reports/native-script-run-instance.txt').read_text())
query=f"SELECT status::text || '|' || EXISTS(SELECT 1 FROM OperationMessageTab WHERE operation_instance_id={instance} AND message LIKE '%{marker}%')::text FROM OperationLogTab WHERE operation_instance_id={instance}"
command=[str(root/'Server/runtime/postgresql-18/pgsql/bin/psql.exe'),'-X','-w','-A','-t','-v','ON_ERROR_STOP=1','-h','127.0.0.1','-p','5432','-U','etlforge_dev','-d','etlforge_dev_tran','-c',query]
deadline=time.monotonic()+90
while time.monotonic()<deadline:
    result=subprocess.run(command,env=env,check=True,capture_output=True,text=True,encoding='utf-8',timeout=15).stdout.strip()
    if result:
        status,logged=result.split('|')
        if status=='6':raise AssertionError('Queued script operation failed; inspect its operation messages')
        if status in ('3','4','5'):
            if logged!='true':raise AssertionError('Worker finished without the expected script log message')
            print(f'PASS: operation instance {instance} executed in the x64 worker and wrote its unique WriteToOperationLog marker; status={status}')
            break
    time.sleep(.25)
else:
    raise AssertionError('Script operation did not complete within 90 seconds')
