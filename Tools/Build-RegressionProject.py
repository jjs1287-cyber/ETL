from pathlib import Path
import xml.etree.ElementTree as E
import copy, json, uuid, shutil, hashlib, re, datetime

ROOT=Path('C:/ETL/Reports/regression-suite')
USER=Path('C:/ETL/Studio/run-user-home-etlforge/application/projects/Test Operations')
STAGE=ROOT/'Test Operations'
NS=uuid.UUID('b2ec18bc-57d3-467b-b2e0-1d33dfec1d13')
def uid(s): return str(uuid.uuid5(NS,str(s)))
def write(path, element):
    path.parent.mkdir(parents=True,exist_ok=True)
    E.indent(element,space='  ')
    E.ElementTree(element).write(path,encoding='UTF-8',xml_declaration=True)
def props(e): return {x.get('key'):x.get('value') for x in e.findall('./Properties/Item')}
def loc(e): return {x.get('key'):x.get('value') for x in e.findall('./Location/Data/Properties/Item')}
def entity(typ,name,id):
    e=E.Element('Entity',type=typ); h=E.SubElement(e,'Header',Deleted='false',DeployDirty='true',Deployed='false',HasMoved='false',ID=id,Name=name)
    E.SubElement(h,'konga.string',name='Description');E.SubElement(e,'Properties');return e
def put(e,key,value):
    p=e.find('./Properties'); old=p.find(f"Item[@key='{key}']")
    if old is None: old=E.SubElement(p,'Item',key=key)
    old.set('value',str(value))
def script(name,code,id):
    e=entity('Script',name,id);E.SubElement(e,'konga.string',name='script').text=code;return e
def operation(name,scriptid,id):
    e=entity('Operation',name,id);put(e,'operation_type_id',8)
    a=E.SubElement(E.SubElement(e,'Pipeline',opType='Script'),'Activities')
    E.SubElement(a,'SCRIPT',activityId=uid(id+'activity'),contentId=scriptid,role='Script',type='23');E.SubElement(e,'Siblings');E.SubElement(e,'Functions');return e
def endpoint(typ,name,id,location):
    e=entity(typ,name,id); d=E.SubElement(E.SubElement(E.SubElement(e,'Location',type='LocalFile'),'Data'),'Properties')
    for k,v in location.items(): E.SubElement(d,'Item',key=k,value=str(v))
    return e
def q(s): return "'"+str(s).replace('\\','\\\\').replace("'","\\'").replace('\r','\\r').replace('\n','\\n')+"'"

if STAGE.exists(): STAGE.rename(ROOT/('stage-before-'+datetime.datetime.now().strftime('%Y%m%d-%H%M%S')))
shutil.copytree(USER,STAGE,ignore=shutil.ignore_patterns('*.lock','.git'))
index=E.parse(STAGE/'New Project.xml').getroot(); original={x.get('entityId') for x in index.findall('./EntityType/Entity')}
objects={}; origin={};script_remap={}
for file in (ROOT/'catalog/Data').glob('*/*.xml'):
    e=E.parse(file).getroot();id=e.find('Header').get('ID')
    if e.get('type')=='Script' and id in ('42420000-0000-4000-8000-000000000006','42420000-0000-4000-8000-000000000007'):
        new=uid('historical-script-'+id);script_remap[id]=new;e.find('Header').set('ID',new)
        for item in e.findall('./Properties/Item'):
            if item.get('key')=='script_id':item.set('value',new)
        id=new
    objects[id]=e
for file in (Path('C:/ETL/Samples/WebServices/StudioProject/ETLForge Web Service Examples/Data')).glob('*/*.xml'):
    e=E.parse(file).getroot();objects[e.find('Header').get('ID')]=e
for e in objects.values():
    for activity in e.findall('./Pipeline/Activities/SCRIPT'):
        if activity.get('contentId') in script_remap:activity.set('contentId',script_remap[activity.get('contentId')])
    for text in e.findall("./konga.string[@name='script']"):
        if text.text:
            for old,new in script_remap.items():text.text=text.text.replace('sc.'+old,'sc.'+new)
for line in (ROOT/'operations.txt').read_text(encoding='utf-8').splitlines():
    n,g,name,p,pname=line.split('|',4);origin[int(n)]=(g,name,int(p),pname)
