import json
from pathlib import Path
root=Path('C:/ETL')
policy=json.loads((root/'Server/features/javascript/host-policy.json').read_text(encoding='utf-8'))
functions=policy['functions']
expected=['ReadFile','WriteFile','DbExecute','DbLookup','GetVar','SetVar','WriteToOperationLog','SetScriptResult','SetScriptOutput','GetSource']
assert [f['id'] for f in functions]==list(range(10))
assert [f['name'].split('.')[-1] for f in functions]==expected
def save(path,text):
    if not path.exists() or path.read_text(encoding='utf-8')!=text:path.write_text(text,encoding='utf-8',newline='\n')
cpp='#pragma once\nnamespace etlforge { namespace javascript { namespace policy {\nstruct HostDefinition {const char* name;bool api;int minimum,maximum;};\nstatic const HostDefinition Hosts[]={\n'
for f in functions:cpp+='  {'+json.dumps(f['name'].split('.')[-1])+','+('true' if f['name'].startswith('ETLForge.') else 'false')+','+str(f['min'])+','+str(f['max'])+'},\n'
cpp+='};\n'
for name,value in policy['defaults'].items():cpp+=f'static constexpr unsigned {name}={value};\n'
cpp+='} } }\n'
save(root/'Server/src/native/javascript/JavaScriptPolicy.generated.h',cpp)
java='package org.etlforge.integration.data.script;\npublic final class JavaScriptHostNames {\n private JavaScriptHostNames(){}\n'
java+=' public static final String[] HOSTS={'+','.join(json.dumps(f['name']) for f in functions if not f.get('extension'))+'};\n'
java+=' public static final String[] MAPPING={'+','.join(json.dumps(f['name']) for f in functions if f.get('extension'))+'};\n'
java+=' public static String signature(String name){\n'
for f in functions:java+='  if(name.equals('+json.dumps(f['name'])+'))return '+json.dumps(f['signature'])+';\n'
java+='  return name+"(...)";\n }\n}\n'
save(root/'Studio/src/main/java/org/etlforge/integration/data/script/JavaScriptHostNames.java',java)
print('Generated native/Studio policy for',len(functions),'host bindings')
