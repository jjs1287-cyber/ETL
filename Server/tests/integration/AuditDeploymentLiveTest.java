package org.etlforge.integration.client.ui.admin.audit;

import java.net.URL;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.*;

public class AuditDeploymentLiveTest {
    static final ObjectMapper JSON=new ObjectMapper();static int checks;
    static String hash,project=UUID.randomUUID().toString(),script=UUID.randomUUID().toString(),operation=UUID.randomUUID().toString();
    static DeployInterchangeData deploy;
    static InterchangeDataTable table(String[] columns,String[]... values){InterchangeDataRow[] rows=new InterchangeDataRow[values.length];for(int i=0;i<rows.length;i++)rows[i]=new InterchangeDataRow(values[i]);return new InterchangeDataTable(columns,rows);}
    static void require(boolean ok,String message){checks++;if(!ok)throw new AssertionError(message);}
    static InterchangeDataHolder data(String content,String description) {
        InterchangeDataHolder data=new InterchangeDataHolder();
        data.setInterchangeProjectsTab(table(new String[]{"InterchangeProject_ID","InterchangeProject_Name"},new String[]{project,"Audit Capture Verification "+project.substring(0,8)}));
        data.setScriptsTab(table(new String[]{"Script_ID","Script_Name","Script_Text"},new String[]{script,"Audit verification script",Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8))}));
        data.setOperationsTab(table(new String[]{"Operation_ID","Operation_Name","Operation_Type_ID","Source_ID","Target_ID","Schedule_ID","Transformation_ID","Success_Operation_ID","Success_Message_ID","Failure_Operation_ID","Failure_Message_ID","Description"},new String[]{operation,"Audit verification operation","8",null,null,null,null,null,null,null,null,description}));
        data.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},new String[]{operation,"1",script}));return data;
    }
    static void deploy(InterchangeDataHolder data)throws Exception{CDeployResults result=deploy.deployInterchangeData("Admin",hash,new InterchangeIdHolder(),data);require(result!=null && result.getDeployStatus()<=1,"Deployment failed");}
    static JsonNode api(ObjectNode request)throws Exception{return AuditLogService.invokeRequest("http://localhost:47100/axis/konga_admin","Admin",hash,request);}
    static JsonNode entries(int type,String name)throws Exception{return api(JSON.createObjectNode().put("action","list").put("projectGuid",project).put("objectType",type).put("search",name).put("days",30).put("limit",100)).path("entries");}
    public static void main(String[] args)throws Exception {
        String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();StringBuilder hex=new StringBuilder();for(byte b:MessageDigest.getInstance("MD5").digest(password.getBytes(StandardCharsets.UTF_8)))hex.append(String.format("%02x",b&255));hash=hex.toString();
        deploy=new DeployInterchangeDataServiceLocator().getkonga_deploy_interchange_data(new URL("http://localhost:47100/axis/konga_deploy_interchange_data"));((org.apache.axis.client.Stub)deploy).setTimeout(60000);
        String before="<trans>\nWriteToOperationLog('audit before');\n</trans>",after="<trans>\nWriteToOperationLog('audit after');\n</trans>";
        deploy(data(before,"initial"));require(entries(0,"").size()==2,"Creation events missing");
        deploy(data(before,"initial"));require(entries(0,"").size()==2,"Unchanged redeploy generated an event");
        InterchangeDataHolder layoutFirst=data(before,"initial");
        layoutFirst.setOperationGraphLayoutTab(table(new String[]{"Operation_ID","Layout"},new String[]{operation,"layout-before"}));
        deploy(layoutFirst);require(entries(0,"").size()==2,"Adding a layout generated an event");
        InterchangeDataHolder layoutMoved=data(before,"initial");
        layoutMoved.setOperationGraphLayoutTab(table(new String[]{"Operation_ID","Layout"},new String[]{operation,"layout-after"}));
        deploy(layoutMoved);require(entries(0,"").size()==2,"Layout-only edit generated an event");
        deploy(data(after,"initial"));require(entries(0,"").size()==3,"Script edit event missing");require(entries(6,"").size()==1,"Script edit incorrectly changed operation audit");
        JsonNode scriptEntry=entries(23,"").get(0);require(scriptEntry.path("action").asText().equals("Updated"),"Script action incorrect");
        JsonNode detail=api(JSON.createObjectNode().put("action","detail").put("eventId",scriptEntry.path("id").asLong()));
        require(detail.path("previous").path("fields").path("script_text").asText().equals(before),"Previous script text incorrect");
        require(detail.path("current").path("fields").path("script_text").asText().equals(after),"Current script text incorrect");
        deploy(data(after,"changed"));require(entries(6,"Audit verification operation").size()==2,"Operation edit event missing");
        JsonNode operationDetail=api(JSON.createObjectNode().put("action","detail").put("eventId",entries(6,"").get(0).path("id").asLong()));
        require(!operationDetail.toString().contains("operationgraphlayouttab"),"Layout remains in new audit details");
        require(entries(6," \t").size()==2,"Blank name did not return project operations");
        InterchangeDataHolder invalid=data("<trans>bad rollback</trans>","changed");invalid.setOperationPipelines(table(new String[]{"Operation_ID","ActivityType_ID","Activity_ID"},new String[]{operation,"1",UUID.randomUUID().toString()}));
        boolean failed=false;try{deploy(invalid);}catch(java.rmi.RemoteException expected){failed=true;}require(failed,"Invalid deployment unexpectedly committed");
        require(entries(0,"").size()==4,"Failed deployment generated an event");
        deploy(data(after,"changed"));require(entries(0,"").size()==4,"Rollback left modified data or false audit baseline");
        deploy(data(before,"initial"));require(entries(0,"").size()==6,"Revert events missing");
        InterchangeDataHolder empty=new InterchangeDataHolder();empty.setInterchangeProjectsTab(data(before,"initial").getInterchangeProjectsTab());
        InterchangeIdHolder deleted=new InterchangeIdHolder();deleted.setOperationsTab_ids(new String[]{operation});deleted.setScriptsTab_ids(new String[]{script});
        CDeployResults result=deploy.deployInterchangeData("Admin",hash,deleted,empty);require(result!=null && result.getDeployStatus()<=1,"Delete deployment failed");
        require(entries(0,"").size()==8,"Deletion events missing");
        Files.write(Paths.get("C:/ETL/Reports/audit-capture-live-project.txt"),(project+"\nAudit Capture Verification "+project.substring(0,8)+"\n").getBytes(StandardCharsets.UTF_8));
        System.out.println("PASS "+checks+" real deployment/audit checks: create, unchanged, script change, operation change, blank filter, rollback, revert, deletion; isolated test project "+project);
    }
}