sections={x.get('name'):x for x in index.findall('EntityType')}
groups={};files={};tables={};cases=[]
def row(table,columns,values):
    dest=tables.setdefault(table,{'columns':columns,'rows':[]});assert dest['columns']==columns;dest['rows'].append(values)
def add(e,group,clean=False):
    typ=e.get('type');h=e.find('Header');id=h.get('ID')
    if clean:h.set('DeployDirty','false');h.set('Deployed','true')
    dest=STAGE/f'Data/{typ}/{id}.xml';write(dest,e);files[id]=e
    section=sections.get(typ)
    if section is None:section=E.SubElement(index,'EntityType',name=typ);sections[typ]=section
    if not any(x.get('entityId')==id for x in section.findall('Entity')):E.SubElement(section,'Entity',entityId=id,name=h.get('Name'))
    groups[id]=group
for id,e in objects.items():
    if id not in original:add(copy.deepcopy(e),'Historical fixtures',True)

projectid=index.get('projectId')
row('InterchangeProjectsTab',['InterchangeProject_ID','InterchangeProject_Name'],[projectid,'Test Operations'])
OPCOL=['Operation_ID','Operation_Name','Operation_Type_ID','Source_ID','Target_ID','Ignore_History','Schedule_ID','Transformation_ID','Success_Operation_ID','Success_Message_ID','Failure_Operation_ID','Failure_Message_ID']
PIPECOL=['Operation_ID','ActivityType_ID','Activity_ID']
SCOL=['Script_ID','Script_Name','Script_Text']
import base64
for id in script_remap.values():
    e=objects[id];row('ScriptsTab',SCOL,[id,e.find('Header').get('Name'),base64.b64encode((e.find("./konga.string[@name='script']").text or '').encode()).decode()])
def addscriptop(name,body,key,group='Regression tests'):
    sc,op=uid(key+'script'),uid(key+'operation');code=body if body.startswith(('<javascript>','<trans>')) else '<trans>\n'+body+'\n</trans>'
    add(script(name+' script',code,sc),group);add(operation(name,sc,op),group)
    row('ScriptsTab',SCOL,[sc,name+' script',base64.b64encode(code.encode()).decode()])
    row('OperationsTab',OPCOL,[op,name,'8',None,None,'0',None,None,None,None,None,None]);row('OperationPipelines',PIPECOL,[op,'1',sc]);return op
def newendpoint(typ,name,location,key):
    id=uid(key);add(endpoint(typ,name,id,location),'Regression resources')
    cols=[typ+'_ID',typ+'_Name','Source_Type_ID','Locator','Path','File_Name','Login','Password','Driver','DriverType','ConnectString']
    vals=[id,name,'16',location.get('locator'),location.get('path'),location.get('file_name'),None,None,None,None,None]
    if typ=='Target':cols+=['Target_Target_ID'];vals+=['-1']
    row(typ+'sTab',cols,vals);return id
def cloneop(number):
    old,name,_,_=origin[number];e=copy.deepcopy(objects[old]);id=uid('fixture'+str(number));e.find('Header').set('ID',id);e.find('Header').set('Name','Fixture '+str(number)+' - '+name)
    p=props(e);p['schedule_id']=None;put(e,'ignore_history','1')
    add(e,'Regression fixtures');row('OperationsTab',OPCOL,[id,'Fixture '+str(number)+' - '+name,p.get('operation_type_id'),p.get('source_id'),p.get('target_id'),'1',None,p.get('transformation_id'),p.get('success_operation_id'),p.get('success_message_id'),p.get('failure_operation_id'),p.get('failure_message_id')])
    for a in e.findall('./Pipeline/Activities/*'):
        wire={'SCRIPT':'1','SOURCE':'3','TARGET':'4','TRANSFORMATION':'5','WEBSERVICECALL':'6','XSLT_TRANSFORM':'8'}[a.tag]
        row('OperationPipelines',PIPECOL,[id,wire,a.get('contentId')])
    return id
