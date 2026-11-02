from pathlib import Path
import shutil
import xml.etree.ElementTree as E

project=Path('C:/ETL/Reports/regression-suite/Test Operations')
assets=project/'filestore/root'
runtime=Path('C:/ETL/Server/instances/dev')
count=0
for folder in ('DataInterchange/UserFiles','DataInterchange/xml_dtd','DataInterchange/wsdl'):
    source=runtime/folder
    if not source.exists(): continue
    for file in source.rglob('*'):
        if not file.is_file():continue
        relative=file.relative_to(runtime)
        dest=assets/relative;dest.parent.mkdir(parents=True,exist_ok=True);shutil.copy2(file,dest);count+=1
        if file.suffix.lower() in ('.xsd','.dtd','.wsdl'):
            flat=assets/file.name
            if not flat.exists():shutil.copy2(file,flat)
for example in Path('C:/ETL/Samples').glob('*/StudioProject/*/filestore/root'):
    for file in example.rglob('*'):
        if file.is_file():
            dest=assets/file.relative_to(example)
            if not dest.exists():dest.parent.mkdir(parents=True,exist_ok=True);shutil.copy2(file,dest);count+=1
missing=[]
for file in (project/'Data/Transformation').glob('*.xml'):
    for prop in E.parse(file).findall('./Properties/Item'):
        if prop.get('key') in ('source_xml','target_xml'):
            value=prop.get('value','')
            if value and not (assets/value).is_file() and not (assets/Path(value).name).is_file():missing.append((file.stem,value))
Path('C:/ETL/Reports/regression-suite/asset-check.txt').write_text('Copied assets: '+str(count)+'\n'+'\n'.join('MISSING '+id+' '+name for id,name in missing)+'\n',encoding='utf-8')
print('Copied',count,'assets; missing schema references:',len(missing))
