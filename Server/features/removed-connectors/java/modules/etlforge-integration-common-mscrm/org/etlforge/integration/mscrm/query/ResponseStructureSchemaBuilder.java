package org.etlforge.integration.mscrm.query;

import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.mscrm.metadata.MSCrmAttribute;
import org.etlforge.xml.XmlStringBuilder;

final class ResponseStructureSchemaBuilder {
   private final String rootName;
   private final ResponseStructure.Record rootRecord;
   private XmlStringBuilder xml;

   public ResponseStructureSchemaBuilder(String var1, ResponseStructure.Record var2) {
      this.rootName = var1;
      this.rootRecord = var2;
   }

   public String buildSchema() {
      this.xml = startSchema();
      this.startComplexTypeElement(this.rootName);
      this.insertPreamble();
      this.writeRecord(this.rootRecord);
      this.closeComplexTypeElement();
      this.closePreamble();
      this.xml.closeElement();
      return this.xml.getXml();
   }

   private void closeComplexTypeElement() {
      this.xml.closeElement();
      this.xml.closeElement();
      this.xml.closeElement();
   }

   private static XmlStringBuilder startSchema() {
      XmlStringBuilder var0 = new XmlStringBuilder();
      var0.addElement("schema");
      var0.addAttribute("xmlns", "http://www.w3.org/2001/XMLSchema");
      return var0;
   }

   private void startComplexTypeElement(String var1) {
      this.xml.addElement("element");
      this.xml.addAttribute("name", var1);
      this.startComplexTypeWithSequence();
   }

   private void startComplexTypeWithSequence() {
      this.xml.addElement("complexType");
      this.xml.addElement("sequence");
   }

   private void insertPreamble() {
      this.insertSimpleElementDefinition("success", "boolean", false);
      this.insertSimpleElementDefinition("message", "string", false);
      this.startComplexTypeElement("retrieveResponse");
      this.insertSimpleElementDefinition("success", "boolean", false);
      this.insertSimpleElementDefinition("message", "string", false);
      this.startComplexTypeElement("resultSet");
      this.startComplexTypeElement("resultset");
   }

   private void insertSimpleElementDefinition(String var1, String var2, boolean var3) {
      this.xml.addElement("element");
      this.xml.addAttribute("name", var1);
      this.xml.addAttribute("type", var2);
      if (var3) {
         this.xml.addAttribute("minOccurs", "0");
      }

      this.xml.closeElement();
   }

   private void writeRecord(ResponseStructure.Record var1) {
      this.startRecordDefinition(var1);

      for (MSCrmAttribute var3 : var1.getAttributes()) {
         this.writeField("", var3);
      }

      for (ResponseStructure.Record var5 : var1.getChildRecords()) {
         this.writeChildRecordFields(var5);
      }

      this.closeComplexTypeElement();
   }

   private void writeChildRecordFields(ResponseStructure.Record var1) {
      String var2 = var1.getName();

      for (MSCrmAttribute var4 : var1.getAttributes()) {
         this.writeField(var2, var4);
      }

      for (ResponseStructure.Record var6 : var1.getChildRecords()) {
         this.writeChildRecordFields(var6);
      }
   }

   private void startRecordDefinition(ResponseStructure.Record var1) {
      this.xml.addElement("element");
      this.xml.addAttribute("name", "result");
      this.xml.addAttribute("minOccurs", "0");
      this.xml.addAttribute("maxOccurs", "unbounded");
      if (var1.isAllAttributes()) {
         this.startComplexTypeWithAllGroup();
      } else {
         this.startComplexTypeWithSequence();
      }
   }

   private void startComplexTypeWithAllGroup() {
      this.xml.addElement("complexType");
      this.xml.addElement("all");
   }

   private void writeField(String var1, MSCrmAttribute var2) {
      String var3 = var1.isEmpty() ? var2.getName() : var1 + "." + var2.getName();
      ImmutableSet var4 = var2.getType().getQueryResponseAttributes();
      if (var4.isEmpty()) {
         this.insertSimpleElementDefinition(var3, var2.getXsdType(), true);
      } else {
         this.insertSimpleContentElementDefinition(var3, var2.getXsdType(), var4);
      }
   }

   private void insertSimpleContentElementDefinition(String var1, String var2, ImmutableSet<String> var3) {
      assert !var3.isEmpty();
      this.xml.addElement("element");
      this.xml.addAttribute("name", var1);
      this.xml.addElement("complexType");
      this.xml.addElement("simpleContent");
      this.xml.addElement("extension");
      this.xml.addAttribute("base", var2);

      for (String var5 : var3) {
         this.insertAttributeOfTypeString(var5);
      }

      this.xml.closeElement();
      this.xml.closeElement();
      this.xml.closeElement();
      this.xml.closeElement();
   }

   private void insertAttributeOfTypeString(String var1) {
      this.xml.addElement("attribute");
      this.xml.addAttribute("name", var1);
      this.xml.addAttribute("type", "string");
      this.xml.closeElement();
   }

   private void closePreamble() {
      this.closeComplexTypeElement();
      this.closeComplexTypeElement();
      this.closeComplexTypeElement();
   }
}