def archive_verifier(number,target,location):
    pattern='*.zip' if number==105 else '*.gz' if number==109 else 'bundle.'+{104:'zip',106:'7z',107:'zip',108:'tar.gz'}[number]
    source=newendpoint('Source',f'Archive verifier input {number}',{'locator':location['locator'],'file_name':pattern},'archive-verify-input'+str(number))
    e=files[source];lp=e.find('./Location/Data/Properties')
    E.SubElement(lp,'Item',key='compression_enabled',value='true')
    row('EntityProperties',['EntityType_ID','Entity_ID','KeyName','KeyValue'],['2',source,'compression_enabled','true'])
    if location.get('compression_password'):
        E.SubElement(lp,'Item',key='compression_password',value=location['compression_password']);row('EntityProperties',['EntityType_ID','Entity_ID','KeyName','KeyValue'],['2',source,'compression_password',location['compression_password']])
    write(STAGE/f'Data/Source/{source}.xml',e)
    folder=f'C:/ETL/Samples/RegressionSuite/archive-verification/{number}';Path(folder).mkdir(parents=True,exist_ok=True)
    dest=newendpoint('Target',f'Archive verifier output {number}',{'locator':folder,'file_name':'[file].[ext]'},'archive-verify-output'+str(number))
    op=uid('archive-verifier-operation'+str(number));e=entity('Operation',f'Verify archive members {number}',op);put(e,'operation_type_id',1);put(e,'ignore_history',1);put(e,'source_id',source);put(e,'target_id',dest)
    activities=E.SubElement(E.SubElement(e,'Pipeline',opType='Archive'),'Activities')
    for tag,id,role,typ,wire in [('SOURCE',source,'Source','2','3'),('TARGET',dest,'Target','3','4')]:
        E.SubElement(activities,tag,activityId=uid(op+tag),contentId=id,role=role,type=typ);row('OperationPipelines',PIPECOL,[op,wire,id])
    E.SubElement(e,'Siblings');E.SubElement(e,'Functions');add(e,'Regression verifiers');row('OperationsTab',OPCOL,[op,f'Verify archive members {number}','1',source,dest,'1',None,None,None,None,None,None])
    return op,folder
