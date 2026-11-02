package com.etlforge.integration.client.proxy.serverapi;

import com.google.common.base.Optional;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.util.net.ProxySettings;

public interface GetProxySettingsCallback extends ServerCallback {
   void gotProxySettings(Optional<ServerInfo> var1, ProxySettings var2);
}
