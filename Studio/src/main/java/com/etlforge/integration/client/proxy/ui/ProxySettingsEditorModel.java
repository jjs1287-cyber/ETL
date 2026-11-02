package com.etlforge.integration.client.proxy.ui;

import com.google.common.base.Preconditions;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;
import org.etlforge.util.net.ProxySettings;

public final class ProxySettingsEditorModel extends AbstractPropertyBasedModel {
   public static final String PROXY = "px";
   public static final String VALID = "valid";
   public static final String BUSY = "busy";
   private final Property<ProxySettings> proxySettings = this.newProperty("px", ProxySettings.NONE);
   private final Property<Boolean> valid = this.newProperty("valid", Boolean.FALSE);
   private final Property<Boolean> busy = this.newProperty("busy", Boolean.FALSE);

   public void setProxySettings(ProxySettings var1) {
      this.proxySettings.set(Preconditions.checkNotNull(var1, "px"));
   }

   public ProxySettings getProxySettings() {
      ProxySettings var1 = this.proxySettings.get();
      return var1 == null ? ProxySettings.NONE : var1;
   }

   public void setValid(boolean var1) {
      this.valid.set(var1);
   }

   public void setBusy(boolean var1) {
      this.busy.set(var1);
   }
}
