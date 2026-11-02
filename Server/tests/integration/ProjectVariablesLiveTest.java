import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.lang.reflect.*;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.entity.id.ProjectVariableId;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl.DeployInterchangeModelWsImplWrapper;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;
public class ProjectVariablesLiveTest extends LocalVariablesRuntimeTest {
 static final Path ROOT=Paths.get("C:/ETL/Samples/ProjectVariables");
 static String id(int n){return "42480000-0000-4000-8000-"+String.format("%012d",n);}
 static int checks;
 static com.fasterxml.jackson.databind.JsonNode audit(com.fasterxml.jackson.databind.node.ObjectNode request)throws Exception{
  Method call=org.etlforge.integration.client.ui.admin.audit.AuditLogService.class.getDeclaredMethod("invokeRequest",String.class,String.class,String.class,com.fasterxml.jackson.databind.node.ObjectNode.class);
  call.setAccessible(true);return (com.fasterxml.jackson.databind.JsonNode)call.invoke(null,"http://localhost:47100/axis/konga_admin","Admin",hash,request);
 }
 static InterchangeDataTable table(String[] names,String[]... rows){InterchangeDataRow[] values=new InterchangeDataRow[rows.length];for(int i=0;i<rows.length;i++)values[i]=new InterchangeDataRow(rows[i]);return new InterchangeDataTable(names,values);}
 static void check(boolean ok,String name){if(!ok)throw new AssertionError(name);checks++;System.out.println("PASS "+name);}
 static InterchangeDataHolder data(int project){
  InterchangeDataHolder data=new InterchangeDataHolder();data.setInterchangeProjectsTab(table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{id(project),"ETLForge Project Variables Verification "+project}));return data;
 }
 static void deploy(InterchangeDataHolder data)throws Exception{
  CDeployResults result=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data")).deployInterchangeData("Admin",hash,new InterchangeIdHolder(),data);
  if(result==null||result.getDeployStatus()>1)throw new AssertionError("Project variables test deployment failed");
 }
 static void presets(InterchangeDataHolder data,String value)throws Exception{
  DeployInterchangeModelWsImplWrapper wrapper=new DeployInterchangeModelWsImplWrapper(null,null);
  Method update=wrapper.getClass().getDeclaredMethod("updateEntity",IntegrationEntity.class);update.setAccessible(true);
  String[][] values={{"PVText",value},{"PVEmpty",""},{"PVLiteral","$PVText <trans>1+2</trans>"}};
  for(int i=0;i<values.length;i++){
   ProjectVariable variable=new ProjectVariable(new ProjectVariableId(id(10+i)),values[i][0]);variable.setDefaultValue(values[i][1]);update.invoke(wrapper,variable);
  }
  Field field=wrapper.getClass().getDeclaredField("m_extendedEntityProperties");field.setAccessible(true);List<InterchangeDataRow> rows=(List<InterchangeDataRow>)field.get(wrapper);
  data.setEntityProperties(new InterchangeDataTable(new String[]{"EntityType_ID","Entity_ID","KeyName","KeyValue"},rows.toArray(new InterchangeDataRow[0])));
 }
 static String file(int target,String expression){return "WriteFile('t."+id(target)+"',"+expression+");FlushFile('t."+id(target)+"');";}
 static String[] script(int n,String name,String code){return new String[]{id(n),name,Base64.getEncoder().encodeToString(code.getBytes(StandardCharsets.UTF_8))};}
 static void setup(String value)throws Exception{
  InterchangeDataHolder data=data(1);presets(data,value);
  List<String[]> targets=new ArrayList<>();for(int i=20;i<=24;i++)targets.add(new String[]{id(i),"Project variable output "+i,"16",ROOT.toString(),"result"+i+".txt",null,null,null,null,null,"-1"});
  data.setTargetsTab(table(new String[]{"Target_ID","Target_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString","Target_Target_ID"},targets.toArray(new String[0][])));
  String root="<javascript>ETLForge.WriteFile('t."+id(20)+"',JSON.stringify(ETLForge.GetVar('PVText')));</javascript><trans>FlushFile('t."+id(20)+"');"+file(21,"String(Length($PVEmpty))+':'+$PVLiteral")+"$PVText='runtime';RunScript('sc."+id(31)+"');RunOperation('op."+id(41)+"',true);</trans><javascript>ETLForge.WriteFile('t."+id(24)+"',ETLForge.GetVar('PVText'));</javascript><trans>FlushFile('t."+id(24)+"');</trans>";
  String childScript="<javascript>ETLForge.SetVar('PVText',ETLForge.GetVar('PVText')+'-script');ETLForge.WriteFile('t."+id(22)+"',ETLForge.GetVar('PVText'));</javascript><trans>FlushFile('t."+id(22)+"');</trans>";
  data.setScriptsTab(table(new String[]{"Script_ID","Script_Name","Script_Text"},script(30,"Read and override project presets",root),script(31,"Child script inherits project variable",childScript),script(32,"Child operation inherits project variable","<trans>"+file(23,"$PVText")+"</trans>")));
  data.setOperationsTab(table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},new String[]{id(40),"Project variable initialization","8",null,null,null,null,null,null,null,null},new String[]{id(41),"Project variable child operation","8",null,null,null,null,null,null,null,null}));
  data.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},new String[]{id(40),"1",id(30)},new String[]{id(41),"1",id(32)}));deploy(data);
 }
 static long run(int operation)throws Exception{
  JobControl jobs=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));
  WsRunOperationResult result=jobs.runOperationInstanceMD5Password("Admin",hash,id(operation));long instance=result.getOperationInstanceId();
  for(int n=0;n<300;n++){
   String state=NativeJavaScriptMappingTest.status(instance);if(state.equals("3")||state.equals("4")||state.equals("5")){System.out.println("completed instance="+instance);return instance;}
   if(!state.isEmpty()&&!state.equals("0")&&!state.equals("2"))throw new AssertionError("Project variable operation failed, instance="+instance+", status="+state);Thread.sleep(100);
  }throw new AssertionError("Operation timeout instance="+instance);
 }
 static String output(int n)throws Exception{String text=new String(Files.readAllBytes(ROOT.resolve("result"+n+".txt")),StandardCharsets.UTF_8);return n==20 && !text.isEmpty() && text.charAt(0)==34?new com.fasterxml.jackson.databind.ObjectMapper().readValue(text,String.class):text;}
 static void clear()throws Exception{for(int n=20;n<=24;n++)Files.deleteIfExists(ROOT.resolve("result"+n+".txt"));}
 public static void main(String[] args)throws Exception{
  init();if(args.length>0){InterchangeDataHolder minimal=data(1);if(args[0].equals("variable"))presets(minimal,"ascii");deploy(minimal);System.out.println("minimal deployment passed");return;}Files.createDirectories(ROOT);String preset="  café 世界 😀\nline two  ";setup(preset);check(true,"deploy Studio-serialized defaults with operation");
  clear();run(40);check(output(20).equals(preset),"preset loaded before root script with exact UTF-8 whitespace");
  check(output(21).equals("0:$PVText <trans>1+2</trans>"),"empty presets defined and values are literal, never evaluated");
  check(output(22).equals("runtime-script"),"JavaScript child script receives mutable runtime global");
  check(output(23).equals("runtime-script"),"child operation preserves inherited override");
  check(output(24).equals("runtime-script"),"trans and JavaScript share project globals");
  clear();run(40);check(output(20).equals(preset),"independent operation starts from preset again");
  InterchangeDataHolder update=data(1);presets(update,"redeployed 世界");deploy(update);clear();run(40);
  check(output(20).equals("redeployed 世界"),"preset-only deployment updates next run");
  InterchangeDataHolder bad=data(1);bad.setEntityProperties(table(new String[]{"EntityType_ID","Entity_ID","KeyName","KeyValue"},new String[]{"35",id(13),"projectvariable_name","etlforge.operation.name"},new String[]{"35",id(13),"default_value","bad"}));
  boolean rejected=false;try{deploy(bad);}catch(Exception expected){rejected=true;}check(rejected,"server rejects reserved system variable names");
  bad=data(1);bad.setEntityProperties(table(new String[]{"EntityType_ID","Entity_ID","KeyName","KeyValue"},new String[]{"35",id(13),"projectvariable_name","PVText"},new String[]{"35",id(13),"default_value","duplicate"}));
  rejected=false;try{deploy(bad);}catch(Exception expected){rejected=true;}check(rejected,"server rejects duplicate names atomically");
  clear();run(40);check(output(20).equals("redeployed 世界"),"failed deployment does not alter valid presets");
  InterchangeDataHolder remove=data(1);remove.setEntityProperties(table(new String[]{"EntityType_ID","Entity_ID","KeyName","KeyValue"},new String[]{"35",id(10),"__deleted","1"}));deploy(remove);
  remove=data(1);remove.setScriptsTab(table(new String[]{"Script_ID","Script_Name","Script_Text"},script(30,"Read and override project presets","<trans>"+file(20,"String(IsNull($PVText))")+"</trans>")));deploy(remove);clear();run(40);
  check(output(20).equals("1"),"deleted project variable no longer initialized");
  setup(preset);
  com.fasterxml.jackson.databind.ObjectMapper json=new com.fasterxml.jackson.databind.ObjectMapper();
  com.fasterxml.jackson.databind.node.ObjectNode query=json.createObjectNode().put("action","list").put("projectGuid",id(1)).put("objectType",35).put("search","PVText").put("days",30).put("limit",100);
  com.fasterxml.jackson.databind.JsonNode entries=audit(query).path("entries");
  check(entries.size()>0,"project variable changes appear in audit history");
  com.fasterxml.jackson.databind.JsonNode detail=audit(json.createObjectNode().put("action","detail").put("eventId",entries.get(0).path("id").asLong()));
  check(detail.path("current").path("fields").path("default_value").asText().equals(preset),"audit displays exact Unicode preset without ANSI conversion");
  int count=entries.size();setup(preset);
  entries=audit(query).path("entries");
  check(entries.size()==count,"unchanged preset redeploy creates no audit event");
  InterchangeDataHolder isolated=data(2);
  isolated.setEntityProperties(table(new String[]{"EntityType_ID","Entity_ID","KeyName","KeyValue"},new String[]{"35",id(14),"projectvariable_name","PVText"},new String[]{"35",id(14),"default_value_utf8",Base64.getEncoder().encodeToString("other project".getBytes(StandardCharsets.UTF_8))}));
  isolated.setTargetsTab(table(new String[]{"Target_ID","Target_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString","Target_Target_ID"},new String[]{id(25),"Isolated project output","16",ROOT.toString(),"result25.txt",null,null,null,null,null,"-1"}));
  isolated.setScriptsTab(table(new String[]{"Script_ID","Script_Name","Script_Text"},script(33,"Isolated project preset","<trans>"+file(25,"$PVText")+"</trans>")));
  isolated.setOperationsTab(table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},new String[]{id(42),"Isolated project operation","8",null,null,null,null,null,null,null,null}));
  isolated.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},new String[]{id(42),"1",id(33)}));deploy(isolated);Files.deleteIfExists(ROOT.resolve("result25.txt"));run(42);
  check(output(25).equals("other project"),"same variable name has an independent default in another project");
  clear();run(40);check(output(20).equals(preset),"another project does not replace this project's preset");
  org.etlforge.integration.data.project.IntegrationProject editor=org.etlforge.integration.data.project.IntegrationProject.createNewProject("Editor Test preset fixture");
  ProjectVariable editorVariable=new ProjectVariable("PVText");editorVariable.setDefaultValue(preset);editorVariable.setParent(editor.getRootFolder(EntityType.ProjectVariable));
  org.etlforge.integration.client.ui.script.debug.ScriptDebugModel debug=new org.etlforge.integration.client.ui.script.debug.ScriptDebugModel(new org.etlforge.integration.debug.client.DebugService(),new org.etlforge.integration.data.entity.id.ScriptId());debug.setProject(editor);
  Method seed=debug.getClass().getDeclaredMethod("projectDefaults",Collection.class);seed.setAccessible(true);
  org.etlforge.integration.debug.client.DebuggingParametersBuilder parameters=new org.etlforge.integration.debug.client.DebuggingParametersBuilder();parameters.setSessionId(org.etlforge.util.id.KongaGuid.createGuid()).setCommand("Run");parameters.setDataElements((Collection<org.etlforge.integration.debug.client.de.DataElement>)seed.invoke(debug,Collections.emptyList()));
  Method build=Class.forName("org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl.WsDebuggingParametersBuilder").getDeclaredMethod("build",org.etlforge.integration.debug.client.DebuggingParameters.class);build.setAccessible(true);
  org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.WsTestResult test=api.testExpression("Admin",hash,"",Base64.getEncoder().encodeToString("<javascript>SetScriptResult(ETLForge.GetVar('PVText'));</javascript>".getBytes(StandardCharsets.UTF_8)),(org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.DebuggingParameters)build.invoke(null,parameters.build()));
  check((test.getErrors()==null||test.getErrors().isEmpty())&&new String(Base64.getMimeDecoder().decode(test.getResult()),StandardCharsets.UTF_8).equals(preset),"Studio Test uses the edited project preset through the live server");
  System.out.println("Passed "+checks+" live project variable checks; sample restored to Unicode preset");
 }
}
