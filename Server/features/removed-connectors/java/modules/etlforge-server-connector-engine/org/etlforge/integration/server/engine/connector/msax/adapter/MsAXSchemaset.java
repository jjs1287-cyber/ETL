package org.etlforge.integration.server.engine.connector.msax.adapter;

import com.google.common.collect.Maps;
import java.util.Map;

public class MsAXSchemaset {
   private String rootName = "";
   private String rootNamespace = "";
   private String rootFilename = "";
   private final Map<String, String> schemas = Maps.newHashMap();

   public String getRootName() {
      return this.rootName;
   }

   public void setRootName(String var1) {
      this.rootName = var1;
   }

   public String getRootNamespace() {
      return this.rootNamespace;
   }

   public void setRootNamespace(String var1) {
      this.rootNamespace = var1;
   }

   public void addSchemaContent(String var1, String var2) {
      if (this.getSchemas().size() == 0) {
         this.setRootFilename(var1);
      }

      this.getSchemas().put(var1, var2);
   }

   public String getRootFilename() {
      return this.rootFilename;
   }

   public void setRootFilename(String var1) {
      this.rootFilename = var1;
   }

   public Map<String, String> getSchemas() {
      return this.schemas;
   }
}
