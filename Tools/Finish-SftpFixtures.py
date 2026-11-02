from pathlib import Path
import sqlite3,json
f=json.loads(Path('C:/ETL/Reports/sftp-live-fixture.json').read_text());assert f['user'].startswith('etlforge_sftp_test_')
c=sqlite3.connect('C:/ProgramData/SFTPGo/sftpgo.db');c.execute('delete from users where username=? and home_dir=?',(f['user'],f['root']));print('Removed owned SFTP test account:',c.total_changes);c.commit();c.close()
review=Path('C:/ETL/Reports/server-library-review.md')
with review.open('a',encoding='utf-8') as out:out.write('''\n## SFTP source/target update (2026-10-29)\n\nThe server now deploys the existing libcurl 8.23.0 build with its libssh2 backend enabled, plus libssh2.dll (264,704 bytes). Runtime string: libssh2/1.11.1_DEV; vcpkg package 1.11.1. OpenSSL and zlib are existing dependencies. libssh2 implements SSH negotiation, authentication and SFTP; it should not be replaced with handwritten protocol code. Native per-endpoint key selection and Windows DPAPI key storage do not call Tomcat. License: Server/dist/licenses/libssh2.txt. See Server/docs/sftp-ssh-keys.md.\n''')
