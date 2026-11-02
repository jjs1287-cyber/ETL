from pathlib import Path
import json,sqlite3
p=Path('C:/ProgramData/SFTPGo/sftpgo.json');c=json.loads(p.read_text(encoding='utf-8-sig'));print('HTTP bindings',c.get('httpd',{}).get('bindings'));print('Provider',c.get('data_provider',{}).get('name'),c.get('data_provider',{}).get('driver'))
for db in ['C:/ProgramData/SFTPGo/sftpgo.db','C:/Program Files/SFTPGo/sftpgo.db']:
 con=sqlite3.connect('file:'+db+'?mode=ro',uri=True)
 print(db,con.execute("select name from sqlite_master where type='table'").fetchall())
 print(con.execute('pragma table_info(users)').fetchall())
 con.close()
