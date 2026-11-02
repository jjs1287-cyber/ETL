import hashlib
import http.client
import json
import pathlib
import socket
import subprocess
import time
import urllib.error
import urllib.request
import xml.etree.ElementTree as ET

ROOT = pathlib.Path('C:/ETL')
REPORT = ROOT / 'Reports/studio-single-port'
REPORT.mkdir(exist_ok=True)
SOAP = 'http://schemas.xmlsoap.org/soap/envelope/'
NAMESPACE = 'http://json.webservice.backend.server.integration.etlforge.invalid'
DIRECT = 'http://127.0.0.1:47112'
GATEWAY = 'http://127.0.0.1:47100'
password = (ROOT/'Server/private/etlforge-dev-studio-admin-password.txt').read_text(encoding='utf-8').strip()
digest = hashlib.md5(password.encode('utf-8')).hexdigest()
checks = 0

def check(condition, label):
    global checks
    if not condition:
        raise AssertionError(label)
    checks += 1
    print('PASS', label, flush=True)

def request(base, path, body=None, headers=None):
    req = urllib.request.Request(base+path, data=body, headers=headers or {})
    try:
        with urllib.request.urlopen(req, timeout=20) as result:
            return result.status, result.read()
    except urllib.error.HTTPError as error:
        return error.code, error.read()

def envelope(operation, values):
    root = ET.Element('{'+SOAP+'}Envelope')
    target = ET.SubElement(ET.SubElement(root,'{'+SOAP+'}Body'),'{'+NAMESPACE+'}'+operation)
    def populate(parent, fields):
        for name, value in fields.items():
            child=ET.SubElement(parent,'{'+NAMESPACE+'}'+name)
            if isinstance(value,dict): populate(child,value)
            else: child.text=value
    populate(target,values)
    return ET.tostring(root,encoding='utf-8',xml_declaration=True)

def call(base, operation, values, chunked=False):
    body=envelope(operation,values)
    headers={'Content-Type':'text/xml; charset=utf-8','SOAPAction':operation}
    if chunked:
        port=int(base.rsplit(':',1)[1]);conn=http.client.HTTPConnection('127.0.0.1',port,timeout=20)
        try:
            conn.request('POST','/axis/services/konga_json_engine',body=iter([body[:79],body[79:]]),headers=headers,encode_chunked=True)
            result=conn.getresponse();data=result.read()
        finally:conn.close()
    else:
        _,data=request(base,'/axis/services/konga_json_engine',body,headers)
    root=ET.fromstring(data)
    fault=root.find('.//{'+SOAP+'}Fault')
    if fault is not None:raise AssertionError('Unexpected Java SOAP fault (details suppressed)')
    return {n.tag.split('}')[-1]:n.text for n in root.iter()}

def wsdl_tests():
    template=(ROOT/'Server/src/resources/apache/studio-java-proxy.conf.in').read_text(encoding='utf-8')
    import re
    allowed=re.search(r'\^/axis/services/\(([^)]+)\)',template).group(1).split('|')
    deployed=ET.parse(ROOT/'Server/dist/webapps/axis/WEB-INF/server-config.wsdd').getroot()
    deployed_names={n.attrib.get('name') for n in deployed if n.tag.split('}')[-1]=='service' and n.attrib.get('name','').startswith('konga_')}
    check(set(allowed)==deployed_names,'gateway allowlist covers every deployed ETLForge Java service')
    timings=[]
    for name in allowed:
        path='/axis/services/'+name+'?wsdl'
        start=time.perf_counter();code,direct=request(DIRECT,path);direct_ms=(time.perf_counter()-start)*1000
        start=time.perf_counter();proxy_code,proxy=request(GATEWAY,path);proxy_ms=(time.perf_counter()-start)*1000
        check(code==200 and proxy_code==200,'direct and gateway WSDL: '+name)
        left,right=ET.fromstring(direct),ET.fromstring(proxy)
        addresses=[n.attrib.get('location','') for n in right.iter() if n.tag.endswith('}address')]
        check(bool(addresses) and all(a.startswith(GATEWAY+'/axis/services/') for a in addresses),'WSDL advertises gateway port: '+name)
        for tree in (left,right):
            for node in tree.iter():
                if node.tag.endswith('}address') and 'location' in node.attrib:node.set('location',node.get('location').replace(DIRECT,'GATEWAY').replace(GATEWAY,'GATEWAY'))
        check(ET.tostring(left)==ET.tostring(right),'WSDL contract unchanged: '+name)
        timings.append({'service':name,'direct_ms':round(direct_ms,2),'gateway_ms':round(proxy_ms,2)})
    (REPORT/'wsdl-timings.json').write_text(json.dumps(timings,indent=2),encoding='utf-8')
    for path in ['/axis/services','/axis/services/','/axis/services/AdminService','/axis/services/Version','/axis/services/unknown','/axis/services/konga_jdbc_infoprovider/extra','/axis/services/konga_jdbc_infoprovider/','/manager/html','/host-manager/html']:
        code,_=request(GATEWAY,path)
        check(code in (400,403,404),'non-allowlisted route is not proxied: '+path)

    code,body=request(GATEWAY,'/axis/servlet/AdminServlet')
    check(code in (403,404) or (code==200 and body.strip()==b'<h3>Url not available</h3>'),'legacy native handler rejects servlet admin path without forwarding')