reportdir='C:/ETL/Samples/RegressionSuite/results';Path(reportdir).mkdir(parents=True,exist_ok=True)
reporttarget=newendpoint('Target','Regression summary CSV',{'locator':reportdir,'file_name':'latest.csv'},'report-target')
selected=[3,23,24,29,30,31,32,33,34,38,39,*range(46,55),55,*range(58,64),*range(65,71),*range(103,118),168,169,170,171,172,173,174,175,*range(184,192),*range(222,229),230,231,233,234,17]
negative={39:'cleanup intentional failure',53:'time',61:'variable',62:'text',68:'UTF',69:'NUL',70:'file',110:'GZIP',112:'passphrase',113:'archive',170:'fingerprint',171:'key',172:'fingerprint',174:'key',188:'pgp',189:'pgp',225:'S3',227:'S3'}
for number in selected:
    op=cloneop(number);name=origin[number][1];expected=number in negative;label=f'{number:03d} - {name}'
    run="ok=RunOperation('op."+op+"',true);error=GetLastError();"
    if expected:
        marker=negative[number];condition='!ok'+(" && Index(ToLower(error),"+q(marker.lower())+")>=0" if marker else '')
    else:condition='ok'
    checks=''
    # These assertions read current output through a dedicated local source.
    fixture=objects[origin[number][0]];p=props(fixture);target=objects.get(p.get('target_id',''))
    if target is not None and not expected and number!=231 and target.find('Location').get('type')=='LocalFile':
        location=loc(target);filename=location.get('file_name','')
        if filename and not any(x in filename for x in '[*?') and not location.get('compression_enabled')=='true' and not location.get('pgp_enabled')=='true':
            reader=newendpoint('Source',label+' verification output',{'locator':location.get('locator',''),'file_name':filename},'verify'+str(number))
            marker='ETLFORGE_REGRESSION_OUTPUT_NOT_PRODUCED'
            seed=newendpoint('Target',label+' output freshness marker',{'locator':location.get('locator',''),'file_name':filename},'seed'+str(number))
            run="WriteFile('t."+seed+"',"+q(marker)+");FlushFile('t."+seed+"');"+run
            checks="If(ok,contents=Base64EncodeFile('s."+reader+"');If(Length(contents)==0 || contents==Base64Encode("+q(marker)+"),RaiseError('Fresh output was not produced')));"
            if number==234:
                src=objects.get(p.get('source_id'));checks="If(ok,If(MD5(Base64EncodeFile('s."+reader+"'))!=MD5(Base64EncodeFile('s."+src.find('Header').get('ID')+"')),RaiseError('UTF8 file bytes differ')));"
    if number in (47,49,52):
        suffix={47:('result0.xml','result1.xml'),49:('result2.json','result3.json'),52:('result4.csv','result5.csv')}[number]
        readers=[newendpoint('Source',label+' parity '+leaf,{'locator':'C:/ETL/Samples/NativeJavaScriptMapping/output','file_name':leaf},'parity'+str(number)+leaf) for leaf in suffix]
        checks+="If(ok,If(MD5(Base64EncodeFile('s."+readers[0]+"'))!=MD5(Base64EncodeFile('s."+readers[1]+"')),RaiseError('JavaScript/trans output bytes differ')));"
    if number in (58,59,60,63,65,66):
        reader=newendpoint('Source',label+' exact output',{'locator':'C:/ETL/Samples/GlobalVariableEndpoints','file_name':'result.txt'},'global-exact'+str(number))
        expected_text={58:'café 世界 😀',59:'café 世界 😀\r\nsecond line',60:'[]',63:'new value',65:'café 世界 😀\r\nsecond line\t"quotes"',66:'café 世界 😀'}[number]
        checks+="If(ok,If(ReadFile('s."+reader+"')!="+q(expected_text)+",RaiseError('Global variable output differs')));"
    if number==55:
        for n,value in [(20,json.dumps('  café 世界 😀\nline two  ',ensure_ascii=False)),(21,'0:$PVText <trans>1+2</trans>'),(22,'runtime-script'),(23,'runtime-script'),(24,'runtime-script')]:
            location={'locator':'C:/ETL/Samples/ProjectVariables','file_name':f'result{n}.txt'}
            reader=newendpoint('Source',f'Preset verification {n}',location,'preset-reader'+str(n));seed=newendpoint('Target',f'Preset freshness marker {n}',location,'preset-seed'+str(n))
            run="WriteFile('t."+seed+"','NOT_PRODUCED');FlushFile('t."+seed+"');"+run
            checks+="If(ok,If(ReadFile('s."+reader+"')!="+q(value)+",RaiseError('Project variable preset/inheritance output differs "+str(n)+"')));"
    if number==54:checks+="If(ok,If($localIsolation!='isolated',RaiseError('Local variable leaked into child')));"
    if number==231:run="If(Length(FileList('s."+p['source_id']+"'))!=0,RaiseError('Empty S3 prefix fixture is not empty'));"+run
    if number in (103,104,105,106,107,108,109,111,115,116,117,168,169,173,185,187,191,228):
        pairs=[];verify_run=''
        if number in (104,105,106,107,108,109):
            verif,folder=archive_verifier(number,target,loc(target));verify_run="If(ok,If(!RunOperation('op."+verif+"',true),RaiseError('Archive cannot be extracted')));"
            archive_names=['entry_alpha.dat.zip','entry_beta.dat.zip'] if number==105 else ['entry_alpha.dat.gz','entry_beta.dat.gz'] if number==109 else ['bundle.'+{104:'zip',106:'7z',107:'zip',108:'tar.gz'}[number]]
            for leaf in archive_names:
                seed=newendpoint('Target',f'Fresh archive {number} {leaf}',{'locator':loc(target)['locator'],'file_name':leaf},f'archive-seed-{number}-{leaf}')
                run="WriteFile('t."+seed+"','NOT_PRODUCED');FlushFile('t."+seed+"');"+run
            reference=loc(objects[props(objects[origin[103][0]])['source_id']])['locator']
            pairs=[(folder,'entry_alpha.dat',reference,'alpha.dat'),(folder,'entry_beta.dat',reference,'beta.dat')]
        elif number in (103,111,115,116,117):
            folder=loc(target)['locator'];reference=loc(objects[p['source_id']])['locator']
            pairs=[(folder,'entry_alpha.dat',reference,'alpha.dat'),(folder,'entry_beta.dat',reference,'beta.dat')] if number in (103,111,115) else [(folder,'entry_payload.txt',reference,'alpha.dat')] if number==116 else []
            if number==117:
                ref=Path('C:/ETL/Samples/RegressionSuite/references');ref.mkdir(parents=True,exist_ok=True);(ref/'tar.txt').write_bytes(b'hello');pairs=[(folder,'entry_input.txt',ref.as_posix(),'tar.txt')]
        elif number in (168,169,173):
            reference='C:/ETL/Samples/SftpKeys/live-b5b3b329-b750-4f44-8548-bebe2ee7f565';folder=reference+'/output' if number==169 else loc(target)['locator'];pairs=[(folder,'result-1.bin' if number==169 else loc(target)['file_name'],reference+'/input','payload.bin')]
        elif number in (185,187,191):
            reference='C:/ETL/Reports/pgp-compare/live-8504884208248915715/input';folder=loc(target)['locator'];pairs=[(folder,'file_a.bin',reference,'a.bin'),(folder,'file_b.bin',reference,'b.bin')] if number==187 else [(folder,loc(target)['file_name'],reference,'a.bin')]
        elif number==228:pairs=[(loc(target)['locator'],'payload.bin','C:/ETL/Samples/S3/live','input.bin'),(loc(target)['locator'],'unicode.txt','C:/ETL/Samples/S3/live','unicode.txt')]
        seed_code='';pair_checks=''
        for j,(folder,leaf,reference,refleaf) in enumerate(pairs):
            actual=newendpoint('Source',f'Exact member {number} {j}',{'locator':folder,'file_name':leaf},f'exact-reader-{number}-{j}')
            expected_source=newendpoint('Source',f'Reference member {number} {j}',{'locator':reference,'file_name':refleaf},f'exact-reference-{number}-{j}')
            seed=newendpoint('Target',f'Fresh member {number} {j}',{'locator':folder,'file_name':leaf},f'exact-seed-{number}-{j}')
            seed_code+="WriteFile('t."+seed+"','NOT_PRODUCED');FlushFile('t."+seed+"');"
            pair_checks+="If(ok,If(MD5(Base64EncodeFile('s."+actual+"'))!=MD5(Base64EncodeFile('s."+expected_source+"')),RaiseError('Output member bytes differ')));"
        run=seed_code+run;checks=verify_run+checks+pair_checks
    body=run+checks+'If('+condition+',WriteToOperationLog('+q('PASS | '+label)+'),WriteToOperationLog('+q('FAIL | '+label)+"+' | '+error);RaiseError("+q('Regression failed: '+label)+'));true;'
    wrapper=addscriptop(label,body,'case'+str(number));cases.append({'number':number,'name':name,'operation':wrapper,'fixture':op,'expectedFailure':expected,'expectedErrorContains':negative.get(number),'assertion':'expected error signature' if expected else 'execution and output' if checks else 'execution'})

