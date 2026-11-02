import sqlite3, json, time
from pathlib import Path

fixture=json.loads(Path('C:/ETL/Reports/sftp-live-fixture.json').read_text(encoding='utf-8'))
home=Path(fixture['root']).resolve()
assert home.is_relative_to(Path('C:/ETL/Samples/SftpKeys').resolve())
assert fixture['user'].startswith('etlforge_sftp_test_')
assert (home/'input/payload.bin').is_file()
keys=[Path('C:/ETL/Samples/SftpKeys/'+name+'.pub').read_text().strip() for name in ('test_ed25519_plain','test_ed25519_pass')]
db=sqlite3.connect('C:/ProgramData/SFTPGo/sftpgo.db')
db.row_factory=sqlite3.Row
existing=db.execute('select * from users where username=?',(fixture['user'],)).fetchone()
if existing:
    assert Path(existing['home_dir']).resolve()==home
    db.execute('update users set status=1,deleted_at=0,public_keys=?,updated_at=? where username=?',(json.dumps(keys),int(time.time()*1000),fixture['user']))
else:
    template=db.execute('select * from users where username=?',('endowance',)).fetchone()
    assert template is not None
    row=dict(template);row.pop('id')
    row.update(username=fixture['user'],home_dir=str(home),status=1,password='',public_keys=json.dumps(keys),permissions=json.dumps({'/':['*']}),created_at=int(time.time()*1000),updated_at=int(time.time()*1000),used_quota_size=0,used_quota_files=0,last_login=0,deleted_at=0)
    db.execute('insert into users ('+','.join(row)+') values ('+','.join('?' for _ in row)+')',list(row.values()))
db.commit();db.close()
print('Restored dedicated key-only regression SFTP account; existing customer accounts unchanged.')
