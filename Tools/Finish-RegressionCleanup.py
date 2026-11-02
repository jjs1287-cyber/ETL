from pathlib import Path
import json,uuid,base64,xml.etree.ElementTree as E
root=Path('C:/ETL/Reports/regression-suite');stage=root/'Test Operations';ns=uuid.UUID('b2ec18bc-57d3-467b-b2e0-1d33dfec1d13')
def uid(key):return str(uuid.uuid5(ns,key))
def write(path,e):E.indent(e,space='  ');E.ElementTree(e).write(path,encoding='UTF-8',xml_declaration=True)
d=json.loads((root/'deploy.json').read_text());s=d['tables']['SourcesTab'];source=uid('s3cleanups3');target=uid('s3cleanuplocal');script=uid('s3cleanupcheckscript')
if 'RenameFileTo' not in s['columns']:
    s['columns'].append('RenameFileTo')
    for row in s['rows']:row.append(None)
for row in s['rows']:
    if row[0]==source:row[-1]='/\\delete/\\'
path=stage/f'Data/Target/{target}.xml';e=E.parse(path).getroot();p=e.find('./Location/Data/Properties')
for key,value in [('compression_enabled','true'),('compression_format','zip'),('compression_mode','combined'),('compression_archive_name','s3-cleanup-last')]:
    old=p.find(f"Item[@key='{key}']")
    if old is None:E.SubElement(p,'Item',key=key,value=value)
    if not any(r[1]==target and r[2]==key for r in d['tables']['EntityProperties']['rows']):d['tables']['EntityProperties']['rows'].append(['3',target,key,value])
write(path,e)
path=stage/f'Data/Script/{script}.xml';e=E.parse(path).getroot();text=e.find("konga.string[@name='script']");needle="If(Length(FileList('s."+source+"'))!=0,RaiseError('S3 fixtures remain after cleanup'));"
if needle not in text.text:text.text=text.text.replace('WriteToOperationLog(',needle+'WriteToOperationLog(')
write(path,e)
for row in d['tables']['ScriptsTab']['rows']:
    if row[0]==script:row[2]=base64.b64encode(text.text.encode()).decode()
(root/'deploy.json').write_text(json.dumps(d,indent=2),encoding='utf-8')
print('S3 cleanup now verifies deletion and overwrites one bounded local ZIP.')