# Independent local-variable and mixed-language checks do not depend on old fixtures.
body="x=1;x+=2;If(x!=3,RaiseError('Local assignment failed'));$RegressionGlobal='global';x='local';If($RegressionGlobal!='global',RaiseError('Global/local collision'));WriteToOperationLog('PASS | Local variables and assignment-before-read');true;"
cases.append({'name':'Local variables and assignment-before-read','operation':addscriptop('Local variables and assignment-before-read',body,'locals')})
invalid=addscriptop('Fixture - unassigned local is rejected','unassignedLocal;','unassigned-fixture','Regression fixtures')
body="ok=RunOperation('op."+invalid+"',true);If(ok || Index(ToLower(GetLastError()),'assigned')<0,RaiseError('Unassigned local was not rejected'));WriteToOperationLog('PASS | Unassigned local is rejected');true;"
cases.append({'name':'Unassigned local is rejected','operation':addscriptop('Unassigned local is rejected',body,'unassigned-check')})
body="<javascript>ETLForge.SetVar('RegressionJavaScript',42);</javascript><trans>If($RegressionJavaScript!=42,RaiseError('Mixed block global mismatch'));WriteToOperationLog('PASS | Mixed JavaScript and trans blocks');</trans>"
cases.append({'name':'Mixed JavaScript and trans blocks','operation':addscriptop('Mixed JavaScript and trans blocks',body,'mixed')})
script_cases_path=ROOT/'script-cases.json'
if script_cases_path.exists():
    for language,tests in json.loads(script_cases_path.read_text(encoding='utf-8')).items():
        for label,text,expected in tests:
            name=language+' - '+label;key='language-'+language+'-'+label;sc=uid(key+'fixture')
            code='<trans>'+text+'</trans>' if language=='local' else text
            add(script('Fixture '+name,code,sc),'Regression scripting fixtures')
            row('ScriptsTab',SCOL,[sc,'Fixture '+name,base64.b64encode(code.encode()).decode()])
            call="RunScript('sc."+sc+"','fixture-argument')"
            if expected=='ERROR':
                marker='__REGRESSION_EXPECTED_REJECTION__'
                body='actual=Eval('+call+','+q(marker)+');If(String(actual)!='+q(marker)+",RaiseError('Expected script rejection'));"
                if language=='local':
                    negativeop=uid(key+'negativeoperation');add(operation('Fixture '+name,sc,negativeop),'Regression scripting fixtures')
                    row('OperationsTab',OPCOL,[negativeop,'Fixture '+name,'8',None,None,'1',None,None,None,None,None,None]);row('OperationPipelines',PIPECOL,[negativeop,'1',sc])
                    body="ok=RunOperation('op."+negativeop+"',true);If(ok || Index(ToLower(GetLastError()),'assign')<0,RaiseError('Unexpected local-variable failure reason'));"
            else:body='actual=String('+call+');If(actual!='+q(expected)+",RaiseError('Script result differs from expected value'));"
            body+='WriteToOperationLog('+q('PASS | '+name)+');true;'
            cases.append({'name':name,'operation':addscriptop(name,body,key+'check'),'expectedFailure':expected=='ERROR','assertion':'expected rejection' if expected=='ERROR' else 'exact script result'})
