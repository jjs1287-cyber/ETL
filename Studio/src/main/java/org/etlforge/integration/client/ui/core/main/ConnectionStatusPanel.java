package org.etlforge.integration.client.ui.core.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.toolbar.ToolbarMode;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ui.ServerRenderer;
import org.etlforge.integration.client.server.ui.ServerToolTip;
import org.etlforge.integration.client.ui.ConnectionStatusIndicator;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.label.KongaLabel;

public final class ConnectionStatusPanel implements ConnectionStatusIndicator {
   private final JComponent ui;
   private final JLabel serverCaption = ServerRenderer.getServerCaptionLabel(TextStyles.TopPanelCaptionText);
   private final JLabel serverLabel = TextStyles.TopPanelPlainText.makeLabel(PackageResources.ConnectionStatus.NOT_CONNECTED);
   private final JLabel userCaption = getUserCaptionLabel();
   private final JLabel userLabel = TextStyles.TopPanelPlainText.makeLabel(PackageResources.ConnectionStatus.NONE);

   public ConnectionStatusPanel() {
      this.ui = this.layoutComponents();
   }

   @Override
   public void setLoginAction(Action var1) {
      ConnectionStatusPanel.LoginActionTrigger var2 = new ConnectionStatusPanel.LoginActionTrigger(var1);
      this.serverCaption.addMouseListener(var2);
      this.serverLabel.addMouseListener(var2);
      this.userCaption.addMouseListener(var2);
      this.userLabel.addMouseListener(var2);
      this.ui.addMouseListener(var2);
   }

   @Override
   public void serverConnectionChanged(ServerConnectionEvent var1) {
      if (var1.isConnected()) {
         this.setConnected(var1);
      } else {
         this.setDisconnected();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
   }

   public void setConnected(final ServerConnectionEvent var1) {
      final ServerToolTip var2 = new ServerToolTip(var1.getServerInfo());
      Runnable var3 = new Runnable() {
         @Override
         public void run() {
            ConnectionStatusPanel.this.serverCaption.setIcon(ServerRenderer.getIconForConnectionState(var1));
            ConnectionStatusPanel.this.serverLabel.setText(var1.getServerName());
            ConnectionStatusPanel.this.serverLabel.setToolTipText(var2.get());
            ConnectionStatusPanel.this.userLabel.setText(var1.getCredentials().getUser());
         }
      };
      UiUtils.runOnEventThread(var3);
   }

   public void setDisconnected() {
      Runnable var1 = new Runnable() {
         @Override
         public void run() {
            ConnectionStatusPanel.this.serverCaption.setIcon(ClientIcons.SERVER_16);
            ConnectionStatusPanel.this.serverLabel.setText(PackageResources.ConnectionStatus.NOT_CONNECTED);
            ConnectionStatusPanel.this.serverLabel.setToolTipText(null);
            ConnectionStatusPanel.this.userLabel.setText(PackageResources.ConnectionStatus.NONE);
         }
      };
      UiUtils.runOnEventThread(var1);
   }

   private JComponent layoutComponents() {
      UiProvider var1;
      if (ToolbarMode.getConfiguredMode() == ToolbarMode.MINIMAL) {
         TwoColumnPanel var2 = new TwoColumnPanel();
         var2.withInsets(2, 2, 2, 2);
         var2.addRow(this.serverCaption, this.serverLabel).addRow(this.userCaption, this.userLabel);
         var2.setOpaque(false, false);
         var2.setAlignmentX(1.0F).setAlignmentY(0.0F);
         var1 = var2;
      } else {
         BoxBuilder var3 = BoxBuilder.horizontal();
         var3.addAllWithSpace(5, this.userCaption, this.userLabel).rigid(10).addAllWithSpace(5, this.serverCaption, this.serverLabel);
         var1 = var3;
      }

      return new BorderLayoutBuilder().east(var1).nonOpaque().withBorder(Empty.border(2, 2, 2, 5)).container();
   }

   private static JLabel getUserCaptionLabel() {
      KongaLabel var0 = TextStyles.TopPanelCaptionText.makeLabel(ApplicationResources.STRINGS.getString("User") + ":");
      var0.setIcon(ClientIcons.USER_16);
      return var0;
   }

   private static final class LoginActionTrigger extends MouseAdapter {
      private final Action action;

      private LoginActionTrigger(Action var1) {
         this.action = var1;
      }

      @Override
      public void mouseClicked(MouseEvent var1) {
         if (this.action.isEnabled()) {
            ActionUtils.run(this.action, this, "login");
         }
      }
   }
}
