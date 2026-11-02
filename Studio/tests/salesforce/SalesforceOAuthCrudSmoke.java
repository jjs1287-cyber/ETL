package org.etlforge.integration.salesforce;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.sforce.ws.ConnectorConfig;
import com.sforce.soap.partner.*;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.async.*;
public final class SalesforceOAuthCrudSmoke {
 static void check(boolean value,String message){if(!value)throw new AssertionError(message);}
 public static void main(String[] args)throws Exception{
  Map<String,String> values=new HashMap<String,String>();for(String line:Files.readAllLines(Paths.get(args[0]),StandardCharsets.UTF_8)){int pos=line.indexOf(':');if(pos>0)values.put(line.substring(0,pos).trim().toLowerCase(Locale.ROOT),line.substring(pos+1).trim());}
  LoginCredentials credentials=new LoginCredentials("","","",false,"oauth",values.get("instance url"),values.get("client id"),values.get("client secret"));
  SalesforceOAuthClient.Token token=SalesforceOAuthClient.login(credentials,new ConnectorConfig());
  ConnectorConfig config=new ConnectorConfig();config.setManualLogin(true);config.setSessionId(token.accessToken);config.setServiceEndpoint(token.instanceUrl+"/services/Soap/u/61.0");config.setConnectionTimeout(15000);config.setReadTimeout(30000);
  PartnerConnection partner=new PartnerConnection(config);partner.setSessionHeader(token.accessToken);
  if(args.length>1 && "diagnose".equals(args[1])) {
   for(Field field:partner.describeSObject("Account").getFields())if(field.getName().toLowerCase(Locale.ROOT).matches(".*(logical|config).*"))System.out.println("Account field: "+field.getName());
   for(DescribeGlobalSObjectResult object:partner.describeGlobal().getSobjects())if(object.getName().toLowerCase(Locale.ROOT).contains("configuration"))System.out.println("Configuration object: "+object.getName());
   return;
  }
  String id=null,jobId=null; boolean owned=false;
  String fixture="ETLForge OAuth Verification "+UUID.randomUUID();
  try{
   if(args.length<2 || !"bulk-only".equals(args[1])) {
   DescribeSObjectResult metadata=partner.describeSObject("Account");check(metadata.isCreateable()&&metadata.isUpdateable()&&metadata.isDeletable(),"Run As user lacks Account test CRUD permissions");
   if(args.length>1 && "existing-update".equals(args[1])) {
    String existingQuery="SELECT Id, Name FROM Account WHERE DUET__configuration__c != null AND DUET__extlogicalid__c != null LIMIT 1"; if(args.length>2){check(args[2].matches("001[A-Za-z0-9]{15}"),"Invalid Account test ID");existingQuery="SELECT Id, Name FROM Account WHERE Id=\u0027"+args[2]+"\u0027";} com.sforce.soap.partner.QueryResult existing=partner.query(existingQuery);
    check(existing.getRecords().length==1,"No existing Account available");id=existing.getRecords()[0].getId();fixture=(String)existing.getRecords()[0].getField("Name");
   } else {
    SObject account=new SObject();account.setType("Account");account.setField("Name",fixture);
    SaveResult created=partner.create(new SObject[]{account})[0];check(created.isSuccess(),"Isolated Account create rejected: "+(created.getErrors().length>0?created.getErrors()[0].getStatusCode():"unknown"));id=created.getId();owned=true;
   }
   SObject changed=new SObject();changed.setType("Account");changed.setId(id);changed.setField("Name",owned?fixture+" updated":fixture);SaveResult updated=partner.update(new SObject[]{changed})[0];check(updated.isSuccess(),"Account update failed: "+(updated.getErrors().length>0?updated.getErrors()[0].getStatusCode()+" / "+updated.getErrors()[0].getMessage():"unknown"));
   changed.setField("Name",owned?fixture+" upserted":fixture);UpsertResult upserted=partner.upsert("Id",new SObject[]{changed})[0];check(upserted.isSuccess()&&!upserted.isCreated(),"Account upsert failed");
   check(partner.query("SELECT Id FROM Account WHERE Id='"+id+"'").getSize()==1,"Isolated record query failed");
   System.out.println(owned?"PASS: isolated synthetic Account create, update, upsert and query":"PASS: existing Account queried, updated and upserted with its unchanged Name; no new record created");
   }
   ConnectorConfig bulkConfig=new ConnectorConfig();bulkConfig.setSessionId(token.accessToken);bulkConfig.setRestEndpoint(token.instanceUrl+"/services/async/61.0");bulkConfig.setConnectionTimeout(15000);bulkConfig.setReadTimeout(30000);
   BulkConnection bulk=new BulkConnection(bulkConfig);JobInfo job=new JobInfo();job.setObject("Account");job.setOperation(OperationEnum.query);job.setContentType(ContentType.CSV);job=bulk.createJob(job);jobId=job.getId();
   String query=id==null ? "SELECT Id FROM Account WHERE Id = null" : "SELECT Id FROM Account WHERE Id='"+id+"'";
   BatchInfo batch=bulk.createBatchFromStream(job,new ByteArrayInputStream(query.getBytes(StandardCharsets.UTF_8)));bulk.closeJob(jobId);
   long deadline=System.nanoTime()+60000000000L;
   do{batch=bulk.getBatchInfo(jobId,batch.getId());if(batch.getState()==BatchStateEnum.Completed)break;check(batch.getState()!=BatchStateEnum.Failed,"Isolated Bulk query failed");Thread.sleep(500);}while(System.nanoTime()<deadline);
   check(batch.getState()==BatchStateEnum.Completed,"Bulk query did not finish within 60 seconds");
   QueryResultList results=bulk.getQueryResultList(jobId,batch.getId());check(results.getResult().length>0,"Bulk query returned no result locator");
   StringBuilder csv=new StringBuilder();try(InputStream stream=bulk.getQueryResultStream(jobId,batch.getId(),results.getResult()[0])){byte[] bytes=new byte[2048];int count;while((count=stream.read(bytes))!=-1){csv.append(new String(bytes,0,count,StandardCharsets.UTF_8));check(csv.length()<10000,"Unexpected Bulk result size");}}
   String content=csv.toString().trim();
   check(id==null ? (content.isEmpty() || content.equals("Id") || content.equals("\"Id\"") || content.equals("Records not found for this query")) : content.contains(id),"Bulk result mismatch (length="+content.length()+", processed="+batch.getNumberRecordsProcessed()+")");
   System.out.println("PASS: OAuth token reused for Bulk API query, job completion and result retrieval");
  }finally{
   if(owned&&id!=null){DeleteResult deleted=partner.delete(new String[]{id})[0];if(!deleted.isSuccess()){Path recovery=Paths.get("C:/ETL/Server/private/salesforce-oauth-test-cleanup.txt");Files.write(recovery,("Account fixture needs cleanup: "+id).getBytes(StandardCharsets.UTF_8));throw new AssertionError("Isolated fixture cleanup failed; private recovery note saved");}System.out.println("PASS: isolated test Account deleted");}
  }
 }
}
