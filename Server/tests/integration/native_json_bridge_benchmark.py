import csv
import importlib.util
import json
import pathlib
import statistics
import subprocess
import time
import xml.etree.ElementTree as ET

spec=importlib.util.spec_from_file_location('backend_smoke',pathlib.Path(__file__).with_name('java_json_backend_smoke.py'))
backend=importlib.util.module_from_spec(spec)
spec.loader.exec_module(backend)
root=pathlib.Path('C:/ETL/Samples/NativeJsonDeployment')
input_json=root/'bridge-input.json'
input_json.write_text(json.dumps({'name':'ETLForge 日本 café','amount':12.5,'flag':True},ensure_ascii=False),encoding='utf-8')
schema=root/'NativeJsonRuntime.xsd'
cli='C:/ETL/Server/build-probe/native-json/JsonConversionCli.exe'
rows=[]
def fields(result):return {n.tag.split('}')[-1]:n.text for n in result.iter()}
def success(result):
    values=fields(result)
    if values.get('ErrorCode')!='0':raise AssertionError(backend.redact(str(values.get('Error'))))
    return values
for source in (True,False):
    xml=root/'bridge-java.xml'
    output_json=root/'bridge-java.json'
    params={'JsonFilePath':str(input_json if source else output_json),'XmlFilePath':str(xml),'TransformationId':'10'}
    times=[]
    for i in range(8):
        start=time.perf_counter()
        session=success(backend.call('startNewJsonSession',{'ETLForgeUser':'Admin','Md5Pwd':backend.digest}))['SessionId']
        identity={'ETLForgeUser':'Admin','Md5Pwd':backend.digest,'SessionGuid':session}
        try:success(backend.call('jsonToXml' if source else 'xmlToJson',{'SessionId':identity,'ConversionParams':params}))
        finally:success(backend.call('closeJsonSession',{'SessionId':identity}))
        if i:times.append((time.perf_counter()-start)*1000)
    native_output=root/('bridge-native.xml' if source else 'bridge-native.json')
    completed=subprocess.run([cli,'bench-json-to-xml' if source else 'bench-xml-to-json',str(input_json if source else xml),str(schema),str(native_output)],capture_output=True,text=True,check=True)
    native_ms,peak=completed.stdout.strip().split(',')
    if source:
        def canonical(node):return (node.tag,sorted(node.attrib.items()),node.text.strip() if node.text else '',[canonical(c) for c in node])
        assert canonical(ET.parse(xml).getroot())==canonical(ET.parse(native_output).getroot())
    else:assert json.loads(output_json.read_text(encoding='utf-8'))==json.loads(native_output.read_text(encoding='utf-8'))
    rows.append(['JSON-to-XML' if source else 'XML-to-JSON',statistics.median(times),native_ms,peak])
with pathlib.Path('C:/ETL/Reports/native-json-bridge-speed.csv').open('w',newline='',encoding='utf-8') as stream:
    writer=csv.writer(stream);writer.writerow(['Direction','Tomcat_start_convert_close_median_ms','Native_conversion_median_ms','Native_peak_working_set_bytes']);writer.writerows(rows)
print('PASS: real Tomcat session/conversion/close benchmark, both directions, matching outputs; native timing excludes DB authorization/schema lookup.')
