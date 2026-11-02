package org.etlforge.integration.server.engine.connector.msax.adapter;

import java.io.IOException;
import org.etlforge.integration.server.engine.connector.util.ConnectorServiceHandler;
import org.etlforge.integration.server.engine.connector.util.XmlWriter;
import org.etlforge.util.net.ProxySettings;

public class MsAXServiceHandler extends ConnectorServiceHandler {
   public MsAXServiceHandler(String var1, String var2) {
      super(ProxySettings.NONE, var1, var2, "ETLForge Dynamics AX 2012 REST");
   }

   @Override
   protected void buildRequestBodyFile(XmlWriter var1, String var2) throws IOException {
      var1.appendFileUtf8(var2);
   }
}
