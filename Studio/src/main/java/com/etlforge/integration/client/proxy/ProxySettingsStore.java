package com.etlforge.integration.client.proxy;

import com.google.common.base.Supplier;
import org.etlforge.util.net.ProxySettings;

public interface ProxySettingsStore extends Supplier<ProxySettings> {
   ProxySettings get();

   boolean put(ProxySettings var1);
}
