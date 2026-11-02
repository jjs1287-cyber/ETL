from pathlib import Path
import subprocess, os, time
root=Path('C:/ETL')
java=root/'Tools/jdk8u504-b01/bin/java.exe'
cp=';'.join(str(p) for p in [root/'Server/build-probe',root/'Server/build-probe/studio-native-integration',root/'Studio/configuration',root/'Studio/dist/lib/*',root/'Studio/lib/*'])
for name,report in [('LocalVariablesValidatorTest','locals-validator-final.txt'),('LocalVariablesRuntimeTest','locals-runtime-after.txt'),('LocalVariablesIsolationTest','locals-isolation-after.txt'),('StudioNativeXsltTestSmoke','locals-xslt-regression.txt')]:
 result=subprocess.run([str(java),'-Dfile.encoding=UTF-8','-cp',cp,name],capture_output=True,encoding='utf-8',timeout=120)
 (root/'Reports'/report).write_text(result.stdout+result.stderr,encoding='utf-8')
 if result.returncode:raise RuntimeError(name+' failed; see '+report)
 print('PASS '+name)
fixture=(root/'Reports/locals-isolation-fixture.txt').read_text().splitlines()
import uuid
operation=str(uuid.UUID(fixture[1]))
env=dict(os.environ,PGPASSFILE=str(root/'Server/private/pgbouncer-dev.pgpass'))
psql=root/'Server/runtime/postgresql-18/pgsql/bin/psql.exe'
query="SELECT l.operation_instance_id,l.status FROM operationlogtab l JOIN operationmessagetab m USING(operation_instance_id) WHERE l.operation_id=(SELECT item_id FROM guidsforoperationstab WHERE guid='"+operation+"') AND l.status IN (3,4,5) AND m.message LIKE 'LOCAL_SCOPE:isolated%' ORDER BY l.operation_instance_id"
command=[str(psql),'-X','-w','-A','-t','-h','127.0.0.1','-p','5432','-U','etlforge_dev','-d','etlforge_dev_tran','-c',query]
for attempt in range(60):
 result=subprocess.run(command,env=env,capture_output=True,encoding='utf-8',check=True,timeout=15).stdout.strip()
 if len(result.splitlines())==2:
  (root/'Reports/locals-worker-scope.txt').write_text(result+'\n',encoding='utf-8')
  print('PASS synchronous and queued child operation logs prove namespace isolation')
  break
 time.sleep(1)
else:raise AssertionError('Child operation worker did not record both isolation results')
