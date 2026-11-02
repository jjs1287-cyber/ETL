package org.etlforge.integration.client.server.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ui.resources.ServerUiResources;
import org.etlforge.integration.client.server.util.ServerComparators;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.decorator.DefaultListDecorRenderer;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.ui.widget.label.KongaLabel;

public final class AvailableServersComboBox implements UiProvider {
   private String caption;
   private JLabel captionLabel;
   private final KongaComboBox comboBox;
   private boolean setCaptionInline;

   public AvailableServersComboBox() {
      this(new ArrayList<>());
   }

   public AvailableServersComboBox(List<KnownServer> var1) {
      this(var1, ServerUiResources.getString("ServerComboBox.Caption"));
   }

   public AvailableServersComboBox(List<KnownServer> var1, String var2) {
      this.caption = var2;
      this.comboBox = this.createCombo();
      this.setKnownServers(var1);
      this.updateComboBoxToolTip();
   }

   private KongaComboBox createCombo() {
      KongaComboBox var1 = new KongaComboBox();
      var1.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXX");
      var1.setRenderer(new DefaultListDecorRenderer());
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            AvailableServersComboBox.this.updateComboBoxToolTip();
         }
      });
      return var1;
   }

   private void updateComboBoxToolTip() {
      Object var1 = this.comboBox.getSelectedItem();
      if (var1 instanceof AvailableServersComboBox.ServerItem) {
         ((AvailableServersComboBox.ServerItem)var1).applyToolTipTo(this.comboBox);
      } else {
         this.comboBox.setToolTipText(null);
      }
   }

   public void setKnownServers(List<KnownServer> var1) {
      Comparator var2 = Collections.reverseOrder(ServerComparators.SORT_BY_LATEST_LOGIN);
      Collections.sort(var1, var2);
      ComboBoxModel var3 = this.createServerModel(var1);
      this.comboBox.setModel(var3);
      this.comboBox.selectFirst();
   }

   private ComboBoxModel createServerModel(List<KnownServer> var1) {
      DefaultComboBoxModel var2 = new DefaultComboBoxModel();

      for (KnownServer var4 : var1) {
         var2.addElement(new AvailableServersComboBox.ServerItem(var4));
      }

      return var2;
   }

   public void addActionListener(ActionListener var1) {
      this.comboBox.addActionListener(var1);
   }

   public ServerInfo getSelectedServer() {
      Object var1 = this.comboBox.getSelectedItem();
      return var1 instanceof AvailableServersComboBox.ServerItem ? ((AvailableServersComboBox.ServerItem)var1).toServerInfo() : null;
   }

   public void setSelectedServer(ServerInfo var1) {
      if (var1 != null) {
         ServerGuid var2 = var1.getGuid();
         int var3 = 0;

         for (int var4 = this.comboBox.getItemCount(); var3 < var4; var3++) {
            Object var5 = this.comboBox.getItemAt(var3);
            if (isMatchingServerItem(var5, var2)) {
               this.comboBox.setSelectedItem(var5);
               return;
            }
         }
      }

      if (this.setCaptionInline) {
         this.comboBox.setSelectedIndex(0);
      } else {
         this.comboBox.setSelectedItem(null);
      }
   }

   private static boolean isMatchingServerItem(Object var0, ServerGuid var1) {
      if (var0 instanceof AvailableServersComboBox.ServerItem) {
         AvailableServersComboBox.ServerItem var2 = (AvailableServersComboBox.ServerItem)var0;
         if (var2.matches(var1)) {
            return true;
         }
      }

      return false;
   }

   public void setEnabled(boolean var1) {
      UiUtils.setEnabled(var1, this.comboBox, this.captionLabel);
   }

   public boolean isEnabled() {
      return this.comboBox.isEnabled();
   }

   public int getServerCount() {
      return this.comboBox.getModel().getSize();
   }

   public void setCaptionInline() {
      if (!this.setCaptionInline) {
         this.caption = ServerUiResources.getString("ServerComboBox.InlineCaption");
         this.setCaptionInline = true;
         this.insertCaptionItem();
      }
   }

   private void insertCaptionItem() {
      DefaultComboBoxModel var1 = (DefaultComboBoxModel)this.comboBox.getModel();
      ListDecorator var2 = new ListDecorator() {
         @Override
         public void decorate(ListDecor var1, boolean var2x) {
            var1.setText(AvailableServersComboBox.this.caption);
            var1.setToolTip(AvailableServersComboBox.this.caption);
            var1.setIcon(null);
         }
      };
      var1.insertElementAt(var2, 0);
      this.comboBox.setSelectedIndex(0);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.vertical().leftAligned();
      if (!this.setCaptionInline) {
         this.captionLabel = this.createCaptionLabel(this.caption);
      }

      var2.addAllWithSpace(3, this.captionLabel, this.comboBox).decorate(var1);
   }

   private JLabel createCaptionLabel(String var1) {
      if (var1 == null) {
         return null;
      }

      KongaLabel var2 = KongaLabel.withMnemonic(var1);
      var2.setLabelFor(this.comboBox);
      return var2;
   }

   @Override
   public void requestFocus() {
      this.comboBox.requestFocusInWindow();
   }

   private static final class ServerItem implements ListDecorator {
      private final ServerInfo serverInfo;
      private final String name;
      private final String tooltip;

      public ServerItem(KnownServer var1) {
         this.serverInfo = var1.getServerInfo();
         this.name = var1.getServerInfo().getName();
         this.tooltip = new ServerToolTip(this.serverInfo).get();
      }

      @Override
      public void decorate(ListDecor var1, boolean var2) {
         var1.setIcon(ClientIcons.APPLICATION_16);
         var1.setText(this.name);
         var1.setToolTip(this.tooltip);
      }

      public boolean matches(ServerGuid var1) {
         return var1.equals(this.serverInfo.getGuid());
      }

      public ServerInfo toServerInfo() {
         return new ServerInfo(this.serverInfo.getGuid(), this.name, this.serverInfo.getVersion());
      }

      public void applyToolTipTo(JComboBox var1) {
         var1.setToolTipText(this.tooltip);
      }
   }
}
