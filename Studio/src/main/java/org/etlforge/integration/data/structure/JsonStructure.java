package org.etlforge.integration.data.structure;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.system.ArgCheck;

public final class JsonStructure extends DataStructure {
   public static final String DEFAULT_ROOT = "json";
   public static final String STRUCTURE_FILEPATH = "JsonStructure.File";
   public static final String ROOT_NAME = "JsonStructure.Root";

   public JsonStructure(String var1) {
      this(var1, "json");
   }

   public JsonStructure(JsonStructure var1) {
      this(var1.getFileName(), var1.getRootName());
   }

   public JsonStructure(String var1, String var2) {
      super(DataStructureType.JSON);
      ArgCheck.checkNotEmpty(var1, "fileName");
      ArgCheck.checkNotEmpty(var1, "rootName");
      this.setProperty("JsonStructure.File", var1);
      this.setProperty("JsonStructure.Root", var2);
      this.setName(createNameFromFileName(var1));
   }

   private static String createNameFromFileName(String var0) {
      String var1 = StringUtils.substringAfter(var0.toLowerCase(), "etlforge.json.");
      var1 = StringUtils.substringBeforeLast(var1, ".xsd");
      if (StringUtils.isBlank(var1)) {
         var1 = "JSON";
      }

      return var1;
   }

   public static String[] getPropertyKeys() {
      return new String[]{"JsonStructure.File", "JsonStructure.Root"};
   }

   public String getFileName() {
      return this.getProperty("JsonStructure.File");
   }

   public void setFileName(String var1) {
      ArgCheck.checkNotEmpty(var1, "fileName");
      this.setName(createNameFromFileName(var1));
      this.setStringProperty("JsonStructure.File", var1);
   }

   public String getRootName() {
      return this.getProperty("JsonStructure.Root");
   }

   public void setRootName(String var1) {
      ArgCheck.checkNotEmpty(var1, "rootName");
      this.setStringProperty("JsonStructure.Root", var1);
   }

   @Override
   public StructureNature getStructureNature() {
      return StructureNature.Tree;
   }

   @Override
   public final boolean isCompatibleWith(DataLocation var1) {
      return var1 != null && var1.isFileBased();
   }
}
