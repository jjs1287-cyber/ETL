package org.etlforge.integration.server.engine.connector.netsuite.process;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.xml.sax.Attributes;

public class NetSuiteRequestRecordFieldsHandler {
   private static final String customFieldListElementName = "customFieldList";
   private boolean isInsideCustomFieldList;
   private NetSuiteRequestRecordCustomFieldsHandler customFieldHandler = null;
   private NetSuiteRequestRecordFieldNullValueHandler nullValueHandler = null;
   private final List<String> nullfieldList = Lists.newArrayList();
   private final StringBuilder recordContentBuilder = new StringBuilder();
   private final NetSuiteRequestRecordFieldProcessor fieldProcessor;
   private final int fieldLevel;

   public NetSuiteRequestRecordFieldsHandler(int var1, NetSuiteRecordRefWrapper var2) {
      this.fieldLevel = var1 + 2;
      this.fieldProcessor = new NetSuiteRequestRecordFieldProcessor(this.fieldLevel, var2);
   }

   public List<String> getNullFieldList() {
      return this.nullfieldList;
   }

   public String getFieldsContent() {
      return this.recordContentBuilder.toString();
   }

   private boolean isFieldLevel(int var1) {
      return var1 == this.fieldLevel;
   }

   private void initField() {
      this.nullValueHandler = null;
      this.fieldProcessor.init();
   }

   private void startElementField(int var1, String var2, String var3, String var4, Attributes var5, Map<String, String> var6) {
      this.initField();
      this.nullValueHandler = new NetSuiteRequestRecordFieldNullValueHandler(var1, false);
   }

   private void endElementField(String var1, String var2, String var3) {
      if (this.nullValueHandler.isNullField()) {
         this.nullfieldList.add(var2);
      } else {
         this.recordContentBuilder.append(this.fieldProcessor.getFieldXml());
      }

      this.initField();
   }

   private StringBuilder getStringBuilder(boolean var1) {
      return var1 ? this.recordContentBuilder : this.fieldProcessor.fieldBuilder;
   }

   public void startElementImpl(int var1, String var2, String var3, String var4, Attributes var5, Map<String, String> var6) {
      if (this.isInsideCustomFieldList) {
         this.customFieldHandler.startElement(var1, var2, var3, var4, var5, var6);
      } else if (isCustomFieldListElement(var3)) {
         this.isInsideCustomFieldList = true;
         boolean var7 = this.isFieldLevel(var1);
         this.customFieldHandler = new NetSuiteRequestRecordCustomFieldsHandler(var1 + 1, var7, this.nullfieldList);
         XmlElementUtils.startElementTag(this.getStringBuilder(var7), var4, var5, var6);
      } else {
         if (this.isFieldLevel(var1)) {
            this.startElementField(var1, var2, var3, var4, var5, var6);
         }

         this.nullValueHandler.startElement(var1, var2, var3, var4, var5);
         this.fieldProcessor.startElementImpl(var1, var2, var3, var4, var5, var6);
      }
   }

   public void endElementImpl(int var1, String var2, String var3, String var4) {
      if (this.isInsideCustomFieldList) {
         if (isCustomFieldListElement(var3)) {
            boolean var5 = this.isFieldLevel(var1);
            StringBuilder var6 = this.getStringBuilder(var5);
            var6.append(this.customFieldHandler.getCustomFieldsContent());
            XmlElementUtils.endElementTag(var6, var4);
            this.isInsideCustomFieldList = false;
            this.customFieldHandler = null;
         } else {
            this.customFieldHandler.endElement(var1, var2, var3, var4);
         }
      } else {
         if (this.nullValueHandler != null) {
            this.nullValueHandler.endElement(this.fieldProcessor.isShortForm(), var1, var2, var3, var4);
         }

         this.fieldProcessor.endElementImpl(var1, var2, var3, var4);
         if (this.isFieldLevel(var1)) {
            this.endElementField(var2, var3, var4);
         }
      }
   }

   public void characters(char[] var1, int var2, int var3) {
      if (this.isInsideCustomFieldList) {
         this.customFieldHandler.characters(var1, var2, var3);
      } else {
         this.fieldProcessor.characters(var1, var2, var3);
      }
   }

   private static boolean isCustomFieldListElement(String var0) {
      return var0.equals("customFieldList");
   }
}
