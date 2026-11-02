package org.etlforge.integration.server.engine.connector.netsuite.process;

import java.util.Map;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.xml.sax.Attributes;

public class NetSuiteRequestRecordFieldProcessor {
   StringBuilder fieldBuilder = new StringBuilder();
   private StringBuilder elementBuilder = new StringBuilder();
   private StringBuilder valueBuilder = null;
   private boolean noAttribute;
   private boolean isShortForm;
   private final int fieldLevel;
   private final NetSuiteRecordRefWrapper recordRef;
   private String fieldQName;
   private String customizationRefQName;

   NetSuiteRequestRecordFieldProcessor(int var1, NetSuiteRecordRefWrapper var2) {
      this.fieldLevel = var1;
      this.recordRef = var2;
   }

   private boolean isCustomizationRef(int var1, String var2, String var3, String var4) {
      if (var1 != this.fieldLevel + 1) {
         return false;
      }

      if (!this.recordRef.isCustomizationRef(var2, var3)) {
         return false;
      }

      this.customizationRefQName = var4;
      return true;
   }

   public void startElementImpl(int var1, String var2, String var3, String var4, Attributes var5, Map<String, String> var6) {
      if (this.fieldLevel == var1) {
         this.fieldQName = var4;
      }

      if (this.isCustomizationRef(var1, var2, var3, var4)) {
         this.fieldBuilder = new StringBuilder();
         this.elementBuilder = new StringBuilder();
         Map var7 = XmlElementUtils.toMap(var5);
         var7.put("xsi:type", this.customizationRefQName);
         XmlElementUtils.startElementTagAttributeMap(this.fieldBuilder, this.fieldQName, var7, var6);
      } else {
         this.startElementTag(var4, var5, var6);
      }
   }

   public void endElementImpl(int var1, String var2, String var3, String var4) {
      boolean var5 = var1 == this.fieldLevel + 1 && var4.equals(this.customizationRefQName);
      if (var5) {
         this.customizationRefQName = null;
      } else {
         this.endElementTag(var4);
      }
   }

   public void characters(char[] var1, int var2, int var3) {
      if (this.valueBuilder != null) {
         this.valueBuilder.append(var1, var2, var3);
      }

      this.isShortForm = false;
   }

   void init() {
      this.fieldBuilder = new StringBuilder();
      this.elementBuilder = new StringBuilder();
      this.fieldQName = null;
      this.customizationRefQName = null;
   }

   private void appendElementValue() {
      if (this.elementBuilder.length() > 0) {
         this.fieldBuilder.append(this.elementBuilder);
         this.elementBuilder = new StringBuilder();
      }
   }

   void startElementTagAttributeMap(String var1, Map<String, String> var2) {
      this.startElementTag(false);
      XmlElementUtils.startElementTagAttributeMap(this.elementBuilder, var1, var2, null);
   }

   void startElementTag(String var1, Attributes var2) {
      this.startElementTag(var2.getLength() == 0);
      XmlElementUtils.startElementTag(this.elementBuilder, var1, var2);
   }

   private void startElementTag(String var1, Attributes var2, Map<String, String> var3) {
      this.startElementTag(var2.getLength() == 0);
      XmlElementUtils.startElementTag(this.elementBuilder, var1, var2, var3);
   }

   private void startElementTag(boolean var1) {
      this.appendElementValue();
      this.noAttribute = var1;
      this.isShortForm = true;
      this.valueBuilder = new StringBuilder();
   }

   void endElementTag(String var1) {
      if (this.isShortForm()) {
         XmlElementUtils.endElementTagShort(this.elementBuilder, var1);
      } else {
         if (this.valueBuilder != null) {
            XmlElementUtils.appendXmlValue(this.elementBuilder, this.valueBuilder.toString());
         }

         XmlElementUtils.endElementTag(this.elementBuilder, var1);
      }

      if (this.isShortForm() && this.noAttribute) {
         this.elementBuilder = new StringBuilder();
      } else {
         this.appendElementValue();
      }

      this.isShortForm = false;
      this.valueBuilder = null;
   }

   public boolean isShortForm() {
      return this.isShortForm;
   }

   String getFieldXml() {
      return this.fieldBuilder.toString();
   }
}
