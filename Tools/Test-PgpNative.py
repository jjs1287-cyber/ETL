from pathlib import Path
import subprocess,tempfile
root=Path(tempfile.mkdtemp(prefix='pgp-',dir='C:/ETL/Reports'));home=root/'generator';home.mkdir()
gpg='C:/ETL/Server/runtime/gnupg/bin/gpg.exe'
def posix(p):
 s=Path(p).as_posix();return '/cygdrive/'+s[0].lower()+s[2:]
def run(args):
 r=subprocess.run([gpg,'--no-options','--agent-program','/cygdrive/c/ETL/Server/runtime/gnupg/bin/gpg-agent.exe','--homedir',posix(home),'--batch','--yes','--pinentry-mode','loopback','--passphrase-fd','0']+args,input=b'fixture-pass\n',capture_output=True,timeout=60)
 if r.returncode:raise RuntimeError(r.stderr.decode(errors='replace'))
 return r.stdout
run(['--quick-generate-key','ETLForge fixture <fixture@example.invalid>','rsa2048','encr','1d'])
run(['--armor','--output',posix(root/'private.asc'),'--export-secret-keys'])
run(['--armor','--output',posix(root/'public.asc'),'--export'])
Path('C:/ETL/Reports/pgp-fixture-path.txt').write_text(str(root),encoding='utf-8')
r=subprocess.run(['C:/ETL/Server/build-probe/pgp_files_tests.exe',str(root),str(root/'private.asc'),str(root/'public.asc')],capture_output=True,timeout=180)
print(r.stdout.decode(errors='replace'));print(r.stderr.decode(errors='replace'));print('Exit',r.returncode)


