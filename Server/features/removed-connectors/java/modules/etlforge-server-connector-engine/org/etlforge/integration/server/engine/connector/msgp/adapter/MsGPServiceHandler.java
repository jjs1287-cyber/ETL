package org.etlforge.integration.server.engine.connector.msgp.adapter;

import java.io.IOException;
import org.etlforge.integration.server.engine.connector.util.ConnectorServiceHandler;
import org.etlforge.integration.server.engine.connector.util.XmlWriter;
import org.etlforge.util.net.ProxySettings;

public class MsGPServiceHandler extends ConnectorServiceHandler {
   final String operation;

   public MsGPServiceHandler(String var1, String var2, String var3) {
      super(ProxySettings.NONE, var2 + var1, var3, "ETLForge Dynamics GP 2010 REST Connector Service");
      this.operation = var1;
   }

   @Override
   protected void buildRequestBodyFile(XmlWriter var1, String var2) throws IOException {
      String var4 = this.operation.toLowerCase();
      var1.appendStartELement(var4).appendStartELement("EConnectDocument");
      boolean var5 = false;
      var1.appendFileUtf8(var2);
      var1.appendCloseELement("EConnectDocument").appendCloseELement(var4);
   }
}
