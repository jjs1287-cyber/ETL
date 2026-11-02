import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.projectadmin.client.*;
public final class SchedulerConsoleSmoke {
 static final String PROJECT="42430000-0000-4000-8000-000000000001",SCHEDULE="42430000-0000-4000-8000-000000000002",OPERATION="42430000-0000-4000-8000-000000000003",SCRIPT="42430000-0000-4000-8000-000000000004";
 static String hash;static ProjectAdministration admin;static int checks;
 static InterchangeDataTable table(String[] columns,String[]...values){InterchangeDataRow[] rows=new InterchangeDataRow[values.length];for(int i=0;i<rows.length;i++)rows[i]=new InterchangeDataRow(values[i]);return new InterchangeDataTable(columns,rows);}
 static void require(boolean ok,String message){if(!ok)throw new AssertionError(message);checks++;}
 static InterchangeDataHolder fixture(String time,boolean disabled){
  InterchangeDataHolder data=new InterchangeDataHolder();data.setInterchangeProjectsTab(table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{PROJECT,"Scheduler Console Verification"}));
  String content="<trans>WriteToOperationLog(\"Scheduler console verification executed\");</trans>";
  data.setScriptsTab(table(new String[]{"Script_ID","Script_Name","Script_Text"},new String[]{SCRIPT,"Scheduler verification script",Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8))}));
  data.setSchedulesTab(table(new String[]{"Schedule_ID","Schedule_Name","occurs","daily","weekly","mon","tue","wed","thu","fri","sat","sun","monthly","monthly_a_day","monthly_a_of_every_x_month","monthly_b_num","monthly_b_day","monthly_b_of_every_x_month","frequency","frequency_once_time","frequency_every_num","frequency_every_hm","frequency_start_time","frequency_end_time","duration_start_date","duration_end_date","duration_yesno_end","priority"},new String[]{SCHEDULE,"Scheduler console test","1","1","1","1","1","1","1","1","1","1","0","1","1","1","1","1","1",time,"1","1","00:00:00","23:59:59","2020-01-01","2099-12-31","0","0"}));
  data.setOperationsTab(table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Ignore_History","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},new String[]{OPERATION,"Scheduler console test operation","8",null,null,"0",SCHEDULE,null,null,null,null,null}));
  data.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},new String[]{OPERATION,"1",SCRIPT}));
  if(disabled)data.setEntityProperties(table(new String[]{"EntityType_ID","Entity_ID","KeyName","KeyValue"},new String[]{"5",SCHEDULE,"enabled","0"}));return data;
 }
 static void deploy(String time,boolean disabled)throws Exception {DeployInterchangeData service=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data"));CDeployResults result=service.deployInterchangeData("Admin",hash,new InterchangeIdHolder(),fixture(time,disabled));require(result!=null&&result.getDeployStatus()<=1,"Fixture deployment failed");}
 static ScheduledOperation[] find(String[] schedules,String[] operations,String start,String end)throws Exception {FindScheduledOperationsResult result=admin.findScheduledOperations("Admin",hash,schedules,operations,start,end);require(result!=null,"Missing forecast result");return result.getScheduledOperations()==null?new ScheduledOperation[0]:result.getScheduledOperations();}
 public static void main(String[] args)throws Exception {
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();StringBuilder digest=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))digest.append(String.format("%02x",b&255));hash=digest.toString();admin=new ProjectAdministrationServiceLocator().getkonga_project_administration(new URL("http://127.0.0.1:47100/axis/konga_project_administration"));((org.apache.axis.client.Stub)admin).setTimeout(60000);
  if(args.length>0&&args[0].equals("disable")){admin.disableSchedules("Admin",hash,new String[]{SCHEDULE});admin.disableScheduleForOperation("Admin",hash,new String[]{OPERATION});System.out.println("PASS: verification schedule and operation scheduling disabled.");return;}
  if(args.length>0&&args[0].equals("redeploy-disabled")){deploy("03:04:05",true);admin.disableSchedules("Admin",hash,new String[]{SCHEDULE});admin.disableScheduleForOperation("Admin",hash,new String[]{OPERATION});System.out.println("PASS: deployment after scheduler controls preserves the audit baseline; verification schedule remains disabled.");return;}
  boolean armed=false;
  try {
   find(null,null,"2030-01-02 00:00:00","2030-01-02 23:59:59");deploy("03:04:05",true);
   String start="2030-01-02 00:00:00",end="2030-01-02 23:59:59";
   require(find(new String[]{SCHEDULE},null,start,end).length==0,"Disabled schedule appeared");
   admin.enableSchedules("Admin",hash,new String[]{SCHEDULE});admin.enableScheduleForOperation("Admin",hash,new String[]{OPERATION});
   ScheduledOperation[] runs=find(new String[]{SCHEDULE},new String[]{OPERATION},start,end);require(runs.length==1,"Expected one scheduled operation");require(OPERATION.equals(runs[0].getOperationGuid()),"Wrong operation returned");require(SCHEDULE.equals(runs[0].getScheduleGuid()),"Wrong schedule returned");require(runs[0].getISO8601ExecutionTimes().length==1,"Expected one daily execution");require("2030-01-02 03:04:05".equals(runs[0].getISO8601ExecutionTimes()[0]),"Wrong daily execution time");
   FindScheduledOperationsResult preview=admin.testScheduleWithGuid("Admin",hash,SCHEDULE,start,end,2);require(preview!=null&&preview.getScheduledOperations()!=null,"Schedule preview missing");require(Arrays.equals(runs[0].getISO8601ExecutionTimes(),preview.getScheduledOperations()[0].getISO8601ExecutionTimes()),"Forecast differs from existing schedule preview");
   admin.disableScheduleForOperation("Admin",hash,new String[]{OPERATION});require(find(null,new String[]{OPERATION},start,end).length==0,"Disabled operation appeared");admin.enableScheduleForOperation("Admin",hash,new String[]{OPERATION});
   admin.disableSchedules("Admin",hash,new String[]{SCHEDULE});require(find(new String[]{SCHEDULE},null,start,end).length==0,"Schedule disable failed");
   boolean rejected=false;try{admin.findScheduledOperations("Admin",hash,null,null,end,start);}catch(org.apache.axis.AxisFault fault){rejected=true;}require(rejected,"Reversed dates accepted");
   rejected=false;try{admin.enableSchedules("Admin",hash,new String[]{"ffffffff-ffff-ffff-ffff-ffffffffffff"});}catch(org.apache.axis.AxisFault fault){rejected=true;}require(rejected,"Unknown schedule enabled");
   System.out.println("PASS: "+checks+" scheduler console checks; forecast, preview, filters, schedule/operation enable-disable and invalid requests.");
   LocalDateTime due=LocalDateTime.now().plusSeconds(45).withNano(0);deploy(due.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),true);admin.enableScheduleForOperation("Admin",hash,new String[]{OPERATION});admin.enableSchedules("Admin",hash,new String[]{SCHEDULE});
   Files.write(Paths.get("C:/ETL/Reports/scheduler-console-test-due.txt"),Arrays.asList(due.toString(),OPERATION),StandardCharsets.UTF_8);armed=true;System.out.println("ARMED: controlled scheduler operation due "+due);
  }finally{if(!armed){try{admin.disableSchedules("Admin",hash,new String[]{SCHEDULE});admin.disableScheduleForOperation("Admin",hash,new String[]{OPERATION});}catch(Exception ignored){}}}
 }
}
