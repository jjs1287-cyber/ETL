package org.etlforge.integration.server.engine.connector.netsuite.customobject;

import java.util.List;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteCustomField;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookup;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.integration.server.engine.connector.netsuite.operation.NetSuiteCustomizationRef;
import org.etlforge.integration.server.engine.connector.netsuite.operation.NetSuiteGetList;
import org.etlforge.integration.server.engine.connector.netsuite.ws.NetSuiteResponseStatusHandler;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteCustomFieldMetadataCustomObjectId implements NetSuiteCustomFieldsLookup {
   private final NetSuitePassport passport;
   private final NetSuiteCustomizationRef customObject;
   private static final String statusRelativePathRoot = "Envelope/Body/getListResponse/readResponseList/readResponse/status";

   public NetSuiteCustomFieldMetadataCustomObjectId(NetSuitePassport var1, String var2) {
      this(var1, new NetSuiteCustomizationRef(var2, "customRecordType"));
   }

   public NetSuiteCustomFieldMetadataCustomObjectId(NetSuitePassport var1, NetSuiteCustomizationRef var2) {
      this.passport = var1;
      this.customObject = var2;
   }

   @Override
   public NetSuiteObjectCustomFields getNetSuiteObjectsCustomFields() {
      String var1 = this.getNetSuiteCustomFieldsResponse();
      return this.parse(var1);
   }

   private String getNetSuiteCustomFieldsResponse() {
      NetSuiteGetList var1 = new NetSuiteGetList(this.passport);
      String var2 = var1.getList(this.customObject);
      NetSuiteResponseStatusHandler.validateResponse("Envelope/Body/getListResponse/readResponseList/readResponse/status", var2);
      return var2;
   }

   private NetSuiteObjectCustomFields parse(String var1) {
      return parse(var1, this.customObject.getInternalId());
   }

   static NetSuiteObjectCustomFields parse(String var0, String var1) {
      KongaSaxParser var2 = new KongaSaxParser();
      NetSuiteCustomFieldMetadataCustomObjectId.Handler var3 = new NetSuiteCustomFieldMetadataCustomObjectId.Handler(var1);
      var2.registerHandler(var3);

      try {
         var2.parseString(var0);
      } catch (SAXException var5) {
         throw new ConnectorWebServiceRuntimeException(var5);
      }

      return var3.customFields;
   }

   public static void main(String[] var0) {
      String var1 = "Z:/etlforge/connectors/etlforgeconnectorsample/NETSUITE/Sample/getCustomizationId/";
      String var2 = var1 + "getListRecordTypeRes.xml";
      String var3 = ConnectorExecutionSystem.readFileUTF8(var2);
      NetSuiteResponseStatusHandler.validateResponse("Envelope/Body/getListResponse/readResponseList/readResponse/status", var3);
      NetSuiteObjectCustomFields var4 = parse(var3, "fake id");
      List var5 = var4.getNetSuiteCustomFieldsObject("fake id");
      System.out.println("size=" + var5.size());

      for (Object __etlItem74 : var5) {
         NetSuiteCustomField var7 = (NetSuiteCustomField) __etlItem74;
         System.out.println(var7.getInternalId());
         System.out.println(var7.getFieldType());
         System.out.println(var7.getCustomLabel());
         System.out.println();
      }
   }

   @RelativePathRoot("Envelope/Body/getListResponse/readResponseList/readResponse/record")
   private static class Handler {
      public NetSuiteObjectCustomFields customFields;
      private String customObjectInternalId;
      private String customLabel;
      private String internalId;
      private String scriptId;
      private String fieldType;

      Handler(String var1) {
         this.customFields = NetSuiteObjectCustomFields.newCustomObjectCustomFields(var1);
      }

      @StartElementHandler("/")
      public void startRecord(Attributes var1) {
         this.customObjectInternalId = var1.getValue("internalId");
      }

      @StartElementHandler("customFieldList/customField")
      public void startCustomField(Attributes var1) {
         this.internalId = var1.getValue("internalId");
         this.customLabel = null;
         this.fieldType = null;
         this.scriptId = null;
      }

      @EndElementHandler("customFieldList/customField/label")
      public void label(String var1) {
         this.customLabel = var1;
      }

      @EndElementHandler("customFieldList/customField/fieldType")
      public void fieldType(String var1) {
         this.fieldType = var1;
      }

      @EndElementHandler("customFieldList/customField/scriptId")
      public void scriptId(String var1) {
         this.scriptId = var1;
      }

      @EndElementHandler("customFieldList/customField")
      public void customField(String var1) {
         NetSuiteCustomField var2 = new NetSuiteCustomField(this.customLabel, this.internalId, this.scriptId, this.fieldType);
         this.customFields.add(this.customObjectInternalId, var2);
      }
   }
}
