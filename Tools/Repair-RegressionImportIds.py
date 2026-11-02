from pathlib import Path
import xml.etree.ElementTree as E
import json,uuid

root=Path('C:/ETL/Reports/regression-suite');project=root/'Test Operations'
index=E.parse(project/'New Project.xml');groups=json.loads((root/'groups.json').read_text());ns=uuid.UUID('b2ec18bc-57d3-467b-b2e0-1d33dfec1d13')
def write(path,e):
    E.indent(e,space='  ');E.ElementTree(e).write(path,encoding='UTF-8',xml_declaration=True)
remap={}
for typ,old in [('HttpEndpoint','42420000-0000-4000-8000-000000000005'),('SalesforceConnector','42420000-0000-4000-8000-000000000002'),('WebServiceCall','42420000-0000-4000-8000-000000000004')]:
    path=project/f'Data/{typ}/{old}.xml'
    if not path.exists():continue
    new=str(uuid.uuid5(ns,'historical-'+typ+'-'+old));tree=E.parse(path).getroot();h=tree.find('Header');h.set('ID',new);h.set('Deployed','false');h.set('DeployDirty','false')
    desc=h.find("konga.string[@name='Description']")
    if desc is not None:desc.text=(desc.text or '')+' Original server ID: '+old+'. Historical fixture; configure before independent deployment.'
    for item in tree.findall('./Properties/Item'):
        if item.get('key') in ('httpendpoint_id','salesforceconnector_id','webservice_id'):item.set('value',new)
    write(path.with_name(new+'.xml'),tree);path.unlink();groups.pop(old,None);groups[new]='Historical fixtures';remap[(typ,old)]=new
    for ref in index.findall(f"./EntityType[@name='{typ}']//Entity"):
        if ref.get('entityId')==old:ref.set('entityId',new)
for link in json.loads((root/'historical-webservice-links.json').read_text()):
    path=project/f"Data/Operation/{link['operation']}.xml"
    if not path.exists():continue
    tree=E.parse(path).getroot();activities=tree.find('./Pipeline/Activities')
    if activities is None or activities.find('WEBSERVICECALL') is not None:continue
    method=remap.get(('WebServiceCall',link['method']),link['method'])
    if not (project/f'Data/WebServiceCall/{method}.xml').exists():raise RuntimeError('Missing archived web service method')
    activities.insert(int(link['position']),E.Element('WEBSERVICECALL',activityId=str(uuid.uuid5(ns,'historical-activity-'+link['operation'])),contentId=method,role='Web Service Call',type='10'))
    functions=tree.find('Functions')
    if functions is None:functions=E.SubElement(tree,'Functions')
    E.SubElement(functions,'Fcn',type='WebServiceCall',value=method);write(path,tree)
write(project/'New Project.xml',index.getroot());(root/'groups.json').write_text(json.dumps(groups),encoding='utf-8')
(root/'historical-id-remap.json').write_text(json.dumps([{'type':t,'original':o,'local':n} for (t,o),n in remap.items()],indent=2),encoding='utf-8')
print('Repaired historical metadata ID collisions:',len(remap))
