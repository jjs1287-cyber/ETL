from pathlib import Path
import subprocess,sqlite3,json,time
r=Path('C:/ETL/Samples/SftpKeys')
for name,password in [('test_ed25519_plain',''),('test_ed25519_pass','fixture-passphrase')]:
 p=r/name
 if not p.exists(): subprocess.run(['C:/Windows/System32/OpenSSH/ssh-keygen.exe','-q','-t','ed25519','-N',password,'-f',str(p),'-C','etlforge-test'],check=True)
f=json.loads(Path('C:/ETL/Reports/sftp-live-fixture.json').read_text());pubs=[(r/(name+'.pub')).read_text().strip() for name in ['test_ed25519_plain','test_ed25519_pass']]
c=sqlite3.connect('C:/ProgramData/SFTPGo/sftpgo.db');c.execute('update users set public_keys=?,updated_at=? where username=?',(json.dumps(pubs),int(time.time()*1000),f['user']));c.commit();c.close()
p=Path('C:/ETL/Server/tests/integration/SftpKeysLiveTest.java');s=p.read_text(encoding='utf-8').replace('SftpKeys/test_ed25519','SftpKeys/test_ed25519_plain');p.write_text(s,encoding='utf-8')
