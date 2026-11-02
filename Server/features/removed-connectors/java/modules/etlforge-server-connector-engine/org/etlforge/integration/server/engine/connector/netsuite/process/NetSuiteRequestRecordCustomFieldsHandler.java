package org.etlforge.integration.server.engine.connector.netsuite.process;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.etlforge.integration.etlforgeconnector.XmlQName;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.xml.sax.Attributes;

public class NetSuiteRequestRecordCustomFieldsHandler {
   private static final String netsuitTypeElementName = "netsuitetype";
   private static final String customFieldElementName = "customField";
   private final List<String> nullfieldList;
   private final StringBuilder customfieldListBuilder = new StringBuilder();
   private final NetSuiteRequestRecordFieldProcessor fieldProcessor;
   private final int customFieldLevel;
   private String customFieldQName = null;
   private NetSuiteRequestRecordFieldNullValueHandler nullValueHandler = null;
   private boolean isCustomFieldNull;
   private final boolean checkNullCustomFields;
   private String netsuitType;
   private String netsuiteIdName;
   private String netsuiteId;

   public NetSuiteRequestRecordCustomFieldsHandler(int var1, boolean var2, List<String> var3) {
      this.nullfieldList = var3;
      this.checkNullCustomFields = var2;
      this.customFieldLevel = var1;
      this.netsuiteIdName = null;
      this.fieldProcessor = new NetSuiteRequestRecordFieldProcessor(var1, new NetSuiteRecordRefWrapper());
      this.initCustomField();
   }

   public String getCustomFieldsContent() {
      return this.customfieldListBuilder.toString();
   }

   private void initCustomField() {
      this.netsuitType = null;
      this.netsuiteId = null;
      this.nullValueHandler = null;
      this.fieldProcessor.init();
      this.isCustomFieldNull = this.checkNullCustomFields;
   }

   private boolean isCustomFieldLevel(int var1) {
      return var1 == this.customFieldLevel;
   }

   private boolean isCustomFieldValueLevel(int var1) {
      return var1 == this.customFieldLevel + 1;
   }

   private void processStartCustomField(String var1, String var2, String var3, Attributes var4) {
      this.initCustomField();
      this.netsuitType = var4.getValue("netsuitetype");
      boolean var5 = !Strings.isNullOrEmpty(this.netsuitType);
      if (var5 && this.netsuiteIdName == null) {
         String var6 = var4.getValue("scriptId");
         if (Strings.isNullOrEmpty(var6)) {
            this.netsuiteIdName = "internalId";
         } else {
            this.netsuiteIdName = "scriptId";
         }
      }

      if (var5) {
         this.netsuiteId = var4.getValue(this.netsuiteIdName);
         XmlQName var10 = new XmlQName(var3);
         if (Strings.isNullOrEmpty(this.netsuiteId)) {
            String var11 = "Invalid request data: no id defined for custom field " + var10.getLocalName() + "[" + this.netsuiteIdName + "]";
            throw new ConnectorServerRuntimeException(var11);
         }

         String var7 = var10.getPrefix();
         this.customFieldQName = XmlQName.getQName(var7, "customField");
         TreeMap var8 = Maps.newTreeMap();
         String var9 = XmlQName.getQName(var7, this.netsuitType);
         var8.put(this.netsuiteIdName, this.netsuiteId);
         var8.put("xsi:type", var9);
         this.fieldProcessor.startElementTagAttributeMap(this.customFieldQName, var8);
      } else {
         this.customFieldQName = var3;
         this.fieldProcessor.startElementTag(var3, var4);
      }
   }

   private boolean isValueNull() {
      return this.nullValueHandler != null && this.nullValueHandler.isNullField();
   }

   public void startElement(int var1, String var2, String var3, String var4, Attributes var5, Map<String, String> var6) {
      if (this.isCustomFieldLevel(var1)) {
         this.processStartCustomField(var2, var3, var4, var5);
      } else {
         if (this.checkNullCustomFields) {
            if (this.isCustomFieldValueLevel(var1)) {
               this.nullValueHandler = null;
               if (this.isCustomFieldNull) {
                  this.nullValueHandler = new NetSuiteRequestRecordFieldNullValueHandler(var1, true);
               }
            }

            if (this.nullValueHandler != null) {
               this.nullValueHandler.startElement(var1, var2, var3, var4, var5);
            }
         }

         this.fieldProcessor.startElementImpl(var1, var2, var3, var4, var5, var6);
      }
   }

   public void endElement(int var1, String var2, String var3, String var4) {
      if (this.isCustomFieldLevel(var1)) {
         this.fieldProcessor.endElementTag(this.customFieldQName);
         if (this.checkNullCustomFields && this.isCustomFieldNull) {
            this.nullfieldList.add(this.netsuiteId);
         } else {
            this.customfieldListBuilder.append(this.fieldProcessor.getFieldXml());
         }

         this.initCustomField();
      } else {
         if (this.nullValueHandler != null) {
            this.nullValueHandler.endElement(this.fieldProcessor.isShortForm(), var1, var2, var3, var4);
            if (this.isCustomFieldNull) {
               if (!this.isValueNull()) {
                  this.isCustomFieldNull = false;
                  this.nullValueHandler = null;
               } else if (this.isCustomFieldValueLevel(var1)) {
                  this.nullValueHandler = null;
               }
            }
         }

         this.fieldProcessor.endElementTag(var4);
      }
   }

   public void characters(char[] var1, int var2, int var3) {
      this.fieldProcessor.characters(var1, var2, var3);
   }
}
