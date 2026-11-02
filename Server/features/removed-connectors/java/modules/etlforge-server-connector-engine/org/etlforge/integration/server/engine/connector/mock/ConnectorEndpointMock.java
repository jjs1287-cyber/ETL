package org.etlforge.integration.server.engine.connector.mock;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointAbstract;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpointParameters;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEngine;

public class ConnectorEndpointMock extends ConnectorEndpointAbstract {
   @Override
   public boolean testConnection() {
      return true;
   }

   @Override
   public ConnectorEngine newConnectorEngine() {
      return new ConnectorEngineMock(this);
   }

   private void put(Map<String, String> var1, String var2, String var3) {
      if (!Strings.isNullOrEmpty(var3)) {
         if (Strings.isNullOrEmpty(var2)) {
            var2 = "NoKey";
         }

         var1.put(var2, var3);
      }
   }

   public Map<String, String> toMap() {
      HashMap var1 = Maps.newHashMap();
      ConnectorEndpointParameters var2 = this.getEndpointParameters();
      this.put(var1, "host", var2.getHost());
      this.put(var1, "user", var2.getUser());
      this.put(var1, "password", var2.getPassword());
      Properties var3 = var2.getParameters();

      for (String var5 : var3.stringPropertyNames()) {
         this.put(var1, var5, var3.getProperty(var5));
      }

      return var1;
   }
}
