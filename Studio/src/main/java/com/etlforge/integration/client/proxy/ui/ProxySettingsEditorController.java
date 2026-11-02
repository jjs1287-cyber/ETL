package com.etlforge.integration.client.proxy.ui;

import com.google.common.base.Optional;
import com.etlforge.integration.client.proxy.serverapi.GetProxySettingsCallback;
import com.etlforge.integration.client.proxy.serverapi.ProxySettingsApi;
import com.etlforge.integration.client.proxy.serverapi.SetProxySettingsCallback;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.server.AbstractServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.util.net.ProxySettings;

public final class ProxySettingsEditorController {
   private final ProxySettingsEditorModel model;
   private final ProxySettingsApi api;

   public ProxySettingsEditorController(ProxySettingsEditorModel var1, ProxySettingsApi var2) {
      this.model = var1;
      this.api = var2;
   }

   public void getSettings() {
      this.model.setBusy(true);
      ProxySettingsEditorController.Getter var1 = new ProxySettingsEditorController.Getter();
      var1.submit();
   }

   public void saveSettings() {
      ProxySettings var1 = this.model.getProxySettings();
      this.model.setBusy(true);
      ProxySettingsEditorController.Setter var2 = new ProxySettingsEditorController.Setter(var1);
      var2.submit();
   }

   private class Getter extends UiJob {
      private final ProxySettingsEditorController.Getter.Callback callback = new ProxySettingsEditorController.Getter.Callback();

      public Getter() {
         super("Getting current proxy settings");
         this.setBlockUntilDone(true);
         this.setUncaughtExceptionHandler(this.callback);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         ProxySettingsEditorController.this.api.getProxySettings(this.callback);
      }

      private class Callback extends AbstractServerCallback implements GetProxySettingsCallback {
         private Callback() {
         }

         @Override
         public void gotProxySettings(Optional<ServerInfo> var1, ProxySettings var2) {
            ProxySettingsEditorController.this.model.setBusy(false);
            ProxySettingsEditorController.this.model.setProxySettings(var2);
         }

         @Override
         public void caught(Throwable var1) {
            ProxySettingsEditorController.this.model.setBusy(false);
            if (!Getter.this.isCancelled()) {
               super.caught(var1);
            }
         }

         @Override
         public void cancelled() {
            ProxySettingsEditorController.this.model.setBusy(false);
         }
      }
   }

   private class Setter extends UiJob {
      private final ProxySettings px;
      private final ProxySettingsEditorController.Setter.Callback callback = new ProxySettingsEditorController.Setter.Callback();

      private Setter(ProxySettings var2) {
         super("Saving new proxy settings");
         this.px = var2;
         this.setBlockUntilDone(true);
         this.setUncaughtExceptionHandler(this.callback);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         ProxySettingsEditorController.this.api.setProxySettings(this.px, this.callback);
      }

      private class Callback extends AbstractServerCallback implements SetProxySettingsCallback {
         private Callback() {
         }

         @Override
         public void done() {
            ProxySettingsEditorController.this.model.setBusy(false);
         }

         @Override
         public void caught(Throwable var1) {
            ProxySettingsEditorController.this.model.setBusy(false);
            if (!Setter.this.isCancelled()) {
               super.caught(var1);
            }
         }

         @Override
         public void cancelled() {
            ProxySettingsEditorController.this.model.setBusy(false);
         }
      }
   }
}