def session_tests():
    for start_at,end_at,chunked in [(DIRECT,GATEWAY,False),(GATEWAY,DIRECT,False),(GATEWAY,GATEWAY,True)]:
        fields=call(start_at,'startNewJsonSession',{'ETLForgeUser':'Admin','Md5Pwd':digest},chunked)
        check(fields.get('ErrorCode')=='0' and bool(fields.get('SessionId')),'Java authenticated session starts via '+start_at)
        closed=call(end_at,'closeJsonSession',{'SessionId':{'ETLForgeUser':'Admin','Md5Pwd':digest,'SessionGuid':fields['SessionId']}})
        check(closed.get('ErrorCode')=='0','session identity and SOAP payload preserved across routes')
    errors=[]
    for base in (DIRECT,GATEWAY):
        fields=call(base,'startNewJsonSession',{'ETLForgeUser':'nonexistent-\u4e16\u754c-\U0001f600','Md5Pwd':'0'*32})
        check(fields.get('ErrorCode')!='0','invalid Unicode username rejected cleanly via '+base)
        errors.append((fields.get('ErrorCode'),fields.get('Error')))
    check(errors[0]==errors[1],'authentication error and UTF-8 response match direct Java behavior')

def unavailable_backend_test():
    folder=REPORT/'unavailable-backend';folder.mkdir(exist_ok=True)
    with socket.socket() as s:s.bind(('127.0.0.1',0));port=s.getsockname()[1]
    with socket.socket() as s:s.bind(('127.0.0.1',0));unavailable=s.getsockname()[1]
    proxy=(ROOT/'Server/src/resources/apache/studio-java-proxy.conf.in').read_text(encoding='utf-8').replace('@BACKEND_PORT@',str(unavailable))
    (folder/'proxy.conf').write_text(proxy,encoding='utf-8')
    config=f'''ServerRoot "C:/ETL/Server/runtime/apache-httpd-2.4.69/Apache24"
Listen 127.0.0.1:{port}
ServerName 127.0.0.1:{port}
LoadModule authz_core_module modules/mod_authz_core.so
LoadModule authz_host_module modules/mod_authz_host.so
PidFile "{folder.as_posix()}/httpd.pid"
ErrorLog "{folder.as_posix()}/error.log"
DocumentRoot "{folder.as_posix()}"
<Directory />
Require all denied
</Directory>
Include "{folder.as_posix()}/proxy.conf"
'''
    path=folder/'httpd.conf';path.write_text(config,encoding='utf-8')
    apache=str(ROOT/'Server/runtime/apache-httpd-2.4.69/Apache24/bin/httpd.exe')
    process=subprocess.Popen([apache,'-X','-f',str(path)],stdout=subprocess.DEVNULL,stderr=subprocess.DEVNULL,creationflags=subprocess.CREATE_NO_WINDOW)
    try:
        for _ in range(50):
            try:
                code,_=request(f'http://127.0.0.1:{port}','/axis/services/konga_jdbc_infoprovider?wsdl');break
            except urllib.error.URLError:time.sleep(.1)
        else:raise AssertionError('Isolated Apache test listener did not start')
        check(code in (502,503),'unavailable Java backend returns a bounded gateway failure')
        conn=http.client.HTTPConnection('127.0.0.1',port,timeout=10)
        try:
            conn.request('GET',DIRECT+'/axis/services/konga_jdbc_infoprovider?wsdl')
            result=conn.getresponse();result.read();check(result.status in (502,503),'absolute-form request cannot select a different reachable upstream')
        finally:conn.close()
    finally:
        process.terminate();process.wait(timeout=10)

if __name__=='__main__':
    wsdl_tests();session_tests();unavailable_backend_test()
    print(f'Passed {checks} single-port Apache/Java transport checks')
