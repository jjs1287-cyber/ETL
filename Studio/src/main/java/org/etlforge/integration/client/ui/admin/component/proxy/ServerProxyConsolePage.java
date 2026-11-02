package org.etlforge.integration.client.ui.admin.component.proxy;

import com.google.common.base.Preconditions;
import com.etlforge.integration.client.proxy.ui.ProxySettingsEditor;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.application.ui.window.action.SaveEditorAction;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.ui.admin.core.console.AbstractConsolePage;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.system.Disposable;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;

public final class ServerProxyConsolePage extends AbstractConsolePage {
   private final ProxySettingsEditor editor;
   private final ServerProxyConsolePage.ChangeHandler changeHandler;
   private final NonAdminMessage nonAdminMessage;
   private final Action saveAction = new SaveEditorAction(this);

   public ServerProxyConsolePage(ProxySettingsEditor var1) {
      super("Admin.Console.Proxy", "Server Proxy");
      this.editor = Preconditions.checkNotNull(var1, "editor");
      this.changeHandler = new ServerProxyConsolePage.ChangeHandler(var1);
      this.nonAdminMessage = new NonAdminMessage(this.getOverlayContainer());
      this.setIcon(ClientIcons.PROXY_16);
      this.setMementoPath(new String[]{"Admin", "ServerProxy"});
      this.useDefaultCategory();
      this.setContributeGlobalUndoRedo();
      var1.setWaitService(this.getWaitService());
      this.addResource(new DisposableWindowElementResource(this.changeHandler.install()));
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.layoutPage();
      this.checkAdminUser();
      this.editor.reset();
   }

   private void layoutPage() {
      this.addToolbarButton(KongaButton.forToolbar(this.saveAction).withSmallActionIcon());
      this.createTitlePanel();
      this.setToolBar(this.createToolBar());
      OneColumnPanel var1 = Vertical.column()
         .stretchHorizontally()
         .withInsets(0, 0, 8, 0)
         .of(
            WizardStyleSheet.caption("Server Proxy Settings"),
            WizardStyleSheet.smallCaption("Define the proxy settings for the ETLForge server's communication with the outside world.")
         );
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 8).north(var1).center(this.layoutEditor());
      this.setMainPanel(new BorderLayoutBuilder().north(var2).deeplyNonOpaque().container());
   }

   private JComponent layoutEditor() {
      this.editor.setSaveAction(this.saveAction);
      return new TwoColumnPanel()
         .withLeftFill(GridBagLayoutPanel.Fill.HORIZONTAL)
         .withRightFill(GridBagLayoutPanel.Fill.HORIZONTAL)
         .withLeftWeights(0.25, 0.0)
         .withRightWeights(0.75, 0.0)
         .addRow(this.editor, new KongaLabel(" ").asUiProvider())
         .container();
   }

   private void checkAdminUser() {
      if (this.isLoggedInAsAdmin()) {
         this.editor.setEnabled(true);
         this.nonAdminMessage.hide();
      } else {
         this.editor.setEnabled(false);
         if (this.isConnected()) {
            ServerInfo var1 = this.getCurrentServer();
            this.nonAdminMessage.show(var1);
         } else {
            this.nonAdminMessage.hide();
         }
      }
   }

   @Override
   public void requestFocus() {
      this.editor.requestFocus();
   }

   @Override
   protected void serverConnectionChanged(ServerConnectionEvent var1) {
      if (var1.isConnected()) {
         this.editor.reset();
      } else {
         this.clearForm();
      }

      this.checkAdminUser();
   }

   private void clearForm() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ServerProxyConsolePage.this.editor.clear();
            ServerProxyConsolePage.this.setClean();
         }
      });
   }

   @Override
   public void save() throws SaveEditorException {
      this.editor.save();
   }

   private class ChangeHandler implements InputChangeListener, PropertyChangeListener {
      private final ProxySettingsEditor editor;
      private boolean firstPageLoad = true;

      public ChangeHandler(ProxySettingsEditor var2) {
         this.editor = var2;
      }

      public Disposable install() {
         this.editor.addInputChangeListener(this);
         this.editor.addUndoableEditListener(ServerProxyConsolePage.this);
         this.editor.getModel().addPropertyChangeListener("px", this);
         return new Disposable() {
            @Override
            public void dispose() {
               ChangeHandler.this.editor.removeInputChangeListener(ChangeHandler.this);
               ChangeHandler.this.editor.removeUndoableEditListener(ServerProxyConsolePage.this);
               ChangeHandler.this.editor.getModel().removePropertyChangeListener("px", ChangeHandler.this);
            }
         };
      }

      @Override
      public void inputChanged(InputChangeEvent var1) {
         ServerProxyConsolePage.this.setDirty();
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ServerProxyConsolePage.this.setClean();
               if (ChangeHandler.this.firstPageLoad) {
                  ServerProxyConsolePage.this.getUndoService().discardAllEdits();
                  ChangeHandler.this.firstPageLoad = false;
               }
            }
         });
      }
   }
}
