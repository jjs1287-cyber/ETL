package org.etlforge.integration.server.engine.connector.netsuite.cf;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNames;
import org.etlforge.integration.design.util.XmlSaxHandler;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteCustomFieldMetaResponseHandler {
   public static List<NetSuiteCustomFieldMetaResponse> getNetSuiteCustomFieldMetaRecord(String var0) {
      try {
         return parse(var0);
      } catch (SAXException var2) {
         throw new ConnectorServerRuntimeException(var2);
      }
   }

   static List<NetSuiteCustomFieldMetaResponse> parse(String var0) throws SAXException {
      NetSuiteCustomFieldMetaResponseHandler.Handler var1 = new NetSuiteCustomFieldMetaResponseHandler.Handler();
      SaxParsers.parseString(var0, var1, true);
      return var1.customFieldRecords;
   }

   public static void main(String[] var0) {
      String var1 = "Z:/etlforge/connectors/etlforgeconnectorsample/NETSUITE/Sample/getCustomizationId/";
      String var2 = var1 + "getListRes.xml";
      String var3 = ConnectorExecutionSystem.readFileUTF8(var2);
   }

   private static class Handler extends XmlSaxHandler {
      private final List<NetSuiteCustomFieldMetaResponse> customFieldRecords = Lists.newArrayList();
      private static final int None_RecorFieldLevel = -10;
      private int recorFieldLevel = -10;
      private StringBuilder value;
      private String customLabel;
      private String internalId;
      private String fieldType;
      private String scriptId;
      private List<String> trueAppliesObjects;

      public Handler() {
         this.initCustomField();
      }

      private void initCustomField() {
         this.customLabel = null;
         this.internalId = null;
         this.fieldType = null;
         this.scriptId = null;
         this.trueAppliesObjects = Lists.newArrayList();
         this.value = null;
      }

      private boolean isRecordElement(String var1, String var2) {
         return ConnectorNetSuiteNames.isRecordElement(var2);
      }

      private boolean isRecordField() {
         return this.getLevel() == this.recorFieldLevel;
      }

      @Override
      public void startElementImpl(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if (this.value != null) {
            this.value = null;
         }

         if (this.isRecordElement(var1, var2)) {
            if (this.recorFieldLevel < 1) {
               this.recorFieldLevel = this.getLevel() + 1;
            }

            this.initCustomField();
            this.internalId = var4.getValue("internalId");
         } else if (this.isRecordField()) {
            this.value = new StringBuilder();
         }
      }

      @Override
      public void endElementImpl(String var1, String var2, String var3) throws SAXException {
         if (this.isRecordElement(var1, var2)) {
            this.recorFieldLevel = -10;
            NetSuiteCustomFieldMetaResponse var4 = new NetSuiteCustomFieldMetaResponse(
               this.customLabel, this.internalId, this.scriptId, this.fieldType, this.trueAppliesObjects
            );
            this.customFieldRecords.add(var4);
            this.initCustomField();
         } else if (this.isRecordField()) {
            if (var2.equals("label")) {
               this.customLabel = this.value.toString().trim();
            } else if (var2.equals("fieldType")) {
               this.fieldType = this.value.toString().trim();
            } else if (var2.equals("scriptId")) {
               this.scriptId = this.value.toString().trim();
            } else if (this.value != null) {
               String var5 = this.value.toString().trim();
               if (var5.equalsIgnoreCase("true") || var5.equals("1")) {
                  this.trueAppliesObjects.add(var2);
               }
            }
         }

         if (this.value != null) {
            this.value = null;
         }
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         super.characters(var1, var2, var3);
         if (this.value != null) {
            this.value.append(var1, var2, var3);
         }
      }
   }
}
