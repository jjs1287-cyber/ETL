package com.etlforge.integration.client.proxy.serverapi;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.util.net.ProxySettings;

public final class MockProxySettingsApi implements ProxySettingsApi {
   private volatile ProxySettings settings = ProxySettings.NONE;

   @Override
   public void getProxySettings(GetProxySettingsCallback var1) {
      this.sleep(var1);
      var1.gotProxySettings(Optional.absent(), this.settings);
   }

   private boolean sleep(ServerCallback var1) {
      try {
         TimeUnit.SECONDS.sleep(1L);
         return true;
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
         var1.cancelled();
         return false;
      }
   }

   @Override
   public void setProxySettings(ProxySettings var1, SetProxySettingsCallback var2) {
      this.sleep(var2);
      this.settings = Preconditions.checkNotNull(var1);
      var2.done();
   }
}
