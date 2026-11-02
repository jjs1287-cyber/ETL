import sqlite3
c=sqlite3.connect('C:/ProgramData/SFTPGo/sftpgo.db');print(c.execute("select username,filters from users where username like 'etlforge_%'").fetchall())
