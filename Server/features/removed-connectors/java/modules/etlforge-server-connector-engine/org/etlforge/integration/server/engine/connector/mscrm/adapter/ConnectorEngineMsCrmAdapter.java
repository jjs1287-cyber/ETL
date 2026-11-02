package org.etlforge.integration.server.engine.connector.mscrm.adapter;

import com.google.common.base.Strings;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.etlforgeconnector.SchemaData;
import org.etlforge.integration.mscrm.FunctionType;
import org.etlforge.integration.mscrm.metadata.BasicEntityDetails;
import org.etlforge.integration.mscrm.metadata.MSCrmMetaDataException;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.metadata.MSCrmSchemaBuilder;
import org.etlforge.integration.mscrm.query.FetchXml;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.framework.ConnectorObjectList;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;
import org.etlforge.integration.server.engine.connector.mscrm.ConnectorEndpointMsCrm;
import org.etlforge.integration.server.engine.connector.util.XmlUtils;
import org.etlforge.integration.server.engine.connector.util.XmlWriter;
import org.etlforge.io.FileUtils;

public class ConnectorEngineMsCrmAdapter {
   final ConnectorEndpointMsCrmAdapter endpointAdapter;

   public ConnectorEngineMsCrmAdapter(ConnectorEndpointMsCrm var1) {
      this.endpointAdapter = var1.getConnectorEndpointMsCrmAdapter();
   }

   private void getResponseMetaDataFile(String var1, String var2, String var3) {
      String var4 = this.endpointAdapter.getWsBaseUrl();
      String var5 = this.endpointAdapter.getAuthInfo();
      MsCrmServiceHandler var6 = new MsCrmServiceHandler(var1, var4, var5);
      var6.callWsFile(var2, var3);
   }

   private String getResponseMetaData(String var1, String var2) {
      String var3 = this.endpointAdapter.getWsBaseUrl();
      String var4 = this.endpointAdapter.getAuthInfo();
      MsCrmServiceHandler var5 = new MsCrmServiceHandler(var1, var3, var4);
      return var5.callWs(var2);
   }

   public ConnectorObjectList getConnectorObjectList() {
      ArrayList var1 = this.describeAllMSCrmObjects();
      ConnectorObjectList var2 = new ConnectorObjectList(false, "CRM Objects");

      for (Object __etlItem47 : var1) {
         BasicEntityDetails var4 = (BasicEntityDetails) __etlItem47;
         String var5 = var4.getName();
         String var6 = var4.getDescription();
         var2.addConnectorObject(var5, var6);
      }

      return var2;
   }

   public ArrayList<BasicEntityDetails> describeAllMSCrmObjects() {
      String var1 = MsCrmRequestBodyBuilder.BasicEntityDetailsOperation();
      String var2 = MsCrmRequestBodyBuilder.BasicEntityDetailsBody();
      String var3 = this.getResponseMetaData(var1, var2);
      return this.parseBasicEntityDetailsResponse(var3);
   }

   private ArrayList<BasicEntityDetails> parseBasicEntityDetailsResponse(String var1) {
      try {
         return MetaDataResponseParsers.parseBasicEntityDetailsResponse(var1);
      } catch (MSCrmMetaDataException var3) {
         throw new ConnectorServerRuntimeException(var3);
      }
   }

   public List<FetchXml> getSavedQueries(String var1) throws MSCrmMetaDataException {
      String var2 = MsCrmRequestBodyBuilder.savedQueryOperation();
      String var3 = MsCrmRequestBodyBuilder.savedQueryBody(var1);
      String var4 = this.getResponseMetaData(var2, var3);
      return SavedQueryResponseParser.parse(var4);
   }

   public ConnectorXsdSchemaFiles getConnectorXsdSchemas(FunctionType var1, String var2) {
      try {
         MSCrmObject var3 = this.getDetailedMetaData(var2);
         MSCrmSchemaBuilder var4 = new MSCrmSchemaBuilder(var1, var3);
         SchemaData var5 = var4.buildRequestSchema();
         SchemaData var6 = var4.buildResponseSchema();
         return new ConnectorXsdSchemaFiles(var5.getContents().inflate(), var5.getRootName(), var6.getContents().inflate(), var6.getRootName());
      } catch (Exception var8) {
         throw new ConnectorServerRuntimeException(var8);
      }
   }

   public MSCrmObject getDetailedMetaData(String var1) throws MSCrmMetaDataException {
      String var2 = MsCrmRequestBodyBuilder.SpecificMetadataOperation();
      String var3 = MsCrmRequestBodyBuilder.SpecificMetadataBody(var1);
      String var4 = this.getResponseMetaData(var2, var3);
      ArrayList var5 = MetaDataResponseParsers.parseSpecificMetaDataResponse(var4);
      switch (var5.size()) {
         case 0:
            throw new MSCrmMetaDataException("Unknown object: " + var1);
         case 1:
            return (MSCrmObject)var5.get(0);
         default:
            String var6 = " Invalid Response: \n" + var4;
            throw new MSCrmMetaDataException(var6);
      }
   }

   private String createQueryRequestBodyFileUtf8(FunctionType var1, String var2, String var3) {
      String var4 = var1.getName().toLowerCase();
      String var5 = FileUtils.getPostfixFilenameXml(var3, "_query");
      XmlWriter var6 = new XmlWriter(var5);

      try {
         var6.append(XmlUtils.xmlUtf8Header);
         var6.appendline();
         var6.appendStartELement(var4);
         var6.appendStartELement(var2);
         var6.appendline();
         boolean var7 = false;
         var6.appendFileUtf8(var3);
         var6.appendCloseELement(var2);
         var6.appendCloseELement(var4);
      } catch (IOException var11) {
         throw new ConnectorServerRuntimeException(var11);
      } finally {
         var6.dispose();
      }

      return var5;
   }

   public void executeConnectorFileUtf8(FunctionType var1, String var2, String var3, String var4) {
      boolean var5 = var1 == FunctionType.RETRIEVE;
      if (var5) {
         String var6 = this.createQueryRequestBodyFileUtf8(var1, var2, var3);
         this.executeConnectorFileUtf8Impl(var1, var6, var4);
      } else {
         this.executeConnectorFileUtf8Impl(var1, var3, var4);
      }
   }

   private void executeConnectorFileUtf8Impl(FunctionType var1, String var2, String var3) {
      if (var1 == FunctionType.UPSERT) {
         String var4 = MsCrmUpsertRequestValidator.validateXmlFile(var2);
         boolean var5 = !Strings.isNullOrEmpty(var4);
         if (var5) {
            throw new ConnectorServerRuntimeException(var4);
         }
      }

      this.getResponseMetaDataFile(var1.name(), var2, var3);
   }
}
