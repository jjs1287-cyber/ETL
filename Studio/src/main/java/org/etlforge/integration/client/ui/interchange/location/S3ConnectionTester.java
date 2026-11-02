package org.etlforge.integration.client.ui.interchange.location;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import org.etlforge.application.Application;
import org.etlforge.integration.client.ui.admin.audit.AuditLogService;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.client.datalocation.connection.serverapi.*;
import org.etlforge.integration.data.entity.*;
import org.etlforge.integration.data.location.S3Location;
import org.etlforge.integration.data.test.TestConnectionResult;

public final class S3ConnectionTester extends LocationConnectionTester<S3Location> {
 public S3ConnectionTester(S3Location location){super(location);}
 private ResultProducingServerCallback<TestConnectionResult> test(IntegrationEntity entity){
  TestFileLocationCallback callback=new TestFileLocationCallback(entity);
  try{ObjectNode r=new ObjectMapper().createObjectNode();r.put("action","s3.test");r.put("direction",entity instanceof Target?"target":"source");r.put("bucket",location.getBucket());r.put("region",location.getRegion());r.put("prefix",location.getPrefix());r.put("authentication",location.getAuthentication());
   r.put("accessKeyProtected",location.getProperty("s3_access_key_protected",""));r.put("secretKeyProtected",location.getProperty("s3_secret_key_protected",""));r.put("sessionTokenProtected",location.getProperty("s3_session_token_protected",""));
   JsonNode result=new AuditLogService(Application.getBean("ws.config.Cpp",CppWebServiceConfiguration.class)).request(r);ArrayList<String> files=new ArrayList<>();for(JsonNode item:result.path("files"))files.add(item.asText());callback.connected(entity instanceof Source?files:null);
  }catch(Exception error){callback.caught(error);}return callback;
 }
 @Override protected ResultProducingServerCallback<TestConnectionResult> makeServerCall(Source entity,TestSourceConnectionApi api){return test(entity);}
 @Override protected ResultProducingServerCallback<TestConnectionResult> makeServerCall(Target entity,TestTargetConnectionApi api){return test(entity);}
}