for name,value in [('PVText','  café 世界 😀\nline two  '),('PVEmpty',''),('PVLiteral','$PVText <trans>1+2</trans>')]:
    id=uid('preset'+name);e=entity('ProjectVariable',name,id);put(e,'default_value',value);put(e,'is_password','0');add(e,'Regression resources')
    for k,v in [('projectvariable_name',name),('default_value_utf8',base64.b64encode(value.encode()).decode())]:row('EntityProperties',['EntityType_ID','Entity_ID','KeyName','KeyValue'],['35',id,k,v])

def s3archive(name,local_file,remote_folder,remote_name,key,cleanup=False):
    template=objects[origin[222 if cleanup else 223][0]]; p=props(template)
    old=objects[p['source_id' if cleanup else 'target_id']]
    e=copy.deepcopy(old); id=uid(key+'s3');e.set('type','Source' if cleanup else 'Target');h=e.find('Header');h.set('ID',id);h.set('Name',name+' S3 endpoint')
    lp=e.find('./Location/Data/Properties')
    values=loc(e);prefix=values['path'].split('input/')[0] if cleanup else values['path'].split('output/')[0]
    values['path']=prefix+remote_folder;values['file_name']=remote_name;values['s3_overwrite']='true';values['s3_recursive']='true'
    if cleanup:values['renamefileto']='/\\delete/\\'
    lp.clear()
    for k,v in values.items():E.SubElement(lp,'Item',key=k,value=v)
    add(e,'Regression resources')
    typ='Source' if cleanup else 'Target'
    cols=[typ+'_ID',typ+'_Name','Source_Type_ID','Locator','Path','File_Name','Login','Password','Driver','DriverType','ConnectString'];vals=[id,h.get('Name'),'18',values['locator'],values['path'],remote_name,None,None,None,None,None]
    if cleanup:cols+=['RenameFileTo'];vals+=['/\\delete/\\']
    else:cols+=['Target_Target_ID'];vals+=['-1']
    # Keep the source/target table shape identical to the local endpoints.
    basecols=cols[:11]+([] if cleanup else ['Target_Target_ID']);basevals=vals[:11]+([] if cleanup else ['-1'])
    if cleanup:
        current=tables['SourcesTab']
        if 'RenameFileTo' not in current['columns']:
            current['columns'].append('RenameFileTo')
            for previous in current['rows']:previous.append(None)
        basecols+=['RenameFileTo'];basevals+=['/\\delete/\\']
    row(typ+'sTab',basecols,basevals)
    for k,v in values.items():
        if k.startswith('s3_') or (cleanup and k=='renamefileto'):row('EntityProperties',['EntityType_ID','Entity_ID','KeyName','KeyValue'],['2' if cleanup else '3',id,k,v])
    if cleanup:
        local=newendpoint('Target',name+' local sink',{'locator':'C:/ETL/Samples/RegressionSuite/s3-cleanup','file_name':'[unique].bin'},key+'local');source,target=id,local
        data=files[local].find('./Location/Data/Properties')
        for k,v in [('compression_enabled','true'),('compression_format','zip'),('compression_mode','combined'),('compression_archive_name','s3-cleanup-last')]:
            E.SubElement(data,'Item',key=k,value=v);row('EntityProperties',['EntityType_ID','Entity_ID','KeyName','KeyValue'],['3',local,k,v])
        write(STAGE/f'Data/Target/{local}.xml',files[local])
    else:
        local=newendpoint('Source',name+' input',{'locator':str(local_file.parent).replace('\\','/'),'file_name':local_file.name},key+'local');source,target=local,id
    opid=uid(key+'archive');op=entity('Operation',name+' transfer',opid);put(op,'operation_type_id',1);put(op,'source_id',source);put(op,'target_id',target)
    a=E.SubElement(E.SubElement(op,'Pipeline',opType='Archive'),'Activities');E.SubElement(a,'SOURCE',activityId=uid(key+'srcactivity'),contentId=source,role='Source',type='2');E.SubElement(a,'TARGET',activityId=uid(key+'tgtactivity'),contentId=target,role='Target',type='3');E.SubElement(op,'Siblings');E.SubElement(op,'Functions');add(op,'Regression fixtures')
    put(op,'ignore_history','1');write(STAGE/f'Data/Operation/{opid}.xml',op)
    row('OperationsTab',OPCOL,[opid,name+' transfer','1',source,target,'1',None,None,None,None,None,None]);row('OperationPipelines',PIPECOL,[opid,'3',source]);row('OperationPipelines',PIPECOL,[opid,'4',target])
    body="If(!RunOperation('op."+opid+"',true),RaiseError("+q(name+' failed')+"));"+("If(Length(FileList('s."+source+"'))!=0,RaiseError('S3 fixtures remain after cleanup'));" if cleanup else '')+"WriteToOperationLog("+q('PASS | '+name)+");true;"
    return {'name':name,'operation':addscriptop(name,body,key+'check')}

