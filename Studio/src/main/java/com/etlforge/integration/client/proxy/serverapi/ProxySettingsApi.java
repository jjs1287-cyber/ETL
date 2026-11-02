package com.etlforge.integration.client.proxy.serverapi;

import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.util.net.ProxySettings;

public interface ProxySettingsApi extends ServerCall {
   void getProxySettings(GetProxySettingsCallback var1);

   void setProxySettings(ProxySettings var1, SetProxySettingsCallback var2);
}
