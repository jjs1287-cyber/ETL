package org.etlforge.integration.server.engine.connector.framework;

import com.google.common.collect.Maps;
import java.util.Map;

public class ConnectorObjectList {
   final boolean isLastList;
   final String objectLabel;
   private final Map<String, String> objectList;

   public ConnectorObjectList(Map<String, String> var1) {
      this(true, "Dummy", var1);
   }

   public ConnectorObjectList(boolean var1, String var2, Map<String, String> var3) {
      this.isLastList = var1;
      this.objectLabel = var2;
      this.objectList = var3;
   }

   public ConnectorObjectList(boolean var1, String var2) {
      this.isLastList = var1;
      this.objectLabel = var2;
      this.objectList = Maps.newHashMap();
   }

   public ConnectorObjectList() {
      this.isLastList = true;
      this.objectLabel = "";
      this.objectList = Maps.newHashMap();
   }

   public Map<String, String> getObjectList() {
      return this.objectList;
   }

   public void addConnectorObject(String var1, String var2) {
      this.objectList.put(var1, var2);
   }
}
