package org.etlforge.integration.server.engine.connector.framework;

import com.google.common.collect.Maps;
import java.util.Map;

public class ConnectorXsdSchemaFiles {
   private final String requestMainSchemaFilename;
   private final String responseMainSchemaFilename;
   private final String requestRootElementExpandedName;
   private final String responseRootElementExpandedName;
   private final Map<String, String> schemaFileMaps;

   public ConnectorXsdSchemaFiles(String var1, String var2, String var3, String var4) {
      this.requestMainSchemaFilename = "req.xsd";
      this.responseMainSchemaFilename = "res.xsd";
      this.requestRootElementExpandedName = var2;
      this.responseRootElementExpandedName = var4;
      this.schemaFileMaps = Maps.newHashMap();
      this.schemaFileMaps.put(this.requestMainSchemaFilename, var1);
      this.schemaFileMaps.put(this.responseMainSchemaFilename, var3);
   }

   public ConnectorXsdSchemaFiles(String var1, String var2, String var3, String var4, Map<String, String> var5) {
      this.requestMainSchemaFilename = var1;
      this.responseMainSchemaFilename = var3;
      this.requestRootElementExpandedName = var2;
      this.responseRootElementExpandedName = var4;
      this.schemaFileMaps = var5;
   }

   public String getRequestMainSchemaFilename() {
      return this.requestMainSchemaFilename;
   }

   public String getRequestRootElementExpandedName() {
      return this.requestRootElementExpandedName;
   }

   public String getResponseMainSchemaFilename() {
      return this.responseMainSchemaFilename;
   }

   public String getResponseRootElementExpandedName() {
      return this.responseRootElementExpandedName;
   }

   public Map<String, String> getSchemaFileMaps() {
      return this.schemaFileMaps;
   }
}
