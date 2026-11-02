import base64
import hashlib
import hmac
import json
import os
from pathlib import Path
import socket
import sys
import threading
import time
sys.path.insert(0, r'C:\ETL\Tools\cloud-logging-test-libs')
import paramiko

ROOT = Path(r'C:\ETL\Samples\CloudLogging\sftp-fixture').resolve()
CONTROL = Path(r'C:\ETL\Server\private\cloud-sftp-fixture.json')
STATE = Path(r'C:\ETL\Reports\cloud-sftp-fixture-state.json')
STOP = Path(r'C:\ETL\Server\private\cloud-sftp-fixture.stop')
credentials = json.loads(CONTROL.read_text(encoding='utf-8'))
ROOT.joinpath('CloudLogging').mkdir(parents=True, exist_ok=True)
metrics = dict(port=22222, connections=0, uploads=0, reads=0)
key = paramiko.RSAKey.generate(2048)
metrics['fingerprint'] = base64.b64encode(hashlib.sha256(key.asbytes()).digest()).decode().rstrip('=')

def state():
    STATE.write_text(json.dumps(metrics), encoding='utf-8')

class Auth(paramiko.ServerInterface):
    def check_auth_password(self, username, password):
        return paramiko.AUTH_SUCCESSFUL if hmac.compare_digest(username, credentials['user']) and hmac.compare_digest(password, credentials['password']) else paramiko.AUTH_FAILED
    def get_allowed_auths(self, username):
        return 'password'
    def check_channel_request(self, kind, chanid):
        return paramiko.OPEN_SUCCEEDED if kind == 'session' else paramiko.OPEN_FAILED_ADMINISTRATIVELY_PROHIBITED

class Files(paramiko.SFTPServerInterface):
    def local(self, remote):
        result = ROOT.joinpath(remote.lstrip('/')).resolve()
        if not result.is_relative_to(ROOT):
            raise PermissionError('Outside fixture')
        return result
    def stat(self, path):
        try:
            return paramiko.SFTPAttributes.from_stat(self.local(path).stat())
        except OSError as exc:
            return paramiko.SFTPServer.convert_errno(exc.errno)
    lstat = stat
    def open(self, path, flags, attr):
        if not flags & (os.O_WRONLY | os.O_RDWR):
            metrics['reads'] += 1
            state()
            return paramiko.SFTP_PERMISSION_DENIED
        try:
            file = os.fdopen(os.open(self.local(path), flags, 0o600), 'wb')
            handle = paramiko.SFTPHandle(flags)
            handle.writefile = file
            metrics['uploads'] += 1
            state()
            return handle
        except OSError as exc:
            return paramiko.SFTPServer.convert_errno(exc.errno)
    def rename(self, source, destination):
        try:
            os.replace(self.local(source), self.local(destination))
            return paramiko.SFTP_OK
        except OSError as exc:
            return paramiko.SFTPServer.convert_errno(exc.errno)
    posix_rename = rename

def client(connection):
    transport = paramiko.Transport(connection)
    transport.add_server_key(key)
    transport.set_subsystem_handler('sftp', paramiko.SFTPServer, Files)
    try:
        transport.start_server(server=Auth())
        while transport.is_active() and not STOP.exists():
            time.sleep(0.05)
    finally:
        transport.close()

listener = socket.socket()
listener.bind(('127.0.0.1', metrics['port']))
listener.listen(5)
listener.settimeout(0.5)
state()
try:
    while not STOP.exists():
        try:
            connection, _ = listener.accept()
        except socket.timeout:
            continue
        metrics['connections'] += 1
        state()
        threading.Thread(target=client, args=(connection,), daemon=True).start()
finally:
    listener.close()
    state()