prep=[]
for leaf,folder,target in [('input.bin','input/','payload.bin'),('unicode.txt','input/nested/','unicode.txt'),('input.bin','delete-after/','payload.bin'),('utf16-input.xml','xml/','input.xml')]:
    prep.append(s3archive('Prepare S3 '+folder+target,Path('C:/ETL/Samples/S3/live')/leaf,folder,target,'s3prep'+folder+target))
cases=prep+cases
Path('C:/ETL/Samples/RegressionSuite/s3-cleanup').mkdir(parents=True,exist_ok=True)
cases.append(s3archive('Clean S3 regression prefix',None,'','*','s3cleanup',True))

calls='{'+','.join(q('op.'+x['operation']) for x in cases)+'}'
names='{'+','.join(q(x.get('name','')) for x in cases)+'}'
masterbody='tests='+calls+';names='+names+";i=0;passed=0;failed=0;csv='Test,Result\\r\\n';WriteToOperationLog('REGRESSION START');While(i<Length(tests),ok=Eval(RunOperation(tests[i],true),false);If(ok,passed++;result='PASS',failed++;result='FAIL');WriteToOperationLog(result+' | '+names[i]);csv+= '\"'+Replace(names[i],'\"','\"\"')+'\",'+result+'\\r\\n';i++);WriteFile('t."+reporttarget+"',csv);FlushFile('t."+reporttarget+"');WriteToOperationLog('REGRESSION SUMMARY | PASS='+String(passed)+' | FAIL='+String(failed));If(failed>0,RaiseError('Regression failures: '+String(failed)));true;"
master=addscriptop('00 - Run All Regression Tests',masterbody,'master','Regression runner')
write(STAGE/'New Project.xml',index)
(ROOT/'deploy.json').write_text(json.dumps({'tables':tables,'master':master,'cases':cases},indent=2),encoding='utf-8')
(ROOT/'groups.json').write_text(json.dumps(groups),encoding='utf-8')
(ROOT/'manifest.json').write_text(json.dumps({'master':master,'project':projectid,'cases':cases,'historicalOperations':len(origin),'importedEntities':len(groups)},indent=2),encoding='utf-8')
print('Staged',len(origin),'historical operations;',len(cases),'repeatable cases; master',master)
