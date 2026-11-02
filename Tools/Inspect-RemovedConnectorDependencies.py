from pathlib import Path
import re,json
root=Path('C:/ETL/Server/src/java/modules')
removed={'etlforge-server-connector-engine','etlforge-integration-common-mscrm','etlforge-integration-common-connector'}
def candidate(p):
    return p.relative_to(root).parts[0] in removed or '/backend/webservice/'+p.parent.name+'/' in '/'+p.as_posix()+'/' and p.parent.name in ('connector','mscrm','netsuite')
files=list(root.rglob('*.java'))
texts={p:p.read_text(encoding='utf-8-sig') for p in files}
classes={}
for p,s in texts.items():
    package=re.search(r'^package\s+([\w.]+);',s,re.M)
    if package:classes[package[1]+'.'+p.stem]=p
edges=[]
for p,s in texts.items():
    if candidate(p):continue
    for name in re.findall(r'^import\s+(?:static\s+)?([\w.]+)(?:\.\*)?;',s,re.M):
        target=classes.get(name)
        if target and candidate(target):edges.append({'source':str(p),'target':str(target),'symbol':name})
print(json.dumps({'candidateFiles':sum(candidate(p) for p in files),'externalImports':edges},indent=2))
