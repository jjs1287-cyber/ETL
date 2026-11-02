"""Exercise the rebuilt Java service and real application database; no secrets in output."""
import hashlib
import pathlib
import re
import time
import urllib.error
import urllib.request
import xml.etree.ElementTree as ET

URL="http://127.0.0.1:47112/axis/services/konga_json_engine"
NS="http://json.webservice.backend.server.integration.etlforge.invalid"
SOAP="http://schemas.xmlsoap.org/soap/envelope/"
password=pathlib.Path(r"C:\ETL\Server\private\etlforge-dev-studio-admin-password.txt").read_text().strip()
digest=hashlib.md5(password.encode("utf-8")).hexdigest()
def redact(value):
    return re.sub(r"\b[a-fA-F0-9]{32,64}\b","<credential-redacted>",value).replace(password,"<credential-redacted>")
def call(name,parameters):
    envelope=ET.Element("{"+SOAP+"}Envelope")
    body=ET.SubElement(envelope,"{"+SOAP+"}Body")
    operation=ET.SubElement(body,"{"+NS+"}"+name)
    def populate(parent,values):
        for key,value in values.items():
            child=ET.SubElement(parent,"{"+NS+"}"+key)
            if isinstance(value,dict):populate(child,value)
            else:child.text=value
    populate(operation,parameters)
    request=urllib.request.Request(URL,data=ET.tostring(envelope,encoding="utf-8",xml_declaration=True),headers={"Content-Type":"text/xml; charset=utf-8","SOAPAction":name})
    try:
        with urllib.request.urlopen(request,timeout=20) as response:
            root=ET.fromstring(response.read())
    except urllib.error.HTTPError as error:
        root=ET.fromstring(error.read())
    fault=root.find(".//{"+SOAP+"}Fault")
    if fault is not None:
        raise AssertionError(redact(" ".join(fault.itertext()))[:1200])
    return root
for attempt in range(30):
    try:
        with urllib.request.urlopen(URL+"?wsdl",timeout=2) as response:
            if response.status==200:break
    except (OSError,urllib.error.URLError):time.sleep(1)
else:raise AssertionError("Development Tomcat service did not become ready")
result=call("startNewJsonSession",{"ETLForgeUser":"Admin","Md5Pwd":digest})
fields={node.tag.split("}")[-1]:node.text for node in result.iter()}
session=fields.get("SessionId")
if fields.get("ErrorCode")!="0" or not session:
    raise AssertionError(redact("JSON session failed: "+str(fields.get("ErrorCode"))+" "+str(fields.get("Error")))[:1200])
closed=call("closeJsonSession",{"SessionId":{"ETLForgeUser":"Admin","Md5Pwd":digest,"SessionGuid":session}})
fields={node.tag.split("}")[-1]:node.text for node in closed.iter()}
if fields.get("ErrorCode")!="0":raise AssertionError(redact(str(fields.get("Error"))))
print("PASS: rebuilt Java JSON service, PostgreSQL 18 application authentication, session creation and session cleanup on x64 Tomcat")
