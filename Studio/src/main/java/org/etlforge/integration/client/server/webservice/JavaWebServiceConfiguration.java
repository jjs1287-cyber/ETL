package org.etlforge.integration.client.server.webservice;

import java.rmi.RemoteException;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.ServerPorts;

public final class JavaWebServiceConfiguration extends WebServiceConfiguration {
   public JavaWebServiceConfiguration(ServerManager var1, ServerPorts var2, WebServiceConfigurationCallback var3) {
      super("/axis/services/", "ETLForge Server", var1, var2, var3);
   }

   @Override
   protected String getServerErrorMessage(RemoteException var1) {
      String var2 = super.getServerErrorMessage(var1);
      String var3 = "java.rmi.RemoteException: ";
      return var2.contains(var3) ? StringUtils.substringAfter(var2, var3) : var2;
   }

   public String replaceJavaPort(String var1) {
      return var1;
   }
}
