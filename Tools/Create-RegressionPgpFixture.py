from pathlib import Path
import subprocess

root=Path('C:/ETL/Samples/RegressionSuite/pgp-keys');home=root/'generator';home.mkdir(parents=True,exist_ok=True)
if (root/'private.asc').exists() and (root/'public.asc').exists():
    print('Persistent regression PGP fixture already exists.')
    raise SystemExit(0)
gpg='C:/ETL/Server/runtime/gnupg/bin/gpg.exe'
def posix(p):
    s=Path(p).as_posix();return '/cygdrive/'+s[0].lower()+s[2:]
def run(args):
    result=subprocess.run([gpg,'--no-options','--agent-program','/cygdrive/c/ETL/Server/runtime/gnupg/bin/gpg-agent.exe','--homedir',posix(home),'--batch','--yes','--pinentry-mode','loopback','--passphrase-fd','0']+args,input=b'fixture-pass\n',capture_output=True,timeout=90)
    if result.returncode:raise RuntimeError('Regression fixture GPG command failed')
run(['--quick-generate-key','ETLForge regression fixture <fixture@example.invalid>','rsa2048','encr','0'])
run(['--armor','--output',posix(root/'private.asc'),'--export-secret-keys'])
run(['--armor','--output',posix(root/'public.asc'),'--export'])
subprocess.run(['C:/ETL/Server/runtime/gnupg/bin/gpgconf.exe','--homedir',posix(home),'--kill','all'],capture_output=True,timeout=15)
print('Created persistent test-only PGP fixture without expiry; private material stays outside the project.')
