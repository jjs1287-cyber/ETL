from pathlib import Path
import json,sqlite3
c=json.loads(Path('C:/ProgramData/SFTPGo/sftpgo.json').read_text(encoding='utf-8-sig'))
d=c['data_provider'];print({k:d.get(k) for k in ['name','driver','host','port','users_base_dir','table_prefix','shared']})
for p in Path('C:/ProgramData/SFTPGo/env.d').glob('*'):
 print('env file',p.name)
 for line in p.read_text(encoding='utf-8-sig').splitlines():
  if 'DATA_PROVIDER' in line and 'PASS' not in line and 'SECRET' not in line: print(line)
for db in ['C:/ProgramData/SFTPGo/sftpgo.db','C:/Program Files/SFTPGo/sftpgo.db']:
 con=sqlite3.connect('file:'+db+'?mode=ro',uri=True)
 print(db,con.execute('select username,status,home_dir,public_keys from users').fetchall())
