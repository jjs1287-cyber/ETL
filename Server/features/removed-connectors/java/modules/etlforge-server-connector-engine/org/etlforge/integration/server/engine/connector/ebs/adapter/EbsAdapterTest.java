package org.etlforge.integration.server.engine.connector.ebs.adapter;

import com.ubs.core.adapter.ebs.common.schema.EbsRecord;
import com.ubs.core.adapter.ebs.connection.EbsDesignConnection;
import com.ubs.core.adapter.ebs.connection.EbsExecutionConnection;
import com.ubs.core.adapter.ebs.interaction.EbsDesignFunction;
import com.ubs.core.adapter.ebs.interaction.EbsDesignFunctionCatalog;
import com.ubs.core.adapter.ebs.interaction.EbsExecutionInteraction;
import com.ubs.core.adapter.ebs.interaction.EbsExecutionMonitor;
import com.ubs.core.adapter.ebs.interaction.EbsExecutionStatusRecord;
import com.ubs.core.adapter.ebs.schema.EbsSchemaManager;
import com.ubs.core.adapter.exception.AdapterExecutionException;
import com.ubs.core.adapter.exception.AdapterSchemaException;
import com.ubs.core.adapter.util.FileUtility;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class EbsAdapterTest {
   public Properties getConnectionProperties() {
      Properties var1 = new Properties();
      var1.put("username", "apps");
      var1.put("password", "apps");
      var1.put("drivername", "oracle.jdbc.driver.OracleDriver");
      var1.put("connecturl", "jdbc:oracle:thin:@ec2-23-20-205-167.compute-1.amazonaws.com:1521:vision");
      return var1;
   }

   public void displayCatalog(EbsDesignFunctionCatalog var1) {
      System.out.println("Catalog Name " + var1.getName());
      this.displayFunctions(var1.listFunctions());

      for (EbsDesignFunctionCatalog var4 : var1.listFunctionCatalogs()) {
         this.displayCatalog(var4);
      }
   }

   public void displayFunctions(ArrayList<EbsDesignFunction> var1) {
      for (EbsDesignFunction var3 : var1) {
         System.out.println("                >> " + var3.getName() + "    " + var3.getDescription());
      }
   }

   public void generateRequestSchema(EbsDesignConnection var1, String var2) throws AdapterSchemaException, IOException {
      Properties var3 = new Properties();
      var3.put("programname", var2);
      EbsSchemaManager var4 = new EbsSchemaManager(var1, var3);
      System.out.println("Request Schema is \n" + var4.createRequestSchema().getRecord().toString());
      FileUtility var5 = new FileUtility();
      var5.writeToFile(new File("c:\\projects\\ebs\\concschemas\\" + var2 + ".xsd"), var4.createRequestSchema().getRecord().toString());
   }

   public void generateResponseSchema(EbsDesignConnection var1, String var2) throws AdapterSchemaException {
      Properties var3 = new Properties();
      var3.put("programname", var2);
      EbsSchemaManager var4 = new EbsSchemaManager(var1, var3);
      System.out.println("Response Schema is \n" + var4.createResponseSchema().getRecord().toString());
   }

   public void execute(EbsExecutionConnection var1, String var2) throws AdapterExecutionException, Exception {
      Properties var3 = new Properties();
      String var4 = this.getRequestSchema();
      System.out.println("Runtime Schema is " + var4);
      var3.put("programname", var2);
      var3.put("schema", var4);
      EbsExecutionInteraction var5 = new EbsExecutionInteraction(var1);
      EbsRecord var6 = new EbsRecord();
      var6.setRecord(this.getRequestXML());
      System.out.println("Response is " + var5.execute(var6, var3).getRecord().toString());
   }

   public void showStatus(EbsExecutionConnection var1, int var2) throws AdapterExecutionException {
      EbsExecutionMonitor var3 = new EbsExecutionMonitor(var1);
      EbsExecutionStatusRecord var4 = var3.getStatus(var2);
      System.out.println("Status " + var4.getActualStatus());
      System.out.println("Phase " + var4.getActualPhase());
   }

   public String getRequestSchema() throws Exception {
      FileUtility var1 = new FileUtility();
      StringBuffer var2 = new StringBuffer(var1.readFile(new File("c:\\projects\\ebs\\concschemas\\BMCOIN.xsd")));
      return var2.toString();
   }

   public String getRequestXML() {
      return "<?xml version=\"1.0\" encoding=\"utf-8\"?><ns:INPUT xmlns:ns=\"urn:ebs-com:document:ebs:concurrent:functions\"><BOMCOIN><HEADER><USER_NAME>SERVICES</USER_NAME><RESPONSIBILITY>BILLS_OF_MATERIAL</RESPONSIBILITY><RESP_APPLICATION>BOM</RESP_APPLICATION><SECURITY_GROUP>STANDARD</SECURITY_GROUP><SERVER_NAME></SERVER_NAME></HEADER><PROGRAM_INPUTS><APPLICATION>BOM</APPLICATION><PROGRAM>BMCOIN</PROGRAM><DESCRIPTION>BILL OF MATERIAL</DESCRIPTION><START_TIME></START_TIME><SUB_REQUEST></SUB_REQUEST><ORGANIZATION_ID>204</ORGANIZATION_ID><ALL_ORG_FLAG>1</ALL_ORG_FLAG><IMPORT_ROUTINGS>1</IMPORT_ROUTINGS><IMPORT_BILLS_OF_MATERIAL>1</IMPORT_BILLS_OF_MATERIAL><DELETE_PROCESSED_ROWS>2</DELETE_PROCESSED_ROWS><BATCH_ID></BATCH_ID></PROGRAM_INPUTS></BOMCOIN></ns:INPUT>";
   }

   public static void main(String[] var0) throws Exception {
      EbsAdapterTest var1 = new EbsAdapterTest();
      EbsDesignConnection var2 = new EbsDesignConnection(var1.getConnectionProperties());
      var2.openConnection();
   }
}
