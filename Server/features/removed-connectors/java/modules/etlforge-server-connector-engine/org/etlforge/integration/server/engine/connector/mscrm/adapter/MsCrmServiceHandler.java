package org.etlforge.integration.server.engine.connector.mscrm.adapter;

import java.io.IOException;
import org.etlforge.integration.server.engine.connector.util.ConnectorServiceHandler;
import org.etlforge.integration.server.engine.connector.util.XmlWriter;
import org.etlforge.util.net.ProxySettings;

public class MsCrmServiceHandler extends ConnectorServiceHandler {
   MsCrmServiceHandler(String var1, String var2, String var3) {
      super(ProxySettings.NONE, var2 + var1, var3, "ETLForge Dynamics CRM 2011 REST");
   }

   @Override
   protected void buildRequestBodyFile(XmlWriter var1, String var2) throws IOException {
      var1.appendFileUtf8(var2);
   }
}
