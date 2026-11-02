import java.net.URL;
import java.nio.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;
import org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client.*;

public final class StudioUtf8QueuedXsltSmoke {
 static InterchangeDataTable table(String[] names,String[]...values){InterchangeDataRow[] rows=new InterchangeDataRow[values.length];for(int i=0;i<rows.length;i++)rows[i]=new InterchangeDataRow(values[i]);return new InterchangeDataTable(names,rows);}
 public static void main(String[] args)throws Exception {
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();StringBuilder digest=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))digest.append(String.format("%02x",b&255));String hash=digest.toString();password=null;
  String project=UUID.randomUUID().toString(),asset=UUID.randomUUID().toString();Path root=Paths.get("C:/ETL/Server/instances/dev/DataInterchange/TemporaryStorage/integration-smoke/utf8-queued-"+project);Files.createDirectories(root.resolve("output"));
  String unicode="caf\u00e9 \u20ac \u4e2d\u6587 \ud83d\ude00";String[] expected={unicode,unicode,"caf\u00e9 \u20ac"};String[] encodings={"UTF-16","UTF-32LE","windows-1252"};
  byte[][] originals=new byte[3][];String[] operations=new String[3],sources=new String[3],targets=new String[3];String[][] opRows=new String[3][],sourceRows=new String[3][],targetRows=new String[3][],pipeline=new String[9][];
  for(int i=0;i<3;i++) {
   operations[i]=UUID.randomUUID().toString();sources[i]=UUID.randomUUID().toString();targets[i]=UUID.randomUUID().toString();
   String xml="<?xml version='1.0' encoding='"+encodings[i]+"'?><r>"+expected[i]+"</r>";originals[i]=xml.getBytes(Charset.forName(encodings[i]));Files.write(root.resolve("input"+i+".xml"),originals[i]);
   sourceRows[i]=new String[]{sources[i],"UTF8 queued input "+i,"16",root.toString(),"input"+i+".xml",null,null,null,null,null};
   targetRows[i]=new String[]{targets[i],"UTF8 queued target "+i,"16",root.resolve("output").toString(),"result"+i+".xml",null,null,null,null,null,null};
   opRows[i]=new String[]{operations[i],"UTF8 queued "+encodings[i],"23",sources[i],targets[i],"1",null,null,null,null,null,null};
   pipeline[i*3]=new String[]{operations[i],"3",sources[i]};pipeline[i*3+1]=new String[]{operations[i],"8",asset};pipeline[i*3+2]=new String[]{operations[i],"4",targets[i]};
  }
  InterchangeDataTable projectTable=table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{project,"UTF8 queued verification "+project.substring(0,8)});InterchangeDataHolder data=new InterchangeDataHolder();data.setInterchangeProjectsTab(projectTable);
  String style="<xsl:stylesheet version='3.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'><xsl:output encoding='UTF-16'/><xsl:template match='/'><result><xsl:value-of select='/r'/></result></xsl:template></xsl:stylesheet>";
  data.setEntityProperties(table(new String[]{"EntityType_ID","Entity_ID","KeyName","KeyValue"},new String[]{"34",asset,"xslttransform_name","UTF8 queued transform"},new String[]{"34",asset,"xslt",style}));
  data.setSourcesTab(table(new String[]{"Source_ID","Source_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString"},sourceRows));
  data.setTargetsTab(table(new String[]{"Target_ID","Target_Name","Source_Type_ID","Locator","File_Name","Login","Password","Driver","DriverType","ConnectString","Target_Target_ID"},targetRows));
  data.setOperationsTab(table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Ignore_History","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID"},opRows));data.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},pipeline));
  DeployInterchangeData deploy=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://127.0.0.1:47100/axis/konga_deploy_interchange_data"));CDeployResults result=deploy.deployInterchangeData("Admin",hash,new InterchangeIdHolder(),data);if(result==null||result.getDeployStatus()>1)throw new AssertionError("Queued UTF8 fixture deploy failed");
  JobControl control=new JobControlServiceLocator().getkonga_jobcontrol(new URL("http://127.0.0.1:47100/axis/konga_jobcontrol"));List<String> manifest=new ArrayList<>();manifest.add(project);manifest.add(root.toString());for(String operation:operations){WsRunOperationResult run=control.runOperationInstanceMD5Password("Admin",hash,operation);if(run==null||run.getOperationInstanceId()<1)throw new AssertionError("Queue submission failed");manifest.add("instance="+run.getOperationInstanceId());}
  Files.write(Paths.get("C:/ETL/Reports/utf8-queued-test-manifest.txt"),manifest,StandardCharsets.UTF_8);
  long deadline=System.nanoTime()+45000000000L;boolean ready=false;while(System.nanoTime()<deadline){ready=true;for(int i=0;i<3;i++)ready&=Files.exists(root.resolve("output/result"+i+".xml"));if(ready)break;Thread.sleep(200);}
  if(!ready)throw new AssertionError("Queued UTF8 output missing; see utf8-queued-test-manifest.txt");
  try {
   for(int i=0;i<3;i++){String actual=StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT).decode(ByteBuffer.wrap(Files.readAllBytes(root.resolve("output/result"+i+".xml")))).toString();if(!actual.contains("encoding=\"UTF-8\"")||!actual.contains("<result>"+expected[i]+"</result>"))throw new AssertionError("Queued UTF8 output differs: "+encodings[i]);if(!Arrays.equals(Files.readAllBytes(root.resolve("input"+i+".xml")),originals[i]))throw new AssertionError("Original queued source modified");}
   System.out.println("PASS: three queued Source -> XSLT -> Target operations preserve UTF16, UTF32 and Windows1252 content and write strict UTF8.");
  } finally {
   InterchangeDataHolder empty=new InterchangeDataHolder();empty.setInterchangeProjectsTab(projectTable);empty.setEntityProperties(table(new String[]{"EntityType_ID","Entity_ID","KeyName","KeyValue"},new String[]{"34",asset,"__deleted","1"}));InterchangeIdHolder deleted=new InterchangeIdHolder();deleted.setOperationsTab_ids(operations);deleted.setSourcesTab_ids(sources);deleted.setTargetsTab_ids(targets);deploy.deployInterchangeData("Admin",hash,deleted,empty);
  }
 }
}
