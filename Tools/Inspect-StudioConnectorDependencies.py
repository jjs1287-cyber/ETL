from pathlib import Path
import re,json
r=Path('C:/ETL/Studio/src/main/java'); fs=list(r.rglob('*.java')); texts={p:p.read_text(encoding='utf-8-sig') for p in fs}
roots=('org/etlforge/integration/client/ui/connector/','org/etlforge/integration/client/connector/','com/etlforge/integration/server/implementation/webservice/connector/')
candidates={p for p in fs if p.relative_to(r).as_posix().startswith(roots)}
classes={}
for p,s in texts.items():
 m=re.search(r'^package\s+([\w.]+);',s,re.M)
 if m:classes[m[1]+'.'+p.stem]=p
edges=[]
for p,s in texts.items():
 if p in candidates:continue
 for imp in re.findall(r'^import\s+(?:static\s+)?([\w.]+)(?:\.\*)?;',s,re.M):
  if classes.get(imp) in candidates:edges.append({'source':str(p.relative_to(r)),'target':imp})
print(json.dumps({'candidates':len(candidates),'outsideImports':edges},indent=2))