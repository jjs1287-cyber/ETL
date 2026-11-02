from pathlib import Path
import hashlib, os, socket, subprocess, time, urllib.request, xml.etree.ElementTree as ET
from xml.sax.saxutils import escape
root=Path(r'C:\ETL\Server'); config=root/'instances/dev/axis-frontend-test';config.mkdir(exist_ok=True)
(config/'axiscpp.conf').write_text('XMLParser:C:/ETL/Server/dist/bin/axis_xerces.dll\nWSDDFilePath:'+str(config/'server.wsdd').replace('\\','/')+'\n')
services={'konga_authenticate':'Authenticate','konga_project_administration':'ProjectAdministration','konga_checkout_interchange_data':'CheckoutInterchangeData','konga_deploy_interchange_data':'DeployInterchangeData'}
(config/'server.wsdd').write_text('<deployment xmlns="http://xml.apache.org/axis/wsdd/" xmlns:CPP="http://xml.apache.org/axis/wsdd/providers/CPP"><globalConfiguration/>'+''.join('<service name="'+name+'" provider="CPP:DOCUMENT"><parameter name="className" value="C:/ETL/Server/dist/bin/ETLForge'+dll+'.dll"/><parameter name="allowedMethods" value="login loginWithVersion loginWithVersionAndKeyValues getInterchangeProjects getOperationTree checkProjectForDeploy findScheduledOperations"/></service>' for name,dll in services.items())+'</deployment>')

with socket.socket() as probe: probe.bind(('127.0.0.1',47113))
env=os.environ.copy();env['ETLFORGE_HOME']=str(root/'instances/dev');env['AXISCPP_DEPLOY']=str(config);env['PATH']=str(root/'dist/bin')+';'+env['PATH']
password=(root/'private/etlforge-dev-studio-admin-password.txt').read_text().strip();hashed=hashlib.md5(password.encode()).hexdigest()
log=open(r'C:\ETL\Reports\frontend-http-fixture.log','w')
process=subprocess.Popen([str(root/'build/windows-x64/etlforge_frontend_http_fixture.exe')],env=env,stdout=log,stderr=log,creationflags=subprocess.CREATE_NO_WINDOW)
try:
 for attempt in range(100):
  if process.poll() is not None: raise RuntimeError('Native HTTP fixture exited: '+str(process.returncode))
  try:
   with socket.create_connection(('127.0.0.1',47113),timeout=.2): break
  except OSError: time.sleep(.1)
 else: raise RuntimeError('Native HTTP fixture startup timed out')
 for value,valid in [(hashed,True),('wrong-password',False),("' OR '1'='1",False)]:
  body=('''<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><a:loginWithVersion xmlns:a="urn:org.etlforge.integration.server.implementation.webservice.interchange.authenticate.Authenticate"><user>Admin</user><password>'''+escape(value)+'''</password><version>5.6.6.35</version></a:loginWithVersion></soap:Body></soap:Envelope>''').encode()
  request=urllib.request.Request('http://127.0.0.1:47113/axis/konga_authenticate',data=body,headers={'Content-Type':'text/xml; charset=utf-8','SOAPAction':'"loginWithVersion"'})
  with urllib.request.urlopen(request,timeout=20) as response: document=ET.fromstring(response.read())
  fault=document.find('.//{http://schemas.xmlsoap.org/soap/envelope/}Fault')
  if valid:
   if fault is not None: raise RuntimeError('Native SOAP login returned a fault')
   values={node.tag.split('}')[-1]:node.text for node in document.iter()}
   if not values.get('serverGuid') or not values.get('userGuid'):raise RuntimeError('Native SOAP login missing GUID fields')
  elif fault is None:raise RuntimeError('Native SOAP authentication accepted an invalid password')
 subprocess.run([r'C:\ETL\Tools\jdk8u504-b01\bin\java.exe','-cp',r'C:\ETL\Server\build-probe\studio-native-integration;C:\ETL\Studio\configuration;C:\ETL\Studio\dist\lib\*;C:\ETL\Studio\lib\*','StudioNativeLoginSmoke'],check=True,timeout=30)
 print('PASS: x64 native Axis HTTP/SOAP authentication, real PostgreSQL account, GUID response, invalid-password and SQL-like-password SOAP faults')
finally:
 process.terminate()
 try:process.wait(timeout=10)
 except subprocess.TimeoutExpired:process.kill();process.wait()
 log.close()
