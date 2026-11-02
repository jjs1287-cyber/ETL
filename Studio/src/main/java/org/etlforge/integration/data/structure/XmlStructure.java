package org.etlforge.integration.data.structure;

import java.io.File;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.location.DataLocation;

public final class XmlStructure extends DataStructure {
   public static final String DTD_FILEPATH = "XmlStructure#DtdFilePath";
   public static final String ROOT_NODE = "XmlStructure#RootNode";
   public static final String TARGET_DTD_URL = "XmlStructure#TargetDtdUrl";
   public static final String PROPERTY_XML = "_xml";
   public static final String PROPERTY_DTD_ROOT = "dtd_root";
   public static final String PROPERTY_TARGET_DTD_URL = "target_dtd_url";
   public static final String DO_XML_VALIDATION = "DoXmlValidation";
   public static final String SCHEMA_LOCATION = "SchemaLocation";
   public static final String JBXML_PARSER = "JbXmlParser";

   public XmlStructure() {
      super(DataStructureType.XML);
   }

   public XmlStructure(XmlStructure var1) {
      this();
      StringKeyValuePairs var2 = var1.getCopyOfKeyValues();

      for (String var4 : var2.getKeys()) {
         this.setProperty(var4, var2.get(var4));
      }
   }

   @Override
   protected void setProperty(String var1, String var2) {
      super.setProperty(var1, var2);
      if (var1.equals("source_xml") || var1.equals("target_xml")) {
         this.setFilePath(var2);
      } else if (var1.equals("sourcedtd_root") || var1.equals("targetdtd_root")) {
         this.setRootNodeName(var2);
      } else if (var1.equals("target_dtd_url")) {
         this.setTargetDtdUrl(var2);
      } else if (var1.equals("use_jbxmlparser")) {
         this.setProperty("JbXmlParser", var2);
      }
   }

   @Override
   public String getName() {
      String var1 = this.getFilePath();
      return var1 != null ? new File(var1).getName() : super.getName();
   }

   public String getFilePath() {
      return this.getProperty("XmlStructure#DtdFilePath");
   }

   public void setFilePath(String var1) {
      this.setStringProperty("XmlStructure#DtdFilePath", var1);
   }

   public String getRootNodeName() {
      return this.getProperty("XmlStructure#RootNode");
   }

   public void setRootNodeName(String var1) {
      this.setStringProperty("XmlStructure#RootNode", var1);
   }

   public String getTargetDtdUrl() {
      return this.getProperty("XmlStructure#TargetDtdUrl");
   }

   public void setTargetDtdUrl(String var1) {
      this.setStringProperty("XmlStructure#TargetDtdUrl", var1);
   }

   @Override
   public StructureNature getStructureNature() {
      return StructureNature.Tree;
   }

   public void setValidationEnabled(boolean var1) {
      this.setBooleanProperty("DoXmlValidation", var1);
   }

   public boolean isValidationEnabled() {
      return this.getBooleanProperty("DoXmlValidation", true);
   }

   public void setSchemaLocation(String var1) {
      this.setStringProperty("SchemaLocation", var1);
   }

   public String getSchemaLocation() {
      return this.getProperty("SchemaLocation", null);
   }

   public boolean isUsingJbXmlParser() {
      return this.getBooleanProperty("JbXmlParser", false);
   }

   public void setUseJbXmlParser(boolean var1) {
      this.setBooleanProperty("JbXmlParser", var1);
   }

   @Override
   public boolean isCompatibleWith(DataStructure var1) {
      if (super.isCompatibleWith(var1)) {
         return true;
      }

      if (var1 == null) {
         return false;
      }

      DataStructureType var2 = var1.getStructureType();
      return var2 == DataStructureType.WebServiceRequest || var2 == DataStructureType.WebServiceResponse;
   }

   @Override
   public boolean isCompatibleWith(DataLocation var1) {
      return var1 != null && var1.isFileBased();
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("File: ");
      var1.append(this.getFilePath());
      var1.append(" %% Root: ");
      var1.append(this.getRootNodeName());
      var1.append(" %% Target URL: ");
      var1.append(this.getTargetDtdUrl());
      return var1.toString();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (var1 instanceof XmlStructure) {
         XmlStructure var2 = (XmlStructure)var1;
         synchronized (this.getDataLock()) {
            return areEqual(this.getFilePath(), var2.getFilePath()) && areEqual(this.getRootNodeName(), var2.getRootNodeName());
         }
      } else {
         return false;
      }
   }

   private static boolean areEqual(Object var0, Object var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }

   @Override
   public int hashCode() {
      synchronized (this.getDataLock()) {
         int var2 = 17;
         String var3 = this.getFilePath();
         var2 = 37 * var2 + (var3 == null ? 0 : var3.hashCode());
         String var4 = this.getRootNodeName();
         return 37 * var2 + (var4 == null ? 0 : var4.hashCode());
      }
   }
}
