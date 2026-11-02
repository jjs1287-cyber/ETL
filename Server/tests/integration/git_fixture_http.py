import http.server,subprocess,os,sys,gzip
from pathlib import Path
root=Path(sys.argv[1]).resolve()
class Handler(http.server.BaseHTTPRequestHandler):
 def do_GET(self): self.handle_git()
 def do_POST(self): self.handle_git()
 def log_message(self,*args): pass
 def handle_git(self):
  path,_,query=self.path.partition('?')
  parts=path.strip('/').split('/');repo=(root/parts[0]).resolve()
  if repo.parent!=root or not repo.is_dir():self.send_error(404);return
  service=query.removeprefix('service=') if self.command=='GET' else parts[-1]
  if service not in ('git-upload-pack','git-receive-pack'):self.send_error(404);return
  args=['git',service.removeprefix('git-'),'--stateless-rpc']
  if self.command=='GET':args.append('--advertise-refs')
  args.append(str(repo));data=self.rfile.read(int(self.headers.get('Content-Length','0')))
  if self.headers.get('Content-Encoding')=='gzip':data=gzip.decompress(data)
  result=subprocess.run(args,input=data,stdout=subprocess.PIPE,stderr=subprocess.PIPE)
  if result.returncode:self.send_error(500);return
  self.send_response(200);kind='advertisement' if self.command=='GET' else 'result'
  self.send_header('Content-Type',f'application/x-{service}-{kind}');self.end_headers()
  if self.command=='GET':
   header=f'# service={service}\n'.encode();self.wfile.write(f'{len(header)+4:04x}'.encode()+header+b'0000')
  self.wfile.write(result.stdout)
server=http.server.ThreadingHTTPServer(('127.0.0.1',0),Handler)
Path(sys.argv[2]).write_text(str(server.server_port),encoding='utf-8')
server.serve_forever()
