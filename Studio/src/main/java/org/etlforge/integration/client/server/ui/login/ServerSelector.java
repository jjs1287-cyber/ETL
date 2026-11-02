package org.etlforge.integration.client.server.ui.login;

import java.awt.Component;
import java.util.List;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ui.ServerToolTip;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.widget.DefaultComponentAction;
import org.etlforge.ui.widget.combobox.KongaComboBox;

final class ServerSelector {
   private final LoginModel model;
   private final KongaComboBox comboBox;

   public ServerSelector(LoginModel var1) {
      this.model = var1;
      this.comboBox = this.createComboBox();
   }

   private KongaComboBox createComboBox() {
      List var1 = this.model.getListOfKnownServerNames();
      KongaComboBox var2 = new KongaComboBox(var1);
      var2.setEditable(true);
      var2.setSelectionReceiver(new Receiver<Object>() {
         @Override
         public void handle(Object var1) {
            String var2x = (String)var1;
            ServerSelector.this.model.setServer(var2x);
         }
      });
      var2.setToolTipText(PackageResources.SERVER_TOOLTIP);
      var2.setRenderer(new ServerSelector.Renderer());
      return var2;
   }

   public JTextField getEditor() {
      return (JTextField)this.comboBox.getEditor().getEditorComponent();
   }

   public void setLabel(JLabel var1) {
      var1.setLabelFor(this.comboBox);
   }

   public JComponent getUi() {
      return this.comboBox;
   }

   public void setDefaultAction(Action var1) {
      DefaultComponentAction.whenInFocusedWindow(this.comboBox, var1);
   }

   private class Renderer extends DefaultListCellRenderer {
      private Renderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         this.applyToolTip((String)var2);
         return this;
      }

      private void applyToolTip(String var1) {
         ServerInfo var2 = ServerSelector.this.model.getServerInfo(var1);
         if (var2 != null) {
            ServerToolTip var3 = new ServerToolTip(var2);
            this.setToolTipText(var3.get());
         } else {
            this.setToolTipText(null);
         }
      }
   }
}
