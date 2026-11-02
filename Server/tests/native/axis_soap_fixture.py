"""Contract fixture for the supplied generated JSON SOAP client; loopback only."""
import http.server
import os
import pathlib
import subprocess
import sys
import tempfile
import threading
import xml.etree.ElementTree as ET

NS="http://json.webservice.backend.server.integration.etlforge.invalid"
class Handler(http.server.BaseHTTPRequestHandler):
    protocol_version="HTTP/1.1"
    def log_message(self,*args):
        pass
    def do_POST(self):
        data=self.rfile.read(int(self.headers["Content-Length"]))
        root=ET.fromstring(data)
        operation=root.find("{http://schemas.xmlsoap.org/soap/envelope/}Body")[0]
        self.server.requests.append(operation)
        if len(self.server.requests)==1:
            xml=f'''<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/"><s:Body><n:startNewJsonSessionResponse xmlns:n="{NS}"><startNewJsonSessionReturn><SessionId>ETLForge-session-64-\u65e5\u672c</SessionId><Error><ErrorCode>0</ErrorCode><Error></Error><Warnings></Warnings></Error></startNewJsonSessionReturn></n:startNewJsonSessionResponse></s:Body></s:Envelope>'''
            status=200
        else:
            xml='''<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/"><s:Body><s:Fault><faultcode>s:Server</faultcode><faultstring>Fixture contract failure</faultstring><detail>Expected fixture fault</detail></s:Fault></s:Body></s:Envelope>'''
            status=500
        payload=xml.encode("utf-8")
        self.send_response(status)
        self.send_header("Content-Type","text/xml; charset=utf-8")
        self.send_header("Content-Length",str(len(payload)))
        self.end_headers()
        self.wfile.write(payload)

with tempfile.TemporaryDirectory(prefix="etlforge-axis-") as temp:
    dlls=pathlib.Path(sys.argv[2])
    config=pathlib.Path(temp,"axiscpp.conf")
    config.write_text("\n".join(f"{key}:{dlls/name}" for key,name in [
        ("XMLParser","axis_xerces.dll"),("Transport_http","axis_http_transport.dll"),("Channel_HTTP","axis_http_channel.dll")]),encoding="utf-8")
    environment=os.environ.copy()
    environment["AXISCPP_DEPLOY"]=temp
    with http.server.HTTPServer(("127.0.0.1",0),Handler) as server:
        server.requests=[]
        thread=threading.Thread(target=server.serve_forever,daemon=True)
        thread.start()
        try:
            endpoint=f"http://127.0.0.1:{server.server_address[1]}/axis/services/konga_json_engine"
            completed=subprocess.run([sys.argv[1],endpoint],env=environment,capture_output=True,text=True,timeout=30)
            print(completed.stdout,end="")
            if completed.returncode:
                raise AssertionError(f"Client exit {completed.returncode}; requests received {len(server.requests)}: {completed.stderr}")
            assert len(server.requests)==2, f"Unexpected SOAP request count: {len(server.requests)}"
            for operation in server.requests:
                assert operation.tag=="{"+NS+"}startNewJsonSession"
                parameters={child.tag.split("}")[-1]:child.text for child in operation}
                assert parameters=={"ETLForgeUser":"fixture-user","Md5Pwd":"fixture-md5"}
            print("PASS: generated SOAP namespace, credentials fields, success DTO and HTTP 500 SOAP Fault; loopback only")
        finally:
            server.shutdown()
            thread.join()
