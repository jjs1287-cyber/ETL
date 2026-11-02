import sqlite3,json,time,uuid
from pathlib import Path
root=Path('C:/ETL/Samples/SftpKeys/live-'+str(uuid.uuid4()));root.mkdir(parents=True)
(root/'input').mkdir();(root/'output').mkdir()
payload='ETLForge SFTP café 世界 😀\r\n'.encode('utf-8')+bytes(range(256))*16
(root/'input'/'payload.bin').write_bytes(payload)
c=sqlite3.connect('C:/ProgramData/SFTPGo/sftpgo.db');c.row_factory=sqlite3.Row
row=c.execute('select * from users where username=?',('endowance',)).fetchone();assert row is not None
r=dict(row);r.pop('id');r.update(username='etlforge_sftp_test_'+uuid.uuid4().hex[:8],home_dir=str(root),public_keys=json.dumps([Path('C:/ETL/Samples/SftpKeys/test_ed25519.pub').read_text().strip()]),password='',permissions=json.dumps({'/':['*']}),created_at=int(time.time()*1000),updated_at=int(time.time()*1000),used_quota_size=0,used_quota_files=0,last_login=0,deleted_at=0)
c.execute('insert into users ('+','.join(r)+') values ('+','.join('?' for _ in r)+')',list(r.values()));c.commit();c.close()
Path('C:/ETL/Reports/sftp-live-fixture.json').write_text(json.dumps({'root':str(root),'user':r['username']}),encoding='utf-8');print('Created isolated key-only SFTP fixture',r['username'])
