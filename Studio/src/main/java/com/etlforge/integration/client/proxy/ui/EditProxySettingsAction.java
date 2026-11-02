package com.etlforge.integration.client.proxy.ui;

import com.google.common.base.Optional;
import com.etlforge.integration.client.proxy.serverapi.ProxySettingsApi;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.util.UiUtils;

public final class EditProxySettingsAction extends AbstractAction {
   private final ProxySettingsApi api;

   public EditProxySettingsAction(ProxySettingsApi var1) {
      super("Proxy Settings", ClientIcons.PROXY_16);
      this.api = var1;
      this.putValue("KongaMediumIconKey", ClientIcons.PROXY_24);
      this.putValue("SwingLargeIconKey", ClientIcons.PROXY_32);
   }

   @Override
   public void actionPerformed(final ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            Optional var1x = UiUtils.getComponentSource(var1);
            EditProxySettingsAction.this.editSettings(var1x);
         }
      });
   }

   private void editSettings(Optional<Component> var1) {
      ProxySettingsEditorModel var2 = new ProxySettingsEditorModel();
      ProxySettingsEditorController var3 = new ProxySettingsEditorController(var2, this.api);
      ProxySettingsEditor var4 = new ProxySettingsEditor(var2, var3);
      ProxySettingsEditorModelDialog var5 = new ProxySettingsEditorModelDialog(var4);
      var5.open(var1);
   }
}
